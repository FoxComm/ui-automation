package storefront.tests.login;

import org.testng.annotations.AfterClass;
import storefront.base.BaseTest;
import storefront.pages.SignupPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class SignupTest extends BaseTest {

    private SignupPage page = new SignupPage(driver);

    @Test
    public void signUpTest() {

        page.logoBtn.click();
        page.logInBtn.click();
        page.signupLink.click();
        setValue(page.fullNameField, "John Doe");
        setValue(page.emailField, "2278qatest2278@gmail.com");
        setValue(page.passwordField, "78qa22!#");
        page.passwordField.submit();
        waitForElementToBePresent(page.loginSubmitBtn);

        // atm test always passes due to a bug,
        // which is about no error message and redirect to login page regardless to signUp success

    }

    @AfterClass
    public void cleanUp() {
        page.closeBtn.click();
        waitForElementToBePresent(page.lastItem);
    }

}