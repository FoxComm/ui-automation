package tests.customers.details;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
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
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void assertDefaultValues() throws IOException {

        provideTestData("customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        assertEquals( p.nameVal_contactInfo(), customerName,
                "Incorrect name is displayed in 'Contact Information'.");
        assertEquals( p.emailVal_contactInfo(), customerEmail,
                "Incorrect email is displayed in 'Contact Information'.");

    }

    @Test(priority = 2)
    public void addPhoneNumber() throws IOException {

        provideTestData("customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editBtn_contactInfo() );
        setFieldVal( p.phoneNumberFld_contactInfo(), "7779994242" );
        click( p.saveBtn() );
        sleep(2000);
        assertEquals( p.phoneNumberVal_contactInfo(), "7779994242",
                "Failed to set customer's phone number.");

    }

    @Test(priority = 3)
    public void editName() throws IOException {

        provideTestData("customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editBtn_contactInfo() );
        setFieldVal( p.phoneNumberFld_contactInfo(), "7779994242" );
        setFieldVal( p.nameFld_contactInfo(), newName );
        click( p.saveBtn() );
        sleep(2000);
        assertEquals( p.nameVal_contactInfo(), newName,
                "Failed to edit customer's name.");
        assertEquals( p.nameVal_overview(), newName,
                "Failed to update name value at customer overview block.");

    }

    @Test(priority = 4)
    public void editEmail() throws IOException {

        provideTestData("customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editBtn_contactInfo() );
        setFieldVal( p.phoneNumberFld_contactInfo(), "7779994242" );
        setFieldVal( p.emailFld_contactInfo(), newEmail );
        click( p.saveBtn() );
        sleep(2000);
        assertEquals( p.emailVal_contactInfo(), newEmail,
                "Failed to edit customer's email.");
        assertEquals( p.emailVal_overview(), newEmail,
                "Failed to update email value at customer overview block.");

    }

//    @Test(priority = 5)
//    public void phoneNumbFromBillAddress() throws IOException {
//
//        provideTestData("customer with a credit card");
//        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);
//
//        assertEquals( p.phoneNumberVal_contactInfo(), "9879879876",
//                "Phone number from billing address isn't displayed in customer contact information.");
//
////      move this assertion to overview-related test
////      assertEquals( p.phoneNumberVal_overview(),  "9879879876",
////                "Phone number from billing address isn't displayed in customer overview.");
//
//    }

}
