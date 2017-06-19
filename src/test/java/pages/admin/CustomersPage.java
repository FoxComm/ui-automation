package pages.admin;

import base.AdminBasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.openqa.selenium.By.xpath;

public class CustomersPage extends AdminBasePage {

    //---------------------------- CATEGORY LIST ------------------------------//
    //------------------------------ ELEMENTS ---------------------------------//

    public SelenideElement addCustomerBtn() {
        return $(xpath("//span[text()='Customer']/.."));
    }

    //------------------------------- HELPERS ---------------------------------//

    @Step("Click \"Add Customer\" btn")
    public void clickAddCustomerBtn() {
        click(addCustomerBtn());
    }

    @Step("Get <{1}> parameter value of <{0}th> customer on the list")
    public SelenideElement getCustomerParamVal(int customerIndex, String paramName) {
        SelenideElement paramVal = null;
        waitForDataToLoad();

        switch (paramName) {
            case "Customer ID":
                paramVal = $(By.xpath("//tbody/a[" + customerIndex + "]/td[contains(@class, 'id')]"));
                break;
            case "Name":
                paramVal = $(By.xpath("//tbody/a[" + customerIndex + "]/td[contains(@class, 'name')]"));
                break;
            case "Email":
                paramVal = $(By.xpath("//tbody/a[" + customerIndex + "]/td[contains(@class, 'email')]"));
                break;
            case "Ship To Region":
                paramVal = $(By.xpath("//tbody/a[" + customerIndex + "]/td[contains(@class, 'shipRegion')]"));
                break;
            case "Bill To Region":
                paramVal = $(By.xpath("//tbody/a[" + customerIndex + "]/td[contains(@class, 'billRegion')]"));
                break;
            case "Rank":
                paramVal = $(By.xpath("//tbody/a[" + customerIndex + "]/td[contains(@class, 'rank')]"));
                break;
            case "Date/Time Joined":
                paramVal = $(By.xpath("//tbody/a[" + customerIndex + "]//time"));
                break;
        }

        return paramVal;
    }

    //--------------------------- G E N E R A L ------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement newCustomerName() {
        return $(xpath("//input[@id='nameCustomerFormField']"));
    }

    public SelenideElement newCustomerEmail() {
        return $(xpath("//input[@id='emailCustomerFormField']"));
    }

    public SelenideElement customerName() {
        return $(xpath("//div[@id='fct-customer-title-name']"));
    }

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

    //------------------------------ HELPERS --------------------------------//

    @Step("Set new customer \"Name\" fld to <{0}>")
    public void setNewCustomerName(String name) {
        setFieldVal(newCustomerName(), name);
    }

    @Step("Set new customer \"Email Address\" fld to <{0}>")
    public void setNewCustomerEmail(String email) {
        setFieldVal(newCustomerEmail(), email);
    }


    //--------------------------- D E T A I L S ------------------------------//
    //---------------------------- ADDRESS BOOK ------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement addNewAddressBtn() {
        return $(By.xpath("//button[@id='fct-add-btn__new-address']"));
    }

    public SelenideElement editAddressBtn(String addressIndex) {
        return $(By.xpath("(.//*[@id='fct-customer-addresses-list']//*[contains(@class,'icon-edit')])[" + addressIndex + "]"));
//        return $(By.xpath("//ul[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//i[@class='icon-edit']/.."));
    }

    public SelenideElement deleteAddressBtn(String addressIndex) {
        return $(By.xpath("(.//*[@id='fct-customer-addresses-list']//*[contains(@class,'icon-trash')])[" + addressIndex + "]"));
    }

    private SelenideElement confirmDeletionBtn() {
        return $(By.xpath("//button[@id='fct-modal-confirm-btn']"));
    }

    private SelenideElement cancelDeletionBtn() {
        return $(By.xpath("//a[@id='fct-modal-cancel-btn']"));
    }

    public SelenideElement defaultShipAddressChbx_byIndex(String addressIndex) {
        //xpath might be wrong
        return $(By.xpath("//*[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//input[contains(@id, 'is-default')]"));
    }

    public SelenideElement defaultShipAddressChbx_byId(String addressId) {
        return $(xpath("//input[@id='" + addressId + "-is-default'"));
    }

    public ElementsCollection addressBook() {
        return $$(By.xpath("//li[@class='fc-card-container fc-address']"));
    }

    public SelenideElement nameFldVal(String addressIndex) {
        return $(By.xpath("//ul[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//li[@class='name']"));
    }

    public SelenideElement address1FldVal(String addressIndex) {
        return $(By.xpath("//ul[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//li[@class='address1']"));
    }

    public SelenideElement address2FldVal(String addressIndex) {
        return $(By.xpath("//ul[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//li[@class='address2']"));
    }

    public SelenideElement cityFldVal(String addressIndex) {
        return $(By.xpath("//ul[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//li[@class='city']"));
    }

    public SelenideElement stateVal(String addressIndex) {
        return $(By.xpath("//ul[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//span[@class='region']"));
    }

    public SelenideElement zipFldVal(String addressIndex) {
        return $(By.xpath("//ul[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//span[@class='zip']"));

    }

    public SelenideElement phoneNumberFldVal(String addressIndex) {
        return $(By.xpath("//ul[@id='fct-customer-addresses-list']/li[" + addressIndex + "]//span[@class='phone']"));
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
        return $(By.xpath("//div[@id='region-dd']"));
    }

    public SelenideElement zipCodeFld() {
        return $(By.xpath("//input[@name='zip']"));
    }

    public SelenideElement phoneNumberFld() {
        return $(By.xpath("//input[@name='phoneNumber']"));
    }

    private SelenideElement cancelBtn_addressForm() {
        return $(By.xpath("//button[@id='fct-modal-cancel-btn']"));
    }
    // -----------

    //------------------------------ HELPERS --------------------------------//

    @Step("Click \"Edit\" next to <{0}th> address in the address book")
    public void clickEditAddressBtn(String index) {
        click(editAddressBtn(index));
    }

    @Step("Add new address to customer's address book")
    public void addNewAddress(String name, String streetAddress1, String streetAddress2, String city, String state, String zipCode, String phoneNumber) {
        setName(name);
        setAddress1(streetAddress1);
        setAddress2(streetAddress2);
        setFieldVal( cityFld(), city );
        setCity(city);
        setState(state);
        setZip(zipCode);
        setPhoneNumber(phoneNumber);
        assertStateIsntReset();         // regression assertion
    }

        @Step("Click \"New Address\" button")
        public void clickNewAddressBtn() {
            click( addNewAddressBtn() );
        }

        @Step("Set \"Name\" fld val to <{0}>")
        public void setName(String name) {
            setFieldVal( nameFld(), name );
        }

        @Step("Set '\"ddress 1\" fld val to <{0}>")
        public void setAddress1(String streetAddress1) {
            setFieldVal( address1Fld(), streetAddress1 );
        }

        @Step("Set \"Address 2\" fld val to <{0}>")
        public void setAddress2(String streetAddress2) {
            setFieldVal( address2Fld(), streetAddress2 );
        }

        @Step("Set \"City\" fld val to <{0}>")
        public void setCity(String city) {
            setFieldVal( cityFld(), city );
        }

        @Step("Set shipping \"State\" to <{0}>")
        public void setState(String state) {
            setDdVal(stateDd(), state);
        }

        @Step("Set \"Zip Code\" fld val to <{0}>")
        public void setZip(String zipCode) {
            setFieldVal( zipCodeFld(), zipCode );
        }

        @Step("Set \"Phone Number\" fld val to <{0}>")
        public void setPhoneNumber(String phoneNumber) {
            setFieldVal_delayed( phoneNumberFld(), phoneNumber );
        }

        @Step("Assert that \"State\" dd val isn't reset")
        public void assertStateIsntReset() {
            stateDd().shouldNotHave(text("- Select -")
                    .because("'State' is reset to default value"));
        }

    /**
     * Click "Save" btn and wait for it to get re-enabled
     * Overrides similar method from BasePage
     */
    @Step("Click \"Save\"")
    public void clickSave_() {
        click(saveBtn());
        sleep(1000);
        shouldBeEnabled(saveBtn(), "Failed to wait until \"Save\" will be re-enabled");
    }

    @Step("Remove <{0}th> shipping address from address book")
    public void deleteAddress(String index) {
        click(deleteAddressBtn(index));
    }

    @Step("Confirm item deletion")
    public void confirmDeletion() {
        click(confirmDeletionBtn());
        shouldNotBeVisible(confirmDeletionBtn(), "Confirm deletion modal window is displayed after clicking \"Confirm\"");
    }

    @Step("Cancel item deletion")
    public void cancelDeletion() {
        click(cancelDeletionBtn());
        shouldNotBeVisible(cancelDeletionBtn(), "Confirm deletion modal window is displayed after clicking \"Cancel\"");
    }


    //---------------------------- CREDIT CARDS ------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement newBillAddressBtn() {
        return $(By.xpath("//div[@class='fc-left' and text()='Address Book']/following-sibling::*/button"));
    }

    public SelenideElement addNewCreditCardBtn() {
        return $(By.xpath("//div[text()='Credit Cards']/following-sibling::*/button"));
    }

    private SelenideElement billingName() {
        return $(By.xpath("//li[contains(@class, 'fc-credit-cards-new')]//li[@class='name']"));
    }

    public SelenideElement editCreditCardBtn(String ccIndex) {
//        return $(By.xpath("//div[contains(@class, 'credit-cards')]/div/ul/li[" + ccIndex + "]/div[1]/div/button[2]"));
        return $(By.xpath("(//div[contains(@class, 'credit-cards')]//*[contains(@class,'icon-edit')]/..)[" + ccIndex + "]"));
    }

    public SelenideElement deleteCreditCardBtn(String ccIndex) {
//        return $(By.xpath("//div[contains(@class, 'credit-cards')]/div/ul/li[" + ccIndex + "]/div[1]/div/button[1]"));
        return $(By.xpath("(//div[contains(@class, 'credit-cards')]//*[contains(@class,'icon-trash')]/..)[" + ccIndex + "]"));
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
        return $(xpath("//label[text()='Expiration Date']/following-sibling::*/div[1]/div/div/div/button"));
    }

    private SelenideElement monthVal(String monthNumber) {
        return $(xpath("//div[@class='fc-grid']/div[1]/div/div/div[3]/ul/li[" + monthNumber + "]"));
    }

    private SelenideElement yearDd() {
        return $(xpath("//label[text()='Expiration Date']/following-sibling::*/div[2]/div/div/div/button"));
    }

    private SelenideElement yearVal(String year) {
        return $(xpath("//div[@class='fc-grid']/div[2]/div/div/div[3]/ul/li[text()='" + year + "']"));
    }

    public SelenideElement chooseBtn(String addressIndex) {
        return $(By.xpath("//div[@class='fc-address-select-list']/div[" + addressIndex + "]/div[2]/button"));
    }
    // -------- -------- -------- --------

    public SelenideElement holderNameFldVal(String ccIndex) {
        return $(By.xpath("(//div[contains(@class, 'credit-cards')]//*[contains(@class,'fct-card-holder-name')])[" + ccIndex + "]"));
    }

    public SelenideElement expirationDateVal(String ccIndex) {
        return $(By.xpath("(//div[contains(@class, 'credit-cards')]//*[contains(@class,'payment-summary')]/div)[" + ccIndex + "]"));
    }

    public SelenideElement billCityVal(String ccIndex) {
        return $(By.xpath("//ul[@id='fct-customer-credit-cards-list']/li[" + ccIndex + "]//li[@class='city']"));
//        billCityVal.substring(0, billCityVal.indexOf(","));
    }

    public SelenideElement nameFldVal_billAddress(String ccIndex) {
        return $(By.xpath("//ul[@id='fct-customer-credit-cards-list']/li[" + ccIndex + "]//li[@class='name']"));
    }


    //------------------------------ HELPERS --------------------------------//

    @Step("Click \"Add New CC\" btn")
    public void clickAddNewCCBtn() {
        click( addNewCreditCardBtn() );
    }

    @Step("Click \"Edit\" btn next to <{0}th> credit card")
    public void clickEditCCBtn(String index) {
        click(editCreditCardBtn("1"));
    }

    @Step("Click \"Change\" next to billing address")
    public void clickChangeBillAddressBtn() {
        click(changeBillAddressBtn());
    }

    @Step("Add new credit card")
    public void fillOutNewCCForm(String holderName, String cardNumber, String cvv, String month, String year) {
        setHolderName(holderName);
        setCardNumber(cardNumber);
        setCVV(cvv);
        setExpirationDate(month, year);
    }

        @Step("Set \"Holder Name\" fld val to <{0}>")
        public void setHolderName(String holderName) {
            setFieldVal(holderNameFld(), holderName);
        }

        @Step("Set \"Card Number\" fld val to <{0}>")
        private void setCardNumber(String cardNumber) {
            setFieldVal_delayed(cardNumberFld(), cardNumber);
        }

        @Step("Set \"CVV\" fld val to <{0}>")
        private void setCVV(String cvv) {
            setFieldVal(cvvFld(), cvv);
        }

        @Step("Set expiration date to <{0}/{1}>")
        public void setExpirationDate(String month, String year) {
            setDdVal(monthDd(), month);
            setDdVal(yearDd(), year);
        }

    @Step("Add new billing address")
    public void addNewBillAddress(String name, String streetAddress1, String streetAddress2, String city, String state, String zipCode, String phoneNumber) {
        clickNewBillAddressBtn();
        addNewAddress(name, streetAddress1, streetAddress2, city, state, zipCode, phoneNumber);
        clickSave_();
        shouldHaveSize(addressBook(), 1, "Failed to create a new address.");
        chooseAddress("1");
        billingName().shouldHave(text(name)
                .because("Incorrect address seems to be set as a billing address; expected name: <" + name + ">, actual: <" + billingName() + ">."));
    }

        @Step("Click \"Add New Billing Address\" btn")
        private void clickNewBillAddressBtn() {
            click(newBillAddressBtn());
        }

        @Step("Choose <{0}th> address from the address book to set it as a billing address")
        public void chooseAddress(String index) {
            click(chooseBtn(index));
        }

    @Step("Check if credit card is created")
    public void assertCardAdded(String customerName) {
        $(By.xpath("//*[@id='fct-customer-credit-cards-list']//dd[text()='" + customerName + "']")).shouldBe(visible
                .because("Failed to create a new credit card."));
    }


    //--------------------------------- CONTACTS -----------------------------//
    //--------------------------------- ELEMENTS -----------------------------//

    public SelenideElement editBtn_contats() {
        return $(By.xpath("//div[text()='Contact Information']/following-sibling::*/button"));
    }

    public SelenideElement nameFld_contacts() {
        return $(By.xpath("//input[@id='nameField']"));
    }

    public SelenideElement nameVal_contacts() {
        return $(By.xpath("//form[contains(@class, 'customer-contacts')]/dl[1]/dd"));
    }

    public SelenideElement emailFld_contacts() {
        return $(By.xpath("//input[@id='emailField']"));
    }

    public SelenideElement emailVal_contacts() {
        return $(By.xpath("//form[contains(@class, 'customer-contacts')]/dl[2]/dd"));
    }

    public SelenideElement phoneNumberFld_contacts() {
        return $(By.xpath("//input[@id='phoneField']"));
    }

    public SelenideElement phoneNumberVal_contacts() {
        return $(By.xpath("//form[contains(@class, 'customer-contacts')]/dl[3]/dd"));
    }


    //--------------------------------- OVERVIEW -----------------------------//
    //--------------------------------- ELEMENTS -----------------------------//

    public SelenideElement nameVal_overview() {
        return $(By.xpath("//div[contains(@class, 'fc-customer-name-block')]/div[1]"));
    }

    public SelenideElement emailVal_overview() {
        return $(By.xpath("//div[contains(@class, 'fc-customer-name-block')]/div[2]"));
    }

    public SelenideElement phoneNumberVal_overview() {
        return $(By.xpath("//i[@class='icon-phone']/following-sibling::*"));
    }

    public double totalSalesVal() {
        SelenideElement totalSales = $(By.xpath("//div[contains(@class, 'customer-details')]/ul[2]/li[2]/span[1]"));
        String strVal = totalSales.getText();
        return Double.valueOf(strVal.substring(1, strVal.length()));
    }

    //--------------------------------- HELPERS -----------------------------//

    @Step("Click 'Edit' btn next to \"Contact Info\"")
    public void clickEditBtn_contacts() {
        click(editBtn_contats());
    }

    @Step("Set contact info \"Phone Number\" fld val to <{0}>")
    public void setPhoneNumber_contacts(String phoneNumber) {
        setFieldVal( phoneNumberFld_contacts(), phoneNumber );
    }

    @Step("Set contact info \"Name\" fld val to <{0}>")
    public void setName_contacts(String name) {
        setFieldVal( nameFld_contacts(), name );
    }

    @Step("Set contact info \"Email\" fld val to <{0}>")
    public void setEmail_contacts(String email) {
        setFieldVal( emailFld_contacts(), email );
    }


    //---------------------------- O R D E R S -------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement searchFld() {
        return $(By.xpath("//input[@placeholder='filter or keyword search']"));
    }

    public ElementsCollection ordersOnList() {
        return $$(By.xpath("//table[@class='fc-table']/tbody/a"));
    }

    //------------------------------ HELPERS --------------------------------//

    @Step("Get <{1}> parameter value of <{0}th> order on the list")
    public SelenideElement getOrderParamVal(int orderIndex, String paramName) {
        SelenideElement orderParamVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Order":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[2]"));
                break;
            case "Date/Time Placed":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[3]/time"));
                break;
            case "Modality":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[4]"));
                break;
            case "Order State":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[5]/span"));
                break;
            case "Payment State":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[6]"));
                break;
            case "Assignee":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[7]"));
                break;
            case "Total":
                orderParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + orderIndex + "]/td[8]/span"));
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
//    public void addFilter_arrowKeys(String firstStatement, String secondStatement, String thirdStatement) {
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

    public SelenideElement gcAvailableBalanceVal() {
        return $(By.xpath("//div[contains(@class, 'gc-value')]/span"));
    }

    public SelenideElement submitBtn() {
        return $(By.xpath("//button[@type='submit']"));
    }

    public SelenideElement availableBalance() {
        return $(By.xpath("//div[@class='fc-store-credits-summary-balance']/span"));
    }

    public ElementsCollection storeCreditsOnList() {
        return  $$(By.xpath("//tbody/tr[@class='fc-table-tr']"));
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

    public SelenideElement transactionTab() {
        return $(By.xpath("//a[text()='Transaction']"));
    }




    //------------------------------ HELPERS --------------------------------//

    @Step("Navigate to \"Store Credits\" tab")
    public void navToSCTab() {
        click( storeCreditTab() );
    }

    @Step("Navigate to \"Transactions\" tab")
    public void navToTransactionTab() {
        click( transactionTab() );
    }

    @Step("Click \"Issue Store Credit\" btn")
    public void clickNewSCBtn() {
        click( newSCBtn() );
    }

    @Step("Set \"Value\" fld val to <{0}>")
    public void setValue(String value) {
        setFieldVal( valueFld(), value );
    }

    @Step("Click \"Submit\"")
    public void clickIssueSCButton() {
        click( submitBtn() );
        sleep(2000);
    }

    @Step("Set SC type to {0}")
    public void selectType(String type) {
        setDdVal(scTypeDd(), type);
    }

    @Step("Set \"Gift Card Number\" fld val to <{0}>")
    public void setGCNumber(String gcCode) {
        setFieldVal( gcNumberFld(), gcCode);
        sleep(2000);
    }

    @Step("Get <{1}> parameter value of <{0}th> store credit on the list")
    public SelenideElement getSCParamVal(String scIndex, String paramName) {
        SelenideElement scParamVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Date/Time Issued":
                scParamVal = $(By.xpath("//tbody[@class='fc-table-body']/tr[" + scIndex + "]/td[2]/time"));
                break;
            case "Store Credit Id":
                scParamVal = $(By.xpath("//tbody/tr[" + scIndex + "]/td[3]"));
                break;
            case "Type":
                scParamVal = $(By.xpath("//tbody/tr[" + scIndex + "]/td[4]/div/div"));
                break;
            case "Issued By":
                scParamVal = $(By.xpath("//tbody/tr[" + scIndex + "]/td[5]/div/div"));
                break;
            case "Original Balance":
                scParamVal = $(By.xpath("//tbody/tr[" + scIndex + "]/td[6]/span"));
                break;
            case "Current Balance":
                scParamVal = $(By.xpath("//tbody/tr[" + scIndex + "]/td[7]/span"));
                break;
            case "Available Balance":
                scParamVal = $(By.xpath("//tbody/tr[" + scIndex + "]/td[8]/span"));
                break;
            case "State":
                scParamVal = $(By.xpath("//tbody/tr[" + scIndex + "]/td[9]/div/div[2]/div/span"));
                break;
        }
        return scParamVal;
    }

    @Step("Change \"State\" of {0}th SC on the list to <{1}>")
    public void changeSet(String scIndex, String state) {
        setState(scIndex, state);
        clickYes();
    }

        @Step("Set \"State\" dd val of <{0}th> SC on the list to <{1}>")
        public void setState(String scIndex, String state) {
            setDdVal(scStateDd(scIndex), state);
        }

    @Step("Change \"State\" of {0}th SC on the list to <{1}>")
    public void changeSet(String scIndex, String state,String reason) {
        setState(scIndex, state);
        setCancellationReason(reason);
        clickYes();
    }

        @Step("Set cancellation reason to <{0}>")
        public void setCancellationReason(String reason) {
            click( cancellationReason(reason) );
        }

    @Step("Get <{1}> parameter value of <{0}th> SC transaction on the list")
    public SelenideElement getTransactionParamVal(String transactionIndex, String paramName) {
        SelenideElement transactionParamVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Date/Time":
                transactionParamVal = $(By.xpath("//tbody[@class='fc-table-body']/tr[" + transactionIndex + "]/td[2]/time"));
                break;
            case "Transaction":
                transactionParamVal = $(By.xpath("//tbody[@class='fc-table-body']/tr[" + transactionIndex + "]/td[3]/div/div"));
                break;
            case "Amount":
                transactionParamVal = $(By.xpath("//tbody[@class='fc-table-body']/tr[" + transactionIndex + "]/td[4]/span"));
                break;
            case "Payment State":
                transactionParamVal = $(By.xpath("//tbody[@class='fc-table-body']/tr[" + transactionIndex + "]/td[5]/span"));
                break;
            case "Total Available Balance":
                transactionParamVal = $(By.xpath("//tbody[@class='fc-table-body']/tr[" + transactionIndex + "]/td[6]/span"));
                break;
        }
        return transactionParamVal;
    }
}