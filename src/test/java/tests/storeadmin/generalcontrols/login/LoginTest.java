package tests.storeadmin.generalcontrols.login;

import base.BaseTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;

import static com.codeborne.selenide.Condition.text;
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
        p.login("sticks and stones", "wrong@email.com", "wrongpassword");
        p.loginErrorMsg().shouldHave(text("Invalid credentials")
                .because("'Incorrect credentials' error msg isn't shown."));
    }

    @Test(priority = 2)
    public void login_logout_test() {
        p.login(adminOrg, adminEmail, adminPassword);
        p.userMenuBtn().shouldBe(visible.because("Log in has failed."));
        p.logout();
    }


    //TODO: finish the test below
//    @Test(priority = 3)
//    public void login_userHasNoRolesTest() {
//        p.login(adminOrg, customerEmail, "78qa22");
//        p.noRolesErrorMsg().shouldBe(visible);    //*[text()='User has no roles in the organization']
//    }

    @AfterTest
    public void cleanUp() {
        refresh();
    }

}
