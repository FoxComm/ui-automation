package pages;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class GeneralControlsPage extends BasePage {


    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement filter(String filterTitle) {
        return $(xpath("//div[@title='" + filterTitle + "']"));
    }

    public SelenideElement removeFilterBtn(String filterTitle) {
        return $(xpath("//div[@title='" + filterTitle + "']/a"));
    }

    private SelenideElement searchContextMenuBtn() {
        return $(xpath("//i[@class='icon-search']/../following-sibling::*"));
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

    //------------------------------ HELPERS ---------------------------------//

    @Step("Select <{0}> option in search context menu.")
    public void searchContextMenu(String option) {
        sleep(3000);
        searchContextMenuBtn().shouldBe(enabled);
        click( searchContextMenuBtn() );
        click( $(xpath("//li[text()='" + option + "']")) );
        waitForDataToLoad();
        waitForDataToLoad();
        if (option.equals("Save New Search")) {
            tabTitleFld().pressEnter();
            ordersCounter().click();
        }
    }

    @Step("Delete all search tabs")
    public void deleteAllSearchTabs() {
        for (int i = 0; i < tabs().size() - 1; i++) {
            click(tabs().get(1));
            searchContextMenu("Delete Search");
            waitForDataToLoad();
            tabs().get(0).shouldBe(enabled);
        }
    }

    @Step("Switch to <{0}> search tab")
    public void switchToTab(String tabTitle) {
        tab(tabTitle).shouldBe(enabled);
        click( tab(tabTitle) );
        waitForDataToLoad();
        tab(tabTitle).shouldBe(enabled);
    }

    @Step("Remove filter {0}")
    public void removeFilter(String filterTitle) {
        removeFilterBtn(filterTitle).click();
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
