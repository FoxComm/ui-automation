package base;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeSuite;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class BaseTest extends ConciseAPI {

    public String getUrl() {
        return getWebDriver().getCurrentUrl();
    }

    @BeforeSuite
    public void browserConfig() {

        System.setProperty("webdriver.chrome.driver", "/home/cosmic/IdeaProjects/FC/chromedriver_2.21");
        Configuration.browser = "chrome";
        Configuration.timeout = 8000;

    }

}
