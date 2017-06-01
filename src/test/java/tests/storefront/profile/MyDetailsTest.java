package tests.storefront.profile;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.StorefrontTPGBasePage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.matchText;
import static com.codeborne.selenide.Condition.visible;

public class MyDetailsTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Profile Page")
    @Description("Can access profile page from user menu")
    public void profileIsAccessible() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.clickLogInLnk();
        p.setEmail_logIn(customerEmail);
        p.setPassword_logIn("78qa22!#");
        p.clickLogInBtn();
        shouldMatchText(p.userMenuBtn_sf(), customerName.toUpperCase(), "Failed to edit customer name");
        p.openProfile();

        p.changePasswordBtn().shouldBe(visible);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Profile Page")
    @Description("Can edit first & last name")
    public void editCustomerName() throws IOException {
        provideTestData("a customer signed up on storefront");
        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");

        p.openProfile();
        p.clickEditLnk("Name");
        p.setNameFld("Edited Name");
        p.clickSaveBtn();

        p.userMenuBtn_sf().should(matchText("EDITED NAME"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Profile Page")
    @Description("Can change email to another one that is unique to the system")
    public void editCustomerEmail_unique() throws IOException {
        String newEmail = "qatest2278+" + generateRandomID() + "@gmail.com";
        provideTestData("a customer signed up on storefront");
        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");

        p.openProfile();
        p.clickEditLnk("Email");
        p.setEmailFld(newEmail);
        p.clickSaveBtn();
        shouldHaveText(p.userEmail(), newEmail, "Failed to edit customer's \"Email\"");
        p.logOut();
        p.logIn(newEmail, "78qa22!#");

        p.userMenuBtn_sf().should(matchText(customerName.toUpperCase()));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Profile Page")
    @Description("Can't set email to an already taken one")
    public void editCustomerEmail_taken() throws IOException {
        provideTestData("two customers signed up on storefront");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openProfile();
        p.clickEditLnk("Email");
        p.setEmailFld(takenEmail);
        p.clickSaveBtn();

        p.errorMsg("The email address you entered is already in use").shouldBe(visible);
    }

}
