package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class ItemsTest extends DataProvider {

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

    @Test (priority = 1)
    public void addItemToCart() throws IOException {
        provideTestData("empty cart and 3 active products");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.addItemToCart(skus.get(0));
        p.addItemToCart(skus.get(1));

        p.lineItem_byName(skus.get(0)).shouldBe(visible);
        p.lineItem_byName(skus.get(1)).shouldBe(visible);
    }

    @Test (priority = 2)
    public void editItemQuantity_arrowBtn() throws IOException {
        provideTestData("cart<1 SKU[active, qty: 1]>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        shouldBeVisible(p.itemQty("1"), "Failed to open cart page");
        p.clickEditBtn("Line Items");
        p.increaseItemQty("1", 2);
        p.clickDoneBtn("Line Items");
        p.itemQty("1").shouldHave(text("3")
                .because("Line item <index: 1> has incorrect quantity value."));

        p.clickEditBtn("Line Items");
        p.decreaseItemQty("1", 1);
        p.clickDoneBtn("Line Items");
        p.itemQty("1").shouldHave(text("2")
                .because("Line item <index: 1> has incorrect quantity value."));
    }

    @Test (priority = 3)
    public void editItemQuantity_directInput() throws IOException {
        provideTestData("cart<1 SKU[active, qty: 1]>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        p.clickEditBtn("Line Items");
        p.setItemQty(sku, "3");
        shouldHaveText(p.itemTotalPrice("1"), "$150.00", "");
        p.clickDoneBtn("Line Items");

        p.itemQty("1").shouldHave(text("3")
                .because("Line item with index <1> has incorrect quantity value."));
    }

    @Test (priority = 4)
    public void deleteItem_cancel() throws IOException {
        provideTestData("cart with 1 item, qty: 3");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        shouldBeVisible(p.itemQty("1"), "Failed to open cart page");
        p.clickEditBtn("Line Items");
        p.clickDeleteBtn_item("1");
        p.cancelDeletion();

        p.lineItems().shouldHaveSize(1);
    }

    @Test (priority = 5)
    public void deleteItem_confirm() throws IOException {
        provideTestData("cart with 3 items");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        shouldBeVisible(p.itemQty("1"), "Failed to open cart page");
        p.clickEditBtn("Line Items");
        p.clickDeleteBtn_item("1");
        p.confirmDeletion();

        p.lineItems().shouldHaveSize(2);
    }

    @Test(priority = 6)
    @Description("Regression test: 1 line item with 'qty > 1' should be displayed as a single line item")
    public void oneItemIsntPropagated() throws IOException {
        provideTestData("cart<1 SKU[active, qty: 1]>");

        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        shouldBeVisible(p.itemQty("1"), "Failed to open cart page");
        p.clickEditBtn("Line Items");
        p.setItemQty(sku, "3");
        p.clickDoneBtn("Line Items");
        shouldHaveText(p.itemQty("1"), "1", "Failed to edit Qty");
        open(adminUrl + "/customers/" + customerId + "/cart");

        p.lineItems().shouldHaveSize(1);
    }

    @Test(priority = 7)
    @Description("Regression test: 1 line item with 'qty > 1' should be displayed as a single line item after cart checkout")
    public void oneItemIsntPropagatedAfterCheckout() throws IOException {
        provideTestData("filled out cart");

        checkoutCart(cartId);
        p = openPage(adminUrl + "/orders/" + cartId, CartPage.class);

        p.lineItems().shouldHaveSize(1);
    }

}