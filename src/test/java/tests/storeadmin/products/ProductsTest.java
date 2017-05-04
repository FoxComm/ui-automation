package tests.storeadmin.products;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CartPage;
import pages.admin.LoginPage;
import pages.admin.ProductsPage;
import pages.admin.SkusPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static testdata.api.collection.Cart.createCart;
import static testdata.api.collection.Customers.createCustomer;

public class ProductsTest extends Preconditions {

    private CartPage cartPage;
    private SkusPage skusPage;
    private ProductsPage productsPage;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    //--------------------- CART

    // Disabled, because it duplicates similar SKU specific tests
    // (because line_item_search_view right now take only skuTitle and skuCode as a search argument)

//    @Test(priority = 1, dataProvider = "canAddProductToCart_admin")
//    @Description("Product is displayed in line_items_search_view and can be added to cart")
//    public void canAddProductToCart_admin(String testData) throws IOException {
//        provideTestData(testData);
//        createCustomer();
//        createCart(customerId);
//
//        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//        cartPage.clickEditBtn("Line Items");
//        cartPage.searchForItem(skuTitle);
//        cartPage.addFoundItem(skuTitle);
//
//        cartPage.lineItem_editing(productTitle).shouldBe(visible);
//    }
//
//    @Test(priority = 2, dataProvider = "productNotDisplayedLineItemsSearchView")
//    @Description("Product isn't displayed in line_items_search_view")
//    public void productNotDisplayedLineItemsSearchView(String testData) throws IOException {
//        provideTestData(testData);
//        createCustomer();
//        createCart(customerId);
//
//        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//        cartPage.clickEditBtn("Line Items");
//        cartPage.searchForItem(skuTitle);
//
//        cartPage.lineItemSearchView_byName(skuTitle).shouldNotBe(visible);
//    }

    @Test(priority = 3, dataProvider = "canAddSkuToCart_admin")
    @Description("SKU is found in line_items_search_view and can be added to cart")
    public void canAddSkuToCart_admin(String testData) throws IOException {
        provideTestData(testData);
        createCustomer();
        createCart(customerId);

        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        cartPage.clickEditBtn("Line Items");
        cartPage.searchForItem(skuCode);
        cartPage.addFoundItem(skuCode);

        cartPage.lineItem_editing(skuCode).shouldBe(visible);
    }

    @Test(priority = 4, dataProvider = "skuNotDisplayedLineItemsSearchView")
    @Description("SKU is not found in line_items_search_view and can be added to cart")
    public void skuNotDisplayedLineItemsSearchView(String testData) throws IOException {
        provideTestData(testData);
        createCustomer();
        createCart(customerId);

        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        cartPage.clickEditBtn("Line Items");
        cartPage.searchForItem(skuCode);

        cartPage.lineItemSearchView_byName(skuCode).shouldNotBe(visible);
    }

    //--------------------- SKUS
    @Test(priority = 5, dataProvider = "skuCreatedAlongWithProduct")
    @Description("SKU is created along with the product")
    public void skuCreatedAlongWithProduct(String testData) throws IOException {
        provideTestData(testData);


        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.search(skuCode);
        skusPage.openSKU(skuCode);

        skusPage.breadcrumb(skuCode).shouldBe(visible);
    }

    @Test(priority = 6, dataProvider = "newSkuInheritsProductState")
    @Description("SKU created along with product inherits product's state")
    public void newSkuInheritsProductState(String testData, String expState) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);

        skusPage.stateVal().shouldHave(text(expState));
    }

    @Test(priority = 7, dataProvider = "skuIsNotArchived")
    @Description("SKU is not removed from skus_search_view")
    public void skuArchiving_skuIsNotRemovedFromSearchView(String testData) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.search(skuCode);
        skusPage.openSKU(skuCode);

        skusPage.breadcrumb(skuCode).shouldBe(visible);
    }

    @Test(priority = 8, dataProvider = "archivedSkuRemovedFromSkusSearchView")
    @Description("Archived SKU is not displayed in skus_search_view")
    public void archivedSkuRemovedFromProductsSearchView(String testData) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.search(skuCode);
        skusPage.noSearchResultsMsg().shouldBe(visible);

        skusPage.addFilter("SKU : Is Archived", "Yes");
        $(By.xpath("//*[text()='" + skuCode + "']")).shouldBe(visible);
    }


    //--------------------- PRODUCTS
    @Test(priority = 9, dataProvider = "productIsNotArchived")
    @Description("Product is not removed from products_search_view")
    public void productArchiving_productIsDisplayedInSearchView(String testData) throws IOException {
        provideTestData(testData);

        productsPage = openPage(adminUrl + "/products", ProductsPage.class);

        productsPage.search(productTitle);
        productsPage.openProduct(productTitle);

        productsPage.breadcrumb(productId).shouldBe(visible);
    }

    @Test(priority = 10, dataProvider = "archivedProductRemovedFromProductsSearchView")
    @Description("Archived product is not displayed in products_search_view")
    public void archivedProductRemovedFromProductsSearchView(String testData) throws IOException {
        provideTestData(testData);

        productsPage = openPage(adminUrl + "/products", ProductsPage.class);
        productsPage.search(productTitle);
        productsPage.noSearchResultsMsg().shouldBe(visible);

        productsPage.addFilter("Product : Is Archived", "Yes");
        $(By.xpath("//*[text()='" + productTitle + "']")).shouldBe(visible);
    }

}