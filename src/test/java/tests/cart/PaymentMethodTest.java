package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.xpath;

public class PaymentMethodTest extends DataProvider {

    private CartPage p;
//    private GiftCardsPage gcp;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

    @Test(priority = 1)
    public void addPaymentMethod_creditCard() throws IOException {

        provideTestData("cart with 1 item and chosen shipping address");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_payment();
        p.clickNewPayMethodBtn();
        p.selectPaymentType("Credit Card");
        p.addNewCreditCard("John Doe", "5555555555554444", "777", "2", "2020");
        shouldBeVisible($(xpath("//strong")), "Payment method wasn't applied");
        p.clickDoneBtn_payment();

        p.assertCardAdded();
        p.fundsWarn().shouldNotBe(visible
                .because("'Insufficient funds' warning is displayed."));

    }

    @Test(priority = 2)
    public void addPaymentMethod_giftCard() throws IOException {

        provideTestData("cart with 1 item && customer with GC");
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_payment();
        p.clickNewPayMethodBtn();
        p.selectPaymentType("Gift Card");
        p.setGCNumber(gcCode);
        p.setAmountToUse(String.valueOf(p.grandTotalVal()));

        double expectedVal = cutDecimal( p.gcAvailableBalanceVal() - p.grandTotalVal() );
        p.gcNewAvailableBalance().shouldHave(text("$" + expectedVal)
                .because("New available balance calculations are incorrect."));

        p.clickAddPayMethodBtn();
        p.fundsWarn().shouldNotBe(visible
                .because("'Insufficient funds' warning is displayed."));

    }

    @Test(priority = 3)
    public void addPaymentMethod_storeCredit() throws IOException {

        provideTestData("cart with 1 item && customer with SC");
        // workaround for possible bug with data sync
//        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_payment();
        p.clickNewPayMethodBtn();
        p.selectPaymentType("Store Credit");
        p.setAmountToUse(String.valueOf(p.grandTotalVal()));

        double expectedVal = cutDecimal( p.gcAvailableBalanceVal() - p.grandTotalVal() );
        System.out.println("GC New available balance should be: <$" + expectedVal + ">");
        p.gcNewAvailableBalance().shouldHave(text("$" + expectedVal)
                .because("New available balance calculations are incorrect."));

        p.clickAddPayMethodBtn();
        p.fundsWarn().shouldNotBe(visible
                .because("'Insufficient funds' warning is displayed."));

    }

    @Test(priority = 4)
    public void addStoreCredit_exceedsTotal() throws IOException {

        provideTestData("cart with 1 item && customer with SC");
        // workaround for possible bug with data sync
//        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        shouldBeVisible(p.cartSummary(), "Failed to open the cart page");
        double amountToUse = p.grandTotalVal() + 10.00;
        p.addPaymentMethod_SC( String.valueOf(amountToUse) );

        p.appliedAmount().shouldHave(text(p.grandTotal().text())
                .because("Amount of funds to be applied as a payment isn't auto-adjusted."));

    }

    @Test(priority = 5)
    public void addGiftCard_exceedsTotal() throws IOException {

        provideTestData("cart with 1 item && customer with GC");
        // workaround for possible bug with data sync
//        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        shouldBeVisible(p.cartSummary(), "Failed to open the cart page");
        double amountToUse = p.grandTotalVal() + 10.00;
        p.addPaymentMethod_GC(gcCode, String.valueOf(amountToUse));

        p.appliedAmount().shouldHave(text(p.grandTotal().text())
                .because("Amount of funds to be applied as a payment isn't auto-adjusted."));

    }

    @Test(priority = 6)
    public void addSC_onHoldState() throws IOException {

        provideTestData("cart with 1 item && SC onHold");
        // workaround for possible bug with data sync
//        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.clickEditBtn_payment();
        p.clickNewPayMethodBtn();
        p.selectPaymentType("Store Credit");
        p.setAmountToUse(String.valueOf(p.grandTotal()));

        p.gcAvailableBalance().shouldHave(text("$0.00")
                .because("A store credit with 'onHold' state can be used as a payment method."));

    }

    @Test(priority = 7)
    public void addSC_canceledState() throws IOException {

        provideTestData("cart with 1 item && SC canceled");
        // workaround for possible bug with data sync
//        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        click( p.editBtn_payment() );
        click( p.newPaymentBtn() );
        p.selectPaymentType("Store Credit");
        setFieldVal( p.amountToUseFld(), String.valueOf(p.grandTotal()) );
        p.setAmountToUse(String.valueOf(p.grandTotal()));

        p.gcAvailableBalance().shouldHave(text("$0.00")
                .because("A store credit with 'canceled' state can be used as a payment method."));

    }

}
