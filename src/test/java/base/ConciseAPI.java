package base;

import com.codeborne.selenide.Configuration;

import java.util.Random;

import static java.lang.Math.random;

public class ConciseAPI extends Configuration {

    protected String addToString(String string1, String string2) {

        Integer intString1 = Integer.valueOf(string1);
        Integer intString2 = Integer.valueOf(string2);
        Integer intResult = intString1 + intString2;
        return String.valueOf(intResult);

    }

    protected static String addToString(String string, int integer) {

        Integer intString1 = Integer.valueOf(string);
        return String.valueOf(intString1 + integer);

    }

    protected String substractFromString(String string1, String string2) {

        Integer intString1 = Integer.valueOf(string1);
        Integer intString2 = Integer.valueOf(string2);
        Integer intResult = intString1 - intString2;
        return String.valueOf(intResult);

    }

    protected String subtractFromString(String string, int integer) {
        Integer intString = Integer.valueOf(string);
        return String.valueOf(intString - integer);
    }

    protected static String generateRandomID() {

        Random rand = new Random();
        String randomId = "";

        for (int i = 0; i < 5; i++) {
            // generates random int between 0 and 9
            int randomNum = rand.nextInt(9 + 1);

            String strRandomNum = String.valueOf(randomNum);
            randomId = randomId.concat(strRandomNum);
        }

        return randomId;

    }


}