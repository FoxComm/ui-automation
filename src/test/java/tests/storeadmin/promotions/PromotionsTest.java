package tests.storeadmin.promotions;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.PromotionsPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class PromotionsTest extends Preconditions {

    private PromotionsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            loginPage.userMenuBtn().shouldBe(visible);
        }
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("Create new promotion")
    public void createNewPromo() {
        String randomId = generateRandomID();

        p = openPage(adminUrl + "/promotions/new", PromotionsPage.class);
        p.fillOutNewPromoForm("coupon", randomId);
        p.clickSave_wait();
        shouldNotHaveText(p.promoIdBreadcrumb(), "new", "\"new\" isn't changed to promoId value of a just created promo");
        String promoId = p.promoIdBreadcrumb().text();
        p.navigateTo("Promotions");
        p.search(randomId);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getPromoParamVal("1", "Promotion ID").shouldHave(text(promoId));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("A just created promotion appears on the list in category")
    public void createNewPromo_appearsOnList() throws IOException {
        provideTestData("a promotion");

        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        p.search(promotionId);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getPromoParamVal("1", "Promotion ID").shouldHave(text(promotionId));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("Can create a new promo of a coupon type in active state")
    public void couponApplyType_stateActive() {
        String randomId = generateRandomID();

        p = openPage(adminUrl + "/promotions/new", PromotionsPage.class);
        p.fillOutNewPromoForm("Coupon", randomId);
        p.clickSave_wait();
        shouldNotHaveText(p.promoIdBreadcrumb(), "new", "\"new\" isn't changed to promoId value of a just created promo");
        p.navigateTo("Promotions");
        p.search(promotionId);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getPromoParamVal("1", "State").shouldHave(text("Active"));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("Can add a text custom property")
    public void addCustomPropTextFld() throws IOException {
        provideTestData("a promotion");

        p = openPage(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        p.addCustomProp("text", "Custom Field");
        setFieldVal(p.customText("Custom Field"), "Custom Text");
        p.clickSave_wait();
        p.navigateTo("Promotions");
        p.search(promotionId);
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.openPromo(promotionId);

        p.customText("Custom Field").shouldHave(value("Custom Text"));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("Can edit apply type")
    public void editApplyType() throws IOException {
        provideTestData("a promotion");

        p = openPage(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        p.setApplyType("auto");
        shouldBeVisible(p.stateDd(), "Failed to want until \"State\" dd will become visible");
        p.clickSave_wait();
        p.navigateTo("Promotions");
        p.search(promotionId);
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.getPromoParamVal("1", "Apply Type").shouldHave(text("auto"));
        p.openPromo(promotionId);

        p.applyTypeRbtn("auto").shouldBe(selected);
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("Can change promo state to inactive")
    public void setStateToInactive() throws IOException {
        provideTestData("active promotion with auto apply type");

        p = openPage(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        shouldBeVisible(p.stateDd(), "Failed to want until \"State\" dd will become visible");
        p.setState("Inactive");
        p.clickSave_wait();
        p.navigateTo("Promotions");
        p.search(promotionId);
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.getPromoParamVal("1", "State").shouldHave(text("Inactive"));
        p.openPromo(promotionId);

        p.stateVal().shouldHave(text("Inactive"));
    }

    @Test(priority = 10)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("Can change promo state to active")
    public void setStateToActive() throws IOException {
        provideTestData("inactive promotion with auto apply type");

        p = openPage(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        shouldBeVisible(p.stateDd(), "Failed to want until \"State\" dd will become visible");
        p.setState("Active");
        p.clickSave_wait();
        p.navigateTo("Promotions");
        p.search(promotionId);
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.getPromoParamVal("1", "State").shouldHave(text("Active"));
        p.openPromo(promotionId);

        p.stateVal().shouldHave(text("Active"));
    }

    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("Can add tag")
    @Test(priority = 11)
    public void addTag() throws IOException {
        provideTestData("a promotion");

        p = openPage(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        p.addTag("test promo");
        p.clickSave_wait();
        refresh();

        p.tag("test promo").shouldBe(visible);
    }

    @Test(priority = 12)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Promotions & Coupons")
    @Description("Can't add the same tag twice")
    public void cantDuplicateTag() throws IOException {
        p = openPage(adminUrl + "/promotions/new", PromotionsPage.class);
        p.addTag("test promo");
        p.addTag("test promo");

        p.allTags().shouldHaveSize(1);
    }

//    createCoupon_singleCode_active

//    createCoupon_bulkCodes_active


}
