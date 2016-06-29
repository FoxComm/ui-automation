package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.JavascriptExecutor;
import ru.yandex.qatools.allure.annotations.Step;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class ConciseAPI extends Configuration {

    @Step("Click {0}.")
    public void click(SelenideElement element) {
        elementIsVisible(element);
        sleep(250);
        element.click();
    }

    @Step ("Click {1}th button from {0} List of elements.")
    protected void click(List<SelenideElement> listOfElements, int index) {
        SelenideElement element = listOfElements.get(index - 1);
        elementIsVisible(element);
        sleep(250);
        element.click();
    }

    @Step ("Check if {0} element is visible.")
    protected void elementIsVisible(SelenideElement element) {
        element.shouldBe(visible);
    }

    @Step ("Check if {0} element is not visible.")
    protected void elementNotVisible(SelenideElement element) {
        element.shouldNot(visible);
    }

    @Step("Set {0} field value to {1}")
    protected void setFieldVal(SelenideElement element, String value) {
        sleep(250);
        elementIsVisible(element);
        element.setValue(value);
    }

    @Step ("Click {0}.")
    public void jsClick(SelenideElement element) {
        JavascriptExecutor executor = (JavascriptExecutor)getWebDriver();
        executor.executeScript("arguments[0].click();", element);
    }

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

        for (int i = 0; i < 7; i++) {
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

    protected static void printList(List<String> list) {
        for(String code : list) {
            System.out.println("Code: <" + code + ">");
        }
    }

}