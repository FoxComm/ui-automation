package tests.storefront.checkout;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class ShippingMethodTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Can select one of available shipping methods predefined by retailer")
    public void canSelectShipMethod() throws IOException {
        provideTestData("a storefront signed up customer with active product in cart and applied shipping address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.setShipMethod(p.shippingMethods().size());
        p.clickContinueBtn();

        p.appliedShipMethod_name().shouldBe(visible);
    }

    //TODO: provideTestData() doesn't exist for this test -- blocked by issue https://trello.com/c/J4TI8Dtx
    @Test(priority = 2, enabled = false)
    @Description("If cart has only gift card as a line item, the only availble shipping method is 'Email' with price '$0.00'")
    public void shipMethodForGC() throws IOException {
        provideTestData("a storefront signed up customer with GC in cart as a line item and applied shipping address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.clickContinueBtn();

        p.appliedShipMethod_name().shouldHave(text("Email"));
        p.appliedShipMethod_price().shouldHave(text("$0.00"));
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
