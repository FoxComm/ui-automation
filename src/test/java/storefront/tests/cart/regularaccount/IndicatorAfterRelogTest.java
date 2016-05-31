package storefront.tests.cart.regularaccount;

import org.testng.annotations.AfterClass;
import storefront.base.BaseTest;
import storefront.pages.CartPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IndicatorAfterRelogTest extends BaseTest {

    private CartPage page = new CartPage(driver);
    private String indicatorBeforeLogout;

    @BeforeMethod
    public void setUp() {

        if ( !(page.checkUserLoginStatus()) ) {

            page.clickLogo();
            page.logIn("2278qatest2278@gmail.com", "78qa22!#");
            page.waitForElementToBePresent(page.userNameBtn);
            page.assertUserName("JOHN DOE");
            page.cleanUpCartIfNeeded();

        }
    }

    @Test(priority = 4, groups = "cleanUp - YES")
    public void cartIndicatorAfterRelogTest() {

        page.navigateToItem(1);
        page.addToCart();
            page.assertCartHasItems(1);
            page.assertItemQuantity(1, 1);
        page.closeCart();
        indicatorBeforeLogout = page.getIndicatorValue();
        page.logout();
        page.logIn("2278qatest2278@gmail.com", "78qa22!#");
            page.assertCartIndicator(indicatorBeforeLogout);

        softAssert.assertAll();

    }

    @AfterClass
    public void cleanUp() {
        if (page.checkUserLoginStatus()) {

            page.cleanUpCartIfNeeded();
            page.logout();
            elementHasText(page.logInBtn, "LOG IN");

        }
    }

}
