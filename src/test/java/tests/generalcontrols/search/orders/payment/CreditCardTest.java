package tests.generalcontrols.search.orders.payment;

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

public class CreditCardTest extends DataProvider {

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

    @Test(priority = 1)
    @Description("Order in remorse hold payed with credit card")
    public void payment_creditCart() throws IOException {
        provideTestData("payment: credit card");
    }

    //======================================== TOTAL ========================================

    @Test(priority = 2, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : = equalPrice ] should find order")
    public void payment_creditCardTotal_eq_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "=", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 3, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : = lowerPrice ] should not find order")
    public void payment_creditCardTotal_eq_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "=", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 4, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : = higherPrice ] should not find order")
    public void payment_creditCardTotal_eq_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "=", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 5, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : <> equalPrice ] should not find order")
    public void payment_creditCardTotal_ne_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<>", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 6, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : <> lowerPrice ] should find order")
    public void payment_creditCardTotal_ne_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<>", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 7, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : <> higherPrice ] should find order")
    public void payment_creditCardTotal_ne_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<>", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 8, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : > equalPrice ] should not find order")
    public void payment_creditCardTotal_gt_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", ">", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 9, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : > lowerPrice ] should find order")
    public void payment_creditCardTotal_gt_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", ">", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 10, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : > higherPrice ] should not find order")
    public void payment_creditCardTotal_gt_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", ">", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 11, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : >= equalPrice ] should find order")
    public void payment_creditCardTotal_ge_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", ">=", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 12, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : >= lowerPrice ] should find order")
    public void payment_creditCardTotal_ge_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", ">", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 13, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : >= higherPrice ] should not find order")
    public void payment_creditCardTotal_ge_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", ">=", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 14, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : < equalPrice ] should not find order")
    public void payment_creditCardTotal_lt_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 15, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : < lowerPrice ] should not find order")
    public void payment_creditCardTotal_lt_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 16, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : < higherPrice ] should find order")
    public void payment_creditCardTotal_lt_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 17, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : <= equalPrice ] should find order")
    public void payment_creditCardTotal_le_equalPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<=", "65");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

    @Test(priority = 18, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : <= lowerPrice ] should not find order")
    public void payment_creditCardTotal_le_lowerPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<=", "64");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldNotBe(visible);
    }

    @Test(priority = 19, dependsOnMethods = "payment_creditCart")
    @Description("[ Payment : Credit Card Total : <= higherPrice ] should find order")
    public void payment_creditCardTotal_le_higherPrice() throws IOException {
        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Payment", "Credit Card Total", "<=", "66");
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
    }

}
