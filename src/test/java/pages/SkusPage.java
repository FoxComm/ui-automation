package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

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
//        return $(By.xpath("//div[@id='foxcom']/div/div[1]/main/div/div[3]/div/div/div[1]/div[1]/div/div/div[4]/div/div[3]/div/div/div"));
        return $(By.xpath("//div[@role='textbox']/div"));
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

    public SelenideElement stateVal() {
        return $(By.xpath("//div[text()='State']/../following-sibling::*[1]/div[2]/div"));
    }

    public SelenideElement skuCodeVal() {
        return $(By.xpath("//div[@class='fc-breadcrumbs']/ul/li[5]/a"));
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

    public SelenideElement customRichTextFld(String title) {
        return $(By.xpath("//div[text()='" + title + "']/following-sibling::*[2]/div/div/div"));
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

    @Step("Click \"Create New SKU\" btn")
    public void clickAddNewSKU() {
        click(addNewSKUBtn());
    }

    @Step("Set SKU state to <{0}>")
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

    @Step("Fill out new SKU form")
    public void createNewSKU(String id, String state) {
        setSKUCode( "SKU-" + id );
        setTitle("Test Title");
        setUpc("Test UPC");
        setDescription("Just another test SKU.");
        setRetailPrice("50.00");
        setSalePrice("32.00");
        setUnitCost("32.00");
        setState(state);
    }

        @Step("Set \"SKU\" fld val to <{0}>")
        public void setSKUCode(String code) {
            setFieldVal(skuFld(), code);
        }

        @Step("Set \"Title\" fld val to <{0}>")
        public void setTitle(String title) {
            setFieldVal(titleFld(), title);
        }

        @Step("Set \"UPC\" fld val to <{0}>")
        public void setUpc(String upc) {
            setFieldVal(upcFld(), upc);
        }

        @Step("Set \"Description\" to <{0}>")
        public void setDescription(String description) {
            setFieldVal(descriptionFld(), description);
        }

        @Step("Set \"Retail price\" fld val to <{0}>")
        public void setRetailPrice(String price) {
            setFieldVal(retailPriceFld(), price);
        }

        @Step("Set \"Sale Price\" fld val to <{0}>")
        public void setSalePrice(String price) {
            setFieldVal(salePriceFld(), price);
        }

        @Step("Set \"Unit Cost\" fld val to <{0}>")
        public void setUnitCost(String price) {
            setFieldVal(unitCostFld(), price);
        }

    @Step("Add custom property of <{0}> type with <{1}> label")
    public void addCustomProp(String type, String label) {
        click(addCustomPropBtn());
        setDdVal(typeDd(), type);
        setFieldVal(labelFld(), label);
        click(saveAndApplyBtn());
        shouldNotExist(saveAndApplyBtn(), "\"Save And Apply\" btn exists (it sholdn't)");
    }

        @Step("Set val of custom text fld <{0}> to <{1}>")
        public void setCustomProp_text(String fldTitle, String value) {
            setFieldVal(customTextFld(fldTitle), value);
        }

        @Step("Set val of custom rich text fld <{0}> to <{1}>")
        public void setCustomProp_richText(String title, String value) {
            setFieldVal(customRichTextFld(title), value);
        }

    @Step("Get <{1}> parameter value of <{0}th> SKU on the list")
    public SelenideElement getSKUParamVal(String skuIndex, String paramName) {
        SelenideElement skuParamVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Code":
                skuParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + skuIndex + "]/td[2]"));
                break;
            case "Title":
                skuParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + skuIndex + "]/td[3]"));
                break;
            case "Sale Price":
                skuParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + skuIndex + "]/td[4]/span"));
                break;
            case "Retail Price":
                skuParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + skuIndex + "]/td[5]/span"));
                break;
        }
        return skuParamVal;
    }

    @Step("Open SKU with code <{0}>")
    public void openSKU(String skuCode) {
        click(findSKUOnList(skuCode));
    }

    @Step("Find SKU with code <{0}> on the list.")
    private SelenideElement findSKUOnList(String skuCode) {
        waitForDataToLoad();
        List<SelenideElement> skusList = $$(By.xpath("//tbody[@class='fc-table-body']/a/td[2]"));
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
