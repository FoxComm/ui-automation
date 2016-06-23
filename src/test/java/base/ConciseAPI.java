package base;

import com.codeborne.selenide.Configuration;

import java.text.DecimalFormat;
import java.util.Random;

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

    protected double cutDecimal(double numb) {

        DecimalFormat cutDecimals = new DecimalFormat(("#.##"));
        return Double.valueOf(cutDecimals.format(numb));

    }

    // calculates the amount of funds to be applied to order with SC + GC
    // useful when grand total value is odd
    protected static int calcAmount(int firstAmount, double grandTotal) {
        double firstAmount_double = (double) firstAmount / 100;
        return (int) ((grandTotal - firstAmount_double) * 100);
    }

}