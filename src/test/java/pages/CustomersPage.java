package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CustomersPage extends BasePage {

    //---------------------------- CATEGORY LIST ------------------------------//
    //------------------------------ ELEMENTS ---------------------------------//



    //------------------------------- HELPERS ---------------------------------//

    @Step("Get {1} parameter value of {0}-th customer on the list.")
    public String getCustomerParamVal(int customerIndex, String paramName) {

        String customerParamVal = "";
        waitForDataToLoad();

        switch (paramName) {
            case "Customer ID":
                customerParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + customerIndex + "]/td[2]")).getText();
                break;
            case "Name":
                customerParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + customerIndex + "]/td[3]")).getText();
                break;
            case "Email":
                customerParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + customerIndex + "]/td[4]")).getText();
                break;
            case "Ship To Region":
                customerParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + customerIndex + "]/td[5]")).getText();
                break;
            case "Bill To Region":
                customerParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + customerIndex + "]/td[6]")).getText();
                break;
            case "Rank":
                customerParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + customerIndex + "]/td[7]")).getText();
                break;
            case "Date/Time Joined":
                customerParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + customerIndex + "]/td[8]/time")).getText();
                break;
        }

        return customerParamVal;

    }

    //-------------------- G E N E R A L    E L E M E N T S ------------------//

    public SelenideElement saveBtn() {
        return $(By.xpath("//span[text()='Save']/.."));
    }

    public SelenideElement cartTab() {
        return $(By.xpath("//a[text()='Cart']"));
    }

    public SelenideElement ordersTab() {
        return $(By.xpath("//a[text()='Orders']"));
    }

    public SelenideElement itemsTab() {
        return $(By.xpath("//a[text()='Items']"));
    }

    public SelenideElement storeCreditTab() {
        return $(By.xpath("//a[text()='Store Credit']"));
    }

    public SelenideElement notesTab() {
        return $(By.xpath("//a[text()='Notes']"));
    }

    public SelenideElement activityTrailTab() {
        return $(By.xpath("//a[text()='Activity Trail']"));
    }


    //--------------------------- D E T A I L S ------------------------------//
    //---------------------------- ADDRESS BOOK ------------------------------//
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
        List<SelenideElement> addresses = $$(By.xpath("//li[@class='fc-card-container fc-address']"));
        return addresses.size();
    }

    public String nameFldVal(String addressIndex) {
        SelenideElement name = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[1]"));
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

    public String stateDdVal(String addressIndex) {
        SelenideElement stateVal = $(By.xpath("//ul[contains(@class, 'addresses-list')]/li[" + addressIndex + "]/div[3]/div/ul/li[4]/span[1]"));
        return stateVal.getText();
    }

    public String zipFldVal(String addressIndex) {
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
        return $(By.xpath("//li[text()='" + stateName + "']"));
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

    @Step("Add new address to customer's address book.")
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
        assertTrue(!phoneNumbErrorMsg().is(visible),
                "Failed to specify a full phone number (there might be a rendering issue).");
        sleep(2000);

    }

    @Step("Set <{0}> as a 'State'.")
    public void setState(String state) {
        click(stateDd());
        click(stateDdValue(state));
    }

    @Step("Assert that 'State' dropdown value isn't reset to default.")
    public void assertStateIsntReset() {
        assertTrue(!Objects.equals(stateDd().getText(), "- Select -"),
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


    //---------------------------- CREDIT CARDS ------------------------------//
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
        return nameFld_billAddress.text();
    }


    //------------------------------ HELPERS --------------------------------//

    @Step("Add new credit card")
    public void fillOutNewCCForm(String holderName, String cardNumber, String cvv, String month, String year) {

        setFieldVal(holderNameFld(), holderName);
        setFieldVal(cardNumberFld(), cardNumber);
        setFieldVal(cvvFld(), cvv);
        setExpirationDate(month, year);

    }

    @Step("Set expiration date: {0}/{1}")
    public void setExpirationDate(String month, String year) {
        click(monthDd());
        click(monthVal(month));
        click(yearDd());
        click(yearVal(year));
    }

    @Step("Add new billing address")
    public void addNewBillAddress(String name, String streetAddress1, String streetAddress2, String city, String state, String zipCode, String phoneNumber) {

        click(newBillAddressBtn());
        addNewAddress(name, streetAddress1, streetAddress2, city, state, zipCode, phoneNumber);
        click(saveBtn());
        sleep(2000);
        assertEquals( addressBookSize(), 1,
                  "Failed to create a new address." );
        click( chooseBtn("1") );
        assertTrue(billName().equals(name),
                "Incorrect address seems to be set as a billing address; expected name: <" + name + ">, actual: <" + billName() + ">.");

    }

    @Step
    public void assertCardAdded(String customerName) {
        sleep(1000);
        assertTrue($(By.xpath("//dd[text()='" + customerName + "']")).is(visible),
                "Failed to create a new credit card.");
    }


    //--------------------------------- CONTACTS -----------------------------//
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


    //--------------------------------- OVERVIEW -----------------------------//
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

    public double totalSalesVal() {
        SelenideElement totalSales = $(By.xpath("//div[contains(@class, 'customer-details')]/ul[2]/li[2]/span[1]"));
        String strVal = totalSales.getText();
        return Double.valueOf(strVal.substring(1, strVal.length()));
    }


    //---------------------------- O R D E R S -------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement searchFld() {
        return $(By.xpath("//input[@placeholder='filter or keyword search']"));
    }

    public int amountOfOrders() {
        List<SelenideElement> orders = $$(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a"));
        return orders.size();
    }

    //------------------------------ HELPERS --------------------------------//

    @Step("Get {1} parameter value of {0}-th order on the list.")
    public String getOrderParamVal(int orderIndex, String paramName) {

        String orderParamVal = "";
        waitForDataToLoad();

        switch (paramName) {
            case "Order":
                orderParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + orderIndex + "]/td[2]")).getText();
                break;
            case "Date/Time Placed":
                orderParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + orderIndex + "]/td[3]/time")).getText();
                break;
            case "Modality":
                orderParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + orderIndex + "]/td[4]")).getText();
                break;
            case "Order State":
                orderParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + orderIndex + "]/td[5]")).getText();
                break;
            case "Payment State":
                orderParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + orderIndex + "]/td[6]")).getText();
                break;
            case "Assignee":
                orderParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + orderIndex + "]/td[7]")).getText();
                break;
            case "Total":
                orderParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + orderIndex + "]/td[8]/span")).getText();
                break;
        }

        return orderParamVal;

    }

    @Step("Order list of orders by {0}")
    public void orderListBy(String param) {
        click($(By.xpath("//th[text()='" + param + "']")));
        waitForDataToLoad();
    }

//    public void hitEnter() {
//        searchFld().sendKeys(ENTER);
//        waitForDataToLoad();
//    }
//
//    private void selectLine(int index) {
//        for (int i = 0; i < index; i++) {
//            searchFld().sendKeys(Keys.ARROW_DOWN);
//        }
//        hitEnter();
//    }
//
//    @Step("Create a search filter {0} : {1} : {2}")
//    public void addFilter(String firstStatement, String secondStatement, String thirdStatement) {
//
//        searchFld().click();
//
//        switch (firstStatement) {
//
//            case "Order":
//                selectLine(1);
//                switch (secondStatement)
//                {
//                    case "Reference Number":
//                        selectLine(1);
//                        searchFld().sendKeys(thirdStatement);
//                        hitEnter();
//                        break;
//                    case "State":
//                        selectLine(2);
//                        switch(thirdStatement)
//                        {
//                            case "Cart":
//                                selectLine(1);
//                                break;
//                            case "Remorse Hold":
//                                selectLine(2);
//                                break;
//                            case "Manual Hold":
//                                selectLine(3);
//                                break;
//                            case "Fraud Hold":
//                                selectLine(4);
//                                break;
//                            case "Fulfillment Started":
//                                selectLine(5);
//                                break;
//                            case "Canceled":
//                                selectLine(8);
//                                break;
//                        }
//                        break;
//                }
//                break;
//
//            case "Items":
//                selectLine(3);
//                switch(secondStatement)
//                {
//                    case "Product Name":
//                        selectLine(2);
//                        searchFld().sendKeys(thirdStatement);
//                        hitEnter();
//                        break;
//                    case "Product SKU":
//                        selectLine(3);
//                        searchFld().sendKeys(thirdStatement);
//                        hitEnter();
//                        break;
//                }
//
//        }
//
//        $(By.xpath("//h1[text()='Orders']")).click();
//        sleep(500);
//
//    }


    //---------------------- S T O R E    C R E D I T S ----------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement newSCBtn() {
        return $(By.xpath("//span[text()='Store Credit']/.."));
    }

    private SelenideElement scTypeDd() {
        return $(By.xpath("//li[contains(@class, 'store-credit-form')]/div[2]/div/div[2]/button"));
    }

    private SelenideElement scTypeListItem(String typeName) {
        return $(By.xpath("//li[text()='" + typeName + "']"));
    }

    public SelenideElement valueFld() {
        return $(By.xpath("//input[@id='scAmountTextField']"));
    }

    public SelenideElement gcNumberFld() {
        return $(By.xpath("//input[@id='gcNumberField']"));
    }

    public double gcAvailableBalanceVal() {
        String gcAvailBalance = $(By.xpath("//div[contains(@class, 'gc-value')]/span")).getText();
        return Double.valueOf(gcAvailBalance.substring(1, gcAvailBalance.length()));
    }

    public SelenideElement submitBtn() {
        return $(By.xpath("//button[@type='submit']"));
    }

    public double availableBalanceVal() {
        SelenideElement availableBalance = $(By.xpath("//div[text()='Total Available Balance']/following-sibling::*/div/span"));
        String availBalanceVal = availableBalance.getText();
        return Double.valueOf( availBalanceVal.substring(1, availBalanceVal.length()) );
    }

    public int amountOfSCs() {
        List <SelenideElement> issuedSCs = $$(By.xpath("//tbody/tr[@class='fc-table-tr']"));
        return issuedSCs.size();
    }

    private SelenideElement scStateDd(String scIndex) {
        return $(By.xpath("//tbody/tr[" + scIndex + "]/td[9]/div/div[2]/button"));
    }

    private SelenideElement cancellationReason(String reason) {
        return $(By.xpath("//li[contains(text(), '" + reason + "')]"));
    }

    private SelenideElement scStateListVal(String stateVal) {
        return $(By.xpath("//tbody/tr[1]/td[9]/div/div[3]/ul/li[text()='" + stateVal + "']"));
    }

    public SelenideElement presetValue(String scVal) {
        return $(By.xpath("//li[contains(@class, 'balances')]/div[text()='" + scVal + "']"));
    }

    private SelenideElement yesBtn() {
        return $(By.xpath("//span[contains(text(), 'Yes')]/.."));
    }

    public SelenideElement transactionTab() {
        return $(By.xpath("//a[text()='Transaction']"));
    }




    //------------------------------ HELPERS --------------------------------//

    @Step("Set SC type to {0}")
    public void selectType(String typeName) {
        sleep(1000);
        click( scTypeDd() );
        click( scTypeListItem(typeName) );
    }

    @Step("Get {1} parameter value of {0}-th store credit on the list.")
    public String getSCParamVal(String scIndex, String paramName) {

        String scParamVal = "";
        waitForDataToLoad();

        switch (paramName) {
            case "Date/Time Issued":
                scParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + scIndex + "]/td[2]/time")).getText();
                break;
            case "Store Credit Id":
                scParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + scIndex + "]/td[3]")).getText();
                break;
            case "Type":
                scParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + scIndex + "]/td[4]/div/div")).getText();
                break;
            case "Issued By":
                scParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + scIndex + "]/td[5]/div")).getText();
                break;
            case "Original Balance":
                scParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + scIndex + "]/td[6]/span")).getText();
                break;
            case "Current Balance":
                scParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + scIndex + "]/td[7]/span")).getText();
                break;
            case "Available Balance":
                scParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + scIndex + "]/td[8]/span")).getText();
                break;
            case "State":
                scParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + scIndex + "]/td[9]/div/div[2]/div")).getText();
                break;
        }

        return scParamVal;

    }

    @Step("Change 'State' of {0}th SC on the list to '{1}'")
    public void setState(String scIndex, String state) {
        click( scStateDd(scIndex) );
        click( scStateListVal(state) );
        click( yesBtn() );
    }

    public void setState(String scIndex, String state,String reason) {
        click( scStateDd(scIndex) );
        click( scStateListVal(state) );
        click( cancellationReason(reason) );
        click( yesBtn() );
    }

    @Step("Get {1} parameter value of {0}-th SC transaction on the list.")
    public String getTransactionParamVal(String transactionIndex, String paramName) {

        String transactionParamVal = "";
        waitForDataToLoad();

        switch (paramName) {
            case "Date/Time":
                transactionParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + transactionIndex + "]/td[2]/time")).getText();
                break;
            case "Transaction":
                transactionParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + transactionIndex + "]/td[3]/div/div")).getText();
                break;
            case "Amount":
                transactionParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + transactionIndex + "]/td[4]/span")).getText();
                break;
            case "Payment State":
                transactionParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + transactionIndex + "]/td[5]/span")).getText();
                break;
            case "Total Available Balance":
                transactionParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/tr[" + transactionIndex + "]/td[6]/span")).getText();
                break;
        }

        return transactionParamVal;

    }



}