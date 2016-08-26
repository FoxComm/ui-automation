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

public class ItemsTest extends DataProvider {

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

    @Test (priority = 1)
    public void addItemToCart() throws IOException {

        provideTestData("empty cart");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.addItemToCart("Shark");
        p.addItemToCart("Fox");

    }

    @Test (priority = 2)
    public void increaseItemQuantity_arrowBtn() throws IOException {

        provideTestData("cart with 1 item");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        sleep(750);

        String expectedResult = addToString(p.getItemQty("1"), 2);

        p.increaseItemQty("1", 2);
        assertEquals(p.getItemQty("1"), expectedResult);

    }

    @Test (priority = 3)
    public void decreaseItemQuantity_arrowBtn() throws IOException {

        provideTestData("cart with 1 item, qty: 3");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        sleep(750);

        String expectedResult = subtractFromString(p.getItemQty("1"), 1);

        p.decreaseItemQty("1", 1);
        assertEquals(p.getItemQty("1"), expectedResult);

    }

    @Test (priority = 4)
    public void decreaseItemQuantityBelowZero() throws IOException {

        provideTestData("cart with 3 items");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        sleep(750);

        int expectedResult = p.itemsInCartAmount() - 1;

        p.decreaseItemQtyBelowZero("1");
        p.confirmDeletion();
        p.applyChangesToItems();

        int actualResult = p.itemsInCartAmount();
        assertEquals(actualResult, expectedResult);

    }

    @Test (priority = 5)
    public void editItemQuantity_directInput() throws IOException {

        provideTestData("cart with 1 item");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        // increase value
        p.editItemQty("1", "3");
        assertEquals(p.getItemQty("1"), "3");
        sleep(750);

        // decrease value
        p.editItemQty("1", "1");
        assertEquals(p.getItemQty("1"), "1"); // <-- fails here

    }

    @Test (priority = 6)
    public void deletionCancel_deleteBtn() throws IOException {

        provideTestData("cart with 1 item, qty: 3");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        sleep(750);

        int expectedResult = p.itemsInCartAmount();

        p.clickEditBtn_items();
        click( p.deleteBtn_item("1") );
        p.cancelDeletion();

        int actualResult = p.itemsInCartAmount();
        assertEquals(actualResult, expectedResult);

    }

    @Test (priority = 7)
    public void deletionCancel_qtyBelowZero() throws IOException {

        provideTestData("cart with 3 items");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        sleep(750);

        int expectedResult = p.itemsInCartAmount();

        p.decreaseItemQtyBelowZero("1");
        p.cancelDeletion();
        p.applyChangesToItems();

        int actualResult = p.itemsInCartAmount();
        assertEquals(actualResult, expectedResult);

    }

    @Test (priority = 8)
    public void deleteItem_deleteBtn() throws IOException {

        provideTestData("cart with 3 items");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        sleep(750);

        int expectedItemsAmount = p.cart().size() - 1;

        p.clickEditBtn_items();
        click( p.deleteBtn_item("1") );
        p.confirmDeletion();

        int actualProductName = p.cart().size();
        assertEquals(actualProductName, expectedItemsAmount);

    }

}