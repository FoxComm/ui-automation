package tests.storefront.guestsession;

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
import static com.codeborne.selenide.Selenide.refresh;
import static org.testng.Assert.assertEquals;
import static testdata.api.collection.Auth.loginAsAdmin;
import static testdata.api.collection.Cart.getGuestOrderEmail;

public class GuestCheckoutTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.restartBrowser();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Before checkout guest customer can either checkout with guest email or sign in & checkout")
    public void guestCheckoutAuth() throws IOException {
        provideTestData("active product, has tag, active SKU, has sellable stockitems");

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();

        p.logInBtn().shouldBe(visible);
        p.checkoutBtn_guestAuth().shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl + "/checkout", timeout);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Can proceed to checkout with a non-taken email")
    public void proceedToCheckout_nonTakenEmail() throws IOException {
        provideTestData("active product, has tag, active SKU, has sellable stockitems");

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_guestAuth("qatest2278+" + generateRandomID() + "@gmail.com");
        p.clickCheckoutBtn_guestAuth();

        p.grandTotal().shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl + "/checkout", timeout);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Can proceed to checkout with an email taken by a registered customer")
    public void proceedToCheckout_takenEmail() throws IOException {
        provideTestData("registered customer, active product on storefront");

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_guestAuth(customerEmail);
        p.clickCheckoutBtn_guestAuth();

        p.grandTotal().shouldBe(visible);
        p.lineItem_checkout(productTitle).shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl + "/checkout", timeout);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("If customer signs in at pre-checkout page, guest cart line items are merged into registered customer cart")
    public void guestCartMergedIntoRegisteredOnCheckout_lineItems() throws IOException {
        provideTestData("a customer ready to checkout, 2 active products, 1 in cart");

        p = openPage(storefrontUrl+"/products/"+productSlugs.get(1), StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.logIn_guestAuth(customerEmail, "78qa22!#");

        p.lineItems_checkout().shouldHaveSize(2);
        p.lineItem_checkout(products.get(0)).shouldBe(visible);
        p.lineItem_checkout(products.get(1)).shouldBe(visible);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Coupon from guest cart is merged into registered customer cart on checkout sign in")
    public void guestCartMergedIntoRegisteredOnCheckout_coupon() throws IOException {
        provideTestData("a customer ready to checkout, single code coupon code");

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.applyCoupon(singleCouponCode);
        p.clickCheckoutBtn_cart();
        p.logIn_guestAuth(customerEmail, "78qa22!#");
        p.clickContinueBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.expandCouponCodeBlock();

        p.appliedCoupon().shouldBe(visible);
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Line items in guest cart are saved if customer signs up right before proceeding to checkout")
    public void checkoutSignUp_guestCartSaved_lineItems() throws IOException {
        provideTestData("an active product visible on storefront");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.clickSignUpLnk();
        p.setName("Customer " + randomId);
        p.setEmail_signUp("qatest2278+" + randomId + "@gmail.com");
        p.clickSignUpBtn();

        p.lineItem_checkout(productTitle).shouldBe(visible);
        p.lineItems_checkout().shouldHaveSize(1);
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Coupon in guest cart is saved if customer signs up right before proceeding to checkout")
    public void checkoutSignUp_guestCartSaved_coupon() throws IOException {
        provideTestData("product<active>, coupon<any, single code>");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.applyCoupon(singleCouponCode);
        p.clickCheckoutBtn_cart();
        p.clickSignUpLnk();
        p.setName("Customer " + randomId);
        p.setEmail_signUp("qatest2278+" + randomId + "@gmail.com");
        p.setPassword_signUp("78qa22!#");
        p.clickSignUpBtn();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.clickSaveAddressBtn();
        p.selectAddressRbtn("1").shouldBe(selected);
        p.clickContinueBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.fillOutCardForm("John Doe", "4242424242424242", "123", "09", "2021", false);
        p.clickSaveCardBtn();
        p.selectCardRbtn("1").shouldBe(selected);
        p.expandCouponCodeBlock();

        p.appliedCoupon().shouldBe(visible);
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Line items in guest cart are saved if customer signs up while browsing the store")
    public void guestCartSavedOnSignUp_storeBrowsing_lineItems() throws IOException {
        provideTestData("an active product visible on storefront");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.closeCart();
        p.clickLogInLnk();
        p.setName("Customer " + randomId);
        p.setEmail_signUp("qatest2278+" + randomId + "@gmail.com");
        p.setPassword_signUp("78qa22!#");
        p.clickSignUpBtn();

        p.cartQty().shouldHave(text("1"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(1);
        p.lineItemByName_cart(productTitle).shouldBe(visible);
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Coupon in guest cart is saved if customer signs up while browsing the store")
    public void guestCartSavedOnSignUp_storeBrowsing_coupon() throws IOException {
        provideTestData("product<active>, coupon<any, single code>");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.applyCoupon(singleCouponCode);
        shouldBeVisible(p.appliedCoupon(), "");
        p.closeCart();
        p.clickLogInLnk();
        p.setName("Customer " + randomId);
        p.setEmail_signUp("qatest2278+" + randomId + "@gmail.com");
        p.setPassword_signUp("78qa22!#");
        p.clickSignUpBtn();

        p.openCart();
        p.appliedCoupon().shouldBe(visible);
    }

    @Test(priority = 10)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Gift card applied to cart as a payment during guest checkout, remains in the cart if guest signs up with the same email")
    public void guestCartSavedOnSignUp_paymentGiftCard() throws IOException {
        provideTestData("an active product, a gift card");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_guestAuth("qatest2278+" + randomId + "@gmail.com");
        p.clickCheckoutBtn_guestAuth();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.clickSaveAddressBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.redeemGiftCard(gcCode);

        scrollPageUp();
        p.clickLogo();
        p.clickLogInLnk();
        p.fillOutSignUpForm("Customer " + randomId, "qatest2278+" + randomId + "@gmail.com", "78qa22!#");
        p.clickSignUpBtn();
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.expandGiftCardBlock();

        p.appliedGiftCard().shouldBe(visible);
    }

    @Test(priority = 11)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("If customer has added a shipping address at checkout, then left checkout and signed up -- shipping address should be saved")
    public void guestAddressIsSavedAfterSignUp() throws IOException {
        provideTestData("active product, has tag, active SKU, has sellable stockitems");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_guestAuth("qatest2278+" + randomId + "@gmail.com");
        p.clickCheckoutBtn_guestAuth();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.clickSaveAddressBtn();

        scrollPageUp();
        p.clickLogo();
        p.clickLogInLnk();
        p.fillOutSignUpForm("Customer " + randomId, "qatest2278+" + randomId + "@gmail.com", "78qa22!#");
        p.clickSignUpBtn();
        p.openCart();
        p.clickCheckoutBtn_cart();

        p.activeCheckoutStep("Delivery").shouldBe(visible);
        p.name_appliedShipAddress().shouldHave(text("Customer " + randomId));
    }

    @Test(priority = 12)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("A guest customer's email can be changed before placing the order")
    public void canEditGuestEmailBeforePlacingOrder() throws IOException {
        provideTestData("active product, has tag, active SKU, has sellable stockitems");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_guestAuth("qatest2278@gmail.com");
        p.clickCheckoutBtn_guestAuth();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.setGuestEmail_checkout("qatest2278+" + randomId + "@gmail.com");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.fillOutCardForm("Customer + " + randomId, "4242424242424242", "123", "10", "2020", false);
        p.clickPlaceOrderBtn();
        String orderNumber = p.confirmationOrderNumber().text();

        loginAsAdmin(adminEmail, adminPassword, adminOrg);
        refresh();
        getGuestOrderEmail(orderNumber);
        assertEquals(guestOrderEmail, "qatest2278+" + randomId + "@gmail.com");
    }

    @Test(priority = 13)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Guest Checkout")
    @Description("Can sign in using with a registered customer email used for guest checkout")
    public void canSignIn_guestCheckoutWithTakenEmail() throws IOException {
        provideTestData("a storefront registered customer, an active product");

        p = openPage(storefrontUrl+"/products/"+productSlug, StorefrontTPGBasePage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_guestAuth(customerEmail);
        p.clickCheckoutBtn_guestAuth();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.fillOutCardForm("Customer + " + randomId, "4242424242424242", "123", "10", "2020", false);
        p.clickPlaceOrderBtn();
        shouldBeVisible(p.confirmationOrderNumber(), "Failed to place order");
        p.clickLogo();
        p.logIn(customerEmail, "78qa22!#");

        p.userMenuBtn_sf().should(matchText(customerName.toUpperCase()));
    }

}
