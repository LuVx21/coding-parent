package org.luvx.coding.embed.rocksdb

import org.rocksdb.RocksDB

fun RocksDB.put(k: String, v: String) {
    this.put(k.toByteArray(), v.toByteArray())
}
