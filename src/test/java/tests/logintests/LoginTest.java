package tests.logintests;

import base.BaseTest;
import com.codeborne.selenide.Condition;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class LoginTest extends BaseTest {

    private LoginPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        p = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
    }

    @Test(priority = 1)
    public void incorrect_credentials_test() {
        p.login("wrong@email.com", "wrongpassword");
        p.loginErrorMsg().shouldBe(Condition.visible);
    }

    @Test(priority = 2)
    public void login_logout_test() {
        p.login("admin@admin.com", "password");
        p.click( p.userMenuBtn() );
        p.click( p.logoutBtn() );
        p.logoutSuccessMsg().shouldBe(Condition.visible);
    }

    @AfterTest
    public void cleanUp() {
        refresh();
    }

}
