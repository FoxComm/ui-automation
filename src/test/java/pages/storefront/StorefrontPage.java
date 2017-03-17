package pages.storefront;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.NoSuchElementException;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Objects;

import static base.BaseTest.storefrontUrl;
import static com.codeborne.selenide.Condition.selected;
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

    public SelenideElement menuLink(String linkText) {
        return $(xpath("//ul[contains(@class, 'menu')]//a[text()='" + linkText + "']"));
    }

    public SelenideElement errorMessage(String errorText) {
        return $(xpath("//div[contains(@class, 'error') and text()='" + errorText + "']"));
    }

    private SelenideElement categoryTitle(String category) {
        return $(xpath("//div[contains(@class, 'navigation')]//a[text()='" + category + "']"));
    }

    //---------------------------------------------- STEPS ----------------------------------------------

    @Step("Open user menu")
    private void openUserMenu() {
        click(userMenuBtn_sf());
    }

    @Step("Select menu link <{0}>")
    private void selectMenuLink(String linkText){
        click(menuLink(linkText));
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

    @Step("Navigate to category <{0}>")
    public void navigateToCategory(String category) {
        click(categoryTitle(category));
    }

    //============================================ HELPERS ===========================================

    public String getUrl() {
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
                menuLink("LOG OUT").click();
                logInLnk().shouldBe(visible);
            }
        }
    }

    public void cleanUp_afterMethod() {
        // if on storeadmin -- go back to storefront
        if (findInText(getUrl(), "/admin")) {
            open(storefrontUrl);
        } else {
            try {
                elementIsPresent("//span[contains(@class, 'back-icon')]");
                closeCart();
            } catch (NoSuchElementException ignored) {}
            try {
                elementIsPresent("//a[contains(@class, 'login-link')]");
                jsClick(logo());
            } catch (NoSuchElementException ignored) {
                open(storefrontUrl);
            }
        }

        // if cart has line items -- remove them all
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
            menuLink("LOG OUT").click();
            logInLnk().shouldBe(visible);
        }
    }

}