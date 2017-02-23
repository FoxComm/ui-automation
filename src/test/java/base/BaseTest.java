package base;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;

import java.util.Optional;

import static com.codeborne.selenide.WebDriverRunner.CHROME;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseTest extends ConciseAPI {

    protected String getUrl() {
        return getWebDriver().getCurrentUrl();
    }
    protected static String apiUrl = System.getenv("API_URL") + "/api";
    protected static String adminUrl = System.getenv("API_URL") + "/admin";
    protected static String storefrontUrl = System.getenv("API_URL");
    protected static String adminOrg = "tenant";
    protected static String adminEmail = "admin@admin.com";
    protected static String adminPassword;
    protected static String storefrontCategory;
//
    private void setAdminPassword() {
        if (System.getenv("API_URL").equals("https://td-prod.foxcommerce.com")) {
            adminPassword = "Fluffybunny";
        } else {
            adminPassword = "password";
        }
    }

    private void setStorefrontCategory() {
        if (System.getenv("API_URL").equals("https://td-prod.foxcommerce.com")) {
            storefrontCategory = "MODERN";
        } else if (System.getenv("API_URL").equals("https://stage-tpg.foxcommerce.com")) {
            storefrontCategory = "APPETIZERS";
        } else {
            storefrontCategory = "test";
        }
    }

    @BeforeSuite
    public void browserConfig() {
        String driverPath = Optional.ofNullable(System.getenv("WEBDRIVER_PATH")).orElse("bin/chromedriver");
        System.setProperty("webdriver.chrome.driver", driverPath);
        Configuration.browser = System.getenv("BROWSER");
        Configuration.timeout = 6000;
        setAdminPassword();
        setStorefrontCategory();
    }

}