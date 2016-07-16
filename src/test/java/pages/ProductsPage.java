package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.ENTER;
import static org.testng.Assert.assertTrue;

public class ProductsPage extends BasePage {

    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement addNewProduct() {
        return $(By.xpath("//span[text()='Product']/.."));
    }

    //-------------------------- NEW PRODUCT
    public SelenideElement titleFld() {
        return $(By.xpath("//input[@name='title']"));
    }

    public SelenideElement descriptionFld() {
        return $(By.xpath("//div[@class='public-DraftEditor-content']"));
    }

    public SelenideElement sku() {
        return $(By.xpath("//tbody/tr[1]/td[2]/div/input"));
    }

    public SelenideElement retailPriceFld() {
        return $(By.xpath("//tbody/tr[1]/td[3]/div/div/input"));
    }

    public SelenideElement salePriceFld() {
        return $(By.xpath("//tbody/tr[1]/td[4]/div/div/input"));
    }

    public SelenideElement stateDd() {
        return $(By.xpath("//div[@class='fc-product-state']/div[2]/div[2]/button"));
    }

    public SelenideElement stateListVal(String state) {
        return $(By.xpath("//li[text()='" + state + "']"));
    }

    public String stateVal() {
        SelenideElement state = $(By.xpath("//div[@class='fc-product-state']/div[2]/div[2]/div"));
        return state.getText();
    }

    private SelenideElement removeStartDateBtn() {
        return $(By.xpath("//div[text()='Start']/following-sibling::*/div[2]/a"));
    }

    public SelenideElement saveDraftBtn() {
        return $(By.xpath("//span[text()='Save Draft']/.."));
    }
    //--------------------------

    private SelenideElement addTagsBtn() {
        return $(By.xpath("//div[text()='Tags']/following-sibling::*"));
    }

    private SelenideElement tagFld() {
        return $(By.xpath("//input[@placeholder='Separate tags with a comma']"));
    }

    public SelenideElement removeTagBtn(String index) {
        // define only btn on the first tag in line
        return $(By.xpath("//div[@class='_tags_tags__tags']/div[" + index + "]/button"));
    }

    private SelenideElement searchFld() {
        return $(By.xpath("//input[@placeholder='filter or keyword search']"));
    }

    private SelenideElement productsCounter() {
        return $(By.xpath("//span[@class='fc-section-title__subtitle fc-light']/span"));
    }

    public SelenideElement noSKUsMsg() {
        return $(By.xpath("//div[text()='No SKUs.']"));
    }

    public SelenideElement productId() {
        return $(By.xpath("//header/div/ul/li[5]"));
    }

    private SelenideElement skuName() {
        return $(By.xpath("//tr[@class='fc-table-tr']/td[2]/div"));
    }



    //------------------------------- HELPERS --------------------------------//

    @Step("Remove all filters from the search field")
    public void clearSearchFld() {
        waitForDataToLoad();
        List<SelenideElement> searchFiltersList = $$(By.xpath("//a[@class='fc-pilled-input__pill-close']"));
        for(SelenideElement filterCloseBtn : searchFiltersList) {
            click( filterCloseBtn );
        }
    }

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

    @Step("Open product with name <{0}>.")
    public void openProduct(String productName) {
        click( findProductOnList(productName) );
    }

    @Step("Find on the list a product with name <{0}>")
    private SelenideElement findProductOnList(String productName) {

        waitForDataToLoad();
        List<SelenideElement> productsList = $$(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a/td[4]"));
        SelenideElement productToClick = null;
        printSEList(productsList);

        for(SelenideElement product : productsList) {

            String listProductName = product.text();
            if (listProductName.equals(productName)) {
                productToClick = product;
            }

        }

        assertTrue( productToClick != null, "Requested product isn't displayed on the list.");
        return productToClick;

    }

    @Step("Add tag <{0}>")
    public void addTag(String tagVal) {
        click( addTagsBtn() );
        setFieldVal( tagFld(), tagVal );
        tagFld().pressEnter();
    }

    @Step("Clear all tags from product")
    public void clearTags() {
        List<SelenideElement> tags = $$(By.xpath("//div[@class='_tags_tags__tags']/div/button"));
        System.out.println(tags.size());
        for(int i = 0; i < tags.size(); i++) {
            click( removeTagBtn("1") );
            sleep(200);
        }
    }

    @Step("Get '{1}' parameter value of {0}th product on the list")
    public String getProductParamVal(String productIndex, String paramName) {
        String productParamVal = "";
        waitForDataToLoad();
        switch (paramName) {
            case "Product ID":
                productParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + productIndex + "]/td[2]")).getText();
                break;
//            case "Image":
//                productParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + productIndex + "]/td[3]/div/div")).getText();
//                break;
            case "Name":
                productParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + productIndex + "]/td[4]")).getText();
                break;
            case "State":
                productParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + productIndex + "]/td[5]/div/div")).getText();
                break;

        }
        return productParamVal;
    }

    private void selectLine(int index) {
        for (int i = 0; i < index; i++) {
            searchFld().sendKeys(Keys.ARROW_DOWN);
        }
        hitEnter();
    }

    private void hitEnter() {
        searchFld().sendKeys(ENTER);
        sleep(200);
    }

    @Step("Create a search filter {0} : {1} : {2}")
    public void addFilter(String firstStatement, String secondStatement, String thirdStatement) {

        searchFld().click();

        switch(firstStatement)
        {
            case "Product":

                switch(secondStatement)
                {
                    case "ID":
                        selectLine(1);
                        waitForDataToLoad();
//                        hitEnter();
                        searchFld().sendKeys(thirdStatement);
                        hitEnter();
                        waitForDataToLoad();
                        break;
                    case "Name":
                        selectLine(2);
//                        hitEnter();
                        searchFld().sendKeys(thirdStatement);
                        hitEnter();
                        waitForDataToLoad();
                        break;
                }
                break;

        }
        // resets searchFld focus (works as a hack)
        takeFocusAway();

    }

    private void takeFocusAway() {
        productsCounter().click();
        sleep(500);
    }

    @Step("Fill out the 'New Product' form - Title: <{0}>, SKU: <{1}>, Retail Price: <{2}>, Sale Price: <{3}>, State: <{4}>")
    public void fillOutProductForm(String productTitle, String SKU, String retailPrice, String salePrice, String tagVal, String state) {

        click( addNewProduct() );
        setFieldVal( titleFld(), productTitle );
        setFieldVal( descriptionFld(), "The best thing to buy in 2016!" );
        setFieldVal( sku(), SKU );
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

        try{
            skuName().shouldBe(visible);
        } catch(NoSuchElementException nsee) {
            assertTrue( noSKUsMsg().is(visible),
                    "'No SKUs.' msg is displayed - SKU wasn't applied, product won't be displayed on storefront." );
        }

    }

}
