package tests.orders;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class OrderStateTest extends DataProvider {

    private OrderDetailsPage p;

    @BeforeTest(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

//    @Test(priority = 1)
//    public void changeOrderState_Cancel() throws IOException {
//
//        provideTestData("order in remorse hold payed with SC");
//        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
//
//        p.setOrderState("Canceled");
//        p.assertOrderState("Canceled");
//
//    }
//
//    @Test(priority = 2)
//    public void changeOrderState_ManualHold() throws IOException {
//
//        provideTestData("order in remorse hold payed with SC");
//        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
//
//        p.setOrderState("Manual Hold");
//        p.assertOrderState("Manual Hold");
//
//    }
//
//    @Test(priority = 3)
//    public void changeOrderState_FraudHold() throws IOException {
//
//        provideTestData("order in remorse hold payed with SC");
//        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
//
//        p.setOrderState("Fraud Hold");
//        p.assertOrderState("Fraud Hold");
//
//    }
//
//    @Test(priority = 4)
//    public void changeOrderState_FulfillmentStarted() throws IOException {
//
//        provideTestData("order in remorse hold payed with SC");
//        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
//
//        p.setOrderState("Fulfillment Started");
//        p.assertOrderState("Fulfillment Started");
//
//    }
//
//    @Test(priority = 5)
//    public void addRemorseHoldTime() throws IOException {
//
//        provideTestData("order in remorse hold payed with SC");
//        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
//
//        p.click( p.addTimeBtn() );
//        p.timer().shouldHave(matchesText("00:44"));
//
//    }

    @Test(priority = 6)
    public void changeOrderState_backToRemorseHold() throws IOException {

        provideTestData("order in remorse hold payed with SC");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        click( p.addTimeBtn() );
        p.setOrderState("Manual Hold");
        p.setOrderState("Remorse Hold");

        p.assertTimerValue(0, 29);

    }

}
