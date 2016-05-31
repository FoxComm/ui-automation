package storefront.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class CheckoutPage extends CartPage {

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/button")
    public WebElement placeOrderBtn;

    //--------------------- SHIPPING ---------------------//

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[1]/div[2]/form/div/div[1]/input")
    public WebElement shipName;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[1]/div[2]/form/div/div[2]/input")
    public WebElement shipAddress1;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[1]/div[2]/form/div/div[4]/div[2]/input")
    public WebElement shipZip;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[1]/div[2]/form/div/div[6]/input")
    public WebElement shipPhone;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[1]/div[2]/form/button")
    public WebElement shippingContinueBtn;


    //--------------------- DELIVERY ---------------------//

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[2]/div[2]/form/div[2]/div/label")
    public WebElement upsNextDay;

    @FindAll({
            @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[2]/div[2]/form/div/div/label")
    })
    public List<WebElement> deliveryServicesList;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[2]/div[2]/form/button")
    public WebElement deliveryContinueBtn;


    //--------------------- BILLING ---------------------//

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/div[1]/input")
    public WebElement nameOnCard;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/div[2]/div[1]/input")
    public WebElement cardNumber;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/div[2]/div[2]/input")
    public WebElement cvv;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/div[3]/div[1]/div/div[1]/input")
    public WebElement monthDropdown;

        @FindAll({
                @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/div[3]/div[1]/div/div[2]/div")
        })
        public List<WebElement> monthDropdownOptions;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/div[3]/div[2]/div[1]/div[1]/input")
    public WebElement yearDropdown;

        @FindAll({
                @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/div[3]/div[2]/div[1]/div[2]/div")
        })
        public List<WebElement> yearDropdownOptions;

//    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[1]/div[3]/div[2]/form/div[4]/label")
//    public WebElement billingAddressCheckbox;

    //--------------------- BILLING ADDRESS ---------------------//
    // This might be used in a complicated scenario. Let's skip it for now.

    //--------------------- GIFT CARD ---------------------//

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[2]/div[2]/div[2]/div/input")
    public  WebElement codeField;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[2]/div[2]/div[2]/button")
    public WebElement redeemBtn;


    //--------------------- ORDER SUMMARY ---------------------//

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[2]/div[1]/ul/li[1]/div/div[2]/span")
    public WebElement subtotal;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[2]/div[1]/ul/li[2]/div/div[2]/span")
    public WebElement shipping;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[2]/div[1]/ul/li[3]/div/div[2]/span")
    public WebElement tax;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[2]/div[1]/div[2]/div[2]/span")
    public WebElement grandTotal;

    @FindAll({
            @FindBy(xpath = ".//*[@id='app']/div/div/div[2]/div[2]/div[1]/table/tbody/tr/td[2]")
    })
    public List<WebElement> itemsInCheckout;


    //--------------------- ORDER IS PLACED ---------------------//

    @FindBy(xpath = ".//*[@id='app']/div/div/div[1]/div/div[3]/button")
    public WebElement takeMeHomeBtn;


    //--------------------- HELPER METHODS ---------------------//

    // By default adds to cart first item in the catalog. Made to simplify the test code.
    public void addItemToCart() {
        navigateToItem(1);
        addToCart();
    }

    public void proceedToCheckout() {

        int amountOfItemsInCart = itemsInCart.size();
        checkoutBtn.click();

        waitForElementToBePresent(shippingContinueBtn);

        int amountOfItemsInCheckout = itemsInCheckout.size();
        Assert.assertTrue(amountOfItemsInCart == amountOfItemsInCheckout);

    }

    public String getSubtotal() {
        String subtotalVal = subtotal.getText();
        int length = subtotalVal.length();
        subtotalVal = subtotalVal.substring(1, length);
        return subtotalVal;
    }


    public void setShippingAddress(String name, String address, String zip, String phone) {
        setValue(shipName, name);
        setValue(shipAddress1, address);
        setValue(shipZip, zip);
        setValue(shipPhone, phone);
        shippingContinueBtn.click();
        waitForElementToBePresent(deliveryContinueBtn);
    }

//    public void selectDelivery(String deliveryService) {
//
//        if (deliveryService.equals("UPS Ground")) {
//            deliveryServicesList.get(0).click();
//            deliveryContinueBtn.click();
//
//        } else if (deliveryService.equals("UPS Next day")) {
//            deliveryServicesList.get(1).click();
//            deliveryContinueBtn.click();
//
//        } else if (deliveryService.equals("UPS 2-day")) {
//            deliveryServicesList.get(2).click();
//            deliveryContinueBtn.click();
//
//        } else {
//            deliveryServicesList.get(0).click();
//            deliveryContinueBtn.click();
//        }
//
//        deliveryContinueBtn.click();
//
//    }

    public void selectDelivery() {
        upsNextDay.click();
        deliveryContinueBtn.click();
        waitForElementToBePresent(placeOrderBtn);
    }

    public void setBilling(String name, String number, String cvvNumb) {

        setValue(nameOnCard,name);
        setValue(cardNumber, number);
        setValue(cvv, cvvNumb);

        monthDropdown.click();
        monthDropdownOptions.get(5).click();

        yearDropdown.click();
        yearDropdownOptions.get(4).click();

    }

    public void redeemGiftCard(String gcCode) {
        setValue(codeField, gcCode);
        redeemBtn.click();
    }

}