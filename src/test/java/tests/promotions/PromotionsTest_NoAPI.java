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
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void addNewPromo() {

        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();

        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        click( p.sideMenu("Promotions") );
        p.search(randomId);
        p.getPromoParamVal("1", "Promotion ID").shouldHave(text(promoId)
                .because("A just created promo isn't displayed on the list."));

    }

    @Test(priority = 2)
    public void addNewPromo_appearsOnList() throws IOException {

        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();

        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        p.getPromoParamVal("1", "Promotion ID").shouldHave(text(promoId)
                .because("Promotion isn't displayed on the list."));

    }

    @Test(priority = 3)
    public void couponApplyType_stateActive() {

        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();

        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        click( p.sideMenu("Promotions") );
        p.search(promoId);
        p.getPromoParamVal("1", "State").shouldHave(text("Active")
                .because("State isn't automatically set to 'Active' for 'Coupon' apply type promotions."));

    }

    @Test(priority = 4)
    public void editName() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        setFieldVal( p.nameFld(), "Edited Promo Name" );
        p.clickSave();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        p.getPromoParamVal("1", "Name").shouldHave(text("Edited Promo Name")
                .because("An old promo name is displayed on the list."));
        click( p.promotion(promoId) );
        p.nameFld().shouldHave(value("Edited Promo Name")
                .because("New promotion name hasn't been saved - an old one is displayed on promo details page."));

    }

    @Test(priority = 5)
    public void editStorefrontName() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        clearField( p.storefrontNameFld() );
        setFieldVal( p.storefrontNameFld(), "Edited Promo Storefront Name" );
        p.clickSave();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        p.getPromoParamVal("1", "Storefront Name").shouldHave(text("Edited Promo Storefront Name")
                .because("An old promo storefront name is displayed on the list."));
        click( p.promotion(promoId) );
        p.storefrontNameFld().shouldHave(text("Edited Promo Storefront Name")
                .because("New promotion storefront name hasn't been saved - an old one is displayed on promo details page."));

    }

    @Test(priority = 6)
    public void editDescription() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.clearField( p.descriptionFld() );
        setFieldVal( p.descriptionFld(), "Edited Promo Description" );
        p.clickSave();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        click( p.promotion(promoId) );
        p.descriptionFld().shouldHave(text("Edited Promo Description")
                .because("New promotion Description hasn't been saved - an old one is displayed on promo details page."));

    }

    @Test(priority = 7)
    public void editDetails() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.clearField( p.detailsFld() );
        setFieldVal( p.detailsFld(), "Edited Promo Details" );
        p.clickSave();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        click( p.promotion(promoId) );
        p.detailsFld().shouldHave(text("Edited Promo Details")
                .because("New promotion Details hasn't been saved - an old one is displayed on promo details page."));

    }

    @Test(priority = 8)
    public void editApplyType() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        setDdVal( p.applyTypeDd(), "Auto" );
        p.stateDd().shouldBe(visible
                .because("'State' dd doesn't appear after changing 'Apply Type' from 'Coupon' to 'Auto'."));
        p.clickSave();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        p.getPromoParamVal("1", "Apply Type").shouldHave(text("auto"));
        click( p.promotion(promoId) );
        p.stateVal().shouldHave(text("Inactive")
                .because("State value isn't 'Inactive'."));

    }

    @Test(priority = 9)
    public void setStateToInactive() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo_autoApply_active("Auto", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.stateDd().shouldBe(visible);
        p.setState("Inactive");
        p.clickSave();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        p.getPromoParamVal("1", "State").shouldHave(text("Inactive")
                .because("Incorrect promotion's 'State' value is displayed on the list."));
        click( p.promotion(promoId) );
        p.stateVal().shouldHave(text("Inactive")
                .because("Promotion's 'State' value isn't 'Inactive'."));

    }

    @Test(priority = 10)
    public void setStateToActive() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Auto", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.stateDd().shouldBe(visible);
        p.setState("Active");
        p.clickSave();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        p.getPromoParamVal("1", "State").shouldHave(text("Active")
                .because("Incorrect promotion's 'State' value is displayed on the list."));
        click( p.promotion(promoId) );
        p.stateVal().shouldHave(text("Active")
                .because("Promotion's 'State' value isn't 'Active' on promotion's details page."));

    }

    @Test(priority = 11)
    public void addTag() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.addTag("test promo");
        p.clickSave();

        click( p.sideMenu("Promotions") );
        p.search(promoId);
        click( p.promotion(promoId) );
        p.tag("test promo").shouldBe(visible
                .because("A just added tag isn't displayed on promotion details page."));

    }

    @Test(priority = 12)
    public void cantDuplicateTag() throws IOException {

        //Preconditions
        p = open(adminUrl + "/promotions", PromotionsPage.class);
        String randomId = generateRandomID();
        p.createNewPromo("Coupon", randomId);
        promoId = p.promotionIdVal().text();
        p.clickSave();

        //Test Steps
        p = open(adminUrl + "/promotions/" + promoId, PromotionsPage.class);
        p.addTag("test promo");
        p.addTag("test promo");

        p.allTags().shouldHaveSize(1);
//            "Tag is duplicated."

    }

}
