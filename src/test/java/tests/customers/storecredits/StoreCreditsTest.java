package tests.customers.storecredits;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomerPage;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;

public class StoreCreditsTest extends DataProvider {

    private CustomerPage p;
    private OrderDetailsPage orderPage;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open("http://admin.stage.foxcommerce.com/");
        if ( (Objects.equals(getUrl(), "http://admin.stage.foxcommerce.com/login")) ) {
            LoginPage loginPage = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void issueSC() throws IOException {

        provideTestData("a customer");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        double expectedResult = p.availableBalanceVal() + 50.00;
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        setFieldVal( p.valueFld(), "50" );
        click( p.issueSCBtn() );
        sleep(1000);

        assertEquals( p.availableBalanceVal(), expectedResult,
                "Current available balance value is incorrect.");

    }

    @Test(priority = 2)
    public void issuedSC_displayedOnList() throws IOException {

        provideTestData("a customer");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        setFieldVal( p.valueFld(), "50" );
        click( p.issueSCBtn() );
        p.waitForDataToLoad();

        assertEquals( p.amountOfSCs(), 1,
                "A just issued SC isn't displayed on the list.");
        assertEquals( p.getSCParamValue("1", "State"), "Active",
                "A just issued SC isn't in 'Active' state.");

    }

    @Test(priority = 3)
    public void issueSC_presetValues() throws IOException {

        provideTestData("a customer");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        click( p.newSCBtn() );
        p.selectType("Csr Appeasement");
        click( p.presetValues("100") );
        click( p.issueSCBtn() );
        p.waitForDataToLoad();

        assertEquals( p.amountOfSCs(), 1,
                "A just issued SC isn't displayed on the list.");
        assertEquals( p.getSCParamValue("1", "Original Balance"), "$100",
                "A just issued SC's Original Balance value is incorrect.");

    }

    @Test(priority = 4)
    public void setState_onHold() throws IOException {

        provideTestData("a customer with issued SC");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        p.setState("1", "On Hold");

        assertEquals( p.getSCParamValue("1", "State"), "On Hold",
                "Failed to change SC state.");

    }

    @Test(priority = 5)
    public void setState_canceled() throws IOException {

        provideTestData("a customer with issued SC");
        p = open("http://admin.stage.foxcommerce.com/customers/" + customerId, CustomerPage.class);

        click( p.storeCreditTab() );
        p.setState("1", "Cancel Store Credit");

        assertEquals( p.getSCParamValue("1", "State"), "Canceled",
                "Failed to change SC state.");

    }

}
