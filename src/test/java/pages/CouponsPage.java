package pages;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class CouponsPage extends BasePage {

    //--------------------------------- ELEMENTS -----------------------------//

    public SelenideElement coupon(String nameOrId) {
        return $(xpath("//tbody[@class='fc-table-body']/a[1]/td[text()='" + nameOrId + "']"));
    }

    public SelenideElement addNewCoupon() {
        return $(xpath("//span[text()='Coupon']/.."));
    }

    public SelenideElement nameFld() {
        return $(xpath("//input[@name='name']"));
    }

    // may fail, as well as other rich text fields
    public SelenideElement storefrontNameFld() {
        return $(xpath("//div[@class='fc-object-form']/div[2]/div[1]/div[3]/div/div/div"));
    }

    public SelenideElement descriptionFld() {
        return $(xpath("//div[@class='fc-object-form']/div[3]/div[1]/div[3]/div/div/div"));
    }

    public SelenideElement detailsFld() {
        return $(xpath("//div[@class='fc-object-form']/div[4]/div[1]/div[3]/div/div/div"));
    }

    private SelenideElement promotionDd() {
        return $(xpath("//input[@name='promotion']/../following-sibling::*"));
    }

    private SelenideElement promotionListVal(String promotionId) {
        return $(xpath("//span[text()='" + promotionId + "']/.."));
    }

    public SelenideElement singleCodeRbtn() {
        return $(xpath("//input[@id='singleCouponCodeRadio']"));
    }

    public SelenideElement bulkGenerateCodesBrtn() {
        return $(xpath("//input[@id='bulkCouponCodeRadio']"));
    }

    public SelenideElement singleCodeFld() {
        return $(xpath("//input[@name='singleCode']"));
    }

    public SelenideElement saveBtn() {
        return $(xpath("//span[text()='Save']/.."));
    }

    public SelenideElement couponIdVal() {
        return $(xpath("//div[@class='fc-breadcrumbs']/ul/li[5]/a"));
    }

    private SelenideElement qtyDecrBtn() {
        return $(xpath("//input[@id='codesQuantity']/preceding-sibling::*/button"));
    }

    private SelenideElement qtyIncrBtn() {
        return $(xpath("//input[@id='codesQuantity']/following-sibling::*/button"));
    }

    private SelenideElement codePrefixFld() {
        return $(xpath("//input[@name='codesPrefix']"));
    }

    private SelenideElement codeLengthDecrBtn() {
        return $(xpath("//input[@id='codesLength']/preceding-sibling::*/button"));
    }

    private SelenideElement codeLengthIncrBtn() {
        return $(xpath("//input[@id='codesLength']/following-sibling::*/button"));
    }

    private SelenideElement generateCodesBtn() {
        return $(xpath("//span[text()='Generate Codes']/.."));
    }

    private SelenideElement submitBtn() {
        return $(xpath("//div[@class='fc-modal-footer']/button"));
    }

    public SelenideElement couponCodesTab() {
        return $(xpath("//a[text()='Coupon Codes']/.."));
    }

    private SelenideElement stateDd() {
        return $(xpath("//div[@class='fc-product-state']//following-sibling::*/div[2]/button"));
    }

    private SelenideElement stateListVal(String state) {
        return $(xpath("//li[text()='" + state + "']"));
    }

    private SelenideElement stateVal() {
        return $(xpath("//div[@class='fc-product-state']/div[2]/div[2]/div"));
    }

    private SelenideElement deleteStartDateBtn() {
        return $(xpath("//div[text()='Start']/following-sibling::*/div[2]/a"));
    }


    //--------------------------------- HELPERS -----------------------------//

    @Step("Select promotion with id <{0}>")
    public void setPromotion(String promotionId) {
        click( promotionDd() );
        click( promotionListVal(promotionId) );
    }

    @Step("Set coupon state to '{0}'")
    public void setState(String state) {

        switch (state) {

            case "Inactive":
                click( deleteStartDateBtn() );
                break;

            case "Active":
                click( stateDd() );
                click( stateListVal(state) );
                break;

        }

    }

    @Step("Bulk generate codes with params Qty: <{0}>, Code prefix: <{1}>, Code length: <{2}>.")
    public void bulkGenerateCodes(int quantity, String codePrefix, int codeLength) {
        for (int i = 0; i < quantity; i++) {
            click( qtyIncrBtn() );
        }
        setFieldVal( codePrefixFld(), codePrefix );
        for (int i = 0; i < codeLength; i++) {
            click( codeLengthIncrBtn() );
        }
        click( generateCodesBtn() );
        click( submitBtn() );       // locator is not defined

    }

    @Step("Assert that codes are displayed on the 'Coupon Codes' tab.")
    public void assertCodesGenerated(int codesQty) {

        waitForDataToLoad();
        $(xpath("//div[text()='No coupon codes found.']")).shouldNotBe(visible
                .because("No coupon codes is displayed on the list."));
        listOfCodes().shouldHave(size(codesQty));
        //.because("There are less codes on the list than it should.")

    }

    private ElementsCollection listOfCodes() {
        return $$(xpath("//tbody[@class='fc-table-body']/tr/td[3]"));
    }

    @Step("Assert that all changes to coupon were saved")
    public void assertCouponIsEdited(String name, String storefrontName, String description, String details, String state) {

        nameFld().shouldHave(text(name)
                .because("'Name' has failed to get edited."));

        storefrontNameFld().shouldHave(text(storefrontName)
                .because("'Storefront Name' has failed to get edited."));

        descriptionFld().shouldHave(text(description)
                .because("'Description' has failed to get edited."));

        detailsFld().shouldHave(text(details)
                .because("'Details' has failed to get edited."));

        stateVal().shouldHave(text(state)
                .because("'State' has failed to get edited."));

    }

    @Step("Get '{1}' parameter value of {0}th coupon on the list")
    public SelenideElement getCouponParamVal(String couponIndex, String paramName) {

        SelenideElement couponParamVal = null;
        waitForDataToLoad();

        switch (paramName) {
            case "Name":
                couponParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + couponIndex + "]/td[2]"));
                break;
            case "Storefront Name":
                couponParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + couponIndex + "]/td[3]"));
                break;
            case "Code":
                couponParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + couponIndex + "]/td[4]"));
                break;
            case "Total Uses":
                couponParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + couponIndex + "]/td[5]"));
                break;
            case "Current Carts":
                couponParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + couponIndex + "]/td[6]"));
                break;
            case "Date/Time Created":
                couponParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + couponIndex + "]/td[7]/time"));
                break;
            case "State":
                couponParamVal = $(xpath("//tbody[@class='fc-table-body']/a[" + couponIndex + "]/td[8]/div/div"));
                break;

        }

        return couponParamVal;

    }

}
