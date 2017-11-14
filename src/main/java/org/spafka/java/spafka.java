package org.spafka.java;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


@Log4j2
public class spafka {

    public static final String charSet = "gbk";

    public static void main(String[] args) throws IOException {



        URL url = spafka.class.getClassLoader().getResource("union/mchnt_0000.txt");
        final List<String> lines = FileUtils.readLines(new File(url.getFile()), Charset.forName(charSet));



        final String line1 = lines.get(0);

        final String[] split = line1.split(",");

        final String s = split[1];
        int batch = 1;

        final List<String> collect = IntStream.rangeClosed(1, 10000).mapToObj(i -> {
            final String[] clone = split.clone();
            clone[1] = String.format("WZPA%04dFS%05d", batch, i);

            final String toString = toString(clone, "", "", ",");
            return toString;
        }).collect(toList());

        final File file = new File("D:\\1.txt");


        try {
            FileUtils.writeLines(file, charSet, collect);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static String toString(Object[] a, String prefix, String endFix, String midFix) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return prefix + endFix;

        StringBuilder b = new StringBuilder();
        b.append(prefix);
        for (int i = 0; ; i++) {
            b.append(String.valueOf(a[i]));
            if (i == iMax)
                return b.append(endFix).toString();
            b.append(midFix);
        }
    }

    public static String toString(Object[] a) {
        return toString(a, "[", "]", ", ");
    }


}