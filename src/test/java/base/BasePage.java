package base;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class BasePage {

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
    public void clickBtn(List<SelenideElement> listOfElements, int index) {
        SelenideElement element = listOfElements.get(index - 1);
        elementIsVisible(element);
        sleep(250);
        element.click();
    }

    @Step ("Check if {0} element is visible.")
    public void elementIsVisible(SelenideElement element) {
        element.shouldBe(visible);
    }

    @Step ("Check if {0} element is not visible.")
    public void elementNotVisible(SelenideElement element) {
        element.shouldNot(visible);
    }

    @Step("Set {0} field value to {1}")
    public void setFieldValue(SelenideElement element, String value) {
        elementIsVisible(element);
        element.val(value);
    }



}
