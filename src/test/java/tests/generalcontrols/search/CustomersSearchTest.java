package tests.generalcontrols.search;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CustomersPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class CustomersSearchTest extends DataProvider {

    private CustomersPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            loginPage.userMenuBtn().shouldBe(visible);
        }

    }

    @Test(priority = 1)
    public void customerName_full() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Customer", "Name", customerName);
        waitForDataToLoad();

        p.itemOnList(customerName).shouldBe(visible
                .because("Search has failed to find a customer with requested name."));
        p.searchResults().shouldHaveSize(1);

    }

    @Test(priority = 2)
    public void customerName_firstName() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Customer", "Name", customerName.substring(0, customerName.indexOf(" ")));
        waitForDataToLoad();

        p.itemOnList(customerName).shouldBe(visible
                .because("Search has failed to find a customer with requested part of the name."));

    }

    @Test(priority = 3)
    public void customerName_lastName() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Customer", "Name", customerName.substring(customerName.indexOf(" "), customerName.length()));
        p.itemOnList(customerName).shouldBe(visible
                .because("Search has failed to find a customer with requested part of the name."));
        p.searchResults().shouldHaveSize(1);

    }

    @Test(priority = 4)
    public void customerEmail_full() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Customer", "Email", customerEmail);
        p.itemOnList(customerEmail).shouldBe(visible
                .because("Search has failed to find a customer with requested email."));
        p.searchResults().shouldHaveSize(1);

    }

    @Test(priority = 5)
    public void customerEmail_part() throws IOException {

        provideTestData("a customer");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Customer", "Email", customerEmail.substring(0, customerEmail.indexOf("@")));
        p.itemOnList(customerEmail).shouldBe(visible
                .because("Search has failed to find a customer with requested email user name."));
        p.searchResults().shouldHaveSize(1);

    }

    @Test(priority = 6)
    public void customerRevenue() {}

    @Test(priority = 7)
    public void customerRanking() {}

    @Test(priority = 8)
    public void customerDateJoined_equals() throws IOException {}

    @Test(priority = 9)
    public void orderRefNum_uppercase() throws IOException {

        provideTestData("order state: remorse hold");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "Reference Number", orderId);
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with requested order refNum (uppercase)."));
        p.searchResults().shouldHaveSize(1);

    }

    @Test(priority = 10)
    public void orderRefNum_lowercase() throws IOException {

        provideTestData("order state: remorse hold");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "Reference Number", orderId.toLowerCase());
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with requested order refNum (lowercase)."));
        p.searchResults().shouldHaveSize(1);

    }

    @Test(priority = 11)
    public void orderRefNum_incorrectVal() throws IOException {

        provideTestData("order state: remorse hold");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "Reference Number", orderId.substring(0, orderId.length() - 1));
        p.itemOnList(customerName).shouldNotBe(visible
                .because("Failed to find a customer with requested order refNum (lowercase)."));
        p.searchResults().shouldHaveSize(0);

    }

    @Test(priority = 12)
    public void orderState_remorseHold() throws IOException {

        provideTestData("order state: remorse hold");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "State", "Remorse Hold");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with order in 'Remorse Hold' state."));

    }

    @Test(priority = 13)
    public void orderState_manualHold() throws IOException {

        provideTestData("order state: manual hold");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "State", "Manual Hold");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with order in 'Manual Hold' state."));

    }

    @Test(priority = 14)
    public void orderState_fraudHold() throws IOException {

        provideTestData("order state: fraud hold");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "State", "Fraud Hold");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with order in 'Fraud Hold' state."));

    }

    @Test(priority = 15)
    public void orderState_fulfilmentStarted() throws IOException {

        provideTestData("order state: fulfilment started");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "State", "Fulfilment Started");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with order in 'Fulfilment Started' state."));

    }

    @Test(priority = 16)
    public void orderState_shipped() throws IOException {

        provideTestData("order state: shipped");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "State", "Shipped");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with order in 'Shipped' state."));

    }

    @Test(priority = 17)
    public void orderState_partiallyShipped() throws IOException {

        provideTestData("order state: partially shipped");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "State", "Partially Shipped");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with order in 'Partially Shipped' state."));

    }

    @Test(priority = 18)
    public void orderState_canceled() throws IOException {

        provideTestData("order state: partially shipped");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Order", "State", "Canceled");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with order in 'Canceled' state."));

    }

    @Test(priority = 19)
    public void orderDatePlaced() {}

    @Test(priority = 20)
    public void orderTotal() {}

    @Test(priority = 21)
    public void shippingCity() throws IOException {

        provideTestData("customer with a shipping address");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Shipping", "City", "Portland");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with 'Portland' as shipping city."));

    }

    @Test(priority = 22)
    public void shippingState() throws IOException {

        provideTestData("customer with a shipping address");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Shipping", "City", "Oregon");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with 'Oregon' as shipping state."));

    }

    @Test(priority = 23)
    public void shippingZip() throws IOException {

        provideTestData("customer with a shipping address");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Shipping", "Zip", "97201");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with '97201' as shipping ZIP."));

    }

    @Test(priority = 23)
    public void billingCity() throws IOException {

        provideTestData("customer with a billing address");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Billing", "City", "Portland");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with 'Portland' as billing city."));

    }

    @Test(priority = 23)
    public void billingState() throws IOException {

        provideTestData("customer with a billing address");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Billing", "State", "Oregon");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with 'Oregon' as billing state."));

    }

    @Test(priority = 23)
    public void billingZip() throws IOException {

        provideTestData("customer with a billing address");
        p = openPage(adminUrl + "/customers", CustomersPage.class);

        p.addFilter("Billing", "Zip", "07097");
        p.itemOnList(customerName).shouldBe(visible
                .because("Failed to find a customer with '07097' as billing ZIP."));

    }

}
