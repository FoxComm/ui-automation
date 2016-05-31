package storefront.tests.cart.googleauth;

import org.testng.annotations.BeforeMethod;
import storefront.base.BaseTest;
import storefront.pages.CartPage;
import org.testng.annotations.Test;

public class AddMultipleItemsTest_GA extends BaseTest {

    private CartPage page = new CartPage(driver);

    @BeforeMethod
    public void setUp() {
        if ( !(page.checkUserLoginStatus()) ) {

            page.clickLogo();
            page.googleAuth("2278qatest2278@gmail.com", "78qa22!#");
            page.waitForElementToBePresent(page.lastItem);
            page.assertUserName("JOHN GOOGLEMAN");

        }
    }

    @Test
    public void addItemsToCart_GoogleAuthTest() {

        page.navigateToItem(4);
        page.addToCartBtn.click();
            page.assertCartHasItems(1);
            page.assertItemQuantity(1, 1);

        page.logoBtn.click();

        page.navigateToItem(5);
        page.increaseQtyBtn.click();
        page.addToCartBtn.click();
            page.assertCartHasItems(2);     //assertion reworked, need to test
            page.assertItemQuantity(2, 2);  //assertion reworked, need to test

    }


}
