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

    public SelenideElement product(String nameOrId) {
        return $(xpath("//tbody[@class='fc-table-body']/a/td[text()='" + nameOrId + "']"));
    }

    //-------------------------- NEW PRODUCT
    public SelenideElement titleFld() {
        return $(xpath("//input[@name='title']"));
    }

    public SelenideElement descriptionFld() {
        return $(xpath("//div[@class='public-DraftEditor-content']"));
    }

    public SelenideElement skuFld(int index) {
        return $(xpath("//div[text()='SKUs']/../following-sibling::*//tbody/tr[" + index + "]//div[@class='_forms_css_loading_input_wrapper__wrapper']/input"));
    }

    private SelenideElement skuFld() {
        return $(xpath("//input[@placeholder='SKU']"));
    }

    private SelenideElement skuSearchView(String skuCode) {
        return $(xpath("//div[contains(@class, 'sku-cell')]/ul/li/strong[text()='" + skuCode + "']"));
    }

    public SelenideElement retailPriceFld() {
        return $(xpath("//tbody[@class='fc-table-body']/tr[1]/td[4]//input"));
    }

    public SelenideElement salePriceFld() {
        return $(xpath("//tbody[@class='fc-table-body']/tr[1]/td[5]//input"));
    }

    public SelenideElement stateDd() {
        return $(xpath("//div[@class='fc-product-state']/div[2]/div[2]/button"));
    }

    public SelenideElement stateListVal(String state) {
        return $(xpath("//li[text()='" + state + "']"));
    }

    public SelenideElement stateVal() {
        return $(xpath("//div[@class='fc-product-state']/div[2]/div[2]/div"));
    }

    private SelenideElement removeStartDateBtn() {
        return $(xpath("//div[text()='Start']/following-sibling::*/div[2]/a"));
    }

    public SelenideElement saveDraftBtn() {
        return $(xpath("//span[text()='Save']/.."));
    }
    //--------------------------

    private SelenideElement productsCounter() {
        return $(xpath("//span[@class='fc-section-title__subtitle fc-light']/span"));
    }

    public SelenideElement noSKUsMsg() {
        return $(xpath("//div[text()='No SKUs.']"));
    }

    private SelenideElement productId() {
        return $(xpath("//header/div/ul/li[5]"));
    }

    private SelenideElement addSKUBtn() {
        return $(xpath("//a[@class='_products_skus_sku_content_box__add-icon']"));
    }

    private SelenideElement addBtn() {
        return $(xpath("//span[text()='Add']/.."));
    }

    private SelenideElement availableOptionChkbx(String valuesPairVal) {
        return $(xpath("//span[text()='" + valuesPairVal + "']/../preceding-sibling::*"));
    }

    /**
     * Looks for a SKU input field filled with a given SKU code
     */
    public SelenideElement sku(String skuCode) {
        return $(xpath("//tr[@class='fc-table-tr']//div[text()='" + skuCode + "']"));
    }

    /**
     * Looks for a block with a given option value pair
     * Values should be given as they are displayed on the page in order left to right
     */
    public SelenideElement skuWithVariant(String firstValue, String secondValue) {
        return $(xpath("//td/div[text()='" + firstValue + "']/../following-sibling::*[1]/div[text()='" + secondValue + "']"));
    }

    public SelenideElement sku_byOptVal(String optValueVal) {
        return $(xpath("//div[text()='" + optValueVal + "']"));
    }

    private SelenideElement removeSKUBtn(String optValueVal) {
        return $(xpath("//div[text()='" + optValueVal + "']/../following-sibling::*//button[@class='fc-btn fc-btn-remove']"));
    }

    private SelenideElement removeSKUBtn(String firstValue, String secondValue){
        return $(xpath("//td/div[text()='"+firstValue+"']/../following-sibling::*[1]/div[text()='"+secondValue+"']/../following-sibling::*//button[@class='fc-btn fc-btn-remove']"));
    }

    private SelenideElement removalConfirmBtn() {
        return $(xpath("//span[text()='Yes, Remove']/.."));
    }

    //-------------------------- OPTIONS (a.k.a VARIANTS)

    public SelenideElement option(String optionVal) {
        return $(xpath("//div[text()='" + optionVal + "']"));
    }

    public SelenideElement optionValue(String optionVal, String optValueVal) {
        return $(xpath("//div[text()='" + optionVal + "']/../following-sibling::*//td[text()='" + optValueVal + "']"));
    }

    public SelenideElement optionValue_SKUsTable(String optionVal, String optValueVal) {
        return $(xpath("//div[text()='" + optionVal + "']/../following-sibling::*//td[text()='" + optValueVal + "']"));
    }

    private SelenideElement addOptionBtn() {
        return $(xpath("//div[text()='Options']/following-sibling::*/a"));
    }

    private SelenideElement saveOptionBtn() {
        return $(xpath("//span[text()='Save option']/.."));
    }

    private SelenideElement addOptionValueBtn(String option) {
        return $(xpath("//div[text()='" + option + "']/following-sibling::*/div/a[1]"));
    }

    private SelenideElement editOptionBtn(String option) {
        return $(xpath("//div[text()='"  + option + "']/following-sibling::*/div/a[2]"));
    }

    private SelenideElement deleteOptionBtn(String option) {
        return $(xpath("//div[text()='" + option + "']/following-sibling::*/div/a[3]"));
    }

    private SelenideElement editOptionValueBtn(String optionValueName) {
        return $(xpath("//td[text()='" + optionValueName + "']/following-sibling::*[3]/a[1]"));
    }

    private SelenideElement deleteOptionValueBtn(String optionValueName) {
        return $(xpath("//td[text()='" + optionValueName + "']/following-sibling::*[3]/a[2]"));
    }

    private SelenideElement nameFld() {
        return $(xpath("//label[text()='Name']/following-sibling::*"));
    }

    private SelenideElement saveValueBtn() {
        return $(xpath("//span[text()='Save value']/.."));
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
        ElementsCollection tags = $$(xpath("//div[@class='_tags_tags__tags']/div/button"));
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
                productParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + productIndex + "]/td[2]"));
                break;
//            case "Image":
//                productParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + productIndex + "]/td[3]/img")).getAttribute("src");
//                break;
            case "Name":
                productParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + productIndex + "]/td[4]"));
                break;
            case "State":
                productParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + productIndex + "]/td[5]/div/div"));
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