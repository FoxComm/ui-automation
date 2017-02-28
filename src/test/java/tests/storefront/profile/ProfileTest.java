package tests.storefront.profile;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import pages.StorefrontPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class ProfileTest extends DataProvider {

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        if (!Objects.equals(p.cartQty().text(), "0")) {
            p.openCart();
            p.cleanCart();
            p.closeCart();
        }
        try {
            getWebDriver().manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            WebElement logInLnk = getWebDriver().findElement(By.xpath("//a[contains(@class, 'login-link')]"));
        } catch (NoSuchElementException ignored) {
            p.userName().click();
            p.menuLink("LOG OUT").click();
            p.logInLnk().shouldBe(visible);
        }
    }

    private StorefrontPage p;

    @Test(priority = 1)
    @Description("Can access profile from user menu")
    public void profileIsAccessible() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickLogInLnk();
        p.setEmail(customerEmail);
        p.setPassword("78qa22!#");
        p.clickLogInBtn();
        shouldMatchText(p.userName(), customerName.toUpperCase(), "Failed to edit customer name");
        p.openProfile();

        p.changePasswordBtn().shouldBe(visible);
    }

    @Test(priority = 2)
    @Description("Can edit first & last name")
    public void editCustomerName() throws IOException {
        provideTestData("a customer signed up on storefront");
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");

        p.openProfile();
        p.clickEditLnk("Name");
        p.setNameFld("Edited Name");
        p.clickSaveBtn();

        p.userName().should(matchText("EDITED NAME"));
    }

    @Test(priority = 3)
    @Description("Can edit customer email a unique to the system")
    public void editCustomerEmail_unique() throws IOException {
        String newEmail = "qatest2278+" + generateRandomID() + "@gmail.com";
        provideTestData("a customer signed up on storefront");
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");

        p.openProfile();
        p.clickEditLnk("Email");
        p.setEmailFld(newEmail);
        p.clickSaveBtn();
        shouldHaveText(p.userEmail(), newEmail, "Failed to edit customer's \"Email\"");
        p.logOut();
        p.logIn(newEmail, "78qa22!#");

        p.userName().should(matchText(customerName.toUpperCase()));
    }

    @Test(priority = 4)
    @Description("Can't set email to an already taken one")
    public void editCustomerEmail_taken() throws IOException {
        provideTestData("two customers signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openProfile();
        p.clickEditLnk("Email");
        p.setEmailFld(takenEmail);

        p.errorMsg("The email address you entered is already in use").shouldBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        click(p.logo());
        if (!Objects.equals(p.cartQty().text(), "0")) {
            p.openCart();
            p.cleanCart();
            p.closeCart();
        }
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