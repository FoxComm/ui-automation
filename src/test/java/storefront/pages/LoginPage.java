package storefront.pages;

import storefront.base.BasePage;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = ".//div[@id='app']//form[2]/div[1]/input")
    public WebElement emailField;

    @FindBy(xpath = ".//div[@id='app']//form[2]/div[2]/div/input")
    public WebElement passwordField;

    @FindBy(xpath = ".//div[@id='app']//form[2]/button")
    public WebElement loginSubmitBtn;

    @FindBy(linkText = "Sign Up")
    public WebElement signupLink;

    @FindBy(xpath = ".//div[@id='app']//form[2]/div[2]/div/span/a")
    public WebElement forgotLink;

    @FindBy(xpath = ".//div[@id='app']//form[1]/button")
    public WebElement googleAuthBtn;

    @FindBy(xpath = ".//div[@id='app']/div/div[1]/a/div")
    public WebElement closeBtn;


    //--------------- GOOGLE AUTH @ GOOGLE LOGIN FORM ---------------//

    @FindBy(id = "Email")
    public WebElement gEmailField;

    @FindBy(id = "Passwd")
    public WebElement gPasswordField;

//    @FindBy(xpath = ".//*[@id='submit_approve_access']")
    @FindBy(id = "submit_approve_access")
    public WebElement acceptBtn;

    @FindBy(id = "connect-approve")
    public WebElement formToApprove;

    @FindAll({@FindBy(id = "submit_approve_access")})
    public List<WebElement> acceptBtnIsPresent;
    public void approveAccessIfNeeded() {
        waitForPageToLoad();
        if (acceptBtnIsPresent.size() > 0) {
            JavascriptExecutor js = (JavascriptExecutor)getWebDriver();
            js.executeScript("arguments[0].click();", acceptBtn);
        }
    }


    //---------------------- HELPERS ----------------------//

    public void logIn(String email, String password) {

        logInBtn.click();
        setValue(emailField, email);
        setValue(passwordField, password);
        loginSubmitBtn.click();
        waitForElementToBePresent(userName);

    }

    public void googleAuth(String email, String password) {

        logInBtn.click();
        googleAuthBtn.click();

        if (getWebDriver().getTitle().equals("Sign in - Google Accounts")) {

            setValue(gEmailField, email);
            gEmailField.submit();
            setValue(gPasswordField, password);
            gPasswordField.submit();
            approveAccessIfNeeded();

        } else {
            waitForElementToBePresent(userName);
        }

    }

}
