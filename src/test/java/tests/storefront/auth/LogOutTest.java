package tests.storefront.auth;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static org.testng.Assert.assertEquals;

public class LogOutTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Can log out")
    public void logOut() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.setEmail(customerEmail);
        p.setPassword("78qa22!#");
        p.clickLogInBtn();
        shouldMatchText(p.userMenuBtn_sf(), customerName.toUpperCase(), "Failed to log out");
        p.selectInUserMenu("LOG OUT");

        p.logInLnk().shouldBe(visible);
    }

    @Test(priority = 2)
    @Description("User is redirected to the home page upon logging out under /profile")
    public void logOutUnderProfile() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.selectInUserMenu("LOG OUT");

        p.logInLnk().shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl);
    }

    @Test(priority = 3)
    @Description("Cart is blanked after log out")
    public void cartBlankedOnLogOut() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.selectInUserMenu("LOG OUT");

        p.logInLnk().shouldBe(visible);
        p.cartQty().shouldHave(text("0"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(0);
    }

    @Test(priority = 4)
    @Description("User is redirected to home page if logging out at order confirmation page")
    public void logOutAtOrderConfirmation() throws IOException {
        provideTestData("a customer ready to checkout");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.clickPlaceOrderBtn();
        shouldBeVisible(p.confirmationOrderNumber(), "Probably order isn't placed");

        p.selectInUserMenu("LOG OUT");
        assertEquals(getUrl(), storefrontUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}