package storefront.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignupPage extends LoginPage {

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = ".//*[@id='app']/div/div[1]/div/div/form/div[1]/input")
    public WebElement fullNameField;

    @FindBy(xpath = ".//*[@id='app']/div/div[1]/div/div/form/div[2]/input")
    public WebElement emailField;

    @FindBy(xpath = ".//*[@id='app']/div/div[1]/div/div/form/div[3]/div/input")
    public WebElement passwordField;


    //--------------------- HELPERS ---------------------//

    public void signUp_regularAcc(String fullName, String email, String password) {
        setValue(fullNameField, "Mini Moe");
        setValue(emailField, "qatest2278@gmail.com");
        setValue(passwordField, "78qa22!#");
    }

}
