package tests.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import pages.StorefrontCategoryPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class EditProductTest extends DataProvider {

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

    @Test(priority = 1)
    public void editTitle_admin_PDP() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        clearField( p.titleFld() );
        setFieldVal( p.titleFld(), "Edited Product " + uid );
        p.clickSave();
        refresh();

        assertEquals( p.titleFld().getValue(), "Edited Product " + uid,
                "Failed to edit product title - incorrect product title is displayed on PDP in admin." );

    }

    @Test(priority = 2)
    public void editTitle_admin_list() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        clearField( p.titleFld() );
        setFieldVal( p.titleFld(), "Edited Product " + uid );
        p.clickSave();
        sleep(1000);
        click( p.productsNavMenu() );
        p.search(uid);

        assertEquals( p.getProductParamVal("1", "Name"), "Edited Product " + uid,
                "Failed to edit product name - incorrect product title is displayed on the list in admin.");

    }

    @Test(priority = 3)
    public void editTitle_storefront_PDP() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        clearField( p.titleFld() );
        setFieldVal( p.titleFld(), "Edited Product " + uid );
        p.clickSave();

        sf = open(storefrontUrl + "/products/" + productId, StorefrontCategoryPage.class);
        assertEquals( sf.titleVal(), "Edited Product " + uid,
                "Failed to edit product title - incorrect product title is displayed on storefront PDP." );

    }

    @Test(priority = 4)
    public void editTitle_storefront_category() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        clearField( p.titleFld() );
        setFieldVal( p.titleFld(), "Edited Product " + uid );
        p.clickSave();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        assertTrue( sf.productDisplayed("Edited Product " + uid),
                "Failed to edit product title - product isn't found by a new title on storefront category page." );

    }

    @Test(priority = 5)
    public void editDescription_admin_PDP() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        clearField( p.descriptionFld() );
        setFieldVal( p.descriptionFld(), "Edited Description" );
        p.clickSave();
        refresh();

        assertEquals( p.descriptionFld().text(), "Edited Description",
                "Failed to edit description - incorrect description is displayed on admin PDP.");

    }

    @Test(priority = 6)
    public void editDescription_storefront_PDP() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        clearField( p.descriptionFld() );
        setFieldVal( p.descriptionFld(), "Edited Description" );
        p.clickSave();

        sf = open(storefrontUrl + "/products/" + productId, StorefrontCategoryPage.class);
        assertEquals( sf.descriptionVal(), "Edited Description",
                "Failed to edit description - incorrect description is displayed on storefront PDP.");

    }

    @Test(priority = 7)
    public void editState_admin_PDP() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        p.setState( "Inactive" );
        p.clickSave();
        sleep(1000);
        refresh();
        p.stateDd().shouldBe(visible);

        assertEquals( p.stateVal(), "Inactive",
                "Failed to edit product state - incorrect product state is displayed on product details page in admin.");

    }

    @Test(priority = 8)
    public void editState_admin_list() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        p.setState( "Inactive" );
        p.clickSave();
        sleep(1000);
        click( p.productsNavMenu() );
        p.search(uid);

        assertEquals( p.getProductParamVal("1", "State"), "Inactive",
                "Failed to edit product state - incorrect state value is displayed on the list in admin.");


    }

    @Test(priority = 9)
    public void editState_storefront() throws IOException {

        provideTestData("product in inactive state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        p.setState( "Active" );
        p.clickSave();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        assertTrue( sf.productDisplayed(productName),
                "Failed to edit product state - product isn't displayed on the category page on storefront.");

    }

    @Test(priority = 10)
    public void editRetailPrice_admin_PDP() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        setFieldVal( p.retailPriceFld(), "35.18" );
        p.clickSave();
        refresh();
        p.retailPriceFld().shouldBe(visible);
        sleep(1000);
        assertEquals( p.retailPriceFld().getValue(), "35.18",
                "Failed to edit retail price.");

    }

    @Test(priority = 11)
    public void editSalePrice_admin_PDP() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        setFieldVal( p.salePriceFld(), "35.18" );
        p.clickSave();
        refresh();
        assertEquals( p.salePriceFld().getValue(), "35.18",
                "Failed to edit sale price.");

    }

    @Test(priority = 12)
    public void editSalePrice_storefront_PDP() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        setFieldVal( p.salePriceFld(), "35.18" );
        p.clickSave();

        sf = open(storefrontUrl + "/products/" + productId, StorefrontCategoryPage.class);
        assertEquals( sf.priceVal(), "35.18",
                "Failed to edit sale price.");

    }

    @Test(priority = 13)
    public void editTag_admin() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        click( p.removeTagBtn("1") );
        p.addTag("eyeglasses");
        p.clickSave();
        refresh();
        p.addTagBtn().shouldBe(visible);
        sleep(1000);

        assertTrue( p.tag("eyeglasses").is(visible),
                "A new tag isn't displayed.");
        assertTrue( !p.tag("sunglasses").is(visible),
                "Removed tag is displayed.");

    }

    @Test(priority = 14)
    public void editTag_storefront() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        click( p.removeTagBtn("1") );
        p.addTag("eyeglasses");
        p.clickSave();

        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        assertTrue( sf.productDisplayed(productName),
                "Product isn't displayed on storefront category page.");

    }

    @Test(priority = 15)
    public void removeTag_admin() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        click( p.removeTagBtn("1") );
        p.clickSave();
        refresh();
        p.addTagBtn().shouldBe(visible);
        assertTrue( !p.tag("eyeglasses").is(visible),
                "A just removed tag is displayed.");

    }

    @Test(priority = 16)
    public void removeTag_storefront() throws IOException {

        provideTestData("product in active state");
        p = open(adminUrl + "/products/default/" + productId, ProductsPage.class);

        click( p.removeTagBtn("1") );
        p.clickSave();

        sf = open(storefrontUrl + "/eyeglasses?type=men", StorefrontCategoryPage.class);
        sf.waitForDataToLoad_sf();
        assertTrue( !sf.productDisplayed(productName),
                "Product is displayed on the storefront category (it shouldn't).");

    }

}
