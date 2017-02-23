package tests.storeadmin.customers.details;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

public class ContactInformationTest extends DataProvider {

    private CustomersPage p;

    private String randomId = generateRandomID();
    private String newName = "Test Buddy-" + randomId;
    private String newEmail = "qatest2278+" + randomId + "@mail.com";

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
    @Description("User is redirected to customer details page after customer creation")
    public void createCustomer() {
        p = openPage(adminUrl + "/customers/", CustomersPage.class);
        p.clickAddCustomerBtn();
        p.setNewCustomerName("John Doe");
        p.setNewCustomerEmail("qatest2278+" + generateRandomID() + "@gmail.com");
        p.clickSaveBtn_modal();

        p.customerName().shouldHave(text("John Doe"));
    }

    @Test(priority = 2)
    public void assertDefaultValues() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.nameVal_contactInfo().shouldHave(text(customerName));
        p.emailVal_contactInfo().shouldHave(text(customerEmail));
    }

    @Test(priority = 3)
    public void addPhoneNumber() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contactInfo();
        p.setPhoneNumber_contactInfo("7779994242");
        p.clickSave();

        p.phoneNumberVal_contactInfo().shouldHave(text("7779994242"));
    }

    @Test(priority = 4)
    public void editName() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contactInfo();
        p.setPhoneNumber_contactInfo("7779994242");
        p.setName_contactInfo(newName);
        p.clickSave();

        p.nameVal_contactInfo().shouldHave(text(newName));
        p.nameVal_overview().shouldHave(text(newName));
    }

    @Test(priority = 5)
    public void editEmail() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contactInfo();
        p.setPhoneNumber_contactInfo("7779994242");
        p.setEmail_contactInfo(newEmail);
        p.clickSave();

        p.emailVal_contactInfo().shouldHave(text(newEmail));
        p.emailVal_overview().shouldHave(text(newEmail));

    }

    @Description("Phone number from billing address should be transmitted to \"Contact Info\"")
    @Test(priority = 6)
    public void phoneNumbFromBillAddress() throws IOException {
        provideTestData("customer with a credit card");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.phoneNumberVal_contactInfo().shouldHave(text("9879879876"));
//      move this assertion to overview-related test
//      assertEquals( p.phoneNumberVal_overview(),  "9879879876",
//                "Phone number from billing address isn't displayed in customer overview.");

    }

}
