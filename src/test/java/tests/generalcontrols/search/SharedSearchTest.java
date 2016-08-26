package tests.generalcontrols.search;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GeneralControlsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class SharedSearchTest extends DataProvider {

    private GeneralControlsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
            loginPage.userMenuBtn().shouldBe(visible);
        }

    }

//    @Test(priority = 1)
//    public void shareSearch() throws IOException {
//
//        provideTestData("saved search with 1 filter");
//        p = open(adminUrl, GeneralControlsPage.class);
//
//        p.switchToTab("Search " + searchRandomId);
//        p.searchContextMenu("Share Search");
//        setFieldVal( p.inviteUsersFld(), "such root" );
//        click( p.userName("Such Root") );
//        p.shareBtn().shouldBe(enabled);
//        p.adminPilledLabel().shouldHave(text("Such Root")
//                .because("Admin user isn't selected for sharing - 'pilled label with admin name' isn't displayed."));
//        click( p.shareBtn() );
//        p.successIcon().shouldBe(visible
//                .because("'Success' message isn't displayed after clicking 'Share'."));
//        click( p.closeModalWindowBtn() );
//        p.logout();
//        p.login("hackerman@yahoo.com", "password1");
//        p.tab("Search " + searchRandomId).shouldBe(visible.because("Shared search isn't displayed."));
//
//    }
//
//    @Test(priority = 2)
//    public void unshareSearch() throws IOException {
//
//        provideTestData("saved search with 1 filter");
//        shareSearch(searchCode, "Such Root");
//        p = open(adminUrl, GeneralControlsPage.class);
//
//        p.switchToTab("Search " + searchRandomId);
//        p.searchContextMenu("Share Search");
//        click( p.removeAdminBtn("Such Root") );
//        p.successIcon().shouldBe(visible
//                .because("'Success msg isn't displayed."));
//        p.successIcon().shouldBe(visible
//                .because("'Success' message isn't displayed after clicking 'Share'."));
//        click( p.closeModalWindowBtn() );
//        p.logout();
//        p.login("hackerman@yahoo.com", "password1");
//        //refresh() is a workaround for a known bug - should be deleted later
//        refresh();
//        p.tab("Search " + searchRandomId).shouldNotBe(visible.because("Shared search is displayed."));
//
//    }

    @Test(priority = 3)
    public void editSharedSearch() throws IOException {

        provideTestData("saved search with 1 filter");
        shareSearch(searchCode, "Such Root");
        p = open(adminUrl, GeneralControlsPage.class);

        p.switchToTab("Search " + searchRandomId);
        click( p.removeFilterBtn("Order : Total : > : $0") );
        waitForDataToLoad();
        p.addFilter("Order", "State", "Remorse Hold");
        p.searchContextMenu("Update Search");
        p.dirtySearchIndicator().shouldNotBe(visible);
        p.logout();
        p.login("hackerman@yahoo.com", "password1");
        //refresh() is a workaround for a known bug - should be deleted later
        refresh();
        p.tab("Search " + searchRandomId).shouldNotBe(visible.because("Shared search is displayed."));

    }

//    @Test(priority = 4)
//    public void deleteSharedSearch() throws IOException {
//
//        provideTestData("saved search with 1 filter");
//        shareSearch(searchCode, "Such Root");
//        p = open(adminUrl, GeneralControlsPage.class);
//
//        p.switchToTab("Search " + searchRandomId);
//        p.searchContextMenu("Delete Search");
//        p.tab("Search " + searchRandomId).shouldNot(exist
//                .because("Failed to delete shared search - search tab still exists."));
//        p.logout();
//        p.login("hackerman@yahoo.com", "password1");
//        //refresh() is a workaround for a known bug - should be deleted later
//        refresh();
//        p.tab("Search " + searchRandomId).shouldNotBe(visible.because("Shared search is displayed."));
//
//    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() throws IOException {
        p.ordersCounter().click();
        if (p.tabs().size() > 1) {
            p.deleteAllSearchTabs();
        }
    }

}
