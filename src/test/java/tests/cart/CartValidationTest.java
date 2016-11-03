package tests.cart;

import com.codeborne.selenide.Condition;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CustomersPage;
import pages.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class CartValidationTest extends DataProvider {

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
    public void fundsWarning() throws IOException {

        provideTestData("filled out cart, SC as a payment method");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Payment Method");
        p.removePaymentMethod("1");

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Test(priority = 2)
    public void noShipAddressWarning() throws IOException {

        provideTestData("filled out cart, SC as a payment method");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Shipping Address");
        p.removeChosenAddress();

        p.shipAddressWarn().shouldBe(visible);
        p.shipMethodWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Test(priority = 3)
    public void emptyCartWarning() throws IOException {

        provideTestData("filled out cart, SC as a payment method");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clearCart();

        p.cartWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Test(priority = 4)
    public void warningReAppears() throws IOException {

        provideTestData("filled out cart, SC as a payment method");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Payment Method");
        p.addPaymentMethod_SC("200");
        p.removePaymentMethod("1");
        p.addPaymentMethod_SC("200");

        p.fundsWarn().shouldNotBe(visible);
        p.placeOrderBtn().shouldBe(enabled);

    }

    @Test(priority = 5)
    public void fundsWarning_grandTotalIncrease() throws IOException {

        provideTestData("filled out cart, SC as a payment method");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Items");
        p.increaseItemQty("1", 10);
        p.clickDoneBtn("Items");

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Description("Shipping address & method warnings should appear if used address is removed from address book at the customer details page")
    @Test(priority = 6)
    public void shipAddressWarning_shipAddressDeleted() throws IOException {

        provideTestData("filled out cart, SC as a payment method");

        deleteAddress(customerId, addressId1);
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.shipAddressWarn().shouldBe(visible);
        p.shipMethodWarn().shouldBe((visible));
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Description("Funds warning should appear if used SC is canceled")
    @Test(priority = 7)
    public void fundsWarning_scCanceled() throws IOException {

        provideTestData("filled out cart, SC as a payment method");

        setStoreCreditState(scId, "canceled");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Description("Funds warning should appear if used SC is put on hold")
    @Test(priority = 8)
    public void fundsWarning_scOnHold() throws IOException {

        provideTestData("filled out cart, SC as a payment method");

        setStoreCreditState(scId, "onHold");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Description("Funds warning should appear if used GC is canceled")
    @Test(priority = 9)
    public void fundsWarning_gcCanceled() throws IOException {

        provideTestData("filled out cart, GC as a payment method");
        setGiftCardState(gcCode, "canceled");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Description("Funds warning should appear if used GC is put on hold")
    @Test(priority = 10)
    public void fundsWarning_gcOnHold() throws IOException {

        provideTestData("filled out cart, GC as a payment method");
        setGiftCardState(gcCode, "onHold");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Description("Funds warning should appear if credit card is deleted from customer profile")
    @Test(priority = 11)
    public void fundsWarning_ccDeleted() throws IOException {

        provideTestData("filled out cart, CC as a payment method");
        deleteCreditCard(customerId, creditCardId);

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }


    @Description("Cart warning should appear if SKU is archived")
    @Test(priority = 12)
    public void cartWarning_skuArchived() throws IOException {

        provideTestData("filled out cart, SC as a payment method");
        archiveSKU(sku);

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.cartWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Description("Cart warning should appear if product is archived")
    @Test(priority = 13)
    public void cartWarning_productArchived() throws IOException {

        provideTestData("filled out cart, product with variants, SC as a payment method");
        archiveProduct(productId);

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.cartWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    @Description("Cart warning should appear if product's SKUs are archived")
    @Test(priority = 14)
    public void cartWarning_productSKUsArchived() throws IOException {

        provideTestData("filled out cart, product with variants, SC as a payment method");
        archiveSKU(variantSKU_1);
        archiveSKU(variantSKU_2);

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.cartWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);

    }

    // TODO: Cart warning should appear if SKU is removed from product (means removed SKU isn't tied to any product anymore)
    // ^ There are 2 cases: product with and without variants

}