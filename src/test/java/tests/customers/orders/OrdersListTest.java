package tests.customers.orders;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomerPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertEquals;

public class OrdersListTest extends DataProvider {

    private CustomerPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open("http://admin.stage.foxcommerce.com/");
        if ( (Objects.equals(getUrl(), "http://admin.stage.foxcommerce.com/login")) ) {
            LoginPage loginPage = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void checkOrderOnList() throws IOException {

        provideTestData("customer with 2 orders in remorse hold");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId + "/transactions", CustomerPage.class);

        p.waitForDataToLoad();
        assertEquals( p.amountOfOrders(), 3,
                "Amount of orders on the list is lower than it should.");

    }

    @Test(priority = 2)
    public void totalSalesTest() throws IOException {

        provideTestData("customer with 2 orders in remorse hold and fulfillment started");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId + "/transactions", CustomerPage.class);

        p.orderListBy("Order");

        String strTotal1 =  p.getOrderParamValue(1, "Total");
        double total1 = Double.valueOf( strTotal1.substring(1, strTotal1.length()) );
        String strTotal2 = p.getOrderParamValue(2, "Total");
        double total2 = Double.valueOf( strTotal2.substring(1, strTotal2.length()) );
        double expectedResult = total1 + total2;

        assertEquals( p.totalSalesVal(), expectedResult,
                "Total Sales sum is incorrect.");

    }

    @Test(priority = 3)
    public void searchFld_orderRefNum() throws IOException {

        provideTestData("customer with 2 orders in remorse hold and fulfillment started");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId + "/transactions", CustomerPage.class);

        p.addFilter("Order", "Reference Number", orderId);
        assertEquals( p.getOrderParamValue(1, "Order State"), "Fulfillment Started",
                "Found order is not in 'Fulfillment Started' state.");

    }

    @Test(priority = 4)
    public void searchFld_productName() throws IOException {

        provideTestData("customer with 2 orders in remorse hold and fulfillment started");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId + "/transactions", CustomerPage.class);

        p.addFilter("Items", "Product Name", "Shark");
        assertEquals( p.amountOfOrders(), 1,
                "Order with 'Shark' product in it isn't listed in search results.");

    }

    @Test(priority = 5)
    public void searchFld_productSKU() throws IOException {

        provideTestData("customer with 2 orders in remorse hold and fulfillment started");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId + "/transactions", CustomerPage.class);

        p.addFilter("Items", "Product SKU", "SKU-BRO");
        assertEquals( p.amountOfOrders(), 1,
                "Order with 'Shark' product in it isn't listed in search results.");

    }

}
