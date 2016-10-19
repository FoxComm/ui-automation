package tests.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import pages.StorefrontCategoryPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class CreateProductsTest extends DataProvider {

    private ProductsPage p;
    private StorefrontCategoryPage sf;

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
    public void productIsDisplayed_admin() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/products", ProductsPage.class);
        String randomId = generateRandomID();
        String productName = "Test Product " + randomId;

        p.clickAddNewProduct();
        p.createProduct(productName, sku, "27.18", "27.18", "sunglasses", "Active");
        p.navigateTo("Catalog", "Products");
        p.waitForDataToLoad();
        p.addFilter("Product : Name", randomId);
        p.getProductParamVal("1", "Name").shouldHave(text(productName)
                .because("Product is not found - either search doesn't work or product wasn't created."));

    }

    @Test(priority = 2)
    public void skuIsApplied() throws IOException {

        provideTestData("active product, has tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.sku(sku).shouldBe(visible
                .because("'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront."));

    }
    // SPLIT THIS TEST
    @Test(priority = 3)
    public void activeProductWithTag_isDisplayed_storefront() throws IOException {

        provideTestData("active product, has tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);      // test might fail if to somment this
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();

        shouldBeVisible(sf.product(productName), "Product isn't displayed on the category page on sf.");
        sf.openProduct(productName);
        sf.titleVal().shouldHave(text(productName)
                .because("Incorrect product title is displayed on PDP."));
        sf.priceVal().shouldHave(text("$50.00")
                .because("Incorrect product price is displayed on PDP."));
        sf.descriptionVal().shouldHave(text("The best thing to buy in 2016!")
                .because("Incorrect product description is displayed on PDP."));

    }

    @Test(priority = 4)
    public void activeProductNoTag_notDisplayed_storefront() throws IOException {

        provideTestData("active product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        sf.product(productName).shouldNotBe(visible
                .because("Product is displayed on the category page on sf."));

    }

    @Test(priority = 5)
    public void activeProductNoTag_canBeFound_storefrontSearch() throws IOException {

        provideTestData("active product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
////        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        sf.product(productName).shouldBe(visible
                .because("Product isn't found by search."));

    }

    @Test(priority = 6)
    public void inactiveProductHasTag_notDisplayed_storefront() throws IOException {

        provideTestData("inactive product, has tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        sf.product(productName).shouldNotBe(visible
                .because("Product is displayed on the category page on sf."));

    }

    @Test(priority = 7)
    public void inactiveProductHasTag_cantBeFound_storefrontSearch() throws IOException {

        provideTestData("inactive product, has tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        sf.product(productName).shouldNotBe(visible
                .because("Product isn't found by search."));

    }

    @Test(priority = 8)
    public void inactiveProductNoTag_notDisplayed_storefront() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        sf.product(productName).shouldNotBe(visible
                .because("Product is displayed on the category page on sf."));

    }

    @Test(priority = 9)
    public void inactiveProductNoTag_cantBeFound_storefrontSearch() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        sf.product(productName).shouldNotBe(visible
                .because("Product is found by search."));

    }

    @Test(priority = 10)
    public void activeProductHasTag_inactiveSKU__notDisplayed_storefront() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        sf.product(productName).shouldNotBe(visible
                .because("Product is displayed on the category page on sf."));

    }

    @Test(priority = 11)
    public void activeProductHasTag_inactiveSKU__cantBeFound_storefrontSearch() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        sf.product(productName).shouldNotBe(visible
                .because("Product is found by search."));

    }

    @Test(priority = 12)
    public void activeProductNoTag_inactiveSKU__notDisplayed_storefront() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        sf.product(productName).shouldNotBe(visible
                .because("Product is displayed on the category page on sf."));

    }

    @Test(priority = 13)
    public void activeProductNoTag_inactiveSKU__cantBeFound_storefrontSearch() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        sf.product(productName).shouldNotBe(visible
                .because("Product is found by search."));

    }

    //------------------------

    @Test(priority = 14)
    public void createProduct_SKU_inactive() throws IOException {

        provideTestData("inactive SKU");
        p = open(adminUrl+ "/products/default/new", ProductsPage.class);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p.createProduct(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
        p.clickSave();
        p.navigateTo("Catalog", "Products");
        p.waitForDataToLoad();

        p.addFilter("Product : Name", randomId);
        shouldHaveText(p.getProductParamVal("1", "Name"), "Test Product " + randomId,
                "Queried product is not found - either search doesn't work or product wasn't created.");
        p.openProduct(productTitle);
//        p.assertSKUApplied();

    }

    @Test(priority = 15)
    public void createProduct_SKU_noTitle() throws IOException {

        provideTestData("SKU with no title");
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p.createProduct(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
        p.clickSave();
        p.navigateTo("Catalog", "Products");
        p.waitForDataToLoad();

        p.addFilter("Product : Name", randomId);
        p.getProductParamVal("1", "Name").shouldHave(text("Test Product " + randomId)
                .because("Queried product is not found - either search doesn't work or product wasn't created."));
        p.openProduct(productTitle);
//        p.assertSKUApplied();

    }

    @Test(priority = 16)
    public void createProduct_SKU_noDescription() throws IOException {

        provideTestData("SKU with no description");
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p.createProduct(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
        p.clickSave();
        p.navigateTo("Catalog", "Products");
        p.waitForDataToLoad();
        p.addFilter("Product : Name", randomId);
        p.openProduct(productTitle);
//        p.assertSKUApplied();

    }

    @Test(priority = 17)
    public void createProduct_SKU_noPrices() throws IOException {

        provideTestData("SKU with no prices set");
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p.createProduct(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
        p.clickSave();
        p.navigateTo("Catalog", "Products");
        p.waitForDataToLoad();

        p.addFilter("Product : Name", randomId);
        shouldHaveText(p.getProductParamVal("1", "Name"), "Test Product " + randomId,
                "Queried product is not found - either search doesn't work correctly or product wasn't created");
        p.openProduct(productTitle);
//        p.assertSKUApplied();

    }

}
