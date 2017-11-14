package org.spafka.java;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parseTxt {

    public static void main(String[] args) throws IOException {



        final File file = new File("src\\main\\resources\\feedback_201711021922_mchnt_0000.txt");
        System.out.println(file.getName());

        final List<String> lines = FileUtils.readLines(new File("src\\main\\resources\\feedback_201711021922_mchnt_0000.txt"), Charset.forName("utf-8"));

        final String line = lines.get(0);

        Pattern p = Pattern.compile("[^0-9{1,5}]");
        Matcher m = p.matcher(line);
        String result = m.replaceAll("");
        final String[] split = result.split(",");


        System.out.println(spafka.toString(split));

        if (!split[2].equals("0")) {
            lines.stream().skip(2).forEach(x -> {
                final String mchId = x.split(",")[1];
                System.out.println(mchId);
            });
        }


        System.out.println(lines.get(0));
    }

}
