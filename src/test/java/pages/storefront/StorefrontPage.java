package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.close;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.By.xpath;

public class StorefrontPage extends NavigationPage {

    //======================================== GENERAL CONTROLS ========================================
    //-------------------------------------------- ELEMENTS --------------------------------------------

    public SelenideElement logo() {
        return $(xpath("//span[contains(@class, 'fc-logo')]"));
    }

    public SelenideElement userMenuBtn_sf() {
        return $(xpath("//span[contains(@class, 'username')]"));
    }

    public SelenideElement userMenuLink(String linkText) {
        return $(xpath("//ul[contains(@class, 'menu')]//a[text()='" + linkText + "']"));
    }

    public SelenideElement errorMessage(String errorText) {
        return $(xpath("//div[contains(@class, 'error') and text()='" + errorText + "']"));
    }

    public SelenideElement notFoundMsg(String msg) {
        return $(xpath("//p[contains(@class, 'not-found') and text()='" + msg + "']"));
    }


    //---------------------------------------------- STEPS ----------------------------------------------

    @Step("Open user menu")
    private void openUserMenu() {
        click(userMenuBtn_sf());
    }

    @Step("Select menu link <{0}>")
    private void selectMenuLink(String linkText){
        click(userMenuLink(linkText));
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

    @Step("Proceed to checkout")
    public void proceedToCheckout() {
        openCart();
        clickCheckoutBtn_cart();
    }

    //============================================ HELPERS ===========================================

    public String getUrl() {
        getWebDriver().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        return getWebDriver().getCurrentUrl();
    }

    public void cleanUp() {
        close();
    }

}