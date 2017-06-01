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
import static com.codeborne.selenide.Condition.visible;
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
    @Stories("Customers : Contacts")
    @Description("Can create new custoemr; User is redirected to customer details page after customer creation")
    public void createCustomer() {
        String uid = generateRandomID();
        String email = "qatest2278+" + uid + "@gmail.com";

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
    @Stories("Customers : Contacts")
    @Description("'Name' and 'Email' are populated with what've been specified during customer creation")
    public void assertDefaultValues() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.nameVal_contacts().shouldHave(text(customerName));
        p.emailVal_contacts().shouldHave(text(customerEmail));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customers : Contacts")
    @Description("Can edit customer name")
    public void editName() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contacts();
        p.setPhoneNumber_contacts("7779994242");
        p.setName_contacts(newName);
        p.clickSave();

        p.nameVal_contacts().shouldHave(text(newName));
        p.nameVal_overview().shouldHave(text(newName));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Customers : Contacts")
    @Description("Phone number is not required")
    public void nameIsRequired() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contacts();
        p.setPhoneNumber_contacts("9879879876");
        p.clearField(p.nameFld_contacts());
        p.clickSave();

        p.editBtn_contats().shouldBe(visible);
        p.errorMsg("fill out this field").shouldNotBe(visible);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Customers : Contacts")
    @Description("Can't set customer email to an already used one")
    public void editEmail_unique() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contacts();
        p.setPhoneNumber_contacts("7779994242");
        p.setEmail_contacts(newEmail);
        p.clickSave();

        p.emailVal_contacts().shouldHave(text(newEmail));
        p.emailVal_overview().shouldHave(text(newEmail));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Customers : Contacts")
    @Description("Can edit customer email")
    public void editEmail_used() throws IOException {
        provideTestData("two customers signed up on storefront");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contacts();
        p.setPhoneNumber_contacts("7779994242");
        p.setEmail_contacts(takenEmail);
        p.clickSave();

        p.errorMsg("already in use");
        p.emailVal_contacts().shouldHave(text(customerEmail));
        p.emailVal_overview().shouldHave(text(customerEmail));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Customers : Contacts")
    @Description("Phone number is not required")
    public void emailIsRequired() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contacts();
        p.setPhoneNumber_contacts("9879879876");
        p.clearField(p.emailFld_contacts());
        p.clickSave();

        p.editBtn_contats().shouldBe(visible);
        p.errorMsg("fill out this field").shouldNotBe(visible);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Customers : Contacts")
    @Description("Phone number is not required")
    public void phoneNumberNotRequired() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contacts();
        p.clickSave();

        p.editBtn_contats().shouldBe(visible);
        p.errorMsg("fill out this field").shouldNotBe(visible);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customers : Contacts")
    @Description("Can add phone number to contact information")
    public void addPhoneNumber() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditBtn_contacts();
        p.setPhoneNumber_contacts("7779994242");
        p.clickSave();

        p.phoneNumberVal_contacts().shouldHave(text("7779994242"));
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customers : Contacts")
    @Description("Phone number from billing address should be transmitted to \"Contact Info\"")
    public void phoneNumbFromBillAddress() throws IOException {
        provideTestData("customer with a credit card");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.phoneNumberVal_contacts().shouldHave(text("9879879876"));
//      move this assertion to overview-related test
//      assertEquals( p.phoneNumberVal_overview(),  "9879879876",
//                "Phone number from billing address isn't displayed in customer overview.");

    }

}
