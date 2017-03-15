package tests.storefront.auth;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;

public class PasswordResetTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
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
    @Description("Can't submit for password recovery an email that doesn't exist in the system")
    public void cantRecoveryUnregisteredAccount() {
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
