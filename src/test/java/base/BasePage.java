package base;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class BasePage extends ConciseAPI {

    public SelenideElement userMenu() {
        return $(By.xpath("//div[@class='_header_header__name']"));
    }

    public SelenideElement logoutButton() {
        return $(By.xpath("//ul[@class='_header_usermenu__usermenu']/li"));
    }






}
