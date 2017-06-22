package tests.storeadmin.customergroups;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CustomerGroupPage;
import pages.admin.LoginPage;
import ru.yandex.qatools.allure.annotations.*;
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

        p.name_searchView(customerGroupName).shouldBe(visible);
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

        p.name_searchView(editedMCGName).shouldBe(visible);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can search customer from MCG")
    public void searchCustomersMCG() throws IOException{
        provideTestData("manual group and customer for adding/searching");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.clickAddCustomersBtn();
        p.setCustomerSearchFld(customerName);

        p.customerNameSearchResultFld(customerName).shouldBe(visible);
        p.customerEmailSearchResultFld(customerEmail).shouldBe(visible);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can't search not-existing customer from MCG")
    public void searchNotExistingCustomerMCG() throws IOException{
        provideTestData("manual group and customer for adding/searching");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.clickAddCustomersBtn();
        p.setCustomerSearchFld(customerName + "_wrong");

        p.customerNameSearchResultFld(customerName).shouldNotBe(visible);
        p.customerEmailSearchResultFld(customerEmail).shouldNotBe(visible);
        p.customerSearchNoResultsFld().shouldBe(visible);
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can add customer to MCG, customer is shown in table in form")
    public void addCustomersMCG() throws IOException{
        provideTestData("manual group and customer for adding/searching");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.clickAddCustomersBtn();
        p.setCustomerSearchFld(customerName);
        p.clickFoundCustomerFld(customerName);
        p.clickSaveCustomerBtn();

        openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.name_searchView(customerName).shouldBe(visible);
        p.groupCounter_form().shouldHave(text("1"));
        p.groupCustomersCollection().shouldHaveSize(1);
    }

    @Test(priority = 10)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can add 2nd customer to MCG, form counter will be updated")
    public void updateCustomersMCG() throws IOException{
        provideTestData("manual group and customer added to it");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        provideTestData("a customer");
        p.clickAddCustomersBtn();
        p.setCustomerSearchFld(customerName);
        p.clickFoundCustomerFld(customerName);
        p.clickSaveCustomerBtn();

        openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.name_searchView(customerName).shouldBe(visible);
        p.groupCustomersCollection().shouldHaveSize(2);
        p.groupCounter_form().shouldHave(text("2"));
    }

    @Test(priority = 11)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can update customers counter for MCG in search view")
    public void customersCounterMCG_searchView() throws IOException{
        provideTestData("manual group and customer added to it");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        provideTestData("a customer");
        p.clickAddCustomersBtn();
        p.setCustomerSearchFld(customerName);
        p.clickFoundCustomerFld(customerName);
        p.clickSaveCustomerBtn();

        openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.groupCounter_searchView(customerGroupName).shouldHave(text("2"));
    }

    @Test(priority = 12)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can remove customer from MCG")
    public void removeCustomerMCG() throws IOException{
        provideTestData("manual group and customer added to it");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.clickSearchViewElementChkbx("1");
        p.openActionsMenu_searchView();
        p.clickDeleteFromGroupBtn();
        p.clickDeleteConfirmBtn();

        p.removingMessageFld().shouldHave(text("1 customer successfully deleted from group"));

        openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.name_searchView(customerName).shouldNotBe(visible);
        p.groupCustomersCollection().shouldHaveSize(0);

        openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);

        p.groupCounter_searchView(customerGroupName).shouldHave(text("0"));
    }

    @Test(priority = 13)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Groups : Manual")
    @Description("Can archive MCG")
    public void archiveMCG() throws IOException{
        provideTestData("manual group and customer added to it");

        p = openPage(adminUrl + "/customers/groups/" + customerGroupId, CustomerGroupPage.class);

        p.clickArchiveGroupBtn();
        p.clickConfirmArchiveGroupBtn();

        openPage(adminUrl + "/customers/groups", CustomerGroupPage.class);
        
        p.name_searchView(customerGroupName).shouldNotBe(visible);
    }

//    @Test(priority = 14)
//    @Severity(SeverityLevel.NORMAL)
//    @Features("Ashes")
//    @Stories("Customer Groups : Manual")
//    @Description("Can't create MCG with duplicated name")
//    @Issue("highlander/#1866")
//    public void duplicatedNameMCG() throws IOException{
//        //NOT Fixed
//        //TODO warning should be thrown
//    }
//
//    @Test(priority = 15)
//    @Severity(SeverityLevel.NORMAL)
//    @Features("Ashes")
//    @Stories("Customer Groups : Manual")
//    @Description("Can show correct statistics for number of orders and total sales for MCG")
//    @Issue("highlander/#1866")
//    public void correctStatisticsMCG_ordersSales() throws IOException{
//        //create customers with few orders for them
//        //should show correct number of orders and value of total sales
//    }
}
