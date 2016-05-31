package storefront._testpackage;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import storefront.base.BaseTest;
import storefront.pages.LoginPage;

public class TestClass extends BaseTest {

    private LoginPage page = new LoginPage(driver);

    @Test
    public void testOne() {

        page.googleAuth("qatest2278@gmail.com", "78qa22!#");
        page.assertUserName("JOHN GOOGLEMAN");

    }


    @Test
    public void testTwo() {

        page.googleAuth("qatest2278@gmail.com", "78qa22!#");
        page.assertUserName("JOHN GOOGLEMAN");

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
