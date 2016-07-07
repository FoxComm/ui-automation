package pages;

import base.BasePage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CouponsPage extends BasePage {

    //--------------------------------- ELEMENTS -----------------------------//

    public SelenideElement addNewCoupon() {
        return $(By.xpath("//span[text()='Coupon']/.."));
    }

    public SelenideElement nameFld() {
        return $(By.xpath("//input[@name='name']"));
    }

    // may fail, as well as other rich text fields
    public SelenideElement storefrontNameFld() {
        return $(By.xpath("//div[@class='fc-object-form']/div[2]/div[1]/div[3]/div/div/div"));
    }

    public SelenideElement descriptionFld() {
        return $(By.xpath("//div[@class='fc-object-form']/div[3]/div[1]/div[3]/div/div/div"));
    }

    public SelenideElement detailsFld() {
        return $(By.xpath("//div[@class='fc-object-form']/div[4]/div[1]/div[3]/div/div/div"));
    }

    private SelenideElement promotionDd() {
        return $(By.xpath("//input[@name='promotion']/../following-sibling::*"));
    }

    private SelenideElement promotionListVal(int promotionId) {
        return $(By.xpath("//span[text()='" + promotionId + "']/.."));
    }

    public SelenideElement singleCodeRbtn() {
        return $(By.xpath("//input[@id='singleCouponCodeRadio']"));
    }

    public SelenideElement bulkGenerateCodesBrtn() {
        return $(By.xpath("//input[@id='bulkCouponCodeRadio']"));
    }

    public SelenideElement singleCodeFld() {
        return $(By.xpath("//input[@name='singleCode']"));
    }

    public SelenideElement saveBtn() {
        return $(By.xpath("//span[text()='Save']/.."));
    }

    public String couponIdVal() {
        SelenideElement couponId = $(By.xpath("//div[@class='fc-breadcrumbs']/ul/li[5]/a"));
        return couponId.getText();
    }

    // check locator
    private SelenideElement itemsOnList() {
        return $(By.xpath("//td[@class='fc-table-td']"));
    }

    private SelenideElement qtyDecrBtn() {
        return $(By.xpath("//input[@id='codesQuantity']/preceding-sibling::*/button"));
    }

    private SelenideElement qtyIncrBtn() {
        return $(By.xpath("//input[@id='codesQuantity']/following-sibling::*/button"));
    }

    private SelenideElement codePrefixFld() {
        return $(By.xpath("//input[@name='codesPrefix']"));
    }

    private SelenideElement codeLengthDecrBtn() {
        return $(By.xpath("//input[@id='codesLength']/preceding-sibling::*/button"));
    }

    private SelenideElement codeLengthIncrBtn() {
        return $(By.xpath("//input[@id='codesLength']/following-sibling::*/button"));
    }

    private SelenideElement generateCodesBtn() {
        return $(By.xpath("//span[text()='Generate Codes']/.."));
    }

    private SelenideElement submitBtn() {
        return $(By.xpath("//div[@class='fc-modal-footer']/button"));
    }

    public SelenideElement couponCodesTab() {
        return $(By.xpath("//a[text()='Coupon Codes']/.."));
    }

    private SelenideElement stateDd() {
        return $(By.xpath("//div[@class='fc-product-state']//following-sibling::*/div[2]/button"));
    }

    private SelenideElement stateListVal(String state) {
        return $(By.xpath("//li[text()='" + state + "']"));
    }

    private String stateVal() {
        SelenideElement state = $(By.xpath("//div[@class='fc-product-state']/div[2]/div[2]/div"));
        return state.getText();
    }

    private SelenideElement deleteStartDateBtn() {
        return $(By.xpath("//div[text()='Start']/following-sibling::*/div[2]/a"));
    }


    //--------------------------------- HELPERS -----------------------------//

    @Step("Wait for data on the list to be loaded.")
    public void waitForDataToLoad() {
        itemsOnList().shouldBe(Condition.visible);
    }

    @Step("Select promotion with id <{0}>")
    public void setPromotion(int promotionId) {
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
        assertTrue( !$(By.xpath("//div[text()='No coupon codes found.']")).is(visible),
                "No coupon codes is displayed on the list.");
        assertEquals( getAmountOfCodes(), codesQty,
                "There are less codes on the list than it should.");

    }

        private int getAmountOfCodes() {
            List<SelenideElement> listOfCodes = $$(By.xpath("//tbody/tr/td[3]"));
            return listOfCodes.size();
        }

    @Step("Assert that all changes to coupon were saved")
    public void assertCouponIsEdited(String name, String storefrontName, String description, String details, String state) {

        assertEquals( nameFld().getText(), name,
                "'Name' has failed to get edited.");
        assertEquals( storefrontNameFld().getText(), storefrontName,
                "'Storefront Name' has failed to get edited.");
        assertEquals( descriptionFld().getText(), description,
                "'Description' has failed to get edited.");
        assertEquals( detailsFld().getText(), details,
                "'Details' has failed to get edited.");
        assertEquals( stateVal(), state,
                "'State' has failed to get edited.");

    }

    @Step("Get '{1}' parameter value of {0}th coupon on the list")
    public String getCouponParamVal(String couponIndex, String paramName) {

        String couponParamVal = "";
        waitForDataToLoad();

        switch (paramName) {
            case "Name":
                couponParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + couponIndex + "]/td[2]")).getText();
                break;
            case "Storefront Name":
                couponParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + couponIndex + "]/td[3]")).getText();
                break;
            case "Code":
                couponParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + couponIndex + "]/td[4]")).getText();
                break;
            case "Total Uses":
                couponParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + couponIndex + "]/td[5]")).getText();
                break;
            case "Current Carts":
                couponParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + couponIndex + "]/td[6]")).getText();
                break;
            case "Date/Time Created":
                couponParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + couponIndex + "]/td[7]/time")).getText();
                break;
            case "State":
                couponParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + couponIndex + "]/td[8]/div/div")).getText();
                break;

        }

        return couponParamVal;

    }

    @Step("Find coupon with name <{0}> on the list of coupons.")
    public SelenideElement openCoupon(String couponName) {

        List<SelenideElement> couponsList = $$(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a/td[2]"));
        SelenideElement couponToClick = null;

        for(SelenideElement coupon : couponsList) {

            String listCouponName = coupon.getText();
            if (listCouponName.equals(couponName)) {
                couponToClick = coupon;
            }

        }

        return couponToClick;

    }

}
