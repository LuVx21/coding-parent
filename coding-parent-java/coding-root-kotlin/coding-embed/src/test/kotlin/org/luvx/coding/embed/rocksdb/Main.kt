package org.luvx.coding.embed.rocksdb

import org.junit.jupiter.api.Test
import org.rocksdb.RocksDB

class main {
    @Test
    fun m1() {
        var client = RocksDB.open(null, "")
        client.put("", "")
    }
}