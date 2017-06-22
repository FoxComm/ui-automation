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
    @Stories("Customer Groups : Dynamic")
    @Description("Can create DCG; User is redirected to Group page after creation")
    public void createEmptyDCG() throws IOException {
        String groupName = "Dynamic Group " + generateRandomID();

        p = openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);
        p.clickAddCGroupBtn();
        p.clickDCGroupBtn();
        p.setCGroupName(groupName);
        p.clickAddCriteriaBtn();
        p.setCriteria("1", "Name", "is", "Not Existing User One");
        p.clickSaveCGroupBtn();

        p.groupName_form().shouldHave(text(groupName));
        p.groupType_form().shouldHave(text("Dynamic"));
        p.groupCounter_form().shouldHave(text("0"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Dynamic")
    @Description("Can show DCG name, type and counter in search view")
    public void createEmptyDCG_searchView() throws IOException{
        provideTestData("empty dynamic group with one criteria");

        p = openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.name_searchView(customerGroupName).shouldBe(visible);
        p.groupType_searchView(customerGroupName).shouldHave(text("Dynamic"));
        p.groupCounter_searchView(customerGroupName).shouldHave(text("0"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Dynamic")
    @Description("Can edit DCG name")
    public void editDCGName() throws IOException{
        provideTestData("empty dynamic group with one criteria");

        p = openPage(adminUrl +"/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        String editedCustomerGroupName = customerGroupName + " Edited";
        p.clickEditCGroupBtn();
        p.setCGroupName(editedCustomerGroupName);
        p.clickSaveCGroupBtn();
        openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.name_searchView(editedCustomerGroupName).shouldBe(visible);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Dynamic")
    @Description("Can add another criteria to existing DCG")
    public void addCriteriaToDCG() throws IOException {
        provideTestData("empty dynamic group with one criteria");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);
        p.clickEditCGroupBtn();
        p.setCriteriaMatcher("any");
        p.clickAddCriteriaBtn();
        p.setCriteria("2", "Name", "is", "Not Existing User Two");
        p.clickSaveCGroupBtn();
        openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.groupCriteriasCollection().shouldHaveSize(2);
        p.criteriaMatcherVal().shouldHave(text("any"));
        p.groupCriteriaVal("2").shouldHave(text("Not Existing User Two"));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Dynamic")
    @Description("Can remove DCG criteria")
    public void removeCriteriaFromDCG() throws IOException {
        provideTestData("dynamic group with two criterias");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);
        p.clickEditCGroupBtn();
        p.clickRemoveCriteriaBtn("2");
        p.clickSaveCGroupBtn();

        p.groupCriteriasCollection().shouldHaveSize(1);
        p.groupCriteriaVal("1").shouldHave(text("Not Existing User One"));
    }

//    TODO - Not possible to create Dynamic group without criterias
//    4. should be possible to archive group
//    - archived group disappears from the table
//    5. should be shown correct data when any or all match parameter is chosen
//    6. should be possible to add different criteria
//    8. should not be possible to delete customers from the group
//    9. should be possible to see customer statistics (preconditions with creation set of data)
}
