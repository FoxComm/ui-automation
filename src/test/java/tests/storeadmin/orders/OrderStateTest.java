package tests.storeadmin.orders;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
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
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Can cancel order if it's on \"Remorse Hold\"")
    public void changeOrderState_Cancel() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Canceled");

        p.assertOrderState("Canceled");
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Can put order on \"Manual Hold\"")
    public void changeOrderState_ManualHold() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Manual Hold");

        p.assertOrderState("Manual Hold");
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Can put order on \"Fraud Hold\"")
    public void changeOrderState_FraudHold() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Fraud Hold");

        p.assertOrderState("Fraud Hold");
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Can put order in \"Fulfillment Started\" state")
    public void changeOrderState_FulfillmentStarted() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Fulfillment Started");

        p.assertOrderState("Fulfillment Started");
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Can increase remorse hold timer")
    public void addRemorseHoldTime() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.clickAddTimeBtn_times(1);

        p.timer().shouldHave(matchesText("00:44"));
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Put order on \"Manual Hold\" than move it to \"Fulfillment Started\"")
    public void changeOrderState_manualHoldToFulfilment() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Manual Hold");
        p.setOrderState("Fulfillment Started");

        p.assertOrderState("Fulfillment Started");
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Put order on \"Manual Hold\" than move it to \"Canceled\"")
    public void changeOrderState_manualHoldToCanceled() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Manual Hold");
        p.setOrderState("Canceled");

        p.assertOrderState("Canceled");
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Put order on \"Manual Hold\" than move it to \"Fraud Hold\"")
    public void changeOrderState_manualHoldToFraud() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Manual Hold");
        p.setOrderState("Fraud Hold");

        p.assertOrderState("Fraud Hold");
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Orders")
    @Description("Put order on \"Fraud Hold\" than move it to \"Fulfillment Started\"")
    public void changeOrderState_fraudHoldToFulfillment() throws IOException {
        provideTestData("order in remorse hold payed with SC");

        p = openPage(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
        p.setOrderState("Fraud Hold");
        p.setOrderState("Fulfillment Started");

        p.assertOrderState("Fulfillment Started");
    }

}
