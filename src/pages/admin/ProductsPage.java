package pages.admin;

import base.AdminBasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class ProductsPage extends AdminBasePage {

    /**
     *                                  E L E M E N T S
     */

    private SelenideElement addNewProductBtn() {
        return $(xpath("//span[text()='Product']/.."));
    }

    public SelenideElement product(String name) {
        return $(xpath("//tbody/a/td[contains(@class, 'fct-row__title') and text()='" + name + "']"));
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
        return $(xpath("//tbody[@id='fct-sku-list']/tr[" + index + "]//input[@placeholder='SKU']"));
    }

    private SelenideElement skuFld() {
        return $(xpath("//td[contains(@class, 'sku')]//input"));
    }

    private SelenideElement skuSearchView(String skuCode) {
        return $(xpath("//li[@id='fct-search-view-line__" + skuCode + "']"));
    }

    public SelenideElement stateDd() {
        return $(xpath("//div[@id='state-dd']"));
    }

    public SelenideElement stateVal() {
        return $(xpath("//div[@id='state-dd-value']"));
    }

    private SelenideElement removeStartDateBtn() {
        return $(xpath("//a[@id='fct-remove-start-date-btn']"));
    }

    public SelenideElement saveDraftBtn() {
        return $(xpath("//span[text()='Save']/.."));
    }
    //--------------------------

    //TODO: to be moved to BasePage
    private SelenideElement productsCounter() {
        return $(xpath("//*[@id='fct-total-counter-value']"));
    }

    public SelenideElement noSKUsMsg() {
        return $(xpath("//div[text()='No SKUs.']"));
    }

    private SelenideElement addSKUBtn() {
        return $(xpath("//a[@id='fct-add-sku-btn__skus-block']"));
    }

    private SelenideElement addBtn() {
        return $(xpath("//button[@id='fct-modal-confirm-btn']"));
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

    public SelenideElement skuRetailPriceFld(String index) {
        return $(xpath("//tbody[@id='fct-sku-list']/tr[" + index + "]/td[contains(@class, 'retailPrice')]//input"));
    }

    public SelenideElement skuSalePriceFld(String index) {
        return $(xpath("//tbody[@id='fct-sku-list']/tr[" + index + "]/td[contains(@class, 'salePrice')]//input"));
    }

    //TODO: rework the element locator - element should be searched by its index, not option values
    /**
     * Looks for a block with a given option value pair
     * Values should be given as they are displayed on the page in order left to right
     */
    public SelenideElement sku_byVariants(String firstValue, String secondValue) {
        return $(xpath("//td/div[text()='" + firstValue + "']/../following-sibling::*[1]/div[text()='" + secondValue + "']"));
    }

    public SelenideElement sku_byVariant(String variant) {
        return $(xpath("//*[@id='fct-sku-list']//td[contains(@class, 'variant')]/*[text()='" + variant + "']"));
    }

    private SelenideElement removeSKUBtn(String optValueVal) {
        return $(xpath("//div[text()='" + optValueVal + "']/../following-sibling::*//button[@class='fc-btn fc-btn-remove']"));
    }

    //TODO: once figured out where to add ids at SKUs box - add id to this
    private SelenideElement removeSKUBtn(String firstValue, String secondValue){
        return $(xpath("//tbody[@id='fct-sku-list']//div[text()='"+firstValue+"']/../following-sibling::*[1]/div[text()='"+secondValue+"']/../following-sibling::*//button[@class='fc-btn fc-btn-remove']"));
    }

    private SelenideElement removeSKUBtn_byIndex(String index) {
        return $(xpath("//tbody[@id='fct-sku-list']/tr[" + index + "]//button[contains(@class, 'delete')]"));
    }

    private SelenideElement removalConfirmBtn() {
        return $(xpath("//button[@id='fct-modal-confirm-btn']"));
    }

    //-------------------------- OPTIONS (a.k.a VARIANTS)

    public SelenideElement option(String option) {
        option = option.replaceAll(" ", "-").toLowerCase();
        return $(xpath("//*[@id='fct-option-box__" + option + "']"));
    }

    public SelenideElement optionValue(String option, String optValueVal) {
        return $(xpath("//div[@id='fct-option-box__" + option + "']//td[@id='fct-option-value-name__" + optValueVal + "']"));
    }

    public SelenideElement optionValue_SKUsTable(String optionVal, String optValueVal) {
        return $(xpath("//div[text()='" + optionVal + "']/../following-sibling::*//td[text()='" + optValueVal + "']"));
    }

    private SelenideElement addOptionBtn() {
        return $(xpath("//a[@id='fct-add-btn__new-option']"));
    }

    private SelenideElement saveOptionBtn() {
        return $(xpath("//*[@id='fct-modal-confirm-btn']"));
    }

    private SelenideElement addOptionValueBtn(String option) {
        return $(xpath("//*[@id='fct-option-box__" + option + "']//a[contains(@class, 'option-add-btn')]"));
    }

    private SelenideElement editOptionBtn(String option) {
        return $(xpath("//*[@id='fct-option-box__" + option + "']//a[contains(@class, 'option-edit-btn')]"));
    }

    private SelenideElement deleteOptionBtn(String option) {
        return $(xpath("//*[@id='fct-option-box__" + option + "']//a[contains(@class, 'option-delete-btn')]"));
    }

    private SelenideElement editOptionValueBtn(String optionValueName) {
        return $(xpath("//a[@id='fct-value-edit-btn__" + optionValueName + "']"));
    }

    private SelenideElement deleteOptionValueBtn(String optionValueName) {
        return $(xpath("//a[@id='fct-value-delete-btn__" + optionValueName + "']"));
    }

    private SelenideElement optionNameFld() {
        return $(xpath("//input[@id='fct-option-name-fld']"));
    }

    private SelenideElement valueNameField() {
        return $(xpath("//input[@id='fct-value-name-fld']"));
    }

    private SelenideElement saveValueBtn() {
        return $(xpath("//button[@id='fct-modal-confirm-btn']"));
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

    @Step("Get <{1}> parameter val of <{0}>th product on the list")
    public SelenideElement getProductParamVal(String productIndex, String paramName) {
        SelenideElement paramVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Product ID":
                paramVal = $(xpath("//tbody/a[" + productIndex + "]//td[contains(@class, 'productId')]"));
                break;
//            case "Image":
//                paramVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + productIndex + "]/td[3]/img")).getAttribute("src");
//                break;
            case "Title":
                paramVal = $(xpath("//tbody/a[" + productIndex + "]//td[contains(@class, 'title')]"));
                break;
            case "State":
                paramVal = $(xpath("//tbody/a[" + productIndex + "]//td[contains(@class, 'state')]//div[contains(text(), 'ctive')]"));
                break;

        }
        return paramVal;
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
    public void fillOutProductForm(String title, String SKU, String retailPrice, String salePrice, String tagVal, String state) {
        setTitle(title);
        setFieldVal( descriptionFld(), "The best thing to buy in 2016!" );
        addNewSKU(SKU, retailPrice, salePrice);
        setState(state);
        addTag(tagVal);
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
            String lastSKUIndex = String.valueOf($$(xpath("//tbody[@id='fct-sku-list']/tr")).size());
            setFieldVal(skuFld(), SKU);
            click(skuSearchView(SKU));
            setFieldVal(skuRetailPriceFld(lastSKUIndex), retailPrice);
            setFieldVal(skuSalePriceFld(lastSKUIndex), salePrice);
        }

        @Step("Set \"Retail Price\" fld val of <{0}th> SKU to <{1}>")
        public void setRetailPrice(String skuIndex, String price) {
            clearField(skuRetailPriceFld(skuIndex));
            setFieldVal(skuRetailPriceFld(skuIndex), price);
        }

        @Step("Set \"Sale Price\" fld val of <{0}th> SKU to <{1}>")
        public void setSalePrice(String skuIndex, String price) {
            clearField(skuSalePriceFld(skuIndex));
            setFieldVal(skuSalePriceFld(skuIndex), price);
        }

    @Step("Add <{0}> option")
    public void addOption(String optionVal) {
        clickAddBtn_option();
        setOptionName(optionVal);
        clickSaveBtn_modal();
    }

        @Step("Click \"+\" btn at \"Options\" block")
        public void clickAddBtn_option() {
            click(addOptionBtn());
        }

        @Step("Set \"Name\" to <{0}>")
        public void setOptionName(String nameVal) {
            setFieldVal(optionNameFld(), nameVal);
        }

    @Step("Add value <{1}> to the option <{0}>")
    public void addOptionValue(String optionVal, String nameVal) {
        clickAddBtn_optionValue(optionVal);
        setValueName(nameVal);
        clickSaveBtn_modal();
    }

        @Step("Click \"+\" btn at <{0}> option")
        private void clickAddBtn_optionValue(String optionVal) {
            click(addOptionValueBtn(optionVal));
        }

        @Step("Set \"Name\" to <{0}>")
        public void setValueName(String nameVal) {
            setFieldVal(valueNameField(), nameVal);
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
        setValueName(newValueVal);
        clickSaveBtn_modal();
    }

    @Step("Remove SKU with an option value combo <{0}> & <{1}>")
    public void removeSKU(String firstValue, String secondValue) {
        click(removeSKUBtn(firstValue, secondValue));
        click(removalConfirmBtn());
    }

    @Step("Remove {0}th SKU on the list")
    public void removeSKU(String index) {
        click(removeSKUBtn_byIndex(index));
        click(removalConfirmBtn());
    }

    @Step("Re-add SKU for option values combo <{0} & {1}>")
    public void reAddSKU(String firstValue, String secondValue) {
        String availableOption = firstValue + "-" + secondValue;
        click(addSKUBtn());
        jsClick(availableOptionChkbx(availableOption));
        click(addBtn());
    }

}