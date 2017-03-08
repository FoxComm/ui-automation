package tests.storefront.cart;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import pages.StorefrontPage;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class CartTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Can add product to cart")
    public void addProductToCart() throws IOException {
        provideTestData("registered customer, active product on storefront");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openPDP(productName);
        p.clickAddToCartBtn();

        p.lineItemByName_cart(productName).shouldBe(visible);
        p.cartSubtotal().shouldHave(text("$50.00"));
        p.closeCart();
        p.cartQty().shouldHave(text("1"));
    }

    @Test(priority = 2)
    @Description("Can remove line item from cart")
    public void removeProductFromCart() throws IOException {
        provideTestData("registered customer, active product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.removeLineItem("1");
        p.closeCart();

        p.cartQty().shouldHave(text("0"));
    }

    @Test(priority = 3)
    @Description("As a registered customer, can proceed to checkout if cart has at least 1 line item")
    public void canProceedToCheckout_registeredCustomer() throws IOException {
        provideTestData("registered customer, active product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();

        p.orderSummary().shouldBe(visible);
    }

    @Test(priority = 4)
    @Description("As a guest, can proceed to checkout if cart has at least 1 line item")
    public void canProceedToCheckout_guest() throws IOException {
        provideTestData("registered customer, active product on storefront");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.openPDP(productName);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.setGuestEmailFld("qatest2278+" + generateRandomID() + "@gmail.com");
        p.clickCheckoutBtn_guestAuth();

        p.orderSummary().shouldBe(visible);
    }

    @Test(priority = 5)
    @Description("Cart is synchronized on logIn and logOut")
    public void cartIsSynchronized() throws IOException {
        provideTestData("registered customer, active product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        shouldHaveText(p.cartQty(), "0", "Incorrect line item indicator value");
        p.logIn(customerEmail, "78qa22!#");
        shouldHaveText(p.cartQty(), "1", "Incorrect line item indicator value");
        p.selectInUserMenu("LOG OUT");

        p.cartQty().shouldHave(text("0"));
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
