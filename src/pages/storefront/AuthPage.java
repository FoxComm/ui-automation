package pages.storefront;

import base.AdminBasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class AuthPage extends AdminBasePage {

    //---------------------------------------- ELEMENTS -----------------------------------------

    public SelenideElement logInLnk() {
        return $(xpath("//a[contains(@class, 'login-link')]"));
    }

    private SelenideElement nameFld() {
        return $(xpath("//input[@name='username']"));
    }

    private SelenideElement emailFld_passReset() {
        return $(xpath("//input[@type='email']"));
    }

    private SelenideElement emailFld_logIn() {
        return $(xpath("//div[text()='LOG IN']/following-sibling::*//input[@type='email']"));
    }

    private SelenideElement emailFld_signUp() {
        return $(xpath("//div[contains(text(), 'SIGN UP')]/following-sibling::*//input[@type='email']"));
    }

    public SelenideElement passwordFld() {
        return $(xpath("//input[@type='password']"));
    }

    public SelenideElement passwordFld_signUp() {
        return $(xpath("//div[contains(text(), 'SIGN UP')]/following-sibling::*//input[@type='password']"));
    }

    public SelenideElement logInBtn() {
        return $(xpath("//button[@type='submit' and text()='LOG IN']"));
    }

    public SelenideElement signUpBtn() {
        return $(xpath("//button[@type='submit' and text()='SIGN UP']"));
    }

    private SelenideElement forgotLnk() {
        return $(xpath("//a[contains(@class, 'restore-link')]"));
    }

    private SelenideElement submitBtn() {
        return $(xpath("//button[@type='submit' and text()='SUBMIT']"));
    }

    private SelenideElement backToLogInBtn() {
        return $(xpath("//button[text()='BACK TO LOG IN']"));
    }

    private SelenideElement backToLogInLnk() {
        return $(xpath("//a[text()='BACK TO LOG IN']"));
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

    @Step("Set log in \"Email\" fld to <{0}>")
    public void setEmail_logIn(String email) {
        setFieldVal(emailFld_logIn(), email);
    }

    @Step("Set sign up \"Email\" fld to <{0}>")
    public void setEmail_signUp(String email) {
        setFieldVal(emailFld_signUp(), email);
    }

    @Step("Set \"Email\" fld to <{0}>")
    public void setEmail_passReset(String email) {
        setFieldVal(emailFld_passReset(), email);
    }

    @Step("Set \"Password\" fld to <{0}>")
    public void setPassword_logIn(String password) {
        setFieldVal(passwordFld(), password);
    }

    @Step("Set sign up \"Password\" fld to <{0}>")
    public void setPassword_signUp(String password) {
        setFieldVal(passwordFld_signUp(), password);
    }

    @Step("Click \"Log In\" btn")
    public void clickLogInBtn() {
        click(logInBtn());
    }

    @Step("Sign in with credentials email:<{0}> / password:<{1}>")
    public void logIn(String email, String password) {
        clickLogInLnk();
        setEmail_logIn(email);
        setPassword_logIn(password);
        clickLogInBtn();
    }

    @Step("Fill out sign up form; name:<{0}>, email:<{1}>, password:<{2}>")
    public void fillOutSignUpForm(String name, String email, String password) {
        setName(name);
        setEmail_signUp(email);
        setPassword_signUp(password);
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
    public void clickBackToLogInBtn() {
        click(backToLogInBtn());
    }

    @Step("Click \"BACK TO SIGN IN\" link")
    public void clickBackToLogInLnk() {
        click(backToLogInLnk());
    }

    @Step("Click \"X\" btn to close auth form")
    public void closeAuthForm() {
        click(closeAuthFormBtn());
    }

}
