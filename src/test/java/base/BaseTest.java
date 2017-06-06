package base;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseTest extends ConciseAPI {

    public String getUrl() {
        getWebDriver().manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        return getWebDriver().getCurrentUrl();
    }

    protected static String apiUrl = System.getenv("API_URL__TESTS");
    protected static String adminUrl = System.getenv("ASHES_URL");
    public static String storefrontUrl = System.getenv("STOREFRONT_URL");
    private static String storefront = System.getenv("STOREFRONT");
    protected static String adminOrg = "tenant";
    protected static String adminEmail = "admin@admin.com";
    protected static String adminPassword;
    protected static String storefrontCategory;

    private void printEnvInfo() {
        String f = "%-25s %s\n";
        System.out.printf(f, "timeout, ms:", "<" + Configuration.timeout + ">");
        System.out.printf(f, "Test Suite:", "<" + System.getenv("SUITE") + ">");
        System.out.printf(f, "apiUrl:", "<" + apiUrl + ">");
        System.out.printf(f, "adminUrl:", "<" + adminUrl + ">");
        System.out.printf(f, "storefront:", "<" + storefront + ">");
        System.out.printf(f, "storefrontUrl:", "<" + storefrontUrl + ">");
        System.out.printf(f, "storefrontCategory:", "<" + storefrontCategory + ">");
        System.out.printf(f, "adminPassword:", "<" + adminPassword.charAt(0) + ">");
        System.out.printf(f, "webdriver:", "<" + getWebDriver().toString() + ">");
        System.out.println("");
    }

    private void setAdminPassword() {
        if (System.getenv("ENV").equals("stage-td")) {
            adminPassword = "Fluffybunny";
        } else {
            adminPassword = "password";
        }
    }

    private static void setStorefrontCategory() {
        switch (storefront) {
            case "perfect-gourmet":
                storefrontCategory = "APPETIZERS";
                break;
            case "top-drawer":
                storefrontCategory = "MODERN";
                break;
            case "firebrand":
                storefrontCategory = "sunglasses";
                break;
        }
    }

    @BeforeSuite
    public void browserConfig() {
        String driverPath = Optional.ofNullable(System.getenv("WEBDRIVER_PATH")).orElse("bin/chromedriver");
        System.setProperty("webdriver.chrome.driver", driverPath);
        Configuration.browser = System.getenv("BROWSER");
        Configuration.timeout = 8000;
        setAdminPassword();
        setStorefrontCategory();
        printEnvInfo();
    }

    @AfterMethod
    public void checkRamUsage() {
        printRamUsage();
    }

}