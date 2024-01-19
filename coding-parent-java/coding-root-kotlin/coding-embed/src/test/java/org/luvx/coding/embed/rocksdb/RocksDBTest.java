package org.luvx.coding.embed.rocksdb;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.rocksdb.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class RocksDBTest {
    private static RocksDB client = RocksDBs.ROCKSDB_SUPPLIER.get();

    @Test
    void insert() throws RocksDBException {
        byte[] key = "Hello".getBytes(), value = "World".getBytes();
        client.put(key, value);
        byte[] getValue = client.get(key);
        System.out.println(new String(getValue));

        byte[] key1 = "foo".getBytes();
        client.put(key1, "bar".getBytes());
        List<byte[]> bytes = client.multiGetAsList(List.of(key, key1));
        for (byte[] b : bytes) {
            System.out.println(new String(b));
        }
        walk();
    }

    @Test
    void delete() throws RocksDBException {
        byte[] key = "Hello".getBytes();
        client.delete(key);
        System.out.println(STR."删除:\{new String(key)}");
    }

    @Test
    void walk() {
        System.out.println("--------------------------------");
        RocksIterator iter = client.newIterator();
        for (iter.seekToFirst(); iter.isValid(); iter.next()) {
            System.out.println(STR."\{new String(iter.key())}=\{new String(iter.value())}");
        }
        System.out.println("--------------------------------");
    }

    @Test
    void testCertainColumnFamily() throws RocksDBException {
        String table = "CertainColumnFamilyTest";
        String key = "certainKey";
        String value = "certainValue";

        Options options = new Options().setCreateIfMissing(true);
        List<byte[]> cfs = RocksDB.listColumnFamilies(options, RocksDBs.dbPath);

        List<ColumnFamilyDescriptor> cfdList;
        if (CollectionUtils.isEmpty(cfs)) {
            cfdList = cfs.stream()
                    .map(cf -> new ColumnFamilyDescriptor(cf, new ColumnFamilyOptions()))
                    .collect(Collectors.toList());
        } else {
            cfdList = Lists.newArrayList(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, new ColumnFamilyOptions()));
        }

        DBOptions dbOptions = new DBOptions().setCreateIfMissing(true);
        List<ColumnFamilyHandle> cfhList = Lists.newArrayList();
        client = RocksDB.open(dbOptions, RocksDBs.dbPath, cfdList, cfhList);

        for (int i = 0; i < cfdList.size(); i++) {
            ColumnFamilyDescriptor cfd = cfdList.get(i);
            if (new String(cfd.getName()).equals(table)) {
                client.dropColumnFamily(cfhList.get(i));
            }
        }

        ColumnFamilyHandle columnFamilyHandle = client.createColumnFamily(new ColumnFamilyDescriptor(table.getBytes(), new ColumnFamilyOptions()));
        client.put(columnFamilyHandle, key.getBytes(), value.getBytes());

        byte[] getValue = client.get(columnFamilyHandle, key.getBytes());
        System.out.println("get Value : " + new String(getValue));

        client.put(columnFamilyHandle, "SecondKey".getBytes(), "SecondValue".getBytes());

        List<byte[]> keys = new ArrayList<byte[]>();
        keys.add(key.getBytes());
        keys.add("SecondKey".getBytes());

        List<ColumnFamilyHandle> handleList = new ArrayList<>();
        handleList.add(columnFamilyHandle);
        handleList.add(columnFamilyHandle);

        List<byte[]> bytes = client.multiGetAsList(handleList, keys);
        for (byte[] b : bytes) {
            System.out.println(new String(b));
        }

        client.delete(columnFamilyHandle, key.getBytes());

        RocksIterator iter = client.newIterator(columnFamilyHandle);
        for (iter.seekToFirst(); iter.isValid(); iter.next()) {
            System.out.println(STR."\{new String(iter.key())}:\{new String(iter.value())}");
        }
    }
}