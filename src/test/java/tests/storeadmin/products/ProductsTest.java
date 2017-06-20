package tests.storeadmin.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CartPage;
import pages.admin.LoginPage;
import pages.admin.ProductsPage;
import pages.admin.SkusPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
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

    // Temporarily disabled, because it duplicates similar SKU specific tests
    // search for line item by productTitle should be added in the nearest future
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
//        cartPage.searchLineItem(skuTitle);
//        cartPage.addFoundLineItem(skuTitle);
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
//        cartPage.searchLineItem(skuTitle);
//
//        cartPage.lineItemSearchView_byName(skuTitle).shouldNotBe(visible);
//    }

    @Test(priority = 3, dataProvider = "canAddSkuToCart_admin")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Parametrized Tests"})
    @Stories("Products : Behavior")
    @Description("SKU is found in line_items_search_view and can be added to cart")
    public void canAddSkuToCart_admin(String testData) throws IOException {
        provideTestData(testData);
        createCustomer();
        createCart(customerId);

        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        cartPage.clickEditBtn("Line Items");
        cartPage.searchLineItem(skuCode);
        cartPage.addFoundLineItem(skuCode);

        cartPage.lineItem_editing(skuCode).shouldBe(visible);
    }

    @Test(priority = 4, dataProvider = "skuNotDisplayedLineItemsSearchView")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Parametrized Tests"})
    @Stories("Products : Ashes Behavior")
    @Description("SKU is not found in line_items_search_view and can be added to cart")
    public void skuNotDisplayedLineItemsSearchView(String testData) throws IOException {
        provideTestData(testData);
        createCustomer();
        createCart(customerId);

        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        cartPage.clickEditBtn("Line Items");
        cartPage.searchLineItem(skuCode);

        cartPage.lineItemSearchView_byName(skuCode).shouldNotBe(visible);
    }

    //--------------------- SKUS
    @Test(priority = 5, dataProvider = "skuCreatedAlongWithProduct")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Parametrized Tests"})
    @Stories("Products : Ashes Behavior")
    @Description("SKU is created along with the product")
    public void skuCreatedAlongWithProduct(String testData) throws IOException {
        provideTestData(testData);


        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.addFilter("SKU : Title : ", skuCode);
        skusPage.openSKU(skuCode);

        skusPage.breadcrumb(skuCode).shouldBe(visible);
    }

    @Test(priority = 6, dataProvider = "newSkuInheritsProductState")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Parametrized Tests"})
    @Stories("Products : Ashes Behavior")
    @Description("SKU created along with product inherits product's state")
    public void newSkuInheritsProductState(String testData, String expState) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);

        skusPage.stateVal().shouldHave(text(expState));
    }

    @Test(priority = 7, dataProvider = "skuIsNotArchived")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Parametrized Tests"})
    @Stories("Products : Ashes Behavior")
    @Description("SKU is not removed from skus_search_view -- it's displayed on the table, details page can be accessed")
    public void skuArchiving_skuIsNotRemovedFromSearchView(String testData) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.addFilter("SKU : Title : ", skuCode);
        waitForDataToLoad();

        skusPage.objOnCategoryTable(skuCode).shouldBe(visible);
        skusPage.openSKU(skuCode);
        skusPage.breadcrumb(skuCode).shouldBe(visible);
    }

    @Test(priority = 8, dataProvider = "archivedSkuRemovedFromSkusSearchView")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Parametrized Tests"})
    @Stories("Products : Ashes Behavior")
    @Description("Archived SKU is not displayed in skus_search_view")
    public void archivedSkuRemovedFromProductsSearchView(String testData) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.addFilter("SKU : Title : ", skuCode);
        skusPage.noSearchResultsMsg().shouldBe(visible);

        skusPage.addFilter("SKU : Is Archived", "Yes");
        skusPage.objOnCategoryTable(skuCode).shouldBe(visible);
    }


    //--------------------- PRODUCTS
    @Test(priority = 9, dataProvider = "productIsNotArchived")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Parametrized Tests"})
    @Stories("Products : Ashes Behavior")
    @Description("Product is not removed from products_search_view")
    public void productArchiving_productIsDisplayedInSearchView(String testData) throws IOException {
        provideTestData(testData);

        productsPage = openPage(adminUrl + "/products", ProductsPage.class);

        productsPage.addFilter("Product : Name : ", productTitle);
        productsPage.openProduct(productTitle);

        productsPage.breadcrumb(productId).shouldBe(visible);
    }

    @Test(priority = 10, dataProvider = "archivedProductRemovedFromProductsSearchView")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Parametrized Tests"})
    @Stories("Products : Ashes Behavior")
    @Description("Archived product is not displayed in products_search_view")
    public void archivedProductRemovedFromProductsSearchView(String testData) throws IOException {
        provideTestData(testData);

        productsPage = openPage(adminUrl + "/products", ProductsPage.class);
        productsPage.addFilter("Product : Name : ", productTitle);
        productsPage.noSearchResultsMsg().shouldBe(visible);

        productsPage.addFilter("Product : Is Archived", "Yes");
        productsPage.objOnCategoryTable(productTitle).shouldBe(visible);
    }

}