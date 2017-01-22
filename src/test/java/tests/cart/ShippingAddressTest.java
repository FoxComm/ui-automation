package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class ShippingAddressTest extends DataProvider {

    private CartPage p;

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
    public void addNewAddress_emptyAddressBook() throws IOException {

        provideTestData("cart with empty address book");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.clearAddressBook();
        p.addNewAddress("John Doe", "2101 Green Valley #320", "Suite 300", "Seattle", "Washington", "98101", "9879879876");

        p.addressBookHeader().shouldBe(visible
                .because("A just added address isn't displayed in address book."));
        p.chosenAddress().shouldBe(visible
                .because("A just added address isn't set as a chosen shipping address."));

    }

    @Test(priority = 2)
    public void addNewAddress_nonEmptyAddressBook() throws IOException {

        provideTestData("cart with non-empty address book");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.addNewAddress("John Doe", "2101 Green Valley #320", "Suite 300", "Seattle", "Washington", "98101", "9879879876");
        p.clickDoneBtn("Shipping Address");

        p.successIcon_shipAddress().shouldBe(visible
                .because("'Success' icon is not displayed"));
        p.shipAddressWarn().shouldNotBe(visible
                .because("'Shipping address' warning is displayed."));

    }

    @Test(priority = 3)
    public void chooseShippingAddress() throws IOException {

        provideTestData("cart with non-empty address book");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.chooseShipAddress(1);
        p.chosenAddress().shouldBe(visible
                .because("A chosen address isn't displayed as a chosen shipping address."));
        p.clickDoneBtn("Shipping Address");
        shouldNotBeVisible(p.nameFld(), "\"New Address\" form isn't closed after clicking 'Save'");

        p.chosenShippingAddressBlock().shouldBe(visible
                .because("A chosen address isn't displayed as a chosen shipping address."));
        p.shipAddressWarn().shouldNotBe(visible
                .because("'No shipping address' warning is displayed."));

    }

    @Test(priority = 3)
    public void editChosenShippingAddress() {

        p.clickEditBtn("Shipping Address");
        p.clickEditBtn_chosenAddress();
        p.setName("Edited Customer Name");
        p.clickSave();
        shouldNotBeVisible(p.nameFld(), "'New Address' form isn't closed after clicking 'Save'");

        p.customerName_chosenShipAddress().shouldHave(text("Edited Customer Name")
                .because("Chosen address has failed to get updated."));

    }

    @Test(priority = 4)
    public void deleteChosenShippingAddress() throws IOException {

        provideTestData("cart with chosen shipping address");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.removeChosenAddress();

        p.chosenAddress().shouldNotBe(visible
                .because("Failed to delete chosen shipping address."));

    }

    @Test(priority = 6)
    public void setDefaultShippingAddress() throws IOException {

        provideTestData("cart with 2 addresses in address book");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.setAddressAsDefault("1");
        sleep(750);
        p.defaultShipAddressChkbox_input("1").shouldBe(selected
                .because("Failed to set address in address book as default shipping address."));

    }

    @Test(priority = 7)
    public void changeDefaultShippingAddress() throws IOException {

        provideTestData("cart with 2 addresses and defined default shipping address");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.setAddressAsDefault("1");
        sleep(750);
        p.defaultShipAddressChkbox_input("1").shouldBe(selected
                .because("Failed to set different address as default shipping address."));

    }

}
