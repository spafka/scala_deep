package org.spafka.nio;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

import org.junit.Test;

import static java.util.stream.Collectors.toList;

public class TestByteBuffer {
    @Test
    public void testRead() {
        ByteBuffer input = ByteBuffer.allocate(1024);
        input.put("111111".getBytes());
        System.out.println(input.position());
        input.flip();


    }

    @Test
    public void testLambda() {

        List<String> collect = IntStream.rangeClosed(1, 10).mapToObj(x -> {
            if (x % 2 == 0) {
                return x + "";
            } else {
                return null;
            }
        }).filter(y -> y != null).collect(toList());
        System.out.println(collect);
    }


    @Test
    public void testArray() {
        String[] strings = {"1", "2"};
        List<String> lString = Arrays.asList(strings);
        // lString.add("3");  底层还是Array，只不过暴露了一些List的方法
        System.out.println(lString.get(1)); // 这里底层调用的还是 a[index]
        System.out.println(lString);
    }


    @Test
    public void testIetratorRemove(){

        // ConcurrentModificationException
        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");


        for (String s : strings) {
            if ("3".equals(s)){
                strings.remove(s);
            }
        }

        System.out.println(strings);

    }


    @Test
    public void testLongadder(){


        LongAdder adder = new LongAdder();
        adder.add(1);
        adder.add(2);
        // fixme
        System.out.println(adder.longValue());
    }

}
