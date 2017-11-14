package org.spafka.java;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

@Log4j2
public class nullTest {

    public static void main(String[] args) {


        Long settleDate = 0L;

        final HashMap<String, String> khkf04Map = new HashMap<>();

        try {
            String settleDate1 = khkf04Map.getOrDefault("SettleDate", "0");
            log.info("merchant >> deal settle date >> settleDate1={}", settleDate1);
            if (StringUtils.isEmpty(settleDate1)) {
                settleDate = 0l;
                if ("null".equalsIgnoreCase(khkf04Map.get("SettleDate"))) {
                    settleDate = 0l;
                }
            } else {
                settleDate = Long.valueOf(settleDate);
            }
        }catch (Exception e){
            log.error("settleDate >> error, settleDate={}, error={}", settleDate, e.getMessage());
        }


    }
}
