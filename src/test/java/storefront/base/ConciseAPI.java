package storefront.base;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.concurrent.TimeUnit;

public abstract class ConciseAPI {

    public abstract WebDriver getWebDriver();

    public void openPage(String url) {
        getWebDriver().get(url);
        getWebDriver().manage().window().maximize();
    }


    //--------------- WAITS ---------------//

    public void waitForElementToBePresent(WebElement element) {
        new WebDriverWait(getWebDriver(), 12).until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForPageToLoad() {
        getWebDriver().manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
    }


    //--------------- ASSERTIONS ---------------//

    public SoftAssert softAssert = new SoftAssert();

    public void assertThat(ExpectedCondition<Boolean> condition) {
        (new WebDriverWait(getWebDriver(), 4)).until(condition);
    }

    public void elementHasText(WebElement element, String text) {
        Assert.assertTrue(element.getText().contains(text));
    }

    public void elementIsPresent(WebElement element) {
        softAssert.assertTrue(element.isDisplayed());
    }


    //--------------- HELPERS ---------------//

    public void setValue(WebElement element, String value) {
        waitForElementToBePresent(element);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
    }

    public void navigateTo(WebElement element) {
        element.click();
    }

}