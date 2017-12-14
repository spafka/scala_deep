package com.twitter.bijection;

import org.junit.Test;
import scala.util.Failure;
import scala.util.Success;
import scala.util.Try;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

/**
 * Bijection is as useful in Java as in Scala, so these tests ensure correct
 * functionality while providing an example of use and implementation from Java.
 */
public class TestBijectionInJava  {


    @Test
    public void javaSer(){


        JavaSerializationInjection<Date> javaSerializationInjection = new JavaSerializationInjection<Date>(Date.class);


        byte[] apply = javaSerializationInjection.apply(new Date());

        Try<Date> invert = javaSerializationInjection.invert(apply);
        System.out.println(invert.get());

    }
}
