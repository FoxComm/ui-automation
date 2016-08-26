package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ShippingAddressTest extends DataProvider {

    private CartPage p;

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
    public void addNewAddress_emptyAddressBook() throws IOException {

        provideTestData("cart with empty address book");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_shipAddress();
        p.clearAddressBook();
        p.addNewAddress("John Doe", "2101 Green Valley #320", "Suite 300", "Seattle", "Washington", "98101", "9879879876");

        assertTrue(p.addressBookHeader().is(visible),
                "A just added address isn't displayed in address book.");
        assertTrue(p.chosenAddress().is(visible),
                "A just added address isn't set as a chosen shipping address.");

    }

    // atm test fails because of bug with input fields rendering
    @Test(priority = 2)
    public void addNewAddress_nonEmptyAddressBook() throws IOException {

        provideTestData("cart with non-empty address book");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_shipAddress();
        p.addNewAddress("John Doe", "2101 Green Valley #320", "Suite 300", "Seattle", "Washington", "98101", "9879879876");
        click( p.doneBtn_shipAddress() );

        p.successIcon_shipAddress().shouldBe(visible
                .because("'Success' icon is not displayed"));
        p.shipAddressWarn().shouldNotBe(visible
                .because("'No shipping address' warning is displayed."));

    }

    @Test(priority = 3)
    public void chooseShippingAddress() throws IOException {

        provideTestData("cart with non-empty address book");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_shipAddress();
        p.chooseShipAddress(1);
        click( p.doneBtn_shipAddress() );

        p.chosenAddress().shouldBe(visible
                .because("A chosen address isn't displayed as a choosen shipping address."));

    }

    @Test(priority = 3)
    public void editChosenShippingAddress() {

        p.clickEditBtn_shipAddress();
        click( p.editBtn_chosenAddress() );
        setFieldVal( p.nameFld(), "Edited Customer Name" );
        p.applyChangesToAddress();

        assertEquals(p.getCustomerName_chosenShipAddress(), "Edited Customer Name",
                "Chosen address has failed to get updated.");

    }

    @Test(priority = 4)
    public void deleteChosenShippingAddress() throws IOException {

        provideTestData("cart with chosen shipping address");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        click( p.editBtn_shipAddress() );
        click( p.deleteBtn_chosenAddress() );
        click( p.confirmDeletionBtn() );

        elementNotVisible( p.chosenAddress() );

    }

    @Test(priority = 6)
    public void setDefaultShippingAddress() throws IOException {

        provideTestData("cart with 2 addresses in address book");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_shipAddress();
        click( p.defaultShipAddressChkbox("1") );
        sleep(750);
        assertTrue( p.defaultShipAddressChkbox_input("1").isSelected(),
                "Failed to set address in address book as default shipping address." );

    }

    @Test(priority = 7)
    public void changeDefaultShippingAddress() throws IOException {

        provideTestData("cart with 2 addresses and defined default shipping address");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_shipAddress();
        click( p.defaultShipAddressChkbox("1") );
        sleep(750);
        assertTrue( p.defaultShipAddressChkbox_input("1").isSelected(),
                "Failed to set different address as default shipping address." );

    }

    // If tests in here doesn't fail because of garbage from previous tests - then delete @AfterMethod
    // And delete restorePageDefaultState() from CartPage.class
//    @AfterMethod
//    public void cleanUp() {
//        p.restorePageDefaultState();
//    }

}
