package tests.storefront.auth;

import base.StorefrontTPGBasePage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.timeout;
import static com.codeborne.selenide.Selenide.refresh;

public class LogOutTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        clearCache();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Log Out")
    @Description("Can log out")
    public void logOut() throws IOException {
        provideTestData("a customer signed up on storefront");
        clearCache();
        refresh();

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setEmail_logIn(customerEmail);
        p.setPassword_logIn("78qa22!#");
        p.clickLogInBtn();
        shouldMatchText(p.userMenuBtn_sf(), customerName.toUpperCase(), "Failed to log out");
        p.logOut();

        p.logInLnk().shouldBe(visible);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Log Out")
    @Description("User is redirected to the home page upon logging out under /profile")
    public void logOutUnderProfile() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openProfile();
        p.logOut();

        p.logInLnk().shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl, timeout);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Log Out")
    @Description("User is redirected to home page if logging out at order confirmation page")
    public void logOutAtOrderConfirmation() throws IOException {
        provideTestData("a customer ready to checkout");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.clickPlaceOrderBtn();
        shouldBeVisible(p.confirmationOrderNumber(), "Probably order isn't placed");

        p.logOut();
        assertUrl(getUrl(), storefrontUrl, timeout);
    }

}