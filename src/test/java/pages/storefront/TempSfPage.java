package pages.storefront;

import com.codeborne.selenide.ex.ElementNotFound;
import org.openqa.selenium.NoSuchElementException;

import java.util.Objects;

import static base.BaseTest.storefrontUrl;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.xpath;

public class TempSfPage extends StorefrontPage {

    //----- main steps

    public void cleanUp_beforeMethod() {
        //if cart is not empty -> remove all line items
        if (!Objects.equals(cartQty().text(), "0")) {
            openCart();
            cleanCart();
            closeCart();
        } else {
            //if signed in -> log out
            try {
                $(xpath("//a[contains(@class, 'login-link')]")).waitUntil(visible, 2000);
            } catch (NoSuchElementException | ElementNotFound ignored) {
                userMenuBtn_sf().click();
                userMenuLink("LOG OUT").click();
                logInLnk().shouldBe(visible);
            }
        }
    }

    public void cleanUp_afterMethod() {
        //if on storeadmin -> go back to storefront
        if (findInText(getUrl(), "/admin")) {
            open(storefrontUrl);
        } else {
            jsClick(logo());
            assertUrl(getUrl(), storefrontUrl, 3000);
        }

        //if signed in -> log out
        try {
            $(xpath("//a[contains(@class, 'login-link')]")).waitUntil(visible, 2000);
        } catch (NoSuchElementException | ElementNotFound ignored) {
            userMenuBtn_sf().click();
            userMenuLink("LOG OUT").click();
            logInLnk().shouldBe(visible);
        }

        //if it's a guest session && cart is not empty -> remove all line items
        if (!checkCustomerAuth()) {
            if (!Objects.equals(cartQty().text(), "0")) {
                openCart();
                cleanCart();
                closeCart();
            }
        }
    }

    //------- backup

//    public void cleanUp_beforeMethod() {
//        if (!Objects.equals(cartQty().text(), "0")) {
//            openCart();
//            cleanCart();
//            closeCart();
//        } else {
//            try {
//                $(xpath("//a[contains(@class, 'login-link')]")).waitUntil(visible, 2000);
//            } catch (NoSuchElementException ignored) {
//                userMenuBtn_sf().click();
//                userMenuLink("LOG OUT").click();
//                logInLnk().shouldBe(visible);
//            }
//        }
//    }
//
//    public void cleanUp_afterMethod() {
//        // if on storeadmin -> go back to storefront
//        if (findInText(getUrl(), "/admin")) {
//            open(storefrontUrl);
//        } else {
//            refresh();
//            try {
//                $(xpath("//a[contains(@class, 'login-link')]")).waitUntil(visible, 2000);
//                jsClick(logo());
//            } catch (NoSuchElementException ignored) {
//                open(storefrontUrl);
//            }
//        }
//
//        // if it's a guest session && cart is not empty -> remove all line items
//        if (!checkCustomerAuth()) {
//            if (!Objects.equals(cartQty().text(), "0")) {
//                openCart();
//                cleanCart();
//                closeCart();
//            }
//        }
//
//        // if signed in -> log out
//        try {
//            $(xpath("//a[contains(@class, 'login-link')]")).waitUntil(visible, 2000);
//        } catch (NoSuchElementException ignored) {
//            userMenuBtn_sf().click();
//            userMenuLink("LOG OUT").click();
//            logInLnk().shouldBe(visible);
//        }
//    }

}
