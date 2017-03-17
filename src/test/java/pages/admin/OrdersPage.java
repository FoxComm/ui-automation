package pages.admin;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class OrdersPage extends BasePage {

    //--------------------------------------- ELEMENTS ---------------------------------------//

    private SelenideElement ordersCounter() {
        return $(By.xpath("//span[@class='fc-section-title__subtitle fc-light']"));
    }

    private SelenideElement orderOnList(String index) {
        return $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + index + "]"));
    }

//    private List<SelenideElement> allSearchFilters() {
//        return $$(By.xpath("//div[@class='fc-pilled-input__pill']/a"));
//    }

    public SelenideElement newOrderButton() {
        return $(By.xpath("//button[@class='fc-btn fc-btn-primary']"));
    }

    public SelenideElement searchCriteriaList() {
        return $(By.xpath("//div[@class='fc-menu']"));
    }

//    private SelenideElement columnLabel(int labelIndex) {
//        return $(By.xpath("//table[@class='fc-table fc-multi-select-table']/thead/tr/th[" + (labelIndex + 1) + "]"));
//    }

//    private SelenideElement noSearchResults() {
//        return $(By.xpath("//div[@class='fc-content-box__empty-row']"));
//    }



    //---------------------------------------- HELPERS ----------------------------------------//

    private void takeFocusFromSearchFld() {
        ordersCounter().click();
        shouldNotBeVisible(searchMenu(), "Failed to wait for search menu to disappear");
    }

    //-------------------- ORDERS LIST --------------------//

    public SelenideElement getOrderParamValue(int orderIndex, String paramName) {
        SelenideElement orderParamVal = null;
        waitForDataToLoad();
        switch(paramName) {
            case "Order":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[2]"));
                break;
            case "Date/Time Placed":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[3]/time"));
                break;
            case "Customer Name":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[4]"));
                break;
            case "Customer Email":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[5]"));
                break;
            case "Order State":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[6]/span"));
                break;
            case "Shipment State":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[7]"));
                break;
            case "Total":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[8]/span"));
                break;
        }
        return orderParamVal;
    }

//     DONT"T DELETE
//    To be refactored --
//    make it possible to assert that order's total on the category list is greater/less than certain value
//    @Step("{1} parameter value of {0}-th order on the list should be '{2} {3}'")
//    public void assertOrderParameter(int index, String paramName, String operatorName, int expectedParamValue) {
//
//        String strActualParamValue = getOrderParamValue(index, paramName);
//        System.out.println("strActualParamValue :" + strActualParamValue);
//        double actualParamValue = Double.valueOf(strActualParamValue.substring(1, strActualParamValue.length()));
//        System.out.println("actualParamValue :" + actualParamValue);
//
//        switch (operatorName) {
//            case "=":
//                assertEquals(actualParamValue, expectedParamValue,
//                        "Search results aren't relevant to a given search criteria");
//                break;
//            case ">":
//                assertTrue(actualParamValue > expectedParamValue,
//                        "Search results aren't relevant to a given search criteria");
//                break;
//            case ">=":
//                System.out.println(actualParamValue >= expectedParamValue);
//                assertTrue( (actualParamValue >= expectedParamValue),
//                        "Search results aren't relevant to a given search criteria");
//                break;
//            case "<":
//                assertTrue(actualParamValue < expectedParamValue,
//                        "Search results aren't relevant to a given search criteria");
//                break;
//            case "<=":
//                assertTrue(actualParamValue <= expectedParamValue,
//                        "Search results aren't relevant to a given search criteria");
//                break;
//        }
//
//    }


    //-------------------- SEARCH FIELD --------------------//

    private void selectLine(int index) {
        for (int i = 0; i < index; i++) {
            searchFld().sendKeys(Keys.ARROW_DOWN);
        }
        hitEnter();
    }

    @Step("Create a search filter <{0} : {1} : {2}>")
    public void addFilter_arrowKeys(String firstStatement, String secondStatement, String thirdStatement) {

        searchFld().click();
        shouldBeVisible(searchMenu(), "Failed to wait for search menu to appear");

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
                }
                break;

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
                break;

            case "Items":
                selectLine(7);
                switch (secondStatement)
                {
                    case "Product SKU":
                        selectLine(3);
                        searchFld().sendKeys(thirdStatement);
                        waitForDataToLoad();
                        break;
                }
                break;

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

        }

        waitForDataToLoad();
        takeFocusFromSearchFld();

    }

    @Step("Create a search filter <{0} : {1} : {2} : {3}>")
    public void addFilter_arrowKeys(String firstStatement, String secondStatement, String thirdStatement, String fourthStatement) {

        searchFld().click();
        shouldNotBeVisible(searchMenu(), "Failed to wait for search menu to appear");

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
                        searchFld().sendKeys(thirdStatement);
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
        takeFocusFromSearchFld();

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



//
//    @Step
//    public void cleanSearchField() {
//        int index = allSearchFilters().size();
//
//        if (index > 0) {
//            for (int i = 0; i < index; i++) {
//                removeFilter("1");
//            }
//        }
//    }
//
//    // indexing starts with 1
//    @Step("Sort list of orders by {0} column index")
//    public void sortListBy(int columnIndex) {
//        click( columnLabel(columnIndex) );
//        waitForDataToLoad();
//    }
//
//    @Step
//    public void assertNoSearchResults() {
//        Assert.assertTrue(noSearchResults().is(visible),
//                "Search query output isn't empty");
//    }

}
