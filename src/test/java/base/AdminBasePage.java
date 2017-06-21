package base;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static org.openqa.selenium.By.xpath;

public class AdminBasePage extends ConciseAPI {

    //---------------------------------- GENERAL CONTROLS -----------------------------------//

    public SelenideElement counter() {
        return $(xpath("//span[@id='fct-total-counter-value']"));
    }

    public SelenideElement userMenuBtn() {
        return $(xpath("//*[@id='fct-user-menu-btn']"));
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

    public SelenideElement breadcrumb(String value) {
        return $(xpath("//div[contains(@class, 'top-nav-menu')]//a[contains(@class, 'item') and text()='" + value + "']"));
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
        click(modalCancelBtn());
    }

    public SelenideElement modalSaveBtn() {
        return $(xpath("//*[@id='fct-modal-confirm-btn']"));
    }

    public SelenideElement modalCancelBtn() {
        return $(xpath("//*[@id='fct-modal-cancel-btn']"));
    }

    @Step("Click \"Save\" btn")
    public void clickSaveBtn_modal() {
        click(modalSaveBtn());
    }

    @Step("Click \"Cancel\" btn")
    public void clickCancelBtn_modal() {
        click(modalCancelBtn());
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

    @Step("Navigate to <{0}> category")
    public void navigateTo(String categoryTitle) {
        click($(xpath("//span[text()='" + categoryTitle + "']")));
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
        return $(xpath("//div[contains(@class, 'success')]//*[text()='You have successfully logged out.']"));
    }

    public SelenideElement loginErrorMsg() {
        return $(xpath("//div[contains(@class, 'error')]//*[text()='Invalid credentials']"));
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
        return $(xpath("//button[@id='fct-primary-save-btn']"));
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
        sleep(1000);
        shouldNotBeVisible($(xpath("//button[@id='fct-primary-save-btn' and contains(@class, 'loading')]")),
                "\"Save\" btn doesn't get re-enabled");
        sleep(3000);
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

    public SelenideElement firstItemOnList() {
        return $(xpath("//tbody/a[1]"));
    }

    @Step("Open first item on the category table")
    public void openFirstItemOnList() {
        waitForDataToLoad();
        shouldBeVisible(contentOnList(), "Data isn't displayed at the category view table");
        click(firstItemOnList());
        shouldBeVisible(saveBtn(), "Failed to open first item on the list");
    }

    private SelenideElement richTextFld(String name) {
        if (Objects.equals(name, "Storefront Name")) {
            name = "storefrontname";
        } else {
            name = name.replaceAll(" ", "-").toLowerCase();
        }
        return $(xpath("//div[contains(@class, 'fc-rich-text__name-" + name + "')]//div[@role='textbox']"));
    }

    private SelenideElement richTextVal(String name) {
        if (Objects.equals(name, "Storefront Name")) {
            name = "storefrontname";
        } else {
            name = name.replaceAll(" ", "-").toLowerCase();
        }
        return $(xpath("//div[contains(@class, 'fc-rich-text__name-" + name + "')]//span"));
    }

    @Step("Set <{0}> rich text fld to <{1}>")
    private void setRichTextFldVal(String name, String text) {
        clearField(richTextFld(name));
        if (Objects.equals(name, "Storefront Name")) {
            name = "storefrontname";
        } else {
            name = name.replaceAll(" ", "-").toLowerCase();
        }
        String className = "fc-rich-text__name-" + name;
        System.out.println(className);

        jsClick(richTextFld(name));
        JavascriptExecutor executor = (JavascriptExecutor)getWebDriver();
        executor.executeScript("function addTextToDraftJs(className, text) {\n" +
                "  var components = document.getElementsByClassName(className);\n" +
                "  if(components && components.length) {\n" +
                "    var textarea = components[0].getElementsByClassName('public-DraftEditor-content')[0];\n" +
                "    var textEvent = document.createEvent('TextEvent');\n" +
                "    textEvent.initTextEvent ('textInput', true, true, null, text);\n" +
                "    textarea.dispatchEvent(textEvent);\n" +
                "  }  \n" +
                "}\n" +
                "addTextToDraftJs(arguments[0], arguments[1]);", className, text);
    }

    //---------------------------------- CUSTOM PROPERTIES -----------------------------------//

    private SelenideElement addCustomPropBtn() {
        return $(xpath("//a[@id='fct-add-btn__custom-property']"));
    }

    public SelenideElement customText(String title) {
        return $(xpath("//input[@name='" + title.toLowerCase() + "']"));
    }

    public SelenideElement customPrice(String title) {
        return $(xpath("//input[@name='" + title.toLowerCase() + "']"));
    }

    public SelenideElement customDate(String title) {
        return $(xpath("//label[text()='" + title + "']/following-sibling::*//input[@name='someDatePretty']"));
    }

    private SelenideElement customRichText(String title) {
        return $(xpath("//div[text()='" + title + "']/following-sibling::*[2]//div[contains(@class,'public-DraftEditor-content')]"));
    }

    public SelenideElement customRichTextVal(String title) {
        return $(xpath("//div[text()='" + title.toLowerCase() + "']/following-sibling::*[2]//span/span"));
    }

    public String getRichTextVal(String label) {
        SelenideElement richText = $(xpath("//div[text()='" + label.toLowerCase() + "']/following-sibling::*[2]/div/div/div/div/div/div/span/span"));
        return richText.text();
    }

    private SelenideElement fieldLabelFld() {
        return $(xpath("//*[@id='fct-field-label-fld']"));
    }

    private SelenideElement fieldTypeDd() {
        return $(xpath("//*[@id='fct-field-type-dd']"));
    }

    @Step("Add custom <{0}> with title <{1}>")
    public void addCustomProp(String type, String title) {
        click(addCustomPropBtn());
        setFieldVal(fieldLabelFld(), title);
        setDdVal(fieldTypeDd(), type);
        clickSaveBtn_modal();
    }

    @Step("Set value of <{0}> custom text fld to <{1}>")
    public void setCustomTextVal(String title, String value) {
        setFieldVal(customText(title), value);
    }

    @Step("Set value of <{0}> custom rich text to <{1}>")
    public void setCustomRichTextVal(String title, String value) {
        setFieldVal(customRichText(title), value);
    }

//----
    public SelenideElement addTagBtn() {
        return $(xpath("//button[@id='fct-tag-toggle-btn']"));
    }

    private SelenideElement tagFld() {
        return $(xpath("//input[@placeholder='Separate tags with a comma']"));
    }

    protected SelenideElement removeTagBtn(String tag) {
        tag = tag.replaceAll(" ", "-").toLowerCase();
        return $(xpath("//div[@id='fct-tag__" + tag + "']/button"));
    }

    public SelenideElement tag(String tag) {
        tag = tag.replaceAll(" ", "-").toLowerCase();
        return $(xpath("//div[@id='fct-tag__" + tag + "']"));
    }

    public ElementsCollection allTags() {
        return $$(xpath("//div[contains(@class, 'fct-pill-label')]"));
    }

    //---------------------------------- HELPERS -----------------------------------//

    @Step("Add tag <{0}>")
    public void addTag(String tagVal) {
        click(addTagBtn());
        setFieldVal(tagFld(), tagVal);
        tagFld().pressEnter();
        shouldNotBeVisible(tagFld(), "\"Tag\" fld shouldn't be visible after pressing \"Enter\" key on it");
    }

    @Step("Remove tag <{0}>")
    public void removeTag(String tag) {
        click(removeTagBtn(tag));
    }


    //---------------------------------------- SEARCH ------------------------------------------//

    public SelenideElement searchFld() {
        return $(xpath("//input[@placeholder='filter or keyword search']"));
    }

    public SelenideElement searchPill(String title) {
        return $(xpath("//div[@title='" + title + "']"));
    }

    private SelenideElement removeFilterBtn(String filterTitle) {
        return $(xpath("//div[@title='" + filterTitle + "']/a"));
    }

    public SelenideElement noSearchResultsMsg() {
        return $(By.xpath("//div[@class='fc-content-box__empty-row']"));
    }

    public ElementsCollection searchResults() {
        return $$(xpath("//tbody[@class='fc-table-body']/a"));
    }

    public SelenideElement searchMenu() {
        return $(xpath("//div[@class='fc-live-search']//*[contains(@class,'block')]"));
    }

    public SelenideElement objOnCategoryTable(String itemParam) {
        return $(xpath("//tbody//*[text()='" + itemParam + "']"));
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

    public SelenideElement searchTab(String title) {
        return $(xpath("//div[@class='fc-editable-tab']/li[text()='" + title + "']"));
    }

    @Step("Switch to <{0}> saved search tab")
    public void switchToSearchTab(String tabTitle) {
        click(searchTab(tabTitle));
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
        return $(xpath("//div[@class='fc-live-search']//*[contains(@class,'block')]/div/span[text()='" + criteria + "']/.."));
    }

    private SelenideElement secondCriteria(String criteria) {
        return $(xpath("//div[@class='fc-live-search']//*[contains(@class,'block')]/div/span[text()='" + criteria + "']/.."));
    }

    private SelenideElement thirdCriteria(String criteria) {
        return $(xpath("//div[@class='fc-live-search']//*[contains(@class,'block')]/div/span[text()='" + criteria + "']/.."));
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
        searchFld().click();
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
        searchFld().click();
        $(xpath("//h1")).click();
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

}
