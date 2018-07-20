package org.spafka.stream;

import org.junit.Test;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Test2 {

    @Test
    public void testStream() {

        Stream<Integer> sorted =
                Stream.of("1", "3", "3")
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(String s) {
                                return s.equals("3");
                            }
                        }).map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) {
                        return Integer.parseInt(s);
                    }
                }).sorted();


        System.out.println(sorted.collect(toList()));
        System.out.println(sorted.toArray());


    }


}
