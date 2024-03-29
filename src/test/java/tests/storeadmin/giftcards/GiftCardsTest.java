package tests.storeadmin.giftcards;

import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.GiftCardsPage;
import pages.admin.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class GiftCardsTest extends Preconditions {

    private GiftCardsPage p;

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
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Can issue gift card")
    public void issueGiftCard() {
        p = openPage(adminUrl + "/gift-cards", GiftCardsPage.class);

        p.clickAddMewGCBtn();
        p.setType("Appeasement");
        p.setValue("123.78");
        p.clickIssueGCBtn();

        p.availableBalance().shouldHave(text("$123.78"));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Can issue GC with preset values")
    public void issueGiftCard_presetValues() {
        p = openPage(adminUrl + "/gift-cards", GiftCardsPage.class);

        p.clickAddMewGCBtn();
        p.setType("Appeasement");
        p.setPresetValue("50");
        p.clickIssueGCBtn();

        p.availableBalance().shouldHave(text("$50.00"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Issued gift card appears on the list")
    public void newGCVisibleOnList() {
        p = openPage(adminUrl + "/gift-cards", GiftCardsPage.class);

        p.clickAddMewGCBtn();
        p.setType("Appeasement");
        p.setValue("123.78");
        p.clickIssueGCBtn();
        shouldBeVisible(p.availableBalance(), "Waiting for \"Available Balance\" to become visible has failed");
        String gcNumber = p.getGCNumber(getUrl(), adminUrl);
        p.navigateTo("Gift Cards");
        p.search(gcNumber);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getGCParamVal("1", "Gift Card Number").shouldHave(text(gcNumber));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("A just issued GC is in Active state")
    public void newGCIsActive() {
        p = openPage(adminUrl + "/gift-cards", GiftCardsPage.class);

        p.clickAddMewGCBtn();
        p.setType("Appeasement");
        p.setValue("123.78");
        p.clickIssueGCBtn();
        shouldHaveText(p.stateVal(), "Active", "Incorrect 'State' val is displayed on GC details page");
        String gcNumber = p.getGCNumber(getUrl(), adminUrl);
        p.navigateTo("Gift Cards");
        p.search(gcNumber);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getGCParamVal("1", "State").shouldHave(text("Active"));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Can hold active GC")
    public void holdGC() throws IOException {
        provideTestData("gift card");

        p = openPage(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);
        p.setState("Hold");
        p.clickYes();
        shouldNotBeVisible(p.yesBtn(), "Failed to wait until yesBtn won't be visible");
        shouldHaveText(p.stateVal(), "On Hold",
                "Failed to edit GC state - incorrect state value is displayed.");
        refresh();

        p.stateVal().shouldHave(text("On Hold"));
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Can cancel a just issued GC")
    public void cancelNewGC() throws IOException {
        provideTestData("gift card");

        p = openPage(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);
        p.setState("Cancel Gift Card");
        p.setCancelReason("1");
        p.clickYes();
        shouldNotBeVisible(p.yesBtn(), "Failed to wait until yesBtn won't be visible");
        sleep(1000);
        refresh();
        shouldHaveText($(By.xpath("//span[@class='fc-model-state']")), "Canceled",
                "GC is not in 'Canceled' state." );

        p.stateDd().shouldNot(exist);
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Can't cancel used GC -- \"Cancel Gift Card\" option shouldn't be displayed in \"State\" dd")
    public void cantCancelUsedGC() throws IOException {
        provideTestData("used gift card");

        p = openPage(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);
        click( p.stateDd() );

        $(By.xpath("//div[text()='Cancel Gift Card']")).shouldNot(exist
                .because("'Cancel Gift Card' option is available for select for used GC."));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("\"State\" val is updated in search_view once edited")
    public void editState_visibleOnList() throws IOException {
        provideTestData("gift card");

        p = openPage(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);
        p.setState("Hold");
        p.clickYes();
        p.yesBtn().shouldNotBe(visible);
        shouldNotBeVisible(p.yesBtn(), "Failed to wait untill yesBtn won't be visible");
        p.navigateTo("Gift Cards");
        p.search(gcCode);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getGCParamVal("1", "State").shouldHave(text("On Hold"));
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Can make on hold GC active back")
    public void setStateToActive() throws IOException {
        provideTestData("gift card on hold");

        p = openPage(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);
        p.setState("Activate");
        p.clickYes();
        refresh();
        shouldHaveText(p.stateVal(), "Active", "Failed to set GC \"State\" to \"Active\"");

//        make this check a separate test -- ES specific
//        p.navigateTo("Gift Cards");
//        p.search(gcCode);
//
//        p.getGCParamVal("1", "State").shouldHave(text("Active")
//                .because("Incorrect 'State' value is displayed on the list."));

    }

    @Test(priority = 10)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Can cancel state change")
    public void cancelStateChange() throws IOException {
        provideTestData("gift card");

        p = openPage(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);
        p.setState("Hold");
        p.clickCancel();
        shouldHaveText(p.stateVal(), "Active", "\"State\" val isn't reverted back to initial value");
        refresh();

        p.stateVal().shouldHave(text("Active"));
    }

    @Test(priority = 11)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("\"Amount to use\" should be cut down to order's grand total when applying GC as the only payment method with amount to use that exceeds order's GT")
    public void gcAsPaymentMethod_availableBalance() throws IOException {
        provideTestData("used gift card");
        String expectedBalance = totalToString(20000 - total);

        p = openPage(adminUrl + "/gift-cards/" + gcCode, GiftCardsPage.class);

        p.availableBalance().shouldHave(text(expectedBalance));
    }

    //Feature got removed temporarily
//    @Test(priority = 12)
//    public void addNewGC_issueToCustomer() throws IOException {
//        provideTestData("a customer");
//        p = openPage(adminUrl + "/gift-cards", GiftCardsPage.class);
//
//        p.clickAddMewGCBtn();
//        p.setType("Appeasement");
//        p.setValue("123.78");
//        p.issueGCToCustomer(customerName, "Test message for customer");
//        click( p.issueGCBtn() );
//
//        p.availableBalance().shouldHave(text("$123.78")
//                .because("Failed to issue new GC"));
//    }

    @Test(priority = 13)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Gift Cards")
    @Description("Can issue miltiple gift cards at once")
    public void issueMultipleGCs() {
        String balance = generateRandomBalanceVal();

        p = openPage(adminUrl + "/gift-cards", GiftCardsPage.class);
        p.waitForDataToLoad();
        p.clickAddMewGCBtn();
        p.setType("Appeasement");
        p.setValue(balance);
        p.increaseQtyBy(1);
        p.clickIssueGCBtn();
        p.waitForDataToLoad();
        p.addFilter("Gift Card : Original Balance", "=", balance);

        p.itemsOnList().shouldHave(size(2));
    }

}