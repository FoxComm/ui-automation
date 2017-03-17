package tests.storeadmin.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CartPage;
import pages.admin.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PaymentMethodTest extends DataProvider {

    private CartPage p;
//    private GiftCardsPage gcp;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

    @Test(priority = 1)
    public void addPaymentMethod_creditCard() throws IOException {
        provideTestData("cart with 1 item and chosen shipping address");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Payment Method");
        p.clickNewPayMethodBtn();
        p.selectPaymentType("Credit Card");
        p.addNewCreditCard("John Doe", "5555555555554444", "777", "02 - February", "2020");
        p.clickDoneBtn("Payment Method");

        p.assertCardAdded();
        p.fundsWarn().shouldNotBe(visible
                .because("'Insufficient funds' warning is displayed."));
    }

    @Test(priority = 2)
    public void addPaymentMethod_giftCard() throws IOException {
        provideTestData("cart with 1 item && customer with GC");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Payment Method");
        p.clickNewPayMethodBtn();
        p.selectPaymentType("Gift Card");
        p.setGCNumber(gcCode);
        p.setAmountToUse(String.valueOf(p.grandTotalVal()));
        double expectedVal = cutDecimal( p.availableBalanceVal() - p.grandTotalVal() );
        shouldHaveText(p.newAvailableBalance(), "$" + expectedVal,
                "New available balance calculations are incorrect.");
        p.clickAddPayMethodBtn();

        p.appliedGC(gcCode).shouldBe(visible);
    }

    @Test(priority = 3)
    public void addPaymentMethod_storeCredit() throws IOException {
        provideTestData("cart with 1 item && customer with SC");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn("Payment Method");
        p.clickNewPayMethodBtn();
        p.selectPaymentType("Store Credit");
        p.setAmountToUse(String.valueOf(p.grandTotalVal()));
        double expectedVal = cutDecimal(p.availableBalanceVal() - p.grandTotalVal());
        System.out.println("New available balance should be: <$" + expectedVal + ">");
        shouldHaveText(p.newAvailableBalance(), "$" + expectedVal,
                "New available balance calculations are incorrect.");

        p.clickAddPayMethodBtn();
        p.appliedSC().shouldBe(visible);
    }

    @Test(priority = 4)
    public void addStoreCredit_exceedsTotal() throws IOException {
        provideTestData("cart with 1 item && customer with SC");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        shouldBeVisible(p.cartSummary(), "Failed to open the cart page");
        double amountToUse = p.grandTotalVal() + 10.00;
        p.clickEditBtn("Payment Method");
        p.addPaymentMethod_SC(String.valueOf(amountToUse));

        p.appliedAmount().shouldHave(text(String.valueOf(amountToUse))
                .because("Amount of funds to be applied as a payment isn't auto-adjusted."));
    }

    @Test(priority = 5)
    public void addGiftCard_exceedsTotal() throws IOException {
        provideTestData("cart with 1 item && customer with GC");
        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);

        shouldBeVisible(p.cartSummary(), "Failed to open the cart page");
        double amountToUse = p.grandTotalVal() + 10.00;
        p.addPaymentMethod_GC(gcCode, String.valueOf(amountToUse));

        p.appliedAmount().shouldHave(text(String.valueOf(amountToUse))
                .because("Amount of funds to be applied as a payment isn't auto-adjusted."));
    }

//    TODO: Move test to API level
//    @Test(priority = 6)
//    public void addSC_onHoldState() throws IOException {
//
//        provideTestData("cart with 1 item && SC onHold");
//        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        p.clickEditBtn("Payment Method");
//        p.clickNewPayMethodBtn();
//        p.selectPaymentType("Store Credit");
//        p.setAmountToUse(String.valueOf(p.total()));
//
//        p.availableBalance().shouldHave(text("$0.00")
//                .because("A store credit with 'onHold' state can be used as a payment method."));
//
//    }

//    TODO: Move test to API level
//    @Test(priority = 7)
//    public void addSC_canceledState() throws IOException {
//
//        provideTestData("cart with 1 item && SC canceled");
//        p = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        click( p.editBtn_payment() );
//        click( p.newPaymentBtn() );
//        p.selectPaymentType("Store Credit");
//        setFieldVal( p.amountToUseFld(), String.valueOf(p.total()) );
//        p.setAmountToUse(String.valueOf(p.total()));
//
//        p.availableBalance().shouldHave(text("$0.00")
//                .because("A store credit with 'canceled' state can be used as a payment method."));
//
//    }

}
