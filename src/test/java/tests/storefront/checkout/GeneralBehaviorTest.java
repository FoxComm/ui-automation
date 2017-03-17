package tests.storefront.checkout;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;

public class GeneralBehaviorTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Can't proceed to checkout if cart is empty")
    public void noLineItems_checkoutBtnDisabled() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();

        p.checkoutBtn_cart().shouldBe(disabled);
    }

    @Test(priority = 2)
    @Description("Customer is returned to 2nd step after leaving checkout with shipping address applied")
    public void forceReSelectDelivery_leaveCheckout() throws IOException {
        provideTestData("a storefront signed up customer, has shipping address and product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.setShipMethod("1");
        p.clickContinueBtn();
        scrollPageUp();
        p.clickLogo();
        p.openCart();
        p.clickCheckoutBtn_cart();

        p.selectShipMethodRbtn("1").shouldBe(selected);
        p.assertCheckoutStepActive("Delivery");
    }

    @Test(priority = 3)
    @Description("Can submit other shipping address for checkout")
    public void changeShipAddress() throws IOException {
        provideTestData("a storefront signed up customer, a cart with 1 product, 2 shipping addresses, NO default address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipAddress("1");
        p.clickContinueBtn();
        p.editStep("shipping");
        p.setShipAddress("2");
        p.clickContinueBtn();

        p.appliedShipAddress_name().shouldHave(text("John Doe"));

    }

    @Test(priority = 4)
    @Description("Can edit submitted shipping address")
    public void editSubmittedShipAddress() throws IOException {
        provideTestData("a storefront signed up customer, with shipping address submitted and product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.editStep("shipping");
        p.editAddress_checkout("1");
        p.setName_shipAddress("Edited Name");
        p.setAddress1("Edited Address 1");
        p.setaddress2("Edited Address 2");
        p.setZIP("90210");
        p.setPhoneNumber("1112223333");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();

        p.appliedShipAddress_name().shouldHave(text("Edited Name"));
        p.appliedShipAddress_address1().shouldHave(text("Edited Address 1"));
        p.appliedShipAddress_address2().shouldHave(text("Edited Address 2"));
        p.appliedShipAddress_zip().shouldHave(text("90210"));
        p.appliedShipAddress_city().shouldHave(text("Beverly Hills" + ","));
        p.appliedShipAddress_state().shouldHave(text("California"));
        p.appliedShipAddress_phoneNumber().shouldHave(text("1112223333"));
    }

    @Test(priority = 5)
    @Description("Customer is forced to re-pick a shipping method after editing Shipping Address, even if it was submitted unchanged")
    public void forceReSelectDelivery_editAddress() throws IOException {
        provideTestData("a customer ready to checkout");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.setShipMethod(p.shippingMethods().size());
        p.setCreditCard("1");
        p.editStep("shipping");
        p.clickContinueBtn();

        p.assertCheckoutStepActive("Delivery");
    }

    @Test(priority = 6)
    @Description("\"Total\" is populated with `customersExpenses` value, which is how much customer will pay with real cash (with a credit card)")
    public void customersExpensesPopulatesGrandTotal() throws IOException {
        provideTestData("a customer ready for checkout, gift card is applied to cart as a payment method");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        getCartTotals(cartId);

        p.grandTotal().shouldHave(text("$" + totalToString(customersExpenses)));
    }

    @Test(priority = 7)
    @Description("A \"Happy Path\" checkout")
    public void happyPath() throws IOException {
        provideTestData("happy path");

        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openPDP(productName);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Block 42", "98115", "9879879876");
        p.clickSaveAddressBtn();
        p.selectAddressRbtn("1").shouldBe(selected);
        p.clickContinueBtn();
        p.setShipMethod(p.shippingMethods().size());
        p.clickContinueBtn();
        p.fillOutCardForm("John Doe", "4242424242424242", "123", "08", "2020", false);
        p.clickSaveCardBtn();
        p.clickPlaceOrderBtn();

        p.confirmationOrderNumber().shouldBe(visible);
        p.takeMeHomeBtn().shouldBe(visible);
        assertUrl(getUrl(), storefrontUrl + "checkout/done");
    }

    @Test(priority = 8)
    @Description("Can't checkout as a blacklisted customer")
    public void blacklistedCustomerCantCheckout() throws IOException {
        provideTestData("a storefront signed up blacklisted customer ready for checkout");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.setShipMethod(p.shippingMethods().size());
        p.clickPlaceOrderBtn();

        scrollPageUp();
        p.checkoutError("Your account has been blocked from making purchases on this site").shouldBe(visible);
    }


    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
