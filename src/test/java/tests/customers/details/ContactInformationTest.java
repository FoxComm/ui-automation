package tests.customers.details;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertEquals;

public class ContactInformationTest extends DataProvider {

    private CustomersPage p;

    private String randomId = generateRandomID();
    private String newName = "Test Buddy-" + randomId;
    private String newEmail = "testbuddy." + randomId + "@mail.com";

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

    @Test(priority = 1)
    public void assertDefaultValues() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.nameVal_contactInfo().shouldHave(text(customerName)
                .because("Incorrect name is displayed in 'Contact Information'."));
        p.emailVal_contactInfo().shouldHave(text(customerEmail)
                .because("Incorrect email is displayed in 'Contact Information'."));

    }

    @Test(priority = 2)
    public void addPhoneNumber() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickEditBtn_contactInfo();
        p.setPhoneNumber_contactInfo("7779994242");
        p.clickSave();

        p.phoneNumberVal_contactInfo().shouldHave(text("7779994242")
                .because("Failed to set customer's phone number."));

    }

    @Test(priority = 3)
    public void editName() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickEditBtn_contactInfo();
        p.setPhoneNumber_contactInfo("7779994242");
        p.setName_contactInfo(newName);
        p.clickSave();

        p.nameVal_contactInfo().shouldHave(text(newName)
                .because("Failed to edit customer's name."));
        p.nameVal_overview().shouldHave(text(newName)
                .because("Failed to update name value at customer overview block."));

    }

    @Test(priority = 4)
    public void editEmail() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickEditBtn_contactInfo();
        p.setPhoneNumber_contactInfo("7779994242");
        p.setEmail_contactInfo(newEmail);
        p.clickSave();

        p.emailVal_contactInfo().shouldHave(text(newEmail)
                .because("Failed to edit customer's email."));
        p.emailVal_overview().shouldHave(text(newEmail)
                .because("Failed to update email value at customer overview block."));

    }

    @Test(priority = 5)
    public void phoneNumbFromBillAddress() throws IOException {

        provideTestData("customer with a credit card");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        assertEquals( p.phoneNumberVal_contactInfo(), "9879879876",
                "Phone number from billing address isn't displayed in customer contact information.");

//      move this assertion to overview-related test
//      assertEquals( p.phoneNumberVal_overview(),  "9879879876",
//                "Phone number from billing address isn't displayed in customer overview.");

    }

}
