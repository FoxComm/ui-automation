package pages.admin;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class SkusPage extends BasePage {

    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//


    public SelenideElement addNewSKUBtn() {
        return $(xpath("//button//span[text()='SKU']"));
    }

    public SelenideElement skuFld() {
        return $(xpath("//input[@name='code']"));
    }

    public SelenideElement titleFld() {
        return $(xpath("//input[@name='title']"));
    }

    public SelenideElement upcFld() {
        return $(xpath("//input[@name='upc']"));
    }

    //TODO: Add id in Ashes (Details, richtext)
    public SelenideElement descriptionFld() {
//        return $(By.xpath("//div[@role='textbox']/div/div"));
        return $(xpath("//div[@class='public-DraftEditor-content']"));
    }

    public SelenideElement retailPriceFld() {
        return $(xpath("//input[@name='retailPrice']"));
    }

    public SelenideElement salePriceFld() {
        return $(xpath("//input[@name='salePrice']"));
    }

    public SelenideElement unitCostFld() {
        return $(xpath("//input[@name='unitCost']"));
    }

    private SelenideElement removeStartDateBtn() {
        return $(xpath("//a[@id='fct-remove-start-date-btn']"));
    }

    private SelenideElement stateDd() {
        return $(xpath("//div[@id='state-dd']"));
    }

    public SelenideElement stateVal() {
        return $(xpath("//div[@id='state-dd-value']"));
    }

    public SelenideElement skuCodeVal() {
        return $(xpath("//h1"));
    }

    private SelenideElement labelFld() {
        return $(xpath("//input[@id='fct-field-label-fld']"));
    }

    private SelenideElement typeDd() {
        return $(xpath("//div[@id='fct-field-type-dd']"));
    }

    private SelenideElement saveAndApplyBtn() {
        return $(xpath("//button[@id='fct-modal-confirm-btn']"));
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
            click(descriptionFld());
            descriptionFld().sendKeys(Keys.chord(Keys.CONTROL, "a"));
            descriptionFld().sendKeys(description);
        }

        @Step("Set \"Retail price\" fld val to <{0}>")
        public void setRetailPrice(String price) {
            clearField(retailPriceFld());
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



    @Step("Get <{1}> parameter value of <{0}th> SKU on the list")
    public SelenideElement getSKUParamVal(String skuIndex, String paramName) {
        SelenideElement skuParamVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Code":
                skuParamVal = $(xpath("//tbody/a[" + skuIndex + "]/td[contains(@class, 'skuCode')]"));
                break;
            case "Title":
                skuParamVal = $(xpath("//tbody/a[" + skuIndex + "]/td[contains(@class, 'title')]"));
                break;
            case "Sale Price":
                skuParamVal = $(xpath("//tbody/a[" + skuIndex + "]/td[contains(@class, 'salePrice')]"));
                break;
            case "Retail Price":
                skuParamVal = $(xpath("//tbody/a[" + skuIndex + "]/td[contains(@class, 'retailPrice')]"));
                break;
        }
        return skuParamVal;
    }

    @Step("Open SKU with code <{0}>")
    public void openSKU(String skuCode) {
        waitForDataToLoad();
        shouldBeVisible(contentOnList(), "Data isn't displayed at the category view table");
        click($(xpath("//tbody/a/td[contains(@class, 'skuCode') and text()='" + skuCode + "']")));
        shouldBeVisible(saveBtn(), "Failed to open <" + skuCode + "> SKU");
    }

}
