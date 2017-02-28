package tests.storeadmin.customers.orders;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertEquals;

public class OrdersListTest extends DataProvider {

    private CustomersPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    @Test(priority = 1)
    public void checkOrderOnList() throws IOException {
        provideTestData("customer with 2 orders in remorse hold");

        p = openPage(adminUrl + "/customers/" + customerId + "/transactions", CustomersPage.class);
        p.waitForDataToLoad();

        p.ordersOnList().shouldHave(size(2));
    }

    //TODO: Order should be "Shipped" to get counted into "Total Sales"
//    @Test(priority = 2)
//    public void totalSalesTest() throws IOException {
//        provideTestData("customer with 2 orders in remorse hold and fulfillment started");
//
//        p = open(adminUrl + "/customers/" + customerId + "/transactions", CustomersPage.class);
//        p.orderListBy("Order");
//        String strTotal1 =  p.getOrderParamVal(1, "Total").getText();
//        double total1 = Double.valueOf( strTotal1.substring(1, strTotal1.length()) );
//        String strTotal2 = p.getOrderParamVal(2, "Total").getText();
//        double total2 = Double.valueOf( strTotal2.substring(1, strTotal2.length()) );
//        double expectedResult = total1 + total2;
//
//        assertEquals( p.totalSalesVal(), expectedResult, "Total Sales sum is incorrect.");
//    }

    @Test(priority = 3)
    public void searchFld_orderRefNum() throws IOException {
        provideTestData("customer with 2 orders in remorse hold and fulfillment started");

        p = openPage(adminUrl + "/customers/" + customerId + "/transactions", CustomersPage.class);
        waitForDataToLoad();
        p.addFilter("Order", "Reference Number", orderId);

        p.getOrderParamVal(1, "Order State").shouldBe(visible
                .because("Found order <" + orderId + "> is not in 'Fulfillment Started' state."));
        p.ordersOnList().shouldHaveSize(1);
    }

    //TODO: Product name should consist of 2 words, e.g. "Product 123" (currently it consists of 3 words)
    @Test(priority = 4)
    public void searchFld_productName() throws IOException {
        provideTestData("customer with 2 orders in remorse hold and fulfillment started");

        p = openPage(adminUrl + "/customers/" + customerId + "/transactions", CustomersPage.class);
        waitForDataToLoad();
        p.addFilter("Items", "Product Name", productName);

        p.ordersOnList().shouldHaveSize(1);
    }

    @Test(priority = 5)
    public void searchFld_productSKU() throws IOException {
        provideTestData("customer with 2 orders in remorse hold and fulfillment started");

        p = openPage(adminUrl + "/customers/" + customerId + "/transactions", CustomersPage.class);
        waitForDataToLoad();
        p.addFilter("Items", "Product SKU", skus.get(0));

        p.ordersOnList().shouldHaveSize(1);
    }

}