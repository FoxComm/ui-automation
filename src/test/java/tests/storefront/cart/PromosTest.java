package tests.storefront.cart;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.refresh;
import static testdata.api.collection.Coupons.archiveCoupon;
import static testdata.api.collection.Coupons.setCouponState;
import static testdata.api.collection.Promotions.archivePromo;
import static testdata.api.collection.Promotions.setPromoState;

public class PromosTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Promos")
    @Description("As a registered customer, can add a coupon to cart")
    public void canApplyCoupon_registeredCustomer() throws IOException {
        provideTestData("a customer signed up on storefront, product<active>, coupon<any, single code>");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.applyCoupon(singleCouponCode);

        p.appliedCoupon().shouldBe(visible);
        p.subTotal_cart().shouldHave(text("$50.00"));
        p.adjustmentTotal_cart().shouldHave(text("– $5.00"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Promos")
    @Description("As a guest, can add a coupon to cart")
    public void canApplyCoupon_guest() throws IOException {
        provideTestData("product<active>, coupon<any, single code>");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.applyCoupon(singleCouponCode);

        p.appliedCoupon().shouldBe(visible);
        p.subTotal_cart().shouldHave(text("$50.00"));
        p.adjustmentTotal_cart().shouldHave(text("– $5.00"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Promos")
    @Description("Coupon can be removed from cart")
    public void canRemoveCoupon() throws IOException {
        provideTestData("a customer signed up on storefront with product and coupon<any, single code> in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.removeCoupon();

        p.appliedCoupon().shouldNotBe(visible);
        p.subTotal_cart().shouldHave(text("$50.00"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Cart : Promos")
    @Description("Coupon is cleared from cart once cart passes checkout")
    public void couponIsAutoRemovedAfterCheckout() throws IOException {
        provideTestData("a customer ready to checkout");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.clickPlaceOrderBtn();

        shouldBeVisible(p.confirmationOrderNumber(), "Order confirmation number isn't visible -- checkout might've failed");
        p.openCart();
        p.appliedCoupon().shouldNotBe(visible);
    }

    // finish setPromoState() once new discounts will land
    @Test(priority = 5, enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Promos")
    @Description("Coupon code i auto-removed from cart once promo state changes to 'Inactive'")
    public void couponIsAutoRemoved_promoInactive() throws IOException {
        provideTestData("a storefront signed up customer, active product in cart, coupon<any, single code>");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.appliedCoupon().shouldBe(visible);
        setPromoState(promotionId, "inactive");
        refresh();
        p.openCart();

        p.appliedCoupon().shouldNotBe(visible);
    }

    // finish setCouponState() once new discounts will land
    @Test(priority = 6, enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Promos")
    @Description("Coupon code is auto-removed from cart once coupon state changes to 'Inactive'")
    public void couponIsAutoRemoved_couponInactive() throws IOException {
        provideTestData("a storefront signed up customer, active product in cart, coupon<any, single code>");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.appliedCoupon().shouldBe(visible);
        setCouponState(couponId, "inactive");
        refresh();
        p.openCart();

        p.appliedCoupon().shouldNotBe(visible);
    }

    // finish activePromo() once new discounts will land
    @Test(priority = 7, enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Promos")
    @Description("Coupon code is auto-removed from cart if promo gets archived")
    public void couponIsAutoRemoved_promoArchived() throws IOException {
        provideTestData("a storefront signed up customer, active product in cart, coupon<any, single code>");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.appliedCoupon().shouldBe(visible);
        archivePromo(promotionId);
        refresh();
        p.openCart();

        p.appliedCoupon().shouldNotBe(visible);
    }

    // finish activeCoupon() once new discounts will land
    @Test(priority = 8, enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Promos")
    @Description("Coupon code is auto-removed from cart if coupon gets archived")
    public void couponIsAutoRemoved_couponArchived() throws IOException {
        provideTestData("a storefront signed up customer, active product in cart, coupon<any, single code>");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.appliedCoupon().shouldBe(visible);
        archiveCoupon(couponId);
        refresh();
        p.openCart();

        p.appliedCoupon().shouldNotBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
