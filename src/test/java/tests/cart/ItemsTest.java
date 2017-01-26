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
import static com.codeborne.selenide.Selenide.open;

// TODO: [New Test] Check if line items with Qty > 1 are stacked to one-line record at Items block

public class ItemsTest extends DataProvider {

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

    @Test (priority = 1)
    public void addItemToCart() throws IOException {

        provideTestData("empty cart");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.addItemToCart("Shark");
        p.addItemToCart("Fox");

    }

    @Test (priority = 2)
    public void editItemQuantity_arrowBtn() throws IOException {

        provideTestData("cart with 1 item");
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
        provideTestData("cart with 1 item");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Line Items");
        p.setItemQty("1", "3");
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

        p.cart().shouldHaveSize(1);
    }

    @Test (priority = 5)
    public void deleteItem_confirm() throws IOException {
        provideTestData("cart with 3 items");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        shouldBeVisible(p.itemQty("1"), "Failed to open cart page");

        p.clickEditBtn("Line Items");
        p.clickDeleteBtn_item("1");
        p.confirmDeletion();

        p.cart().shouldHaveSize(2);
    }

    @Test(priority = 6, dependsOnMethods = "editItemQuantity_directInput")
    @Description("Regression test: 1 line item with 'qty > 1' should be displayed as a single line item")
    public void oneItemIsntPropagated() throws IOException {
        provideTestData("cart with 1 item");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        shouldBeVisible(p.itemQty("1"), "Failed to open cart page");

        p.clickEditBtn("Line Items");
        p.setItemQty("1", "3");
        shouldHaveText(p.itemQty("1"), "3", "Failed to edit Qty");
        p.clickDoneBtn("Line Items");
        open(adminUrl + "/customers/" + customerId + "/cart");

        p.cart().shouldHaveSize(1);
    }

}