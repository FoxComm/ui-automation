package tests.giftcards;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.GiftCardsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

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

    @Test(priority = 1)
    public void addGiftCard() {

        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);

        click( p.addNewGCBtn() );
        setDdVal( p.typeDd(), "Appeasement" );
        setFieldVal( p.valueFld(), "123.78" );
        click( p.issueGCBtn() );
        p.availableBalanceVal().shouldHave(text("$123.78")
                .because("Incorrect available balance value."));

    }

    @Test(priority = 2)
    public void addGiftCard_presetValues() {

        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);

        click( p.addNewGCBtn() );
        setDdVal( p.typeDd(), "Appeasement" );
        click( p.presetValue("50") );
        click( p.issueGCBtn() );
        p.availableBalanceVal().shouldHave(text("$50.00")
                .because("Incorrect available balance value."));

    }

    @Test(priority = 3)
    public void newGCVisibleOnList() {

        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);

        click( p.addNewGCBtn() );
        setDdVal( p.typeDd(), "Appeasement" );
        setFieldVal( p.valueFld(), "123.78" );
        click( p.issueGCBtn() );
        p.availableBalanceVal().shouldBe(visible);
        String gcNumber = getUrl().substring(46, 62);
        click( p.sideMenu("Gift Cards") );
        p.search(gcNumber);
        p.getGCParamVal("1", "Gift Card Number").shouldHave(text(gcNumber)
                .because("Search failed to find a just created GC."));

    }

    @Test(priority = 4)
    public void newGCIsActive() {

        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);

        click( p.addNewGCBtn() );
        setDdVal( p.typeDd(), "Appeasement" );
        setFieldVal( p.valueFld(), "123.78" );
        click( p.issueGCBtn() );
        p.stateVal().shouldHave(text("Active")
                .because("Incorrect 'State' value is displayed on GC details page."));

        String gcNumber = getUrl().substring(46, 62);
        click( p.sideMenu("Gift Cards") );
        p.search(gcNumber);
        p.getGCParamVal("1", "State").shouldHave(text("Active")
                .because("A just created gift card isn't in 'Active state'."));

    }

    @Test(priority = 5)
    public void holdGC() throws IOException {

        provideTestData("gift card");
        p = open(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);

        setDdVal( p.stateDd(), "Hold" );
        click( p.yesBtn() );
        p.yesBtn().shouldNotBe(visible);
        p.stateVal().shouldHave(text("On Hold")
                .because("Failed to edit GC state - incorrect state value is displayed."));
        refresh();
        p.stateVal().shouldHave(text("On Hold")
                .because("Failed to edit GC state - incorrect state value is displayed."));

    }

    @Test(priority = 6)
    public void cancelNewGC() throws IOException {

        provideTestData("gift card");
        p = open(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);

        setDdVal( p.stateDd(), "Cancel Gift Card" );
        setDdVal( p.cancelReasonDd(), "Other cancellation reason" );
        click( p.yesBtn() );
        p.yesBtn().shouldNotBe(visible);
        sleep(1000);
        refresh();
        $(By.xpath("//span[@class='fc-model-state']")).shouldHave(text("Canceled")
                .because("GC is not in 'Canceled' state."));
        p.stateDd().shouldNot(exist
                .because("'State' dropdown is displayed."));

    }

    @Test(priority = 7)
    public void cantCancelUsedGC() throws IOException {

        provideTestData("used gift card");
        p = open(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);

        click( p.stateDd() );
        $(By.xpath("//div[text()='Cancel Gift Card']")).shouldNot(exist
                .because("'Cancel Gift Card' option is available for select for used GC."));

    }

    @Test(priority = 8)
    public void editState_visibleOnList() throws IOException {

        provideTestData("gift card");
        p = open(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);

        setDdVal( p.stateDd(), "Hold" );
        click( p.yesBtn() );
        p.yesBtn().shouldNotBe(visible);
        click( p.sideMenu("Gift Cards") );
        p.search(gcCode);
        p.getGCParamVal("1", "State").shouldHave(text("On Hold")
                .because("Failed to set GC's state to 'On Hold'."));

    }

    @Test(priority = 9)
    public void setStateToActive() throws IOException {

        provideTestData("gift card on hold");
        p = open(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);

        setDdVal( p.stateDd(), "Activate" );
        click( p.yesBtn() );
        refresh();
        p.stateVal().shouldHave(text("Active")
                .because("Failed to set GC 'State' to 'Active'."));
        click( p.sideMenu("Gift Cards") );
        p.search(gcCode);
        p.getGCParamVal("1", "State").shouldHave(text("Active")
                .because("Incorrect 'State' value is displayed on the list."));

    }

    @Test(priority = 10)
    public void cancelStateChange() throws IOException {

        provideTestData("gift card");
        p = open(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);

        setDdVal( p.stateDd(), "Hold" );
        click( p.cancelBtn() );
        p.yesBtn().shouldNotBe(visible);
        p.stateVal().shouldHave(text("Active")
                .because("'Current state dd value isn't reset to actual GC 'State' value after canceling state change."));

    }

    @Test(priority = 11)
    public void gcAsPaymentMethod_availableBalance() throws IOException {

        provideTestData("used gift card");
        p = open(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);

        p.availableBalanceVal().shouldHave(text("$162.73")
                .because("Incorrect available balance value after placing order with GC as a payment method."));

    }

    @Test(priority = 12)
    public void addNewGC_issueToCustomer() throws IOException {

        provideTestData("a customer");
        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);

        click( p.addNewGCBtn() );
        setDdVal( p.typeDd(), "Appeasement" );
        setFieldVal( p.valueFld(), "123.78" );
        jsClick( p.gcToCustomerChbx() );
        setFieldVal( p.chooseCustomerFld(), customerName );
        p.selectCustomer(customerName);
        click( p.addCustomerBtn() );
        p.chosenCustomer(customerName).shouldBe(visible.because("Customer is not selected."));
        setFieldVal( p.messageFld(), "Test message for customer" );
        click( p.issueGCBtn() );

        p.gcAvailableBalance().shouldHave(text("$123.78")
                .because("Failed to issue new GC"));

    }

    @Test(priority = 13)
    public void issueMultipleGCs() {

        p = open(adminUrl + "/gift-cards", GiftCardsPage.class);

        String expectedResult = addToString(p.counter().text(), 2);
        p.waitForDataToLoad();
        click( p.addNewGCBtn() );
        setDdVal( p.typeDd(), "Appeasement" );
        setFieldVal( p.valueFld(), "123.78" );
        click( p.qtyIncrBtn() );
        click( p.issueGCBtn() );
        p.waitForDataToLoad();
        p.counter().shouldHave(text(expectedResult)
                .because("Incorrect GCs amount counter - either GCs creation has failed or counter value isn't updated."));

    }

}