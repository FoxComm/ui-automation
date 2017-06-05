package tests;

import base.StorefrontTPGBasePage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;

public class BuildTest extends Preconditions {

    private LoginPage p;
    private StorefrontTPGBasePage s;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        clearCache();
        System.out.println(">>> @BeforeMethod is run successfully");
    }

    @AfterMethod
    public void afterMethod() {
        System.out.println(">>> @AfterMethod is run successfully");
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Parametrized", "Storefront"})
    @Stories("Fox Tests")
    @Description("This test fails and marks build as failing")
    public void criticalTest_fail() throws IOException {
        p = openPage(adminUrl + "/login", LoginPage.class);
        p.login(adminOrg, adminEmail, adminPassword);

        p.loginErrorMsg().shouldBe(visible);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Storefront", "Parametrized"})
    @Stories("Fox Tests")
    @Description("This test is passing, but build is marked as failed because critical test has failed")
    public void criticalTest_pass() throws IOException {
        p = openPage(adminUrl + "/login", LoginPage.class);

        p.passwordFld().shouldBe(visible);
    }

    @Test(priority = 2, dataProvider = "parametrizedTest")
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Storefront", "Parametrized"})
    @Stories("Fox Tests")
    @Description("This test is passing, but build is marked as failed because critical test has failed")
    public void parametrizedTest(String org, String email, String password) throws IOException {
        p = openPage(adminUrl + "/login", LoginPage.class);
        p.login(org, email, password);

        p.loginErrorMsg().shouldBe(visible);
    }

//    @Test(priority = 3)
//    @Severity(SeverityLevel.MINOR)
//    @Features({"Ashes", "Parametrized"})
//    @Stories("Fox Tests")
//    @Description("This test is passing, but build is marked as failed because critical test has failed")
//    public void minorTest_pass() throws IOException {
//        p = openPage(adminUrl + "/login", LoginPage.class);
//
//        p.passwordFld().shouldBe(visible);
//    }
//
//    @Test(priority = 4)
//    @Severity(SeverityLevel.CRITICAL)
//    @Features({"Ashes", "Parametrized"})
//    @Stories("Fox Tests")
//    public void criticalTest_broken() {
//        p = openPage(adminUrl + "/login", LoginPage.class);
//        p.login(adminOrg, adminEmail, adminPassword);
//
//        shouldBeVisible(p.loginErrorMsg(), "");
//    }

//    @Test(priority = 1)
//    @Severity(SeverityLevel.BLOCKER)
//    @Features({"Storefront-TPG", "Configs"})
//    @Stories({"WD Behavior with SF Tests", "Auth"})
//    @Description("Can sign up using email that is unique to the system")
//    public void signUp_correctCredentials() {
//        String randomId = generateRandomID();
//
//        s = openPage(storefrontUrl, StorefrontTPGBasePage.class);
//        s.clickLogInLnk();
//        s.setName("Customer " + randomId);
//        s.setEmail_signUp("qatest2278+" + randomId + "@gmail.com");
//        s.setPassword_signUp("78qa22!#");
//        s.clickSignUpBtn();
//
//        s.userMenuBtn_sf().should(matchText(randomId.toUpperCase()));
//    }
//
//    @Test(priority = 2)
//    @Severity(SeverityLevel.BLOCKER)
//    @Features({"Storefront-TPG", "Configs"})
//    @Stories({"WD Behavior with SF Tests", "Auth"})
//    @Description("Can sign in")
//    public void signIn() throws IOException {
//        provideTestData("a customer signed up on storefront");
//
//        s = openPage(storefrontUrl, StorefrontTPGBasePage.class);
//        s.clickLogInLnk();
//        s.logIn(customerEmail, "78qa22!#");
//
//        s.userMenuBtn_sf().shouldBe(visible);
//    }
//
//    @Test(priority = 3)
//    @Severity(SeverityLevel.MINOR)
//    @Features("Configs")
//    @Stories("WD Behavior with SF Tests")
//    @Description("Check if WD will start just one more")
//    public void startWebDriver() {
//        s = openPage(storefrontUrl, StorefrontTPGBasePage.class);
//        s.logInLnk().shouldBe(visible);
//    }
//
//    @Test(priority = 4)
//    @Features("Config")
//    @Stories("Cache Clearing & Ashes Auth")
//    @Description("Auth as admin and expect auth to be gone after this test")
//    public void ashesSignIn() {
//        p = openPage(adminUrl, LoginPage.class);
//        p.login(adminOrg, adminEmail, adminPassword);
//        p.userMenuBtn().shouldBe(visible);
//    }
//
//    @Test(priority = 5)
//    @Features("Config")
//    @Stories("Cache Clearing & Ashes Auth")
//    @Description("Reresh the page and make sure auth is cleared by @AfterMethod")
//    public void ashesSignInAgain() {
//        refresh();
//        p.userMenuBtn().shouldNotBe(visible);
//    }
//
//    @Test(priority = 6)
//    @Features("Config")
//    @Stories("Cache Clearing & Ashes Auth")
//    @Description("Auth as admin and expect auth to be gone after this test")
//    public void ashesSignIn2() {
//        p = openPage(adminUrl, LoginPage.class);
//        p.login(adminOrg, adminEmail, adminPassword);
//        p.userMenuBtn().shouldBe(visible);
//    }
//
//    @Test(priority = 7)
//    @Features("Config")
//    @Stories("Cache Clearing & Ashes Auth")
//    @Description("Open /login and make sure auth is cleared by @AfterMethod")
//    public void ashesSignInAgain2() {
//        p = openPage(adminUrl, LoginPage.class);
//        p.userMenuBtn().shouldNotBe(visible);
//    }

}
