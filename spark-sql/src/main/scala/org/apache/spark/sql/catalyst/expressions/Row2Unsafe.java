package org.apache.spark.sql.catalyst.expressions;

import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.unsafe.types.UTF8String;

public class Row2Unsafe {

    public SpecificUnsafeProjection generate(Object[] references) {
        return new SpecificUnsafeProjection(references);
    }

    class SpecificUnsafeProjection extends org.apache.spark.sql.catalyst.expressions.UnsafeProjection {

        private Object[] references;
        private org.apache.spark.sql.catalyst.expressions.codegen.BufferHolder[] mutableStateArray1 = new org.apache.spark.sql.catalyst.expressions.codegen.BufferHolder[1];
        private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[] mutableStateArray2 = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[1];
        private UnsafeRow[] mutableStateArray = new UnsafeRow[1];

        public SpecificUnsafeProjection(Object[] references) {
            this.references = references;
            mutableStateArray[0] = new UnsafeRow(2);
            mutableStateArray1[0] = new org.apache.spark.sql.catalyst.expressions.codegen.BufferHolder(mutableStateArray[0], 32);
            mutableStateArray2[0] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(mutableStateArray1[0], 2);

        }

        public void initialize(int partitionIndex) {

        }


        public UnsafeRow apply(InternalRow i) {
            mutableStateArray1[0].reset();

            mutableStateArray2[0].zeroOutNullBytes();


            boolean isNull = i.isNullAt(0);
            UTF8String value = isNull ?
                    null : (i.getUTF8String(0));
            if (isNull) {
                mutableStateArray2[0].setNullAt(0);
            } else {
                mutableStateArray2[0].write(0, value);
            }


            boolean isNull1 = i.isNullAt(1);
            int value1 = isNull1 ?
                    -1 : (i.getInt(1));
            if (isNull1) {
                mutableStateArray2[0].setNullAt(1);
            } else {
                mutableStateArray2[0].write(1, value1);
            }
            mutableStateArray[0].setTotalSize(mutableStateArray1[0].totalSize());
            return mutableStateArray[0];
        }


    }

    public static void main(String[] args) {
        Row2Unsafe row2Unsafe = new Row2Unsafe();

        SpecificUnsafeProjection specificUnsafeProjection = row2Unsafe.generate(null);

        Object[] row = new Object[2];
        row[0]= UTF8String.fromString("spafka");
        row[1]=1;
        GenericInternalRow genericInternalRow = new GenericInternalRow(row);

        UnsafeRow unsafeRow = specificUnsafeProjection.apply(genericInternalRow);

        System.out.println(unsafeRow);

    }

}
