package tests.storeadmin.orders;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import pages.admin.LoginPage;
import pages.admin.OrderDetailsPage;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.matchesText;
import static com.codeborne.selenide.Selenide.open;

public class OrderStateTest extends Preconditions {

    private OrderDetailsPage p;

    @BeforeTest(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    @Test(priority = 1)
    public void changeOrderState_Cancel() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Canceled");

        p.assertOrderState("Canceled");
    }

    @Test(priority = 2)
    public void changeOrderState_ManualHold() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Manual Hold");

        p.assertOrderState("Manual Hold");
    }

    @Test(priority = 3)
    public void changeOrderState_FraudHold() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Fraud Hold");

        p.assertOrderState("Fraud Hold");
    }

    @Test(priority = 4)
    public void changeOrderState_FulfillmentStarted() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Fulfillment Started");

        p.assertOrderState("Fulfillment Started");
    }

    @Test(priority = 5)
    public void addRemorseHoldTime() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.clickAddTimeBtn_times(1);

        p.timer().shouldHave(matchesText("00:44"));
    }

    @Test(priority = 6)
    @Description("Put order on \"Manual Hold\" than move it to \"Fulfillment Started\"")
    public void changeOrderState_manualHoldToFulfilment() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Manual Hold");
        p.setOrderState("Fulfillment Started");

        p.assertOrderState("Fulfillment Started");
    }

    @Test(priority = 7)
    @Description("Put order on \"Manual Hold\" than move it to \"Canceled\"")
    public void changeOrderState_manualHoldToCanceled() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Manual Hold");
        p.setOrderState("Canceled");

        p.assertOrderState("Canceled");
    }

    @Test(priority = 8)
    @Description("Put order on \"Manual Hold\" than move it to \"Fraud Hold\"")
    public void changeOrderState_manualHoldToFraud() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Manual Hold");
        p.setOrderState("Fraud Hold");

        p.assertOrderState("Fraud Hold");
    }

    @Test(priority = 9)
    @Description("Put order on \"Fraud Hold\" than move it to \"Fulfillment Started\"")
    public void changeOrderState_fraudHoldToFulfillment() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Fraud Hold");
        p.setOrderState("Fulfillment Started");

        p.assertOrderState("Fulfillment Started");
    }

}
