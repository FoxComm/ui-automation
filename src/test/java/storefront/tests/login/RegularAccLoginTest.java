package storefront.tests.login;

import storefront.base.BaseTest;
import storefront.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class RegularAccLoginTest extends BaseTest {

    private LoginPage page = new LoginPage(driver);

    @Test
    public void logInTest() throws InterruptedException {

        openPage("http://stage.foxcommerce.com/");
        elementIsPresent(page.logInBtn);

        page.logInBtn.click();
        setValue(page.emailField, "2278qatest2278@gmail.com");
        setValue(page.passwordField, "78qa22!#");
        page.loginSubmitBtn.click();
        waitForElementToBePresent(page.lastItem);
            page.assertUserName("JOHN DOE");

    }

    @AfterMethod
    public void cleanUpTest() {
        if (page.checkUserLoginStatus()) {
            page.logout();
            elementHasText(page.logInBtn, "LOG IN");
        }
        page.clickLogo();
    }

}
