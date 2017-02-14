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

public class ReferenceNumberTest extends DataProvider {

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
    @Description("Find order by its reference number")
    public void orderRefNum() throws IOException {
        provideTestData("order in remorse hold");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Reference Number", orderId);
        waitForDataToLoad();

        p.itemOnList(orderId).shouldBe(visible);
        p.searchResults().shouldHaveSize(1);
    }

}