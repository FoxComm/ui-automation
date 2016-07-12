package tests.orderstests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ShippingAddressTest extends DataProvider {

    private OrderDetailsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void addNewAddress_emptyAddressBook() throws IOException {

        provideTestData("cart with empty address book");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        p.clickEditBtn_shipAddress();
        p.clearAddressBook();
        p.addNewAddress("John Doe", "2101 Green Valley #320", "Seattle", "Washington", "98101", "9879879876");

        assertTrue(p.addressBookHeader().is(visible),
                "A just added address isn't displayed in address book.");
        assertTrue(p.chosenAddressHeader().is(visible),
                "A just added address isn't set as a chosen shipping address.");

    }

    // atm test fails because of bug with input fields rendering
    @Test(priority = 2)
    public void addNewAddress_nonEmptyAddressBook() throws IOException {

        provideTestData("cart with non-empty address book");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        p.clickEditBtn_shipAddress();
        p.addNewAddress("John Doe", "2101 Green Valley #320", "Seattle", "Washington", "98101", "9879879876");

        String actualResult = p.getNameFromAddressBook(1);
        assertEquals(actualResult, "John Doe",
                "New shipping address isn't displayed in address book.");

    }

    @Test(priority = 3)
    public void chooseShippingAddress() throws IOException {

        provideTestData("cart with non-empty address book");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        p.clickEditBtn_shipAddress();
        p.chooseShipAddress(1);
        click( p.doneBtn_shipAddress() );

    }

    @Test(priority = 3)
    public void editChosenShippingAddress() {

        p.clickEditBtn_shipAddress();
        click( p.editBtn_chosenAddress() );
        setFieldVal( p.nameFld(), "John Doe" );
        p.applyChangesToAddress();

        assertEquals(p.getCustomerName_chosenShipAddress(), "John Doe",
                "Chosen address has failed to get updated.");

    }

    @Test(priority = 4)
    public void deleteChosenShippingAddress() throws IOException {

        provideTestData("cart with chosen shipping address");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        click( p.editBtn_shipAddress() );
        click( p.deleteBtn_chosenAddress() );
        click( p.confirmDeletionBtn() );

        elementNotVisible( p.chosenAddressHeader() );

    }

    @Test(priority = 6)
    public void setDefaultShippingAddress() throws IOException {

        provideTestData("cart with 2 addresses in address book");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        p.clickEditBtn_shipAddress();
        click( p.defaultShipAddressChkbox("1") );
        sleep(750);
        assertTrue( p.defaultShipAddressChkbox_input("1").isSelected(),
                "Failed to set address in address book as default shipping address." );

    }

    @Test(priority = 7)
    public void changeDefaultShippingAddress() throws IOException {

        provideTestData("cart with 2 addresses and defined default shipping address");
        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);

        p.clickEditBtn_shipAddress();
        click( p.defaultShipAddressChkbox("1") );
        sleep(750);
        assertTrue( p.defaultShipAddressChkbox_input("1").isSelected(),
                "Failed to set different address as default shipping address." );

    }


    @AfterMethod
    public void cleanUp() {
        p.restorePageDefaultState();
    }

}
