package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Step;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.By.xpath;

public class ConciseAPI extends Configuration {

    public String stageAdmin = "http://admin.stage.foxcommerce.com/";
    public String stageStorefront = "http://stage.foxcommerce.com/";
    public String tgtAdmin = "http://admin.tgt.foxcommerce.com/";
    public String tgtStorefront = "http://tgt.foxcommerce.com/";

    @Step("Click {0}.")
    public void click(SelenideElement element) {
        elementIsVisible(element);
        sleep(250);
        element.click();
    }

    @Step("Click {1}th button from {0} List of elements.")
    protected void click(List<SelenideElement> listOfElements, int index) {
        SelenideElement element = listOfElements.get(index - 1);
        elementIsVisible(element);
        sleep(250);
        element.click();
    }

    @Step("Click {0}.")
    protected void jsClick(SelenideElement element) {
        JavascriptExecutor executor = (JavascriptExecutor)getWebDriver();
        executor.executeScript("arguments[0].click();", element);
    }

    @Step("Select <{1}> option from <{0}> dropdown.")
    protected void setDdVal(SelenideElement ddElement, String ddValue) {
        ddElement.click();
        SelenideElement option = $(By.xpath("//li[text()='" + ddValue + "']"));
        option.click();
    }

    @Step("Check if {0} element is visible.")
    protected void elementIsVisible(SelenideElement element) {
        element.shouldBe(visible);
    }

    @Step("Check if {0} element is not visible.")
    protected void elementNotVisible(SelenideElement element) {
        element.shouldNot(visible);
    }

    @Step("Set {0} field value to {1}")
    protected void setFieldVal(SelenideElement element, String value) {
        sleep(250);
        elementIsVisible(element);
        element.setValue(value);
    }

    @Step("Set {0} field value to {1}")
    protected void setFieldVal_delayed(SelenideElement element, String value) {
        sleep(250);
        elementIsVisible(element);
        for(int i = 0; i < value.length(); i++) {
            element.sendKeys(String.valueOf(value.charAt(i)));
            sleep(50);
        }
    }

    public void clearField(SelenideElement element) {
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
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

    protected static String subtractFromString(String string1, String string2) {

        Integer intString1 = Integer.valueOf(string1);
        Integer intString2 = Integer.valueOf(string2);
        Integer intResult = intString1 - intString2;
        return String.valueOf(intResult);

    }

    protected static String subtractFromString(String string, int integer) {
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

    protected SelenideElement itemsOnList() {
        return $(xpath("//td[@class='fc-table-td']"));
    }

    protected SelenideElement emptyList() {
        return $(xpath("//div[@class='fc-content-box__empty-row']"));
    }

    private SelenideElement loadingSpinner() {
        return $(xpath("//div[@class='fc-wait-animation fc-wait-animation_size_l']"));
    }

    @Step("Wait for data on the list to be loaded.")
    public void waitForDataToLoad() {
//        itemsOnList().should(exist.because("There's no content on the list."));
        sleep(1000);
        loadingSpinner().shouldNotBe(visible
                .because("Data loading either took too long or it was interrupted by an error."));
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    protected static String getTomorrowDate() {
        String today = getDate();
        String day = today.substring(8, 10);
        String tomorrow = addToString(day, 1);
        return today.substring(0, 8) + tomorrow;
    }

    public static String getYesterdayDate() {
        String today = getDate();
        String day = today.substring(8, 10);
        String yesterday = subtractFromString(day, 1);
        return today.substring(0, 8) + yesterday;
    }


    //----------------------------------------- DEBUG -----------------------------------------//

    protected static void printStringList(List<String> list) {
        System.out.println("**** **** **** ");
        System.out.println("Total amount of elements in list: <" + list.size() + ">.");
        for(String code : list) {
            System.out.println("Code: <" + code + ">");
        }
        System.out.println("**** **** **** ");
    }

    protected static void printSEList(List<SelenideElement> list) {
        System.out.println("**** **** **** ");
        System.out.println("Total amount of elements in list: <" + list.size() + ">.");
        for (SelenideElement element : list) {
            try {
                System.out.println("Item name: <" + element.getText() + ">.");
            } catch (IndexOutOfBoundsException e){
                System.out.println("IndexOutOfBoundsException is caught. Exiting printSEList() method.");
                break;
            }
        }
        System.out.println("**** **** **** ");
    }

}