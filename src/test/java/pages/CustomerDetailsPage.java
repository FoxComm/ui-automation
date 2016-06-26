package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertTrue;

public class CustomerDetailsPage extends BasePage {

    //-------------------- G E N E R A L    E L E M E N T S ------------------//

    public SelenideElement saveBtn() {
        return $(By.xpath("//span[text()='Save']/.."));
    }

    // navigation across the tabs goes here


    //----------------------------- D E T A I L S ----------------------------//
    //------------------------ A D D R E S S    B O O K ----------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement addNewAddressBtn() {
        return $(By.xpath("//div[text()= 'Address Book']/following-sibling::*/button"));
    }

    public SelenideElement editAddressBtn(String addressIndex) {
        return $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[2]/div/button[2]"));
    }

    public SelenideElement deleteAddressBtn(String addressIndex) {
        return $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[2]/div/button[1]"));
    }

        private SelenideElement confirmDeletionBtn() {
            return $(By.xpath("//span[text()='Yes, Delete']/.."));
        }

        private SelenideElement cancelDeletionBtn() {
            return $(By.xpath("//div[@class='fc-modal-footer']/a"));
        }

    public SelenideElement defaultShipAddressChbx(String addressIndex) {
        //xpath may be wrong
        return $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]div/label/div/input"));
    }

    public int addressBookSize() {
        List<SelenideElement> addresses = $$(By.xpath("//ul[@class='fc-address-details']"));
        return addresses.size();
    }

    public String nameFldVal(String addressIndex) {
        SelenideElement name =  $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[1]"));
        return name.getText();
    }

    public String address1FldVal(String addressIndex) {
        SelenideElement address1 = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[2]"));
        return address1.getText();
    }

    public String address2FldVal(String addressIndex) {
        SelenideElement address2 = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[3]"));
        return address2.getText();
    }

    public String cityFldVal(String addressIndex) {
        SelenideElement cityFld = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[4]"));
        String cityFldVal = cityFld.getText();
        return cityFldVal.substring(0, cityFldVal.indexOf(","));
    }

    public String stateDdVal (String addressIndex) {
        SelenideElement stateVal = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[4]/span[1]"));
        return stateVal.getText();
    }

    public String zipFldVal (String addressIndex) {
        SelenideElement zipVal = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[4]/span[2]"));
        return zipVal.getText();
    }

    public String phoneNumberFldVal(String addressIndex) {
        SelenideElement phoneNumberVal = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[6]/span"));
        return phoneNumberVal.getText();
    }

    public SelenideElement phoneNumbErrorMsg() {
        return $(By.xpath("//div[contains(text(), 'Phone Number must not')]"));
    }

    // ----------- >> NEW ADDRESS FORM
    public SelenideElement nameFld() {
        return $(By.xpath("//input[@name='name']"));
    }

    public SelenideElement address1Fld() {
        return $(By.xpath(".//input[@name='address1']"));
    }

    public SelenideElement address2Fld() {
        return $(By.xpath(".//input[@name='address2']"));
    }

    public SelenideElement cityFld() {
        return $(By.xpath("//input[@name='city']"));
    }

    private SelenideElement stateDd() {
        return $(By.xpath("//ul[@class='fc-address-form-fields']/li[6]/div/div/div[2]/button"));
    }

    private SelenideElement stateDdValue(String stateName) {
        return $(By.xpath("//li[text()='" + stateName +"']"));
    }

    public SelenideElement zipFld() {
        return $(By.xpath("//input[@name='zip']"));
    }

    public SelenideElement phoneNumberFld() {
        return $(By.xpath("//input[@name='phoneNumber']"));
    }

    private SelenideElement saveBtn_addressForm() {
        return $(By.xpath("//span[text()='Save']/.."));
    }

    private SelenideElement cancelBtn_addressForm() {
        return $(By.xpath("//a[text()='Cancel']"));
    }
    // -----------

    //------------------------------ HELPERS --------------------------------//

    @Step
    public void addNewAddress(String name, String streetAddress1, String streetAddress2, String city, String state, String zipCode, String phoneNumber) {

        click(addNewAddressBtn());
        setFieldVal(nameFld(), name);
        setFieldVal(address1Fld(), streetAddress1);
        setFieldVal(address2Fld(), streetAddress2);
        setFieldVal(cityFld(), city);
        setState(state);
        setFieldVal(zipFld(), zipCode);
        setFieldVal(phoneNumberFld(), phoneNumber);
        assertStateIsntReset();
        click(saveBtn_addressForm());

    }

    @Step
    public void setState(String state) {
        click( stateDd() );
        click( stateDdValue(state) );
    }

    public void assertStateIsntReset() {
        assertTrue( !Objects.equals(stateDd().getText(), "- Select -"),
                "'State' is reset to default value");
    }

    @Step("Confirm item deletion")
    public void confirmDeletion() {
        confirmDeletionBtn().click();
        confirmDeletionBtn().shouldNot(visible);
        sleep(2000);
    }

    @Step("Cancel item deletion")
    public void cancelDeletion() {
        cancelDeletionBtn().click();
        cancelDeletionBtn().shouldNot(visible);
        sleep(2000);
    }



    //------------------------ C R E D I T    C A R D S ----------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement newBillAddressBtn() {
        return $(By.xpath("//div[@class='fc-left' and text()='Address Book']/following-sibling::*/button"));
    }

    public SelenideElement addNewCreditCardBtn() {
        return $(By.xpath("//div[text()='Credit Cards']/following-sibling::*/button"));
    }

    private String billName() {
        SelenideElement name = $(By.xpath("//label[contains(@class, 'credit-card')]/following-sibling::*/li[1]"));
        return name.getText();
    }

    public SelenideElement editCreditCardBtn(String ccIndex) {
        return $(By.xpath("//div[contains(@class, 'credit-cards')]/div/ul/li[" + ccIndex + "]/div[2]/div/button[2]"));
    }

    public SelenideElement deleteCreditCardBtn(String ccIndex) {
        return $(By.xpath("//div[contains(@class, 'credit-cards')]/div/ul/li[" + ccIndex + "]/div[2]/div/button[1]"));
    }

    public SelenideElement changeBillAddressBtn() {
        return $(By.xpath("//a[text()='Change']"));
    }

    // ----------- >> NEW CREDIT CARD FORM
    public SelenideElement holderNameFld() {
        return $(By.xpath("//input[@name='holderName']"));
    }

    private SelenideElement cardNumberFld() {
        return $(By.xpath("//input[@name='cardNumber']"));
    }

    private SelenideElement cvvFld() {
        return $(By.xpath("//input[@name='cvv']"));
    }

    private SelenideElement monthDd() {
        return $(By.xpath("//label[text()='Expiration Date']/following-sibling::*/div/div/div[2]/div"));
    }

    private SelenideElement monthVal(String monthNumber) {
        return $(By.xpath("//div[@class='fc-grid']/div[1]/div/div[3]/ul/li[" + monthNumber + "]"));
    }

    private SelenideElement yearDd() {
        return $(By.xpath("//label[text()='Expiration Date']/following-sibling::*/div[2]/div/div[2]/div"));
    }

    private SelenideElement yearVal(String year) {
        return $(By.xpath("//div[@class='fc-grid']/div[2]/div/div[3]/ul/li[text()='" + year + "']"));
    }

    public SelenideElement chooseBtn(String addressIndex) {
        return $(By.xpath("//div[@class='fc-address-select-list']/div[" + addressIndex + "]/div[2]/button"));
    }
    // -------- -------- -------- --------

    public String holderNameFldVal(String ccIndex) {
        SelenideElement holderName = $(By.xpath("//div[contains(@class, 'credit-cards')]/div/ul/li[" + ccIndex + "]/div[3]/div/dl[1]/dd"));
        return holderName.getText();
    }

    public String expirationDateVal(String ccIndex) {
        SelenideElement expirationDate = $(By.xpath("//div[contains(@class, 'credit-cards')]/div/ul/li[" + ccIndex + "]/div[3]/div/div/div/div/div"));
        return expirationDate.getText();
    }

    public String billCityVal(String ccIndex) {
        SelenideElement billCity = $(By.xpath("//div[contains(@class, 'credit-cards')]/div/ul/li[" + ccIndex + "]/div[3]/div/dl[2]/dd/ul/li[4]"));
        String billCityVal = billCity.getText();
        return billCityVal.substring(0, billCityVal.indexOf(","));
    }

    public String nameFldVal_billAddress(String ccIndex) {
        SelenideElement nameFld_billAddress = $(By.xpath("//div[contains(@class, 'credit-cards')]/div/ul/li[" + ccIndex + "]/div[3]/div/dl[2]/dd/ul/li[1]"));
        return nameFld_billAddress.getText();
    }


    //------------------------------ HELPERS --------------------------------//

    @Step("Add new credit card")
    public void addNewCreditCard(String holderName, String cardNumber, String cvv, String month, String year) {

        setFieldVal( holderNameFld(), holderName );
        setFieldVal( cardNumberFld(), cardNumber );
        setFieldVal( cvvFld(), cvv );
        setExpirationDate(month, year);

    }

    @Step("Set expiration date: {0}/{1}")
    public void setExpirationDate(String month, String year) {
        click( monthDd() );
        click( monthVal(month) );
        click( yearDd() );
        click( yearVal(year) );
    }

    @Step("Add new billing address")
    public void addNewBillAddress(String name, String streetAddress1, String streetAddress2, String city, String state, String zipCode, String phoneNumber) {

        click( newBillAddressBtn() );
        setFieldVal(nameFld(), name);
        setFieldVal(address1Fld(), streetAddress1);
        setFieldVal(address2Fld(), streetAddress2);
        setFieldVal(cityFld(), city);
        setState(state);
        setFieldVal(zipFld(), zipCode);
        setFieldVal(phoneNumberFld(), phoneNumber);
        assertStateIsntReset();
        click( saveBtn() );
        sleep(1500);

        assertTrue( !phoneNumbErrorMsg().is(visible),
                "Phone number wasn't fully input; expected: <(987) 987-9876>, actual: <" + phoneNumberFld().getAttribute("value") + ">.");
        assertTrue( changeBillAddressBtn().is(visible),
                "Added address wasn't set as a billing address.");
        assertTrue( billName().equals(name),
                "Incorrect address seems to be set as a billing address; expected name: <" + name + ">, actual: <" + billName() + ">.");

    }

    @Step
    public void assertCardAdded(String customerName) {
        sleep(1000);
        assertTrue( $(By.xpath("//dd[text()='" + customerName + "']")).is(visible),
                "Failed to create a new credit card.");
    }



    //----------------------------- C O N T A C T S --------------------------//
    //--------------------------------- ELEMENTS -----------------------------//

    public SelenideElement editBtn_contactInfo() {
        return $(By.xpath("//div[text()='Contact Information']/following-sibling::*/button"));
    }

    public SelenideElement nameFld_contactInfo() {
        return $(By.xpath("//input[@id='nameField']"));
    }

    public String nameVal_contactInfo() {
        SelenideElement nameFld_contactInfo = $(By.xpath("//form[contains(@class, 'customer-contacts')]/dl[1]/dd"));
        return nameFld_contactInfo.getText();
    }

    public SelenideElement emailFld_contactInfo() {
        return $(By.xpath("//input[@id='emailField']"));
    }

    public String emailVal_contactInfo() {
        SelenideElement emailFld_contactInfo = $(By.xpath("//form[contains(@class, 'customer-contacts')]/dl[2]/dd"));
        return emailFld_contactInfo.getText();
    }

    public SelenideElement phoneNumberFld_contactInfo() {
        return $(By.xpath("//input[@id='phoneField']"));
    }

    public String phoneNumberVal_contactInfo() {
        SelenideElement phoneNumberFld_contactInfo = $(By.xpath("//form[contains(@class, 'customer-contacts')]/dl[3]/dd"));
        return phoneNumberFld_contactInfo.getText();
    }

    //--------------------------------- HELPERS -----------------------------//

    //



    //----------------------------- O V E R V I E W --------------------------//
    //--------------------------------- ELEMENTS -----------------------------//

    public String nameVal_overview() {
        SelenideElement name_overview = $(By.xpath("//div[@class='_customers_title_block__name']"));
        return name_overview.getText();
    }

    public String emailVal_overview() {
        SelenideElement email_overview = $(By.xpath("//div[@class='_customers_title_block__email']"));
        return email_overview.getText();
    }

    public String phoneNumberVal_overview() {
        SelenideElement phoneNumber_overview = $(By.xpath("//i[@class='icon-phone']/following-sibling::*"));
        return phoneNumber_overview.getText();
    }






}