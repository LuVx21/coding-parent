package org.luvx.coding.embed.rocksdb;

import com.github.phantomthief.util.MoreFunctions;
import com.github.phantomthief.util.MoreSuppliers.CloseableSupplier;
import org.luvx.coding.common.OnOffSwitch;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static com.github.phantomthief.util.MoreSuppliers.lazy;
import static com.google.common.base.StandardSystemProperty.USER_HOME;

public class RocksDBs {
    private static final OnOffSwitch deleteLog = OnOffSwitch.of(false);
    public static final  String      dbPath    = STR."\{USER_HOME.value()}/data/RocksDB";

    public static final CloseableSupplier<RocksDB> ROCKSDB_SUPPLIER = lazy(() -> {
        RocksDB.loadLibrary();

        Options options = new Options()
                .setCreateIfMissing(true);
        Path path = Path.of(dbPath);
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            } else if (deleteLog.isOn()) {
                try (Stream<Path> stream = Files.list(path)) {
                    stream.filter(p -> p.getFileName().toString().startsWith("LOG.old."))
                            .forEachOrdered(p -> MoreFunctions.runCatching(() -> Files.deleteIfExists(p)));
                }
            }
            return RocksDB.open(options, dbPath);
        } catch (Exception e) {
            throw new RuntimeException("初始化异常");
        }
    });

    public static void put(String k, String v) throws RocksDBException {
        ROCKSDB_SUPPLIER.get().put(k.getBytes(), v.getBytes());
    }

    public static String get(String k) throws RocksDBException {
        byte[] bytes = ROCKSDB_SUPPLIER.get().get(k.getBytes());
        return new String(bytes);
    }

    public static void delete(String k) throws RocksDBException {
        ROCKSDB_SUPPLIER.get().delete(k.getBytes());
    }
}
