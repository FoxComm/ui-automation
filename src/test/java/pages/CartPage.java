package pages;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class CartPage extends BasePage {

    //------------------------ G E N E R A L    C O N T R O L S ------------------------//
    //----------------------------------------------------------------------------------//
    //----------------------------------- ELEMENTS -------------------------------------//

    public SelenideElement cartSummary() {
        return $(xpath("//div[text()='Cart Summary']"));
    }

    public double subtotalVal() {
        String subtotal = $(xpath("//dl[@class='rma-totals']/dd[1]/span")).getText();
        return Double.valueOf(subtotal.substring(1, subtotal.length()));
    }

    public double discountsVal() {
        String discounts = $(xpath("//dl[@class='rma-totals']/div[1]/dd[1]/span")).getText();
        return Double.valueOf(discounts.substring(1, discounts.length()));
    }

    public double newSubtotal() {
        String newSubtotal = $(xpath("//dl[@class='rma-totals']/div/dd[2]/span")).getText();
        return Double.valueOf(newSubtotal.substring(1, newSubtotal.length()));
    }

    public double shipping() {
        String shipping = $(xpath("//dl[@class='rma-totals']/div[2]/dd/span")).getText();
        return Double.valueOf(shipping.substring(1, shipping.length()));
    }

    public double tax() {
        String tax = $(xpath("//dl[@class='rma-totals']/dd[2]/span")).getText();
        return Double.valueOf(tax.substring(1, tax.length()));
    }

    public SelenideElement grandTotal() {
        return $(xpath("//dt[text()='Grand Total']/following-sibling::*/span"));
    }

    public double grandTotalVal() {
        return Double.valueOf(grandTotal().text().substring(1, grandTotal().text().length()));
    }

    public SelenideElement cartWarn() {
        return $(xpath("//div[@class='fc-order-messages']/div[text()='Cart is empty.']"));
    }

    public SelenideElement shipAddressWarn() {
        return $(xpath("//div[@class='fc-order-messages']/div[text()='No shipping address applied.']"));
    }

    public SelenideElement shipMethodWarn() {
        return $(xpath("//div[@class='fc-order-messages']/div[text()='No shipping method applied.']"));
    }

    public SelenideElement fundsWarn() {
        return $(xpath("//div[@class='fc-order-messages']/div[text()='Insufficient funds.']"));
    }

    public SelenideElement orderOverviewPanel() {
        return $(xpath("//div[@class=' fc-panel-list']"));
    }

    // assignees, watchers, customer info will be listed here

    public SelenideElement placeOrderBtn() {
        return $(xpath("//div[contains(@class, 'order-checkout')]/button"));
    }

    //---------------------------------------- HELPERS ----------------------------------------//

    @Step("Click \"Place Order\" btn")
    public void clickPlaceOderBtn() {
        click( placeOrderBtn() );
    }

    @Step("Assert that order doesn't have any warnings.")
    public void assertNoWarnings() {
        assertNoCartWarn();
        assertNoShipAddressWarn();
        assertNoShipMethodWarn();
        assertNoFundsWarn();
    }

        @Step("Assert that \"Cart is empty\" warning isn't displayed")
        private void assertNoCartWarn() {
            shouldNotBeVisible(cartWarn(), "'Cart is empty' warning is displayed.");
        }

        @Step("Assert that \"No shipping address\" warning isn't displayed")
        private void assertNoShipAddressWarn() {
            shouldNotBeVisible(shipAddressWarn(), "'No shipping address' warning is displayed.");
        }

        @Step("Assert that \"No shipping method\" warning isn't displayed")
        private void assertNoShipMethodWarn() {
            shouldNotBeVisible(shipMethodWarn(), "'No shipping method' warning is displayed.");
        }

        @Step("Assert that \"Insufficient funds\" warning isn't displayed")
        private void assertNoFundsWarn() {
            shouldNotBeVisible(fundsWarn(), "'Insufficient funds' warning is displayed.");
        }

    //------------------------ I T E M S    B L O C K ------------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement editBtn_items() {
        return $(xpath("//div[@class='fc-content-box fc-editable-content-box fc-line-items']/header/div[2]/button"));
    }

    public SelenideElement itemQty(String itemIndex) {

//        if (itemsInEditMode()) {
//            clickDoneBtn_items();
//        }

        return $(xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[5]"));
    }

    private SelenideElement lineItemSearchFld() {
        return $(xpath("//input[@class='fc-input fc-typeahead__input']"));
    }

    private SelenideElement doneBtn_items() {
        return $(xpath("//div[contains(@class, 'line-items')]/div/footer/button"));
    }

    private SelenideElement decreaseItemQtyBtn(String itemIndex) {
        return $(xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[5]/div/div[1]/button"));
    }

    private SelenideElement increaseItemQtyBtn(String itemIndex) {
        return $(xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[5]/div/div[2]/button"));
    }

    private SelenideElement itemQtyInputFld(String itemIndex) {
        return $(xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[5]/div/input"));
    }

    public SelenideElement deleteBtn_item(String itemIndex) {
        return $(xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[7]/button"));
    }

    public SelenideElement confirmDeletionBtn() {
        return $(xpath("//span[text()='Yes, Delete']/.."));
    }

    private SelenideElement cancelDeletionBtn() {
        return $(xpath("//div[@class='fc-modal-footer']/a"));
    }

    private SelenideElement lineItemSearchView_byName(String itemName) {
        return $(xpath("//ul[@class='fc-typeahead__items']/li/div/div[text()='" + itemName + "']"));
    }

    private SelenideElement lineItem_byName(String itemName) {
        return $(xpath("//tbody[@class='fc-table-body']/tr/td[text()='" + itemName + "']"));
    }

    public ElementsCollection cart() {
            return $$(xpath("//table[@class='fc-table']/tbody/tr/td[6]"));
    }

    public int itemsInCartAmount() {
        return cart().size();
    }

    private String indexOfLastItemInCart() {
        return String.valueOf(itemsInCartAmount());
    }


    //------------------------------- HELPERS --------------------------------//
    //------------------------------------------------------------------------//
    //-------------------------------- ITEMS ---------------------------------//

    @Step("Click \"Edit\" at \"Items\" block")
    public void clickEditBtn_items() {
        click( editBtn_items() );
        shouldBeVisible( doneBtn_items(), "Items 'Done' btn isn't visible - failed to enter editing mode." );
    }

    @Step("Click \"Done\" at \"Items\" block")
    public void clickDoneBtn_items() {
        click( doneBtn_items() );
        shouldNotBeVisible(doneBtn_items(), "'Done' btn is visible (it shouldn't)");
    }

    @Step("Add item to cart, searchQuery: {0}")
    public void addItemToCart(String searchQuery) {

        if ( !(itemsInEditMode()) ) {
            clickEditBtn_items();
        }
        shouldBeVisible(lineItemSearchFld(),
                "Failed to enter 'Edit' mode for line items");
        // itemIndex - index of a to be added item
        // it makes this test less dependent on initial itemsInCartAmount value when it comes to 1st assertion in this method
        String itemIndex = String.valueOf(itemsInCartAmount() + 1);

        searchForItem(searchQuery);
        addFoundItem(searchQuery);
        lineItem_byName(searchQuery).shouldBe(visible
                .because("Failed to add line, used search query: <" + searchQuery + ">"));
        clickDoneBtn_items();
        lineItem_byName(searchQuery).shouldBe(visible
                .because("Line item isn't displayed after clicking 'Done' btn."));

    }

    @Step("Set \"Search\" field val to <{0}>")
    private void searchForItem(String searchQuery) {
        setFieldVal(lineItemSearchFld(), searchQuery);
    }

    @Step("Click <{0}> in search view")
    private void addFoundItem(String searchQuery) {
        click(lineItemSearchView_byName(searchQuery));
    }

    @Step("Confirm item deletion")
    public void confirmDeletion() {
        click(confirmDeletionBtn());
        shouldNotBeVisible(confirmDeletionBtn(), "Confirmation modal window isn't auto-hidden");
//        sleep(2000);
    }

    @Step("Cancel item deletion")
    public void cancelDeletion() {
        click(cancelDeletionBtn());
        shouldNotBeVisible(cancelDeletionBtn(), "Confirmation modal window isn't auto-hidden");
//        sleep(2000);
    }

    @Step
    private void deleteItem(String itemIndex) {
        int expectedItemsAmount = cart().size() - 1;
        System.out.println("Deleting items... expectedItemsAmount: " + expectedItemsAmount);

        if ( !(itemsInEditMode()) ) {
            clickEditBtn_items();
        }

        click(deleteBtn_item(itemIndex));
        confirmDeletion();

        int actualItemsAmount = cart().size();
        System.out.println("actualItemsAmount: " + actualItemsAmount);

    }

    @Step("Remove all items from cart")
    public void clearCart() {

        if ( !(itemsInEditMode()) ) {
            clickEditBtn_items();
        }

        int timesToIterate = itemsInCartAmount();
        String itemIndex;
        for (int i = 1; i <= timesToIterate; i++) {
            itemIndex = String.valueOf(i);
            deleteItem(itemIndex);
        }

        clickDoneBtn_items();

    }

    public String getItemName(String index) {
        return lineItem_byName(index).getText();
    }

    private boolean itemsInEditMode() {
        return doneBtn_items().isDisplayed();
    }

    @Step("Increase {0}th item in cart qty by {1}")
    public void increaseItemQty(String itemIndex, int increaseBy) {
//        if ( !(doneBtn_items().is(visible)) ) {
//            clickEditBtn_items();
//        }
        int initialQty = Integer.valueOf(itemQtyInputFld(itemIndex).getValue());
        for (int i = 0; i < increaseBy; i++) {
            String expectedValue = String.valueOf(initialQty + 1);
            clickIncreaseQty(itemIndex);
            sleep(750);
            shouldHaveValue(itemQtyInputFld(itemIndex), expectedValue,
                    "Item QTY input field has incorrect value");
            initialQty += 1;
        }
    }

    @Step("Click increase quantity btn")
    private void clickIncreaseQty(String itemIndex) {
        click(increaseItemQtyBtn(itemIndex));
    }

    @Step("Decrease <{0}th> line item QTY by <{1}>")
    public void decreaseItemQty(String itemIndex, int decreaseBy) {
//        if ( !(doneBtn_items().is(visible)) ) {
//            clickEditBtn_items();
//        }
        int initialQty = Integer.valueOf(itemQtyInputFld(itemIndex).getValue());
        for (int i = 0; i < decreaseBy; i++) {
            String expectedValue = String.valueOf(initialQty - 1);
            clickDecreaseQty(itemIndex);
            sleep(750);
            if (Integer.valueOf(expectedValue) == 0) {
                confirmDeletionBtn().shouldBe(visible
                        .because("'Confirm deletion' modal window doesn't appear after item quantity is decreased to '0'."));
            } else {
                shouldHaveValue(itemQtyInputFld(itemIndex), expectedValue,
                        "Item QTY input field has incorrect value");
                initialQty -= 1;
            }
        }
    }

    @Step("Click \"Decrease Quantity\" btn")
    private void clickDecreaseQty(String itemIndex) {
        click(decreaseItemQtyBtn(itemIndex));
    }

    public void decreaseItemQtyBelowZero(String itemIndex) {
        int decreaseBy =  Integer.valueOf(itemQtyInputFld("1").getValue());
        System.out.println("decreaseBy" + decreaseBy);

        decreaseItemQty(itemIndex, decreaseBy);
        shouldBeVisible(confirmDeletionBtn(), "'Confirm' modal window isn't displayed");
    }

    @Step("Edit QTY of <{0}th> line item using input fld; <newQTY:{1}>")
    public void editItemQty(String itemIndex, String newQuantity) {
        if ( !(itemsInEditMode()) ) {
            clickEditBtn_items();
        }
        setLineItemQty(itemIndex, newQuantity);
        clickDoneBtn_items();
    }

        @Step("Set line item QTY to <{0}> using direct input")
        public void setLineItemQty(String index, String qty) {
            setFieldVal(itemQtyInputFld(index), qty);
        }



    //------------------- S H I P P I N G    A D D R E S S -------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement customerName_chosenShipAddress() {
        return $(xpath(".//ul[@class='fc-addresses-list']/li/div[3]/div/ul/li[1]"));
    }

    public SelenideElement editBtn_shipAddress() {
        return $(xpath("//div[contains(@class, 'shipping-address')]/header/div[2]/button"));
    }

    public SelenideElement doneBtn_shipAddress() {
        return $(xpath("//div[contains(@class, 'shipping-address')]/div/footer/button"));
    }

    public SelenideElement successIcon_shipAddress() {
        return $(xpath("//div[text()='Shipping Address']/preceding-sibling::*/i[contains(@class, 'success')]"));
    }

    public SelenideElement warningIcon_shipAddress() {
        return $(xpath("//div[text()='Shipping Address']/preceding-sibling::*/i[contains(@class, 'warning')]"));
    }

    public SelenideElement editBtn_chosenAddress() {
        return $(xpath("//li[@class='fc-card-container fc-address is-active']/div/div/button[2]"));
    }

    public SelenideElement deleteBtn_chosenAddress() {
        return $(xpath("//li[contains (@class, 'address is-active')]/div[2]/div/button[1]"));
    }

    private SelenideElement deleteBtn_inAddressBook(String addressIndex) {
        return $(xpath("//div[@class='fc-tile-selector__items']/div[" + addressIndex + "]/li/div[2]/div/button[1]"));
    }

    public SelenideElement addNewAddressBtn() {
        return $(xpath("//div[text()='Address Book']/following-sibling::*"));
    }

    public SelenideElement chosenAddress() {
        return $(xpath("//ul[@class='fc-addresses-list']//ul[@class='fc-address-details']"));
    }

    public SelenideElement addressBookHeader() {
        return $(xpath("//div[text()='Address Book']"));
    }

    private ElementsCollection chooseAddressBtns() {
        return $$(xpath("//span[text()='Choose']/.."));
    }

    private ElementsCollection namesInAddressBook() {
        return $$(xpath("//li[@class='name']"));
    }

    public SelenideElement nameOnAddressCard(int addressIndex) {
        return namesInAddressBook().get(addressIndex - 1);
    }

    public SelenideElement defaultShipAddressChkbox(String addressIndex) {
        return $(xpath("//div[" + addressIndex + "]/li/div/label/div"));
    }
    public SelenideElement defaultShipAddressChkbox_input(String addressIndex) {
        return $(xpath("//div[" + addressIndex + "]/li/div/label/div/input"));
    }

    // ----------- >> NEW ADDRESS FORM
    public SelenideElement editAddressBtn() {
        return $(xpath("//div[@class='fc-content-box fc-editable-content-box fc-shipping-methods']/header/div[2]/button"));
    }

    public SelenideElement nameFld() {
        return $(xpath("//input[@name='name']"));
    }

    private SelenideElement address1Fld() {
        return $(xpath(".//input[@name='address1']"));
    }

    private SelenideElement address2Fld() {
        return $(xpath(".//input[@name='address2']"));
    }

    private SelenideElement cityFld() {
        return $(xpath(".//input[@name='city']"));
    }

    private SelenideElement stateDd() {
        return $(xpath("//label[text()='State']/following-sibling::*[1]/div[2]/button"));
    }

    private SelenideElement stateDdValue(String stateName) {
        return $(xpath("//li[text()='" + stateName +"']"));
    }

    private SelenideElement zipCodeFld() {
        return $(xpath("//input[@name='zip']"));
    }

    private SelenideElement phoneNumberFld() {
        return $(xpath("//input[@name='phoneNumber']"));
    }

    private SelenideElement cancelBtn_addressForm() {
        return $(xpath("//a[text()='Cancel']"));
    }

    public SelenideElement addressDetails() {
        return $(xpath("//ul[@class='fc-address-details']"));
    }


    //------------------------------- HELPERS --------------------------------//

    @Step("Click \"Edit\" btn at chosen shipping address")
    public void clickEditBtn_chosenAddress() {
        click(deleteBtn_chosenAddress());
    }

    @Step("Click \"Delete\" btn at chosen shipping address")
    public void removeChosenAddress() {
        click( deleteBtn_chosenAddress() );
        click( confirmDeletionBtn() );
    }

    @Step("Click \"Done\" at 'Shipping Address' block")
    public void clickDoneBtn_shipAddress() {
        click( doneBtn_shipAddress() );
        shouldNotBeVisible(nameFld(),
                "'New Address' form isn't closed after clicking 'Save'");
    }

    @Step("Choose existing shipping address")
    public void chooseShipAddress(int addressIndex) {

        String expectedResult = nameOnAddressCard(addressIndex).text();
        click( chooseAddressBtns(), 1 );

        customerName_chosenShipAddress().shouldHave(text(expectedResult)
                .because("Failed to choose shipping address from address book."));

    }

    @Step("Set <{0}th> shipping address as default")
    public void setAddressAsDefault(String addressIndex) {
        click( defaultShipAddressChkbox(addressIndex) );
    }

    @Step("Add new shipping address")
    public void addNewAddress(String name, String streetAddress1, String streetAddress2, String city, String state, String zipCode, String phoneNumber) {

        clickNewAddressBtn();
        setName(name);
        setAddress1(streetAddress1);
        setAddress2(streetAddress2);
        setFieldVal( cityFld(), city );
        setCity(city);
        setState(state);
        setZip(zipCode);
        setPhoneNumber(phoneNumber);
        // assertion for a known bug
        assertStateIsntReset();
        clickSave();
        // wait till changes in address book will be displayed - customer name on any address should be visible
        shouldBeVisible( $(xpath("//li[@class='name']")),
                "'New shipping address' form isn't closed after clicking 'Save'" );

    }

        @Step("Click \"Add New Address\" button")
        private void clickNewAddressBtn() {
            click( addNewAddressBtn() );
        }

        @Step("Set \"Name\" fld vla to <{0}>")
        public void setName(String name) {
            setFieldVal( nameFld(), name );
        }

        @Step("Set \"Address 1\" fld val to <{0}>")
        private void setAddress1(String streetAddress1) {
            setFieldVal( address1Fld(), streetAddress1 );
        }

        @Step("Set \"Address\" fld val to <{0}>")
        private void setAddress2(String streetAddress2) {
            setFieldVal( address2Fld(), streetAddress2 );
        }

        @Step("Set \"City\" fld val to <{0}>")
        private void setCity(String city) {
            setFieldVal( cityFld(), city );
        }

        @Step("Set shipping state to <{0}>")
        private void setState(String state) {
            setDdVal(stateDd(), state);
        }

        @Step("Set \"Zip Code\" fld val to <{0}>")
        private void setZip(String zipCode) {
            setFieldVal( zipCodeFld(), zipCode );
        }

        @Step("Set \"Phone Number\" fld val to <{0}>")
        private void setPhoneNumber(String phoneNumber) {
            setFieldVal_delayed( phoneNumberFld(), phoneNumber );
        }

        @Step("Assert that \"State\" dd val isn't reset")
        private void assertStateIsntReset() {
            stateDd().shouldNotHave(text("- Select -")
                    .because("'State' is reset to default value"));
        }

    @Step("Click \"Edit\" btn at \"Shipping Address\" block")
    public void clickEditBtn_shipAddress() {
        if ( !(addNewAddressBtn().is(visible)) ) {
            click( editBtn_shipAddress() );
        }
        shouldBeVisible( doneBtn_shipAddress(), "Items 'Done' btn isn't visible - failed to enter editing mode." );
    }

    private boolean isAddressInEditMode() {
        return addNewAddressBtn().isDisplayed();
    }

//    @Step
//    public void restorePageDefaultState() {
//
//        while ( !(editBtn_items().is(visible)) ) {
//
//            if ( confirmDeletionBtn().is(visible) ) {
//                click( cancelDeletionBtn() );
//            }
//            if ( saveBtn_addressForm().is(visible) ) {
//                click( cancelBtn_addressForm() );
//            }
//            if ( doneBtn_shipAddress().is(visible) ) {
//                click( doneBtn_shipAddress() );
//            }
//
//        }
//
//    }

    @Step("Remove all addresses from address book")
    public void clearAddressBook() {

        ElementsCollection addressBook = $$(xpath("//div[@class='fc-tile-selector__items']/div/li/div[2]/div/button[1]"));
        int addressesAmount = addressBook.size();
        System.out.println("Addresses in AB: " + addressesAmount);

        for (int i = 0; i < addressesAmount; i++) {
            click( deleteBtn_inAddressBook("1") );
            click( confirmDeletionBtn() );
//            sleep(750);
            shouldNotBeVisible(confirmDeletionBtn(), "Confirmation modal window isn't auto-hidden");
        }

    }

    @Step("Set an address from address book as a shipping address.")
    public void setShipAddress() {

        click( editBtn_shipAddress() );
        chooseShipAddress(1);
        click( doneBtn_shipAddress() );

    }



    //-------------------- S H I P P I N G    M E T H O D --------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement editBtn_shipMethod() {
        return $(xpath("//div[contains(@class, 'shipping-method')]/header/div[2]/button"));
    }

    // should be clicked with 'jsClick(element)'
    public SelenideElement shipMethodRdbtn(String index) {
        return $(xpath("//table[@class='fc-table']/tbody/tr[" + index + "]//input"));
    }

    public SelenideElement doneBtn_shipMethod() {
        return $(xpath("//div[contains(@class, 'shipping-method')]/div/footer/button"));
    }

    public SelenideElement editSelectedShipMethodBtn() {
        return $(xpath("//div[@class='fc-right']/button"));
    }

    private SelenideElement successIcon_shipMethod() {
        return $(xpath("//div[text()='Shipping Method']/preceding-sibling::*/i[contains(@class, 'success')]"));
    }

    private SelenideElement warningIcon_shipMethod() {
        return $(xpath("//div[text()='Shipping Method']/preceding-sibling::*/i[contains(@class, 'warning')]"));
    }


    //------------------------------- HELPERS --------------------------------//

    @Step("Check if \"Shipping Method\" is defined")
    public void assertShipMethodDefined() {
        successIcon_shipMethod().shouldBe(visible
                .because("Success icon isn't deisplayed next to 'Shipping Method' block"));
        shipMethodWarn().shouldNotBe(visible
                .because("Shipping method warning is displayed"));
    }

    @Step("Set shipping method")
    public void setShipMethod() {
        click( editBtn_shipMethod() );
        clickEditBtn_shipMethod();
        selectShipMethod("1");
        click( doneBtn_shipMethod() );
        assertShipMethodDefined();
    }

    @Step("Click \"Edit\" at \"Shipping Method\" block")
    public void clickEditBtn_shipMethod() {
        click( editBtn_shipMethod() );
    }

    @Step("Select <{0}th> shipping method")
    public void selectShipMethod(String methodIndex) {
        jsClick( shipMethodRdbtn(methodIndex) );
    }

    @Step("Click \"Done\" at 'Shipping Method' block")
    public void clickDoneBtn_shipMethod() {
        click( doneBtn_shipMethod() );
    }






    //--------------------- P A Y M E N T    M E T H O D ---------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement editBtn_payment() {
        return $(xpath("//div[contains(@class, 'order-payment')]/header/div[2]/button"));
    }

    public SelenideElement doneBtn_payment() {
        return $(xpath("//div[contains(@class, 'order-payment')]/div/footer/button"));
    }

    public SelenideElement newPaymentBtn() {
        return $(xpath("//div[contains(@class, 'order-payment')]/header/div[2]/button"));
    }

    private SelenideElement paymentTypeDd() {
        return $(xpath("//label[contains(@class, 'payment-type')]/following-sibling::*/div[2]/div"));
    }

    private SelenideElement paymentTypeVal(String paymentType) {
        return $(xpath("//li[text()='"+ paymentType + "']"));
    }

    public SelenideElement appliedAmount() {
        return $(xpath("//tr[contains(@class, 'payment-row')]/td[2]/span"));
    }

    // ----------- >> NEW CREDIT CARD FORM
    private SelenideElement newCreditCardBtn() {
        return $(xpath("//div[contains(@class, 'new-order-payment')]/following-sibling::*/div/div/button"));
    }

    private SelenideElement holderNameFld() {
        return $(xpath("//input[@name='holderName']"));
    }

    private SelenideElement cardNumberFld() {
        return $(xpath("//input[@name='cardNumber']"));
    }

    private SelenideElement cvvFld() {
        return $(xpath("//input[@name='cvv']"));
    }

    private SelenideElement monthDd() {
        return $(xpath("//label[text()='Expiration Date']/following-sibling::*/div[1]/div/div/div[2]/button"));
    }

    private SelenideElement monthVal(String monthNumber) {
        return $(xpath("//div[@class='fc-grid']/div[1]/div/div/div[3]/ul/li[" + monthNumber + "]"));
    }

    private SelenideElement yearDd() {
        return $(xpath("//label[text()='Expiration Date']/following-sibling::*/div[2]/div/div/div[2]/button"));
    }

    private SelenideElement yearVal(String year) {
        return $(xpath("//div[@class='fc-grid']/div[2]/div/div/div[3]/ul/li[text()='" + year + "']"));
    }

    private SelenideElement chooseBtn() {
        return $(xpath("//span[text()='Choose']/.."));
    }

    public SelenideElement addPaymentBtn() {
        return $(xpath("//span[text()='Add Payment Method']/.."));
    }
    // -------- -------- -------- --------

    public SelenideElement gcNumberFld() {
        return $(xpath("//input[@name='giftCardCode']"));
    }

    public SelenideElement amountToUseFld() {
        return $(xpath("//input[@name='currencyInput']"));
    }

    public SelenideElement editPaymentBtn() {
        return $(xpath("//td[contains(@class, 'orders_payments')]/button[1]"));
    }

    public SelenideElement deletePaymentBtn() {
        return $(xpath("//td[contains(@class, 'orders_payments')]/button[2]"));
    }

    public SelenideElement gcAvailableBalance() {
        return $(xpath("//div[text()='Available Balance']/following-sibling::*/span"));
    }

    public Double gcAvailableBalanceVal() {
        String  availableBalance = $(xpath("//div[text()='Available Balance']/following-sibling::*/span")).getText();
        return Double.valueOf(availableBalance.substring(1, availableBalance.length()));
    }

    public SelenideElement gcNewAvailableBalance() {
        return $(xpath("//div[text()='New Available Balance']/following-sibling::*/span"));
    }

    public Double gcNewAvailableBalanceVal() {
        String  newAvailableBalance = $(xpath("//div[text()='New Available Balance']/following-sibling::*/span")).getText();
        return Double.valueOf(newAvailableBalance.substring(1, newAvailableBalance.length()));
    }


    //------------------------------- HELPERS --------------------------------//

    @Step("Click \"Edit\" at \"Payment Method\" block")
    public void clickEditBtn_payment() {
        click(editBtn_payment());
        shouldBeVisible( doneBtn_payment(), "Payment method's 'Done' btn isn't visible - failed to enter editing mode." );
    }

    @Step("Click \"Add New Payment Method\" button")
    public void clickNewPayMethodBtn() {
        click(newPaymentBtn());
        shouldBeVisible(paymentTypeDd(), "Payment Type' dd isn't visible");
    }

    @Step("Add new credit card")
    public void addNewCreditCard(String holderName, String cardNumber, String cvv, String month, String year) {

        clickNewCCBtn();
        clickNewCCBtn();
        setHolderName(holderName);
        setCardNumber(cardNumber);
        setCVV(cvv);
        setExpirationDate(month, year);
        clickChooseBtn();
        clickAddPayMethodBtn();

    }

        @Step("Click \"Add New Credit Card\" btn")
        private void clickNewCCBtn() {
            click(newCreditCardBtn());
            shouldBeVisible( holderNameFld(), "New credit card form isn't displayed" );
        }

        @Step("Select payment type: <{0}>")
        public void selectPaymentType(String paymentType) {
            setDdVal(paymentTypeDd(), paymentType);
        }

        @Step("Set \"Holder Name\" value to <{0}>")
        private void setHolderName(String holderName) {
            setFieldVal( holderNameFld(), holderName );
        }

        @Step("Set \"Card Number\" value to <{0}>'")
        private void setCardNumber(String cardNumber) {
            setFieldVal_delayed( cardNumberFld(), cardNumber );
        }

        @Step("Set \"CVV\" value to <{0}>")
        private void setCVV(String cvv) {
            setFieldVal( cvvFld(), cvv );
        }

        @Step("Set expiration date: <{0}/{1}>")
        private void setExpirationDate(String month, String year) {
            click( monthDd() );
            click( monthVal(month) );
            click( yearDd() );
            click( yearVal(year) );
        }

        @Step("Click \"Choose\" at existing shipping address")
        private void clickChooseBtn() {
            click( chooseBtn() );
        }

        @Step("Click 'Add Payment Method' btn")
        public void clickAddPayMethodBtn() {
            click( addPaymentBtn() );
        }

    @Step("Assert that credit card is added")
    public void assertCardAdded() {
//        editBtn_payment().shouldBe(visible);
        shouldBeVisible($(xpath("//strong[contains(text(), 'xxxx xxxx xxxx')]")),
                "Failed to add credit card to order as a payment method.");
    }

    @Step("Set \"Gift Card Number\" field value to <{0}>")
    public void setGCNumber(String gcCode){
        setFieldVal( gcNumberFld(), gcCode );
    }

    @Step("Set \"Amount To Use\" value to '<{0}>")
    public void setAmountToUse(String amount) {
        clearField( amountToUseFld() );
        setFieldVal( amountToUseFld(), amount );
    }

    @Step("Click \"Done\" at 'Payment Method' block")
    public void clickDoneBtn_payment() {
        click( doneBtn_payment() );
        shouldNotBeVisible(doneBtn_payment(), "'Done' button is visible at 'Payment Method' block (it shouldn't)");
    }

    @Step("Add credit card as a payment method")
    public void addPaymentMethod_CC(String holderName, String cardNumber, String cvv, String month, String year) {
        click( editBtn_payment() );
        click( newPaymentBtn() );
        selectPaymentType("Credit Card");
        addNewCreditCard(holderName, cardNumber, cvv, month, year);

        //remove this assert - call it directly in tests after adding credit card
        assertCardAdded();
    }

    @Step("Add gift card as a payment method")
    public void addPaymentMethod_GC(String gcNumber, String amountToUse) {
        clickEditBtn_payment();
        clickNewPayMethodBtn();
        selectPaymentType("Gift Card");
        setGCNumber(gcNumber);
        setAmountToUse(amountToUse);
        clickAddPayMethodBtn();
    }

    @Step("Add store credit as a payment method")
    public void addPaymentMethod_SC(String amountToUse) {

        clickEditBtn_payment();
        clickNewPayMethodBtn();
        selectPaymentType("Store Credit");
        clearField(amountToUseFld());
        setFieldVal( amountToUseFld(), amountToUse );
        setAmountToUse(amountToUse);
        clickAddPayMethodBtn();

    }

}