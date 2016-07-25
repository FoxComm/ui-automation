package tests.generalcontrols.search;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GeneralControlsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class SharedSearchTest extends DataProvider {

    private GeneralControlsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void shareSearch() throws IOException {

        provideTestData("saved search with 1 filter");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Test Search");
        p.searchContextMenu("Share Search");
        setFieldVal( p.inviteUsersFld(), "such root" );
        click( p.userName("Such Root") );       // element locator isn't defined due to bug
        p.shareBtn().shouldBe(enabled);
        click( p.shareBtn() );
        p.logout();
        p.login("hackerman@yahoo.com", "password1");
        p.tab("Test Title").shouldBe(visible.because("Shared search isn't displayed."));

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() throws IOException {
        p.ordersCounter().click();
        if (p.tabs().size() > 1) {
            p.deleteAllSearchTabs();
        }
    }

}
