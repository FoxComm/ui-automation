package tests.storefront.auth;

import org.testng.annotations.AfterMethod;
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

public class SignInTest extends Preconditions {

    private StorefrontPage p;

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign In")
    @Description("Can sign in credentials of a registered customer")
    public void signIn_correctCreds() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.setEmail(customerEmail);
        p.setPassword("78qa22!#");
        p.clickLogInBtn();

        p.userMenuBtn_sf().should(matchText(customerName.toUpperCase()));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign In")
    @Description("Can't sign in using incorrect credentials")
    public void signIn_incorrectCreds() throws IOException {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.setEmail("incorrect@email.com");
        p.setPassword("incorrectPassword");
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

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");

        p.closeAuthFormBtn().shouldNotBe(visible);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Sign In")
    @Description("Sign In form can be closed without signing in")
    public void closeAuthForm_abortSignIn() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.setEmail("dummy@mail.com");
        p.setPassword("dummyPassword");
        p.closeAuthForm();

        p.closeAuthFormBtn().shouldNotBe(visible);
        p.logInLnk().shouldBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp();
    }

}