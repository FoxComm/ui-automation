package tests.generalcontrols.search;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GeneralControlsPage;
import pages.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class SharedSearchTest extends DataProvider {

    private GeneralControlsPage p;

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
    public void shareSearch() throws IOException {

        provideTestData("saved search with 1 filter");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.searchContextMenu("Share Search");
        p.shareSearchWith("Such Root");
//        p.adminPilledLabel().shouldHave(text("Such Root")
//                .because("Admin user isn't selected for sharing - 'pilled label with admin name' isn't displayed."));
        p.clickShare();
        shouldBeVisible(p.successIcon(), "\"Success\" message isn't displayed after clicking \"Share\"");
        p.closeModalWindow();

        p.logout();
        p.login(adminOrg, "hackerman@yahoo.com", "password1");
        p.tab("Search " + searchRandomId).shouldBe(visible.because("Shared search isn't displayed."));

    }

    @Description("Can remove admin user from \"Search Associations\" list")
    @Test(priority = 2)
    public void removeUserFromAssociationsList() throws IOException {

        provideTestData("saved search with 1 filter");
        shareSearch(searchCode, "Such Root");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.searchContextMenu("Share Search");
        p.removeAdmin("Such Root");
        p.adminOnAssociationsList("Such Root").shouldNotBe(visible);

    }

    @Test(priority = 3)
    public void unshareSearch() throws IOException {

        provideTestData("saved search with 1 filter");
        shareSearch(searchCode, "Such Root");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.searchContextMenu("Share Search");
        p.unshareSearchWith("Such Root");
        p.closeModalWindow();
        p.logout();
        p.login(adminOrg, "hackerman@yahoo.com", "password1");
        //refresh() is a workaround for a known bug - should be deleted later
        refresh();
        p.tab("Search " + searchRandomId).shouldNotBe(visible.because("Shared search is displayed."));

    }

    @Test(priority = 4)
    public void editSharedSearch() throws IOException {

        provideTestData("saved search with 1 filter");
        shareSearch(searchCode, "Such Root");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.removeSearchFilter("Order : Total : > : $0");
        waitForDataToLoad();
        p.addFilter("Order", "State", "Remorse Hold");
        p.searchContextMenu("Update Search");
        shouldNotBeVisible(p.dirtySearchIndicator(),
                "\"Dirty Search\" indicator isn't displayed after search has been edited");
        p.logout();
        p.login(adminOrg, "hackerman@yahoo.com", "password1");
        //refresh() is a workaround for a known bug - should be deleted later
        refresh();
        p.tab("Search " + searchRandomId).shouldNotBe(visible.because("Shared search is displayed."));

    }

    @Test(priority = 5)
    public void deleteSavedSearch() throws IOException {
        provideTestData("saved search with 1 filter");
        shareSearch(searchCode, "Such Root");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.searchContextMenu("Delete Search");
        p.tab("Search " + searchRandomId).shouldNot(exist
                .because("Failed to delete shared search - search tab still exists."));
    }

    @Description("Shared search shouldn't be displayed for associated admin users if owner has deleted it")
    @Test(priority = 6)
    public void deleteSharedSearch() throws IOException {

        provideTestData("saved search with 1 filter");
        shareSearch(searchCode, "Such Root");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.searchContextMenu("Delete Search");
        shouldNotExist(p.tab("Search " + searchRandomId),
                "Failed to delete shared search - search tab still exists.");
        p.logout();
        p.login(adminOrg, "hackerman@yahoo.com", "password1");
        //refresh() is a workaround for a known bug - should be deleted later
        refresh();
        p.tab("Search " + searchRandomId).shouldNotBe(visible.because("Shared search is displayed."));

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() throws IOException {
        refresh();
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            p.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
        p.ordersCounter().click();
        if (p.tabs().size() > 1) {
            p.deleteAllSearchTabs();
        }
    }

}
