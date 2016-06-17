package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;

public class OrderDetailsPage extends OrdersPage {

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
        return $(By.xpath("//div[@class='fc-content-box fc-editable-content-box fc-line-items is-editing']/div/footer/button"));
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



    //-------------------- S H I P P I N G    A D R E S S --------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement editBtn_shippingAddress() {
        return $(By.xpath("//div[contains(@class, 'shipping-address')]/header/div[2]/button"));
    }

    public SelenideElement doneBtn_shippingAddress() {
        return $(By.xpath("//div[@class='fc-content-box fc-editable-content-box fc-shipping-address is-editing is-indent-for-content']/div/footer/button"));
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

    public String getCustomerName_chosenShippingAddress() {
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
    public void chooseAddress(int addressIndex) {

        String expectedResult = getNameFromAddressBook(addressIndex);
        clickBtn( chooseAddressBtns(), 1 );
        elementIsVisible( chosenAddressHeader() );

        String actualResult = getCustomerName_chosenShippingAddress();
        Assert.assertEquals(expectedResult, actualResult, "Chosen address isn't displayed.");

    }

    @Step
    public void addNewAddress(String name, String streetAddress, String city, String state, String zipCode, String phoneNumber) {

        clickBtn( addNewAddressBtn() );
        setFieldValue( nameFld(), name );
        setFieldValue( address1Fld(), streetAddress );
        setFieldValue( cityFld(), city );
        setState(state);
        setFieldValue( zipCodeFld(), zipCode );
        setFieldValue( phoneNumberFld(), phoneNumber );
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
    public void clickEditBtn_shippingAddress() {

        if ( !(addNewAddressBtn().is(visible)) ) {
            clickBtn( editBtn_shippingAddress() );
        }
        elementIsVisible( doneBtn_shippingAddress() );

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

            if ( doneBtn_shippingAddress().is(visible) ) {
                clickBtn( doneBtn_shippingAddress() );
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


}