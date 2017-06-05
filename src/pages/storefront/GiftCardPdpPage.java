package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class GiftCardPdpPage extends PdpPage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

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