package storefront.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

public class CartPage extends LoginPage {

    public CartPage(WebDriver driver) {
        super(driver);
    }

//    @FindBy(xpath = ".//*[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/div/a")
//    public WebElement userMenuSign;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/button")
    public WebElement cartBtn;

    @FindBy(xpath = ".//*[@id='app']/div/div/div[1]/div/div[1]/div[4]/div/button/sup")
    public WebElement cartIndicator;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[4]/div/div[2]/div[3]/button")
    public WebElement checkoutBtn;

    @FindBy(xpath = ".//div[@id='app']/div/div/div[4]/div/div[2]/div[1]/div[2]")
    public WebElement keepShoppingBtn;

    // list of elements (divs, no interaction), refers to amount of items (not total units) in the cart
    @FindAll({
            @FindBy(xpath = ".//div[@id='app']/div/div/div[4]/div/div[2]/div[2]/div[1]/div")
    })
    public List<WebElement> itemsInCart;

    // list of elements (text), refers to item names that are in the cart
    @FindAll({
            @FindBy(xpath = ".//*[@id='app']/div/div/div[4]/div/div[2]/div[2]/div[1]/div/div[2]/div[1]/div[1]")
    })
    public List<WebElement> itemNames;

    // list of elements (text), refers to quantities of each item that is in the cart
    @FindAll({
            @FindBy(xpath = ".//*[@id='app']/div/div/div[4]/div/div[2]/div[2]/div[1]/div/div[2]/div[1]/div[2]/span[3]")
    })
    public List<WebElement> itemsQuantities;

    // list of elements, buttons - delete a specific item from the cart
    @FindAll({
            @FindBy(xpath = ".//*[@id='app']/div/div/div[4]/div/div[2]/div[2]/div[1]/div/div[3]/a/div")
    })
    public List<WebElement> deleteItemBtns;


    //--------------------- HELPERS ---------------------//

    public void addToCart() {
        addToCartBtn.click();
        waitForElementToBePresent(checkoutBtn);
    }

    public void openCart() {
        cartBtn.click();
        waitForElementToBePresent(checkoutBtn);
    }

    public void closeCart() {
        keepShoppingBtn.click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void cleanUpCart() {
        int totalItems = itemsInCart.size();
        for (int i = 0; i < totalItems; i++) {
            deleteItemBtns.get(totalItems - (i + 1)).click();
//            totalItems -= 1;
        }
    }

    public boolean cartIsntEmpty() {
        return itemsInCart.size() > 0;
    }

    public void cleanUpCartIfNeeded() {
        openCart();
        if (cartIsntEmpty()) {
            cleanUpCart();
        }
        closeCart();
    }

    public String getIndicatorValue() {
        return cartIndicator.getText();
    }

    public void assertCartHasItems(int amountOfItems) {
        Assert.assertTrue(itemsInCart.size() == amountOfItems);
    }

    public void removeItemFromCart(int itemPosition) {
        deleteItemBtns.get(itemPosition-1).click();
    }

    // itemPosition is for position of item on the screen in the cart
    // counting goes from top to bottom
    public void assertItemQuantity(int itemPosition, int expectedQty) {
        String strActualQty = itemsQuantities.get(itemPosition-1).getText();
        int actualQty = Integer.parseInt(strActualQty);
        Assert.assertTrue(actualQty == expectedQty);
    }

    public void assertCartIndicator(String strEexpectedValue) {
        int exptectedValue = Integer.parseInt(strEexpectedValue);
        int indicatorValue = Integer.parseInt(getIndicatorValue());
        Assert.assertTrue(indicatorValue == exptectedValue);

    }

    public void assertCartIndicator(int expectedValue) {
        int actualValue = Integer.parseInt(cartIndicator.getText());
        Assert.assertTrue(actualValue == expectedValue, "Incorrect cartIndicator value");
    }

}
