package tests.storeadmin.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CartPage;
import pages.admin.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class ShippingAddressTest extends Preconditions {

    private CartPage p;

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
    @Stories("Cart :  Shipping Address")
    @Description("Can create a new shipping address in empty address book | New ship address is automatically applied to cart")
    public void addNewAddress_emptyAddressBook() throws IOException {
        provideTestData("cart with empty address book");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Shipping Address");
        p.addNewAddress("Test Buddy", "1124 Random Rd", "Suite 300", "El Cajon", "California", "92020", "9879879876");

        p.addressBook().shouldHaveSize(1);
        p.chosenAddress().shouldBe(visible
                .because("A just added address isn't set as a chosen shipping address."));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Shipping Address")
    @Description("Creating a new shipping address in a non-empty address book auto-applies it to the cart if none was applied yet")
    public void addNewAddress_nonEmptyAddressBook() throws IOException {
        provideTestData("cart with non-empty address book");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.addNewAddress("Test Buddy", "1124 Random Rd", "Suite 300", "El Cajon", "California", "92020", "9879879876");
        p.addressBook().shouldHaveSize(2);
        p.customerName_chosenShipAddress().shouldHave(text("Test Buddy"));

        p.clickDoneBtn("Shipping Address");
        p.appliedShipAddress_name().shouldHave(text("Test Buddy"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Cart : Shipping Address")
    @Description("Can apply existing ship address from address book")
    public void chooseShippingAddress() throws IOException {
        provideTestData("cart with non-empty address book");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.chooseShipAddress(1);
        shouldBeVisible(p.chosenAddress(), "A chosen shipping address isn't displayed as a chosen shipping address.");

        p.clickDoneBtn("Shipping Address");
        p.appliedShipAddress_name().shouldHave(text("John Doe"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Cart : Shipping Address")
    @Description("Can edit applied shipping address")
    public void editChosenShippingAddress() throws IOException {
        provideTestData("cart with chosen shipping address");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.clickEditBtn_chosenAddress();
        p.setName("Edited Name");
        p.clickSaveBtn_modal();
        shouldNotBeVisible(p.nameFld(), "'New Address' form isn't closed after clicking 'Save'");

        p.customerName_chosenShipAddress().shouldHave(text("Edited Name")
                .because("Failed to update chosen shipping address"));
        p.clickDoneBtn("Shipping Address");
        p.appliedShipAddress_name().shouldHave(text("Edited Name"));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Cart : Shipping Address")
    @Description("Can remove applied ship address from cart")
    public void deleteChosenShippingAddress() throws IOException {
        provideTestData("cart with chosen shipping address");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.removeChosenAddress();
        p.chosenAddress().shouldNotBe(visible
                .because("Failed to delete chosen shipping address."));
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Shipping Address")
    @Description("Can set existing address from address book as default")
    public void setDefaultShippingAddress() throws IOException {
        provideTestData("filled out cart 2 addresses in address book");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Address");
        p.setAddressAsDefault("1");
        p.chooseShipAddress(1);
        p.defaultShipAddressChkbox_input("1").shouldBe(selected);
        p.clickDoneBtn("Shipping Address");
        p.assertNoWarnings();
        p.clickPlaceOderBtn();
        shouldHaveText(p.orderState(), "Remorse Hold", "Failed to place order");
        openPage(adminUrl + "/customers/" + customerId + "/cart", CartPage.class);
        p.clickEditCartBtn();

        p.appliedShipAddress_name().shouldHave(text("John Doe"));
        p.shipAddressWarn().shouldNotBe(visible);
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Cart : Shipping Address")
    @Description("Can set different ship address as default")
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
