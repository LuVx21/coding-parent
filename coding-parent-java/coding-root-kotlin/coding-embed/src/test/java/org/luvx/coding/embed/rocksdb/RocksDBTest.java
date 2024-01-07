package org.luvx.coding.embed.rocksdb;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.rocksdb.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.StandardSystemProperty.USER_HOME;

class RocksDBTest {
    private static final String dbPath = STR."\{USER_HOME.value()}/data/RocksDB";

    private static RocksDB client;

    static {
        RocksDB.loadLibrary();

        Options options = new Options()
                .setCreateIfMissing(true);
        try {
            if (!Files.isSymbolicLink(Paths.get(dbPath))) {
                Files.createDirectories(Paths.get(dbPath));
            }
            client = RocksDB.open(options, dbPath);
        } catch (Exception e) {
            throw new RuntimeException("初始化异常");
        }
    }

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
        System.out.println("删除:" + new String(key));
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
        List<byte[]> cfs = RocksDB.listColumnFamilies(options, dbPath);

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
        client = RocksDB.open(dbOptions, dbPath, cfdList, cfhList);

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
            System.out.println(new String(iter.key()) + ":" + new String(iter.value()));
        }
    }
}