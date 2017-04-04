package tests.storefront.navigation;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;

public class NavigationTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

//    @Test(priority = 1, dataProvider = "homePageBtns")
//    @Description("Buttons on home page redirects where they should")
//    public void homePageBtnsRedirection(String btnTitle, String btnRedirectSlug, String expectedUrl) {
//        p = openPage(storefrontUrl, StorefrontPage.class);
//        p.clickBtnAtHome(btnTitle, btnRedirectSlug);
//
//        assertUrl(getUrl(), expectedUrl);
//    }

    @Test(priority = 2)
    @Description("Can switch between checkout steps using pannel on top of the page")
    public void canSwitchBetweenCheckoutSteps() throws IOException {
        provideTestData("a customer ready to checkout");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.proceedToCheckout();
        p.clickContinueBtn();

        p.placeOrderBtn().shouldBe(visible);
        p.activeCheckoutStep("Billing").shouldBe(visible);

        p.switchToStep("Delivery");
        p.selectShipMethodRbtn("1").shouldBe(visible);
        p.activeCheckoutStep("Delivery").shouldBe(visible);

        p.switchToStep("Shipping");
        p.addAddressBtn().shouldBe(visible);
        p.activeCheckoutStep("Shipping");

        p.clickContinueBtn();
        p.selectShipMethodRbtn("1").shouldBe(visible);
        p.activeCheckoutStep("Delivery");

        p.clickContinueBtn();
        p.placeOrderBtn().shouldBe(visible);
        p.activeCheckoutStep("Billing").shouldBe(visible);
    }


    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
