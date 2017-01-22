package pages;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class GeneralControlsPage extends BasePage {


    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement filter(String filterTitle) {
        return $(xpath("//div[@title='" + filterTitle + "']"));
    }

    //TODO: add id in Ashes
    private SelenideElement searchContextMenuBtn() {
        return $(xpath("//div[@class='_common_button_with_menu__controls']/button[2]"));
    }
    public SelenideElement tab(String tabTitle) {
        return $(xpath("//li[text()='" + tabTitle + "']"));
    }

    public SelenideElement tabTitleFld() {
        return $(xpath("//input[@placeholder='Name your search']"));
    }

    public SelenideElement dirtySearchIndicator() {
        return $(xpath("//div[@class='fc-editable-tab__dirty-icon']"));
    }

    public SelenideElement ordersCounter() {
        return $(xpath("//span[@class='fc-section-title__subtitle fc-light']"));
    }

    public ElementsCollection tabs() {
        return $$(xpath("//ul[@class='fc-tab-list__current-tabs']/div"));
    }

    public SelenideElement inviteUsersFld() {
        return $(xpath("//input[@placeholder='Name or email...']"));
    }

    public SelenideElement userName(String nameVal) {
        return $(xpath("//div[text()='" + nameVal + "']"));
    }

    public SelenideElement shareBtn() {
        return $(xpath("//span[text()='Share']/.."));
    }

    public SelenideElement adminPilledLabel() {
        return $(xpath("//div[@class='fc-pilled-input__pills-wrapper']/div"));
    }

    public SelenideElement removeAdminBtn(String adminName) {
        return $(xpath("//span[text()='" + adminName + "']/following-sibling::*[3]/a"));
    }

    public SelenideElement adminOnAssociationsList(String adminName) {
        return $(xpath("//span[text()='" + adminName + "']"));
    }

    //------------------------------ HELPERS ---------------------------------//

    @Step("Set tab title to <{0}>")
    public void setTabTitle(String title) {
        tabTitleFld().setValue("Edited Name").pressEnter();
    }

    @Step("Select <{0}> in search context menu")
    public void searchContextMenu(String option) {
        sleep(3000);
        shouldBeEnabled(searchContextMenuBtn(), "Failed to wait until searchContextMenuBtn() will become <enabled>");
        click(searchContextMenuBtn());
        click($(xpath("//li[text()='" + option + "']")));
        waitForDataToLoad();
        if (option.equals("Save New Search")) {
            tabTitleFld().pressEnter();
            click(ordersCounter());
        }
    }

    @Step("Delete all search tabs")
    public void deleteAllSearchTabs() {
        for (int i = 0; i < tabs().size() - 1; i++) {
            openSecondSearchTab();
            searchContextMenu("Delete Search");
            waitForDataToLoad();
            shouldBeEnabled(tabs().get(0), "Failed to remove extra search tabs -- something went wrong");
        }
    }

        @Step("Open 2nd search tab")
        private void openSecondSearchTab() {
            click(tabs().get(1));
        }

    @Step("Switch to <{0}> search tab")
    public void switchToTab(String title) {
        shouldBeEnabled(tab(title), "Tab with <" + title + "> isn't enabled");
        click(tab(title));
        waitForDataToLoad();
        shouldBeEnabled(tab(title), "Tab with <" + title + "> isn't enabled");
    }

    @Step("Add <{0}> admin user to \"Search Associations\" list")
    public void shareSearchWith(String adminName) {
        setFieldVal(inviteUsersFld(), adminName);
        click(userName(adminName));
        shouldBeEnabled(shareBtn(), "\"Share\" btn isn't re-enabled after it was clicked");
    }

    @Step("Remove <{0}> admin user from \"Search Associations\" list")
    public void unshareSearchWith(String adminName) {
        removeAdmin(adminName);
        shouldBeVisible(successIcon(), "\"Success\" msg isn't displayed.");
    }

        @Step("Click \"X\" btn next to <{0}> admin user")
        public void removeAdmin(String adminName) {
            click(removeAdminBtn(adminName));
        }

    @Step("Click \"Share\"")
    public void clickShare() {
        click( shareBtn() );
    }

    //-------------------- SEARCH FIELD --------------------//
    private void selectLine(int index) {
        for (int i = 0; i < index; i++) {
            searchFld().sendKeys(Keys.ARROW_DOWN);
        }
        hitEnter();
    }

    public void hitEnter() {
        searchFld().sendKeys(Keys.ENTER);
        sleep(200);
    }

    private void takeFocusAway() {
        ordersCounter().click();
        sleep(500);
    }

    @Step("Create a search filter {0} : {1} : {2}")
    public void addFilter_arrowKeys(String firstStatement, String secondStatement, String thirdStatement) {

        searchFld().click();

        switch(firstStatement)
        {
            case "Order":
                selectLine(1);
                switch(secondStatement)
                {
                    case "Reference Number":
                        selectLine(1);
                        searchFld().sendKeys(thirdStatement);
                        hitEnter();
                        waitForDataToLoad();
                        break;
                    case "State":
                        selectLine(2);
                        switch(thirdStatement)
                        {
                            case "Remorse Hold":
                                selectLine(1);
                                waitForDataToLoad();
                                break;
                            case "Manual Hold":
                                selectLine(2);
                                waitForDataToLoad();
                                break;
                            case "Fulfillment Started":
                                selectLine(4);
                                waitForDataToLoad();
                                break;
                        }
                        break;
                }

            case "Customer":
                selectLine(4);
                switch(secondStatement) {
                    case "Name":
                        selectLine(1);
                        searchFld().sendKeys(thirdStatement);
                        hitEnter();
                        waitForDataToLoad();
                        break;
                    case "Email":
                        selectLine(2);
                        searchFld().sendKeys(thirdStatement);
                        hitEnter();
                        waitForDataToLoad();
                        break;
                }

            case "Assignee":
                selectLine(8);
                switch(thirdStatement)
                {
                    case "Has Assignee":
                        selectLine(1);
                        waitForDataToLoad();
                        break;
                    case "Has No Assignee":
                        selectLine(2);
                        waitForDataToLoad();
                        break;
                }
                break;

            case "Shipping":
                selectLine(5);
                switch(secondStatement)
                {
                    case "City":
                        selectLine(1);
                        searchFld().sendKeys(thirdStatement);
                        hitEnter();
                        waitForDataToLoad();
                        break;

                    case "State":
                        selectLine(2);
                        searchFld().sendKeys(thirdStatement);
                        hitEnter();
                        waitForDataToLoad();
                        break;

                    case "Zip":
                        selectLine(3);
                        searchFld().sendKeys(thirdStatement);
                        hitEnter();
                        waitForDataToLoad();
                        break;
                }

        }
        // resets searchFld focus (works as a hack)
        takeFocusAway();

    }

    @Step("Create a search filter {0} : {1} : {2} : {3}")
    public void addFilter_arrowKeys(String firstStatement, String secondStatement, String thirdStatement, String fourthStatement) {

        searchFld().click();

        switch(firstStatement)
        {
            case "Items":
                selectLine(7);
                switch (secondStatement)
                {
                    case "Total Number":
                        selectLine(1);
                        defineOperator(thirdStatement, fourthStatement);
                        waitForDataToLoad();
                        break;
                    case "Product SKU":
                        selectLine(3);
                        defineOperator(thirdStatement, fourthStatement);
                        waitForDataToLoad();
                        break;
                }
                break;
            case "Order":
                selectLine(1);
                switch(secondStatement)
                {
                    case "Date Placed":
                        selectLine(3);
                        // fourthStatement format must be MMDDYYYY, e.g. - "06212016"
                        defineOperator(thirdStatement, fourthStatement);
                        waitForDataToLoad();
                        break;
                    case "Total":
                        selectLine(4);
                        defineOperator(thirdStatement, fourthStatement);
                        waitForDataToLoad();
                        break;
                }

        }
        // resets searchFld focus (works as a hack)
        takeFocusAway();

    }

    // sub-method for defining 3rd and 4th method for 4-argument search filters
    private void defineOperator(String thirdStatement, String fourthStatement) {

        switch (thirdStatement)
        {
            case "=":
                setStatementVal(1, fourthStatement);
                break;
            case "<>":
                setStatementVal(2, fourthStatement);
                break;
            case ">":
                setStatementVal(3, fourthStatement);
                break;
            case ">=":
                setStatementVal(4, fourthStatement);
                break;
            case "<":
                setStatementVal(5, fourthStatement);
                break;
            case "<=":
                setStatementVal(6, fourthStatement);
                break;
        }

    }

    private void setStatementVal(int index_arrowDown, String fourthStatement) {
        selectLine(index_arrowDown);
        sleep(500);
        searchFld().sendKeys(fourthStatement);
        hitEnter();
    }
    //--------------------


}
