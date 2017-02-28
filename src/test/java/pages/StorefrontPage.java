package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;
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

    @Step("Log out")
    public void logOut() {
        openUserMenu();
        selectMenuLink("LOG OUT");
    }

    @Step("Log out")
    public void openProfile() {
        openUserMenu();
        selectMenuLink("PROFILE");
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


    //---------------------------------------------- STEPS --------------------------------------------

    @Step("Set guest's \"Email\" fld to <{0}>")
    public void setGuestEmailFld(String email) {
        setFieldVal(guestEmailFld(), email);
    }

    @Step("Click \"Checkout\" btn at the guest auth form")
    public void clickCheckoutBtn_guestAuth() {
        click(checkoutBtn_guestAuth());
    }

}