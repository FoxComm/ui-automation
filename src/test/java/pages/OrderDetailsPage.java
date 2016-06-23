package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class OrderDetailsPage extends OrdersPage {

    //------------------- G E N E R A L    C O N T R O L S -------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement placeOrderBtn() {
        return $(By.xpath("//div[contains(@class, 'order-checkout')]/button"));
    }

    public SelenideElement cartSummary() {
        return $(By.xpath("//div[text()='Cart Summary']"));
    }

    public double subtotalVal() {
        String subtotal = $(By.xpath("//dl[@class='rma-totals']/dd[1]/span")).getText();
        return Double.valueOf(subtotal.substring(1, subtotal.length()));
    }

    public double discountsVal() {
        String discounts = $(By.xpath("//dl[@class='rma-totals']/div[1]/dd[1]/span")).getText();
        return Double.valueOf(discounts.substring(1, discounts.length()));
    }

    public double newSubtotal() {
        String newSubtotal = $(By.xpath("//dl[@class='rma-totals']/div/dd[2]/span")).getText();
        return Double.valueOf(newSubtotal.substring(1, newSubtotal.length()));
    }

    public double shipping() {
        String shipping = $(By.xpath("//dl[@class='rma-totals']/div[2]/dd/span")).getText();
        return Double.valueOf(shipping.substring(1, shipping.length()));
    }

    public double tax() {
        String tax = $(By.xpath("//dl[@class='rma-totals']/dd[2]/span")).getText();
        return Double.valueOf(tax.substring(1, tax.length()));
    }

    public double grandTotal() {
        String grandTotal = $(By.xpath("//dt[text()='Grand Total']/following-sibling::*/span")).getText();
        System.out.println(grandTotal);
        return Double.valueOf(grandTotal.substring(1, grandTotal.length()));
    }

    public SelenideElement cartWarn() {
        return $(By.xpath("//div[@class='fc-order-messages']/div[1]"));
    }

    public SelenideElement shipAddressWarn() {
        return $(By.xpath("//div[@class='fc-order-messages']/div[2]"));
    }

    public SelenideElement shipMethodWarn() {
        return $(By.xpath("//div[@class='fc-order-messages']/div[3]"));
    }

    public SelenideElement fundsWarn() {
        return $(By.xpath("//div[@class='fc-order-messages']/div[4]"));
    }

    private SelenideElement orderOverviewPanel() {
        return $(By.xpath("//div[@class=' fc-panel-list']"));
    }

    // assignees, watchers, customer info will be listed here

    //------------------------------- HELPERS --------------------------------//

    @Step("Assert that order doesn't have any warnings.")
    public void assertNoWarnings() {

        assertNoShipAddressWarn();
        assertNoShipMethod();
        assertNoFundsWarn();
        assertNoFundsWarn();

    }

        @Step("Assert that 'Cart is empty' warning isn't displayed")
        public void assertNoCartWarn() {
            Assert.assertTrue( !cartWarn().is(visible), "'Cart is empty' warning is displayed." );
        }

        @Step("Assert that 'No shipping address' warning isn't displayed")
        public void assertNoShipAddressWarn() {
            Assert.assertTrue( !shipAddressWarn().is(visible), "'No shipping address' warning is displayed." );
        }

        @Step("Assert that 'No shipping method' warning isn't displayed")
        public void assertNoShipMethod() {
            Assert.assertTrue( !shipMethodWarn().is(visible), "'No shipping method' warning is displayed." );
        }

        @Step("Assert that 'Insufficient funds' warning isn't displayed")
        public void assertNoFundsWarn() {
            Assert.assertTrue( !fundsWarn().is(visible), "'Insufficient funds' warning is displayed." );
        }

    @Step("Assert that order's state is '{0}'.")
    public void assertOrderState(String expectedState) {

        System.out.println(orderOverviewPanel().waitUntil(visible, 10000));

        if (Objects.equals( expectedState, "Remorse Hold" ) ||
            Objects.equals( expectedState, "Manual Hold" ) ||
            Objects.equals( expectedState, "Fraud Hold" )) {

            Assert.assertTrue($(By.xpath("//div[text()='" + expectedState + "']")).is(visible),
                    "Order is not on " + expectedState + ".");

        } else if (Objects.equals( expectedState, "Fulfillment Started" ) ||
                    Objects.equals( expectedState, "Canceled" )) {

            Assert.assertTrue($(By.xpath("//div[@class=' fc-panel-list']/div[1]/div/span[text()='" + expectedState + "']")).is(visible),
                    "Order is not in '" + expectedState + "' state");

        }

    }



    //------------------------ I T E M S    B L O C K ------------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement editBtn_items() {
        return $(By.xpath("//div[@class='fc-content-box fc-editable-content-box fc-line-items']/header/div[2]/button"));
    }

    private SelenideElement addItemFld() {
        return $(By.xpath("//input[@class='fc-input fc-typeahead__input']"));
    }

    private SelenideElement doneBtn_items() {
        return $(By.xpath("//div[contains(@class, 'line-items')]/div/footer/button"));
    }

    private SelenideElement decreaseItemQtyBtn(String itemIndex) {
        return $(By.xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[5]/div/div[1]/button"));
    }

    private SelenideElement increaseItemQtyBtn(String itemIndex) {
        return $(By.xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[5]/div/div[2]/button"));
    }

    private SelenideElement itemQtyInputFld(String itemIndex) {
        return $(By.xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[5]/div/input"));
    }

    public SelenideElement deleteBtn_item(String itemIndex) {
        return $(By.xpath("//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[7]/button"));
    }

    public SelenideElement deleteConfirmBtn() {
        return $(By.xpath("//span[text()='Yes, Delete']/.."));
    }

    private SelenideElement cancelDeletionBtn() {
        return $(By.xpath("//div[@class='fc-modal-footer']/a"));
    }

    private SelenideElement itemInSearchResults(String itemIndex) {
        return $(By.xpath("//div[@class='fc-typeahead__menu _visible']/ul/li/div[" + itemIndex + "]"));
    }

    private SelenideElement itemName(String itemIndex) {
        return $(By.xpath("//div[@class='fc-table__table']/table/tbody/tr[" + itemIndex + "]/td[2]"));
    }

    public List<SelenideElement> cart() {
        return $$(By.xpath("//table[@class='fc-table']/tbody/tr/td[6]"));
    }

    public int itemsInCartAmount() {
        return cart().size();
    }

    private String indexOfLastItemInCart() {
        return String.valueOf(itemsInCartAmount());
    }


    //------------------------------- HELPERS --------------------------------//

    public void clickEditBtn_items() {

        clickBtn( editBtn_items() );
        elementIsVisible( doneBtn_items() );

    }

    @Step("Apply changes to 'Items' block")
    public void applyChangesToItems() {
        clickBtn( doneBtn_items() );
        doneBtn_items().shouldNot(visible);
    }

    @Step("Add item to cart, searchQuary: {0}")
    public void addItemToCart(String searchQuery) {

        if ( !(isItemsInEditMode()) ) {
            clickEditBtn_items();
        }
        addItemFld().shouldBe(visible);
        // itemIndex - index of a to be added item
        // it makes this test less dependent on initial itemsInCartAmount value when it comes to 1st assertion in this method
        String itemIndex = String.valueOf(itemsInCartAmount() + 1);

        addItemFld().setValue(searchQuery);
        itemInSearchResults("1").click();
        itemName(itemIndex).shouldBe(visible);
        Assert.assertEquals(itemName(indexOfLastItemInCart()).getText(), searchQuery);

        applyChangesToItems();
        Assert.assertEquals(itemName(indexOfLastItemInCart()).getText(), searchQuery);

    }

    @Step("Confirm item deletion")
    public void confirmDeletion() {
        deleteConfirmBtn().click();
        deleteConfirmBtn().shouldNot(visible);
        sleep(2000);
    }

    @Step("Cancel item deletion")
    public void cancelDeletion() {
        cancelDeletionBtn().click();
        cancelDeletionBtn().shouldNot(visible);
        sleep(2000);
    }

    @Step
    private void deleteItem(String itemIndex) {
        int expectedItemsAmount = cart().size() - 1;
        System.out.println("Deleting items... expectedItemsAmount: " + expectedItemsAmount);

        if ( !(isItemsInEditMode()) ) {
            clickEditBtn_items();
        }

        clickBtn(deleteBtn_item(itemIndex));
        confirmDeletion();

        int actualItemsAmount = cart().size();
        System.out.println("actualItemsAmount: " + actualItemsAmount);

    }

    @Step("Remove all items from cart")
    public void clearCart() {

        if ( !(isItemsInEditMode()) ) {
            clickEditBtn_items();
        }

        int timesToIterate = itemsInCartAmount();
        String itemIndex;
        for (int i = 1; i <= timesToIterate; i++) {
            itemIndex = String.valueOf(i);
            deleteItem(itemIndex);
        }

        applyChangesToItems();

    }

    @Step
    public String getItemName(String index) {
        return itemName(index).getText();
    }

    @Step
    private boolean isItemsInEditMode() {
        return doneBtn_items().isDisplayed();
    }

    @Step
    public String getItemQty(String itemIndex) {

        if (isItemsInEditMode()) {
            applyChangesToItems();
        }

        return $(By.xpath(".//table[@class='fc-table']/tbody/tr[" + itemIndex + "]/td[5]")).getText();
    }

    @Step
    public void increaseItemQty(String itemIndex, int increaseBy) {

        if ( !(doneBtn_items().is(visible)) ) {
            clickEditBtn_items();
        }

        for (int i = 0; i < increaseBy; i++) {
            increaseItemQtyBtn(itemIndex).click();
            sleep(1000);
        }

    }

    @Step
    public void decreaseItemQty(String itemIndex, int decreaseBy) {

        if ( !(doneBtn_items().is(visible)) ) {
            clickEditBtn_items();
        }

        for (int i = 0; i < decreaseBy; i++) {
            decreaseItemQtyBtn(itemIndex).click();
            sleep(1000);
        }

    }

    @Step
    public void decreaseItemQtyBelowZero(String itemIndex) {

        int decreaseBy =  Integer.valueOf(getItemQty("1"));

        decreaseItemQty(itemIndex, decreaseBy);
        deleteConfirmBtn().shouldBe(visible);

    }

    @Step
    public void editItemQty(String itemIndex, String newQuantity) {

        if ( !(isItemsInEditMode()) ) {
            clickEditBtn_items();
        }

        itemQtyInputFld(itemIndex).setValue(newQuantity);
        applyChangesToItems();

    }



    //------------------- S H I P P I N G    A D D R E S S -------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement editBtn_shipAddress() {
        return $(By.xpath("//div[contains(@class, 'shipping-address')]/header/div[2]/button"));
    }

    public SelenideElement doneBtn_shipAddress() {
        return $(By.xpath("//div[contains(@class, 'shipping-address')]/div/footer/button"));
    }

    public SelenideElement editBtn_chosenAddress() {
        return $(By.xpath("//li[@class='fc-card-container fc-address is-active']/div/div/button[2]"));
    }

    public SelenideElement deleteBtn_chosenAddress() {
        return $(By.xpath("//li[contains (@class, 'address is-active')]/div[2]/div/button[1]"));
    }

    private SelenideElement deleteBtn_inAddressBook(String addressIndex) {
        return $(By.xpath("//div[@class='fc-tile-selector__items']/div[" + addressIndex + "]/li/div[2]/div/button[1]"));
    }

    private SelenideElement addNewAddressBtn() {
        return $(By.xpath("//div[text()='Address Book']/following-sibling::*"));
    }

    public SelenideElement chosenAddressHeader() {
        return $(By.xpath("//h3[@class='fc-shipping-address-sub-title']"));
    }

    public SelenideElement addressBookHeader() {
        return $(By.xpath("//div[text()='Address Book']"));
    }

    private List<SelenideElement> chooseAddressBtns() {
        return $$(By.xpath("//span[text()='Choose']/.."));
    }

    private List<SelenideElement> listOfNamesInAddressBook() {
        return $$(By.xpath("//li[@class='name']"));
    }

    public String getNameFromAddressBook(int addressIndex) {
        return listOfNamesInAddressBook().get(addressIndex - 1).getText();
    }

    public SelenideElement defaultShipAddressChkbox(String addressIndex) {
        return $(By.xpath("//div[" + addressIndex + "]/li/div/label/div"));
    }
    public SelenideElement defaultShipAddressChkbox_input(String addressIndex) {
        return $(By.xpath("//div[" + addressIndex + "]/li/div/label/div/input"));
    }

    // ----------- >> NEW ADDRESS FORM

    public SelenideElement editAddressBtn() {
        return $(By.xpath("//div[@class='fc-content-box fc-editable-content-box fc-shipping-methods']/header/div[2]/button"));
    }

    public SelenideElement nameFld() {
        return $(By.xpath("//input[@name='name']"));
    }

    private SelenideElement address1Fld() {
        return $(By.xpath(".//input[@name='address1']"));
    }

    private SelenideElement address2Fld() {
        return $(By.xpath(".//input[@name='address2']"));
    }

    private SelenideElement cityFld() {
        return $(By.xpath(".//input[@name='city']"));
    }

    private SelenideElement stateDropdown() {
        return $(By.xpath("//ul[@class='fc-address-form-fields']/li[7]/div/div/div[2]/button"));
    }

    private SelenideElement stateDropdownValue(String stateName) {
        return $(By.xpath("//li[text()='" + stateName +"']"));
    }

    private SelenideElement zipCodeFld() {
        return $(By.xpath("//input[@name='zip']"));
    }

    private SelenideElement phoneNumberFld() {
        return $(By.xpath("//input[@name='phoneNumber']"));
    }

    private SelenideElement saveBtn_addressForm() {
        return $(By.xpath("//span[text()='Save']/.."));
    }

    private SelenideElement cancelBtn_addressForm() {
        return $(By.xpath("//a[text()='Cancel']"));
    }


    //------------------------------- HELPERS --------------------------------//

    public String getCustomerName_chosenShipAddress() {
        return $(By.xpath(".//ul[@class='fc-addresses-list']/li/div[3]/div/ul/li[1]")).getText();
    }

    @Step
    public void clickEditBtn_chosenAddress() {
        deleteBtn_chosenAddress().click();       // ?????
        nameFld().shouldBe(visible);
    }

    @Step
    public void applyChangesToAddress() {
        clickBtn( saveBtn_addressForm() );
        elementNotVisible( nameFld() );
    }

    @Step
    public void chooseShipAddress(int addressIndex) {

        String expectedResult = getNameFromAddressBook(addressIndex);
        clickBtn( chooseAddressBtns(), 1 );
        elementIsVisible( chosenAddressHeader() );

        String actualResult = getCustomerName_chosenShipAddress();
        Assert.assertEquals(expectedResult, actualResult, "Chosen address isn't displayed.");

    }

    @Step
    public void addNewAddress(String name, String streetAddress, String city, String state, String zipCode, String phoneNumber) {

        clickBtn( addNewAddressBtn() );
        setFieldVal( nameFld(), name );
        setFieldVal( address1Fld(), streetAddress );
        setFieldVal( cityFld(), city );
        setState(state);
        setFieldVal( zipCodeFld(), zipCode );
        setFieldVal( phoneNumberFld(), phoneNumber );
        // assertion for a known bug
        assertStateIsntReset();
        clickBtn( saveBtn_addressForm() );
        // wait till changes in address book will be displayed - customer name on any address should be visible
        elementIsVisible( $(By.xpath("//li[@class='name']")).shouldBe(visible) );

    }

    private void assertStateIsntReset() {
        Assert.assertTrue( !Objects.equals(stateDropdown().getText(), "- Select -"),
                "'State' is reset to default value");
    }

        // Will deprecate once we'll switch from custom to normal dropdowns.
        @Step
        private void setState(String state) {
            clickBtn( stateDropdown() );
            clickBtn( stateDropdownValue(state) );
        }

    @Step
    public void clickEditBtn_shipAddress() {

        if ( !(addNewAddressBtn().is(visible)) ) {
            clickBtn( editBtn_shipAddress() );
        }
        elementIsVisible( doneBtn_shipAddress() );

    }

    private boolean isAddressInEditMode() {
        return addNewAddressBtn().isDisplayed();
    }

    @Step
    public void restorePageDefaultState() {

        while ( !(editBtn_items().is(visible)) ) {

            if ( deleteConfirmBtn().is(visible) ) {
                clickBtn( cancelDeletionBtn() );
            }

            if ( saveBtn_addressForm().is(visible) ) {
                clickBtn( cancelBtn_addressForm() );
            }

            if ( doneBtn_shipAddress().is(visible) ) {
                clickBtn( doneBtn_shipAddress() );
            }

        }

    }

    @Step
    public void clearAddressBook() {

        List<SelenideElement> addressBook = $$(By.xpath("//div[@class='fc-tile-selector__items']/div/li/div[2]/div/button[1]"));
        int addressesAmount = addressBook.size();
        System.out.println("Addresses in AB: " + addressesAmount);

        for (int i = 0; i < addressesAmount; i++) {
            clickBtn( deleteBtn_inAddressBook("1") );
            clickBtn( deleteConfirmBtn() );
            sleep(750);
        }

    }

    @Step("Set an address from address book as a shipping address.")
    public void setShipAddress() {

        clickBtn( editBtn_shipAddress() );
        chooseShipAddress(1);
        clickBtn( doneBtn_shipAddress() );

    }



    //-------------------- S H I P P I N G    M E T H O D --------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement editBtn_shipMethod() {
        return $(By.xpath("//div[contains(@class, 'shipping-method')]/header/div[2]/button"));
    }

    // should be clicked with 'jsClick(element)'
    public SelenideElement shipMethodRdbtn() {
        return $(By.xpath("//table[@class='fc-table']/tbody/tr[1]//input"));
    }

    public SelenideElement doneBtn_shipMethod() {
        return $(By.xpath("//div[contains(@class, 'shipping-method')]/div/footer/button"));
    }

    public SelenideElement editSelectedShipMethodBtn() {
        return $(By.xpath("//div[@class='fc-right']/button"));
    }


    //------------------------------- HELPERS --------------------------------//

    @Step("Check if 'Shipping Method' is defined")
    public boolean isShipMethodDefined() {
        return $(By.xpath("//th[text()='Method']")).is(visible);
    }

    @Step("Set shipping method")
    public void setShippingMethod() {
        clickBtn( editBtn_shipMethod() );
        jsClick( shipMethodRdbtn() );
        clickBtn( doneBtn_shipMethod() );
        Assert.assertTrue( isShipMethodDefined(), "Shipping Method isn't defined" );
    }



    //--------------------- P A Y M E N T    M E T H O D ---------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement editBtn_payment() {
        return $(By.xpath("//div[contains(@class, 'order-payment')]/header/div[2]/button"));
    }

    public SelenideElement doneBtn_payment() {
        return $(By.xpath("//div[contains(@class, 'order-payment')]/div/footer/button"));
    }

    public SelenideElement newPaymentBtn() {
        return $(By.xpath("//div[contains(@class, 'order-payment')]/header/div[2]/button"));
    }

    private SelenideElement paymentTypeDropdown() {
        return $(By.xpath("//label[contains(@class, 'payment-type')]/following-sibling::*/div[2]/div"));
    }

        private SelenideElement paymentTypeVal(String paymentType) {
            return $(By.xpath("//li[text()='"+ paymentType + "']"));
        }

    public double appliedAmount() {
        String amount = $(By.xpath("//tr[contains(@class, 'payment-row')]/td[2]/span")).getText();
        return Double.valueOf(amount.substring(1, amount.length()));
    }


    // ----------- >> NEW CREDIT CARD FORM
    private SelenideElement newCreditCardBtn() {
        return $(By.xpath("//div[contains(@class, 'new-order-payment')]/following-sibling::*/div/div/button"));
    }

        private SelenideElement holderNameFld() {
            return $(By.xpath("//input[@name='holderName']"));
        }

        private SelenideElement cardNumberFld() {
            return $(By.xpath("//input[@name='cardNumber']"));
        }

        private SelenideElement cvvFld() {
            return $(By.xpath("//input[@name='cvv']"));
        }

        private SelenideElement monthDropdown() {
            return $(By.xpath("//label[text()='Expiration Date']/following-sibling::*/div/div/div[2]/div"));
        }

            private SelenideElement monthVal(String monthNumber) {
                return $(By.xpath("//div[@class='fc-grid']/div[1]/div/div[3]/ul/li[" + monthNumber + "]"));
            }

        private SelenideElement yearDropdown() {
            return $(By.xpath("//label[text()='Expiration Date']/following-sibling::*/div[2]/div/div[2]/div"));
        }

            private SelenideElement yearVal(String year) {
                return $(By.xpath("//div[@class='fc-grid']/div[2]/div/div[3]/ul/li[text()='" + year + "']"));
            }

        private SelenideElement chooseBtn() {
            return $(By.xpath("//span[text()='Choose']/.."));
        }

        public SelenideElement addPaymentBtn() {
            return $(By.xpath("//span[text()='Add Payment Method']/.."));
        }
    // -------- -------- -------- --------

    public SelenideElement gcNumberFld() {
        return $(By.xpath("//input[@name='giftCardCode']"));
    }

    public SelenideElement amountToUseFld() {
        return $(By.xpath("//input[@name='currencyInput']"));
    }

    public SelenideElement editPaymentBtn() {
        return $(By.xpath("//td[contains(@class, 'orders_payments')]/button[1]"));
    }

    public SelenideElement deletePaymentBtn() {
        return $(By.xpath("//td[contains(@class, 'orders_payments')]/button[2]"));
    }

    public Double gcAvailableBalance() {
        String  availableBalance = $(By.xpath("//div[text()='Available Balance']/following-sibling::*/span")).getText();
        return Double.valueOf(availableBalance.substring(1, availableBalance.length()));
    }

    public Double gcNewAvailableBalance() {
        String  newAvailableBalance = $(By.xpath("//div[text()='New Available Balance']/following-sibling::*/span")).getText();
        return Double.valueOf(newAvailableBalance.substring(1, newAvailableBalance.length()));
    }


    //------------------------------- HELPERS --------------------------------//

    @Step("Add new credit card")
    public void addNewCreditCard(String holderName, String cardNumber, String cvv, String month, String year) {

        clickBtn( newCreditCardBtn() );
        setFieldVal( holderNameFld(), holderName );
        setFieldVal( cardNumberFld(), cardNumber );
        setFieldVal( cvvFld(), cvv );
        setExpirationDate(month, year);
        clickBtn( chooseBtn() );
        clickBtn( addPaymentBtn() );

    }

        @Step("Select payment type: {0}")
        public void selectPaymentType(String paymentType) {
            clickBtn( paymentTypeDropdown() );
            clickBtn( paymentTypeVal(paymentType) );
        }

        @Step("Set expiration date: {0}/{1}")
        private void setExpirationDate(String month, String year) {
            clickBtn( monthDropdown() );
            clickBtn( monthVal(month) );
            clickBtn( yearDropdown() );
            clickBtn( yearVal(year) );
        }

    @Step("Assert that credit card is added")
    public void assertCardAdded() {
        editBtn_payment().shouldBe(visible);
        Assert.assertTrue($(By.xpath("//strong[contains(text(), 'xxxx xxxx xxxx')]")).is(visible),
                "Failed to add credit card to order as a payment method.");
    }

    @Step("Add credit card as a payment method.")
    public void addPaymentMethod_CC(String holderName, String cardNumber, String cvv, String month, String year) {

        clickBtn( editBtn_payment() );
        clickBtn( newPaymentBtn() );
        selectPaymentType("Credit Card");
        addNewCreditCard(holderName, cardNumber, cvv, month, year);

        assertCardAdded();

    }

    @Step("Add gift card as a payment method.")
    public void addPaymentMethod_GC(String gcNumber, String amountToUse) {

        clickBtn( editBtn_payment() );
        clickBtn( newPaymentBtn() );
        selectPaymentType("Gift Card");
        setFieldVal( gcNumberFld(), gcNumber );
        setFieldVal( amountToUseFld(), amountToUse );
        clickBtn( addPaymentBtn() );

    }

    @Step("Add store credit as a payment method.")
    public void addPaymentMethod_SC(String amountToUse) {

        clickBtn( editBtn_payment() );
        clickBtn( newPaymentBtn() );
        selectPaymentType("Store Credit");
        setFieldVal( amountToUseFld(), amountToUse );
        clickBtn( addPaymentBtn() );

    }


}