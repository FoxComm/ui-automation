package tests.storefront.guestsession;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.refresh;
import static org.testng.Assert.assertEquals;
import static testdata.api.collection.Auth.loginAsAdmin;
import static testdata.api.collection.Cart.getGuestOrderEmail;

public class GuestCheckoutTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Before checkout guest customer can either checkout with guest email or sign in as a registered customer")
    public void guestCheckoutAuth() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();

        p.logInBtn().shouldBe(visible);
        p.signUpLnk().shouldBe(visible);
        p.checkoutBtn_guestAuth().shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl + "/checkout");
    }

    @Test(priority = 2)
    @Description("Can proceed to checkout with a non-taken email")
    public void proceedToCheckout_nonTakenEmail() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_auth("qatest2278+" + generateRandomID() + "@gmail.com");
        p.clickCheckoutBtn_guestAuth();

        p.grandTotal().shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl + "/checkout");
    }

    @Test(priority = 3)
    @Description("Can proceed to checkout with an email taken by a registered customer")
    public void proceedToCheckout_takenEmail() throws IOException {
        provideTestData("registered customer, active product on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_auth(customerEmail);
        p.clickCheckoutBtn_guestAuth();

        p.grandTotal().shouldBe(visible);
        p.lineItem_checkout(productTitle).shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl + "/checkout");
    }

    @Test(priority = 4)
    @Description("If customer signs in at pre-checkout page, guest cart line items are merged into registered customer cart")
    public void guestCartMergedIntoRegisteredOnCheckout_lineItems() throws IOException {
        provideTestData("a customer ready to checkout, 2 active products, 1 in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(products.get(1));
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.logIn_guestAuth(customerEmail, "78qa22!#");

        p.lineItems_checkout().shouldHaveSize(2);
        p.lineItem_checkout(products.get(0)).shouldBe(visible);
        p.lineItem_checkout(products.get(1)).shouldBe(visible);
    }

    @Test(priority = 5)
    @Description("Coupon from guest cart is merged into registered customer cart on checkout sign in")
    public void guestCartMergedIntoRegisteredOnCheckout_coupon() throws IOException {
        provideTestData("a customer ready to checkout, single code coupon code");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
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
    @Description("Line items in guest cart are saved if customer signs up right before proceeding to checkout")
    public void checkoutSignUp_guestCartSaved_lineItems() throws IOException {
        provideTestData("an active product visible on storefront");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.clickSignUpLnk();
        p.setName("Test Buddy " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
        p.clickSignUpBtn();

        p.lineItem_checkout(productTitle).shouldBe(visible);
        p.lineItems_checkout().shouldHaveSize(1);
    }

    @Test(priority = 7)
    @Description("Coupon in guest cart is saved if customer signs up right before proceeding to checkout")
    public void checkoutSignUp_guestCartSaved_coupon() throws IOException {
        provideTestData("product<active>, coupon<any, single code>");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.applyCoupon(singleCouponCode);
        p.clickCheckoutBtn_cart();
        p.clickSignUpLnk();
        p.setName("Test Buddy " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
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
    @Description("Line items in guest cart are saved if customer signs up while browsing the store")
    public void guestCartIsSavedOnSignUp_storeBrowsing_lineItems() throws IOException {
        provideTestData("an active product visible on storefront");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.closeCart();
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Test Buddy " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
        p.clickSignUpBtn();

        p.cartQty().shouldHave(text("1"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(1);
        p.lineItemByName_cart(productTitle).shouldBe(visible);
    }

    @Test(priority = 9)
    @Description("Coupon in guest cart is saved if customer signs up while browsing the store")
    public void guestCartIsSavedOnSignUp_storeBrowsing_coupon() throws IOException {
        provideTestData("product<active>, coupon<any, single code>");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.applyCoupon(singleCouponCode);
        shouldBeVisible(p.appliedCoupon(), "");
        p.closeCart();
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Test Buddy " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
        p.clickSignUpBtn();

        p.openCart();
        p.appliedCoupon().shouldBe(visible);
    }

    // broken due to bug with guest & registered cart sync
    @Test(priority = 10)
    @Description("Gift card applied to cart as a payment during guest checkout should remain in the cart if guest signs up with the same email")
    public void guestCartIsSavedOnSignUp_paymentGiftCard() throws IOException {
        provideTestData("an active product, a gift card");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_auth("qatest2278+" + randomId + "@gmail.com");
        p.clickCheckoutBtn_guestAuth();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.clickSaveAddressBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.redeemGiftCard(gcCode);

        scrollPageUp();
        p.clickLogo();
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.fillOutSignUpForm("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com", "78qa22!#");
        p.clickSignUpBtn();
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.expandGiftCardBlock();

        p.appliedGiftCard().shouldBe(visible);
    }

    // broken due to bug with guest & registered cart sync
    @Test(priority = 11)
    @Description("If customer has added a shipping address at checkout, then left checkout and signed up -- shipping address should be saved")
    public void guestAddressIsSavedAfterSignUp() throws IOException {
        provideTestData("an active product visible on storefront");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_auth("qatest2278+" + randomId + "@gmail.com");
        p.clickCheckoutBtn_guestAuth();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.clickSaveAddressBtn();

        scrollPageUp();
        p.clickLogo();
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.fillOutSignUpForm("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com", "78qa22!#");
        p.clickSignUpBtn();
        p.openCart();
        p.clickCheckoutBtn_cart();

        p.assertCheckoutStepActive("delivery");
        p.appliedShipAddress_name().shouldHave(text("Test Buddy " + randomId));
    }

    @Test(priority = 12)
    @Description("A guest customer's email can be changed before placing the order")
    public void canEditGuestEmailBeforePlacingOrder() throws IOException {
        provideTestData("an active product visible on storefront");
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_auth("qatest2278@gmail.com");
        p.clickCheckoutBtn_guestAuth();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.setGuestEmail_checkout("qatest2278+" + randomId + "@gmail.com");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.fillOutCardForm("Test Buddy + " + randomId, "4242424242424242", "123", "10", "2020", false);
        p.clickPlaceOrderBtn();
        String orderNumber = p.confirmationOrderNumber().text();

        loginAsAdmin(adminEmail, adminPassword, adminOrg);
        refresh();
        getGuestOrderEmail(orderNumber);
        assertEquals(guestOrderEmail, "qatest2278+" + randomId + "@gmail.com");
    }

    @Test(priority = 13)
    @Description("Can sign in using with a registered customer email used for guest checkout")
    public void canSignIn_guestCheckoutWithTakenEmail() throws IOException {
        provideTestData("a storefront registered customer, an active product");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        p.openPDP(productTitle);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmail_auth(customerEmail);
        p.clickCheckoutBtn_guestAuth();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Suit 42", "98115", "9879879876");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.fillOutCardForm("Test Buddy + " + randomId, "4242424242424242", "123", "10", "2020", false);
        p.clickPlaceOrderBtn();
        shouldBeVisible(p.confirmationOrderNumber(), "Failed to place order");
        p.clickLogo();
        p.logIn(customerEmail, "78qa22!#");

        p.userMenuBtn_sf().should(matchText(customerName.toUpperCase()));
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
