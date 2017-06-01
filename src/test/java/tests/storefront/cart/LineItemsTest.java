package tests.storefront.cart;

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

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.timeout;

public class LineItemsTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.restartBrowser();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("Added product appears in cart")
    public void addProductToCart_lineItemIsVisible() throws IOException {
        provideTestData("registered customer, active product on storefront");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.clickAddToCartBtn();

        p.lineItemByName_cart(productTitle).shouldBe(visible);
        p.subTotal_cart().shouldHave(text("$50.00"));

        p.closeCart();
        p.cartQty().shouldHave(text("1"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("Line items units indicator shows correct amount of total line items units in cart")
    public void addProductToCart_indicatorUpdated() throws IOException {
        provideTestData("registered customer, 2 active products on storefront");

        p = openPage(storefrontUrl + "/products/" + productSlugs.get(0), StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.setQty_pdp("2");
        p.clickAddToCartBtn();
        p.closeCart();
        p = openPage(storefrontUrl + "/products/" + productSlugs.get(1), StorefrontTPGBasePage.class);
        p.setQty_pdp("3");
        p.clickAddToCartBtn();
        p.closeCart();
        p.cartQty().shouldHave(text("5"));

        p.openCart();
        p.removeLineItem("1");
        p.closeCart();
        p.cartQty().shouldNotHave(text("5"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("Can remove line item from cart")
    public void removeProductFromCart() throws IOException {
        provideTestData("registered customer, active product in cart");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.removeLineItem("1");
        p.closeCart();

        p.cartQty().shouldHave(text("0"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("Gift card appears in cart as a line item")
    public void addGiftCardToCart() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl + "/GIFT-CARDS", StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.setPriceSelector("$10.00");
        p.setRecipientName("John Smith");
        p.setRecipientEmail("qatest2278@gmail.com");
        p.setGiftCardMsg("test message");
        p.setSenderName("John Doe");
        p.clickAddToCartBtn();

        p.lineItemsAmount().shouldHaveSize(1);
    }

    //TODO: provideTestData() isn't finished for this test -- blocked by issue https://trello.com/c/J4TI8Dtx
    @Test(priority = 5, enabled = false)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("Gift card can be removed from cart as a regular line item")
    public void removeGCLineItemFromCart() throws IOException {
        provideTestData("a customer with GC in cart as a line item");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.removeLineItem("1");

        p.lineItemsAmount().shouldHaveSize(0);
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("\"Checkout\" btn is locked if there are no line items in cart and it gets unlocked if there's at least 1 line item")
    public void checkoutBtnBehavior_registeredCustomer() throws IOException {
        provideTestData("registered customer, active product on storefront");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.checkoutBtn_cart().shouldBe(disabled);
        p.closeCart();
        p.clickAddToCartBtn();
        p.checkoutBtn_cart().shouldBe(enabled);
        p.clickCheckoutBtn_cart();

        p.grandTotal().shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl + "/checkout", timeout);
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("Cart is synchronized on logIn and logOut")
    public void cartIsSynchronized() throws IOException {
        provideTestData("a customer signed up on storefront with product and coupon<any, single code> in cart");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        shouldHaveText(p.cartQty(), "0", "Incorrect line item indicator value");
        shouldNotBeVisible(p.appliedCoupon(), "Cart has a coupon applied");
        p.logIn(customerEmail, "78qa22!#");
        shouldHaveText(p.cartQty(), "1", "Incorrect line item indicator value");
        p.openCart();
        p.appliedCoupon().shouldBe(visible);
        p.closeCart();
        p.logOut();

        p.cartQty().shouldHave(text("0"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(0);
        p.appliedCoupon().shouldNotBe(visible);
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("Cart is cleaned after checkout")
    public void noLineItemsAfterCheckout() throws IOException {
        provideTestData("a customer ready to checkout");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.clickPlaceOrderBtn();

        p.cartQty().shouldHave(text("0"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(0);
    }

}
