package storefront.tests.cart.regularaccount;

import storefront.base.BaseTest;
import storefront.pages.CartPage;
import org.testng.annotations.*;

public class AddSingleItem_RA extends BaseTest {

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
    public void addItemToCartTest(){

        // Add 1st Item :: Qty: 1; Total Items in Cart: 1;
        page.navigateToItem(1);
        page.increaseQty(1);
        page.addToCart();
            page.assertCartHasItems(1);
            page.assertItemQuantity(1, 2);
        page.closeCart();
            page.assertCartIndicator("2");

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