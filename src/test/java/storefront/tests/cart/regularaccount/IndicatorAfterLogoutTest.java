package storefront.tests.cart.regularaccount;

import org.testng.annotations.AfterClass;
import storefront.base.BaseTest;
import storefront.pages.CartPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class IndicatorAfterLogoutTest extends BaseTest {

    private CartPage page = new CartPage(driver);


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

    @Test
    public void cartIndicatorAfterLogoutTest() {

        page.navigateToItem(1);
        page.addToCart();
            page.assertCartHasItems(1);
        page.closeCart();
        page.logout();
            page.assertCartIndicator(0);      // this assertion seems to be broken as there's a bug and it doesn't catch it

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
