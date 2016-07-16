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

    //---------------------------- N A V I G A T I O N    M E N U ----------------------------//
    public SelenideElement productsNavMenu() {
        return $(By.xpath("//span[text()='Products']"));
    }

    public SelenideElement skusNavMenu() {
        return $(By.xpath("//span[text()='SKUs']"));
    }

    public SelenideElement couponsNavMenu() {
        return $(By.xpath("//span[text()='Coupons']"));
    }
    //----------------------------------------------------------------------------------------//






}
