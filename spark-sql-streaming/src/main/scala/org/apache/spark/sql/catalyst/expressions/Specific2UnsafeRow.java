package org.apache.spark.sql.catalyst.expressions;

import org.apache.spafka.sql.PeopleDemo;
import org.apache.spark.sql.catalyst.InternalRow;
import org.apache.spark.unsafe.types.UTF8String;


public class Specific2UnsafeRow {

    public SpecificUnsafeProjection generate(Object[] references) {
        return new SpecificUnsafeProjection(references);
    }

    class SpecificUnsafeProjection extends UnsafeProjection {

        private Object[] references;
        private boolean resultIsNull;
        private boolean globalIsNull;
        private org.apache.spark.sql.catalyst.expressions.codegen.BufferHolder[] mutableStateArray2 = new org.apache.spark.sql.catalyst.expressions.codegen.BufferHolder[1];
        private java.lang.String[] mutableStateArray = new java.lang.String[1];
        private org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[] mutableStateArray3 = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter[1];
        private UnsafeRow[] mutableStateArray1 = new UnsafeRow[1];

        public SpecificUnsafeProjection(Object[] references) {
            this.references = references;

            mutableStateArray1[0] = new UnsafeRow(2);
            mutableStateArray2[0] = new org.apache.spark.sql.catalyst.expressions.codegen.BufferHolder(mutableStateArray1[0], 32);
            mutableStateArray3[0] = new org.apache.spark.sql.catalyst.expressions.codegen.UnsafeRowWriter(mutableStateArray2[0], 2);

        }

        public void initialize(int partitionIndex) {

        }

        public UnsafeRow apply(InternalRow i) {
            mutableStateArray2[0].reset();

            mutableStateArray3[0].zeroOutNullBytes();


            UTF8String value4 = StaticInvoke(i);
            if (globalIsNull) {
                mutableStateArray3[0].setNullAt(0);
            } else {
                mutableStateArray3[0].write(0, value4);
            }


            boolean isNull6 = i.isNullAt(0);
            PeopleDemo.Person value7 = isNull6 ?
                    null : ((PeopleDemo.Person) i.get(0, null));

            if (isNull6) {
                throw new NullPointerException(((java.lang.String) references[1]));
            }
            boolean isNull4 = true;
            int value5 = -1;
            if (!false) {

                isNull4 = false;
                if (!isNull4) {
                    value5 = value7.age();
                }
            }
            if (isNull4) {
                mutableStateArray3[0].setNullAt(1);
            } else {
                mutableStateArray3[0].write(1, value5);
            }
            mutableStateArray1[0].setTotalSize(mutableStateArray2[0].totalSize());
            return mutableStateArray1[0];
        }


        private UTF8String StaticInvoke(InternalRow i) {
            resultIsNull = false;
            if (!resultIsNull) {

                boolean isNull3 = i.isNullAt(0);
                PeopleDemo.Person value3 = isNull3 ?
                        null : ((PeopleDemo.Person) i.get(0, null));

                if (isNull3) {
                    throw new NullPointerException(((java.lang.String) references[0]));
                }
                boolean isNull1 = true;
                java.lang.String value1 = null;
                if (!false) {

                    isNull1 = false;
                    if (!isNull1) {

                        Object funcResult = null;
                        funcResult = value3.name();

                        if (funcResult != null) {
                            value1 = (java.lang.String) funcResult;
                        } else {
                            isNull1 = true;
                        }


                    }
                }
                resultIsNull = isNull1;
                mutableStateArray[0] = value1;
            }

            boolean isNull = resultIsNull;
            UTF8String value = null;
            if (!resultIsNull) {
                value = org.apache.spark.unsafe.types.UTF8String.fromString(mutableStateArray[0]);
            }
            globalIsNull = isNull;
            return value;
        }

    }

    public static void main(String[] args) {
        Specific2UnsafeRow generatedClass = new Specific2UnsafeRow();
        SpecificUnsafeProjection x = generatedClass.generate(null);


        PeopleDemo.Person spafka = new PeopleDemo.Person("spafka", 1);
        Object[] p=   new Object[1];
        p[0]=spafka;
        UnsafeRow apply = x.apply(new GenericInternalRow(p));
        System.out.println(apply);

    }

}
