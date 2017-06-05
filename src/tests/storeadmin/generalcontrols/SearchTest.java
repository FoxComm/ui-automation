package tests.storeadmin.generalcontrols;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.OrdersPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class SearchTest extends Preconditions {

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
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Search & Search Filters")
    @Description("Can remove search filter")
    public void removeFilter() throws IOException {
        provideTestData("order in remorse hold");

        p = openPage(adminUrl + "/orders", OrdersPage.class);
        p.addFilter("Order", "Total", ">", "1");
        p.removeSearchFilter("Order : Total : > : $1");

        p.searchPill("Order : Total : > : $1").shouldNotBe(visible);
    }

    @Test (priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Search & Search Filters")
    @Description("Can add search filter using keyboard arrows")
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
