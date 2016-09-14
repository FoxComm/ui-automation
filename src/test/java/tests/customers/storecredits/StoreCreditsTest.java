package tests.customers.storecredits;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class StoreCreditsTest extends DataProvider {

    private CustomersPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
            loginPage.userMenuBtn().shouldBe(visible);
        }

    }

    @Test(priority = 1)
    public void issueSC_csrAppeasement() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        setFieldVal( p.valueFld(), "50" );
        click( p.submitBtn() );

        p.totalAvailableBalance().shouldHave(text("$50.00")
                .because("Current available balance value is incorrect."));

    }

    @Test(priority = 2)
    public void issueSC_gcTransfer() throws IOException {

        provideTestData("a customer && GC");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Gift Card Transfer");
        setFieldVal( p.gcNumberFld(), gcCode);
        sleep(2000);
        p.availableBalanceVal().shouldHave(text("$125.00")
                .because("GC available balance isn't displayed."));
        click( p.submitBtn() );
        p.availableBalanceVal().shouldHave(text("$125.00")
                .because("Current available balance value is incorrect."));

    }

    @Test(priority = 3)
    public void issuedSC_displayedOnList() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        setFieldVal( p.valueFld(), "50" );
        click( p.submitBtn() );
        p.newSCBtn().shouldBe(visible);     //bug workaround
        refresh();                          //bug workaround
        waitForDataToLoad();

        p.storeCreditsOnList().shouldHaveSize(1);
//                "A just issued SC isn't displayed on the list."
        p.getSCParamVal("1", "State").shouldHave(text("Active").because("A just issued SC isn't in 'Active' state."));

    }

    @Test(priority = 4)
    public void issueSC_presetValues() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        click( p.presetValue("100") );
        click( p.submitBtn() );
        sleep(2000);
        p.availableBalanceVal().shouldHave(text("$100.00")
                .because("Current available balance value is incorrect."));

        p.storeCreditsOnList().shouldHaveSize(1);
//                "A just issued SC isn't displayed on the list."
        p.getSCParamVal("1", "Original Balance").shouldHave(text("$100.00")
                .because("A just issued SC's Original Balance value is incorrect."));

    }

    @Test(priority = 5)
    public void setState_onHold() throws IOException {

        provideTestData("a customer with issued SC");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.storeCreditTab() );
        waitForDataToLoad();
        p.setState("1", "On Hold");

        p.getSCParamVal("1", "State").shouldHave(text("On Hold")
                .because("Failed to change SC state."));

    }

    @Test(priority = 6)
    public void setState_canceled() throws IOException {

        provideTestData("a customer with issued SC");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.storeCreditTab() );
        waitForDataToLoad();
        p.setState("1", "Cancel Store Credit");

        p.getSCParamVal("1", "State").shouldHave(text("Canceled")
                .because("Failed to change SC state."));

    }

    @Test(priority = 7)
    public void checkTransaction_csrAppeasement() throws IOException {

        provideTestData("order in Remorse Hold, payed with SC (CSR Appeasement)");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.storeCreditTab() );
        click( p.transactionTab() );
        waitForDataToLoad();
        p.getTransactionParamVal("1", "Amount").shouldHave(text("-$36.00")
                .because("Incorrect amount of funds was applied to order as a payment."));
        p.getTransactionParamVal("1", "Transaction").shouldHave(text("CSR Appeasement")
                .because("Incorrect transaction type."));

    }

    @Test(priority = 8)
    public void checkTransaction_gcTransfer() throws IOException {

        provideTestData("order in Remorse Hold, payed with SC (GC Transfer)");
        p = open(adminUrl + "/customers/" + customerId, CustomersPage.class);

        click( p.storeCreditTab() );
        click( p.transactionTab() );
        waitForDataToLoad();
        p.getTransactionParamVal("1", "Amount").shouldHave(text("-$36.00")
                .because("Incorrect amount of funds was applied to order as a payment."));
        p.getTransactionParamVal("1", "Transaction").shouldHave(text("Gift Card Transfer")
                .because("Incorrect transaction type."));

    }

}
