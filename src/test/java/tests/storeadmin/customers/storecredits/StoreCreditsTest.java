package tests.storeadmin.customers.storecredits;

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
import static com.codeborne.selenide.Selenide.open;

public class StoreCreditsTest extends Preconditions {

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
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Customer Store Credits")
    @Description("Can issue SC with of 'CSR Appeasement' type")
    public void issueSC_csrAppeasement() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.navToSCTab();
        p.clickNewSCBtn();
        p.selectType("Csr Appeasement");
        p.setValue("50");
        p.clickIssueSCButton();

        p.availableBalance().shouldHave(text("$50.00"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Store Credits")
    @Description("Can issue SC with preset values")
    public void issueSC_presetValues() throws IOException {
        provideTestData("a customer");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.navToSCTab();
        p.clickNewSCBtn();
        p.selectType("Csr Appeasement");
        p.setValue("100");
        p.clickIssueSCButton();

        p.availableBalance().shouldHave(text("$100.00"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Customer Store Credits")
    @Description("Can transfer gift card to store credits")
    public void issueSC_gcTransfer() throws IOException {
        provideTestData("a customer && GC");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.navToSCTab();
        p.clickNewSCBtn();
        p.selectType("Gift Card Transfer");
        p.setGCNumber(gcCode);
        shouldHaveText(p.gcAvailableBalanceVal(), "$125.00", "GC available balance isn't displayed/refreshed.");
        p.clickIssueSCButton();

        p.availableBalance().shouldHave(text("$125.00"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("Customer Store Credits")
    @Description("Issues SC is displayed on the list")
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

    @Test(priority = 5, dependsOnMethods = "issuedSC_displayedOnList")
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("Customer Store Credits")
    @Description("Can set SC on hold")
    public void setState_onHold() throws IOException {
        provideTestData("a customer with issued SC");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.navToSCTab();
        waitForDataToLoad();
        p.changeSet("1", "On Hold");

        p.getSCParamVal("1", "State").shouldHave(text("On Hold"));
    }

    @Test(priority = 6, dependsOnMethods = "issuedSC_displayedOnList")
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("Customer Store Credits")
    @Description("Can cancel SC")
    public void setState_canceled() throws IOException {
        provideTestData("a customer with issued SC");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.navToSCTab();
        waitForDataToLoad();
        p.changeSet("1", "Cancel Store Credit");

        p.getSCParamVal("1", "State").shouldHave(text("Canceled"));
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("Customer Store Credits")
    @Description("[CSR Appeasement type] Transaction appears on the list and has correct 'Amount' value")
    public void checkTransaction_csrAppeasement() throws IOException {
        provideTestData("order in Remorse Hold, payed with SC (CSR Appeasement)");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.navToSCTab();
        p.navToTransactionTab();
        waitForDataToLoad();

        p.getTransactionParamVal("1", "Amount").shouldHave(text("-$50.00"));
//        p.getTransactionParamVal("1", "Transaction").shouldHave(text("CSR Appeasement")
//                .because("Incorrect transaction type."));

    }

    @Test(priority = 8)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("Customer Store Credits")
    @Description("[GC Transfer type] Transaction appears on the list and has correct 'Amount' value")
    public void checkTransaction_gcTransfer() throws IOException {
        provideTestData("order in Remorse Hold, payed with SC (GC Transfer)");

        p = openPage(adminUrl + "/customers/" + customerId, CustomersPage.class);
        p.navToSCTab();
        p.navToTransactionTab();
        waitForDataToLoad();

        p.getTransactionParamVal("1", "Amount").shouldHave(text("-$50.00"));
//        p.getTransactionParamVal("1", "Transaction").shouldHave(text("Gift Card Transfer")
//                .because("Incorrect transaction type."));
    }

}
