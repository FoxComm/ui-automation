package storefront.tests.login;

import org.testng.annotations.AfterClass;
import storefront.base.BaseTest;
import storefront.pages.LoginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class GoogleAuthTest extends BaseTest {

    private LoginPage page = new LoginPage(driver);

    @Test(priority = 2)
    public void googleAuthTest() {

        page.googleAuth("qatest2278@gmail.com", "78qa22!#");
        page.assertUserName("JOHN GOOGLEMAN");

    }

    @AfterClass
    public void cleanUpTest() {
        if (page.checkUserLoginStatus()) {
            page.logout();
            elementHasText(page.logInBtn, "LOG IN");
        }
        page.clickLogo();
    }

}
