package tests.orderstests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrdersPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class SearchTest extends DataProvider {

    private OrdersPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }
        p = open(adminUrl + "/orders", OrdersPage.class);
    }

    @Test (priority = 1)
    public void removeFilter() {

        p.addFilter("Order", "State", "Cart");
        p.removeFilter("1");

    }

    @Test (priority = 2)
    public void addSearchFilter_usingArrowKeys() {

        p.addFilter("Order", "State", "Cart");
        p.assertOrderParameter(1, "Order State", "Cart");

    }

    @Test (priority = 3)
    public void customerName_filter() throws IOException {

        provideTestData("a customer");

        p.addFilter("Customer", "Name", customerName);
        p.assertOrderParameter(1, "Customer Name", customerName);

    }

    @Test (priority = 4)
    public void customerEmail_filter() throws IOException {

        p.addFilter("Customer", "Email", customerEmail);
        p.assertOrderParameter(1, "Customer Email", customerEmail);

    }

    @Test (priority = 5)
    public void orderTotal_filter() {

        p.addFilter("Order", "Total", ">=", "$50");
        p.sortListBy(7);
        p.assertOrderParameter(1, "Total", ">=", 50);

    }

    @Test (priority = 6)
    public void noSearchResults_email() {

        p.addFilter("Customer", "Email", "find.nothing@withthis.com");
        p.assertNoSearchResults();

    }

    @Test (priority = 6)
    public void noSearchResults_SKU() {

        p.addFilter("Items", "Product SKU", "SKU-NOP");
        p.assertNoSearchResults();

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        p.cleanSearchField();
    }

}
