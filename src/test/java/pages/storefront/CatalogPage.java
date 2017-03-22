package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;
import static org.openqa.selenium.By.xpath;

public class CatalogPage extends ProfilePage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

    public SelenideElement productTitle_catalog(String title) {
        return $(xpath("//h1/a[text()='" + title + "']"));
    }

    private SelenideElement addToCartBtn_catalog(String productTitle) {
        return $(xpath("//h1[@alt='" + productTitle + "']/following-sibling::div//button"));
    }

    public SelenideElement productPrice_catalog(String productTitle) {
        return $(xpath("//h1[@alt='" + productTitle + "']/following-sibling::div//div[contains(@class, 'price')]/span"));
    }

    public SelenideElement productImage(String productTitle) {
        return $(xpath("//h1[@alt='" + productTitle + "']/../..//img"));
    }

    public SelenideElement imagePlaceholder(String productTitle) {
        return $(xpath("//h1[@alt='" + productTitle + "']/../..//div[contains(@class, 'image-placeholder')]/span"));
    }

    public SelenideElement additionalDescription(String productTitle) {
        return $(xpath("//h1[@alt='" + productTitle + "']/../..//h2[contains(@class, 'additional-description')]"));
    }

    //---------------------------------------------- STEPS --------------------------------------------

    @Step("Click \"Add To Cart\" btn at catalog view")
    public void clickAddToCartBtn_catalog(String productTitle) {
        click(addToCartBtn_catalog(productTitle));
    }

    @Step("Open PDP: <{0}>")
    public void openPDP(String productName) {
        try {
            click(productTitle_catalog(productName));
        } catch (RuntimeException ignored) {
            refresh();
            click(productTitle_catalog(productName));
        }
    }

}