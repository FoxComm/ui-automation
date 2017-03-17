package pages.storefront;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class AuthPage extends BasePage {

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

    public SelenideElement signUpLnk() {
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

    @Step("Fill out sign up form; name:<{0}>, email:<{1}>, password:<{2}>")
    public void fillOutSignUpForm(String name, String email, String pass) {
        setName(name);
        setEmail(email);
        setPassword(pass);
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

}
