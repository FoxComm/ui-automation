package tests.orderstests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

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

        p.clickBtn( p.editBtn_shipMethod() );
        p.jsClick( p.shipMethodRdbtn() );
        p.clickBtn( p.doneBtn_shipMethod() );
        Assert.assertTrue( p.isShipMethodDefined(), "Shipping Method isn't defined" );

    }

}
