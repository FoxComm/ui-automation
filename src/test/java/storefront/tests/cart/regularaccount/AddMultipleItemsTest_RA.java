package storefront.tests.cart.regularaccount;

import org.testng.annotations.AfterClass;
import storefront.base.BaseTest;
import storefront.pages.CartPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AddMultipleItemsTest_RA extends BaseTest {

    private CartPage page = new CartPage(driver);

    @BeforeMethod
    public void setUp() {
        if ( !(page.checkUserLoginStatus()) ) {

            page.clickLogo();
            page.logIn("2278qatest2278@gmail.com", "78qa22!#");
            page.waitForElementToBePresent(page.lastItem);
            page.assertUserName("JOHN DOE");
            page.cleanUpCartIfNeeded();

        }
    }

    @Test
    public void multipleItemsToCartTest() {

        // Add 1st Item :: Qty: 1; Total Items in Cart: 1; Indicator: 1;
        page.navigateToItem(2);
        page.addToCart();
            page.assertCartHasItems(1);
            page.assertItemQuantity(1, 1);
        page.closeCart();
            page.assertCartIndicator("1");

        page.clickLogo();

        // Add 2nd Item :: Qty: 3; Total Items in Cart: 2; Indicator: 4;
        page.navigateToItem(3);
        page.increaseQty(2);
        page.addToCart();
            page.assertCartHasItems(2);
            page.assertItemQuantity(1, 1);
            page.assertItemQuantity(2, 3);
        page.closeCart();
            page.assertCartIndicator("4");

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
