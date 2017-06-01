package tests;

import org.openqa.selenium.By;
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
import static com.codeborne.selenide.Selenide.$;

public class BuildTest extends Preconditions {

    private LoginPage p;

    @BeforeMethod
    public void before() {
        restartBrowser();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Storefront"})
    @Stories("Fox Tests")
    @Description("This test fails and marks build as failing")
    public void criticalTest_fail() throws IOException {
        p = openPage(adminUrl + "/login", LoginPage.class);
        p.login(adminOrg, adminEmail, adminPassword);

        p.loginErrorMsg().shouldBe(visible);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features({"Ashes", "Storefront"})
    @Stories("Fox Tests")
    @Description("This test is passing, but build is marked as failed because critical test has failed")
    public void criticalTest_pass() throws IOException {
        p = openPage(adminUrl + "/login", LoginPage.class);

        $(By.xpath("//button[@type='submit']")).shouldBe(visible);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("Fox Tests")
    @Description("This test is passing, but build is marked as failed because critical test has failed")
    public void minorTest_pass() throws IOException {
        p = openPage(adminUrl + "/login", LoginPage.class);

        $(By.xpath("//input[@type='password']")).shouldBe(visible);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Fox Tests")
    public void criticalTest_broken() {
        p = openPage(adminUrl + "/login", LoginPage.class);
        p.login(adminOrg, adminEmail, adminPassword);

        shouldBeVisible(p.loginErrorMsg(), "");
        p.loginErrorMsg().shouldBe(visible);
    }

}