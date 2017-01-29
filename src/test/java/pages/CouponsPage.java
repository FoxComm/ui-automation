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

    private SelenideElement coupon(String id) {
        return $(xpath("//a[@href='/coupons/" + id + "']"));
    }

    private SelenideElement addNewCouponBtn() {
        return $(xpath("//span[text()='Coupon']/.."));
    }

    public SelenideElement nameFld() {
        return $(xpath("//input[@name='name']"));
    }

    //TODO: add IDs to all rich text fileds on this page
    // may fail, as well as other rich text fields
    public SelenideElement storefrontNameFld() {
        return $(xpath("//div[@class='fc-object-form']/div[2]/div[1]/div[3]/div/div/div"));
    }

    public SelenideElement descriptionFld() {
        return $(xpath("//textarea[@name='description']"));
    }

    public SelenideElement detailsFld() {
        return $(xpath("//div[@class='fc-object-form']/div[4]/div[1]/div[3]/div/div/div"));
    }

    private SelenideElement promotionDd() {
        return $(xpath("//*[@id='promotionSelector']"));
    }

    //TODO: Remove if locator //*[text()='" + ddValue + "'] at setDdVal() works
//    private SelenideElement promotionListVal(String promotionId) {
//        return $(xpath("//span[text()='" + promotionId + "']"));
//    }

    private SelenideElement generateCodesRadio(String type) {
        return $(xpath("//input[@id='" + type + "CouponCodeRadio']"));
    }

    private SelenideElement singleCodeFld() {
        return $(xpath("//input[@name='singleCode']"));
    }

    public SelenideElement saveBtn() {
        return $(xpath("//span[text()='Save']/.."));
    }

    public SelenideElement couponIdVal() {
        return $(xpath("//a[@id='item-id']"));
    }

    private SelenideElement qtyDecrBtn() {
        return $(xpath("//div[@id='code-quantity-counter']//button[contains(@class, 'decrement')]"));
    }

    private SelenideElement qtyIncrBtn() {
        return $(xpath("//div[@id='code-quantity-counter']//button[contains(@class, 'increment')]"));
    }

    private SelenideElement codePrefixFld() {
        return $(xpath("//input[@name='codesPrefix']"));
    }

    private SelenideElement codeLengthDecrBtn() {
        return $(xpath("//div[@id='code-character-length-counter']//button[contains(@class, 'decrement')]"));
    }

    private SelenideElement codeLengthIncrBtn() {
        return $(xpath("//div[@id='code-character-length-counter']//button[contains(@class, 'increment')]"));
    }

    private SelenideElement generateCodesBtn() {
        return $(xpath("//span[text()='Generate Codes']/.."));
    }

    private SelenideElement submitBtn() {
        return $(xpath("//button[@id='modal-confirm-btn']"));
    }

    private SelenideElement stateDd() {
        return $(xpath("//div[@class='fc-product-state']//following-sibling::*/div[2]/button"));
    }

    private SelenideElement stateVal() {
        return $(xpath("//div[@id='state-dd']//div[@class='fc-dropdown__value']"));
    }

    private SelenideElement deleteStartDateBtn() {
        return $(xpath("//div[@id='state-dd']"));
    }

    public SelenideElement couponCodesTab() {
        return $(xpath("//a[text()='Coupon Codes']/.."));
    }


    //--------------------------------- HELPERS -----------------------------//

    @Step("Set \"Name\" fld val to <{0}>")
    public void setCouponName(String name) {
        setFieldVal(nameFld(), name);
    }

    @Step("Click \"Create New Promotion\" btn")
    public void clickAddNewCouponBtn() {
        click(addNewCouponBtn());
    }

    @Step("Set \"Storefront Name\" fld val to <{0}>")
    public void setStorefrontName(String name) {
        setFieldVal(storefrontNameFld(), name);
    }

    @Step("Set \"Description\" rich text fld val to <{0}>")
    public void setDescription(String description) {
        setFieldVal(descriptionFld(), description);
    }

    @Step("Set \"Details\" rich text fld val to <{0}>")
    public void setDetails(String details) {
        setFieldVal(detailsFld(), details);
    }

    @Step("Select promotion with id <{0}>")
    public void setPromotion(String promotionId) {
//        click(promotionDd());
//        click(promotionListVal(promotionId));
        setDdVal(promotionDd(), promotionId);
    }

    @Step("Set \"Code\" fld val to <{0}>")
    public void setCode(String code) {
        setFieldVal(singleCodeFld(), code);
    }

    @Step("Set coupon state to <{0}>")
    public void setState(String state) {
        switch (state) {
            case "Inactive":
                click(deleteStartDateBtn());
                break;
            case "Active":
                setDdVal(stateDd(), state);
                break;
        }
    }

    @Step("Generate single code with params <Code prefix:{1}>, <Code length:{2}>")
    public void generateCodes_single(String code) {
        selectCodeGenRadio("single");
        setCode(code);
    }

    @Step("Bulk generate codes with params <QTY:{0}>, <Code prefix:{1}>, <Code length:{2}>")
    public void generateCodes_bulk(int quantity, String prefix, int codeLength) {
        selectCodeGenRadio("bulk");
        for (int i = 0; i < quantity; i++) {
            click(qtyIncrBtn());
        }
        setPrefix(prefix);
        for (int i = 0; i < codeLength; i++) {
            click(codeLengthIncrBtn());
        }
        clickGenerateCodesBtn();
        clickSubmit();
    }

        @Step("Select <{0}> coupon code(s) generation radio")
        private void selectCodeGenRadio(String type) {
            jsClick(generateCodesRadio(type));
        }

        @Step("Set \"Prefix\" fld val to <{0}>")
        private void setPrefix(String prefix) {
            setFieldVal(codePrefixFld(), prefix);
        }

        @Step("Click \"Generate Codes\" btn")
        private void clickGenerateCodesBtn() {
            click(generateCodesBtn());
        }

        @Step("Click \"Submit\" btn")
        private void clickSubmit() {
            click(submitBtn());
        }

    @Step("Assert that codes are displayed on the \"Coupon Codes\" tab")
    public void assertCodesGenerated(int codesQty) {

        waitForDataToLoad();
        $(xpath("//div[text()='No coupon codes found.']")).shouldNotBe(visible
                .because("No coupon codes is displayed on the list"));
        listOfCodes().shouldHave(size(codesQty));
        //.because("There are less codes on the list than it should")

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

    @Step("Get <{1}> parameter value of <{0}th> coupon on the list")
    public SelenideElement getCouponParamVal(String couponIndex, String paramName) {
        SelenideElement couponParamVal = null;
        waitForDataToLoad();

        switch (paramName) {
            case "Name":
                couponParamVal = $(xpath("//tbody/a[" + couponIndex + "]/td[contains(@class, 'fct-name')]"));
                break;
            case "Storefront Name":
                couponParamVal = $(xpath("//tbody/a[" + couponIndex + "]/td[contains(@class, 'fct-storefrontName')]"));
                break;
            case "Codes":
                couponParamVal = $(xpath("//tbody/a[" + couponIndex + "]/td[contains(@class, 'fct-codes')]/span"));
                break;
            case "Additional Codes":
                couponParamVal = $(xpath("//tbody/a[" + couponIndex + "]/td[contains(@class, 'fct-codes')]/span/span"));
                break;
            case "Total Uses":
                couponParamVal = $(xpath("//tbody/a[" + couponIndex + "]/td[contains(@class, 'fct-totalUsed')]"));
                break;
            case "Current Carts":
                couponParamVal = $(xpath("//tbody/a[" + couponIndex + "]/td[contains(@class, 'fct-curentCarts')]"));
                break;
            case "Date/Time Created":
                couponParamVal = $(xpath("//tbody/a[" + couponIndex + "]//time"));
                break;
            case "State":
                couponParamVal = $(xpath("//tbody/a[" + couponIndex + "]/td[contains(@class, 'fct-state')]//div[contains(text(), 'ctive')]"));
                break;
        }
        return couponParamVal;
    }

    @Step("Click coupon with name or ID <{0}> on the list")
    public void openCoupon(String couponName) {
        click(coupon(couponName));
    }

}
