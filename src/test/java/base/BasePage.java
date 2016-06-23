package base;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BasePage extends ConciseAPI {

    public SelenideElement userMenu() {
        return $(By.xpath("//div[@class='_header_header__name']"));
    }

    public SelenideElement logoutButton() {
        return $(By.xpath("//ul[@class='_header_usermenu__usermenu']/li"));
    }


    @Step ("Click {0} button.")
    public void clickBtn(SelenideElement element) {
        elementIsVisible(element);
        sleep(250);
        element.click();
    }

    @Step ("Click {1}th button from {0} list of elements.")
    protected void clickBtn(List<SelenideElement> listOfElements, int index) {
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
    public void elementNotVisible(SelenideElement element) {
        element.shouldNot(visible);
    }

    @Step("Set {0} field value to {1}")
    public void setFieldVal(SelenideElement element, String value) {
        sleep(250);
        elementIsVisible(element);
        element.val(value);
    }

    @Step ("Click {0}.")
    public void jsClick(SelenideElement element) {
        JavascriptExecutor executor = (JavascriptExecutor)getWebDriver();
        executor.executeScript("arguments[0].click();", element);
    }



}
