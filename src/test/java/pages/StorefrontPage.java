package pages;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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


    //======================================== GENERAL CONTROLS ========================================
    //-------------------------------------------- ELEMENTS --------------------------------------------

    public SelenideElement logo() {
        return $(xpath("//span[contains(@class, 'fc-logo')]"));
    }

    public SelenideElement userName() {
        return $(xpath("//span[contains(@class, 'username')]"));
    }

    public SelenideElement menuLink(String linkText) {
        return $(xpath("//ul[contains(@class, 'menu')]//a[text()='" + linkText + "']"));
    }

    public SelenideElement errorMessage(String errorText) {
        return $(xpath("//div[contains(@class, 'error') and text()='" + errorText + "']"));
    }

    //---------------------------------------------- STEPS ----------------------------------------------

    @Step("Open user menu")
    private void openUserMenu() {
        click(userName());
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

    private SelenideElement defaultAddressRbtn(String index) {
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
        setZip(zip);
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
    private void setAddress1(String address1) {
        setFieldVal(address1Fld(), address1);
    }

    @Step("Set \"Address 2\" fld val to <{0}>")
    private void setaddress2(String address2) {
        setFieldVal(address2Fld(), address2);
    }

    @Step("Set \"ZIP\" fld val to <{0}>")
    public void setZip(String zip) {
        setFieldVal(zipFld(), zip);
    }

    @Step("Set \"Phone Number\" fld val to <{0}>")
    private void setPhoneNumber(String phoneNumber) {
        setFieldVal(phoneNumberFld(), phoneNumber);
    }

    @Step("Assert that address is not displayed as deleted")
    public void assertAddressRestored(String index) {
        deletedAddress(index).shouldNotBe(visible);
    }

    @Step("Assert that address <{0}> is set as default")
    public void assertAddressIsDefault(String index) {
        defaultAddressRbtn(index).shouldBe(selected);
    }


    //========================================= CATEGORY & PDP ========================================
    //-------------------------------------------- ELEMENTS -------------------------------------------

    private SelenideElement pdpTitle(String title) {
        return $(xpath("//h1/a[text()='" + title + "']"));
    }

    private SelenideElement addToCartBtn_PDP() {
        return $(xpath("//button[contains(@class, 'add-to-cart-btn')]"));
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

    @Step("Click \"Add To Cart\" btn")
    public void clickAddToCartBtn() {
        click(addToCartBtn_PDP());
    }


    //========================================= CATEGORY & PDP ========================================
    //-------------------------------------------- ELEMENTS -------------------------------------------

    public SelenideElement lineItemByName_cart(String productName) {
        return $(xpath("//div[contains(@class, 'product-name') and text()='" + productName + "']"));
    }

    public SelenideElement cartSubtotal() {
        return $(xpath("//div[contains(@class, 'cart-subtotal')]//span"));
    }

    private SelenideElement closeCartBtn() {
        return $(xpath("//span[contains(@class, 'back-icon')]"));
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

    private SelenideElement checkoutBtn_cart() {
        return $(xpath("//button[contains(@class, 'checkout-button')]"));
    }

    //---------------------------------------------- STEPS --------------------------------------------

    @Step("Close cart")
    public void closeCart() {
        click(closeCartBtn());
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


    //============================================ CHECKOUT ===========================================
    //-------------------------------------------- ELEMENTS -------------------------------------------

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
    public void clickEditAddressBtn_checkout(String index) {
        click(editAddressBtn_checkout(index));
    }

    //============================================ HELPERS ===========================================

    public String getUrl() {
        return getWebDriver().getCurrentUrl();
    }

    public void cleanUp_afterMethod() {
        // if on storeadmin -- go back to storefront
        if (findInText(getUrl(), "/admin")) {
            open(storefrontUrl);
        } else {
            try {
                getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                WebElement logInLnk = getWebDriver().findElement(By.xpath("//a[contains(@class, 'login-link')]"));
                click(logo());
            } catch (NoSuchElementException ignored) {
                open(storefrontUrl);
            }
        }

        // if cart has line items -- remove them all
        if (checkCustomerAuth()) {
            if (!Objects.equals(cartQty().text(), "0")) {
                openCart();
                cleanCart();
                closeCart();
            }
        }

        // if signed in -- log out
        try {
            getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebElement logInLnk = getWebDriver().findElement(By.xpath("//a[contains(@class, 'login-link')]"));
        } catch (NoSuchElementException ignored) {
            userName().click();
            menuLink("LOG OUT").click();
            logInLnk().shouldBe(visible);
        }
    }

    public void cleanUp_beforeMethod() {
        if (!Objects.equals(cartQty().text(), "0")) {
            openCart();
            cleanCart();
            closeCart();
        } else {
            try {
                getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                WebElement logInLnk = getWebDriver().findElement(By.xpath("//a[contains(@class, 'login-link')]"));
            } catch (NoSuchElementException ignored) {
                userName().click();
                menuLink("LOG OUT").click();
                logInLnk().shouldBe(visible);
            }
        }
    }

}