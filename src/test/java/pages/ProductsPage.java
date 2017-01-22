package pages;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class ProductsPage extends BasePage {

    /**
     *                                  E L E M E N T S
     */

    private SelenideElement addNewProductBtn() {
        return $(xpath("//span[text()='Product']/.."));
    }

    public SelenideElement product(String name) {
        return $(xpath("//tbody/a/td[contains(@class, 'product-name') and text()='" + name + "']"));
    }

    //-------------------------- NEW PRODUCT
    public SelenideElement titleFld() {
        return $(xpath("//input[@name='title']"));
    }

    //TODO: rich text. you know what needs to be done.
    public SelenideElement descriptionFld() {
        return $(xpath("//div[@class='public-DraftEditor-content']"));
    }

    public SelenideElement skuFld(int index) {
        return $(xpath("//tbody[@id='sku-list']/tr[" + index + "]//input[@placeholder='SKU']"));
    }

    private SelenideElement skuFld() {
        return $(xpath("//input[@placeholder='SKU']"));
    }

    private SelenideElement skuSearchView(String skuCode) {
        return $(xpath("//li[@id='search-view-" + skuCode + "']"));
    }

    public SelenideElement retailPriceFld() {
        return $(xpath("//td[contains(@class, 'retail-price')]//input"));
    }

    public SelenideElement salePriceFld() {
        return $(xpath("//td[contains(@class, 'sale-price')]//input"));
    }

    public SelenideElement stateDd() {
        return $(xpath("//button[@id='state-dd]"));
    }

    public SelenideElement stateVal() {
        return $(xpath("//div[@id=state-dd--value']"));
    }

    private SelenideElement removeStartDateBtn() {
        return $(xpath("//a[@id='remove-start-date-btn']"));
    }

    public SelenideElement saveDraftBtn() {
        return $(xpath("//span[text()='Save']/.."));
    }
    //--------------------------

    //TODO: to be moved to BasePage
    private SelenideElement productsCounter() {
        return $(xpath("//*[@id='total-counter-value']"));
    }

    public SelenideElement noSKUsMsg() {
        return $(xpath("//div[text()='No SKUs.']"));
    }

    private SelenideElement productId() {
        return $(xpath("//a[@id='item-id']"));
    }

    private SelenideElement addSKUBtn() {
        return $(xpath("//a[@id='sku-block-add-sku-btn']"));
    }

    private SelenideElement addBtn() {
        return $(xpath("//button[@id='modal-confirm-btn']"));
    }

    private SelenideElement availableOptionChkbx(String valuesPairVal) {
        valuesPairVal = valuesPairVal.replaceAll(" ", "-").toLowerCase();
        return $(xpath("//input[@name='" + valuesPairVal + "-option-chbox']"));
    }

    /**
     * Looks for a SKU input field filled with a given SKU code
     */
    public SelenideElement sku(String skuCode) {
        return $(xpath("//input[@placeholder='SKU' and @value='" + skuCode + "']"));
    }

    //TODO: rework the element locator - element should be searched by its index, not option values
    /**
     * Looks for a block with a given option value pair
     * Values should be given as they are displayed on the page in order left to right
     */
    public SelenideElement skuWithVariant(String firstValue, String secondValue) {
        return $(xpath("//td/div[text()='" + firstValue + "']/../following-sibling::*[1]/div[text()='" + secondValue + "']"));
    }

    public SelenideElement sku_byOptVal(String optValueVal) {
        return $(xpath("//tbody[@id='sku-list']//div[text()='" + optValueVal + "']"));
    }

    private SelenideElement removeSKUBtn(String optValueVal) {
        return $(xpath("//div[text()='" + optValueVal + "']/../following-sibling::*//button[@class='fc-btn fc-btn-remove']"));
    }

    //TODO: once figured out where to add ids at SKUs box - add id to this
    private SelenideElement removeSKUBtn(String firstValue, String secondValue){
        return $(xpath("//tbody[@id='sku-list']//div[text()='"+firstValue+"']/../following-sibling::*[1]/div[text()='"+secondValue+"']/../following-sibling::*//button[@class='fc-btn fc-btn-remove']"));
    }

    private SelenideElement removalConfirmBtn() {
        return $(xpath("//button[@id='modal-confirm-btn']"));
    }

    //-------------------------- OPTIONS (a.k.a VARIANTS)

    public SelenideElement option(String optionVal) {
        optionVal = optionVal.replaceAll(" ", "-").toLowerCase();
        return $(xpath("//*[@id='product-option-" + optionVal + "-box']"));
    }

    public SelenideElement optionValue(String optionVal, String optValueVal) {
        return $(xpath("//div[@id='product-option-" + optionVal + "-box']//td[@id='" + optValueVal + "-value-name']"));
    }

    public SelenideElement optionValue_SKUsTable(String optionVal, String optValueVal) {
        return $(xpath("//div[text()='" + optionVal + "']/../following-sibling::*//td[text()='" + optValueVal + "']"));
    }

    private SelenideElement addOptionBtn() {
        return $(xpath("//a[@id='add-new-option-btn']"));
    }

    private SelenideElement saveOptionBtn() {
        return $(xpath("//*[@id='modal-confirm-btn']"));
    }

    private SelenideElement addOptionValueBtn(String option) {
        return $(xpath("//*[@id='product-option-" + option + "-box']//a[contains(@class, 'option-add-btn')]"));
    }

    private SelenideElement editOptionBtn(String option) {
        return $(xpath("//*[@id='product-option-" + option + "-box']//a[contains(@class, 'option-edit-btn')]"));
    }

    private SelenideElement deleteOptionBtn(String option) {
        return $(xpath("//*[@id='product-option-" + option + "-box']//a[contains(@class, 'option-delete-btn')]"));
    }

    private SelenideElement editOptionValueBtn(String optionValueName) {
        return $(xpath("//a[@id='" + optionValueName + "-value-edit-btn']"));
    }

    private SelenideElement deleteOptionValueBtn(String optionValueName) {
        return $(xpath("//a[@id='" + optionValueName + "-value-delete-btn']"));
    }

    private SelenideElement nameFld() {
        return $(xpath("//input[@id='value-name-field']"));
    }

    private SelenideElement saveValueBtn() {
        return $(xpath("//button[@id='modal-confirm-btn']"));
    }


    /**
     *                                  H E L P E R S
     */

//    @Step("Remove all filters from the search field")
//    public void clearSearchFld() {
//        waitForDataToLoad();
//        ElementsCollection searchFiltersList = $$(By.xpath("//a[@class='fc-pilled-input__pill-close']"));
//        for(SelenideElement filterCloseBtn : searchFiltersList) {
//            click( filterCloseBtn );
//        }
//    }

    @Step("Open product by its ID/Name: <{0}>")
    public void openProduct(String idOrName){
        click(product(idOrName));
    }

    @Step("Set product state to '{0}'")
    public void setState(String state) {
        switch (state) {
            case "Inactive":
                click(removeStartDateBtn());
                break;
            case "Active":
                setDdVal(stateDd(), state);
                break;
        }
    }

    @Step("Clear all tags from product")
    public void clearTags() {
        ElementsCollection tags = $$(xpath("//*[contains(@id, 'tag-close-btn')]"));
        System.out.println(tags.size());
        for(int i = 0; i < tags.size(); i++) {
            click( removeTagBtn("1") );
            removeTagBtn("1").shouldNot(exist);
        }
    }

    @Step("Get '{1}' parameter value of {0}th product on the list")
    public SelenideElement getProductParamVal(String productIndex, String paramName) {
        SelenideElement productParamVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Product ID":
                productParamVal = $(xpath("//tbody/a[" + productIndex + "]//td[contains(@class, 'product-id')]"));
                break;
//            case "Image":
//                productParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + productIndex + "]/td[3]/img")).getAttribute("src");
//                break;
            case "Name":
                productParamVal = $(xpath("//tbody/a[" + productIndex + "]//td[contains(@class, 'product-name')]"));
                break;
            case "State":
                productParamVal = $(xpath("//tbody/a[" + productIndex + "]//td[contains(@class, 'state')]"));
                break;

        }
        return productParamVal;
    }

//    private void selectLine(int index) {
//        for (int i = 0; i < index; i++) {
//            searchFld().sendKeys(Keys.ARROW_DOWN);
//        }
//        hitEnter();
//    }
//
//    @Step("Create a search filter {0} : {1} : {2}")
//    public void addFilter_arrowKeys(String firstStatement, String secondStatement, String thirdStatement) {
//
//        searchFld().click();
//
//        switch(firstStatement)
//        {
//            case "Product":
//
//                switch(secondStatement)
//                {
//                    case "ID":
//                        selectLine(1);
//                        waitForDataToLoad();
////                        hitEnter();
//                        searchFld().sendKeys(thirdStatement);
//                        hitEnter();
//                        waitForDataToLoad();
//                        break;
//                    case "Name":
//                        selectLine(2);
////                        hitEnter();
//                        searchFld().sendKeys(thirdStatement);
//                        hitEnter();
//                        waitForDataToLoad();
//                        break;
//                }
//                break;
//
//        }
//        // resets searchFld focus (works as a hack)
//        takeFocusAway();
//
//    }
//
//    private void takeFocusAway() {
//        productsCounter().click();
//        sleep(500);
//    }

    @Step("Click \"Add New Product\" btn")
    public void clickAddNewProduct(){
        click(addNewProductBtn());
    }

    @Step("Add existind SKU <{1}>")
    public void addExistingSKU(int index, String SKU) {
        setFieldVal(skuFld(index), SKU);
        click(skuSearchView(SKU));
    }

    @Step("Add existind SKU <{0}>")
    public void addExistingSKU(String SKU) {
        setFieldVal(skuFld(), SKU);
        click(skuSearchView(SKU));
    }

    @Step("Fill out the 'New Product' form - Title: <{0}>, SKU: <{1}>, Retail Price: <{2}>, Sale Price: <{3}>, State: <{4}>")
    public void createProduct(String title, String SKU, String retailPrice, String salePrice, String tagVal, String state) {
        setTitle(title);
        setFieldVal( descriptionFld(), "The best thing to buy in 2016!" );
        addNewSKU(SKU, retailPrice, salePrice);
        setState(state);
        addTag(tagVal);
        clickSave();
        shouldBeEnabled(saveBtn(), "\"Save\" btn isn't re-enabled after it was clicked to create a new product");
        shouldNotHaveText(productId(), "new", "Failed to create a new product - rout is displayed as `/new`");
    }

        @Step("Set \"Title\" fld val to <{0}>")
        public void setTitle(String title) {
            setFieldVal(titleFld(), title);
        }

        @Step("Set \"Description\" val to <{0}>")
        public void setDescription(String description) {
            setFieldVal( descriptionFld(), description );
        }

        @Step("Add SKU to a product: <{0}>, <salePr:{1}>, <retailPr:{2}>")
        private void addNewSKU(String SKU, String retailPrice, String salePrice) {
            setFieldVal(skuFld(), SKU);
            click(skuSearchView(SKU));
            setFieldVal(retailPriceFld(), retailPrice);
            setFieldVal(salePriceFld(), salePrice);
        }

        @Step("Set \"Retail Price\" fld val to <{0}>")
        public void setRetailPrice(String price) {
            setFieldVal(retailPriceFld(), price);
        }

        @Step("Set \"Sale Price\" fld val to <{0}>")
        public void setSalePrice(String price) {
            setFieldVal(salePriceFld(), price);
        }


    @Step("Add <{0}> option")
    public void addOption(String optionVal) {
        clickAddBtn_option();
        setName(optionVal);
        clickSaveOptionBtn();
    }


        @Step("Click \"+\" btn at \"Options\" block")
        public void clickAddBtn_option() {
            click(addOptionBtn());
        }

        @Step("Set \"Name\" to <{0}>")
        public void setName(String nameVal) {
            setFieldVal(nameFld(), nameVal);
        }

        @Step("Click \"Save option\" btn")
        public void clickSaveOptionBtn() {
            click( saveOptionBtn() );
        }

    @Step("Add value <{1}> to the option <{0}>")
    public void addOptionValue(String optionVal, String nameVal) {
        clickAddBtn_optionValue(optionVal);
        setName(nameVal);
        clickSaveValueBtn();
    }

        @Step("Click \"+\" btn at <{0}> option")
        private void clickAddBtn_optionValue(String optionVal) {
            click(addOptionValueBtn(optionVal));
        }

        @Step("Click \"Save value\" btn")
        private void clickSaveValueBtn() {
            click( saveValueBtn() );
        }

    @Step("Assert that \"SKUs\" block has <{0}> SKU lines")
    public void assertAmountOfSKUs(int expectedAmount) {
        $$(xpath("//input[@placeholder='SKU']")).shouldHaveSize(expectedAmount);
    }

    @Step("Remove <{0}> option value")
    public void removeOptionValue(String optionName) {
        click(deleteOptionValueBtn(optionName));
    }

    @Step("Change value name to <{0}>")
    public void editValue(String optionValueVal, String newValueVal) {
        click(editOptionValueBtn(optionValueVal));
        setName(newValueVal);
        clickSaveValueBtn();
    }

    @Step("Remove SKU with an option value combo <{0}> & <{1}>")
    public void removeSKU(String firstValue, String secondValue) {
        click(removeSKUBtn(firstValue, secondValue));
        click(removalConfirmBtn());
    }

    @Step("Re-add SKU for option values combo <{0} & {1}>")
    public void reAddSKU(String firstValue, String secondValue) {
        String availableOption = firstValue + ", " + secondValue;
        click(addSKUBtn());
        jsClick(availableOptionChkbx(availableOption));
        click(addBtn());
    }

}