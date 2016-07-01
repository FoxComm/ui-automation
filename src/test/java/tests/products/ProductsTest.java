package tests.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class ProductsTest extends DataProvider {

    private ProductsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open("http://admin.stage.foxcommerce.com/");
        if ( (Objects.equals(getUrl(), "http://admin.stage.foxcommerce.com/login")) ) {
            LoginPage loginPage = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void createProduct_active() throws IOException {

        provideTestData("");
        p = open("http://admin.stage.foxcommerce.com/products/", ProductsPage.class);

        click( p.addNewProduct() );
        setFieldVal( p.titleFld(), "Product #" + generateRandomID() );
        setFieldVal( p.descriptionFld(), "The best thing to buy in 2016!" );

        setFieldVal( p.sku(), "SKU-TST" );
        setFieldVal( p.retailPriceFld(), "25.25" );
        setFieldVal( p.salePriceFld(), "25.25" );
        p.setState("Active");
        click( p.saveDraftBtn() );
        p.waitForDataToLoad();

        // assertTrue(product is created);



    }

}
