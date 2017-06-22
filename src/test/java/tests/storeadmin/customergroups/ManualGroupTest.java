package tests.storeadmin.customergroups;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CustomerGroupPage;
import pages.admin.LoginPage;
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

public class ManualGroupTest extends Preconditions {
    private CustomerGroupPage p;

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
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can create MCG; User is redirected to Group page after creation")
    public void createEmptyMCG() throws IOException {
        String groupName = "Manual Group " + generateRandomID();

        p = openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);
        p.clickAddCGroupBtn();
        p.clickMCGroupBtn();
        p.setCGroupName(groupName);
        p.clickSaveCGroupBtn();

        p.groupName_form().shouldHave(text(groupName));
        p.groupType_form().shouldHave(text("Manual"));
        p.groupCounter_form().shouldHave(text("0"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can show MCG name, type and counter in search view")
    public void createEmptyMCG_searchView() throws IOException{
        provideTestData("empty manual group");

        p = openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.groupName_searchView(customerGroupName).shouldBe(visible);
        p.groupType_searchView(customerGroupName).shouldHave(text("Manual"));
        p.groupCounter_searchView(customerGroupName).shouldHave(text("0"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can edit MCG name")
    public void editMCGName() throws IOException{
        provideTestData("empty manual group");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        String editedMCGName = customerGroupName + " Edited";
        p.clickEditCGroupBtn();
        p.setCGroupName(editedMCGName);
        p.clickSaveCGroupBtn();
        openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.groupName_searchView(editedMCGName).shouldBe(visible);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can add customer to MCG")
    public void addCustomersMCG() throws IOException{

    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can update customers counter for MCG in search view")
    public void customersCounterMCG_searchView() throws IOException{

    }


//    4. should be possible to archive group
//    - archived group disappears from the table
//    6. should not be possible to create group with duplicated name
//    7. should be possible to add/remove customers to the group
//    - updated counter in search view
//    8. should be possible to search customers from group
//    9. should be possible to delete customer from the group
//    10. should be possible to see customer statistics
}
