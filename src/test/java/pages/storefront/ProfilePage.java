package pages.storefront;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.selected;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class ProfilePage extends AuthPage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

    //TODO: id
    public SelenideElement userEmail() {
        return $(xpath("//div[contains(@class, 'content')]/div[2]/div[2]"));
    }

    private SelenideElement editLnk(String parameter) {
        parameter = parameter.toLowerCase().replaceAll(" ", "-");
        return $(xpath("//a[contains(@href, '/profile/" + parameter + "')]"));
    }

    public SelenideElement changePasswordBtn() {
        return $(xpath("//button[text()='CHANGE PASSWORD']"));
    }

    //TODO: id
    private SelenideElement nameFld_edit() {
        return $(xpath("//div[contains(text(), 'Use this form')]/following-sibling::*/input"));
    }

    //TODO: id
    private SelenideElement emailFld_edit() {
        return $(xpath("//div[contains(text(), 'Use this form')]/following-sibling::*/input"));
    }

    public SelenideElement saveBtn() {
        return $(xpath("//button[contains(@class, 'save-button')]"));
    }

    //TODO: id
    public ElementsCollection myAddresses() {
        return $$(xpath("//div[text()='My Shipping Addresses']/following-sibling::*/div/ul/li"));
    }

    //TODO: id
    private SelenideElement editAddressBtn(String index) {
        return $(xpath("//div[text()='My Shipping Addresses']/following-sibling::*/div/ul/li[" + index + "]//a[text()='EDIT']"));
    }

    //TODO: id
    private SelenideElement removeAddressBtn(String index) {
        return $(xpath("//div[text()='My Shipping Addresses']/following-sibling::*/div/ul/li[" + index + "]//div[text()='REMOVE']"));
    }

    //TODO: id
    public SelenideElement restoreAddressBtn(String index) {
        return $(xpath("//div[text()='My Shipping Addresses']/following-sibling::*/div/ul/li[" + index + "]//div[text()='RESTORE']"));
    }

    //TODO: id
    public SelenideElement shipAddress_name(String index) {
        return $(xpath("//ul[contains(@class, 'list')]/li[" + index + "]//h3[contains(@class, 'title')]"));
    }

    //TODO: id
    public SelenideElement shipAddress_zip(String index) {
        return $(xpath("//ul[contains(@class, 'list')]/li[" + index + "]//ul[contains(@class, 'address-details')]/li[3]/span[2]"));
    }

    //TODO: id
    public SelenideElement shipAddress_state(String index) {
        return $(xpath("//ul[contains(@class, 'list')]/li[" + index + "]//ul[contains(@class, 'address-details')]/li[3]/span[1]"));
    }

    //TODO: id
    private SelenideElement deletedAddress(String index) {
        return $(xpath("//li[" + index + "]//ul[contains(@class, 'deleted-content')]"));
    }

    private SelenideElement addressIsDefaultChbx() {
        return $(xpath("//input[@type='checkbox']"));
    }

    public SelenideElement addAddressBtn() {
        return $(xpath("//button[text()='Add Address']"));
    }

    private SelenideElement nameFld_shipAddress() {
        return $(xpath("//input[@name='name']"));
    }

    private SelenideElement address1Fld() {
        return $(xpath("//input[@name='address1']"));
    }

    private SelenideElement address2Fld() {
        return $(xpath("//input[@name='address2']"));
    }

    private SelenideElement zipFld() {
        return $(xpath("//input[@placeholder='ZIP']"));
    }

    public SelenideElement cityFld() {
        return $(xpath("//input[@name='city']"));
    }

    private SelenideElement phoneNumberFld() {
        return $(xpath("//input[@name='phoneNumber']"));
    }

    public SelenideElement selectAddressRbtn(String index) {
        return $(xpath("//li[" + index + "]//input[contains(@name, 'address-radio')]"));
    }

    //---------------------------------------------- STEPS ---------------------------------------------

    @Step("Click \"EDIT\" next to <{0}>")
    public void clickEditLnk(String parameter) {
        click(editLnk(parameter));
    }

    @Step("Set \"Name\" fld to <{0}>")
    public void setNameFld(String name) {
        setFieldVal(nameFld_edit(), name);
    }

    @Step("Set \"Email\" fld to <{0}>")
    public void setEmailFld(String email) {
        setFieldVal(emailFld_edit(), email);
    }

    @Step("Click \"Save\" btn")
    public void clickSaveBtn() {
        click(saveBtn());
    }

    @Step("Click \"EDIT\" btn next to <{0}th> address")
    public void clickEditBtn_address(String index) {
        click(editAddressBtn(index));
    }

    @Step("Click \"REMOVE\" btn next to <{0}th> address")
    public void clickRemoveBtn_address(String index) {
        click(removeAddressBtn(index));
    }

    @Step("Click \"RESTORE\" btn next to <{0}th> address")
    public void clickRestoreBtn_address(String index){
        click(restoreAddressBtn(index));
    }

    @Step("Click \"Add Address\" btn")
    public void clickAddAddressBtn() {
        click(addAddressBtn());
    }

    @Step("Fill out new address form; name:<{0}>, address1:<{1}>, address2:<{2}>, zip:<{3}>, phoneNumber:<{4}>")
    public void fillOutAddressForm(String name, String address1, String address2, String zip, String phoneNumber) {
        setName_shipAddress(name);
        setAddress1(address1);
        setaddress2(address2);
        setZIP(zip);
        setPhoneNumber(phoneNumber);
    }

    @Step("Check the \"Make this address my default\" checkbox")
    public void clickDefaultChbx() {
        shouldNotBeEmpty(nameFld_shipAddress(), "\"Name\" fld is empty");
        jsClick(addressIsDefaultChbx());
    }

    @Step("Set \"Name\" fld val to <{0}>")
    public void setName_shipAddress(String name) {
        setFieldVal(nameFld_shipAddress(), name);
    }

    @Step("Set \"Address 1\" fld val to <{0}>")
    public void setAddress1(String address1) {
        setFieldVal(address1Fld(), address1);
    }

    @Step("Set \"Address 2\" fld val to <{0}>")
    public void setaddress2(String address2) {
        setFieldVal(address2Fld(), address2);
    }

    @Step("Set \"ZIP\" fld val to <{0}>")
    public void setZIP(String zip) {
        setFieldVal(zipFld(), zip);
    }

    @Step("Set \"Phone Number\" fld val to <{0}>")
    public void setPhoneNumber(String phoneNumber) {
        setFieldVal(phoneNumberFld(), phoneNumber);
    }

    @Step("Assert that address is not displayed as deleted")
    public void assertAddressRestored(String index) {
        deletedAddress(index).shouldNotBe(visible);
    }

    @Step("Assert that address <{0}> is set as default")
    public void assertAddressIsSelected(String index) {
        scrollToElement($(xpath("//div[text()='My Shipping Addresses']")));
        selectAddressRbtn(index).shouldBe(selected);
    }

}
