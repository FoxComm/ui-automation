package pages.admin;

import base.AdminBasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.By.xpath;

public class CartPage extends AdminBasePage {

    //------------------------ G E N E R A L    C O N T R O L S ------------------------//
    //----------------------------------------------------------------------------------//
    //----------------------------------- ELEMENTS -------------------------------------//

    public SelenideElement editCartBtn() {
        return $(xpath("//span[text()='Edit Cart']/.."));
    }

    public SelenideElement cartSummary() {
        return $(xpath("//div[text()='Cart Summary']"));
    }

    public double subtotalVal() {
        String subtotal = $(xpath("//span[@id='fct-totals__subtotal']")).getText();
        return Double.valueOf(subtotal.substring(1, subtotal.length()));
    }

    public double discountsVal() {
        String discounts = $(xpath("//span[@id='fct-totals__discounts-total']")).getText();
        return Double.valueOf(discounts.substring(1, discounts.length()));
    }

    public double newSubtotal() {
        String newSubtotal = $(xpath("//span[@id='fct-totals__new-subtotal']")).getText();
        return Double.valueOf(newSubtotal.substring(1, newSubtotal.length()));
    }

    public double shipping() {
        String shipping = $(xpath("//span[@id='fct-totals__shipping-total']")).getText();
        return Double.valueOf(shipping.substring(1, shipping.length()));
    }

    public double tax() {
        String tax = $(xpath("//span[@id='fct-totals__tax-total']")).getText();
        return Double.valueOf(tax.substring(1, tax.length()));
    }

    public SelenideElement grandTotal() {
        return $(xpath("//span[@id='fct-totals__grand-total']"));
    }

    public double grandTotalVal() {
        return Double.valueOf(grandTotal().text().substring(1, grandTotal().text().length()));
    }

    public SelenideElement itemsWarn() {
        return $(xpath("//div[contains(@class, 'messages')]//*[text()='Cart is empty.']"));
    }

    public SelenideElement shipAddressWarn() {
        return $(xpath("//div[contains(@class, 'messages')]//*[text()='No shipping address applied.']"));
    }

    public SelenideElement shipMethodWarn() {
        return $(xpath("//div[contains(@class, 'messages')]/*[text()='No shipping method applied.']"));
    }

    public SelenideElement fundsWarn() {
        return $(xpath("//div[contains(@class, 'messages')]//*[text()='Insufficient funds.']"));
    }

    public SelenideElement orderOverviewPanel() {
        return $(xpath("//div[@class='fc-panel-list']"));
    }

    // assignees, watchers, customer info will be listed here

    private SelenideElement editBtn(String blockName) {
        blockName = blockName.toLowerCase().replaceAll(" ", "-");
        return $(xpath("//*[@id='fct-edit-btn__" + blockName + "']"));
    }

    private SelenideElement doneBtn(String blockName) {
        blockName = blockName.toLowerCase().replaceAll(" ", "-");
        return $(xpath("//*[@id='fct-done-btn__" + blockName + "']"));
    }

    public SelenideElement placeOrderBtn() {
        return $(xpath("//button[@id='fct-place-order-btn']"));
    }

    public SelenideElement orderState() {
        return $(xpath("//*[@id='fct-order-state__value']"));
    }

    //---------------------------------------- HELPERS ----------------------------------------//

    @Step
    public void clickEditCartBtn() {
        click(editCartBtn());
    }

    @Step("Click \"Edit\" at <{0}> block")
    public void clickEditBtn(String blockName) {
        click( editBtn(blockName) );
        shouldBeVisible( doneBtn(blockName), "\"Done\" btn isn't visible - failed to enter editing mode." );
    }

    @Step("Click \"Done\" at <{0}> block")
    public void clickDoneBtn(String blockName) {
        click( doneBtn(blockName) );
        shouldNotBeVisible(doneBtn(blockName), "\"Done\" btn is visible (it shouldn't)");
    }

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
            shouldNotBeVisible(itemsWarn(), "'Cart is empty' warning is displayed.");
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

    //----------------------------- I T E M S    B L O C K -----------------------------//
    //----------------------------------------------------------------------------------//
    //----------------------------------- ELEMENTS -------------------------------------//

    public SelenideElement noItemsMsg() {
        return $(xpath("//*[@id='fct-cart-items-block']//*[text()='No items yet.']"));
    }

    // Works when cart is not in edit mode
    public SelenideElement itemQty(String itemIndex) {
        return $(xpath("//tbody[@id='fct-cart-line-items']/tr[" + itemIndex + "]/td[5]"));
    }

    private SelenideElement lineItemSearchFld() {
        return $(xpath("//input[@name='typeahead']"));
    }

    private SelenideElement decreaseItemQtyBtn(String itemIndex) {
        return $(xpath("//tbody[@id='fct-cart-line-items']/tr[" + itemIndex + "]//i[contains(@class, 'chevron-down')]"));
    }

    private SelenideElement increaseItemQtyBtn(String itemIndex) {
        return $(xpath("//tbody[@id='fct-cart-line-items']/tr[" + itemIndex + "]//i[contains(@class, 'chevron-up')]/.."));
    }

    private SelenideElement qtyInput_skuIndex(String itemIndex) {
        return $(xpath("//tbody[@id='fct-cart-line-items']/tr[" + itemIndex + "]//input"));
    }

    private SelenideElement qtyInput_sku(String skuCode) {
        skuCode = skuCode.replaceAll(" ", "-").toLowerCase();
        return $(xpath("//input[@id='fct-counter-input__" + skuCode + "']"));
    }

    public SelenideElement itemTotalPrice(String index) {
        return $(xpath("//tbody[@id='fct-cart-line-items']/tr[" + index + "]//*[contains(@class, 'item-total-price')]"));
    }

    private SelenideElement deleteBtn_item(String itemIndex) {
        return $(xpath("//tbody[@id='fct-cart-line-items']/tr[" + itemIndex + "]//i[contains(@class, 'trash')]"));
    }

    private SelenideElement confirmDeletionBtn() {
        return $(xpath("//button[@id='fct-modal-confirm-btn']"));
    }

    private SelenideElement cancelDeletionBtn() {
        return $(xpath("//button[@id='fct-modal-cancel-btn']"));
    }

    public SelenideElement lineItem_byName(String itemName) {
        return $(xpath("//tbody[@id='fct-cart-line-items']//a[text()='" + itemName + "']"));
    }

    /**
     * Works with any parameter of a line item
     */
    public SelenideElement lineItem_editing(String param) {
        return $(xpath("//tr[@class='line-item']//*[text()='" + param + "']"));
    }

    public ElementsCollection lineItems() {
        return $$(xpath("//tbody[@id='fct-cart-line-items']/tr"));
    }

    private int itemsInCartAmount() {
        return lineItems().size();
    }

    private String indexOfLastItemInCart() {
        return String.valueOf(itemsInCartAmount());
    }

    public SelenideElement lineItemSearchView_byName(String itemName) {
        return $(xpath("//li[contains(@class, 'item')]//*[text()='" + itemName + "']"));
    }


    //------------------------------------ HELPERS -------------------------------------//

    @Step("Add item to cart, searchQuery: {0}")
    public void addItemToCart(String searchQuery) {
        if ( !(itemsInEditMode()) ) {
            clickEditBtn("Line Items");
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
        clickDoneBtn("Line Items");
    }

    @Step("Set \"Search\" field val to <{0}>")
    public void searchForItem(String searchQuery) {
        setFieldVal(lineItemSearchFld(), searchQuery);
    }

    @Step("Click <{0}> in search view")
    public void addFoundItem(String searchQuery) {
        click(lineItemSearchView_byName(searchQuery));
    }

    @Step("Click \"Delete\" btn at <{0}th> line item")
    public void clickDeleteBtn_item(String index) {
        click(deleteBtn_item(index));
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
    }

    @Step("Remove <{0}th> line item from the cart")
    public void removeItem(String itemIndex) {
        click(deleteBtn_item(itemIndex));
        confirmDeletion();
    }

    @Step("Remove all line items from cart")
    public void removeAllItems() {
        int timesToIterate = itemsInCartAmount();
        String itemIndex;
        for (int i = 1; i <= timesToIterate; i++) {
            itemIndex = String.valueOf(i);
            removeItem(itemIndex);
        }
    }

    public String getItemName(String index) {
        return lineItem_byName(index).getText();
    }

    private boolean itemsInEditMode() {
        return doneBtn("Line Items").isDisplayed();
    }

    @Step("Increase {0}th item in cart qty by {1}")
    public void increaseItemQty(String itemIndex, int increaseBy) {
        int initialQty = Integer.valueOf(qtyInput_skuIndex(itemIndex).getValue());
        for (int i = 0; i < increaseBy; i++) {
            String expectedValue = String.valueOf(initialQty + 1);
            clickIncreaseQty(itemIndex);
            sleep(750);
            shouldHaveValue(qtyInput_skuIndex(itemIndex), expectedValue,
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
        int initialQty = Integer.valueOf(qtyInput_skuIndex(itemIndex).getValue());
        for (int i = 0; i < decreaseBy; i++) {
            String expectedValue = String.valueOf(initialQty - 1);
            clickDecreaseQty(itemIndex);
            sleep(750);
            if (Integer.valueOf(expectedValue) == 0) {
                confirmDeletionBtn().shouldBe(visible
                        .because("'Confirm deletion' modal window doesn't appear after item quantity is decreased to '0'."));
            } else {
                shouldHaveValue(qtyInput_skuIndex(itemIndex), expectedValue,
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
        int decreaseBy =  Integer.valueOf(qtyInput_skuIndex("1").getValue());
        System.out.println("decreaseBy" + decreaseBy);

        decreaseItemQty(itemIndex, decreaseBy);
        shouldBeVisible(confirmDeletionBtn(), "'Confirm' modal window isn't displayed");
    }

    @Step("Set QTY of <{0}th> line item using input fld; <newQTY:{1}>")
    public void setItemQty(String sku, String qty) {
        clearField(qtyInput_sku(sku));
        setFieldVal(qtyInput_sku(sku), qty);
        shouldHaveValue(qtyInput_sku(sku), qty, "Failed to edit \"Qty\" input field value");
    }


    //------------------------ S H I P P I N G    A D D R E S S ------------------------//
    //----------------------------------------------------------------------------------//
    //----------------------------------- ELEMENTS -------------------------------------//

    public SelenideElement customerName_chosenShipAddress() {
        return $(xpath("//li[contains(@class, 'is-active')]//li[@class='name']"));
    }

    public ElementsCollection addressBook() {
        return $$(xpath("//*[@id='fct-cart-shipping-address-block']//div[@class='fc-tile-selector__item']"));
    }

    public SelenideElement successIcon_shipAddress() {
        return $(xpath("//div[@id='fct-cart-shipping-address-block']//i[contains(@class, 'success')]"));
    }

    public SelenideElement warningIcon_shipAddress() {
        return $(xpath("//*[@id='fct-cart-shipping-address-block']//i[contains(@class, 'warning')]"));
    }

    private SelenideElement deleteBtn_chosenAddress() {
        return $(xpath("//*[contains(@class, 'is-active')]//i[contains(@class, 'icon-trash')]/.."));
    }

    private SelenideElement editBtn_chosenAddress() {
        return $(xpath("//*[contains(@class, 'is-active')]//i[contains(@class, 'icon-edit')]/.."));
    }

    private SelenideElement deleteBtn_inAddressBook(String addressIndex) {
        return $(xpath("//*[@class='fc-tile-selector__items']/div[" + addressIndex + "]//button[contains(@class, 'trash')]"));
    }

    private SelenideElement editBtn_inAddressBook(String addressIndex) {
        return $(xpath("//*[@class='fc-tile-selector__items']/div[" + addressIndex + "]//button[contains(@class, 'edit')]"));
    }

    private SelenideElement addNewAddressBtn() {
        return $(xpath("//button[@id='fct-add-btn__new-shipping-address']"));
    }

    public SelenideElement chosenAddress() {
        return $(xpath("//*[@class='fc-card-container fc-address is-active']"));
    }

    public SelenideElement addressBookHeader() {
        return $(xpath("//*[text()='Address Book']"));
    }

    /**
     * TODO: [Ashes] Generate ID for multiple similar elements in the block
     * Refactoring should also cover chooseBtn and control buttons (edit, delete).
     * After refactoring elements should be searched by address index.
     */
    private ElementsCollection chooseAddressBtns() {
        return $$(xpath("//span[text()='Choose']/.."));
    }

    private ElementsCollection namesInAddressBook() {
        return $$(xpath("//li[@class='name']"));
    }

    private SelenideElement nameOnAddressCard(int addressIndex) {
        return namesInAddressBook().get(addressIndex - 1);
    }

    private SelenideElement defaultShipAddressChkbox(String addressIndex) {
        return $(xpath("//*[@class='fc-tile-selector__items']/*[" + addressIndex + "]//*[contains(@id, 'is-default')]/.."));
    }
    public SelenideElement defaultShipAddressChkbox_input(String addressIndex) {
        return $(xpath("//*[@class='fc-tile-selector__items']/*[" + addressIndex + "]//*[contains(@id, 'is-default')]"));
    }

    // ----------- >> NEW ADDRESS FORM

    public SelenideElement nameFld() {
        return $(xpath("//input[@name='name']"));
    }

    private SelenideElement address1Fld() {
        return $(xpath("//input[@name='address1']"));
    }

    private SelenideElement address2Fld() {
        return $(xpath("//input[@name='address2']"));
    }

    private SelenideElement cityFld() {
        return $(xpath("//input[@name='city']"));
    }

    private SelenideElement stateDd() {
        return $(xpath("//div[@id='region-dd']"));
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

    public SelenideElement chosenShippingAddressBlock() {
        return $(xpath("//li[contains(@class, 'is-active')]"));
    }

    public SelenideElement appliedShipAddress_name() {
        return $(xpath("//div[@id='fct-cart-shipping-address-block']//*[@class='name']"));
    }

    //------------------------------------ HELPERS -------------------------------------//

    @Step("Click \"Edit\" btn at chosen shipping address")
    public void clickEditBtn_chosenAddress() {
        click(editBtn_chosenAddress(), "Failed to wait for \"Edit\" btn on chosen shipp address to become visible");
    }

    @Step("Click \"Delete\" btn at chosen shipping address")
    public void removeChosenAddress() {
        click(deleteBtn_chosenAddress());
        click(confirmDeletionBtn());
    }

    @Step("Choose existing shipping address")
    public void chooseShipAddress(int addressIndex) {
        String expectedResult = nameOnAddressCard(addressIndex).text();
        click(chooseAddressBtns(), 1);

        shouldHaveText(customerName_chosenShipAddress(), expectedResult,
                "Failed to choose shipping address from address book.");
    }

    @Step("Set <{0}th> shipping address as default")
    public void setAddressAsDefault(String addressIndex) {
        click( defaultShipAddressChkbox(addressIndex) );
        sleep(750);
    }

    @Step("Add new shipping address")
    public void addNewAddress(String name, String streetAddress1, String streetAddress2, String city, String state, String zipCode, String phoneNumber) {

        clickNewAddressBtn();
        setName(name);
        setAddress1(streetAddress1);
        setAddress2(streetAddress2);
        setCity(city);
        setState(state);
        setZip(zipCode);
        setPhoneNumber(phoneNumber);
        // assertion for a known bug
        assertStateIsntReset();
        clickSaveBtn_modal();
        // wait till changes in address book will be displayed - customer name on any address should be visible
        shouldBeVisible($(xpath("//li[@class='name']")),
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

    private boolean isAddressInEditMode() {
        return addNewAddressBtn().isDisplayed();
    }

//    @Step
//    public void restorePageDefaultState() {
//        while ( !(editBtn("Items").is(visible)) ) {
//
//            if ( confirmDeletionBtn().is(visible) ) {
//                click( cancelDeletionBtn() );
//            }
//            if ( saveBtn_addressForm().is(visible) ) {
//                click( cancelBtn_addressForm() );
//            }
//            if ( doneBtn("Shipping Address").is(visible) ) {
//                click( doneBtn("Shipping Address") );
//            }
//        }
//    }

    @Step("Remove all addresses from address book")
    public void clearAddressBook() {
        ElementsCollection addressBook = $$(xpath("//div[@class='fc-tile-selector__items']/div/li/div[2]/div/button[1]"));
        int addressesAmount = addressBook.size();
        System.out.println("Addresses in AB: " + addressesAmount);

        for (int i = 0; i < addressesAmount; i++) {
            click( deleteBtn_inAddressBook("1") );
            click( confirmDeletionBtn() );
            shouldNotBeVisible(confirmDeletionBtn(), "Confirmation modal window isn't auto-hidden");
        }
    }

    @Step("Set an address from address book as a shipping address")
    public void setShipAddress() {
        clickEditBtn("Shipping Address");
        chooseShipAddress(1);
        clickDoneBtn("Shipping Address");
    }



    //------------------------- S H I P P I N G    M E T H O D -------------------------//
    //----------------------------------------------------------------------------------//
    //----------------------------------- ELEMENTS -------------------------------------//

    // should be clicked with 'jsClick(element)'
    private SelenideElement shipMethodRdbtn(String index) {
        return $(xpath("//*[@id='fct-cart-shipping-method-block']//tr[" + index + "]//input"));
    }

    public SelenideElement editSelectedShipMethodBtn() {
        return $(xpath("//div[@class='fc-right']/button"));
    }

    private SelenideElement successIcon_shipMethod() {
        return $(xpath("//*[@id='fct-cart-shipping-method-block']//i[contains(@class, 'success')]"));
    }

    private SelenideElement warningIcon_shipMethod() {
        return $(xpath("//*[@id='fct-cart-shipping-method-block']//i[contains(@class, 'warning')]"));
    }


    //------------------------------------ HELPERS -------------------------------------//

    @Step("Check if \"Shipping Method\" is defined")
    public void assertShipMethodDefined() {
        successIcon_shipMethod().shouldBe(visible
                .because("Success icon isn't displayed next to 'Shipping Method' block"));
        shipMethodWarn().shouldNotBe(visible
                .because("Shipping method warning is displayed"));
    }

    @Step("Set shipping method")
    public void setShipMethod() {
        clickEditBtn("Shipping Method");
        selectShipMethod("1");
        clickDoneBtn("Shipping Method");
        assertShipMethodDefined();
    }

    @Step("Select <{0}th> shipping method")
    public void selectShipMethod(String methodIndex) {
        jsClick( shipMethodRdbtn(methodIndex) );
    }



    //-------------------------------- C O U P O N S -----------------------------------//
    //----------------------------------- ELEMENTS -------------------------------------//

    private SelenideElement addCouponFld() {
        return $(xpath("//input[@name='couponCode']"));
    }

    private SelenideElement applyBtn() {
        return $(xpath("//button[@id='fct-apply-coupon-btn']"));
    }

    public SelenideElement removeCouponBtn() {
        return $(xpath("//*[@id='fct-cart-coupons-block']//button[contains(@class, 'delete')]"));
    }

    public SelenideElement appliedCoupon(String couponCode) {
        return $(xpath("//div[@id='fct-cart-coupons-block']//td[contains(@class, 'code') and text()='" + couponCode + "']"));
    }

    public SelenideElement noCouponMsg() {
        return $(xpath("//*[@id='fct-cart-coupons-block']//div[text()='No coupons applied.']"));
    }

    public SelenideElement noDiscountsMsg() {
        return $(xpath("//div[text()='No discounts applied.']"));
    }

    public SelenideElement couponErrorMsg() {
        return $(xpath("//*[@id='fct-cart-coupons-block']//div[text()='This coupon code does not exist.']"));
    }


    //------------------------------------ HELPERS -------------------------------------//

    @Step("Add <{0}> coupon code")
    public void addCouponCode(String codeVal) {
        setFieldVal(addCouponFld(), codeVal);
    }

    @Step("Click \"Apply\"")
    public void clickApplyBtn() {
        click(applyBtn());
    }

    @Step("Click \"Remove\" btn")
    public void clickRemoveCouponBtn() {
        click(removeCouponBtn());
    }



    //-------------------------- P A Y M E N T    M E T H O D --------------------------//
    //----------------------------------- ELEMENTS -------------------------------------//

    //TODO: find it in Ashes
    public SelenideElement editBtn_payment() {
        return $(xpath("//button[@id='payment-methods-edit-btn']"));
    }

    public SelenideElement newPaymentBtn() {
        return $(xpath("//button[@id='fct-add-btn__payment-method']"));
    }

    private SelenideElement paymentTypeDd() {
        return $(xpath("//*[@id='fct-payment-type-dd']"));
    }

    public SelenideElement appliedAmount() {
        return $(xpath("//*[@id='fct-cart-payment-method-block']//*[@class='fc-currency']"));
    }

    private SelenideElement removePaymentMethodBtn(String index) {
        return $(xpath("//*[@id='fct-cart-payment-method-block']//tr["+index+"]//i[contains(@class, 'trash')]/.."));
    }

    // ----------- >> NEW CREDIT CARD FORM
    private SelenideElement newCreditCardBtn() {
        return $(xpath("//button[@id='fct-payment-add-btn__new-credit-card']"));
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
        return $(xpath("//*[@id='expMonth']"));
    }

    private SelenideElement monthVal(String monthNumber) {
        return $(xpath("//*[@id='expMonth']//li[" + monthNumber + "]"));
    }

    private SelenideElement yearDd() {
        return $(xpath("//*[@id='expYear']"));
    }

    private SelenideElement chooseBtn(String index) {
        return $(xpath("//*[@id='fct-address-select-list']/div[" + index + "]//button"));
    }

    private SelenideElement addPaymentBtn() {
        return $(xpath("//span[text()='Add Payment Method']/.."));
    }
    // -------- -------- -------- --------

    public SelenideElement appliedGC(String gcCode) {
        String code = "";
        for (int i = 0; i < 13; i += 4) {
            code += gcCode.substring(i, i + 4);
            if (i < 12) {
                code += " ";
            }
        }
        return $(xpath("//span[@class='fc-gift-card-code' and text()='" + code + "']"));
    }

    public SelenideElement appliedSC() {
        return $(xpath("//strong[text()='Store Credit']"));
    }

    private SelenideElement gcNumberFld() {
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

    //TODO: Add IDs in Ashes
    public SelenideElement availableBalance() {
        return $(xpath("//div[text()='Available Balance']/following-sibling::*/span"));
    }

    public Double availableBalanceVal() {
        String  availableBalance = $(xpath("//div[text()='Available Balance']/following-sibling::*/span")).getText();
        return Double.valueOf(availableBalance.substring(1, availableBalance.length()));
    }

    public SelenideElement newAvailableBalance() {
        return $(xpath("//div[text()='New Available Balance']/following-sibling::*/span"));
    }

    public Double newAvailableBalanceVal() {
        String  newAvailableBalance = $(xpath("//div[text()='New Available Balance']/following-sibling::*/span")).getText();
        return Double.valueOf(newAvailableBalance.substring(1, newAvailableBalance.length()));
    }

    public SelenideElement noPayMethodError() {
        return $(xpath("//*[@id='fct-cart-payment-method-block']//*[text()='No payment method applied']"));
    }


    //------------------------------------ HELPERS -------------------------------------//

    @Step("Click \"Add New Payment Method\" button")
    public void clickNewPayMethodBtn() {
        click(newPaymentBtn());
        shouldBeVisible(paymentTypeDd(), "Payment Type' dd isn't visible");
    }

    @Step("Add new credit card")
    public void addNewCreditCard(String holderName, String cardNumber, String cvv, String month, String year) {

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
            setDdVal(monthDd(), month);
            setDdVal(yearDd(), year);
        }

        @Step("Click \"Choose\" at existing shipping address")
        private void clickChooseBtn() {
            click( chooseBtn("1") );
        }

        @Step("Click \"Add Payment Method\" btn")
        public void clickAddPayMethodBtn() {
            click( addPaymentBtn() );
            shouldBeVisible($(xpath("//strong")), "Payment method wasn't applied");
        }

    @Step("Assert that credit card is applied")
    public void assertCardAdded() {
//        editBtn("Payment Method").shouldBe(visible);
        shouldBeVisible($(xpath("//strong[contains(text(), 'xxxx xxxx xxxx')]")),
                "Failed to add credit card to order as a payment method.");
    }

    @Step("Assert that gift card is applied")
    public void asserGiftCardAdded(String gcCode) {
        shouldBeVisible(appliedGC(gcCode), "Failed to add gift card, code: <" + gcCode + ">");
    }

    @Step("Set \"Gift Card Number\" field value to <{0}>")
    public void setGCNumber(String gcCode){
        setFieldVal( gcNumberFld(), gcCode );
    }

    @Step("Set \"Amount To Use\" value to '<{0}>")
    public void setAmountToUse(String amount) {
        shouldNotHaveText(availableBalance(), "$0.00", "");
        clearField(amountToUseFld());
        setFieldVal(amountToUseFld(), amount);
    }

    @Step("Add credit card as a payment method")
    public void addPaymentMethod_CC(String holderName, String cardNumber, String cvv, String month, String year) {
        click( editBtn("Payment Method") );
        click( newPaymentBtn() );
        selectPaymentType("Credit Card");
        addNewCreditCard(holderName, cardNumber, cvv, month, year);

        //remove this assert - call it directly in tests after adding credit card
        assertCardAdded();
    }

    @Step("Add gift card as a payment method")
    public void addPaymentMethod_GC(String gcNumber, String amountToUse) {
        clickEditBtn("Payment Method");
        clickNewPayMethodBtn();
        selectPaymentType("Gift Card");
        setGCNumber(gcNumber);
        setAmountToUse(amountToUse);
        clickAddPayMethodBtn();
    }

    @Step("Add store credit as a payment method")
    public void addPaymentMethod_SC(String amountToUse) {
        clickNewPayMethodBtn();
        selectPaymentType("Store Credit");
        clearField(amountToUseFld());
        setFieldVal( amountToUseFld(), amountToUse );
        setAmountToUse(amountToUse);
        clickAddPayMethodBtn();
    }

    private ElementsCollection paymentMethodsList() {
        return $$(xpath("//*[@id='fct-cart-payment-method-block']//tbody/tr"));
    }

    @Step("Remove <{0}th> payment method on the table")
    public void removePaymentMethod(String index) {
        int listSize = paymentMethodsList().size();
        click(removePaymentMethodBtn(index));
        if (listSize > 1) {
            shouldHaveSize(paymentMethodsList(), listSize - 1,
                    "Failed to remove payment method. " +
                    "Actual: <" + paymentMethodsList().size() + ">, Expected: <" + (listSize - 1) + ">."
            );
        } else {
            shouldBeVisible(noPayMethodError(), "Failed to remove payment method");
        }
    }

}