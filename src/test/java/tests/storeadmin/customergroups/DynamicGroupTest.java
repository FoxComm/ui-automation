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
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.sleep;

public class DynamicGroupTest extends Preconditions {
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
    @Stories("Customer Groups : Dynamic Groups")
    @Description("Can create Dynamic group; User is redirected to Group page after group creation")
    public void dynamicGroupCreateEmpty() throws IOException {
        String groupName = "Dynamic Group " + generateRandomID();

        p = openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);
        p.clickAddGroupBtn();
        p.clickDGroupBtn();
        p.setCustomerGroupName(groupName);
        p.clickAddCriteriaBtn();
        p.selectCriteria("1", "Name", "is", "Not Existing User One");
        p.clickSaveGroupBtn();

        p.groupNameInFormFld().shouldHave(text(groupName));
        p.groupTypeInFormFld().shouldHave(text("Dynamic"));
        p.groupCounterInForm().shouldHave(text("0"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Dynamic Groups")
    @Description("Dynamic Group name, type and counter are shown in search view")
    public void dynamicGroupIsShownInSearchView() throws IOException{
        provideTestData("create empty dynamic group with one criteria");

        p = openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.groupNameInSearchView(customerGroupName).shouldBe(visible);
        p.groupTypeInSearchView(customerGroupName).shouldHave(text("Dynamic"));
        p.groupCounterInSearchView(customerGroupName).shouldHave(text("0"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Dynamic Groups")
    @Description("Dynamic group name can be edited")
    public void dynamicGroupNameEdit() throws IOException{
        provideTestData("create empty dynamic group with one criteria");

        p = openPage(adminUrl +"/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        String editedCustomerGroupName = customerGroupName + " Edited";
        p.clickEditGroupBtn();
        p.setCustomerGroupName(editedCustomerGroupName);
        p.clickSaveGroupBtn();
        openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.groupNameInSearchView(editedCustomerGroupName).shouldBe(visible);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Dynamic Groups")
    @Description("Dynamic group second criteria can be added")
    public void dynamicGroupCriteriaAdd() throws IOException {
        provideTestData("create empty dynamic group with one criteria");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);
        p.clickEditGroupBtn();
        p.selectMatchingOption("any");
        p.clickAddCriteriaBtn();
        p.selectCriteria("2", "Name", "is", "Not Existing User Two");
        p.clickSaveGroupBtn();
        openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.groupCriteriasCollection().shouldHaveSize(2);
        p.getMatchGroupOption().shouldHave(text("any"));
        p.getGroupCriteriaValue("2").shouldHave(text("Not Existing User Two"));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Dynamic Groups")
    @Description("Dynamic group criteria can be removed")
    public void dynamicGroupCriteriaRemove() throws IOException {
        provideTestData("create dynamic group with two criterias");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);
        p.clickEditGroupBtn();
        p.clickRemoveCriteriaBtn("2");
        p.clickSaveGroupBtn();

        p.groupCriteriasCollection().shouldHaveSize(1);
        p.getGroupCriteriaValue("1").shouldHave(text("Not Existing User One"));
    }

//    TODO - Not possible to create Dynamic group without criterias
//    4. should be possible to archive group
//    - archived group disappears from the table
//    5. should be shown correct data when any or all match parameter is chosen
//    6. should be possible to add different criteria
//    8. should not be possible to delete customers from the group
//    9. should be possible to see customer statistics (preconditions with creation set of data)
}
