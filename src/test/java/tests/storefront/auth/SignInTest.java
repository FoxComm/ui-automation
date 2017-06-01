package tests.storefront.auth;

import base.StorefrontTPGBasePage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;

public class SignInTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        clearCache();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign In")
    @Description("Can sign in credentials of a registered customer")
    public void signIn_correctCreds() throws IOException {
        provideTestData("a customer signed up on storefront");
        clearCache();

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setEmail_logIn(customerEmail);
        p.setPassword_logIn("78qa22!#");
        p.clickLogInBtn();

        p.userMenuBtn_sf().should(matchText(customerName.toUpperCase()));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign In")
    @Description("Can't sign in using incorrect credentials")
    public void signIn_incorrectCreds() throws IOException {
        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setEmail_logIn("incorrect@email.com");
        p.setPassword_logIn("incorrectPassword");
        p.clickLogInBtn();

        p.errorMsg_authForm("Email or password is invalid").shouldBe(visible);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign In")
    @Description("Auth form is closed after user has successfully signed in")
    public void authFormClosed_signInSuccess() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");

        p.closeAuthFormBtn().shouldNotBe(visible);
    }

    @Test(priority = 4, enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign In")
    @Description("Sign In form can be closed without signing in")
    public void closeAuthForm_abortSignIn() {
        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setEmail_logIn("dummy@mail.com");
        p.setPassword_logIn("dummyPassword");
        p.closeAuthForm();

        p.closeAuthFormBtn().shouldNotBe(visible);
        p.logInLnk().shouldBe(visible);
    }

}