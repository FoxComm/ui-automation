package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class LoginPage extends BasePage {

    public SelenideElement emailField() {
        return $("#form-field-1");
    }
    public SelenideElement passwordField() {
        return $("#form-field-2");
    }
    public SelenideElement googleAuthButton() {
        return $(By.xpath("//button[@class='fc-btn fc-login__google-btn']"));
    }
    public SelenideElement logoutSuccessMsg() {
        return $(By.xpath("//div[@class='fc-alert is-alert-success']"));
    }
    public SelenideElement loginErrorMsg() {
        return $(By.xpath("//div[@class='fc-alert is-alert-error']"));
    }

    @Step("Log in as {0} / {1}")
    public void login(String email, String password) {
        emailField().val(email);
        passwordField().val(password).submit();
        sleep(3000);
    }

}