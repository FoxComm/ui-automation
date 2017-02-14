package tests.generalcontrols.search.orders.order;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrdersPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class StateTest extends DataProvider {

    private OrdersPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            loginPage.userMenuBtn().shouldBe(visible);
        }
    }

    @Test(priority = 2)
    @Description("<Order : State : Remorse Hold> should return orders in 'Remorse Hold' state")
    public void orderState_remorseHold() throws IOException {
        provideTestData("order state: remorse hold");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "State", "Remorse Hold");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 3)
    @Description("<Order : State : Manual Hold> should return orders in 'Manual Hold' state")
    public void orderState_manualHold() throws IOException {
        provideTestData("order state: manual hold");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "State", "Manual Hold");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 4)
    @Description("<Order : State : Fraud Hold> should return orders in 'Fraud Hold' state")
    public void orderState_fraudHold() throws IOException {
        provideTestData("order state: fraud hold");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "State", "Fraud Hold");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 5)
    @Description("<Order : State : Fulfillment Started> should return orders in 'Fulfillment Started' state")
    public void orderState_fulfillmentStarted() throws IOException {
        provideTestData("order state: fulfilment started");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "State", "Fulfillment Started");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 6)
    @Description("<Order : State : Shipped> should return orders in 'Shipped' state")
    public void orderState_shipped() throws IOException {
        provideTestData("order state: shipped");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "State", "Shipped");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 7)
    @Description("<Order : State : Partially Shipped> should return orders in 'Parially Shipped' state")
    public void orderState_partiallyShipped() throws IOException {
        provideTestData("order state: partially shipped");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "State", "Partially Shipped");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 8)
    @Description("<Order : State : Canceled> should return orders in 'Canceled' state")
    public void orderState_canceled() throws IOException {
        provideTestData("order state: canceled");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "State", "Canceled");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

}
