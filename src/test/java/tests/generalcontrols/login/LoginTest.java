package tests.generalcontrols.login;

import base.BaseTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.refresh;

public class LoginTest extends BaseTest {

    private LoginPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        p = openPage(adminUrl + "/login", LoginPage.class);
    }

    @Test(priority = 1)
    public void incorrect_credentials_test() {
        p.login("wrong@email.com", "wrongpassword");
        p.loginErrorMsg().shouldBe(visible
                .because("'Incorrect credentials' error msg isn't shown."));
    }

    @Test(priority = 2)
    public void login_logout_test() {
        p.login("admin@admin.com", "password");
        p.userMenuBtn().shouldBe(visible.because("Log in has failed."));
        p.logout();
    }

    @AfterTest
    public void cleanUp() {
        refresh();
    }

}
