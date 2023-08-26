package pro.sky.telegrambot;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Test {

    public static void main(String[] args) {

        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        System.out.println(localDateTime);
    }


}
