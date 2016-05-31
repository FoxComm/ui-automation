package storefront.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import java.util.List;

public class BasePage extends ConciseAPI {

    private WebDriver driver;

    @Override
    public WebDriver getWebDriver() {
        return driver;
    }


    //------------------- BASIC INTERFACE -------------------//

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div")
    public WebElement logInBtn;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div/div/ul/li")
    public WebElement logOutBtn;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div/div/span/span[3]")
    public WebElement userName;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[1]/div[3]/a/div")
    public WebElement logoBtn;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[1]/div[1]/div/div[1]")
    public WebElement showSearchField;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[1]/div[1]/div/input")
    public WebElement inputSearchField;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div/div/span/span[3]")
    public WebElement userNameBtn;

    //------------- ITEMS CATALOGUE -------------//

    @FindBy(xpath = ".//*[@id='app']/div/div/div[1]/div/div[3]/div[7]")
    public WebElement lastItem;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[3]")
    public WebElement catalog;

    @FindAll({
            @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[3]/div")
    })
    public List<WebElement> itemsCatalog;


    //------------------- ITEM PAGE -------------------//

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[3]/div[2]/div[2]/button")
    public WebElement addToCartBtn;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[3]/div[2]/div[2]/div[3]/div/div/div[2]/button")
    public WebElement increaseQtyBtn;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[3]/div[2]/div[2]/div[3]/div/div/div[1]/button")
    public WebElement decreaseQtyBtn;


    //------------------- HELPERS -------------------//

    public void clickLogo() {
        logoBtn.click();
        waitForElementToBePresent(lastItem);
    }

    public void increaseQty(int n) {
        for (int i = 0; i < n; i++ ) {
            increaseQtyBtn.click();
        }
    }

    public void navigateToItem(int numberOnPage) {
        itemsCatalog.get(numberOnPage - 1).click();
        waitForElementToBePresent(addToCartBtn);
    }

    public void logout() {
        logInBtn.click();
        logOutBtn.click();
        WebElement loginBtnSign = getWebDriver().findElement(By.xpath(".//*[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div"));
        String strLoginText = loginBtnSign.getText();
        Assert.assertEquals(strLoginText, "LOG IN");
    }

    public void assertUserName(String expectedName) {
        String uName = userName.getText();
        Assert.assertEquals(uName, expectedName);
    }

    // if List is not empty - user menu has 'LOG IN' title , means user isn't logged in | return false
    // if List is empty - 'LOG IN' title isn't found, means user is logged in | return true
    @FindAll({@FindBy(xpath = ".//*[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div/a")})
    public List<WebElement> checkLoginStatus;
    public boolean checkUserLoginStatus() {
        if (checkLoginStatus.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

}