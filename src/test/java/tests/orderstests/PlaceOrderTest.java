package tests.orderstests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static org.testng.Assert.assertTrue;

public class PlaceOrderTest extends DataProvider {

    private OrderDetailsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open("http://admin.stage.foxcommerce.com/");
        if ( (Objects.equals(getUrl(), "http://admin.stage.foxcommerce.com/login")) ) {
            LoginPage loginPage = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void placeOrder_CC() throws IOException {

        provideTestData("cart with 1 item, shipping method, and credit card payment");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.assertNoWarnings();
        click( p.placeOrderBtn() );
        p.assertOrderState("Remorse Hold");

    }

    @Test(priority = 2)
    public void placeOrder_SC() throws IOException {

        provideTestData("cart with 1 item, shipping method and issued SC");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        int amountToUse = (int) (p.grandTotal() * 100);

        setPayment_storeCredit(orderId, amountToUse);
        refresh();
        p.assertNoWarnings();
        click( p.placeOrderBtn() );
        p.assertOrderState("Remorse Hold");

    }

    @Test(priority = 3)
    public void placeOrder_GC() throws IOException {

        provideTestData("cart with 1 item, shipping method and issued GC");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        int amountToUse = (int) (p.grandTotal() * 100);


        setPayment_giftCard(orderId, gcNumber, amountToUse);
        refresh();
        p.assertNoWarnings();
        click( p.placeOrderBtn() );
        p.assertOrderState("Remorse Hold");

    }

    @Test(priority = 4)
    public void placeOrder_CC_SC() throws IOException {

        provideTestData("cart with 1 item, shipping method, credit card payment and issued SC");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        int amountToUse = (int) (p.grandTotal() / 2 * 100);

        setPayment_storeCredit(orderId, amountToUse);
        refresh();
        p.assertNoWarnings();
        click( p.placeOrderBtn() );
        p.assertOrderState("Remorse Hold");

    }

    @Test(priority = 5)
    public void placeOrder_CC_GC() throws IOException {

        provideTestData("cart with 1 item, shipping method, credit card payment and issued GC");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        int amountToUse = (int) (p.grandTotal() / 2 * 100);

        setPayment_giftCard(orderId, gcNumber, amountToUse);
        refresh();
        p.assertNoWarnings();
        click( p.placeOrderBtn() );
        p.assertOrderState("Remorse Hold");

    }

    @Test(priority = 6)
    public void placeOrder_SC_GC() throws IOException {

        provideTestData("cart with 1 item, shipping method, issued SC and GC");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        int amountToUse_GC = (int) (p.grandTotal() / 2 * 100);
        int amountToUse_SC = calcAmount(amountToUse_GC, p.grandTotal());

        setPayment_giftCard(orderId, gcNumber, amountToUse_GC);
        setPayment_storeCredit(orderId, amountToUse_SC);
        refresh();
        p.assertNoWarnings();
        click( p.placeOrderBtn() );
        p.assertOrderState("Remorse Hold");

    }

    @Test(priority = 6)
    public void placeOrder_coupon() throws IOException {

        provideTestData("cart with 1 item, shipping method, CC and coupon");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        assertTrue(p.couponName().is(visible),
                "Failed to apply coupon to an order.");
        p.assertNoWarnings();
        click( p.placeOrderBtn() );
        p.assertOrderState("Remorse Hold");

    }

}
