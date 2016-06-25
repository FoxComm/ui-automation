package tests.orderstests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertTrue;

public class ShippingMethodTest extends DataProvider {

    OrderDetailsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open("http://admin.stage.foxcommerce.com/");
        if ( (Objects.equals(getUrl(), "http://admin.stage.foxcommerce.com/login")) ) {
            LoginPage loginPage = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void setShipMethod() throws IOException {

        provideTestData("cart with chosen shipping address");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        click( p.editBtn_shipMethod() );
        jsClick( p.shipMethodRdbtn() );
        click( p.doneBtn_shipMethod() );
        assertTrue( p.isShipMethodDefined(), "Shipping Method isn't defined" );

    }

}
