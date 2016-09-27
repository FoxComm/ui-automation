package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
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

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.By.xpath;

public class ConciseAPI extends Configuration {

    private String actualValue;     // used in shouldHaveText()
    private int actualSize;         // used in shouldHaveSize()

    //------------------------- ELEMENTS -------------------------//

    protected SelenideElement itemsOnList() {
        return $(xpath("//td[@class='fc-table-td']"));
    }

    protected SelenideElement emptyList() {
        return $(xpath("//div[@class='fc-content-box__empty-row']"));
    }

    private SelenideElement loadingSpinner() {
        return $(xpath("//div[@class='fc-wait-animation fc-wait-animation_size_l']"));
    }

    private String element;

    //------------------------- ACTIONS -------------------------//

    @Step("Open <{0}>")
    public static <PageObjectClass> PageObjectClass openPage(String relativeOrAbsoluteUrl,
                                                         Class<PageObjectClass> pageObjectClassClass) {
        return open(relativeOrAbsoluteUrl, "", "", "", pageObjectClassClass);
    }

    public void click(SelenideElement element) {
        shouldBeVisible(element, "Element is not visible");
        element.click();
    }

    public void click(SelenideElement element, String errorMsg) {
        shouldBeVisible(element, errorMsg);
        element.click();
    }

    protected void click(List<SelenideElement> listOfElements, int index) {
        SelenideElement element = listOfElements.get(index - 1);
        shouldBeVisible(element, "Element is not visible");
        element.click();
    }

    protected void jsClick(SelenideElement element) {
        JavascriptExecutor executor = (JavascriptExecutor)getWebDriver();
        executor.executeScript("arguments[0].click();", element);
    }

    protected void setDdVal(SelenideElement ddElement, String ddValue) {
        click( ddElement, "Dropdown isn't visible" );
        SelenideElement option = $(By.xpath("//li[text()='" + ddValue + "']"));
        click( option, "Option <" + ddValue + "> on dd list isn't visible" );
    }

    protected void setFieldVal(SelenideElement element, String value) {
        shouldBeVisible(element, "Field is not visible");
        element.setValue(value);
    }

    protected void setFieldVal(SelenideElement element, String value, String errorMsg) {
        shouldBeVisible(element, errorMsg);
        element.setValue(value);
    }

    protected void setFieldVal_delayed(SelenideElement element, String value) {
        sleep(250);
        shouldBeVisible(element, "Failed to set field value - it's not visible");
        for(int i = 0; i < value.length(); i++) {
            element.sendKeys(String.valueOf(value.charAt(i)));
        }
    }

    public void clearField(SelenideElement element) {
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
    }

    //------------------------- ASSERTIONS -------------------------//

    public void waitForDataToLoad() {
        shouldNotBeVisible(loadingSpinner(),
                "Data loading either took too long or it was interrupted by an error.");
    }

    protected void shouldBeVisible(SelenideElement element, String errorMsg) {
        try {
            element.shouldBe(visible);
        } catch (ElementNotFound e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    protected void shouldNotBeVisible(SelenideElement element, String errorMsg) {
        try {
            element.shouldNotBe(visible);
        } catch (ElementNotFound e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    protected void shouldBeEnabled(SelenideElement element, String errorMsg) {
        try {
            element.shouldBe(enabled);
        } catch (ElementNotFound e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    protected void shouldNotExist(SelenideElement element, String errorMsg) {
        try {
            element.shouldNot(exist);
        } catch (ElementNotFound e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    protected void shouldHaveValue(SelenideElement element, String expValue, String errorMsg) {
        try {
            actualValue = element.getValue();
            element.shouldHave(attribute("value", expValue));
        // must be different exception
        } catch (ElementNotFound e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg +
                    "\nExpected: [" + expValue + "], Actual: [" + actualValue + "].");
        }
    }

    protected void shouldHaveText(SelenideElement element, String expValue, String errorMsg) {
        try {
            actualValue = element.getText();
            element.shouldHave(text(expValue));
            // must be different exception
        } catch (ElementNotFound e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg +
                    "\nExpected: [" + expValue + "], Actual: [" + actualValue + "].");
        }
    }

    protected void shouldNotHaveText(SelenideElement element, String expValue, String errorMsg) {
        try {
            actualValue = element.getText();
            element.shouldNotHave(text(expValue));
            // must be different exception
        } catch (ElementNotFound e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg +
                    "\nExpected: [" + expValue + "], Actual: [" + actualValue + "].");
        }
    }

    protected void shouldHaveSize(ElementsCollection collection, int expValue, String errorMsg) {
        try {
            actualSize = collection.size();
            collection.shouldHaveSize(expValue);
            // must be different exception
        } catch (ElementNotFound e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg +
                    "\nExpected: [" + expValue + "], Actual: [" + actualSize + "].");
        }
    }

    //------------------------- HELPERS -------------------------//
    //------------ MATH
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

    private static String subtractFromString(String string, int integer) {
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

    //------------ DATES
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