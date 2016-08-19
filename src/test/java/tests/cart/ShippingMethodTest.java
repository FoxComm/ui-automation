package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertTrue;

public class ShippingMethodTest extends DataProvider {

    CartPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void setShipMethod() throws IOException {

        provideTestData("cart with chosen shipping address");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        click( p.editBtn_shipMethod() );
        jsClick( p.shipMethodRdbtn() );
        click( p.doneBtn_shipMethod() );
        assertTrue( p.isShipMethodDefined(), "Shipping Method isn't defined" );

    }

}
