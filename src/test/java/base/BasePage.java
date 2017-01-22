package base;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class BasePage extends ConciseAPI {

    //---------------------------------- GENERAL CONTROLS -----------------------------------//

    public SelenideElement userMenuBtn() {
        return $(xpath("//div[contains(@class, 'user')]/div[3]"));
    }

    public SelenideElement logoutBtn() {
        return $(xpath("//a[text()='Log out']"));
    }

    public SelenideElement userSettingsBtn() {
        return $(xpath("//a[text()='Settings']"));
    }

    // xpath might fail tests. old one: //a[@class='fc-modal-close']
    public SelenideElement closeModalWindowBtn() {
        return $(xpath("//a[@class='fc-modal-close']/i"));
    }

    @Step("Close modal window")
    public void closeModalWindow() {
        click(closeModalWindowBtn());
    }

    public SelenideElement successIcon() {
        return $(xpath("//i[@class='icon-success']"));
    }

    public SelenideElement yesBtn() {
        return $(By.xpath("//span[contains(text(), 'Yes')]/.."));
    }

    @Step("Click \"Yes\" btn")
    public void clickYes() {
        click(yesBtn());
    }

    public SelenideElement cancelBtn() {
        return $(xpath("//a[text()='Cancel']"));
    }

    @Step("Click \"Cancel\"")
    public void clickCancel() {
        click(cancelBtn());
    }

    //----------------------------------- NAVIGATION MENU ------------------------------------//

    public SelenideElement sideMenu(String sectionName) {
        return $(xpath("//span[text()='" + sectionName + "']"));
    }

    private SelenideElement navContainer(String sectionTitle) {
        return $(xpath("//span[text()='" + sectionTitle + "']/../following-sibling::*/i"));
    }

    private SelenideElement category(String sectionTitle, String categoryTitle) {
        return $(xpath("//span[text()='" + sectionTitle + "']/../../following-sibling::*/a[text()='" + categoryTitle + "']"));
    }

    @Step("Navigate to <{0}> -> <{1}>")
    public void navigateTo(String sectionTitle, String categoryTitle) {
        click(navContainer(sectionTitle));
        click(category(sectionTitle, categoryTitle));
        waitForDataToLoad();
    }

    //------------------------------------ LOGIN SCREEN --------------------------------------//
    public SelenideElement organizationFld() {
        return $(xpath("//label[text()='Organization']/following-sibling::*[@type='text']"));
    }

    public SelenideElement emailFld() {
        return $(xpath("//label[text()='Email']/following-sibling::*[@type='text']"));
    }

    public SelenideElement passwordFld() {
        return $(xpath("//input[@type='password']"));
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

    @Step("Log in as <{0}> / <{1}>")
    public void login(String organization, String email, String password) {
        setFieldVal(organizationFld(), organization);
        setFieldVal(emailFld(), email);
        setFieldValWithSubmit(passwordFld(), password);
    }

    @Step("Log out")
    public void logout() {
        click(userMenuBtn());
        click(logoutBtn());
        logoutSuccessMsg().shouldBe(visible
                .because("Failed to log out."));
    }

    //---------------------------- GENERAL FORM SPECIFIC ----------------------------//
    //---------------------------------- ELEMENTS -----------------------------------//
    protected SelenideElement saveBtn() {
        return $(xpath("//span[text()='Save']/.."));
    }

    /**
     * Click "Save" btn without waiting for it to get re-enabled
     */
    @Step("Click \"Save\"")
    public void clickSave() {
        click(saveBtn());
        sleep(1000);
    }

    /**
     * Click "Save" btn and wait for it to get re-enabled
     */
    @Step("Click \"Save\" and wait until it's re-enabled")
    public void clickSave_wait() {
        jsClick(saveBtn());
        shouldBeEnabled(saveBtn(), "\"Save\" btn doesn't get re-enabled");
    }

    private SelenideElement saveOptionsDd() {
        return $(xpath("//div[@class='_common_button_with_menu__controls']/button[2]"));
    }

    @Step("Open dd next to \"Save\" btn and select <{0}>")
    public void saveWithOption(String optionVal) {
        setDdVal(saveOptionsDd(), optionVal);
    }

    /**
     * A part of error message works fine for this.
     */
    public SelenideElement errorMsg(String text) {
        return $(xpath("//*[contains(text(), '" + text + "')]"));
    }


//----
    public SelenideElement addTagBtn() {
        return $(xpath("//button[@class='_tags_tags__icon']"));
    }

    private SelenideElement tagFld() {
        return $(xpath("//input[@placeholder='Separate tags with a comma']"));
    }

    protected SelenideElement removeTagBtn(String index) {
        // define only btn on the first tag in line
        return $(xpath("//div[@class='_tags_tags__tags']/div[1]/button"));
    }

    public SelenideElement tag(String tagVal) {
        return $(xpath("//div[@class='_tags_tags__tags']/div/div[text()='" + tagVal + "']"));
    }

    public ElementsCollection allTags() {
        return $$(xpath("//div[@class='_rounded_pill_rounded_pill__text']"));
    }

    //---------------------------------- HELPERS -----------------------------------//

    @Step("Add tag <{0}>")
    public void addTag(String tagVal) {
        click(addTagBtn());
        setFieldVal(tagFld(), tagVal);
        tagFld().pressEnter();
        shouldNotBeVisible(tagFld(), "\"Tag\" fld shouldn't be visible after pressing \"Enter\" key on it");
    }

    @Step("Remove <{0}th> tag (left to right)")
    public void removeTag(String index) {
        click(removeTagBtn(index));
    }


    //---------------------------------------- SEARCH ------------------------------------------//

    //TODO: test out this method
    public void findItemOnList(String expectedParamVal) {
        int totalItemsOnTable = $$(xpath("//a[@class='fc-table-tr']")).size();
        for (int i = 0; i < totalItemsOnTable; i++) {
            $(xpath("//tbody/a[" + i + "]//*")).shouldHave(text(expectedParamVal));
        }
    }

    public SelenideElement searchFld() {
        return $(xpath("//input[@placeholder='filter or keyword search']"));
    }

    private SelenideElement removeFilterBtn(String filterTitle) {
        return $(xpath("//div[@title='" + filterTitle + "']/a"));
    }

    private SelenideElement noSearchResultsMsg() {
        return $(By.xpath("//div[@class='fc-content-box__empty-row']"));
    }

    public ElementsCollection searchResults() {
        return $$(xpath("//tbody[@class='fc-table-body']/a"));
    }

    public SelenideElement searchMenu() {
        return $(xpath("//ul[@class='fc-menu-items']"));
    }

    public SelenideElement itemOnList(String itemParam) {
        return $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a/td[text()='" + itemParam + "']"));
    }

    public SelenideElement itemOnList_byPrice(String priceVal) {
        return $(xpath("//span[text()='" + priceVal + "']"));
    }

    private void removeFilterBtn_byIndex(String index) {
        $(By.xpath("//div[@class='fc-pilled-input__pill'][" + index + "]/a")).click();
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
        return $(By.xpath("//table[@class='fc-table']/thead/tr/th[" + (labelIndex + 1) + "]"));
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

    @Step("Remove search filter; <{0}>")
    public void removeSearchFilter(String label) {
        click(removeFilterBtn(label));
    }

    // used with filters like 'Products : ID : val', where "Products : ID" - is a single (non-composite) line
    @Step("Create a search filter <{0} : {1}>")
    public void addFilter(String firstCriteria, String secondCriteria) {
        click(searchFld());
        click(firstCriteria(firstCriteria));
        searchFld().sendKeys(secondCriteria);
        hitEnter();
        waitForDataToLoad();
        $(xpath("//h1")).click();
    }

    // used with filters like 'Orders : State : Canceled'
    @Step("Create a search filter <{0} : {1} : {2}>")
    public void addFilter(String firstCriteria, String secondCriteria, String thirdCriteria) {
        String secondCriteriaVal = firstCriteria + " : " + secondCriteria;

        click( searchFld() );
        shouldBeVisible(searchMenu(), "Failed to wait for search menu to appear");
        click( firstCriteria(firstCriteria) );
        click( secondCriteria(secondCriteriaVal) );
        searchFld().sendKeys(thirdCriteria);
        hitEnter();
        waitForDataToLoad();
        click($(xpath("//h1")));

    }

    // used with date pickers
    @Step("Create a search filter <{0} : {1} : {2}>")
    public void addFilter(String firstCriteria, String secondCriteria, SelenideElement date) {
        String secondCriteriaVal = firstCriteria + " : " + secondCriteria;

        click( searchFld() );
        shouldBeVisible(searchMenu(), "Failed to wait for search menu to appear");
        click( firstCriteria(firstCriteria) );
        click( secondCriteria(secondCriteriaVal) );
        click( date );
        waitForDataToLoad();
        click($(xpath("//h1")));

    }

    // used with date pickers
    @Step("Create a search filter <{0} : {1} : {2} : {3}>")
    public void addFilter(String firstCriteria, String secondCriteria, String thirdCriteria, SelenideElement date) {
        String secondCriteriaVal = firstCriteria + " : " + secondCriteria;
        String thirdCriteriaVal = secondCriteriaVal + " : " + thirdCriteria;

        click( searchFld() );
        shouldBeVisible(searchMenu(), "Failed to wait for search menu to appear");
        click( firstCriteria(firstCriteria) );
        click( secondCriteria(secondCriteriaVal) );
        click( thirdCriteria(thirdCriteriaVal) );
        click( date );
        waitForDataToLoad();
        click($(xpath("//h1")));

    }

    //used with filters like 'Order : Total : > : $1'
    @Step("Create a search filter <{0} : {1} : {2} : {3}>")
    public void addFilter(String firstCriteria, String secondCriteria, String thirdCriteria, String fourthCriteria) {
        String secondCriteriaVal = firstCriteria + " : " + secondCriteria;
        String thirdCriteriaVal = secondCriteriaVal + " : " + thirdCriteria;

        click( searchFld() );
        shouldBeVisible(searchMenu(), "Failed to wait for search menu to appear");
        click( firstCriteria(firstCriteria) );
        click( secondCriteria(secondCriteriaVal) );
        click( thirdCriteria(thirdCriteriaVal) );
        searchFld().sendKeys(fourthCriteria);
        hitEnter();
        waitForDataToLoad();
        click($(xpath("//h1")));

    }

    // fast search, uses no search filters
    @Step("Search for: <{0}>")
    public void search(String searchQuery) {
        waitForDataToLoad();
        setFieldVal(searchFld(), searchQuery);
        hitEnter();
        shouldBeVisible(itemsOnList(), "Search request returned no results.");
        click(searchFld());
        click($(xpath("//h1")));
    }

    @Step("Remove all search filters from search field")
    public void cleanSearchField() {
        int index = allSearchFilters().size();

        if (index > 0) {
            for (int i = 0; i < index; i++) {
                removeFilterBtn_byIndex("1");
            }
        }
    }

    // indexing starts with 1
    @Step("Sort list of orders by {0} column index")
    public void sortListBy(int columnIndex) {
        click(columnLabel(columnIndex));
        waitForDataToLoad();
    }

    @Step("Search should return no results -- \"No orders found\" msg should be displayed")
    public void assertNoSearchResults() {
        noSearchResultsMsg().shouldBe(visible
                .because("Search output isn't empty"));
    }

}
