package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.testng.Assert.assertTrue;

public class SkusPage extends BasePage {

    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement addNewSKUBtn() {
        return $(By.xpath("//button[@class='fc-btn fc-btn-primary']"));
    }

    public SelenideElement skuFld() {
        return $(By.xpath("//input[@name='code']"));
    }

    public SelenideElement titleFld() {
        return $(By.xpath("//input[@name='title']"));
    }

    public SelenideElement upcFld() {
        return $(By.xpath("//input[@name='upc']"));
    }

    public SelenideElement descriptionFld() {
        return $(By.xpath("//div[@id='foxcom']/div/div[1]/main/div/div[3]/div/div/div[1]/div[1]/div/div/div[4]/div/div[3]/div/div/div"));
    }

    public SelenideElement retailPriceFld() {
        return $(By.xpath("//input[@name='retailPrice']"));
    }

    public SelenideElement salePriceFld() {
        return $(By.xpath("//input[@name='salePrice']"));
    }

    public SelenideElement unitCostFld() {
        return $(By.xpath("//input[@name='unitCost']"));
    }

    private SelenideElement removeStartDateBtn() {
        return $(By.xpath("//a[@class='fc-date-time-picker__close']"));
    }

    private SelenideElement stateDd() {
        return $(By.xpath("//div[text()='State']/../following-sibling::*/div[2]/button"));
    }

    private SelenideElement stateListVal(String state) {
        return $(By.xpath("//li[text()='" + state + "']"));
    }

    public String stateVal() {
        SelenideElement stateVal = $(By.xpath("//div[text()='State']/../following-sibling::*[1]/div[2]/div"));
        return stateVal.text();
    }

    public String skuCodeVal() {
        SelenideElement skuCode = $(By.xpath("//div[@class='fc-breadcrumbs']/ul/li[5]/a"));
        return skuCode.text();
    }

    private SelenideElement addCustomPropBtn() {
        return $(By.xpath("//div[text()='Custom Property']/a"));
    }

    private SelenideElement labelFld() {
        return $(By.xpath("//input[@name='field']"));
    }

    private SelenideElement typeDd() {
        return $(By.xpath("//label[text()='Field Type']/following-sibling::*/div[2]/button"));
    }

    private SelenideElement typeVal(String val) {
        return $(By.xpath("//li[text()='" + val + "']"));
    }

    private SelenideElement saveAndApplyBtn() {
        return $(By.xpath("//span[text()='Save and Apply']/.."));
    }

    public SelenideElement customTextFld(String label) {
        return $(By.xpath("//input[@name='" + label.toLowerCase() + "']"));
    }

    public SelenideElement customRichTextFld() {
//        return $(By.xpath("//div[text()='" + label.toLowerCase() + "']/following-sibling::*[2]/div/div/div"));
        return $(By.xpath("//div[@class='fc-object-form']/div[5]/div/div[3]/div/div/div"));
    }

    public String getRichTextVal(String label) {
        SelenideElement richText = $(By.xpath("//div[text()='" + label.toLowerCase() + "']/following-sibling::*[2]/div/div/div/div/div/div/span/span"));
        return richText.text();
    }

    public SelenideElement customPriceFld(String label) {
        return $(By.xpath("//input[@name='" + label.toLowerCase() + "']"));
    }



    //------------------------------------------------------------------------//
    //------------------------------- HELPERS --------------------------------//

    @Step("Set SKU state to <{0}>")
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

    @Step("Fill out new SKU form.")
    public void createNewSKU(String id, String state) {

        setFieldVal( skuFld(), "SKU-" + id );
        setFieldVal( titleFld(), "Test Title" );
        setFieldVal( upcFld(), "Test UPC" );
        descriptionFld().val("Just another test SKU.");
        setFieldVal( retailPriceFld(), "50.00" );
        setFieldVal( salePriceFld(), "32.00" );
        setFieldVal( unitCostFld(), "32.00" );
        setState(state);

    }

    @Step("Add custom property of <{0}> type with <{1}> label")
    public void addCustomProp(String type, String label) {

        click( addCustomPropBtn() );
        click( typeDd() );
        click( typeVal(type) );
        setFieldVal( labelFld(), label );
        click( saveAndApplyBtn() );
        saveAndApplyBtn().shouldNot(exist);

    }

    @Step("Get '{1}' parameter value of {0}th SKU on the list")
    public String getSKUParamVal(String skuIndex, String paramName) {
        String skuParamVal = "";
        waitForDataToLoad();
        switch (paramName) {
            case "Code":
                skuParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + skuIndex + "]/td[2]")).getText();
                break;
            case "Title":
                skuParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + skuIndex + "]/td[3]")).getText();
                break;
            case "Price":
                String skuPrice = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + skuIndex + "]/td[4]/span")).text();
                skuParamVal = skuPrice.substring(1, skuPrice.length());
                break;

        }
        return skuParamVal;
    }

    @Step("Open SKU with code <{0}>.")
    public void openSKU(String skuCode) {
        click( findSKUOnList(skuCode) );
    }

    @Step("Find SKU with code <{0}> on the list.")
    private SelenideElement findSKUOnList(String skuCode) {

        waitForDataToLoad();
        List<SelenideElement> skusList = $$(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a/td[2]"));
        SelenideElement skuToClick = null;

        for(SelenideElement sku : skusList) {

            String skuCodeVal = sku.text();
            if (skuCodeVal.equals(skuCode)) {
                skuToClick = sku;
            }

        }

        assertTrue( skuToClick != null, "Requested coupon isn't displayed on the list.");
        return skuToClick;

    }

}
