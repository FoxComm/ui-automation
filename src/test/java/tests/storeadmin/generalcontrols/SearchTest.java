package tests.storeadmin.generalcontrols;

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

    //TODO: More search filters tests

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        p.cleanSearchField();
    }

}
