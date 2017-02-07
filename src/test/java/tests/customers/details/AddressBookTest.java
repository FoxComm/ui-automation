package tests.customers.details;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.sizeLessThan;
import static com.codeborne.selenide.Condition.matchesText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

public class AddressBookTest extends DataProvider {

    private CustomersPage p;

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
    public void addNewAddress() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickNewAddressBtn();
        p.addNewAddress(customerName, "2101 Green Valley", "Suite 300", "Seattle", "Washington", "98101", "9879879876");
        p.clickSave();

        p.addressBook().shouldHaveSize(1);
    }

    @Test(priority = 2)
    public void editNameFld() throws IOException {
        provideTestData("customer with a shipping address");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditAddressBtn("1");
        p.setName("John Doe");
        p.assertStateIsntReset();
        p.clickSave();

        p.nameFldVal("1").shouldHave(text("John Doe")
                .because("Failed to edit name field; expected: <John Doe>, actual: <" + p.nameFldVal("1") + ">."));
    }

    @Test(priority = 3)
    public void editAddress1Fld() throws IOException {
        provideTestData("customer with a shipping address");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditAddressBtn("1");
        p.setAddress1("2525 Narrow Ave");
        p.assertStateIsntReset();
        p.clickSave();

        p.address1FldVal("1").shouldHave(text("2525 Narrow Ave")
                .because("Failed to edit address1 field; expected: <2525 Narrow Ave>, actual: <" + p.address1FldVal("1") + ">."));
    }

    @Test(priority = 4)
    public void editAddress2Fld() throws IOException {
        provideTestData("customer with a shipping address");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditAddressBtn("1");
        p.setAddress2("Suite 300");
        p.assertStateIsntReset();
        p.clickSave();

        p.address2FldVal("1").shouldHave(text("Suite 300")
                .because("Failed to edit address1 field; expected: <Suite 300>, actual: <" + p.address2FldVal("1") + ">."));
    }

    @Test(priority = 5)
    public void editCityFld() throws IOException {
        provideTestData("customer with a shipping address");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditAddressBtn("1");
        p.setCity("New York");
        p.assertStateIsntReset();
        p.clickSave();

        p.cityFldVal("1").should(matchesText("New York")
                .because("Failed to edit city field; expected: <New York>, actual: <" + p.cityFldVal("1") + ">."));
    }

    @Test(priority = 6)
    public void editStateDd() throws IOException {
        provideTestData("customer with a shipping address");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditAddressBtn("1");
        p.setState("New York");
        p.assertStateIsntReset();
        p.clickSave();

        p.stateVal("1").shouldHave(text("New York")
                .because("Failed to edit state dd value; expected: <New York>, actual: " + p.stateVal("1") + ">."));
    }

    @Test(priority = 7)
    public void editZipFld() throws IOException {
        provideTestData("customer with a shipping address");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditAddressBtn("1");
        p.setZip("10001");
        p.assertStateIsntReset();
        p.clickSave();

        p.zipFldVal("1").shouldHave(text("10001")
                .because("Failed to edit zip field; expected: <10001>, actual: <" + p.zipFldVal("1").text() + ">."));
    }

    @Test(priority = 8)
    public void editPhoneNumberFld() throws IOException {
        provideTestData("customer with a shipping address");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.clickEditAddressBtn("1");
        clearField( p.phoneNumberFld() );
        p.setPhoneNumber("5551237575");
        p.assertStateIsntReset();
        p.clickSave();

        p.phoneNumberFldVal("1").shouldHave(text("(555) 123-7575")
                .because("Failed to edit phone number in existing address at address book."));
    }

    @Test(priority = 9)
    public void deleteAddress() throws IOException {
        provideTestData("customer with a shipping address");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        shouldBeVisible( p.deleteAddressBtn("1"), "Address book seems to be empty" );
        int initAddressBookSize = p.addressBook().size();
        p.deleteAddress("1");
        p.confirmDeletion();

        p.addressBook().shouldHave(sizeLessThan(initAddressBookSize));
    }

}