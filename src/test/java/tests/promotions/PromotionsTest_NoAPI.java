package tests.promotions;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.PromotionsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;

public class PromotionsTest_NoAPI extends DataProvider {

    private PromotionsPage p;
    private String promoId;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

    @Test(priority = 1)
    public void addNewPromo() {

        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();

        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.navigateTo("Marketing", "Promotions");
        p.search(randomId);
        p.getPromoParamVal("1", "Promotion ID").shouldHave(text(promoId)
                .because("A just created promo isn't displayed on the list."));

    }

    @Test(priority = 2)
    public void addNewPromo_appearsOnList() throws IOException {

        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();

        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        p.getPromoParamVal("1", "Promotion ID").shouldHave(text(promoId)
                .because("Promotion isn't displayed on the list."));

    }

    @Test(priority = 3)
    public void couponApplyType_stateActive() {

        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();

        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        p.getPromoParamVal("1", "State").shouldHave(text("Active")
                .because("State isn't automatically set to 'Active' for 'Coupon' apply type promotions."));

    }

    @Test(priority = 4)
    public void editName() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        setFieldVal( p.nameFld(), "Edited Promo Name" );
        p.clickSave();

        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        shouldHaveText(p.getPromoParamVal("1", "Name"), "Edited Promo Name",
                "Incorrect \"Name\" is displayed on the list");
        p.openPromo(promoId);
        p.nameFld().shouldHave(value("Edited Promo Name")
                .because("New promotion name hasn't been saved - an old one is displayed on promo details page."));

    }

    @Test(priority = 5)
    public void editStorefrontName() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        clearField(p.storefrontNameFld());
        p.setStorefrontName("Edited Promo Storefront Name");
        p.clickSave();

        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        shouldHaveText(p.getPromoParamVal("1", "Storefront Name"), "Edited Promo Storefront Name",
                "Incorrect \"Storefront Type\" is displayed on the list");
        p.openPromo(promoId);
        p.storefrontNameFld().shouldHave(text("Edited Promo Storefront Name")
                .because("New promotion storefront name hasn't been saved - an old one is displayed on promo details page."));

    }

    @Test(priority = 6)
    public void editDescription() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.clearField( p.descriptionFld() );
        p.setDescription("Edited Promo Description");
        p.clickSave();

        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        p.openPromo(promoId);
        p.descriptionFld().shouldHave(text("Edited Promo Description")
                .because("New promotion Description hasn't been saved - an old one is displayed on promo details page."));

    }

    @Test(priority = 7)
    public void editDetails() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.clearField( p.detailsFld() );
        p.setDetails("Edited Promo Details");
        p.clickSave();

        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        p.openPromo(promoId);
        p.detailsFld().shouldHave(text("Edited Promo Details")
                .because("New promotion Details hasn't been saved - an old one is displayed on promo details page."));

    }

    @Test(priority = 8)
    public void editApplyType() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.setApplyType("Auto");
        shouldBeVisible(p.stateDd(), "\"State\" dd isn't displayed after changing \"Apply Type\" from \"Coupon\" to \"Auto\"");
        p.clickSave();

        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        p.getPromoParamVal("1", "Apply Type").shouldHave(text("auto"));
        shouldHaveText(p.getPromoParamVal("1", "Apply Type"), "auto",
                "Incorrect \"Apply Type\" is displayed on the list");
        p.openPromo(promoId);
        p.stateVal().shouldHave(text("Inactive")
                .because("State value isn't \"Inactive\"."));

    }

    @Test(priority = 9)
    public void setStateToInactive() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo_autoApply_active("Auto", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.stateDd().shouldBe(visible);
        shouldBeVisible(p.stateDd(), "Failed to wait for \"State\" dd to become visible");
        p.setState("Inactive");
        p.clickSave();

        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        p.getPromoParamVal("1", "State").shouldHave(text("Inactive")
                .because("Incorrect promotion's \"State\" value is displayed on the list."));
        shouldHaveText(p.getPromoParamVal("1", "State"), "Inactive",
                "Incorrect \"State\" is displayed on the list");
        p.openPromo(promoId);
        p.stateVal().shouldHave(text("Inactive")
                .because("Promotion's 'State' value isn't 'Inactive'."));

    }

    @Test(priority = 10)
    public void setStateToActive() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Auto", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        shouldBeVisible(p.stateDd(), "Failed to wait for \"State\" dd to become visible");
        p.setState("Active");
        p.clickSave();

        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        shouldHaveText(p.getPromoParamVal("1", "State"), "Active",
                "Incorrect \"State\" is displayed on the list");
        p.openPromo(promoId);
        p.stateVal().shouldHave(text("Active")
                .because("Promotion's 'State' value isn't 'Active' on promotion's details page."));

    }

    @Test(priority = 11)
    public void addTag() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.addTag("test promo");
        p.clickSave();

        p.navigateTo("Marketing", "Promotions");
        p.search(promoId);
        p.openPromo(promoId);
        p.tag("test promo").shouldBe(visible
                .because("A just added tag isn't displayed on promotion details page."));

    }

    @Test(priority = 12)
    public void cantDuplicateTag() throws IOException {

        //Preconditions
        p = openPage(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = openPage(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.addTag("test promo");
        p.addTag("test promo");

        p.allTags().shouldHaveSize(1);
//            "Tag is duplicated."

    }

}