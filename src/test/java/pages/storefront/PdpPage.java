package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;
import static org.openqa.selenium.By.xpath;

public class PdpPage extends ProfilePage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

    public SelenideElement productTitle_catalog(String title) {
        return $(xpath("//h1/a[text()='" + title + "']"));
    }

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

    private SelenideElement addToCartBtn_pdp() {
        return $(xpath("//button[contains(@class, 'add-to-cart-btn')]"));
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

    private SelenideElement additionalInfoBlock(String title) {
        return $(xpath("//div[contains(@class, 'item-title') and text()='" + title + "']"));
    }

    public SelenideElement productTitle_pdp() {
        return $(xpath("//h1[contains(@class, 'title')]"));
    }

    public SelenideElement salePrice() {
        return $(xpath("//div[contains(@class, '_price_')]/span"));
    }

    public SelenideElement customAttribute(String title) {
        return $(xpath("//div[contains(@class, 'attribute-title') and text()='" + title + "']/following-sibling::div[1]"));
    }

    //----------------------- GIFT CARDS

    private SelenideElement priceSelector() {
        return $(xpath("//div[contains(@class, 'price-selector')]"));
    }

    private SelenideElement priceOption(String price) {
        return $(xpath("//div[contains(@class, 'price-selector')]//option[text()='" + price + "']"));
    }

    private SelenideElement recipientNameFld() {
        return $(xpath("//input[@name='giftCard.recipientName']"));
    }

    private SelenideElement recipientEmailFld() {
        return $(xpath("//input[@name='giftCard.recipientEmail']"));
    }

    private SelenideElement giftCardMsgFld() {
        return $(xpath("//textarea[@name='giftCard.message']"));
    }

    private SelenideElement senderNameFld() {
        return $(xpath("//input[@name='giftCard.senderName']"));
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

    //----------------------- GIFT CARDS

    @Step("Set GC price to <{0}>")
    public void setPriceSelector(String price) {
        click(priceSelector());
        click(priceOption(price));
    }

    @Step("Set \"Recipient Name\" fld val to <{0}>")
    public void setRecipientName(String name) {
        setFieldVal(recipientNameFld(), name);
    }

    @Step("Set \"Recipient Name\" fld val to <{0}>")
    public void setRecipientEmail(String email) {
        setFieldVal(recipientEmailFld(), email);
    }

    @Step("Set \"Message\" text area val to <{0}>")
    public void setGiftCardMsg(String msg) {
        setFieldVal(giftCardMsgFld(), msg);
    }

    @Step("Set \"Sender Name\" fld val to <{0}>")
    public void setSenderName(String name) {
        setFieldVal(senderNameFld(), name);
    }

}