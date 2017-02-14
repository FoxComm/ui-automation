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

public class TotalTest extends DataProvider {

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

    @Test(priority = 43, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : = equalPrice ] should find order")
    public void orderTotal_eq_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "=", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 44, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : = lowerPrice ] should not find order")
    public void orderTotal_eq_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "=", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 45, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : = higherPrice ] should not find order")
    public void orderTotal_eq_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "=", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 46, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : <> equalPrice ] should not find order")
    public void orderTotal_ne_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<>", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 47, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : <> lowerPrice ] should find order")
    public void orderTotal_ne_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<>", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 48, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : <> higherPrice ] should find order")
    public void orderTotal_ne_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<>", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 49, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : > equalPrice ] should not find order")
    public void orderTotal_gt_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", ">", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 50, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : > lowerPrice ] should find order")
    public void orderTotal_gt_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", ">", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 51, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : > higherPrice ] should not find order")
    public void orderTotal_gt_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", ">", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 52, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : >= equalPrice ] should find order")
    public void orderTotal_ge_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", ">=", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 53, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : >= lowerPrice ] should find order")
    public void orderTotal_ge_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", ">", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 54, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : >= higherPrice ] should not find order")
    public void orderTotal_ge_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", ">=", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 55, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : < equalPrice ] should not find order")
    public void orderTotal_lt_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 56, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : < lowerPrice ] should not find order")
    public void orderTotal_lt_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 57, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : < higherPrice ] should find order")
    public void orderTotal_lt_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 58, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : <= equalPrice ] should find order")
    public void orderTotal_le_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<=", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 59, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : <= lowerPrice ] should not find order")
    public void orderTotal_le_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<=", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 60, dependsOnMethods = "placeOrder_remorseHold")
    @Description("[ Order : Total : <= higherPrice ] should find order")
    public void orderTotal_le_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", "<=", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

}
