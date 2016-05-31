package storefront.tests.checkout;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import storefront.base.BaseTest;
import storefront.pages.CartPage;
import storefront.pages.CheckoutPage;

public class PlaceOrderTest_RA extends BaseTest {

    private CheckoutPage page = new CheckoutPage(driver);

    @BeforeMethod
    public void setUp() {

        openPage("http://stage.foxcommerce.com/");

        // xpath = LoginPage.logInBtn
        elementIsPresent(getWebDriver().
                findElement(By.xpath(".//div[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div")));

        if ( !(page.checkUserLoginStatus()) ) {

            page.clickLogo();
            page.logIn("2278qatest2278@gmail.com", "78qa22!#");
            page.waitForElementToBePresent(page.lastItem);
            page.assertUserName("JOHN DOE");
            page.cleanUpCartIfNeeded();

        }
    }

    @Test
    public void placeOrderTest() {

        page.addItemToCart();
        page.proceedToCheckout();
        page.setShippingAddress("John Doe", "2101, Green Valley #320", "10001", "9879879876");
        page.selectDelivery();
        page.setBilling("John Doe", "4242424242424242", "123");
        page.placeOrderBtn.click();

        waitForElementToBePresent(page.takeMeHomeBtn);

    }





    @AfterClass
    public void cleanUp() {
        page.clickLogo();

        if (page.checkUserLoginStatus()) {

            page.cleanUpCartIfNeeded();
            page.logout();
            elementHasText(page.logInBtn, "LOG IN");

        }
    }

}
