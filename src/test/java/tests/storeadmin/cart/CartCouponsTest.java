package tests.storeadmin.cart;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CartPage;
import pages.admin.LoginPage;
import pages.admin.OrderDetailsPage;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static testdata.api.collection.SharedSearch.deleteSearch;

public class CartCouponsTest extends Preconditions {

    private CartPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

//    @Test(priority = 1)
//    public void applyCoupon_singleCode() throws IOException {
//        provideTestData("cart<1 SKU>, coupon<any, single code>");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        p.clickEditBtn("Coupons");
//        p.addCouponCode(singleCouponCode);
//        p.clickApplyBtn();
//
//        p.appliedCoupon(singleCouponCode).shouldBe(visible);
//        p.grandTotal().shouldHave(text("$135.00"));
//    }
//
//    @Test(priority = 2)
//    public void removeCoupon() throws IOException {
//        provideTestData("cart<1 SKU, coupon applied>; coupon<any, single code>");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        p.clickEditBtn("Coupons");
//        p.clickRemoveCouponBtn();
//        p.clickDoneBtn("Coupons");
//
//        p.noCouponMsg().shouldBe(visible);
//        p.noDiscountsMsg().shouldBe(visible);
//        p.grandTotal().shouldHave(text("$150.00"));
//    }
//
//    @Test(priority = 3)
//    @Description("Assert that coupon code is automatically removed from cart after checkout")
//    public void couponRemovedAfterCheckout() throws IOException {
//        provideTestData("cart<1 SKU in stock, shipAddress, shipMethod, coupon applied, payMethod[SC]>; coupon<any, single code>");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        p.clickEditBtn("Coupons");
//        p.addCouponCode(singleCouponCode);
//        p.clickApplyBtn();
//        p.clickPlaceOderBtn();
//        shouldHaveText(p.orderState(), "Remorse Hold", "Checkout failed");
//        open(adminUrl + "/customers/" + customerId + "/cart");
//        p.clickEditCartBtn();
//
//        p.noCouponMsg().shouldBe(visible);
//        p.noDiscountsMsg().shouldBe(visible);
//    }
//
//    @Test(priority = 4)
//    public void applyCoupon_fail() throws IOException {
//        provideTestData("empty cart");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        p.clickEditBtn("Coupons");
//        p.addCouponCode("incorrect-coupon-code");
//        p.clickApplyBtn();
//
//        p.errorMsg("This coupon code does not exist.").shouldBe(visible);
//    }

    @Test(priority = 5)
    public void applyCoupon_bulkGenerated() throws IOException {
        provideTestData("cart<1 SKU>; coupon<any, bulk generated codes>");
        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);

        p.clickEditBtn("Coupons");
        p.addCouponCode(bulkCodes.get(0));
        p.clickApplyBtn();

        p.appliedCoupon(bulkCodes.get(0)).shouldBe(visible);
        p.grandTotal().shouldHave(text("$135.00"));
    }

//    @Test(priority = 6)
//    public void applyCoupon_itemsQualifier() throws IOException {
//        provideTestData("cart<1 SKU>; coupon<items -- no qualifier/percent off, single code>");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        p.clickEditBtn("Coupons");
//        p.addCouponCode(singleCouponCode);
//        p.clickApplyBtn();
//
//        p.appliedCoupon(singleCouponCode).shouldBe(visible);
//        p.grandTotal().shouldHave(text("45.00"));
//    }

    @AfterTest
    public void cleanUp() throws IOException {
        if (searchCode != null) {
            deleteSearch(searchCode);
            searchCode = "";
        }
    }

}
