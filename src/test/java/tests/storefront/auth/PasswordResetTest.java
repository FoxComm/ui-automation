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

import static com.codeborne.selenide.Condition.visible;

public class PasswordResetTest extends Preconditions {

    private StorefrontPage p;

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Password Reset")
    @Description("Customer can sign in with the old password if he/she decided to do not reset the password after recovery email has been sent")
    public void signIn_rememberOldPassword() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickForgotLnk();
        p.setEmail_passReset(customerEmail);
        p.clickSubmitBtn();
        p.clickBackToLogInBtn();
        p.setEmail_passReset(customerEmail);
        p.setPassword_logIn("78qa22!#");
        p.clickLogInBtn();

        p.userMenuBtn_sf().shouldBe(visible);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.MINOR)
    @Features("Storefront-TPG")
    @Stories("Auth : Password Reset")
    @Description("Abort password reset")
    public void abortPasswordReset() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickForgotLnk();
        p.clickBackToLogInLnk();

        p.logInBtn().shouldBe(visible);
        p.signUpBtn().shouldBe(visible);
    }

//    @Test(priority = 3)
//    @Severity(SeverityLevel.NORMAL)
//    @Features("Storefront-TPG")
//    @Stories("Auth : Password Reset")
//    @Description("Can't submit for password recovery an email that doesn't exist in the system")
//    public void cantRecoverUnregisteredAccount() {
//        p = openPage(storefrontUrl, StorefrontPage.class);
//        p.clickLogInLnk();
//        p.clickForgotLnk();
//        p.setEmail_passReset("this.mail@doesnt.exist");
//        p.clickSubmitBtn();
//
//        p.message_passRecovery("Oops! We don’t have a user with that email.").shouldBe(visible);
//    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp();
    }

}
