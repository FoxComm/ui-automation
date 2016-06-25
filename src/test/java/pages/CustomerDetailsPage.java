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

    public int addressBookSize() {
        List<SelenideElement> addresses = $$(By.xpath("//ul[@class='fc-address-details']"));
        return addresses.size();

    }

    public SelenideElement saveBtn() {
        return $(By.xpath("//span[text()='Save']/.."));
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

//    public String cityFldVal(String addressIndex) {
//        SelenideElement address2 = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[3]"));
//        return address2.getText();
//    }

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
        return $(By.xpath(".//input[@name='city']"));
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

    public SelenideElement addNewCreditCardBtn() {
        return $(By.xpath("//div[text()='Credit Cards']/following-sibling::*/button"));
    }


}