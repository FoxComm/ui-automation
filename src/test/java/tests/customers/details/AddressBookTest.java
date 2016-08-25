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
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }
}

    @Test(priority = 1)
    public void addNewAddress() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.addNewAddress(customerName, "2101 Green Valley", "Suite 300", "Seattle", "Washington", "98101", "9879879876");

        p.addressBook().shouldHaveSize(1);
//                "Failed to add new address to address book."

    }

    @Test(priority = 2)
    public void editNameFld() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editAddressBtn("1") );
        setFieldVal( p.nameFld(), "John Doe" );
        p.assertStateIsntReset();
        click( p.saveBtn() );
        p.nameFldVal("1").shouldHave(text("John Doe")
                .because("Failed to edit name field; expected: <John Doe>, actual: <" + p.nameFldVal("1") + ">."));

    }

    @Test(priority = 3)
    public void editAddress1Fld() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editAddressBtn("1") );
        setFieldVal( p.address1Fld(), "2525 Narrow Ave" );
        p.assertStateIsntReset();
        click( p.saveBtn() );
        p.address1FldVal("1").shouldHave(text("2525 Narrow Ave")
                .because("Failed to edit address1 field; expected: <2525 Narrow Ave>, actual: <" + p.address1FldVal("1") + ">."));

    }

    @Test(priority = 4)
    public void editAddress2Fld() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editAddressBtn("1") );
        setFieldVal( p.address2Fld(), "Suite 300" );
        p.assertStateIsntReset();
        click( p.saveBtn() );
        p.address2FldVal("1").shouldHave(text("Suite 300")
                .because("Failed to edit address1 field; expected: <Suite 300>, actual: <" + p.address2FldVal("1") + ">."));

    }

    @Test(priority = 5)
    public void editCityFld() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editAddressBtn("1") );
        setFieldVal( p.cityFld(), "New York" );
        p.assertStateIsntReset();
        click( p.saveBtn() );
        p.cityFldVal("1").should(matchesText("New York")
                .because("Failed to edit city field; expected: <New York>, actual: <" + p.cityFldVal("1") + ">."));

    }

    @Test(priority = 6)
    public void editStateDd() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editAddressBtn("1") );
        p.setState("New York");
        p.assertStateIsntReset();
        click( p.saveBtn() );
        p.stateVal("1").shouldHave(text("New York")
                .because("Failed to edit state dd value; expected: <New York>, actual: " + p.stateVal("1") + ">."));

    }

    @Test(priority = 7)
    public void editZipFld() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editAddressBtn("1") );
        setFieldVal( p.zipFld(), "10001" );
        p.assertStateIsntReset();
        click( p.saveBtn() );

        p.zipFldVal("1").shouldHave(text("10001")
                .because("Failed to edit zip field; expected: <10001>, actual: <" + p.zipFldVal("1") + ">."));

    }

    @Test(priority = 8)
    public void editPhoneNumberFld() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.editAddressBtn("1") );
        clearField( p.phoneNumberFld() );
        setFieldVal_delayed( p.phoneNumberFld(), "5551237575" );
        p.assertStateIsntReset();
        click( p.saveBtn() );

        p.phoneNumberFldVal("1").shouldHave(text("(555) 123-7575")
                .because("Failed to edit phone number in existing address at address book."));

    }

    @Test(priority = 9)
    public void deleteAddress() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        elementIsVisible( p.deleteAddressBtn("1") );
        int initialAddressBookSize = p.addressBook().size();
        click( p.deleteAddressBtn("1") );
        p.confirmDeletion();
        p.addressBook().shouldHave(sizeLessThan(initialAddressBookSize));

    }

}