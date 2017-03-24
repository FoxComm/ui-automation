package tests.storeadmin.generalcontrols.auth;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.OrdersPage;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.refresh;
import static org.testng.Assert.assertEquals;

public class AuthTest extends Preconditions {

    private LoginPage lp;
    private StorefrontPage sfp;
    private OrdersPage op;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        lp = openPage(adminUrl + "/login", LoginPage.class);
    }

    @Test(priority = 1)
    public void incorrect_credentials_test() {
        lp.login("sticks and stones", "wrong@email.com", "wrongpassword");
        lp.loginErrorMsg().shouldHave(text("Invalid credentials")
                .because("'Incorrect credentials' error msg isn't shown."));
    }

    @Test(priority = 2)
    public void login_logout_test() {
        lp.login(adminOrg, adminEmail, adminPassword);
        lp.userMenuBtn().shouldBe(visible.because("Log in has failed."));
        lp.logout();
    }


    //TODO: finish this test
//    @Test(priority = 3)
//    public void login_userHasNoRolesTest() {
//        p.login(adminOrg, customerEmail, "78qa22");
//        p.noRolesErrorMsg().shouldBe(visible);    //*[text()='User has no roles in the organization']
//    }

    @Test(priority = 99)
    @Description("Can't view pages under /admin using customer auth")
    public void customerCantAccessAdmin() throws IOException {
        provideTestData("a customer signed up on storefront");

        sfp = openPage(storefrontUrl, StorefrontPage.class);
        sfp.logIn(customerEmail, "78qa22!#");
        op = openPage(adminUrl + "/orders", OrdersPage.class);

        op.userMenuBtn().shouldNotBe(visible);
        op.organizationFld().shouldBe(visible);
        assertEquals(getUrl(), adminUrl + "/login");
    }

    @AfterTest
    public void cleanUp() {
        refresh();
    }

    @AfterClass(alwaysRun = true)
    public void backToAshes() {
        sfp = openPage(storefrontUrl, StorefrontPage.class);
        sfp.cleanUp_afterMethod();
        lp = openPage(adminUrl + "/login", LoginPage.class);
    }

}