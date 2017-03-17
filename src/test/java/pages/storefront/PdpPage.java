package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.refresh;
import static org.openqa.selenium.By.xpath;

public class PdpPage extends ProfilePage {

    //-------------------------------------------- ELEMENTS -------------------------------------------

    private SelenideElement pdpTitle(String title) {
        return $(xpath("//h1/a[text()='" + title + "']"));
    }

    private SelenideElement qty_PDP() {
        return $(xpath("//div[contains(@class, 'cart-actions')]//select"));
    }

    private SelenideElement qtyOption_PDP(String qty) {
        return $(xpath("//div[contains(@class, 'cart-actions')]//select/option[@value='" + qty + "']"));
    }

    private SelenideElement addToCartBtn_PDP() {
        return $(xpath("//button[contains(@class, 'add-to-cart-btn')]"));
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

    @Step("Open PDP: <{0}>")
    public void openPDP(String productName) {
        try {
            click(pdpTitle(productName));
        } catch (RuntimeException ignored) {
            refresh();
            click(pdpTitle(productName));
        }
    }

    @Step("Set product quantity to <{0}>")
    public void setQty_PDP(String qty) {
        click(qty_PDP());
        click(qtyOption_PDP(qty));
    }

    @Step("Click \"Add To Cart\" btn")
    public void clickAddToCartBtn() {
        click(addToCartBtn_PDP());
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
