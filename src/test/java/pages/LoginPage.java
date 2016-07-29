package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.openqa.selenium.By.xpath;

public class LoginPage extends BasePage {

    public SelenideElement emailField() {
        return $(xpath("//label[text()='Email']/following-sibling::*"));
    }
    public SelenideElement passwordField() {
        return $(xpath("//div[text()='Password']/../../following-sibling::*"));
    }
    public SelenideElement googleAuthButton() {
        return $(xpath("//button[@class='fc-btn fc-login__google-btn']"));
    }
    public SelenideElement logoutSuccessMsg() {
        return $(xpath("//div[@class='fc-alert is-alert-success']"));
    }
    public SelenideElement loginErrorMsg() {
        return $(xpath("//div[@class='fc-alert is-alert-error']"));
    }

    @Step("Log in as {0} / {1}")
    public void login(String email, String password) {
        emailField().val(email);
        passwordField().val(password).submit();
        sleep(3000);
    }

}