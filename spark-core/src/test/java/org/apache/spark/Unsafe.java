package org.apache.spark;

import org.apache.spark.unsafe.Platform;
import org.apache.spark.unsafe.memory.MemoryAllocator;
import org.apache.spark.unsafe.memory.MemoryBlock;

/***
 * unsafe 获取堆外内存
 * @see http://www.360doc.com/content/18/0301/23/53066208_733552683.shtml
 */
public class Unsafe {


    public static void main(String[] args) {


       Integer arraySizeMax= 1 <<31 -1;
       MemoryBlock allocate = allocate(arraySizeMax);

        long size = allocate.size();
        System.out.println(size);
    }

    public static MemoryBlock allocate(long size) throws OutOfMemoryError {
        long address = Platform.allocateMemory(size);
        MemoryBlock memory = new MemoryBlock(null, address, size);
        if (MemoryAllocator.MEMORY_DEBUG_FILL_ENABLED) {
            memory.fill(MemoryAllocator.MEMORY_DEBUG_FILL_CLEAN_VALUE);
        }
        return memory;
    }
}
