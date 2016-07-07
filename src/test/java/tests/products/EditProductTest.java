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
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class EditProductTest extends DataProvider {

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
    public void editProduct_title() throws IOException {

        provideTestData("product in active state");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        clearField( p.titleFld() );
        setFieldVal( p.titleFld(), "Edited Product " + uid );
        click( p.saveDraftBtn() );
        p.saveDraftBtn().shouldBe(enabled);
        sleep(1000);
        click( p.productsNavMenu() );
        p.clearSearchFld();
        p.addFilter("Product", "Name", uid);

        assertEquals( p.getProductParamVal("1", "Name"), "Edited Product " + uid,
                "Failed to edit product name.");

    }

    @Test(priority = 2)
    public void editProduct_description() throws IOException {

        provideTestData("product in active state");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        clearField( p.descriptionFld() );
        setFieldVal( p.descriptionFld(), "Edited Description" );
        click( p.saveDraftBtn() );
        p.saveDraftBtn().shouldBe(enabled);
        sleep(1000);
        click( p.productsNavMenu() );
        p.clearSearchFld();
        p.addFilter("Product", "Name", uid);

        p.openProduct(productName);
        assertEquals( p.descriptionFld().text(), "Edited Description",
                "Failed to edit description.");

    }

    @Test(priority = 3)
    public void editProduct_state() throws IOException {

        provideTestData("product in active state");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        p.setState( "Inactive" );
        click( p.saveDraftBtn() );
        p.saveDraftBtn().shouldBe(enabled);

        sf = open("http://stage.foxcommerce.com/sunglasses?type=men", StorefrontCategoryPage.class);
        assertTrue( !sf.productDisplayed(productName),
                "Product is displayed ion the category page on storefront.");

    }

    @Test(priority = 4)
    public void editProduct_retailPrice() throws IOException {

        provideTestData("product in active state");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        setFieldVal( p.retailPriceFld(), "35.18" );
        click( p.saveDraftBtn() );
        p.saveDraftBtn().shouldBe(enabled);
        click( p.productsNavMenu() );
        p.clearSearchFld();
        p.addFilter("Product", "Name", uid);

        p.openProduct(productName);
        assertEquals( p.retailPriceFld().text(), "35.18",
                "Failed to edit retail price.");

    }

    @Test(priority = 5)
    public void editProduct_salePrice() throws IOException {

        provideTestData("product in active state");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);
        String uid = productName.substring(13, 20);

        setFieldVal( p.salePriceFld(), "35.18" );
        click( p.saveDraftBtn() );
        p.saveDraftBtn().shouldBe(enabled);
        click( p.productsNavMenu() );
        p.clearSearchFld();
        p.addFilter("Product", "Name", uid );

        p.openProduct(productName);
        assertEquals( p.salePriceFld().text(), "35.18",
                "Failed to edit sale price.");

    }

    @Test(priority = 6)
    public void editProduct_tag() throws IOException {

        provideTestData("product in active state");
        p = open("http://admin.stage.foxcommerce.com/products/default/" + productId, ProductsPage.class);

        click( p.removeTagBtn("1") );
        p.addTag("eyeglasses");
        click( p.saveDraftBtn() );
        p.saveDraftBtn().shouldBe(enabled);

        sf = open("http://stage.foxcommerce.com/eyeglasses?type=men", StorefrontCategoryPage.class);
        assertTrue( sf.productDisplayed(productName),
                "Product isn't displayed ion the category page on storefront.");

    }

}
