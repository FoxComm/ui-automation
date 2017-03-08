package base;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;

import java.util.Optional;

import static com.codeborne.selenide.WebDriverRunner.CHROME;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.source;

public class BaseTest extends ConciseAPI {

    public String getUrl() {
        return getWebDriver().getCurrentUrl();
    }
    protected static String apiUrl = System.getenv("API_URL") + "/api";
    protected static String adminUrl = System.getenv("API_URL") + "/admin";
    private static String storefront = System.getenv("STOREFRONT");
    public static String storefrontUrl = System.getenv("API_URL") + "/" + storefront;
    protected static String adminOrg = "tenant";
    protected static String adminEmail = "admin@admin.com";
    protected static String adminPassword;
    protected static String storefrontCategory;

    private void printEnvInfo() {
        String f = "%-25s %s\n";
        System.out.printf(f, "timeout, ms:", "<" + Configuration.timeout + ">");
        System.out.printf(f, "apiUrl:", "<" + apiUrl + ">");
        System.out.printf(f, "adminUrl:", "<" + adminUrl + ">");
        System.out.printf(f, "storefront:", "<" + storefront + ">");
        System.out.printf(f, "storefrontUrl:", "<" + storefrontUrl + ">");
        System.out.printf(f, "storefrontCategory:", "<" + storefrontCategory + ">");
        System.out.printf(f, "adminPassword:", "<" + adminPassword.charAt(0) + ">");
        System.out.println("");
    }

    private void setAdminPassword() {
        if (System.getenv("API_URL").equals("https://td-prod.foxcommerce.com")) {
            adminPassword = "Fluffybunny";
        } else {
            adminPassword = "password";
        }
    }

    private void setStorefrontCategory() {
        if (System.getenv("API_URL").equals("https://stage.foxcommerce.com") && storefront.equals("top-drawer")) {
            storefrontCategory = "MODERN";
        } else if (System.getenv("API_URL").equals("https://td-prod.foxcommerce.com") && storefront.equals("")) {
            storefrontCategory = "MODERN";
        } else if (System.getenv("API_URL").equals("https://stage.foxcommerce.com") && storefront.equals("perfect-gourmet")) {
            storefrontCategory = "APPETIZERS";
        } else if (System.getenv("API_URL").equals("https://stage-tpg.foxcommerce.com") && storefront.equals("")) {
            storefrontCategory = "APPETIZERS";
        } else if ((System.getenv("API_URL").equals("https://stage.foxcommerce.com") && storefront.equals(""))) {
            storefrontCategory = "sunglasses";
        } else {
            storefrontCategory = "";
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
        printEnvInfo();
    }

}