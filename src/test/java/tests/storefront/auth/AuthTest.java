package tests.storefront.auth;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import pages.StorefrontPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class AuthTest extends DataProvider {

    private StorefrontPage p;

    @Test(priority = 1)
    @Description("Can sign up a new customer")
    public void signUp_correctCredentials() {
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Test Buddy " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.userName().should(matchText(randomId.toUpperCase()));
    }

    @Test(priority = 2)
    @Description("Name shouldn't require to be unique")
    public void signUp_nameIsNotUnique() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName(customerName);
        p.setEmail("qatest2278+" + generateRandomID() + "@gmail.com");
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.userName().should(matchText(customerName.toUpperCase()));
    }

    @Test(priority = 3)
    @Description("Can't sign up with existing email")
    public void signUp_incorrectCredentials_email() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Test Buddy " + generateRandomID());
        p.setEmail(customerEmail);
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.errorMessage("The email address you entered is already in use").shouldBe(visible);
    }

    @Test(priority = 4)
    @Description("Can log in with an already registered customer")
    public void logIn() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.setEmail(customerEmail);
        p.setPassword("78qa22!#");
        p.clickLogInBtn();

        p.userName().should(matchText(customerName.toUpperCase()));
    }

    @Test(priority = 5)
    @Description("Can log out")
    public void logOut() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.setEmail(customerEmail);
        p.setPassword("78qa22!#");
        p.clickLogInBtn();
        shouldMatchText(p.userName(), customerName.toUpperCase(), "Failed to log out");
        p.logOut();

        p.logInLnk().shouldBe(visible);
    }

    @Test(priority = 6)
    @Description("Auth form is closed after user has successfully signed in")
    public void authFormIsClosed_LogIn() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");

        p.logInBtn().shouldNotBe(visible);
    }

    @Test(priority = 7)
    @Description("Auth form is closed after user has successfully signed in")
    public void authFormIsClosed_SignUp() {
        String randomId = generateRandomID();

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.clickSignUpLnk();
        p.setName("Test Buddy " + randomId);
        p.setEmail("qatest2278+" + randomId + "@gmail.com");
        p.setPassword("78qa22!#");
        p.clickSignUpBtn();

        p.userName().should(matchText(randomId.toUpperCase()));
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        click(p.logo());
        try {
            getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebElement logInLnk = getWebDriver().findElement(By.xpath("//a[contains(@class, 'login-link')]"));
        } catch (NoSuchElementException ignored) {
            p.userName().click();
            p.menuLink("LOG OUT").click();
            p.logInLnk().shouldBe(visible);
        }
    }

}
