package tests.storeadmin.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CartPage;
import pages.admin.LoginPage;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class ShippingMethodTest extends Preconditions {

    CartPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    @Test(priority = 1)
    public void setShippingMethod() throws IOException {
        provideTestData("cart with chosen shipping address");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Shipping Method");
        p.selectShipMethod("1");
        p.clickDoneBtn("Shipping Method");
        p.assertShipMethodDefined();
    }

}
