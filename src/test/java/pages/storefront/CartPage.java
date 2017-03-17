package pages.storefront;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class CartPage extends SearchPage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

    public SelenideElement lineItemByName_cart(String productName) {
        return $(xpath("//div[contains(@class, 'product-name') and text()='" + productName + "']"));
    }

    public SelenideElement subTotal_cart() {
        return $(xpath("//div[contains(@class, 'cart-subtotal')]//span"));
    }

    private SelenideElement closeCartBtn() {
        return $(xpath("//span[contains(@class, 'back-icon')]"));
//        return $(xpath("//div[text()='KEEP SHOPPING']"));
    }

    public SelenideElement cartQty() {
        return $(xpath("//sup[contains(@class, 'cart-quantity')]"));
    }

    private SelenideElement cartBtn() {
        return $(xpath("//span[contains(@class, 'fc-cart')]"));
    }

    private SelenideElement removeLineItemBtn_byIndex(String index) {
        return $(xpath("//div[contains(@class, 'line-items')]/div[" + index + "]//span[contains(@class, 'fc-close')]"));
    }

    public SelenideElement checkoutBtn_cart() {
        return $(xpath("//button[contains(@class, 'checkout-button')]"));
    }

    public ElementsCollection lineItemsAmount() {
        return $$(xpath("//div[contains(@class, 'line-items')]/div[contains(@class, 'box')]"));
    }

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

    @Step("Close cart")
    public void closeCart() {
        jsClick(closeCartBtn());
    }

    @Step("Open cart")
    public void openCart() {
        click(cartBtn());
    }

    @Step("Remove <{0}th> line item from cart")
    public void removeLineItem(String index) {
        click(removeLineItemBtn_byIndex(index));
    }

    @Step("Click \"Checkout\" btn in cart")
    public void clickCheckoutBtn_cart() {
        click(checkoutBtn_cart());
    }

    public void cleanCart() {
        int cartQty = Integer.valueOf(cartQty().text());
        for(int i = 0; i < cartQty; i++) {
            removeLineItem(String.valueOf(i+1));
            shouldNotBeVisible(removeLineItemBtn_byIndex(String.valueOf(cartQty - i)), "oops");
        }
    }

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
