package tests.storeadmin.inventory;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CartPage;
import pages.admin.InventoryPage;
import pages.admin.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static org.openqa.selenium.By.xpath;

public class InventoryTest extends Preconditions {

    private InventoryPage p;
    private CartPage cartPage;

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
    @Stories("Inventory")
    @Description("Can edit amount of sellable items on-hand")
    public void editOnHand_sellable() throws IOException {
        provideTestData("active SKU for inventory");

        p = openPage(adminUrl + "/skus/" + skuCode + "/inventory", InventoryPage.class);
        p.expandWarehouse("default");
        p.setOnHand("Sellable", "1");
        p.clickSave_wait();
        refresh();

        p.onHandQty("default").shouldHave(text("1"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Inventory")
    @Description("Can edit amount of non-sellable items on-hand")
    public void editOnHand_nonsellable() throws IOException {
        provideTestData("active SKU for inventory");

        p = openPage(adminUrl + "/skus/" + skuCode + "/inventory", InventoryPage.class);
        p.expandWarehouse("default");
        p.setOnHand("Non-sellable", "1");
        p.clickSave_wait();
        refresh();

        p.onHandQty("default").shouldHave(text("1"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Inventory")
    @Description("Can edit amount of backorder items on-hand")
    public void editOnHand_backorder() throws IOException {
        provideTestData("active SKU for inventory");

        p = openPage(adminUrl + "/skus/" + skuCode + "/inventory", InventoryPage.class);
        p.expandWarehouse("default");
        p.setOnHand("Backorder", "1");
        p.clickSave_wait();
        refresh();

        p.onHandQty("default").shouldHave(text("1"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Inventory")
    @Description("Can edit amount of pre-order items on-hand")
    public void editOnHand_preorder() throws IOException {
        provideTestData("active SKU for inventory");

        p = openPage(adminUrl + "/skus/" + skuCode + "/inventory", InventoryPage.class);
        p.expandWarehouse("default");
        p.setOnHand("Preorder", "1");
        p.clickSave_wait();
        refresh();

        p.onHandQty("default").shouldHave(text("1"));
    }

    // TODO: [Fix] Investigate why not all 4 types are edited
    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Inventory")
    @Description("Can edit amount of multiple different item types at once and submit changes successfully")
    public void editOnHand_multipleTypes() throws IOException {
        provideTestData("active SKU for inventory");

        p = openPage(adminUrl + "/skus/" + skuCode + "/inventory", InventoryPage.class);
        p.expandWarehouse("default");
        p.setOnHand("Preorder", "1");
        p.setOnHand("Backorder", "1");
        p.setOnHand("Non-sellable", "1");
        p.setOnHand("Sellable", "1");
        p.clickSave_wait();
        refresh();

        p.onHandQty("default").shouldHave(text("4"));
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Inventory")
    @Description("Can edit amount of on-hand sellable items using arrow btn")
    public void editOnHand_arrowBtns() throws IOException {
        provideTestData("active SKU for inventory");

        p = openPage(adminUrl + "/skus/" + skuCode + "/inventory", InventoryPage.class);
        p.expandWarehouse("default");
        p.increaseOnHand_arrowBtn("Sellable", 2);
        p.onHandFld("Sellable").shouldHave(value("2"));
        p.decreaseOnHand_arrowBtn("Sellable", 1);
        p.onHandFld("Sellable").shouldHave(value("1"));
        p.clickSave_wait();
        refresh();

        p.onHandQty("default").shouldHave(text("1"));
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Inventory")
    @Description("Check visibility of \"Adjust Quantity\" block for the last row on table")
    public void visibilityOfAdjustQty() throws IOException {
        provideTestData("active SKU for inventory");

        p = openPage(adminUrl + "/skus/" + skuCode + "/inventory", InventoryPage.class);
        p.expandWarehouse("default");
        p.setOnHand("Preorder", "1");

        p.arrowBtn("Preorder", "increment").shouldBe(visible
                .because("\"Adjust Quantity\" block isn't visible"));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Inventory")
    @Description("Transactions are listed in transations tab")
    public void transactionsLog() throws IOException {
        provideTestData("active SKU for inventory");

        p = openPage(adminUrl + "/skus/" + skuCode + "/inventory", InventoryPage.class);
        p.expandWarehouse("default");
        p.setOnHand("Sellable", "1");
        p.clickSave_wait();
        p.openTransactions();
        waitForDataToLoad();

        $(xpath("//td[text()='default']")).shouldBe(visible);
    }

//    @Test(priority = 9)
//    public void checkoutCart_backorder() throws IOException {
//
//        provideTestData("cart with backorder SKU");
//        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        cartPage.clickPlaceOderBtn();
//        cartPage.orderState().shouldHave(text("Remorse Hold"));
//
//    }
//
//    @Test(priority = 10)
//    public void checkoutCart_backorderAndSellable_oneSKU() throws IOException {
//
//        provideTestData("cart with backorder and sellable line items of one SKU");
//        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        cartPage.clickPlaceOderBtn();
//        cartPage.orderState().shouldHave(text("Remorse Hold"));
//
//    }
//
//    @Test(priority = 11)
//    public void checkoutCart_backorderAndSellable_twoSKUs() throws IOException {
//
//        provideTestData("cart with backorder and sellable items of two SKUs");
//        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        cartPage.clickPlaceOderBtn();
//        cartPage.orderState().shouldHave(text("Remorse Hold"));
//
//    }

}