package tests.storefront.cart;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import pages.StorefrontPage;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;

public class CartTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }



    @Test(priority = 4)
    @Description("As a guest, can proceed to checkout if cart has at least 1 line item")
    public void canProceedToCheckout_guest() throws IOException {
        provideTestData("registered customer, active product on storefront");

        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        p.openPDP(productName);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmailFld("qatest2278+" + generateRandomID() + "@gmail.com");
        p.clickCheckoutBtn_guestAuth();

        p.orderSummary().shouldBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
