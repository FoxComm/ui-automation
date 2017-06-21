package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class PdpPage extends CatalogPage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

    public SelenideElement description_pdp() {
        return $(xpath("//div[contains(@class, '_description_')]"));
    }

    public SelenideElement description_textStyles(String element, String content) {
        if (element.equals("ul") || element.equals("ol")) {
            return $(xpath("//div[contains(@class, 'description')]/" + element + "/li[text()='" + content + "']"));
        } else {
            return $(xpath("//div[contains(@class, 'description')]/" + element + "[text()='" + content + "']"));
        }
    }

    private SelenideElement qty_pdp() {
        return $(xpath("//div[contains(@class, 'cart-actions')]//select"));
    }

    private SelenideElement qtyOption_pdp(String qty) {
        return $(xpath("//div[contains(@class, 'cart-actions')]//select/option[@value='" + qty + "']"));
    }

    public SelenideElement addToCartBtn_pdp() {
        return $(xpath("//button[contains(@class, 'add-to-cart-btn')]"));
    }



    private SelenideElement additionalInfoBlock(String title) {
        return $(xpath("//div[contains(@class, 'item-title') and text()='" + title + "']"));
    }

    public SelenideElement productTitle_pdp() {
        return $(xpath("//h1[contains(@class, 'title')]"));
    }

    public SelenideElement salePrice() {
        return $(xpath("//div[contains(@class, '_price_')]/span"));
    }

    public String customAttributeVal(String propTitle) {
        return $(xpath("//div[contains(@class, 'attribute-title') and text()='" + propTitle + "']")).text();
    }

    //---------------------------------------------- STEPS --------------------------------------------

    @Step("Set product quantity to <{0}>")
    public void setQty_pdp(String qty) {
        click(qty_pdp());
        click(qtyOption_pdp(qty));
    }

    @Step("Click \"Add To Cart\" btn")
    public void clickAddToCartBtn() {
        click(addToCartBtn_pdp());
    }

    @Step("Switch to <{0}> additional info section")
    public void switchInfoBlock(String title) {
        click(additionalInfoBlock(title));
    }

}