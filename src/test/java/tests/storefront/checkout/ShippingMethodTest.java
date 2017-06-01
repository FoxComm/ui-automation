package tests.storefront.checkout;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.StorefrontTPGBasePage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class ShippingMethodTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_after() {
        restartBrowser();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Checkout : Ship Method")
    @Description("Can select one of available shipping methods predefined by retailer")
    public void canSelectShipMethod() throws IOException {
        provideTestData("a storefront signed up customer with active product in cart and applied shipping address");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.setShipMethod(p.shippingMethods().size());
        p.clickContinueBtn();

        p.name_appliedShipMethod().shouldBe(visible);
    }

    //TODO: provideTestData() doesn't exist for this test -- blocked by issue https://trello.com/c/J4TI8Dtx
    @Test(priority = 2, enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Checkout : Ship Method")
    @Description("If cart has only gift card as a line item, the only availble shipping method is 'Email' with price '$0.00'")
    public void shipMethodForGC() throws IOException {
        provideTestData("a storefront signed up customer with GC in cart as a line item and applied shipping address");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.clickContinueBtn();

        p.name_appliedShipMethod().shouldHave(text("Email"));
        p.price_appliedShipMethod().shouldHave(text("$0.00"));
    }

}
