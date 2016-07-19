package base;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class BasePage extends ConciseAPI {

    public SelenideElement userMenuBtn() {
        return $(By.xpath("//div[@class='_header_header__name']"));
    }

    public SelenideElement logoutBtn() {
        return $(By.xpath("//a[text()='Log out']"));
    }

    public SelenideElement settingsBtn() {
        return $(By.xpath("//a[text()='Settings']"));
    }

    //---------------------------- N A V I G A T I O N    M E N U ----------------------------//
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
    //----------------------------------------------------------------------------------------//

    //---------------------------- GENERAL CONTROLS, FORM SPECIFIC----------------------------//
    private SelenideElement saveBtn() {
        return $(By.xpath("//span[text()='Save']/.."));
    }

    @Step("Save all changes.")
    public void clickSave() {
        click( saveBtn() );
        saveBtn().shouldBe(enabled);
    }
//----
    private SelenideElement searchFld() {
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
