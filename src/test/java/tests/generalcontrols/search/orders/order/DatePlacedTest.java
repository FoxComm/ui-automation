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

public class DatePlacedTest extends DataProvider {

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

    @Test(priority = 9)
    public void placeOrder_remorseHold() throws IOException {
        provideTestData("order state: remorse hold");
    }

    @Test(priority = 9, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : = : today ] should return order placed today")
    public void orderDatePlaced_eq_today() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "=", getDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 10, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : = : yesterday ] should not find order placed today")
    public void orderDatePlaced_eq_yesterday() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "=", getYesterdayDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 11, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : = : tomorrow ] should find no orders")
    public void orderDatePlaced_eq_tomorrow() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "=", getTomorrowDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 12, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : <> : today ] should not find order placed today")
    public void orderDatePlaced_ne_today() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "=", getDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 13, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : <> : yesterday ] should find order placed today")
    public void orderDatePlaced_ne_yesterday() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "=", getYesterdayDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 30, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : <> : tomorrow ] should find order placed today")
    public void orderDatePlaced_ne_tomorrow() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "=", getTomorrowDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 31, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : > : today ] should find no orders")
    public void orderDatePlaced_gt_today() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", ">", getDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 32, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : > : yesterday ] should find order placed today")
    public void orderDatePlaced_gt_yesterday() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", ">", getYesterdayDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 33, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : > : tomorrow ] should find no orders")
    public void orderDatePlaced_gt_tomorrow() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", ">", getTomorrowDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 34, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : >= : today ] should find order placed today")
    public void orderDatePlaced_ge_today() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", ">=", getDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 35, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : >= : yesterday ] should find order placed today")
    public void orderDatePlaced_ge_yesterday() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", ">=", getYesterdayDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 36, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : >= : tomorrow ] should find no orders")
    public void orderDatePlaced_ge_tomorrow() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", ">=", getTomorrowDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 37, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : < : today ] should not find order placed today")
    public void orderDatePlaced_lt_today() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "<", getDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 38, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : < : yesterday ] should not find order placed today")
    public void orderDatePlaced_lt_yesterday() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "<", getYesterdayDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 39, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : < : tomorrow ] should find find order placed today")
    public void orderDatePlaced_lt_tomorrow() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "<", getTomorrowDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 40, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : <= : today ] should find order placed today")
    public void orderDatePlaced_le_today() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "<", getDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 41, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : <= : yesterday ] should not find order placed today")
    public void orderDatePlaced_le_yesterday() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "<=", getYesterdayDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 42, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Date Placed : <= : tomorrow ] should find find order placed today")
    public void orderDatePlaced_le_tomorrow() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Date Placed", "<=", getTomorrowDate());
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

}
