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

public class ManualGroupTest extends Preconditions{
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
    @Stories("Customer Groups : Manual Groups")
    @Description("Can create Manual group; User is redirected to Manual group page after group creation")
    public void manualGroupCreateEmpty() throws IOException {
        String groupName = "Manual Group " + generateRandomID();

        p = openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);
        p.clickAddGroupBtn();
        p.clickMGroupBtn();
        p.setCustomerGroupName(groupName);
        p.clickSaveGroupBtn();

        p.groupNameInFormFld().shouldHave(text(groupName));
        p.groupTypeInFormFld().shouldHave(text("Manual"));
        p.groupCounterInForm().shouldHave(text("0"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual Groups")
    @Description("Manual Group name, type and counter are shown in search view")
    public void manualGroupIsShownInSearchView() throws IOException{
        provideTestData("create empty manual group");

        p = openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.groupNameInSearchView(customerGroupName).shouldBe(visible);
        p.groupTypeInSearchView(customerGroupName).shouldHave(text("Manual"));
        p.groupCounterInSearchView(customerGroupName).shouldHave(text("0"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual Groups")
    @Description("Manual Group name can be edited")
    public void manualGroupNameEdit() throws IOException{
        provideTestData("create empty manual group");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        String editedCustomerGroupname = customerGroupName + " Edited";
        p.clickEditGroupBtn();
        p.setCustomerGroupName(editedCustomerGroupname);
        p.clickSaveGroupBtn();
        openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.groupNameInSearchView(editedCustomerGroupname).shouldBe(visible);
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
