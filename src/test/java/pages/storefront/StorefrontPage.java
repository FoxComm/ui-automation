package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.NoSuchElementException;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static base.BaseTest.storefrontUrl;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
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

    public void cleanUp_beforeMethod() {
        if (!Objects.equals(cartQty().text(), "0")) {
            openCart();
            cleanCart();
            closeCart();
        } else {
            try {
                elementIsPresent("//a[contains(@class, 'login-link')]");
            } catch (NoSuchElementException ignored) {
                userMenuBtn_sf().click();
                userMenuLink("LOG OUT").click();
                logInLnk().shouldBe(visible);
            }
        }
    }

    public void cleanUp_afterMethod() {
        // if on storeadmin -- go back to storefront
        if (findInText(getUrl(), "/admin")) {
            open(storefrontUrl);
        } else {
            refresh();
            try {
                elementIsPresent("//a[contains(@class, 'login-link')]");
                jsClick(logo());
            } catch (NoSuchElementException ignored) {
                open(storefrontUrl);
            }
        }

        // if it's a guest session and cart is not empty -- remove all line items
        if (!checkCustomerAuth()) {
            if (!Objects.equals(cartQty().text(), "0")) {
                openCart();
                cleanCart();
                closeCart();
            }
        }

        // if signed in -- log out
        try {
            elementIsPresent("//a[contains(@class, 'login-link')]");
        } catch (NoSuchElementException ignored) {
            userMenuBtn_sf().click();
            userMenuLink("LOG OUT").click();
            logInLnk().shouldBe(visible);
        }
    }

}