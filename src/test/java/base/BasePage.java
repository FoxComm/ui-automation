package base;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class BasePage extends ConciseAPI {

    public SelenideElement userMenuBtn() {
        return $(By.xpath("//div[@class='_header_header__name']"));
    }

    public SelenideElement logoutBtn() {
        return $(By.xpath("//a[text()='Log out']"));
    }

    public SelenideElement settingsBtn() {
        return $(By.xpath("//a[text()='Settings']"));
    }

    public SelenideElement couponsNavMenu() {
        return $(By.xpath("//span[text()='Coupons']"));
    }




}
