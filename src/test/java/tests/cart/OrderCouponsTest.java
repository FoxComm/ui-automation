package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.xpath;

public class OrderCouponsTest extends DataProvider {

    private CartPage p = openPage(adminUrl + "/orders/" + orderId, CartPage.class);

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

    @Test(priority = 1)
    public void addCoupon() throws IOException {

        provideTestData("a cart and a single code coupon");
        p = openPage(adminUrl + "/orders/" + orderId, CartPage.class);

        p.clickEditBtn("Coupons");
        p.addCouponCode("newcpn-12345");
        p.clickApplyBtn();
        p.clickDoneBtn("Coupons");

        p.appliedCoupon("newcpn-12345").shouldBe(visible);

    }

    @Test(priority = 2)
    public void addCoupon_checkTotal() throws IOException {
        provideTestData("a cart with 1 SKU and single code coupon applied");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        p.grandTotal().shouldHave(text("$270.00"));
    }

    @Test(priority = 2)
    public void removeCoupon() throws IOException {
        provideTestData("a cart with 1 SKU and single code coupon applied");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        p.clickEditBtn("Coupons");
        p.clickRemoveCouponBtn();
        p.clickDoneBtn("Coupons");

        p.noCouponMsg().shouldBe(visible);
        p.grandTotal().shouldHave(text("$300.00"));
    }


}
