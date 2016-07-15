package base;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseTest extends ConciseAPI {

    protected String getUrl() {
        return getWebDriver().getCurrentUrl();
    }
    protected static String adminUrl = "http://admin.stage.foxcommerce.com";
    protected static String storefrontUrl = "http://stage.foxcommerce.com";

    @BeforeSuite
    public void browserConfig() {

        System.setProperty("webdriver.chrome.driver", "/home/cosmic/IdeaProjects/FC/chromedriver_2.21");
        Configuration.browser = "chrome";
        Configuration.timeout = 15000;

    }

}
