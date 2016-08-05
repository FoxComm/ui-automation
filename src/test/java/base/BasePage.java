package base;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class BasePage extends ConciseAPI {

    //---------------------------------- GENERAL CONTROLS -----------------------------------//
    public SelenideElement userMenuBtn() {
        return $(xpath("//div[@class='_header_header__name']"));
    }

    public SelenideElement logoutBtn() {
        return $(xpath("//a[text()='Log out']"));
    }

    public SelenideElement userSettingsBtn() {
        return $(xpath("//a[text()='Settings']"));
    }

    public SelenideElement closeModalWindowBtn() {
        return $(xpath("//a[@class='fc-modal-close']"));
    }

    public SelenideElement successIcon() {
        return $(xpath("//i[@class='icon-success']"));
    }

    //----------------------------------- NAVIGATION MENU ------------------------------------//
    public SelenideElement productsNavMenu() {
        return $(xpath("//span[text()='Products']"));
    }

    public SelenideElement skusNavMenu() {
        return $(xpath("//span[text()='SKUs']"));
    }

    public SelenideElement couponsNavMenu() {
        return $(xpath("//span[text()='Coupons']"));
    }

    public SelenideElement promotionsNavMenu() {
        return $(xpath("//span[text()='Promotions']/.."));
    }

    public SelenideElement gcNavMenu() {
        return $(xpath("//span[text()='Gift Cards']/.."));
    }

    //------------------------------------ LOGIN SCREEN --------------------------------------//
    public SelenideElement emailField() {
        return $(xpath("//label[text()='Email']/following-sibling::*"));
    }
    public SelenideElement passwordField() {
        return $(xpath("//div[text()='Password']/../../following-sibling::*"));
    }
    public SelenideElement googleAuthButton() {
        return $(xpath("//button[@class='fc-btn fc-login__google-btn']"));
    }

    public SelenideElement logoutSuccessMsg() {
        return $(xpath("//div[@class='fc-alert is-alert-success']"));
    }

    public SelenideElement loginErrorMsg() {
        return $(xpath("//div[@class='fc-alert is-alert-error']"));
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
        return $(xpath("//span[text()='Save']/.."));
    }

    @Step("Save all changes.")
    public void clickSave() {
        click( saveBtn() );
        saveBtn().shouldBe(enabled);
    }
//----
    public SelenideElement searchFld() {
        return $(xpath("//input[@placeholder='filter or keyword search']"));
    }

    @Step("Search for: <{0}>")
    public void search(String searchQuery) {
        waitForDataToLoad();
        searchFld().val( searchQuery ).pressEnter();
        itemsOnList().shouldBe(visible.because("Search request returned no results."));
    }
//----
    public SelenideElement addTagBtn() {
        return $(xpath("//div[text()='Tags']/following-sibling::*"));
    }

    private SelenideElement tagFld() {
        return $(xpath("//input[@placeholder='Separate tags with a comma']"));
    }

    public SelenideElement removeTagBtn(String index) {
        // define only btn on the first tag in line
        return $(xpath("//div[@class='_tags_tags__tags']/div[" + index + "]/button"));
    }

    public SelenideElement tag(String tagVal) {
        return $(xpath("//div[@class='_tags_tags__tags']//div[text()='" + tagVal + "']"));
    }

    public int getTagsAmount() {
        List<SelenideElement> listOfTags = $$(xpath("//div[@class='_tags_tags__tags']/div"));
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
