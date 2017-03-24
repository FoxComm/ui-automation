package tests.storefront.checkout;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;

public class BillingTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Can create a credit card")
    public void createCreditCard_checkout() throws IOException {
        provideTestData("a storefront signed up customer with active product in cart and applied shipping address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.setShipMethod(p.shippingMethods().size());
        p.clickContinueBtn();
        p.fillOutCardForm("John Doe", "5555555555554444", "123", "08", "2020", false);
        p.clickSaveCardBtn();

        p.creditCard().shouldBe(visible);
        p.customerCardsWallet().shouldHaveSize(1);
    }

    @Test(priority = 2)
    @Description("Can pick an existing credit card")
    public void pickExistingCreditCard() throws IOException {
        provideTestData("a storefront customer ready for checkout, has 2 credit cards");
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod(p.shippingMethods().size());
        p.clickContinueBtn();
        p.setCreditCard("2");

        p.assertCardIsSelected("2");
    }

    @Test(priority = 3)
    @Description("A default credit card is marked as \"Default Card\"")
    public void defaultCardMarked() throws IOException {
        provideTestData("a storefront signed up customer with active product in cart and applied shipping address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.fillOutCardForm("John Doe", "5555555555554444", "123", "08", "2020", true);
        p.clickSaveCardBtn();

        p.creditCard().shouldBe(visible);
        p.customerCardsWallet().shouldHaveSize(1);
        p.defaultCardMark().shouldBe(visible);
    }

    @Test(priority = 4)
    @Description("A default credit card is pre-selected with radiobutton")
    public void defaultCardIsPreselected() throws IOException {
        provideTestData("a storefront signed up customer ready for checkout, has 2 credit cards, has default card");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod("1");
        p.clickContinueBtn();

        p.selectCardRbtn("1").shouldBe(selected);
    }

    @Test(priority = 5)
    @Description("Can delete an existing credit card")
    public void deleteCreditCard() throws IOException {
        provideTestData("a customer ready to checkout");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.clickDeleteCardLnk("1");

        p.saveCardBtn().shouldBe(visible);
    }

    @Test(priority = 6)
    @Description("Can apply a coupon code on checkout page")
    public void canApplyCoupon_checkout() throws IOException {
        provideTestData("a customer ready to checkout, single code coupon code");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.applyCouponCode(singleCouponCode);

        p.appliedCoupon().shouldBe(visible);
    }

    @Test(priority = 7)
    @Description("Can remove coupon on checkout page")
    public void removeCoupon() throws IOException {
        provideTestData("a storefront signed up customer, with no qualifier coupon code applied");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.expandCouponCodeBlock();
        p.removeCoupon();

        p.appliedCoupon().shouldNotBe(visible);
    }

    @Test(priority = 8)
    @Description("Can apply a gift card at checkout page")
    public void canRedeemGiftCard() throws IOException {
        provideTestData("a customer ready to checkout, a gift card issued");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.redeemGiftCard(gcCode);

        p.appliedGiftCard().shouldBe(visible);
    }

    @Test(priority = 9)
    @Description("Can remove a gift card at checkout page")
    public void canRemoveGiftCard() throws IOException {
        provideTestData("a customer ready for checkout, gift card is applied to cart as a payment method");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.setShipMethod("1");
        p.clickContinueBtn();
        p.removeGiftCard();

        p.appliedGiftCard().shouldNotBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}