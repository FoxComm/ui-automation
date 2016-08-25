package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
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

    public String stateVal() {
        SelenideElement state = $(xpath("//div[@class='fc-product-state']/div[2]/div[2]/div"));
        return state.getText();
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

    public SelenideElement productId() {
        return $(xpath("//header/div/ul/li[5]"));
    }

    private SelenideElement skuName() {
        return $(xpath("//tr[@class='fc-table-tr']/td[2]/div"));
    }



    //------------------------------- HELPERS --------------------------------//

//    @Step("Remove all filters from the search field")
//    public void clearSearchFld() {
//        waitForDataToLoad();
//        List<SelenideElement> searchFiltersList = $$(By.xpath("//a[@class='fc-pilled-input__pill-close']"));
//        for(SelenideElement filterCloseBtn : searchFiltersList) {
//            click( filterCloseBtn );
//        }
//    }

    @Step("Set product state to '{0}'")
    public void setState(String state) {

        switch (state) {

            case "Inactive":
                click( removeStartDateBtn() );
                break;

            case "Active":
                click( stateDd() );
                click( stateListVal(state) );
                break;

        }

    }

    @Step("Clear all tags from product")
    public void clearTags() {
        List<SelenideElement> tags = $$(xpath("//div[@class='_tags_tags__tags']/div/button"));
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
//    public void addFilter(String firstStatement, String secondStatement, String thirdStatement) {
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

    @Step("Fill out the 'New Product' form - Title: <{0}>, SKU: <{1}>, Retail Price: <{2}>, Sale Price: <{3}>, State: <{4}>")
    public void fillOutProductForm(String productTitle, String SKU, String retailPrice, String salePrice, String tagVal, String state) {

        click( addNewProduct() );
        setFieldVal( titleFld(), productTitle );
        setFieldVal( descriptionFld(), "The best thing to buy in 2016!" );
        setFieldVal( sku(), SKU );
        click( skuSearchView(SKU) );
        setFieldVal( retailPriceFld(), retailPrice );
        setFieldVal( salePriceFld(), salePrice );
        setState( state );
        addTag( tagVal );
        click( saveDraftBtn() );
        saveDraftBtn().shouldBe(enabled);
        productId().shouldNotHave(text("new"));

    }

    @Step("Assert that SKU is applied to the product")
    public void assertSKUApplied() {

        skuName().shouldBe(visible
                .because("'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront."));

    }

}
