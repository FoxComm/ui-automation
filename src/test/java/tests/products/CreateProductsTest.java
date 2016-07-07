package tests.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import pages.StorefrontCategoryPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CreateProductsTest extends DataProvider {

    private ProductsPage p;
    private StorefrontCategoryPage sf;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open("http://admin.stage.foxcommerce.com/");
        if ( (Objects.equals(getUrl(), "http://admin.stage.foxcommerce.com/login")) ) {
            LoginPage loginPage = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void productIsDisplayed_admin() throws IOException {

        provideTestData("active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/", ProductsPage.class);
        String randomId = generateRandomID();
        String productName = "Test Product " + randomId;

        p.fillOutProductForm( productName, sku, "27.18", "27.18", "sunglasses", "Active");
        click( p.productsNavMenu() );
        p.waitForDataToLoad();
        p.addFilter("Product", "Name", randomId);
        assertEquals( p.getProductParamVal("1", "Name"), productName,
                "Product is not found - either search doesn't work or product wasn't created.");

    }

    @Test(priority = 2)
    public void skuIsApplied() throws IOException {

        provideTestData("active product, has tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

    }

//    p.openProduct( productTitle );
//    p.saveDraftBtn().shouldBe(enabled);
//    assertTrue( !p.noSKUsMsg().is(visible),
//    "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

    @Test(priority = 3)
    public void activeProductWithTag_isDisplayed_storefront() throws IOException {

        provideTestData("active product, has tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        assertTrue( sf.productDisplayed(productName),
                "Product isn't displayed ion the category page on sf.");
        sf.openProduct(productName);
        assertEquals( sf.titleVal(), productName,
                "Wrong product title is displayed on PDP.");
        assertEquals( sf.priceVal(), "27.18",
                "Wrong product price is displayed on PDP.");
        assertEquals( sf.descriptionVal(), "The best thing to buy in 2016!",
                "Wrong product description is displayed on PDP.");

    }

    @Test(priority = 4)
    public void activeProductNoTag_notDisplayed_storefront() throws IOException {

        provideTestData("active product, no tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad();
        assertTrue( !sf.productDisplayed(productName),
                "Product is displayed on the category page on sf.");

    }

    @Test(priority = 5)
    public void activeProductNoTag_canBeFound_storefrontSearch() throws IOException {

        provideTestData("active product, no tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        assertTrue( sf.productDisplayed(productName),
                "Product isn't found by search.");

    }

    @Test(priority = 6)
    public void inactiveProductHasTag_notDisplayed_storefront() throws IOException {

        provideTestData("inactive product, has tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad();
        assertTrue( !sf.productDisplayed(productName),
                "Product is displayed on the category page on sf.");

    }

    @Test(priority = 7)
    public void inactiveProductHasTag_canBeFound_storefrontSearch() throws IOException {

        provideTestData("inactive product, has tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        assertTrue( sf.productDisplayed(productName),
                "Product isn't found by search.");

    }

    @Test(priority = 8)
    public void inactiveProductNoTag_notDisplayed_storefront() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad();
        assertTrue( !sf.productDisplayed(productName),
                "Product is displayed on the category page on sf.");

    }

    @Test(priority = 9)
    public void inactiveProductNoTag_cantBeFound_storefrontSearch() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        assertTrue( sf.productDisplayed(productName),
                "Product is found by search.");

    }

    @Test(priority = 10)
    public void activeProductHasTag_inactiveSKU__notDisplayed_storefront() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad();
        assertTrue( !sf.productDisplayed(productName),
                "Product is displayed on the category page on sf.");

    }

    @Test(priority = 11)
    public void activeProductHasTag_inactiveSKU__cantBeFound_storefrontSearch() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        assertTrue( sf.productDisplayed(productName),
                "Product is found by search.");

    }

    @Test(priority = 12)
    public void activeProductNoTag_inactiveSKU__notDisplayed_storefront() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad();
        assertTrue( !sf.productDisplayed(productName),
                "Product is displayed on the category page on sf.");

    }

    @Test(priority = 13)
    public void activeProductNoTag_inactiveSKU__cantBeFound_storefrontSearch() throws IOException {

        provideTestData("inactive product, no tag, active SKU");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.search(productName);
        sf.waitForSearchResultsToLoad();
        assertTrue( sf.productDisplayed(productName),
                "Product is found by search.");

    }

    //------------------------

    @Test(priority = 14)
    public void createProduct_SKU_inactive() throws IOException {

        provideTestData("inactive SKU");
        p = open("http://admin.stage.foxcommerce.com/products/", ProductsPage.class);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p.fillOutProductForm(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
        click( p.saveDraftBtn() );
        click( p.productsNavMenu() );
        p.waitForDataToLoad();

        p.addFilter("Product", "Name", randomId);
        assertEquals(p.getProductParamVal("1", "Name"), "Test Product " + randomId,
                "Queried product is not found - either search doesn't work or product wasn't created.");
        p.openProduct( productTitle );
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

    }

    @Test(priority = 15)
    public void createProduct_SKU_noTitle() throws IOException {

        provideTestData("SKU with no title");
        p = open("http://admin.stage.foxcommerce.com/products/", ProductsPage.class);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p.fillOutProductForm(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
        click(p.saveDraftBtn());
        click(p.productsNavMenu());
        p.waitForDataToLoad();

        p.addFilter("Product", "Name", randomId);
        assertEquals(p.getProductParamVal("1", "Name"), "Test Product " + randomId,
                "Queried product is not found - either search doesn't work or product wasn't created.");
        p.openProduct( productTitle );
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

    }

    @Test(priority = 16)
    public void createProduct_SKU_noDescription() throws IOException {

        provideTestData("SKU with no description");
        p = open("http://admin.stage.foxcommerce.com/products/", ProductsPage.class);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p.fillOutProductForm(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
        click(p.saveDraftBtn());
        click(p.productsNavMenu());
        p.waitForDataToLoad();
        p.addFilter("Product", "Name", randomId);
        p.openProduct( productTitle );
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront." );

    }

    @Test(priority = 17)
    public void createProduct_SKU_noPrices() throws IOException {

        provideTestData("SKU with no prices set");
        p = open("http://admin.stage.foxcommerce.com/products/", ProductsPage.class);
        String randomId = generateRandomID();
        String productTitle = "Test Product " + randomId;

        p.fillOutProductForm(productTitle, sku, "27.18", "27.18", "sunglasses", "Active");
        click(p.saveDraftBtn());
        click(p.productsNavMenu());
        p.waitForDataToLoad();

        p.addFilter("Product", "Name", randomId);
        assertEquals(p.getProductParamVal("1", "Name"), "Test Product " + randomId,
                "Queried product is not found - either search doesn't work or product wasn't created.");
        p.openProduct( productTitle );
        p.saveDraftBtn().shouldBe(enabled);
        assertTrue( !p.noSKUsMsg().is(visible),
                "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");

    }

}
