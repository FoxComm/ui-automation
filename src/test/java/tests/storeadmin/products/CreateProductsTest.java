package tests.storeadmin.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.ProductsPage;
import pages.admin.StorefrontCategoryPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class CreateProductsTest extends Preconditions {

    private ProductsPage p;
    private StorefrontCategoryPage sf;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    // TODO: Can create prod with $0.00 prices SKU
    // TODO: SKU created along with creating a product inherits product's state
    // TODO: Add coverage for custom properties for TPG storefront
    // TODO: Add coverage for product slugs

    @Test(priority = 1)
    public void productIsDisplayed_admin() throws IOException {
        provideTestData("active SKU");
        checkInventoryAvailability(skuCode);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p = openPage(adminUrl + "/products", ProductsPage.class);
        p.clickAddNewProduct();
        p.createProduct(productTitle, skuCode, "27.18", "27.18", "sunglasses", "Active");
        p.navigateTo("Products");
        p.waitForDataToLoad();
        p.search(randomId);

        p.getProductParamVal("1", "Title").shouldHave(text(productTitle));
    }

    @Test(priority = 2)
    public void skuIsApplied() throws IOException {
        provideTestData("active product, has tag, active SKU");
        checkInventoryAvailability(skuCode);
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.sku(skuCode).shouldBe(visible);
    }

    @Test(priority = 3)
    @Description("retailPrice and salePrice are atuofilled with price values of the selected SKU")
    public void pricesAutofilled() throws IOException {
        provideTestData("active SKU");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products", ProductsPage.class);
        p.clickAddNewProduct();
        p.addExistingSKU(skuCode);

        p.skuSalePriceFld("1").shouldHave(value("50.00"));
        p.skuRetailPriceFld("1").shouldHave(value("50.00"));
    }

//    TODO: Split this test
//    TODO: Move test to API level
//    @Test(priority = 3)
//    public void activeProductWithTag_isDisplayed_storefront() throws IOException {
//
//        provideTestData("active product, has tag, active SKU");
////        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);      // test might fail if to somment this
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//
//        shouldBeVisible(sf.product(productTitle), "Product isn't displayed on the category page on sf.");
//        sf.openProduct(productTitle);
//        sf.titleVal().shouldHave(text(productTitle)
//                .because("Incorrect product title is displayed on PDP."));
//        sf.priceVal().shouldHave(text("$50.00")
//                .because("Incorrect product price is displayed on PDP."));
//        sf.descriptionVal().shouldHave(text("The best thing to buy in 2016!")
//                .because("Incorrect product description is displayed on PDP."));
//
//    }
//
//    @Test(priority = 4)
//    public void activeProductNoTag_notDisplayed_storefront() throws IOException {
//
//        provideTestData("active product, no tag, active SKU");
////        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product is displayed on the category page on sf."));
//
//    }
//
//    @Test(priority = 5)
//    public void activeProductNoTag_canBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("active product, no tag, active SKU");
////        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productTitle);
//        sf.waitForSearchResultsToLoad();
//        sf.product(productTitle).shouldBe(visible
//                .because("Product isn't found by search."));
//
//    }
//
//    @Test(priority = 6)
//    public void inactiveProductHasTag_notDisplayed_storefront() throws IOException {
//
//        provideTestData("inactive product, has tag, active SKU");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product is displayed on the category page on sf."));
//
//    }
//
//    @Test(priority = 7)
//    public void inactiveProductHasTag_cantBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("inactive product, has tag, active SKU");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productTitle);
//        sf.waitForSearchResultsToLoad();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product isn't found by search."));
//
//    }
//
//    @Test(priority = 8)
//    public void inactiveProductNoTag_notDisplayed_storefront() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product is displayed on the category page on sf."));
//
//    }
//
//    @Test(priority = 9)
//    public void inactiveProductNoTag_cantBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productTitle);
//        sf.waitForSearchResultsToLoad();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product is found by search."));
//
//    }
//
//    @Test(priority = 10)
//    public void activeProductHasTag_inactiveSKU__notDisplayed_storefront() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product is displayed on the category page on sf."));
//
//    }
//
//    @Test(priority = 11)
//    public void activeProductHasTag_inactiveSKU__cantBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productTitle);
//        sf.waitForSearchResultsToLoad();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product is found by search."));
//
//    }
//
//    @Test(priority = 12)
//    public void activeProductNoTag_inactiveSKU__notDisplayed_storefront() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product is displayed on the category page on sf."));
//
//    }
//
//    @Test(priority = 13)
//    public void activeProductNoTag_inactiveSKU__cantBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
////        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productTitle);
//        sf.waitForSearchResultsToLoad();
//        sf.product(productTitle).shouldNotBe(visible
//                .because("Product is found by search."));
//
//    }

    //------------------------

    @Test(priority = 14)
    @Description("A just created product is displayed in category_view, its PDP can be accessed")
    public void createProduct_SKU_inactive() throws IOException {
        provideTestData("inactive SKU");
        checkInventoryAvailability(skuCode);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p = open(adminUrl+ "/products/default/new", ProductsPage.class);
        p.createProduct(productTitle, skuCode, "27.18", "27.18", "sunglasses", "Active");
        p.clickSave_wait();
        p.navigateTo("Products");
        p.waitForDataToLoad();
        p.search(randomId);

        p.getProductParamVal("1", "Title").shouldHave(text(productTitle));
    }

    //TODO: Move test to API level
    @Test(priority = 15)
    @Description("A just created product is displayed in category_view, its PDP can be accessed")
    public void createProduct_SKU_noTitle() throws IOException {
        provideTestData("SKU with no title");
        checkInventoryAvailability(skuCode);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.createProduct(productTitle, skuCode, "27.18", "27.18", "sunglasses", "Active");
        p.clickSave_wait();
        p.navigateTo("Products");
        p.waitForDataToLoad();
        p.search(randomId);

        p.getProductParamVal("1", "Title").shouldHave(text(productTitle));
    }

    @Test(priority = 16)
    public void createProduct_SKU_noDescription() throws IOException {
        provideTestData("SKU with no description");
        checkInventoryAvailability(skuCode);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.createProduct(productTitle, skuCode, "27.18", "27.18", "sunglasses", "Active");
        p.clickSave_wait();
        p.navigateTo("Products");
        p.waitForDataToLoad();
        p.search(randomId);

        p.getProductParamVal("1", "Title").shouldHave(text(productTitle));
    }

    //TODO: Move test to API level
    // {"errors":["Object sku with id=1433 doesn't pass validation: $.retailPrice.value: null found, number expected"
//    @Test(priority = 17)
//    public void createProduct_SKU_noPrices() throws IOException {
//        provideTestData("SKU with no prices set");
//        String randomId = generateRandomID();
//        String productTitle = "Test Product " + randomId;
//
//        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
//        p.createProduct(productTitle, skuCode, "27.18", "27.18", "sunglasses", "Active");
//        p.clickSave_wait();
//        p.navigateTo("Products");
//        p.waitForDataToLoad();
//        p.search(randomId);
//        p.openProduct(productTitle);
//
//        p.sku(skuCode).shouldBe(visible);
//    }

}