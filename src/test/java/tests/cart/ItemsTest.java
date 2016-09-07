package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
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
    public void editItemQuantity_arrowBtn() throws IOException {

        provideTestData("cart with 1 item");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        p.itemQty("1").shouldBe(visible);
        String expectedResult = addToString(p.itemQty("1").text(), 2);

        p.clickEditBtn_items();
        p.increaseItemQty("1", 2);
        p.clickDoneBtn_items();
        p.itemQty("1").shouldHave(text(expectedResult)
                .because("Line item with index <1> has incorrect quantity value."));

        p.clickEditBtn_items();
        p.decreaseItemQty("1", 1);
        p.clickDoneBtn_items();
        p.itemQty("1").shouldHave(text(expectedResult)
                .because("Line item with index <1> has incorrect quantity value."));

    }

    @Test (priority = 3)
    public void decreaseItemQuantityBelowZero() throws IOException {

        provideTestData("cart with 3 items");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        p.itemQty("1").shouldBe(visible);
        int expectedResult = p.itemsInCartAmount() - 1;

        p.clickEditBtn_items();
        p.decreaseItemQtyBelowZero("1");
        p.confirmDeletion();
        p.clickDoneBtn_items();

        p.cart().shouldHaveSize(expectedResult);

    }

    @Test (priority = 4)
    public void editItemQuantity_directInput() throws IOException {

        provideTestData("cart with 1 item");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        // increase value
        p.editItemQty("1", "3");
        p.itemQty("1").shouldHave(text("3")
                .because("Line item with index <1> has incorrect quantity value."));

        // decrease value
        p.editItemQty("1", "1");
        p.itemQty("1").shouldHave(text("1")
                .because("Line item with index <1> has incorrect quantity value."));

    }

    @Test (priority = 5)
    public void deletionCancel_deleteBtn() throws IOException {

        provideTestData("cart with 1 item, qty: 3");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        p.itemQty("1").shouldBe(visible);
        int expectedResult = p.itemsInCartAmount();

        p.clickEditBtn_items();
        click( p.deleteBtn_item("1") );
        p.cancelDeletion();

        p.cart().shouldHaveSize(expectedResult);

    }

    @Test (priority = 6)
    public void deletionCancel_qtyBelowZero() throws IOException {

        provideTestData("cart with 3 items");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        p.itemQty("1").shouldBe(visible);
        int expectedResult = p.itemsInCartAmount();

        p.clickEditBtn_items();
        p.decreaseItemQtyBelowZero("1");
        p.cancelDeletion();
        p.clickDoneBtn_items();

        p.cart().shouldHaveSize(expectedResult);

    }

    @Test (priority = 7)
    public void deleteItem_deleteBtn() throws IOException {

        provideTestData("cart with 3 items");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
        p.itemQty("1").shouldBe(visible);
        int expectedItemsAmount = p.cart().size() - 1;

        p.clickEditBtn_items();
        click( p.deleteBtn_item("1") );
        p.confirmDeletion();

        p.cart().shouldHaveSize(expectedItemsAmount);

    }

}