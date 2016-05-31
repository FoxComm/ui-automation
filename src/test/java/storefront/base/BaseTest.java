package storefront.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;

public class BaseTest extends ConciseAPI {

    public static FirefoxDriver driver = new FirefoxDriver();

    @BeforeSuite
    public void setUp() {
        openPage("http://stage.foxcommerce.com/");

        // xpath of logInBtn at loginPage
        elementIsPresent(getWebDriver().
                findElement(By.xpath(".//div[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div")));
    }

    //-------------------------------------------//
    // should launch tests in chrome

//    private static String driverPath = "/home/cosmic/IdeaProjects/FC/chromedriver_2.21";
//    public static ChromeDriver driver;
//
//    @BeforeSuite
//    public void setUpEnvironment() {
//        System.setProperty("webdriver.chrome.driver", driverPath);
//        driver = new ChromeDriver();
//
//        openPage("http://stage.foxcommerce.com/");
//
//        // xpath = LoginPage.logInBtn
//        elementIsPresent(getWebDriver().
//                findElement(By.xpath(".//div[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div")));
//
//    }

    //-------------------------------------------//

    @Override
    public WebDriver getWebDriver() {
        return driver;
    }

    @AfterSuite
    public void tearDown() {
        driver.quit();
    }


}