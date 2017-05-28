package tests.storeadmin.customers.details;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CustomersPage;
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

public class ContactInformationTest extends Preconditions {

    private CustomersPage p;

    private String randomId = generateRandomID();
    private String newName = "Customer " + randomId;
    private String newEmail = "qatest2278+" + randomId + "@gmail.com";

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
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories({"Customers general behavior", "Customer Contact Information"})
    @Description("Can create new custoemr; User is redirected to customer details page after customer creation")
    public void createCustomer() {
        String email = "qatest2278+" + generateRandomID() + "@gmail.com";

        p = openPage(adminUrl + "/customers/", CustomersPage.class);
        p.clickAddCustomerBtn();
        p.setNewCustomerName("John Doe");
        p.setNewCustomerEmail("qatest2278+" + randomId + "@gmail.com");
        p.clickSaveBtn_modal();

        p.customerName().shouldHave(text("John Doe"));
        p.newCustomerEmail().shouldHave(text(email));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer contact information")
    @Description("'Name' and 'Email' are populated with what've been specified during customer creation")
    public void assertDefaultValues() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.nameVal_contactInfo().shouldHave(text(customerName));
        p.emailVal_contactInfo().shouldHave(text(customerEmail));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer contact information")
    @Description("Can add phone number to contact information")
    public void addPhoneNumber() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contactInfo();
        p.setPhoneNumber_contactInfo("7779994242");
        p.clickSave();

        p.phoneNumberVal_contactInfo().shouldHave(text("7779994242"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer contact information")
    @Description("Can edit customer name")
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
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Customer contact information")
    @Description("Can edit customer email")
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

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer contact information")
    @Description("Phone number from billing address should be transmitted to \"Contact Info\"")
    public void phoneNumbFromBillAddress() throws IOException {
        provideTestData("customer with a credit card");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.phoneNumberVal_contactInfo().shouldHave(text("9879879876"));
//      move this assertion to overview-related test
//      assertEquals( p.phoneNumberVal_overview(),  "9879879876",
//                "Phone number from billing address isn't displayed in customer overview.");

    }

}
