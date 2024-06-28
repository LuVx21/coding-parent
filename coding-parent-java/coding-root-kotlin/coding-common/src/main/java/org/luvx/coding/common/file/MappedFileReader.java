package org.luvx.coding.common.file;

import lombok.Getter;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 读取大文件
 */
public class MappedFileReader implements Closeable {
    private final FileInputStream    fis;
    private final MappedByteBuffer[] mappedBufArray;
    private final int                number;
    private final int                arrayLength;

    private int    count = 0;
    @Getter
    private long   fileLength;
    @Getter
    private byte[] array;

    public MappedFileReader(String fileName, int arrayLength) throws IOException {
        this.fis = new FileInputStream(fileName);
        FileChannel fileChannel = fis.getChannel();

        this.number = (int) Math.ceil((double) fileLength / Integer.MAX_VALUE);
        this.mappedBufArray = new MappedByteBuffer[number];
        long preLength = 0, regionSize = Integer.MAX_VALUE;
        for (int i = 0; i < number; i++) {
            long len = fileLength - preLength;
            if (len < (long) Integer.MAX_VALUE) {
                regionSize = len;
            }
            mappedBufArray[i] = fileChannel.map(FileChannel.MapMode.READ_ONLY, preLength, regionSize);
            preLength += regionSize;
        }

        this.fileLength = fileChannel.size();
        this.arrayLength = arrayLength;
    }

    public int read() throws IOException {
        if (count >= number) {
            return -1;
        }
        MappedByteBuffer buffer = mappedBufArray[count];
        int limit = buffer.limit(), position = buffer.position();
        int len = limit - position;
        if (len > arrayLength) {
            buffer.get(array = new byte[arrayLength]);
            return arrayLength;
        }
        buffer.get(array = new byte[len]);
        if (count < number) {
            count++;
        }
        return len;
    }

    @Override
    public void close() throws IOException {
        fis.close();
        array = null;
    }
}
