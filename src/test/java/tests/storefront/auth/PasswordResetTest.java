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

import static com.codeborne.selenide.Condition.visible;

public class PasswordResetTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

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
        p.setEmail(customerEmail);
        p.clickSubmitBtn();
        p.clickBackToSignInBtn();
        p.setEmail(customerEmail);
        p.setPassword("78qa22!#");
        p.clickLogInBtn();

        p.userMenuBtn_sf().shouldBe(visible);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Auth : Password Reset")
    @Description("Can't submit for password recovery an email that doesn't exist in the system")
    public void cantRecoverUnregisteredAccount() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickForgotLnk();
        p.setEmail("this.mail@doesnt.exist");
        p.clickSubmitBtn();

        p.message_passRecovery("Oops! We don’t have a user with that email.").shouldBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
