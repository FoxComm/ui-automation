package pages.storefront;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.openqa.selenium.By.xpath;

public class CheckoutPage extends CartPage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

    public SelenideElement navItem(String index) {
        return $(xpath("//ol[contains(@class, 'nav-list')]/li[" + index + "]"));
    }

    public SelenideElement signUpLnk() {
        return $(xpath("//a[text()='Sign Up']"));
    }

    /**
     * email field at "LOG IN & CHECKOUT" form
     * (which is used for sign in & checkout as a registered customer instead of doing guest checkout)
     */
    //TODO: id
    private SelenideElement emailFld_guestAuth() {
        return $(xpath("//div[text()='LOG IN & CHECKOUT']/following-sibling::form//input[@type='email']"));
    }

    public SelenideElement checkoutStep(String title) {
        return $(xpath("//li[contains(@class, 'nav-item')]/a[text()='" + title + "']"));
    }

    public SelenideElement activeCheckoutStep(String step) {
        return $(xpath("//ol/li[contains(@class, 'active')]//*[text()='" + step + "']"));
    }

    public SelenideElement orderSummary() {
        return $(xpath("//*[contains(@class, 'order-summary')]"));
    }

    private SelenideElement guestEmailFld_auth() {
        return $(xpath("//div[text()='CHECKOUT AS GUEST']//following-sibling::div/input"));
    }

    public SelenideElement guestEmail_checkout() {
        return $(xpath("//input[contains(@class, 'guest-email')]"));
    }

    public SelenideElement checkoutBtn_guestAuth() {
        return $(xpath("//button[text()='CHECKOUT']"));
    }

    private SelenideElement saveAddressBtn() {
        return $(xpath("//button[text()='Save Address']"));
    }

    public SelenideElement shipAddress_checkout() {
        return $(xpath("//ul[contains(@class, 'address-details')]"));
    }

    private SelenideElement editAddressBtn_checkout(String index) {
        return $(xpath("//li[" + index + "]//div[text()='EDIT']"));
    }

    private SelenideElement continueBtn_checkout() {
        return $(xpath("//button[contains(@class, 'checkout-submit') and text()='Continue']"));
    }

    public SelenideElement placeOrderBtn() {
        return $(xpath("//button[contains(@class, 'checkout-submit') and text()='Place Order']"));
    }

    public SelenideElement confirmationOrderNumber() {
        return $(xpath("//div[contains(@class, 'order-number')]/strong"));
    }

    //TODO: class
    public SelenideElement name_appliedShipAddress() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[1]"));
    }

    //TODO: class
    public SelenideElement address1_appliedShipAddress() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[2]"));
    }

    //TODO: class
    public SelenideElement address2_appliedShipAddress() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[3]"));
    }

    //TODO: class
    public SelenideElement zip_appliedShipAddress() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[4]/span[2]"));
    }

    //TODO: class
    public SelenideElement city_appliedShipAddress() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[4]"));
    }

    //TODO: class
    public SelenideElement state_appliedShipAddress() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[4]/span[1]"));
    }

    //TODO: class
    public SelenideElement phoneNumber_appliedShipAddress() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[5]/span"));
    }

    public SelenideElement shipAddressByName_checkout(String name) {
        return $(xpath("//h3[text()='" + name + "']"));
    }

    public ElementsCollection shippingMethods() {
        return $$(xpath("//div[contains(@class, 'shipping-method')]"));
    }

    //TODO: id
    public SelenideElement selectShipMethodRbtn(String index) {
        return $(xpath("//div[contains(@class, 'form-header')]/following-sibling::div[" + index + "]//input[@name='delivery']"));
    }

    private SelenideElement selectShipMethodRbtn(int index) {
        return $(xpath("//div[contains(@class, 'form-header')]/following-sibling::div[" + index + "]//input[@name='delivery']"));
    }

    public SelenideElement name_appliedShipMethod() {
        return $(xpath("//article[contains(@class, 'delivery')]/div/div[contains(@class, 'name')]"));
    }

    public SelenideElement price_appliedShipMethod() {
        return $(xpath("//article[contains(@class, 'delivery')]//span"));
    }

    private SelenideElement cardDefaultChbx() {
        return $(xpath("//input[@id='set-default-card']"));
    }

    private SelenideElement holderNameFld() {
        return $(xpath("//input[@name='holderName']"));
    }

    private SelenideElement cardNumberFld() {
        return $(xpath("//input[@name='number']"));
    }

    private SelenideElement cvcFld() {
        return $(xpath("//input[@placeholder='CVC']"));
    }

    private SelenideElement monthDd() {
        return $(xpath("//input[@placeholder='MONTH']"));
    }

    private SelenideElement yearDd() {
        return $(xpath("//input[@placeholder='YEAR']"));
    }

    private SelenideElement dateDdOption(String option) {
        return $(xpath("//div[contains(@class, 'item') and text()='" + option + "']"));
    }

    public SelenideElement saveCardBtn() {
        return $(xpath("//button[contains(@class, 'checkout-submit')]"));
    }

    public SelenideElement creditCard() {
        return $(xpath("//div[contains(@class, 'credit-card')]"));
    }

    public ElementsCollection customerCardsWallet() {
        return $$(xpath("//fieldset[contains(@class, 'cards')]"));
    }

    public SelenideElement defaultCardMark() {
        return $(xpath("//div[contains(@class, 'credit-card')]//div[contains(@class, 'default-card')]"));
    }

    private SelenideElement deleteCardLnk(String index) {
        return $(xpath("//fieldset/div/div[" + index + "]//span[text()='Delete']"));
    }

    //TODO: class for div that wraps all cards
    public SelenideElement selectCardRbtn(String index) {
        return $(xpath("//fieldset/div/div[" + index + "]//input[@name='credit-card']"));
    }

    private SelenideElement couponBlockTitle_checkout() {
        return $(xpath("//h4[text()='COUPON CODE?']"));
    }

    private SelenideElement gcBlockTitle_checkout() {
        return $(xpath("//h4[text()='GIFT CARD?']"));
    }

    private SelenideElement gcNumberFld() {
        return $(xpath("//input[@placeholder='Gift Card Number']"));
    }

    private SelenideElement redeemBtn() {
        return $(xpath("//button[text()='Redeem']"));
    }

    public SelenideElement appliedGiftCard() {
        return $(xpath("//div[contains(@class, '_gift-card_')]"));
    }

    public SelenideElement removeGiftCardBtn() {
        return $(xpath("//div[contains(@class, '_gift-card_')]//span[contains(@class, 'fc-close')]"));
    }

    public SelenideElement takeMeHomeBtn() {
        return $(xpath("//button[contains(@class, 'button') and text()='Take me home']"));
    }

    public SelenideElement checkoutError(String errMsg) {
        return $(xpath("//div[@class='fc-alert _type_error' and text()='" + errMsg + "']"));
    }

    public SelenideElement grandTotal() {
        return $(xpath("//div[contains(@class, 'grand-total')]/div[contains(@class, 'value')]/span"));
    }

    private SelenideElement editStepBtn(String step) {
        step = step.toLowerCase();
        return $(xpath("//article[contains(@class, '" + step + "')]//div[text()='EDIT']"));
    }

    public ElementsCollection lineItems_checkout() {
        return $$(xpath("//table[contains(@class, 'products')]/tbody/tr"));
    }

    public SelenideElement lineItem_checkout(String productTitle) {
        return $(xpath("//table[contains(@class, 'products')]//span[contains(@class, 'product-name') and text()='" + productTitle + "']"));
    }

    //---------------------------------------------- STEPS --------------------------------------------

    @Step("Click \"Sign Up\" link")
    public void clickSignUpLnk() {
        click(signUpLnk());
    }

    @Step("Log in as <{0}> / <{1}> at guest checkout auth")
    public void logIn_guestAuth(String email, String password) {
        setEmail_guestAuth(email);
        setPassword_logIn(password);
        clickLogInBtn();
    }

    @Step("Set \"EMAIL\" fld to <{0}>")
    private void setEmail_guestAuth(String email) {
        setFieldVal(emailFld_guestAuth(), email);
    }

    @Step("Set guest \"Email\" fld to <{0}>")
    public void setGuestEmail_guestAuth(String email) {
        setFieldVal(guestEmailFld_auth(), email);
    }

    @Step("Click \"Checkout\" btn at the guest auth form")
    public void clickCheckoutBtn_guestAuth() {
        click(checkoutBtn_guestAuth());
    }

    @Step("Set guest email at checkout")
    public void setGuestEmail_checkout(String email) {
        setFieldVal(guestEmail_checkout(), email);
    }

    @Step("Click \"Save Address\" btn at checkout")
    public void clickSaveAddressBtn() {
        click(saveAddressBtn());
    }

    @Step("Click \"EDIT\" btn next to <{0}th> shipping address")
    public void editAddress_checkout(String index) {
        click(editAddressBtn_checkout(index));
    }

    @Step("Select <{0}th> shipping address")
    public void setShipAddress(String index) {
        jsClick(selectAddressRbtn(index));
    }

    @Step("Set \"Shipping Method\" to last available value on the list")
    public void setShipMethod(String index) {
        shouldBeVisible($(xpath("//article[contains(@class, 'delivery')]//button[contains(@class, 'checkout-submit') and text()='Continue']")), "");
        sleep(100);
        jsClick(selectShipMethodRbtn(index));
    }

    @Step("Set \"Shipping Method\" to No.<{0}> available option")
    public void setShipMethod(int index) {
        shouldBeVisible($(xpath("//article[contains(@class, 'delivery')]//button[contains(@class, 'checkout-submit') and text()='Continue']")), "");
        sleep(100);
        jsClick(selectShipMethodRbtn(index));
    }

    @Step("Click \"CONTINUE\" btn")
    public void clickContinueBtn() {
        click(continueBtn_checkout());
    }

    @Step("Fill out card form -- holderName:<{0}>, cardNumber:<{1}>, cvc:<{2}>, month:<{3}>, year:<{4}> makeDefault: <{5}>")
    public void fillOutCardForm(String holderName, String cardNumber, String cvc, String month, String year, boolean makeDefault) {
        setHolderNameFld(holderName);
        setCardNumberFld(cardNumber);
        setCVCFld(cvc);
        setMonthDd(month);
        setYearDd(year);
        if (makeDefault) { clickCardDefaultChbx(); }
    }

    @Step("Click \"Set Card Default\" checkbox")
    private void clickCardDefaultChbx() {
        jsClick(cardDefaultChbx());
    }

    @Step("Set \"Holder Name\" fld val to <{0}>")
    private void setHolderNameFld(String name) {
        setFieldVal(holderNameFld(), name);
    }

    @Step("Set \"Card Nmber\" fld val to <{0}>")
    private void setCardNumberFld(String cardBumber) {
        setFieldVal(cardNumberFld(), cardBumber);
    }

    @Step("Set \"CVC\" fld val to <{0}>")
    private void setCVCFld(String cvc) {
        setFieldVal(cvcFld(), cvc);
    }

    @Step("Set \"Month\" dd val to <{0}>")
    private void setMonthDd(String month) {
        click(monthDd());
        click(dateDdOption(month));
    }

    @Step("Set \"Year\" dd val to <{0}>")
    private void setYearDd(String year) {
        click(yearDd());
        click(dateDdOption(year));
    }

    @Step("Click \"Save Card\" btn")
    public void clickSaveCardBtn() {
        click(saveCardBtn());
    }

    @Step("Select <{0}th> credit card")
    public void setCreditCard(String index) {
        jsClick(selectCardRbtn(index));
    }

    @Step("Assert credit card is selected")
    public void assertCardIsSelected(String index) {
        selectCardRbtn(index).shouldBe(selected);
    }

    @Step("Click \"PLACE ORDER\" btn")
    public void clickPlaceOrderBtn() {
        click(placeOrderBtn());
    }

    @Step("Click \"Delete\" lnk next to <{0}th> card in customer's wallet")
    public void clickDeleteCardLnk(String index) {
        click(deleteCardLnk(index));
    }

    @Step("Apply coupon code to card: <{0}>")
    public void applyCouponCode(String code) {
        expandCouponCodeBlock();
        setCouponCode(code);
        clickApplyBtn_coupon();
    }

    @Step("Expand \"COUPON CODE\" block")
    public void expandCouponCodeBlock() {
        click(couponBlockTitle_checkout());
    }

    @Step("Expand \"GIFT CARD\" block")
    public void expandGiftCardBlock() {
        click(gcBlockTitle_checkout());
    }

    @Step("Apply gift card code:<{0}>")
    public void redeemGiftCard(String gcCode) {
        expandGiftCardBlock();
        setGiftCardNumb(gcCode);
        clickRedeemBtn();
    }

    @Step("Set \"GIFT CARD NUMBER\" fld val to <{0}>")
    private void setGiftCardNumb(String gcCode) {
        setFieldVal(gcNumberFld(), gcCode);
    }

    @Step("Click \"X\" btn at applied gift card")
    private void clickRemoveGcBtn() {
        click(removeGiftCardBtn());
    }

    @Step("Remove Gift Card")
    public void removeGiftCard() {
        expandGiftCardBlock();
        clickRemoveGcBtn();
    }

    @Step("Click \"REDEEM\" btn")
    private void clickRedeemBtn() {
        click(redeemBtn());
    }

    @Step("Switch to {0} step")
    public void switchToStep(String title) {
        click(checkoutStep(title));
    }

    @Step("Click \"EDIT\" at <{0}> step")
    public void editStep(String step) {
        click(editStepBtn(step));
    }

}