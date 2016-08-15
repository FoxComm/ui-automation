package tests.generalcontrols.search;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SkusPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.xpath;

public class GiftCardsSearchTest extends DataProvider {

    private SkusPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws IOException {

        open(adminUrl);
        if ((Objects.equals(getUrl(), adminUrl + "/login"))) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }
        provideTestData("SKU for search tests");
        p = open(adminUrl + "/skus", SkusPage.class);

    }

    @Test(priority = 1)
    public void gcNumber() {

        p.addFilter("Gift Card : Number", gcNumber);
        p.itemOnList(gcNumber).shouldBe(visible
                .because("Search failed to find GC with number <" + gcNumber + ">."));

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if ($(xpath("//a[text()='×']")).is(visible)) {
            p.cleanSearchField();
        }
    }

}
