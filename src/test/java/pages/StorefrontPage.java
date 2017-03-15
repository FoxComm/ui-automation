package pages;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.NoSuchElementException;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Objects;

import static base.BaseTest.storefrontUrl;
import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.By.xpath;

public class StorefrontPage extends BasePage {

    //======================================== AUTH FORM ========================================
    //---------------------------------------- ELEMENTS -----------------------------------------

    public SelenideElement logInLnk() {
        return $(xpath("//a[contains(@class, 'login-link')]"));
    }

    private SelenideElement nameFld() {
        return $(xpath("//input[@name='username']"));
    }

    public SelenideElement emailFld() {
        return $(xpath("//input[@type='email']"));
    }

    public SelenideElement passwordFld() {
        return $(xpath("//input[@type='password']"));
    }

    public SelenideElement logInBtn() {
        return $(xpath("//button[@type='submit' and text()='LOG IN']"));
    }

    private SelenideElement signUpLnk() {
        return $(xpath("//a[text()='Sign Up']"));
    }

    private SelenideElement signUpBtn() {
        return $(xpath("//button[@type='submit' and text()='SIGN UP']"));
    }

    private SelenideElement forgotLnk() {
        return $(xpath("//a[contains(@class, 'restore-link')]"));
    }

    private SelenideElement submitBtn() {
        return $(xpath("//button[@type='submit' and text()='SUBMIT']"));
    }

    private SelenideElement backToSignInBtn() {
        return $(xpath("//button[text()='BACK TO SIGN IN']"));
    }

    //TODO: id
    public SelenideElement closeAuthFormBtn() {
        return $(xpath("//a[contains(@class, 'close-button_cface')]/span[contains(@class, 'close-icon')]"));
    }

    public SelenideElement errorMsg_authForm(String msg) {
        return $(xpath("//div[contains(@class, 'error') and text()='" + msg + "']"));
    }

    public SelenideElement message_passRecovery(String msg) {
        return $(xpath("//div[contains(@class, 'top-message') and contains(text(), '" + msg + "')]"));
    }

    //---------------------------------------- STEPS -----------------------------------------

    @Step("Click \"Log In\" link")
    public void clickLogInLnk() {
        click(logInLnk());
    }

    @Step("Set \"Email\" fld to <{0}>")
    public void setEmail(String email) {
        setFieldVal(emailFld(), email);
    }

    @Step("Set \"Password\" fld to <{0}>")
    public void setPassword(String password) {
        setFieldVal(passwordFld(), password);

    }

    @Step("Click \"Log In\" btn")
    public void clickLogInBtn() {
        click(logInBtn());
    }

    @Step("Sign in with credentials email:<{0}> / password:<{1}>")
    public void logIn(String email, String password) {
        clickLogInLnk();
        setEmail(email);
        setPassword(password);
        clickLogInBtn();
    }

    @Step("Click \"Sign Up\" lnk")
    public void clickSignUpLnk() {
        click(signUpLnk());
    }

    @Step("Set \"First & Last Name\" fld to <{0}>")
    public void setName(String name) {
        setFieldVal(nameFld(), name);
    }

    @Step("Click \"Sign Up\" btn")
    public void clickSignUpBtn() {
        click(signUpBtn());
    }

    @Step("Click \"forgot?\" link")
    public void clickForgotLnk() {
        click(forgotLnk());
    }

    @Step("Click \"Submit\" btn")
    public void clickSubmitBtn() {
        click(submitBtn());
    }

    @Step("Click \"BACK TO SIGN IN\" btn")
    public void clickBackToSignInBtn() {
        click(backToSignInBtn());
    }

    @Step("Click \"X\" btn to close auth form")
    public void closeAuthForm() {
        click(closeAuthFormBtn());
    }


    //======================================== GENERAL CONTROLS ========================================
    //-------------------------------------------- ELEMENTS --------------------------------------------

    public SelenideElement logo() {
        return $(xpath("//span[contains(@class, 'fc-logo')]"));
    }

    public SelenideElement userMenuBtn_sf() {
        return $(xpath("//span[contains(@class, 'username')]"));
    }

    public SelenideElement menuLink(String linkText) {
        return $(xpath("//ul[contains(@class, 'menu')]//a[text()='" + linkText + "']"));
    }

    public SelenideElement errorMessage(String errorText) {
        return $(xpath("//div[contains(@class, 'error') and text()='" + errorText + "']"));
    }

    private SelenideElement categoryTitle(String category) {
        return $(xpath("//div[contains(@class, 'navigation')]//a[text()='" + category + "']"));
    }

    //---------------------------------------------- STEPS ----------------------------------------------

    @Step("Open user menu")
    private void openUserMenu() {
        click(userMenuBtn_sf());
    }

    @Step("Select menu link <{0}>")
    private void selectMenuLink(String linkText){
        click(menuLink(linkText));
    }

    @Step("Open \"User Menu\" and click <{0}>")
    public void selectInUserMenu(String menuLink) {
        menuLink = menuLink.toUpperCase();
        openUserMenu();
        selectMenuLink(menuLink);
    }

    @Step("Click logo")
    public void clickLogo() {
        click(logo());
    }

    @Step("Navigate to category <{0}>")
    public void navigateToCategory(String category) {
        click(categoryTitle(category));
    }


    //============================================ PROFILE ============================================
    //-------------------------------------------- ELEMENTS -------------------------------------------

    //TODO: id
    public SelenideElement userEmail() {
        return $(xpath("//div[contains(@class, 'content')]/div[2]/div[2]"));
    }

    private SelenideElement editLnk(String parameter) {
        parameter = parameter.toLowerCase().replaceAll(" ", "-");
        return $(xpath("//a[contains(@href, '/profile/" + parameter + "')]"));
    }

    public SelenideElement changePasswordBtn() {
        return $(xpath("//button[text()='CHANGE PASSWORD']"));
    }

    //TODO: id
    private SelenideElement nameFld_edit() {
        return $(xpath("//div[contains(text(), 'Use this form')]/following-sibling::*/input"));
    }

    //TODO: id
    private SelenideElement emailFld_edit() {
        return $(xpath("//div[contains(text(), 'Use this form')]/following-sibling::*/input"));
    }

    public SelenideElement saveBtn() {
        return $(xpath("//button[contains(@class, 'save-button')]"));
    }

    //TODO: id
    public ElementsCollection myAddresses() {
        return $$(xpath("//div[text()='My Shipping Addresses']/following-sibling::*/div/ul/li"));
    }

    //TODO: id
    private SelenideElement editAddressBtn(String index) {
        return $(xpath("//div[text()='My Shipping Addresses']/following-sibling::*/div/ul/li[" + index + "]//a[text()='EDIT']"));
    }

    //TODO: id
    private SelenideElement removeAddressBtn(String index) {
        return $(xpath("//div[text()='My Shipping Addresses']/following-sibling::*/div/ul/li[" + index + "]//div[text()='REMOVE']"));
    }

    //TODO: id
    public SelenideElement restoreAddressBtn(String index) {
        return $(xpath("//div[text()='My Shipping Addresses']/following-sibling::*/div/ul/li[" + index + "]//div[text()='RESTORE']"));
    }

    //TODO: id
    public SelenideElement shipAddress_name(String index) {
        return $(xpath("//ul[contains(@class, 'list')]/li[" + index + "]//h3[contains(@class, 'title')]"));
    }

    //TODO: id
    public SelenideElement shipAddress_zip(String index) {
        return $(xpath("//ul[contains(@class, 'list')]/li[" + index + "]//ul[contains(@class, 'address-details')]/li[3]/span[2]"));
    }

    //TODO: id
    public SelenideElement shipAddress_state(String index) {
        return $(xpath("//ul[contains(@class, 'list')]/li[" + index + "]//ul[contains(@class, 'address-details')]/li[3]/span[1]"));
    }

    //TODO: id
    private SelenideElement deletedAddress(String index) {
        return $(xpath("//li[" + index + "]//ul[contains(@class, 'deleted-content')]"));
    }

    private SelenideElement addressIsDefaultChbx() {
        return $(xpath("//input[@type='checkbox']"));
    }

    private SelenideElement addAddressBtn() {
        return $(xpath("//button[text()='Add Address']"));
    }

    private SelenideElement nameFld_shipAddress() {
        return $(xpath("//input[@name='name']"));
    }

    private SelenideElement address1Fld() {
        return $(xpath("//input[@name='address1']"));
    }

    private SelenideElement address2Fld() {
        return $(xpath("//input[@name='address2']"));
    }

    private SelenideElement zipFld() {
        return $(xpath("//input[@placeholder='ZIP']"));
    }

    public SelenideElement cityFld() {
        return $(xpath("//input[@name='city']"));
    }

    private SelenideElement phoneNumberFld() {
        return $(xpath("//input[@name='phoneNumber']"));
    }

    public SelenideElement selectAddressRbtn(String index) {
        return $(xpath("//li[" + index + "]//input[contains(@name, 'address-radio')]"));
    }

    //---------------------------------------------- STEPS ---------------------------------------------

    @Step("Click \"EDIT\" next to <{0}>")
    public void clickEditLnk(String parameter) {
        click(editLnk(parameter));
    }

    @Step("Set \"Name\" fld to <{0}>")
    public void setNameFld(String name) {
        setFieldVal(nameFld_edit(), name);
    }

    @Step("Set \"Email\" fld to <{0}>")
    public void setEmailFld(String email) {
        setFieldVal(emailFld_edit(), email);
    }

    @Step("Click \"Save\" btn")
    public void clickSaveBtn() {
        click(saveBtn());
    }

    @Step("Click \"EDIT\" btn next to <{0}th> address")
    public void clickEditBtn_address(String index) {
        click(editAddressBtn(index));
    }

    @Step("Click \"REMOVE\" btn next to <{0}th> address")
    public void clickRemoveBtn_address(String index) {
        click(removeAddressBtn(index));
    }

    @Step("Click \"RESTORE\" btn next to <{0}th> address")
    public void clickRestoreBtn_address(String index){
        click(restoreAddressBtn(index));
    }

    @Step("Click \"Add Address\" btn")
    public void clickAddAddressBtn() {
        click(addAddressBtn());
    }

    @Step("Fill out new address form; name:<{0}>, address1:<{1}>, address2:<{2}>, zip:<{3}>, phoneNumber:<{4}>")
    public void fillOutAddressForm(String name, String address1, String address2, String zip, String phoneNumber) {
        setName_shipAddress(name);
        setAddress1(address1);
        setaddress2(address2);
        setZIP(zip);
        setPhoneNumber(phoneNumber);
    }

    @Step("Check the \"Make this address my default\" checkbox")
    public void clickDefaultChbx() {
        shouldNotBeEmpty(nameFld_shipAddress(), "\"Name\" fld is empty");
        jsClick(addressIsDefaultChbx());
    }

    @Step("Set \"Name\" fld val to <{0}>")
    public void setName_shipAddress(String name) {
        setFieldVal(nameFld_shipAddress(), name);
    }

    @Step("Set \"Address 1\" fld val to <{0}>")
    public void setAddress1(String address1) {
        setFieldVal(address1Fld(), address1);
    }

    @Step("Set \"Address 2\" fld val to <{0}>")
    public void setaddress2(String address2) {
        setFieldVal(address2Fld(), address2);
    }

    @Step("Set \"ZIP\" fld val to <{0}>")
    public void setZIP(String zip) {
        setFieldVal(zipFld(), zip);
    }

    @Step("Set \"Phone Number\" fld val to <{0}>")
    public void setPhoneNumber(String phoneNumber) {
        setFieldVal(phoneNumberFld(), phoneNumber);
    }

    @Step("Assert that address is not displayed as deleted")
    public void assertAddressRestored(String index) {
        deletedAddress(index).shouldNotBe(visible);
    }

    @Step("Assert that address <{0}> is set as default")
    public void assertAddressIsSelected(String index) {
        scrollToElement(addAddressBtn());
        selectAddressRbtn(index).shouldBe(selected);
    }


    //========================================= CATEGORY & PDP ========================================
    //-------------------------------------------- ELEMENTS -------------------------------------------

    private SelenideElement pdpTitle(String title) {
        return $(xpath("//h1/a[text()='" + title + "']"));
    }

    private SelenideElement qty_PDP() {
        return $(xpath("//div[contains(@class, 'cart-actions')]//select"));
    }

    private SelenideElement qtyOption_PDP(String qty) {
        return $(xpath("//div[contains(@class, 'cart-actions')]//select/option[@value='" + qty + "']"));
    }

    private SelenideElement addToCartBtn_PDP() {
        return $(xpath("//button[contains(@class, 'add-to-cart-btn')]"));
    }

    //TODO: id
    private SelenideElement couponCodeFld() {
        return $(xpath("//input[@placeholder='Coupon Code']"));
    }

    //TODO: id
    private SelenideElement applyCouponBtn() {
        return $(xpath("//button[text()='Apply']"));
    }

    //TODO: id & classes
    public SelenideElement appliedCoupon() {
        return $(xpath("//div[contains(@class, 'coupon-code')]"));
    }

    //TODO: class
    private SelenideElement removeCouponBtn() {
        return $(xpath("//span[contains(@class, 'delete-promo')]"));
    }

    //TODO: id
    public SelenideElement adjustmentTotal_cart() {
        return $(xpath("//div[contains(@class, 'coupon')]/span[1]"));
    }


    //---------------------------------------------- STEPS --------------------------------------------

    @Step("Open PDP: <{0}>")
    public void openPDP(String productName) {
        try {
            click(pdpTitle(productName));
        } catch (RuntimeException ignored) {
            refresh();
            click(pdpTitle(productName));
        }
    }

    @Step("Set product quantity to <{0}>")
    public void setQty_PDP(String qty) {
        click(qty_PDP());
        click(qtyOption_PDP(qty));
    }

    @Step("Click \"Add To Cart\" btn")
    public void clickAddToCartBtn() {
        click(addToCartBtn_PDP());
    }

    @Step("Set \"Coupon Code\" fld val to <{0}>")
    private void setCouponCode(String code) {
        setFieldVal(couponCodeFld(), code);
    }

    //TODO: id
    @Step("Click \"Apply\" btn")
    private void clickApplyBtn_coupon() {
        click(applyCouponBtn());
    }

    @Step("Apply coupon to cart, code: <{0}>")
    public void applyCoupon(String code) {
        setCouponCode(code);
        clickApplyBtn_coupon();
    }

    @Step("Remove coupon from cart")
    public void removeCoupon() {
        click(removeCouponBtn());
    }


    //============================================== CART =============================================
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


    //============================================ CHECKOUT ===========================================
    //-------------------------------------------- ELEMENTS -------------------------------------------

    public SelenideElement navItem(String index) {
        return $(xpath("//ol[contains(@class, 'nav-list')]/li[" + index + "]"));
    }

    public void assertCheckoutStepActive(String step) {
        $(xpath("//ol/li[contains(@class, 'active')]//*[text()='Delivery']"));
    }

    public SelenideElement orderSummary() {
        return $(xpath("//*[contains(@class, 'order-summary')]"));
    }

    private SelenideElement guestEmailFld() {
        return $(xpath("//div[text()='CHECKOUT AS GUEST']//following-sibling::div/input"));
    }

    private SelenideElement checkoutBtn_guestAuth() {
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

    private SelenideElement placeOrderBtn_checkout() {
        return $(xpath("//button[contains(@class, 'checkout-submit') and text()='Place Order']"));
    }

    public SelenideElement confirmationOrderNumber() {
        return $(xpath("//div[contains(@class, 'order-number')]/strong"));
    }

    //TODO: class
    public SelenideElement appliedShipAddress_name() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[1]"));
    }

    //TODO: class
    public SelenideElement appliedShipAddress_address1() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[2]"));
    }

    //TODO: class
    public SelenideElement appliedShipAddress_address2() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[3]"));
    }

    //TODO: class
    public SelenideElement appliedShipAddress_zip() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[4]/span[2]"));
    }

    //TODO: class
    public SelenideElement appliedShipAddress_city() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[4]"));
    }

    //TODO: class
    public SelenideElement appliedShipAddress_state() {
        return $(xpath("//ul[contains(@class, 'savedAddress')]/li[4]/span[1]"));
    }

    //TODO: class
    public SelenideElement appliedShipAddress_phoneNumber() {
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

    //TODO: id
    public SelenideElement selectShipMethodRbtn(int index) {
        return $(xpath("//div[contains(@class, 'form-header')]/following-sibling::div[" + index + "]//input[@name='delivery']"));
    }

    public SelenideElement appliedShipMethod_name() {
        return $(xpath("//article[contains(@class, 'delivery')]/div/div[contains(@class, 'name')]"));
    }

    public SelenideElement appliedShipMethod_price() {
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

    //---------------------------------------------- STEPS --------------------------------------------

    @Step("Set guest's \"Email\" fld to <{0}>")
    public void setGuestEmailFld(String email) {
        setFieldVal(guestEmailFld(), email);
    }

    @Step("Click \"Checkout\" btn at the guest auth form")
    public void clickCheckoutBtn_guestAuth() {
        click(checkoutBtn_guestAuth());
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
        jsClick(selectShipMethodRbtn(index));
    }

    @Step("Set \"Shipping Method\" to No.<{0}> available option")
    public void setShipMethod(int index) {
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
        click(placeOrderBtn_checkout());
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
        click(redeemBtn());
    }

    @Step("Set \"GIFT CARD NUMBER\" fld val to <{0}>")
    public void setGiftCardNumb(String gcCode) {
        setFieldVal(gcNumberFld(), gcCode);
    }

    @Step("Click \"X\" btn at applied gift card")
    public void clickRemoveGcBtn() {
        click(removeGiftCardBtn());
    }

    @Step("Remove Gift Card")
    public void removeGiftCard() {
        expandGiftCardBlock();
        clickRemoveGcBtn();
    }

    @Step("Click \"EDIT\" at <{0}> step")
    public void editStep(String step) {
        click(editStepBtn(step));
    }

    //============================================ HELPERS ===========================================

    public String getUrl() {
        return getWebDriver().getCurrentUrl();
    }

    public void cleanUp_beforeMethod() {
        if (!Objects.equals(cartQty().text(), "0")) {
            openCart();
            cleanCart();
            closeCart();
        } else {
            try {
                elementIsPresent("//a[contains(@class, 'login-link')]");
            } catch (NoSuchElementException ignored) {
                userMenuBtn_sf().click();
                menuLink("LOG OUT").click();
                logInLnk().shouldBe(visible);
            }
        }
    }

    public void cleanUp_afterMethod() {
        // if on storeadmin -- go back to storefront
        if (findInText(getUrl(), "/admin")) {
            open(storefrontUrl);
        } else {
            try {
                elementIsPresent("//span[contains(@class, 'back-icon')]");
                closeCart();
            } catch (NoSuchElementException ignored) {}
            try {
                elementIsPresent("//a[contains(@class, 'login-link')]");
                jsClick(logo());
            } catch (NoSuchElementException ignored) {
                open(storefrontUrl);
            }
        }

        // if cart has line items -- remove them all
        if (!checkCustomerAuth()) {
            if (!Objects.equals(cartQty().text(), "0")) {
                openCart();
                cleanCart();
                closeCart();
            }
        }

        // if signed in -- log out
        try {
            elementIsPresent("//a[contains(@class, 'login-link')]");
        } catch (NoSuchElementException ignored) {
            userMenuBtn_sf().click();
            menuLink("LOG OUT").click();
            logInLnk().shouldBe(visible);
        }
    }

}