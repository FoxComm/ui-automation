package tests.promotions;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PromotionsPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class PromotionsTest extends DataProvider {

    private PromotionsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            loginPage.userMenuBtn().shouldBe(visible);
        }
    }

//    @Test(priority = 1)
//    public void addNewPromo() {
//        String randomId = generateRandomID();
//
//        p = open(adminUrl + "/promotions/new", PromotionsPage.class);
//        p.fillOutNewPromoForm("Coupon", randomId);
//        p.clickSave_wait();
//        shouldNotHaveText(p.promoIdBreadcumb(), "new", "\"new\" isn't changed to promoId value of a just created promo");
//        String promoId = p.promoIdBreadcumb().text();
//        p.navigateTo("Marketing", "Promotions");
//        p.search(randomId);
//
//        p.getPromoParamVal("1", "Promotion ID").shouldHave(text(promoId));
//    }
//
//    @Test(priority = 2)
//    public void addNewPromo_appearsOnList() throws IOException {
//        provideTestData("a promotion");
//
//        p = open(adminUrl + "/promotions", PromotionsPage.class);
//        p.search(promotionId);
//
//        p.getPromoParamVal("1", "Promotion ID").shouldHave(text(promotionId));
//    }
//
//    @Test(priority = 3)
//    public void couponApplyType_stateActive() {
//        String randomId = generateRandomID();
//
//        p = open(adminUrl + "/promotions/new", PromotionsPage.class);
//        p.fillOutNewPromoForm("Coupon", randomId);
//        p.clickSave_wait();
//        shouldNotHaveText(p.promoIdBreadcumb(), "new", "\"new\" isn't changed to promoId value of a just created promo");
//        p.navigateTo("Marketing", "Promotions");
//        p.search(promotionId);
//
//        p.getPromoParamVal("1", "State").shouldHave(text("Active"));
//    }
//
//    @Test(priority = 4)
//    public void editName() throws IOException {
//        provideTestData("a promotion");
//
//        p = open(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
//        p.setPromoName("Edited Promo Name");
//        p.clickSave_wait();
//        p.navigateTo("Marketing", "Promotions");
//        p.search(promotionId);
//        p.getPromoParamVal("1", "Name").shouldHave(text("Edited Promo Name")
//                .because("An old promo name is displayed on the list."));
//        p.openPromo(promotionId);
//
//        p.nameFld().shouldHave(value("Edited Promo Name"));
//    }
//
//    @Test(priority = 5)
//    public void editStorefrontName() throws IOException {
//        provideTestData("a promotion");
//
//        p = open(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
//        clearField(p.storefrontNameFld());
//        p.setStorefrontName("Edited Promo Storefront Name");
//        p.clickSave_wait();
//        p.navigateTo("Marketing", "Promotions");
//        p.search(promotionId);
//        p.getPromoParamVal("1", "Storefront Name").shouldHave(text("Edited Promo Storefront Name")
//                .because("An old promo storefront name is displayed on the list."));
//        p.openPromo(promotionId);
//
//        p.storefrontNameFld().shouldHave(text("Edited Promo Storefront Name"));
//    }
//
//    @Test(priority = 6)
//    public void editDescription() throws IOException {
//        provideTestData("a promotion");
//
//        p = open(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
//        p.clearField( p.descriptionFld() );
//        p.setDescription("Edited Promo Description");
//        p.clickSave_wait();
//        p.navigateTo("Marketing", "Promotions");
//        p.search(promotionId);
//        p.openPromo(promotionId);
//
//        p.descriptionFld().shouldHave(text("Edited Promo Description"));
//    }
//
//    @Test(priority = 7)
//    public void editDetails() throws IOException {
//        provideTestData("a promotion");
//
//        p = open(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
//        p.clearField( p.detailsFld() );
//        p.setDetails("Edited Promo Details");
//        p.clickSave_wait();
//        p.navigateTo("Marketing", "Promotions");
//        p.search(promotionId);
//        p.openPromo(promotionId);
//
//        p.detailsFld().shouldHave(text("Edited Promo Details"));
//    }

    @Test(priority = 8)
    public void editApplyType() throws IOException {
        provideTestData("a promotion");

        p = open(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        p.setApplyType("Auto");
        shouldBeVisible(p.stateDd(), "Failed to want until \"State\" dd will become visible");
        p.clickSave_wait();
        p.navigateTo("Marketing", "Promotions");
        p.search(promotionId);
        p.getPromoParamVal("1", "Apply Type").shouldHave(text("auto"));
        p.openPromo(promotionId);

        p.applyTypeVal().shouldHave(text("Auto"));
    }

    @Test(priority = 9)
    public void setStateToInactive() throws IOException {
        provideTestData("active promotion with auto apply type");

        p = open(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        shouldBeVisible(p.stateDd(), "Failed to want until \"State\" dd will become visible");
        p.setState("Inactive");
        p.clickSave_wait();
        p.navigateTo("Marketing", "Promotions");
        p.search(promotionId);
        p.getPromoParamVal("1", "State").shouldHave(text("Inactive"));
        p.openPromo(promotionId);

        p.stateVal().shouldHave(text("Inactive"));
    }

    @Test(priority = 10)
    public void setStateToActive() throws IOException {
        provideTestData("inactive promotion with auto apply type");

        p = open(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        shouldBeVisible(p.stateDd(), "Failed to want until \"State\" dd will become visible");
        p.setState("Active");
        p.clickSave_wait();
        p.navigateTo("Marketing", "Promotions");
        p.search(promotionId);
        p.getPromoParamVal("1", "State").shouldHave(text("Active"));
        p.openPromo(promotionId);

        p.stateVal().shouldHave(text("Active"));
    }

    @Description("Fails due confirmation box bug")
    @Test(priority = 11)
    public void addTag() throws IOException {
        provideTestData("a promotion");

        p = open(adminUrl + "/promotions/" + promotionId, PromotionsPage.class);
        p.addTag("test promo");
        p.clickSave_wait();
        refresh();

        p.tag("test promo").shouldBe(visible);
    }

    @Test(priority = 12)
    public void cantDuplicateTag() throws IOException {
        p = openPage(adminUrl + "/promotions/new", PromotionsPage.class);
        p.addTag("test promo");
        p.addTag("test promo");

        p.allTags().shouldHaveSize(1);
    }

}
