package tests.storefront.auth;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.StorefrontTPGBasePage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;

public class SignUpTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign Up")
    @Description("Can sign up using email that is unique to the system")
    public void signUp_correctCredentials() {
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setName("Customer " + randomId);
        p.setEmail_signUp("qatest2278+" + randomId + "@gmail.com");
        p.setPassword_signUp("78qa22!#");
        p.clickSignUpBtn();

        p.userMenuBtn_sf().should(matchText(randomId.toUpperCase()));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign Up")
    @Description("Name doesn't need to be unique on sign up")
    public void signUp_nameIsNotUnique() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setName(customerName);
        p.setEmail_signUp("qatest2278+" + generateRandomID() + "@gmail.com");
        p.setPassword_signUp("78qa22!#");
        p.clickSignUpBtn();

        p.userMenuBtn_sf().should(matchText(customerName.toUpperCase()));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign Up")
    @Description("Can't sign up using taken email")
    public void signUp_incorrectCredentials_email() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setName("Customer " + generateRandomID());
        p.setEmail_signUp(customerEmail);
        p.setPassword_signUp("78qa22!#");
        p.clickSignUpBtn();

        p.errorMessage("The email address you entered is already in use").shouldBe(visible);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign Up")
    @Description("Auth form is closed after user has successfully signed in")
    public void authFormIsClosed_SignUp() {
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setName("Customer " + randomId);
        p.setEmail_signUp("qatest2278+" + randomId + "@gmail.com");
        p.setPassword_signUp("78qa22!#");
        p.clickSignUpBtn();

        p.userMenuBtn_sf().should(matchText(randomId.toUpperCase()));
    }

    @Test(priority = 5, enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign Up")
    @Description("Can sign up with an email used for guest checkout earlier [Disabled ->  //TODO]")
    public void canSignUp_guestCheckoutEmail() {
        //TODO: add method to Preconditions that does guest checkout
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign Up")
    @Description("Customer is signed in after successful sign up")
    public void authFormClosed_signUpSuccess() {
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setName("Customer " + randomId);
        p.setEmail_signUp("qatest2278+" + randomId + "@gmail.com");
        p.setPassword_signUp("78qa22!#");
        p.clickSignUpBtn();

        p.closeAuthFormBtn().shouldNotBe(visible);
    }

    @Test(priority = 7, enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign Up")
    @Description("Customer is signed in after successful sign up [Disabled -> there's currently no close btn]")
    public void closeAuthForm_abortSignUp() {
        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setName("Abort Registration");
        p.setEmail_signUp("wont@register.com");
        p.setPassword_signUp("78qa22!#");
        p.closeAuthForm();

        p.closeAuthFormBtn().shouldNotBe(visible);
    }

}