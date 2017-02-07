package tests.orders;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrdersPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

public class SearchTest extends DataProvider {

    private OrdersPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
        p = openPage(adminUrl + "/orders", OrdersPage.class);
    }

    @Test (priority = 1)
    public void removeFilter() throws IOException {

        provideTestData("order in remorse hold");
        p = openPage(adminUrl + "/orders", OrdersPage.class);

        p.addFilter("Order", "Total", ">", "1");
        p.removeSearchFilter("Order : Total : > : $1");

    }

    @Test (priority = 2)
    public void addSearchFilter_usingArrowKeys() throws IOException {

        provideTestData("order in remorse hold");
        p = openPage(adminUrl + "/orders", OrdersPage.class);

        p.addFilter_arrowKeys("Order", "State", "Remorse Hold");
        p.getOrderParamValue(1, "Order State").shouldHave(text("Remorse Hold"));

    }

    @Test (priority = 3)
    public void customerName_filter() throws IOException {

        provideTestData("order in remorse hold");
        p = openPage(adminUrl + "/orders", OrdersPage.class);

        p.addFilter("Customer", "Name", customerName);
        p.getOrderParamValue(1, "Customer Name").shouldHave(text(customerName));

    }

    @Test (priority = 4)
    public void customerEmail_filter() throws IOException {

        provideTestData("order in remorse hold");
        p = openPage(adminUrl + "/orders", OrdersPage.class);

        p.addFilter("Customer", "Email", customerEmail);
        p.getOrderParamValue(1, "Customer Email").shouldHave(text(customerEmail));

    }


//    Need to refactor assertion for this
//    @Test (priority = 5)
//    public void orderTotal_filter() {
//
//        p = openPage(adminUrl + "/orders", OrdersPage.class);
//
//        p.addFilter_arrowKeys("Order", "Total", ">=", "$50");
//        p.sortListBy(7);
//        p.assertOrderParameter(1, "Total", ">=", 50);
//
//    }

    @Test (priority = 6)
    public void noSearchResults_email() {

        p = openPage(adminUrl + "/orders", OrdersPage.class);

        p.addFilter("Customer", "Email", "find.nothing@withthis.com");
        p.assertNoSearchResults();

    }

    @Test (priority = 7)
    public void noSearchResults_SKU() {

        p = openPage(adminUrl + "/orders", OrdersPage.class);

        p.addFilter("Items", "Product SKU", "SKU-NONE");
        p.assertNoSearchResults();

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        p.cleanSearchField();
    }

}
