package base;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;

import java.util.Optional;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseTest extends ConciseAPI {

    protected String getUrl() {
        return getWebDriver().getCurrentUrl();
    }
    protected static String adminUrl = "https://admin.stage.foxcommerce.com";
    protected static String storefrontUrl = "https://stage.foxcommerce.com";

    @BeforeSuite
    public void browserConfig() {
        String driverPath = Optional.ofNullable(System.getenv("WEBDRIVER_PATH")).orElse("bin/chromedriver");
        System.setProperty("webdriver.chrome.driver", driverPath);
        Configuration.browser = "chrome";
        Configuration.timeout = 10000;
    }
}