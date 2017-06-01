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

import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static testdata.api.collection.Customers.deleteAddress;
import static testdata.api.collection.GiftCards.setGiftCardState;
import static testdata.api.collection.StoreCredits.setStoreCreditState;

public class CartValidationTest extends Preconditions {

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

    //TODO: implement soft asserts here

    @Test(priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Funds warning is displayed and Place Order btn is locked if applied funds are less than grand total")
    public void fundsWarning() throws IOException {
        provideTestData("cart<filled out, payment method: SC>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Payment Method");
        p.removePaymentMethod("1");

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Shipping address warning is displayed and Place Order btn is locked if no ship address is applied to cart")
    public void noShipAddressWarning() throws IOException {
        provideTestData("cart<filled out, payment method: SC>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Shipping Address");
        p.removeChosenAddress();

        p.shipAddressWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Empty cart warning is displayed and Place Order btn is locked if cart doesn't have any line items")
    public void emptyCartWarning() throws IOException {
        provideTestData("cart<filled out, payment method: SC>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Line Items");
        p.removeItem("1");
        shouldBeVisible(p.itemsWarn(), "Items warning doesn't appear once line items are cleared from cart");
        p.clickDoneBtn("Line Items");

        p.itemsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Warning re-appears and Place Order btn get locked if cart has been edited and no longer qualifies for checkout")
    public void warningReAppears() throws IOException {
        provideTestData("cart<filled out, payment method: SC>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Payment Method");
        p.removePaymentMethod("1");
        p.addPaymentMethod_SC("200");

        p.fundsWarn().shouldNotBe(visible);
        p.placeOrderBtn().shouldBe(enabled);
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Funds warning re-appears and Place Order btn is locked if grand total has been increased")
    public void fundsWarning_grandTotalIncrease() throws IOException {
        provideTestData("cart<filled out, payment method: SC>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Line Items");
        p.increaseItemQty("1", 10);
        p.clickDoneBtn("Line Items");

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Shipping address & method warnings shouldn't appear if used address is removed from address book")
    public void shipAddressWarning_shipAddressDeleted() throws IOException {
        provideTestData("cart<filled out, payment method: SC>");

        deleteAddress(customerId, addressId1);
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.shipAddressWarn().shouldNotBe(visible);
        p.shipMethodWarn().shouldNotBe((visible));
        p.placeOrderBtn().shouldBe(enabled);
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Funds warning should appear if used SC is canceled")
    public void fundsWarning_scCanceled() throws IOException {
        provideTestData("cart<filled out, payment method: SC>");

        setStoreCreditState(scId, "canceled");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Funds warning should appear if used SC is put on hold")
    public void fundsWarning_scOnHold() throws IOException {
        provideTestData("cart<filled out, payment method: SC>");

        setStoreCreditState(scId, "onHold");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Funds warning should appear if used GC is canceled")
    public void fundsWarning_gcCanceled() throws IOException {
        provideTestData("cart<filled out, payment method: GC>");

        setGiftCardState(gcCode, "canceled");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    @Test(priority = 10)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Funds warning should appear if used GC is put on hold")
    public void fundsWarning_gcOnHold() throws IOException {
        provideTestData("cart<filled out, payment method: GC>");

        setGiftCardState(gcCode, "onHold");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    @Test(priority = 11)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Cart : Validation")
    @Description("Funds warning should appear if credit card is deleted from customer profile")
    public void fundsWarning_ccDeleted() throws IOException {
        provideTestData("cart<filled out, payment method: CC>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Payment Method");
        p.removePaymentMethod("1");

        p.fundsWarn().shouldBe(visible);
        p.placeOrderBtn().shouldBe(disabled);
    }

    //TODO: Move to API as a positive test (you can't archive SKU if its in a cart)
//    @Description("Cart warning should appear if SKU is archived")
//    @Test(priority = 12)
//    public void cartWarning_skuArchived() throws IOException {
//        provideTestData("cart<filled out, payment method: SC>");
//
//        archiveSKU(skuCode);
//        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        p.itemsWarn().shouldBe(visible);
//        p.placeOrderBtn().shouldBe(disabled);
//    }

    //TODO: Move to API as a positive test (you can't archive SKU if its in a cart)
//    @Description("Cart warning should appear if product is archived")
//    @Test(priority = 13)
//    public void cartWarning_productArchived() throws IOException {
//        provideTestData("filled out cart, product with variants, SC as a payment method");
//
//        archiveProduct(productId);
//        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        p.itemsWarn().shouldBe(visible);
//        p.placeOrderBtn().shouldBe(disabled);
//    }

    //TODO: Move to API as a positive test (you can't archive SKU if its in a cart)
//    @Description("Cart warning should appear if product's SKUs are archived")
//    @Test(priority = 14)
//    public void cartWarning_productSKUsArchived() throws IOException {
//        provideTestData("filled out cart, product with variants, SC as a payment method");
//
//        archiveSKU(variantSKU_1);
//        archiveSKU(variantSKU_2);
//        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        p.itemsWarn().shouldBe(visible);
//        p.placeOrderBtn().shouldBe(disabled);
//    }

    // TODO: Cart warning should appear if SKU is removed from product (means removed SKU isn't tied to any product anymore)
    // ^ There are 2 cases: product with and without variants

}