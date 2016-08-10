package base;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.Assert;
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

    //---------------------------------------- SEARCH ------------------------------------------//
    public SelenideElement searchFld() {
        return $(xpath("//input[@placeholder='filter or keyword search']"));
    }

    private List<SelenideElement> allSearchFilters() {
        return $$(By.xpath("//div[@class='fc-pilled-input__pill']/a"));
    }

    public SelenideElement today() {
        return $(xpath("//div[@class='fc-datepicker__day _current']"));
    }

    public SelenideElement beforeYesterday() {
        return $(xpath("//div[@class='fc-datepicker__day _current']/preceding-sibling::*[2]"));
    }

    public SelenideElement yesterday() {
        return $(xpath("//div[@class='fc-datepicker__day _current']/preceding-sibling::*[1]"));
    }

    public SelenideElement tomorrow() {
        return $(xpath("//div[@class='fc-datepicker__day _current']/following-sibling::*[1]"));
    }

    public SelenideElement afterTomorrow() {
        return $(xpath("//div[@class='fc-datepicker__day _current']/following-sibling::*[2]"));
    }

//----
    private SelenideElement columnLabel(int labelIndex) {
        return $(By.xpath("//table[@class='fc-table fc-multi-select-table']/thead/tr/th[" + (labelIndex + 1) + "]"));
    }

    private SelenideElement firstCriteria(String criteria) {
        return $(xpath("//ul[@class='fc-menu-items']/li/span[text()='" + criteria + "']/.."));
    }

    private SelenideElement secondCriteria(String criteria) {
        return $(xpath("//ul[@class='fc-menu-items']/li/span[text()='" + criteria + "']/.."));
    }

    private SelenideElement thirdCriteria(String criteria) {
        return $(xpath("//ul[@class='fc-menu-items']/li/span[text()='" + criteria + "']/.."));
    }

    public void hitEnter() {
        searchFld().sendKeys(Keys.ENTER);
        sleep(200);
    }
//----
    private SelenideElement noSearchResults() {
        return $(By.xpath("//div[@class='fc-content-box__empty-row']"));
    }

    public ElementsCollection searchResults() {
        return $$(xpath("//tbody[@class='fc-table-body']/a"));
    }

    // used with filters like 'Products : ID : val', where "Products : ID" - is a single (non-composite) line
    @Step("Create a search filter {0} : {1}")
    public void addFilter(String firstCriteria, String secondCriteria) {

        click( searchFld() );
        click( firstCriteria(firstCriteria) );
        searchFld().sendKeys(secondCriteria);
        hitEnter();
        waitForDataToLoad();
        $(xpath("//h1")).click();

    }

    // used with filters like 'Orders : State : Canceled'
    @Step("Create a search filter {0} : {1} : {2}")
    public void addFilter(String firstCriteria, String secondCriteria, String thirdCriteria) {

        String secondCriteriaVal = firstCriteria + " : " + secondCriteria;

        click( searchFld() );
        click( firstCriteria(firstCriteria) );
        click( secondCriteria(secondCriteriaVal) );
        searchFld().sendKeys(thirdCriteria);
        hitEnter();
        waitForDataToLoad();
        $(xpath("//h1")).click();

    }

    // used with date pickers
    @Step("Create a search filter {0} : {1} : {2}")
    public void addFilter(String firstCriteria, String secondCriteria, SelenideElement date) {

        String secondCriteriaVal = firstCriteria + " : " + secondCriteria;

        click( searchFld() );
        click( firstCriteria(firstCriteria) );
        click( secondCriteria(secondCriteriaVal) );
        click( date );
        waitForDataToLoad();
        $(xpath("//h1")).click();

    }

    //used with filters like 'Order : Total : > : $1'
    @Step("Create a search filter {0} : {1} : {2} : {3}")
    public void addFilter(String firstCriteria, String secondCriteria, String thirdCriteria, String fourthCriteria) {

        String secondCriteriaVal = firstCriteria + " : " + secondCriteria;
        String thirdCriteriaVal = secondCriteriaVal + " : " + thirdCriteria;

        click( searchFld() );
        click( firstCriteria(firstCriteria) );
        click( secondCriteria(secondCriteriaVal) );
        click( thirdCriteria(thirdCriteriaVal) );
        searchFld().sendKeys(fourthCriteria);
        hitEnter();
        waitForDataToLoad();
        $(xpath("//h1")).click();

    }

    // fast search, uses no search filters
    @Step("Search for: <{0}>")
    public void search(String searchQuery) {
        waitForDataToLoad();
        searchFld().val( searchQuery ).pressEnter();
        itemsOnList().shouldBe(visible.because("Search request returned no results."));
    }

    @Step("Open coupon with name <{0}>.")
    public void openItem(String paramVal) {
        click( findItemOnList(paramVal) );
    }

    @Step("Find coupon with name <{0}> on the list of coupons.")
    public SelenideElement findItemOnList(String paramVal) {

        List<SelenideElement> itemsList = $$(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a/td"));
        SelenideElement itemToClick = null;

        for(SelenideElement item : itemsList) {

            String listCouponName = item.text();
            if (listCouponName.equals(paramVal)) {
                itemToClick = item;
            }

        }

//        assertTrue( itemToClick!= null, "Item with requested param value isn't displayed on the list.");
        return itemToClick;

    }

    public SelenideElement itemOnList(String itemParam) {
        return $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a/td[text()='" + itemParam + "']"));
    }

    @Step
    public void removeFilter(String index) {
        $(By.xpath("//div[@class='fc-pilled-input__pill'][" + index + "]/a")).click();
    }

    @Step
    public void cleanSearchField() {
        int index = allSearchFilters().size();

        if (index > 0) {
            for (int i = 0; i < index; i++) {
                removeFilter("1");
            }
        }
    }

    // indexing starts with 1
    @Step("Sort list of orders by {0} column index")
    public void sortListBy(int columnIndex) {
        click( columnLabel(columnIndex) );
        waitForDataToLoad();
    }

    @Step
    public void assertNoSearchResults() {
        Assert.assertTrue(noSearchResults().is(visible),
                "Search query output isn't empty");
    }

}
