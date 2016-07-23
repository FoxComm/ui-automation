package tests.customers.storecredits;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomerPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertEquals;

public class StoreCreditsTest extends DataProvider {

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
    public void issueSC_csrAppeasement() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        setFieldVal( p.valueFld(), "50" );
        click( p.submitBtn() );
        sleep(1000);

        assertEquals( p.availableBalanceVal(), 50.00,
                "Current available balance value is incorrect.");

    }

    @Test(priority = 2)
    public void issueSC_gcTransfer() throws IOException {

        provideTestData("a customer && GC");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Gift Card Transfer");
        setFieldVal( p.gcNumberFld(), gcNumber );
        sleep(2000);
        assertEquals( p.gcAvailableBalanceVal(), 125.00,
                "GC available balance isn't displayed.");
        click( p.submitBtn() );
        assertEquals( p.availableBalanceVal(), 125.00,
                "Current available balance value is incorrect.");

    }

    @Test(priority = 3)
    public void issuedSC_displayedOnList() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        setFieldVal( p.valueFld(), "50" );
        click( p.submitBtn() );
        p.newSCBtn().shouldBe(visible);     //bug workaround
        refresh();                          //bug workaround
        waitForDataToLoad();

        assertEquals( p.amountOfSCs(), 1,
                "A just issued SC isn't displayed on the list.");
        assertEquals( p.getSCParamVal("1", "State"), "Active",
                "A just issued SC isn't in 'Active' state.");

    }

    @Test(priority = 4)
    public void issueSC_presetValues() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        click( p.presetValue("100") );
        click( p.submitBtn() );
        sleep(2000);
        assertEquals( p.availableBalanceVal(), 100.00,
                "Current available balance value is incorrect.");

        assertEquals( p.amountOfSCs(), 1,
                "A just issued SC isn't displayed on the list.");
        assertEquals( p.getSCParamVal("1", "Original Balance"), "$100.00",
                "A just issued SC's Original Balance value is incorrect.");

    }

    @Test(priority = 5)
    public void setState_onHold() throws IOException {

        provideTestData("a customer with issued SC");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        waitForDataToLoad();
        p.setState("1", "On Hold");

        assertEquals( p.getSCParamVal("1", "State"), "On Hold",
                "Failed to change SC state.");

    }

    @Test(priority = 6)
    public void setState_canceled() throws IOException {

        provideTestData("a customer with issued SC");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        waitForDataToLoad();
        p.setState("1", "Cancel Store Credit");

        assertEquals( p.getSCParamVal("1", "State"), "Canceled",
                "Failed to change SC state.");

    }

    @Test(priority = 7)
    public void checkTransaction_csrAppeasement() throws IOException {

        provideTestData("order in Remorse Hold, payed with SC (CSR Appeasement)");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        click( p.transactionTab() );
        waitForDataToLoad();
        assertEquals( p.getTransactionParamVal("1", "Amount"), "-$37.27",
                "Incorrect amount of funds was applied to order as a payment.");
        assertEquals( p.getTransactionParamVal("1", "Transaction"), "CSR Appeasement",
                "Incorrect transaction type.");

    }

    @Test(priority = 8)
    public void checkTransaction_gcTransfer() throws IOException {

        provideTestData("order in Remorse Hold, payed with SC (GC Transfer)");
        p = open(adminUrl + "/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        click( p.transactionTab() );
        waitForDataToLoad();
        assertEquals( p.getTransactionParamVal("1", "Amount"), "-$37.27",
                "Incorrect amount of funds was applied to order as a payment.");
        assertEquals( p.getTransactionParamVal("1", "Transaction"), "Gift Card Transfer",
                "Incorrect transaction type.");

    }

}
