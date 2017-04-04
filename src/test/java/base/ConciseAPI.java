package base;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.ex.ElementShould;
import com.codeborne.selenide.ex.ElementShouldNot;
import com.codeborne.selenide.ex.ListSizeMismatch;
import com.google.common.io.Files;
import org.openqa.selenium.*;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.By.xpath;
import static org.testng.Assert.assertEquals;

public class ConciseAPI implements IHookable {

    private String actualValue;     // used in shouldHaveText()
    private int actualSize;         // used in shouldHaveSize()

    //------------------------- ELEMENTS -------------------------//

    protected SelenideElement contentOnList() {
        return $(xpath("//tbody/a"));
    }

    public ElementsCollection itemsOnList() {
        return $$(xpath("//tbody/a"));
    }

    protected SelenideElement emptyList() {
        return $(xpath("//div[@class='fc-content-box__empty-row']"));
    }

    private SelenideElement loadingSpinner() {
        return $(xpath("//div[@class='fc-wait-animation fc-wait-animation_size_l']"));
    }

    //------------------------- ACTIONS -------------------------//

    @Step("Open <{0}>")
    protected static <PageObjectClass> PageObjectClass openPage(String relativeOrAbsoluteUrl,
                                                                Class<PageObjectClass> pageObjectClassClass) {
        relativeOrAbsoluteUrl = relativeOrAbsoluteUrl.replaceAll("(?<!(http:|https:))[//]+", "/");
        return open(relativeOrAbsoluteUrl, "", "", "", pageObjectClassClass);
    }

    public void click(SelenideElement element) {
        shouldBeVisible(element, "Element is not visible: " + element);
        shouldBeEnabled(element, "Element is not enabled: " + element);
        element.click();
    }

    public void click(SelenideElement element, String errorMsg) {
        shouldBeVisible(element, errorMsg);
        shouldBeEnabled(element, "Element is not enabled: " + element);
        element.click();
    }

    protected void click(List<SelenideElement> listOfElements, int index) {
        SelenideElement element = listOfElements.get(index - 1);
        shouldBeVisible(element, "Element is not visible: " + element);
        shouldBeEnabled(element, "Element is not enabled: " + element);
        element.click();
    }

    protected void jsClick(SelenideElement element) {
        JavascriptExecutor jse = (JavascriptExecutor)getWebDriver();
        jse.executeScript("arguments[0].click();", element);
    }

    protected void setDdVal(SelenideElement ddElement, String ddValue) {
        click(ddElement, "Dropdown isn't visible: " + ddElement);
        SelenideElement option = $(By.xpath("//*[text()='" + ddValue + "']"));
        click(option, "Option <" + ddValue + "> on dd list isn't visible");
    }

    protected void setDdVal_li(SelenideElement ddElement, String ddValue) {
        click(ddElement, "Dropdown isn't visible: " + ddElement);
        SelenideElement option = $(By.xpath("//li[text()='" + ddValue + "']"));
        click(option, "Option <" + ddValue + "> on dd list isn't visible");
    }

    protected void setFieldVal(SelenideElement element, String value) {
        shouldBeVisible(element, "Field is not visible: " + element);
        element.setValue(value);
    }

    protected void setFieldVal(SelenideElement element, String value, String errorMsg) {
        shouldBeVisible(element, errorMsg);
        element.setValue(value);
    }

    protected void setFieldValWithSubmit(SelenideElement element, String value) {
        shouldBeVisible(element, "Field is not visible: " + element);
        element.setValue(value).submit();
    }

    protected void setFieldVal_delayed(SelenideElement element, String value) {
        sleep(250);
        shouldBeVisible(element, "Element is not visible: " + element);
        for(int i = 0; i < value.length(); i++) {
            element.sendKeys(String.valueOf(value.charAt(i)));
            sleep(250);
        }
    }

    public void clearField(SelenideElement element) {
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);
    }

    protected void scrollPageUp() {
        JavascriptExecutor jse = (JavascriptExecutor)getWebDriver();
        jse.executeScript("window.scrollTo(0, 0)");
    }

    protected void scrollToElement(SelenideElement element) {
        JavascriptExecutor jse = (JavascriptExecutor)getWebDriver();
        jse.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //------------------------- ASSERTIONS -------------------------//

    protected SoftAssert soft = new SoftAssert();

    @Step("Soft assertEquals Expected: <{1}>, Actual: <{0}>")
    protected void assertEquals_soft(String actual, String expected) {
        soft.assertEquals(actual, expected);
    }

    public void waitForDataToLoad() {
        shouldNotBeVisible(loadingSpinner(),
                "Data loading either took too long or it was interrupted by an error.");
    }

    protected void shouldBeVisible(SelenideElement element, String errorMsg) {
        try {
            element.shouldBe(visible);
        } catch (ElementNotFound | ElementShould | NullPointerException e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    protected void shouldNotBeVisible(SelenideElement element, String errorMsg) {
        try {
            element.shouldNotBe(visible);
        } catch (ElementNotFound | ElementShouldNot | NullPointerException e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    protected void shouldBeEnabled(SelenideElement element, String errorMsg) {
        try {
            element.shouldBe(enabled);
        } catch (ElementNotFound | ElementShould |NullPointerException e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    protected void shouldNotExist(SelenideElement element, String errorMsg) {
        try {
            element.shouldNot(exist);
        } catch (ElementNotFound | ElementShouldNot | NullPointerException e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    protected void shouldHaveValue(SelenideElement element, String expValue, String errorMsg) {
        try {
            actualValue = element.getValue();
            element.shouldHave(attribute("value", expValue));
        } catch (ElementNotFound | ElementShould | NullPointerException e) {
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
        } catch (ElementNotFound | ElementShould | NullPointerException e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg +
                    "\nExpected: [" + expValue + "], Actual: [" + actualValue + "].");
        }
    }

    protected void shouldMatchText(SelenideElement element, String expValue, String errorMsg) {
        try {
            actualValue = element.getText();
            element.should(matchText(expValue));
        } catch (ElementNotFound | ElementShould | NullPointerException e) {
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
        } catch (ElementNotFound | ElementShouldNot | NullPointerException e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg +
                    "\nExpected: [" + expValue + "], Actual: [" + actualValue + "].");
        }
    }

    protected void shouldHaveSize(ElementsCollection elemCollection, int expValue, String errorMsg) {
        try {
            actualSize = elemCollection.size();
            elemCollection.shouldHaveSize(expValue);
            // must be different exception
        } catch (ElementNotFound | ListSizeMismatch | NullPointerException e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg +
                    "\nExpected: [" + expValue + "], Actual: [" + actualSize + "].");
        }
    }

    protected void shouldNotBeEmpty(SelenideElement element, String errorMsg) {
        try {
            element.shouldNotBe(empty);
        } catch (ElementNotFound | ElementShouldNot | NullPointerException e) {
            System.err.println(e.getStackTrace());
            e.printStackTrace();
            throw new RuntimeException(errorMsg);
        }
    }

    @Step("Assert twice that element {1}")
    protected void assertTwice(SelenideElement element, String condition, String errorMsg) {
        switch (condition.toLowerCase()) {

            case "should be visible":
                try {
                    element.shouldBe(visible.because(errorMsg));
                } catch (ElementNotFound | ElementShould | NullPointerException e) {
                    refresh();
                    element.shouldBe(visible.because(errorMsg));
                }
                break;
            case "should not be visible":
                try {
                    element.shouldNotBe(visible.because(errorMsg));
                } catch (ElementNotFound | ElementShouldNot | NullPointerException e) {
                    refresh();
                    element.shouldNotBe(visible.because(errorMsg));
                }
                break;
            case "should be enabled":
                try {
                    element.shouldBe(enabled.because(errorMsg));
                } catch (ElementNotFound | ElementShould | NullPointerException e) {
                    refresh();
                    element.shouldBe(enabled.because(errorMsg));
                }
                break;
            case "should not exist":
                try {
                    element.shouldNot(exist.because(errorMsg));
                } catch (ElementNotFound | ElementShouldNot | NullPointerException e) {
                    refresh();
                    element.shouldNot(exist.because(errorMsg));
                }
                break;
        }
    }

    public void assertTwice(SelenideElement element, String condition, String value, String errorMsg) {
        switch (condition.toLowerCase()) {
            case "should have value":
                try {
                    element.shouldHave(value(value).because(errorMsg));
                } catch (ElementNotFound | ElementShould | NullPointerException e) {
                    refresh();
                    element.shouldHave(value(value).because(errorMsg));
                }
                break;
            case "should have text":
                try {
                    element.shouldHave(text(value).because(errorMsg));
                } catch (ElementNotFound | ElementShould | NullPointerException e) {
                    refresh();
                    element.shouldHave(text(value).because(errorMsg));
                }
                break;
            case "should not have text":
                try {
                    element.shouldNotHave(text(value).because(errorMsg));
                } catch (ElementNotFound | ElementShouldNot | NullPointerException e) {
                    refresh();
                    element.shouldNotHave(text(value).because(errorMsg));
                }
                break;
        }
    }

    public void assertTwice(ElementsCollection elemCollection, int expValue) {
        try {
            elemCollection.shouldHaveSize(expValue);
        } catch (ElementNotFound | ElementShouldNot | NullPointerException ignored) {
            refresh();
            elemCollection.shouldHaveSize(expValue);
        }
    }

    @Step("Assert that current URL is <{1}>")
    protected void assertUrl(String actualUrl, String expectedUrl) {
        String lastChar_actual = actualUrl.substring(actualUrl.length() - 1);
        System.out.println("lastChar_actual: <" + lastChar_actual + ">");
        if(lastChar_actual.equals("/")) {
            actualUrl = actualUrl.substring(0, actualUrl.length() - 1);
        }
        String lastChar_expected = expectedUrl.substring(expectedUrl.length() - 1);
        System.out.println("lastChar_expected: <" + lastChar_expected + ">");
        if(lastChar_expected .equals("/")) {
            expectedUrl = expectedUrl.substring(0, expectedUrl.length() - 1);
        }
        System.out.println("actualUrl: <" + actualUrl + ">");
        System.out.println("expectedUrl: <" + expectedUrl + ">");
        assertEquals(actualUrl, expectedUrl);
    }

    /**
     * Returns true if customer is signed in
     */
    protected Boolean checkCustomerAuth() {
        getWebDriver().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        Boolean result = null;
        try {
            getWebDriver().manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
            getWebDriver().findElement(By.xpath("//a[contains(@class, 'login-link')]"));
        } catch (NoSuchElementException ignored) {
            result = true;
        }
        return result != null;
    }

    /**
     * Webdriver based explicit wait.
     * Checks if element with given xpath is present, timeout = 1 second
     * Use it if you need to check the presence of the element as a condition
     * ONLY to be used inside of try-catch blocks as it will throw NoSuchElementException in case of failure
     * and will fail the test
     */
    protected void elementIsPresent(String xpath) {
        getWebDriver().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        getWebDriver().findElement(By.xpath(xpath));
    }

    //------------------------- HELPERS -------------------------//

    protected static void fullScreen(SelenideElement element) {
        element.sendKeys(Keys.chord(Keys.CONTROL, Keys.RETURN));
    }

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

    //TODO: Use for generating random balance value when issuing multiple GCs
    protected String generateRandomBalanceVal() {
        return String.valueOf(cutDecimal(0.01 + Math.random() * 200.00));
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

    //------------ TEXT
    protected static Boolean findInText(String textToLookAt, String textToMatch) {
        Pattern pattern = Pattern.compile(textToMatch, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textToLookAt);

        return matcher.find();
    }

    protected static String definePromoOfferType(String type) {
        type = type.toLowerCase();
        if (findInText(type, "percent")) {
            return "percent";
        } else if ((findInText(type, "amount")) || findInText(type, "price") || findInText(type, "discounted")) {
            return "currency";
        } else if (findInText(type, "free")) {
            return "free shipping";
        } else {
            return "unknown offer type";
        }
    }

    protected static String totalToString(int total) {
        String strTotal = Integer.toString(total);
        strTotal = new StringBuilder(strTotal).insert(strTotal.length() - 2, ".").toString();
        return strTotal;
    }


    //----------------------------------------- SCREENSHOTS -----------------------------------------//

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        callBack.runTestMethod(testResult);
        if (testResult.getThrowable() != null) {
            try {
                takeScreenShotStep(testResult.getMethod().getMethodName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void takeScreenShotStep(String methodName) throws IOException {
        File lastScreenShot = Screenshots.getLastScreenshot();
        if (lastScreenShot != null) {
            byte[] bytes = Files.toByteArray(lastScreenShot);
            if (isByteArrIsText(bytes))
                takeScreenShot(methodName, bytes);
            else
                errorText(new String(bytes, "UTF-8"));
        } else {
            log("MyScreenShotListener: takeScreenShotStep: new ScreenShot");
            File newScreenShot = Screenshots.takeScreenShotAsFile();

            if (newScreenShot != null) {
                byte[] bytes = Files.toByteArray(newScreenShot);
                if (isByteArrIsText(bytes))
                    takeScreenShot(methodName, bytes);
                else
                    errorText(new String(bytes, "UTF-8"));
            } else {
                log("MyScreenShotListener: takeScreenShotStep: ScreenShot is null, return text attachment");
                errorText("ScreenShot is null");
            }
        }
    }

    private boolean isByteArrIsText(byte[] value) {
        String contentType = null;
        try {
            contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(value));
            if ("image/png".equals(contentType))
                return true;
            else {
                log("contentType is: " + contentType);
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Attachment(value = "Failure in method <{0}>", type = "text/html")
    private String errorText(String text) {
        return text;
    }

    @Attachment(value = "Failure in method <{0}>", type = "image/png")
    private byte[] takeScreenShot(String methodName, byte[] image) throws IOException {
        return image;
    }

    private void log(String text) {
        //nothing
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

    protected static void printIntList(List<Integer> list) {
        System.out.println("**** **** **** ");
        System.out.println("Total amount of elements in list: <" + list.size() + ">.");
        for(int code : list) {
            System.out.println("ID: <" + code + ">");
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