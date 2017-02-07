package tests.generalcontrols.search;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GeneralControlsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class SavedSearchTest extends DataProvider {

    private GeneralControlsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws IOException {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
        provideTestData("order in remorse hold payed with SC");

    }

    @Test(priority = 1)
    public void addSearchFilter() {

        p = open(adminUrl, GeneralControlsPage.class);

        p.addFilter("Order", "Total", ">", "1");
        p.filter("Order : Total : > : $1").shouldBe(visible.because("Search filter is not displayed"));

    }

    @Test(priority = 2)
    public void removeSearchFilter() {

        p = open(adminUrl, GeneralControlsPage.class);

        p.addFilter("Order", "Total", ">", "1");
        p.removeSearchFilter("Order : Total : > : $1");
        p.filter("Order : Total : > : $1").shouldNot(exist.because("Search filter wasn't removed"));

    }

    @Test(priority = 3)
    public void clearAllFilters() {

        p = open(adminUrl, GeneralControlsPage.class);

        p.addFilter("Order", "Total", ">", "1");
        p.addFilter("Order", "State", "Remorse Hold");
        p.searchContextMenu("Clear All Filters");
        p.filter("Order : Total : > : $1").shouldNot(exist.because("Mentioned search filter is displayed."));
        p.filter("Order : State : Remorse Hold").shouldNot(exist.because("Mentioned search filter is displayed."));

    }

    @Test(priority = 4)
    public void saveSearch() {

        p = open(adminUrl, GeneralControlsPage.class);

        p.addFilter("Order", "Total", ">", "1");
        p.searchContextMenu("Save New Search");
        shouldBeVisible(p.tab("All - Copy"), "A just saved search isn't put into a separate tab.");
        refresh();
        p.switchToTab("All - Copy");
        p.filter("Order : Total : > : $1").shouldBe(visible
                .because("Saved search is gone after refreshing the page."));

    }

    @Test(priority = 5)
    public void editSearchName() throws IOException {

        provideTestData("saved search with 1 filter");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.searchContextMenu("Edit Search Name");
        p.setTabTitle("Edited Name");
        refresh();
        p.tab("Search " + searchRandomId).shouldNotBe(visible
                .because("A saved search tab with an old name is visible."));
        p.tab("Edited Name").shouldBe(visible
                .because("Saved search name failed to get edited."));

    }

    @Test(priority = 6)
    public void editSavedSearch_dirtySearchIndicator() throws IOException {

        provideTestData("saved search with 2 filters");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.removeSearchFilter("Order : State : Remorse Hold");
        waitForDataToLoad();
        p.dirtySearchIndicator().shouldBe(visible
                .because("A yellow dot indicator above the saved search tab title isn't displayed."));

    }

    @Test(priority = 7)
    public void updateSearch() throws IOException {

        provideTestData("saved search with 2 filters");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.removeSearchFilter("Order : Total : > : $1");
        waitForDataToLoad();
        p.searchContextMenu("Update Search");
        refresh();
        p.switchToTab("Search " + searchRandomId);
        p.filter("Order : Total : > : $1").shouldNot(exist
                .because("A just removed search filter is displayed in a search field."));
        p.filter("Order : State : Remorse Hold").should(exist
                .because("A search filter that should remain doesn't exist."));

    }

    @Test(priority = 8)
    public void deleteSearch() throws IOException {

        provideTestData("saved search with 1 filter");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        p.searchContextMenu("Delete Search");
        refresh();
        waitForDataToLoad();
        p.tab("Search " + searchRandomId).shouldNot(exist.because("A just deleted saved search is displayed."));

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() throws IOException {
        refresh();
        p.ordersCounter().click();
        if (p.tabs().size() > 1) {
            p.deleteAllSearchTabs();
        }
    }

}