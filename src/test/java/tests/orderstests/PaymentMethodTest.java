package tests.orderstests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class PaymentMethodTest extends DataProvider {

    private OrderDetailsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open("http://admin.stage.foxcommerce.com/");
        if ( (Objects.equals(getUrl(), "http://admin.stage.foxcommerce.com/login")) ) {
            LoginPage loginPage = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void addPaymentMethod_creditCard() throws IOException {

        provideTestData("cart with 1 item and chosen shipping address");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.clickBtn( p.editBtn_payment() );
        p.clickBtn( p.newPaymentBtn() );
        p.selectPaymentType("Credit Card");
        p.addNewCreditCard("John Doe", "5555555555554444", "777", "2", "2020");

        p.assertCardAdded();
        p.assertNoFundsWarn();

    }


    @Test(priority = 2)
    public void addPaymentMethod_giftCard() throws IOException {

        provideTestData("cart with 1 item && customer with GC");
        open("http://admin.stage.foxcommerce.com/gift-cards/" + gcNumber);
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.clickBtn( p.editBtn_payment() );
        p.clickBtn( p.newPaymentBtn() );
        p.selectPaymentType("Gift Card");
        p.setFieldVal( p.gcNumberFld(), "D26BB43F228AA2CD" );
        p.setFieldVal( p.amountToUseFld(), String.valueOf(p.grandTotal()) );
        System.out.println(p.gcAvailableBalance());

        double expectedVal = cutDecimal( p.gcAvailableBalance() - p.grandTotal() );
        System.out.println(expectedVal);
        sleep(1000);
        Assert.assertTrue( p.gcNewAvailableBalance() == expectedVal,
                "New available balance calculations are incorrect." );

        p.clickBtn( p.addPaymentBtn() );
        p.assertNoFundsWarn();

    }

    @Test(priority = 3)
    public void addPaymentMethod_storeCredit() throws IOException {

        provideTestData("cart with 1 item && customer with SC");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.clickBtn( p.editBtn_payment() );
        p.clickBtn( p.newPaymentBtn() );
        p.selectPaymentType("Store Credit");
        p.setFieldVal( p.amountToUseFld(), String.valueOf(p.grandTotal()) );

        double expectedVal = cutDecimal( p.gcAvailableBalance() - p.grandTotal() );
        System.out.println(expectedVal);
        sleep(1000);
        Assert.assertTrue( p.gcNewAvailableBalance() == expectedVal,
                "New available balance calculations are incorrect." );

        p.clickBtn( p.addPaymentBtn() );
        p.assertNoFundsWarn();

    }

    @Test(priority = 4)
    public void addStoreCredit_exceedsTotal() throws IOException {

        provideTestData("cart with 1 item && customer with SC");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        double amountToUse = p.grandTotal() + 10.00;
        p.addPaymentMethod_SC( String.valueOf(amountToUse) );

        Assert.assertTrue(p.appliedAmount() == p.grandTotal(),
                "Amount of funds to be applied as a payment isn't auto-adjusted.");

    }

    @Test(priority = 5)
    public void addGiftCard_exceedsTotal() throws IOException {

        provideTestData("cart with 1 item && customer with GC");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        p.cartSummary().waitUntil(visible, 10000);
        double amountToUse = p.grandTotal() + 10.00;
        p.addPaymentMethod_GC(gcNumber, String.valueOf(amountToUse));

        Assert.assertTrue(p.appliedAmount() == p.grandTotal(),
                "Amount of funds to be applied as a payment isn't auto-adjusted.");

    }

}
