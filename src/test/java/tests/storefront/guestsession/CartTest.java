package tests.storefront.guestsession;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class CartTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Can add products to cart")
    public void addProductToCart() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();

        p.lineItemByName_cart(productTitle).shouldBe(visible);
    }

    @Test(priority = 2)
    @Description("Guest cart doesn't override registered customer cart")
    public void emptyGuestCartDoesntOverrideRegisteredCart() throws IOException {
        provideTestData("registered customer, active product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.removeLineItem("1");
        p.closeCart();
        p.selectInUserMenu("LOG OUT");
        p.logIn(customerEmail, "78qa22!#");

        p.cartQty().shouldHave(text("1"));
        p.openCart();
        p.lineItemByName_cart(productTitle).shouldBe(visible);
    }

    @Test(priority = 3)
    @Description("Guest cart is merged into registered customer cart")
    public void guestCartMergedIntoRegistered() throws IOException {
        provideTestData("a storefront registered customer, 2 active products, 1 in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(products.get(1));
        p.clickAddToCartBtn();
        p.closeCart();
        p.logIn(customerEmail, "78qa22!#");

        p.cartQty().shouldHave(text("2"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(2);
        p.lineItemByName_cart(products.get(0)).shouldBe(visible);
        p.lineItemByName_cart(products.get(1)).shouldBe(visible);
    }

    @Test(priority = 4)
    @Description("Guest customer is terminated after signing in and then out as a registered customer with non-empty cart")
    public void guestCartTerminatedOnSignInThenOut() throws IOException {
        provideTestData("a storefront registered customer, 2 active products, 1 in cart, coupon<no qualifier, 10% off, single code>");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(products.get(1));
        p.clickAddToCartBtn();
        p.applyCoupon(singleCouponCode);
        p.closeCart();
        p.logIn(customerEmail, "78qa22!#");
        shouldNotHaveText(p.cartQty(), "0", "");
        p.selectInUserMenu("LOG OUT");

        p.cartQty().shouldHave(text("0"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(0);
        p.appliedCoupon().shouldNotBe(visible);
    }


    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
