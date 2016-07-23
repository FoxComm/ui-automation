package tests.giftcards;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GiftCardsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertEquals;

public class GiftCardsTest extends DataProvider {

    private GiftCardsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

//    @Test(priority = 1)
//    public void addGiftCard() {
//
//        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);
//
//        click( p.addNewGCBtn() );
//        setDdVal( p.typeDd(), "Appeasement" );
//        setFieldVal( p.valueFld(), "123.78" );
//        click( p.issueGCBtn() );
//        p.availableBalance().shouldBe(visible);
//        assertEquals( p.availableBalanceVal(), "123.78",
//                "Incorrect available balance value.");
//
//    }
//
//    @Test(priority = 2)
//    public void addGiftCard_presetValues() {
//
//        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);
//
//        click( p.addNewGCBtn() );
//        setDdVal( p.typeDd(), "Appeasement" );
//        click( p.presetValue("50") );
//        click( p.issueGCBtn() );
//        p.availableBalance().shouldBe(visible);
//        assertEquals( p.availableBalance().text(), "$50.00",
//                "Incorrect available balance value.");
//
//    }
//
//    @Test(priority = 3)
//    public void newGCVisibleOnList() {
//
//        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);
//
//        click( p.addNewGCBtn() );
//        setDdVal( p.typeDd(), "Appeasement" );
//        setFieldVal( p.valueFld(), "123.78" );
//        click( p.issueGCBtn() );
//        p.availableBalance().shouldBe(visible);
//        String gcNumber = getUrl().substring(46, 62);
//        click( p.gcNavMenu() );
//        p.search(gcNumber);
//        assertEquals( p.getGCParamVal("1", "Gift Card Number"), gcNumber,
//                "Search failed to find a just created GC.");
//
//    }
//
//    @Test(priority = 4)
//    public void newGCIsActive() {
//
//        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);
//
//        click( p.addNewGCBtn() );
//        setDdVal( p.typeDd(), "Appeasement" );
//        setFieldVal( p.valueFld(), "123.78" );
//        click( p.issueGCBtn() );
//        p.availableBalance().shouldBe(visible);
//        assertEquals( p.stateVal(), "Active",
//                "Incorrect 'State' value is displayed on GC details page.");
//
//        String gcNumber = getUrl().substring(46, 62);
//        click( p.gcNavMenu() );
//        p.search(gcNumber);
//        assertEquals( p.getGCParamVal("1", "State"), "Active",
//                "A just created gift card isn't in 'Active state'.");
//
//    }
//
////    @Test
////    public void addNewGC_issueToCustomer() throws IOException {
////
////        provideTestData("a customer");
////        p = open(adminUrl + "/products", GiftCardsPage.class);
////
////        click( p.addNewGCBtn() );
////        setDdVal( p.gcTypeDd(), "Appeasement" );
////        setFieldVal( p.value(), "123.78" );
////        jsClick( p.gcToCustomerChbx() );
////        selectCustomer(customerName);
////
////        // incomplete because of a bug
////
////        click( p.issueGCBtn() );
////
////
////    }
//
//    @Test(priority = 5)
//    public void holdGC() throws IOException {
//
//        provideTestData("gift card");
//        p = open(adminUrl + "/gift-cards/" + gcNumber, GiftCardsPage.class);
//
//        setDdVal( p.stateDd(), "Hold" );
//        click( p.yesBtn() );
//        p.yesBtn().shouldNotBe(visible);
//        assertEquals( p.stateVal(), "On Hold",
//                "Failed to edit GC state - incorrect state value is displayed.");
//        refresh();
//        assertEquals( p.stateVal(), "On Hold",
//                "Failed to edit GC state - incorrect state value is displayed.");
//
//    }
//
//    @Test(priority = 6)
//    public void cancelNewGC() throws IOException {
//
//        provideTestData("gift card");
//        p = open(adminUrl + "/gift-cards/" + gcNumber, GiftCardsPage.class);
//
//        setDdVal( p.stateDd(), "Cancel Gift Card" );
//        setDdVal( p.cancelReasonDd(), "Other cancellation reason" );
//        click( p.yesBtn() );
//        p.yesBtn().shouldNotBe(visible);
//        sleep(1000);
//        refresh();
//        assertEquals( $(By.xpath("//span[@class='fc-model-state']")).text(), "Canceled",
//                "GC is not in 'Canceled' state.");
//        assertTrue( !p.stateDd().is(exist), "'State' dropdown is displayed.");
//
//    }
//
//    @Test(priority = 7)
//    public void cantCancelUsedGC() throws IOException {
//
//        provideTestData("used gift card");
//        p = open(adminUrl + "/gift-cards/" + gcNumber, GiftCardsPage.class);
//
//        click( p.stateDd() );
//        assertTrue( !$(By.xpath("//div[text()='Cancel Gift Card']")).is(exist),
//                "'Cancel Gift Card' option is available for select for used GC.");
//
//    }
//
//    @Test(priority = 8)
//    public void editState_visibleOnList() throws IOException {
//
//        provideTestData("gift card");
//        p = open(adminUrl + "/gift-cards/" + gcNumber, GiftCardsPage.class);
//
//        setDdVal( p.stateDd(), "Hold" );
//        click( p.yesBtn() );
//        p.yesBtn().shouldNotBe(visible);
//        click( p.gcNavMenu() );
//        p.search( gcNumber );
//        assertEquals( p.getGCParamVal("1", "State"), "On Hold" );
//
//    }
//
//    @Test(priority = 9)
//    public void setStateToActive() throws IOException {
//
//        provideTestData("gift card on hold");
//        p = open(adminUrl + "/gift-cards/" + gcNumber, GiftCardsPage.class);
//
//        setDdVal( p.stateDd(), "Activate" );
//        click( p.yesBtn() );
//        refresh();
//        assertEquals( p.stateVal(), "Active", "Failed to set GC 'State' to 'Active'.");
//        click( p.gcNavMenu() );
//        p.search(gcNumber);
//        assertEquals( p.getGCParamVal("1", "State"), "Active", "Incorrect 'State' value is displayed on the list.");
//
//    }
//
//    @Test(priority = 10)
//    public void cancelStateChange() throws IOException {
//
//        provideTestData("gift card");
//        p = open(adminUrl + "/gift-cards/" + gcNumber, GiftCardsPage.class);
//
//        setDdVal( p.stateDd(), "Hold" );
//        click( p.cancelBtn() );
//        p.yesBtn().shouldNotBe(visible);
//        assertEquals( p.stateVal(), "Active",
//                "'Current state dd value isn't reset to actual GC 'State' value after canceling state change.");
//
//    }

    @Test(priority = 11)
    public void gcAsPaymentMethod_availableBalance() throws IOException {

        provideTestData("used gift card");
        p = open(adminUrl + "/gift-cards/" + gcNumber, GiftCardsPage.class);

        p.availableBalance().shouldBe(visible);
        assertEquals( p.availableBalanceVal(), "162.73",
                "Incorrect available balance value after placing order with GC as a payment method.");

    }



}