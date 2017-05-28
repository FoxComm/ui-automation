package tests.storefront.auth;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
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

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign Up")
    @Description("Can sign up using email that is unique to the system")
    public void signUp_correctCredentials() {
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Customer " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.userMenuBtn_sf().should(matchText(randomId.toUpperCase()));
    }

    @Test(priority = 2)
    @Description("Name doesn't need to be unique on sign up")
    public void signUp_nameIsNotUnique() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName(customerName);
        p.setEmail("qatest2278+" + generateRandomID() + "@gmail.com");
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.userMenuBtn_sf().should(matchText(customerName.toUpperCase()));
    }

    @Test(priority = 3)
    @Description("Can't sign up using taken email")
    public void signUp_incorrectCredentials_email() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Customer " + generateRandomID());
        p.setEmail(customerEmail);
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.errorMessage("The email address you entered is already in use").shouldBe(visible);
    }

    @Test(priority = 4)
    @Description("Auth form is closed after user has successfully signed in")
    public void authFormIsClosed_SignUp() {
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Customer " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.userMenuBtn_sf().should(matchText(randomId.toUpperCase()));
    }

    @Test(priority = 5)
    @Description("Can sign up with an email used for guest checkout earlier")
    public void canSignUp_guestCheckoutEmail() {
        //TODO: add method to Preconditions that does guest checkout
    }

    @Test(priority = 6)
    @Description("Customer is signed in after successful sign up")
    public void authFormClosed_signUpSuccess() {
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Customer " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.closeAuthFormBtn().shouldNotBe(visible);
    }

    @Test(priority = 7)
    @Description("Customer is signed in after successful sign up")
    public void closeAuthForm_abortSignUp() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Abort Registration");
        p.setEmail("wont@register.com");
        p.setPassword("78qa22!#");
        p.closeAuthForm();

        p.closeAuthFormBtn().shouldNotBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}