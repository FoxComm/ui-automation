package base;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;

public class BasePage extends ConciseAPI {

    //---------------------------------- GENERAL CONTROLS -----------------------------------//
    public SelenideElement userMenuBtn() {
        return $(By.xpath("//div[@class='_header_header__name']"));
    }

    public SelenideElement logoutBtn() {
        return $(By.xpath("//a[text()='Log out']"));
    }

    public SelenideElement userSettingsBtn() {
        return $(By.xpath("//a[text()='Settings']"));
    }

    //----------------------------------- NAVIGATION MENU ------------------------------------//
    public SelenideElement productsNavMenu() {
        return $(By.xpath("//span[text()='Products']"));
    }

    public SelenideElement skusNavMenu() {
        return $(By.xpath("//span[text()='SKUs']"));
    }

    public SelenideElement couponsNavMenu() {
        return $(By.xpath("//span[text()='Coupons']"));
    }

    public SelenideElement promotionsNavMenu() {
        return $(By.xpath("//span[text()='Promotions']/.."));
    }

    public SelenideElement gcNavMenu() {
        return $(By.xpath("//span[text()='Gift Cards']/.."));
    }

    //------------------------------------ LOGIN SCREEN --------------------------------------//
    public SelenideElement emailField() {
        return $("#form-field-1");
    }

    public SelenideElement passwordField() {
        return $("#form-field-2");
    }

    public SelenideElement googleAuthButton() {
        return $(By.xpath("//button[@class='fc-btn fc-login__google-btn']"));
    }

    public SelenideElement logoutSuccessMsg() {
        return $(By.xpath("//div[@class='fc-alert is-alert-success']"));
    }

    public SelenideElement loginErrorMsg() {
        return $(By.xpath("//div[@class='fc-alert is-alert-error']"));
    }

    @Step("Log in as {0} / {1}")
    public void login(String email, String password) {
        emailField().val(email);
        passwordField().val(password).submit();
        sleep(3000);
    }

    @Step("Log out.")
    public void logout() {
        click( userMenuBtn() );
        click( logoutBtn() );
        logoutSuccessMsg().shouldBe(Condition.visible);
    }

    //---------------------------- GENERAL FORM SPECIFIC----------------------------//
    private SelenideElement saveBtn() {
        return $(By.xpath("//span[text()='Save']/.."));
    }

    @Step("Save all changes.")
    public void clickSave() {
        click( saveBtn() );
        saveBtn().shouldBe(enabled);
    }
//----
    public SelenideElement searchFld() {
        return $(By.xpath("//input[@placeholder='filter or keyword search']"));
    }

    @Step("Search for: <{0}>")
    public void search(String searchQuery) {
        waitForDataToLoad();
        searchFld().val( searchQuery ).pressEnter();
        waitForDataToLoad();
    }
//----
    public SelenideElement addTagBtn() {
        return $(By.xpath("//div[text()='Tags']/following-sibling::*"));
    }

    private SelenideElement tagFld() {
        return $(By.xpath("//input[@placeholder='Separate tags with a comma']"));
    }

    public SelenideElement removeTagBtn(String index) {
        // define only btn on the first tag in line
        return $(By.xpath("//div[@class='_tags_tags__tags']/div[" + index + "]/button"));
    }

    public SelenideElement tag(String tagVal) {
        return $(By.xpath("//div[@class='_tags_tags__tags']//div[text()='" + tagVal + "']"));
    }

    public int getTagsAmount() {
        List<SelenideElement> listOfTags = $$(By.xpath("//div[@class='_tags_tags__tags']/div"));
        return listOfTags.size();
    }

    @Step("Add tag <{0}>")
    public void addTag(String tagVal) {

        click( addTagBtn() );
        setFieldVal( tagFld(), tagVal );
        tagFld().pressEnter();
        tagFld().shouldNotBe(visible);

    }
    //----------------------------------------------------------------------------------------//



}
