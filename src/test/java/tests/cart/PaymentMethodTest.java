package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.GiftCardsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static com.codeborne.selenide.Selenide.sleep;

public class PaymentMethodTest extends DataProvider {

    private CartPage p;
    private GiftCardsPage gcp;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
            loginPage.userMenuBtn().shouldBe(visible);
        }

    }

//    @Test(priority = 1)
//    public void addPaymentMethod_creditCard() throws IOException {
//
//        provideTestData("cart with 1 item and chosen shipping address");
//        p = open(adminUrl + "/carts/" + cartId, CartPage.class);
//
//        click( p.editBtn_payment() );
//        click( p.newPaymentBtn() );
//        p.selectPaymentType("Credit Card");
//        p.addNewCreditCard("John Doe", "5555555555554444", "777", "2", "2020");
//
//        p.assertCardAdded();
//        p.assertNoFundsWarn();
//
//    }


    @Test(priority = 2)
    public void addPaymentMethod_giftCard() throws IOException {

        provideTestData("cart with 1 item && customer with GC");
        gcp = open(adminUrl + "/gift-cards/new", GiftCardsPage.class);
        gcp.issueGC("500.00");
        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        click( p.editBtn_payment() );
        click( p.newPaymentBtn() );
        p.selectPaymentType("Gift Card");
        setFieldVal( p.gcNumberFld(), "D26BB43F228AA2CD" );
        clearField(p.amountToUseFld());
        setFieldVal( p.amountToUseFld(), String.valueOf(p.grandTotal()) );
        System.out.println(p.gcAvailableBalance());

        double expectedVal = cutDecimal( p.gcAvailableBalanceVal() - p.grandTotalVal() );
        System.out.println(expectedVal);
        sleep(1000);
        p.gcNewAvailableBalance().shouldHave(text("$" + expectedVal)
                .because("New available balance calculations are incorrect."));

        click( p.addPaymentBtn() );
        p.assertNoFundsWarn();

    }

    @Test(priority = 3)
    public void addPaymentMethod_storeCredit() throws IOException {

        provideTestData("cart with 1 item && customer with SC");
        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        click( p.editBtn_payment() );
        click( p.newPaymentBtn() );
        p.selectPaymentType("Store Credit");
        clearField(p.amountToUseFld());
        setFieldVal( p.amountToUseFld(), String.valueOf(p.grandTotal()) );

        double expectedVal = cutDecimal( p.gcAvailableBalanceVal() - p.grandTotalVal() );
        System.out.println("GC New available balance should be: <$" + expectedVal + ">");
        sleep(1000);
        p.gcNewAvailableBalance().shouldHave(text("$" + expectedVal)
                .because("New available balance calculations are incorrect."));

        click( p.addPaymentBtn() );
        p.assertNoFundsWarn();

    }

    @Test(priority = 4)
    public void addStoreCredit_exceedsTotal() throws IOException {

        provideTestData("cart with 1 item && customer with SC");
        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        double amountToUse = p.grandTotalVal() + 10.00;
        p.addPaymentMethod_SC( String.valueOf(amountToUse) );

        p.appliedAmount().shouldHave(text(p.grandTotal().getText())
                .because("Amount of funds to be applied as a payment isn't auto-adjusted."));

    }

    @Test(priority = 5)
    public void addGiftCard_exceedsTotal() throws IOException {

        provideTestData("cart with 1 item && customer with GC");
        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        double amountToUse = p.grandTotalVal() + 10.00;
        p.addPaymentMethod_GC(gcCode, String.valueOf(amountToUse));

        p.appliedAmount().shouldHave(text(p.grandTotal().getText())
                .because("Amount of funds to be applied as a payment isn't auto-adjusted."));

    }

    @Test(priority = 6)
    public void addSC_onHoldState() throws IOException {

        provideTestData("cart with 1 item && SC onHold");
        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        click( p.editBtn_payment() );
        click( p.newPaymentBtn() );
        p.selectPaymentType("Store Credit");
        setFieldVal( p.amountToUseFld(), String.valueOf(p.grandTotal()) );

        p.gcAvailableBalance().shouldHave(text("$0.00")
                .because("A store credit with 'onHold' state can be used as a payment method."));

    }

    @Test(priority = 7)
    public void addSC_canceledState() throws IOException {

        provideTestData("cart with 1 item && SC canceled");
        refresh();
        p = open(adminUrl + "/carts/" + cartId, CartPage.class);

        click( p.editBtn_payment() );
        click( p.newPaymentBtn() );
        p.selectPaymentType("Store Credit");
        setFieldVal( p.amountToUseFld(), String.valueOf(p.grandTotal()) );

        p.gcAvailableBalance().shouldHave(text("$0.00")
                .because("A store credit with 'canceled' state can be used as a payment method."));

    }

}
