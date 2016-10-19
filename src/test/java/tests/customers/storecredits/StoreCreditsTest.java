package tests.customers.storecredits;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class StoreCreditsTest extends DataProvider {

    private CustomersPage p;

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
    public void issueSC_csrAppeasement() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.navToSCTab();
        p.clickNewSCBtn();
        p.selectType("Csr Appeasement");
        p.setValue("50");
        p.clickIssueSCButton();

        p.totalAvailableBalance().shouldHave(text("$50.00")
                .because("Current available balance value is incorrect."));

    }

    @Test(priority = 2)
    public void issueSC_gcTransfer() throws IOException {

        provideTestData("a customer && GC");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.navToSCTab();
        p.clickNewSCBtn();
        p.selectType("Gift Card Transfer");
        p.setGCNumber(gcCode);
        shouldHaveText(p.gcAvailableBalanceVal(), "$125.00", "GC available balance isn't displayed.");
        p.clickIssueSCButton();
        p.availableBalanceVal().shouldHave(text("$125.00")
                .because("Current available balance value is incorrect."));

    }

    @Test(priority = 3)
    public void issuedSC_displayedOnList() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.navToSCTab();
        p.clickNewSCBtn();
        p.selectType("Csr Appeasement");
        p.setValue("50");
        p.clickIssueSCButton();
        waitForDataToLoad();

        p.storeCreditsOnList().shouldHaveSize(1);

    }

    @Test(priority = 4)
    public void issueSC_presetValues() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.navToSCTab();
        p.clickNewSCBtn();
        p.selectType("Csr Appeasement");
        p.setValue("100");
        p.clickIssueSCButton();
        shouldHaveText(p.availableBalanceVal(), "$100.00",
                "Current available balance value is incorrect.");

    }

    @Test(priority = 5)
    public void setState_onHold() throws IOException {

        provideTestData("a customer with issued SC");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.navToSCTab();
        waitForDataToLoad();
        p.changeSet("1", "On Hold");

        p.getSCParamVal("1", "State").shouldHave(text("On Hold")
                .because("Failed to change SC state."));

    }

    @Test(priority = 6)
    public void setState_canceled() throws IOException {

        provideTestData("a customer with issued SC");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.navToSCTab();
        waitForDataToLoad();
        p.changeSet("1", "Cancel Store Credit");

        p.getSCParamVal("1", "State").shouldHave(text("Canceled")
                .because("Failed to change SC state."));

    }

    @Test(priority = 7)
    public void checkTransaction_csrAppeasement() throws IOException {

        provideTestData("order in Remorse Hold, payed with SC (CSR Appeasement)");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.navToSCTab();
        p.navToTransactionTab();
        waitForDataToLoad();
        p.getTransactionParamVal("1", "Amount").shouldHave(text("-$36.00")
                .because("Incorrect amount of funds was applied to order as a payment."));
//        p.getTransactionParamVal("1", "Transaction").shouldHave(text("CSR Appeasement")
//                .because("Incorrect transaction type."));

    }

    @Test(priority = 8)
    public void checkTransaction_gcTransfer() throws IOException {

        provideTestData("order in Remorse Hold, payed with SC (GC Transfer)");
        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);

        p.navToSCTab();
        p.navToTransactionTab();
        waitForDataToLoad();
        p.getTransactionParamVal("1", "Amount").shouldHave(text("-$36.00")
                .because("Incorrect amount of funds was applied to order as a payment."));
//        p.getTransactionParamVal("1", "Transaction").shouldHave(text("Gift Card Transfer")
//                .because("Incorrect transaction type."));

    }

}
