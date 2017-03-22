package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.NoSuchElementException;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Objects;

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

    public SelenideElement menuLink(String linkText) {
        return $(xpath("//ul[contains(@class, 'menu')]//a[text()='" + linkText + "']"));
    }

    public SelenideElement errorMessage(String errorText) {
        return $(xpath("//div[contains(@class, 'error') and text()='" + errorText + "']"));
    }

    private SelenideElement categoryTitle(String category) {
        return $(xpath("//div[contains(@class, 'navigation')]//a[text()='" + category + "']"));
    }

    private SelenideElement subCategoryTitle(String subCategory) {
        return $(xpath("//div[contains(@class, '_item_') and text()='" + subCategory + "']"));
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

    @Step("Navigate to sub-category <{0}>")
    public void navigateToSubCategory(String subCategory) {
        click(subCategoryTitle(subCategory));
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
            menuLink("LOG OUT").click();
            logInLnk().shouldBe(visible);
        }
    }

}