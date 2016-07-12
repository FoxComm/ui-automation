package tests.customers.details;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomerPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;

public class CreditCardsTest extends DataProvider {

    private CustomerPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void addNewCC_newBillAddress() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.addNewCreditCardBtn() );
        p.addNewBillAddress(customerName, "2101 Green Valley", "Suite 300", "Seattle", "Washington", "98101", "9879876");
        p.addNewCreditCard(customerName, "5555555555554444", "777", "2", "2020");
        click( p.saveBtn() );
        p.assertCardAdded(customerName);

    }

    @Test(priority = 2)
    public void addNewCC_existingBillAddress() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.addNewCreditCardBtn() );
        p.addNewCreditCard(customerName, "5555555555554444", "777", "2", "2020");
        click( p.chooseBtn("1") );
        click( p.saveBtn() );
        sleep(1500);
        p.assertCardAdded(customerName);

    }

    @Test(priority = 3)
    public void editCC_holderName() throws IOException {

        provideTestData("customer with a credit card");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.editCreditCardBtn("1") );
        setFieldVal( p.holderNameFld(), "John Doe" );
        click( p.saveBtn() );
        sleep(1500);
        assertEquals( p.holderNameFldVal("1"), "John Doe",
                "Failed to edit holderName field; expected: <John Doe>, actual: <" + p.holderNameFldVal("1") + ">.");

    }

    @Test(priority = 4)
    public void editCC_expirationDate() throws IOException {

        provideTestData("customer with a credit card");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.editCreditCardBtn("1") );
        p.setExpirationDate("12", "2030");
        click( p.saveBtn() );
        sleep(2500);
        assertEquals( p.expirationDateVal("1"), "12/2030",
                "Failed to edit expiration date; expected: <12/2030>, actual: <" + p.expirationDateVal("1") + ">.");

    }

    @Test(priority = 5)
    public void editCC_billAddress() throws IOException {

        provideTestData("customer with a credit card and 2 addresses");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.editCreditCardBtn("1") );
        click( p.changeBillAddressBtn() );
        click( p.chooseBtn("2") );
        click( p.saveBtn() );
        sleep(2500);
        assertEquals( p.billCityVal("1"), "New Jersey",
                "Failed to choose different billing address; expected city: <New Jersey>, actual: <" + p.billCityVal("1") + ">.");

    }

    @Test(priority = 6)
    public void editBillAddressInAddressBook() throws IOException {

        provideTestData("customer with a credit card");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click (p.editAddressBtn("1"));
        setFieldVal( p.nameFld(), "John Doe" );
        click( p.saveBtn() );
        sleep(2500);
        assertEquals( p.nameFldVal_billAddress("1"), "John Doe",
                "Billing address failed to get updated after editing a corresponding address in address book." +
                        "\n expected: <John Doe>, actual <" + p.nameFldVal_billAddress("1") + ">.");

    }

}
