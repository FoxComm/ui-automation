package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class ShippingMethodTest extends DataProvider {

    CartPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

    @Test(priority = 1)
    public void setShippingMethod() throws IOException {

        provideTestData("cart with chosen shipping address");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_shipMethod();
        p.selectShipMethod("1");
        p.clickDoneBtn_shipMethod();
        p.assertShipMethodDefined();

    }

}
