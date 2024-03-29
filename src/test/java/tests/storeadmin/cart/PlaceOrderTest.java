package tests.storeadmin.cart;

import org.testng.annotations.BeforeClass;
import pages.admin.LoginPage;
import pages.admin.OrderDetailsPage;
import testdata.Preconditions;

import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class PlaceOrderTest extends Preconditions {

    private OrderDetailsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

//    TODO: Move test to API level
//    @Test(priority = 1)
//    public void placeOrder_CC() throws IOException {
//
//        provideTestData("cart with 1 item, shipping method, and credit card payment");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        p.assertNoWarnings();
//        p.clickPlaceOderBtn();
//        p.assertOrderState("Remorse Hold");
//
//    }

//    TODO: Move test to API level
//    @Test(priority = 2)
//    public void placeOrder_SC() throws IOException {
//
//        provideTestData("cart with 1 item, shipping method and issued SC");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        shouldBeVisible(p.cartSummary(), "Failed to open cart page");
//        int amountToUse = (int) (p.grandTotalVal() * 100);
//
//        setPayment_storeCredit(cartId, amountToUse);
//        refresh();
//        p.assertNoWarnings();
//        p.clickPlaceOderBtn();
//        p.assertOrderState("Remorse Hold");
//
//    }

//    TODO: Move test to API level
//    @Test(priority = 3)
//    public void placeOrder_GC() throws IOException {
//
//        provideTestData("cart with 1 item, shipping method and issued GC");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        shouldBeVisible(p.cartSummary(), "Failed to open cart page");
//        int amountToUse = (int) (p.grandTotalVal() * 100);
//
//        setPayment_giftCard(cartId, gcCode, amountToUse);
//        refresh();
//        p.assertNoWarnings();
//        p.clickPlaceOderBtn();
//        p.assertOrderState("Remorse Hold");
//
//    }

//    TODO: Move test to API level
//    @Test(priority = 4)
//    public void placeOrder_CC_SC() throws IOException {
//
//        provideTestData("cart with 1 item, shipping method, credit card payment and issued SC");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        shouldBeVisible(p.cartSummary(), "Failed to open cart page");
//        int amountToUse = (int) (p.grandTotalVal() / 2 * 100);
//
//        setPayment_storeCredit(cartId, amountToUse);
//        refresh();
//        p.assertNoWarnings();
//        p.clickPlaceOderBtn();
//        p.assertOrderState("Remorse Hold");
//
//    }

//    TODO: Move test to API level
//    @Test(priority = 5)
//    public void placeOrder_CC_GC() throws IOException {
//
//        provideTestData("cart with 1 item, shipping method, credit card payment and issued GC");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        shouldBeVisible(p.cartSummary(), "Failed to open cart page");
//        int amountToUse = (int) (p.grandTotalVal() / 2 * 100);
//
//        setPayment_giftCard(cartId, gcCode, amountToUse);
//        refresh();
//        p.assertNoWarnings();
//        p.clickPlaceOderBtn();
//        p.assertOrderState("Remorse Hold");
//
//    }

//    TODO: Move test to API level
//    @Test(priority = 6)
//    public void placeOrder_SC_GC() throws IOException {
//
//        provideTestData("cart with 1 item, shipping method, issued SC and GC");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        shouldBeVisible(p.cartSummary(), "Failed to open cart page");
//        int amountToUse_GC = (int) (p.grandTotalVal() / 2 * 100);
//        int amountToUse_SC = calcAmount(amountToUse_GC, p.grandTotalVal());
//
//        setPayment_giftCard(cartId, gcCode, amountToUse_GC);
//        setPayment_storeCredit(cartId, amountToUse_SC);
//        refresh();
//        p.assertNoWarnings();
//        p.clickPlaceOderBtn();
//        p.assertOrderState("Remorse Hold");
//
//    }

//    TODO: Move test to API level
//    @Test(priority = 6)
//    public void placeOrder_coupon() throws IOException {
//
//        provideTestData("cart with 1 item, shipping method, CC and coupon");
//        p = openPage(adminUrl + "/carts/" + cartId, OrderDetailsPage.class);
//
//        assertTrue(p.couponName().is(visible),
//                "Failed to apply coupon to an order.");
//        p.assertNoWarnings();
//        click( p.placeOrderBtn() );
//        p.assertOrderState("Remorse Hold");
//
//    }

//    TODO: User is redirected to order details page after checkout.

//    TODO: Cart with order number isn't displayed on the table in catalog_view in "Carts" category.

//    TODO: Cart isn't displayed on the table in category_view after checkout (cart uses the same number as a just placed order.

}
