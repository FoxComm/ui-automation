package pages.storefront;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class CartPage extends SearchPage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

    public SelenideElement lineItemByName_cart(String productName) {
        return $(xpath("//div[contains(@class, 'product-name') and text()='" + productName + "']"));
    }

    public SelenideElement subTotal_cart() {
        return $(xpath("//div[contains(@class, 'cart-subtotal')]//span"));
    }

    private SelenideElement closeCartBtn() {
        return $(xpath("//span[contains(@class, 'back-icon')]"));
//        return $(xpath("//div[text()='KEEP SHOPPING']"));
    }

    public SelenideElement cartQty() {
        return $(xpath("//sup[contains(@class, 'cart-quantity')]"));
    }

    private SelenideElement cartBtn() {
        return $(xpath("//span[contains(@class, 'fc-cart')]"));
    }

    private SelenideElement removeLineItemBtn_byIndex(String index) {
        return $(xpath("//div[contains(@class, 'line-items')]/div[" + index + "]//span[contains(@class, 'fc-close')]"));
    }

    private SelenideElement qtyBtn(String index) {
        return $(xpath("//div[contains(@class, 'cart-box')]//div[contains(@class, 'line-items')]/div[" + index + "]//div[contains(@class, 'quantity')]//div[contains(@class, 'select-box')]"));
    }

    private SelenideElement qtyOption(String index, int value) {
        return $(xpath("//div[contains(@class, 'cart-box')]//div[contains(@class, 'line-items')]/div[" + index + "]//div[contains(@class, 'quantity')]//option[@value='" + value + "']"));
    }

    public SelenideElement checkoutBtn_cart() {
        return $(xpath("//button[contains(@class, 'checkout-button')]"));
    }

    public ElementsCollection lineItemsAmount() {
        return $$(xpath("//div[contains(@class, 'line-items')]/div[contains(@class, 'box')]"));
    }

    //TODO: id
    private SelenideElement couponCodeFld() {
        return $(xpath("//input[@placeholder='Coupon Code']"));
    }

    //TODO: id
    private SelenideElement applyCouponBtn() {
        return $(xpath("//button[text()='Apply']"));
    }

    //TODO: id & classes
    public SelenideElement appliedCoupon() {
        return $(xpath("//div[contains(@class, 'coupon-code')]"));
    }

    //TODO: class
    private SelenideElement removeCouponBtn() {
        return $(xpath("//span[contains(@class, 'delete-promo')]"));
    }

    //TODO: id
    public SelenideElement adjustmentTotal_cart() {
        return $(xpath("//div[contains(@class, 'coupon')]/span[1]"));
    }

    //---------------------------------------------- STEPS --------------------------------------------

    @Step("Close cart")
    public void closeCart() {
        jsClick(closeCartBtn());
    }

    @Step("Open cart")
    public void openCart() {
        click(cartBtn());
    }

    @Step("Remove <{0}th> line item from cart")
    public void removeLineItem(String index) {
        click(removeLineItemBtn_byIndex(index));
    }

    @Step("Click \"Checkout\" btn in cart")
    public void clickCheckoutBtn_cart() {
        click(checkoutBtn_cart());
    }

    public void cleanCart() {
        int lineItemsAmount = lineItemsAmount().size();
        for(int i = 0; i < lineItemsAmount; i++) {
            removeLineItem(String.valueOf(i+1));
            shouldNotBeVisible(removeLineItemBtn_byIndex(String.valueOf(lineItemsAmount - i)), "oops");
        }
    }

    @Step("Set \"Coupon Code\" fld val to <{0}>")
    protected void setCouponCode(String code) {
        setFieldVal(couponCodeFld(), code);
    }

    //TODO: id
    @Step("Click \"Apply\" btn")
    protected void clickApplyBtn_coupon() {
        click(applyCouponBtn());
    }

    @Step("Apply coupon to cart, code: <{0}>")
    public void applyCoupon(String code) {
        setCouponCode(code);
        clickApplyBtn_coupon();
    }

    @Step("Remove coupon from cart")
    public void removeCoupon() {
        click(removeCouponBtn());
    }

}
