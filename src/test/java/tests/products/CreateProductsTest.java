package tests.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import pages.StorefrontCategoryPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CreateProductsTest extends DataProvider {

    private ProductsPage p;
    private StorefrontCategoryPage sf;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

//    @Test(priority = 1)
//    public void productIsDisplayed_admin() throws IOException {
//
//        provideTestData("active SKU");
//        p = open(adminUrl + "/products", ProductsPage.class);
//        String randomId = generateRandomID();
//        String productName = "Test Product " + randomId;
//
//        p.fillOutProductForm( productName, sku, "27.18", "27.18", "sunglasses", "Active");
//        click( p.productsNavMenu() );
//        p.waitForDataToLoad();
//        p.addFilter("Product", "Name", randomId);
//        assertEquals( p.getProductParamVal("1", "Name"), productName,
//                "Product is not found - either search doesn't work or product wasn't created.");
//
//    }
//
//    @Test(priority = 2)
//    public void skuIsApplied() throws IOException {
//
//        provideTestData("active product, has tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//    }

    @Test(priority = 3)
    public void activeProductWithTag_isDisplayed_storefront() throws IOException {

        provideTestData("active product, has tag, active SKU");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
        sleep(1500);
        p.assertSKUApplied();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        assertTrue( sf.productDisplayed(productName),
                "Product isn't displayed on the category page on sf.");
        sf.openProduct(productName);
        assertEquals( sf.titleVal(), productName,
                "Incorrect product title is displayed on PDP.");
        assertEquals( sf.priceVal(), "27.18",
                "Incorrect product price is displayed on PDP.");
        assertEquals( sf.descriptionVal(), "The best thing to buy in 2016!",
                "Incorrect product description is displayed on PDP.");

    }

//    @Test(priority = 4)
//    public void activeProductNoTag_notDisplayed_storefront() throws IOException {
//
//        provideTestData("active product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        assertTrue( !sf.productDisplayed(productName),
//                "Product is displayed on the category page on sf.");
//
//    }
//
//    @Test(priority = 5)
//    public void activeProductNoTag_canBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("active product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productName);
//        sf.waitForSearchResultsToLoad();
//        assertTrue( sf.productDisplayed(productName),
//                "Product isn't found by search.");
//
//    }
//
//    @Test(priority = 6)
//    public void inactiveProductHasTag_notDisplayed_storefront() throws IOException {
//
//        provideTestData("inactive product, has tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        assertTrue( !sf.productDisplayed(productName),
//                "Product is displayed on the category page on sf.");
//
//    }
//
//    @Test(priority = 7)
//    public void inactiveProductHasTag_cantBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("inactive product, has tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productName);
//        sf.waitForSearchResultsToLoad();
//        assertTrue( !sf.productIsFound(productName),
//                "Product isn't found by search.");
//
//    }
//
//    @Test(priority = 8)
//    public void inactiveProductNoTag_notDisplayed_storefront() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        assertTrue( !sf.productDisplayed(productName),
//                "Product is displayed on the category page on sf.");
//
//    }
//
//    @Test(priority = 9)
//    public void inactiveProductNoTag_cantBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productName);
//        sf.waitForSearchResultsToLoad();
//        assertTrue( !sf.productIsFound(productName),
//                "Product is found by search.");
//
//    }
//
//    @Test(priority = 10)
//    public void activeProductHasTag_inactiveSKU__notDisplayed_storefront() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        assertTrue( !sf.productDisplayed(productName),
//                "Product is displayed on the category page on sf.");
//
//    }
//
//    @Test(priority = 11)
//    public void activeProductHasTag_inactiveSKU__cantBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productName);
//        sf.waitForSearchResultsToLoad();
//        assertTrue( !sf.productIsFound(productName),
//                "Product is found by search.");
//
//    }
//
//    @Test(priority = 12)
//    public void activeProductNoTag_inactiveSKU__notDisplayed_storefront() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        assertTrue( !sf.productDisplayed(productName),
//                "Product is displayed on the category page on sf.");
//
//    }
//
//    @Test(priority = 13)
//    public void activeProductNoTag_inactiveSKU__cantBeFound_storefrontSearch() throws IOException {
//
//        provideTestData("inactive product, no tag, active SKU");
//        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        sleep(1500);
//        p.assertSKUApplied();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.search(productName);
//        sf.waitForSearchResultsToLoad();
//        assertTrue( !sf.productIsFound(productName),
//                "Product is found by search.");
//
//    }
//
//    //------------------------
//
//    @Test(priority = 14)
//    public void createProduct_SKU_inactive() throws IOException {
//
//        provideTestData("inactive SKU");
//        p = open(adminUrl+ "/products", ProductsPage.class);
//        String randomId = generateRandomID();
//        String productTitle = "Test Product " + randomId;
//
//        p.fillOutProductForm(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
//        click( p.saveDraftBtn() );
//        click( p.productsNavMenu() );
//        p.waitForDataToLoad();
//
//        p.addFilter("Product", "Name", randomId);
//        assertEquals(p.getProductParamVal("1", "Name"), "Test Product " + randomId,
//                "Queried product is not found - either search doesn't work or product wasn't created.");
//        p.openProduct( productTitle );
//        sleep(1500);
//        p.assertSKUApplied();
//
//    }
//
//    @Test(priority = 15)
//    public void createProduct_SKU_noTitle() throws IOException {
//
//        provideTestData("SKU with no title");
//        p = open(adminUrl + "/products", ProductsPage.class);
//        String randomId = generateRandomID();
//        String productTitle = "Test Product " + randomId;
//
//        p.fillOutProductForm(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
//        click(p.saveDraftBtn());
//        click(p.productsNavMenu());
//        p.waitForDataToLoad();
//
//        p.addFilter("Product", "Name", randomId);
//        assertEquals(p.getProductParamVal("1", "Name"), "Test Product " + randomId,
//                "Queried product is not found - either search doesn't work or product wasn't created.");
//        p.openProduct( productTitle );
//        sleep(1500);
//        p.assertSKUApplied();
//
//    }
//
//    @Test(priority = 16)
//    public void createProduct_SKU_noDescription() throws IOException {
//
//        provideTestData("SKU with no description");
//        p = open(adminUrl + "/products", ProductsPage.class);
//        String randomId = generateRandomID();
//        String productTitle = "Test Product " + randomId;
//
//        p.fillOutProductForm(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
//        click(p.saveDraftBtn());
//        click(p.productsNavMenu());
//        p.waitForDataToLoad();
//        p.addFilter("Product", "Name", randomId);
//        p.openProduct( productTitle );
//        sleep(1500);
//        p.assertSKUApplied();
//
//    }
//
//    @Test(priority = 17)
//    public void createProduct_SKU_noPrices() throws IOException {
//
//        provideTestData("SKU with no prices set");
//        p = open(adminUrl + "/products", ProductsPage.class);
//        String randomId = generateRandomID();
//        String productTitle = "Test Product " + randomId;
//
//        p.fillOutProductForm(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
//        click(p.saveDraftBtn());
//        click(p.productsNavMenu());
//        p.waitForDataToLoad();
//
//        p.addFilter("Product", "Name", randomId);
//        assertEquals(p.getProductParamVal("1", "Name"), "Test Product " + randomId,
//                "Queried product is not found - either search doesn't work or product wasn't created.");
//        p.openProduct( productTitle );
//        sleep(1500);
//        p.assertSKUApplied();
//
//    }

}
