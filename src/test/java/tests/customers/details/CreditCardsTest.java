package tests.customers.details;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

public class CreditCardsTest extends DataProvider {

    private CustomersPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

    @Test(priority = 1)
    public void addNewCC_newBillAddress() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickAddNewCCBtn();
        p.fillOutNewCCForm(customerName, "5555555555554444", "777", "2", "2020");
        p.addNewBillAddress(customerName, "2101 Green Valley", "Suite 300", "Seattle", "Washington", "98101", "9879879876");
        p.clickSave();
        p.assertCardAdded(customerName);

    }

    @Test(priority = 2)
    public void addNewCC_existingBillAddress() throws IOException {

        provideTestData("customer with a shipping address");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickAddNewCCBtn();
        p.fillOutNewCCForm(customerName, "5555555555554444", "777", "2", "2020");
        p.chooseAddress("1");
        p.clickSave();
        p.assertCardAdded(customerName);

    }

    @Test(priority = 3)
    public void editCC_holderName() throws IOException {

        provideTestData("customer with a credit card");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickEditCCBtn("1");
        p.clearField( p.holderNameFld() );
        p.setHolderName("John Doe");
        p.clickSave();
        p.holderNameFldVal("1").shouldHave(text("John Doe")
                .because("Failed to edit holderName field; expected: <John Doe>, actual: <" + p.holderNameFldVal("1") + ">."));

    }

    @Test(priority = 4)
    public void editCC_expirationDate() throws IOException {

        provideTestData("customer with a credit card");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickEditCCBtn("1");
        p.setExpirationDate("12", "2030");
        p.clickSave();
        p.expirationDateVal("1").shouldHave(text("12/2030")
                .because("Failed to edit expiration date; expected: <12/2030>, actual: <" + p.expirationDateVal("1") + ">."));

    }

    @Test(priority = 5)
    public void editCC_billAddress() throws IOException {

        provideTestData("customer with a credit card and 2 addresses");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickEditCCBtn("1");
        p.clickChangeBillAddressBtn();
        p.chooseAddress("2");
        p.clickSave();
        p.billCityVal("1").should(matchText("New Jersey")
                .because("Failed to choose different billing address; expected city: <New Jersey>, actual: <" + p.billCityVal("1") + ">."));

    }

    @Test(priority = 6)
    public void editBillAddressInAddressBook() throws IOException {

        provideTestData("customer with a credit card");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.clickEditAddressBtn("1");
        p.setName("John Doe");
        p.clickSave();
        p.nameFldVal_billAddress("1").shouldHave(text(customerName)
                .because("Billing address has been modified (should be unchangeable once it's set)."));

    }

}
