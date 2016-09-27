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

    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement addNewProduct() {
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

    public SelenideElement sku() {
        return $(xpath("//input[@placeholder='SKU']"));
    }

    private SelenideElement skuSearchView(String skuCode) {
        return $(xpath("//div[contains(@class, 'sku-cell')]/ul/li/strong[text()='" + skuCode + "']"));
    }

    public SelenideElement retailPriceFld() {
        return $(xpath("//tbody[@class='fc-table-body']/tr/td[3]/div/div/input"));
    }

    public SelenideElement salePriceFld() {
        return $(xpath("//tbody[@class='fc-table-body']/tr/td[4]/div/div/input"));
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

    public SelenideElement skuName() {
        return $(xpath("//tr[@class='fc-table-tr']/td[2]/div"));
    }



    //------------------------------- HELPERS --------------------------------//

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
        click(addNewProduct());
    }

    @Step("Fill out the 'New Product' form - Title: <{0}>, SKU: <{1}>, Retail Price: <{2}>, Sale Price: <{3}>, State: <{4}>")
    public void createProduct(String title, String SKU, String retailPrice, String salePrice, String tagVal, String state) {
        setTitle(title);
        setFieldVal( descriptionFld(), "The best thing to buy in 2016!" );
        addSKU(SKU, retailPrice, salePrice);
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
        private void addSKU(String SKU, String retailPrice, String salePrice) {
            click(skuSearchView(SKU));
            click(skuSearchView(SKU));
            setFieldVal(retailPriceFld(), retailPrice);
            setFieldVal(salePriceFld(), salePrice);
        }

        @Step("Set \"Retail Price\" fld val to <{0}>")
        public void setRetailPrice(String price) {
            setFieldVal(retailPriceFld(), price);
        }

        @Step("Set \"Retail Price\" fld val to <{0}>")
        public void setSalePrice(String price) {
            setFieldVal(salePriceFld(), price);
        }

    @Step("Assert that SKU is applied to the product")
    public void assertSKUApplied() {
        shouldBeVisible(skuName(), "\"No SKUs.\" msg is displayed - SKU wasn't applied, product won't be displayed on storefront.");
    }

}
