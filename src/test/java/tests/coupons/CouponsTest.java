package tests.coupons;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CouponsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;

public class CouponsTest extends DataProvider {

    private CouponsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
            loginPage.userMenuBtn().shouldBe(visible);
        }

    }

    @Test(priority = 1)
    public void createCoupon_singleCode_active() throws IOException {

        provideTestData("a promotion");
        p = open(adminUrl + "/coupons/", CouponsPage.class);
        String randomId = generateRandomID();

        click( p.addNewCoupon() );
        setFieldVal( p.nameFld(), "test coupon " + randomId );
        setFieldVal( p.storefrontNameFld(), "that's a storefront name" );
        setFieldVal( p.descriptionFld(), "that's nothing but another test coupon." );
        setFieldVal( p.detailsFld(), "get 10% off for any order" );
        p.setPromotion(promotionId);
        jsClick( p.singleCodeRbtn() );
        setFieldVal( p.singleCodeFld(), "NWCPN-" + randomId );
        click( p.saveBtn() );
        click( p.sideMenu("Coupons") );
        p.waitForDataToLoad();

        p.getCouponParamVal("1", "Name").shouldHave(text("test coupon " + randomId)
                .because("A just created coupon isn't displayed on the list."));

    }

    @Test(priority = 2)
    public void createCoupon_bulkCodes_active() throws IOException {

        provideTestData("a promotion");
        p = open(adminUrl + "/coupons/", CouponsPage.class);
        String randomId = generateRandomID();

        click( p.addNewCoupon() );
        setFieldVal( p.nameFld(), "test coupon " + randomId );
        setFieldVal( p.storefrontNameFld(), "that's a storefront name" );
        setFieldVal( p.descriptionFld(), "that's nothing but another test coupon." );
        setFieldVal( p.detailsFld(), "get 10% off for any order" );
        p.setPromotion(promotionId);
        jsClick( p.bulkGenerateCodesBrtn() );
        p.bulkGenerateCodes(4, "BULKCPN_" + randomId + "-", 5);
        p.setState("Active");
        p.couponIdVal().shouldNotHave(text("new")
                .because("Failed to create a new coupon."));

        click( p.saveBtn() );
        click( p.sideMenu("Coupons") );
        p.waitForDataToLoad();
        p.getCouponParamVal("1", "Name").shouldHave(text("test coupon " + randomId)
                .because("A just created coupon isn't displayed on the list."));

    }

    @Test(priority = 3)
    public void codesDisplayedInCodesTab_bulkGenerated() throws IOException {

        provideTestData("coupon with bulk generated codes");
        p = open(adminUrl + "/coupons/" + couponId + "/codes", CouponsPage.class);

        p.assertCodesGenerated(5);

    }

    @Test(priority = 4)
    public void codesDisplayedInCodesTab_singleCode() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId + "/codes", CouponsPage.class);

        p.assertCodesGenerated(1);

    }

    @Test(priority = 5)
    public void editCoupon_name() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);
        String randomId = generateRandomID();

        setFieldVal( p.nameFld(), "edited coupon " + randomId );
        p.clickSave();
        click( p.sideMenu("Coupons") );
        p.search(couponId);

        p.getCouponParamVal("1", "Name").shouldHave(text("edited coupon " + randomId)
                .because("Failed to edit coupon's 'Name'."));

    }

    @Test(priority = 6)
    public void editCoupon_storefrontName() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField( p.storefrontNameFld() );
        setFieldVal( p.storefrontNameFld(), "edited SF name" );
        p.clickSave();
        click( p.sideMenu("Coupons") );
        p.search(couponId);

        p.getCouponParamVal("1", "Storefront Name").shouldHave(text("<p>edited SF name</p>")
                .because("Failed to edit 'Storefront Name'."));

    }

    @Test(priority = 7)
    public void editCoupon_description() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField( p.descriptionFld() );
        setFieldVal( p.descriptionFld(), "edited description" );
        p.clickSave();
        click( p.sideMenu("Coupons") );
        p.search(couponId);
        System.out.println("Coupon name: <" + couponName + ">");
        click( p.coupon(couponName) );

        p.descriptionFld().shouldHave(text("edited description")
                .because("Failed to edit 'Description'."));

    }

    @Test(priority = 8)
    public void editCoupon_details() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField( p.detailsFld() );
        setFieldVal( p.detailsFld(), "edited details" );
        p.clickSave();
        click( p.sideMenu("Coupons") );
        p.search(couponId);
        click( p.coupon(couponName) );

        p.detailsFld().shouldHave(text("edited details")
                .because("Failed to edit 'Details'."));

    }

    @Test(priority = 9)
    public void editCoupon_state() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        p.setState("Inactive");
        p.clickSave();
        click( p.sideMenu("Coupons") );
        p.search(couponId);

        p.getCouponParamVal("1", "State").shouldHave(text("Inactive")
                .because("Failed to edit 'State'."));

    }

    @Test(priority = 10)
    public void totalUses() throws IOException {
        // will fail due to bug with applying coupon to order
        provideTestData("order in remorse hold with applied coupon");
        p = open(adminUrl + "/coupons/", CouponsPage.class);
        p.search(couponId);
        p.getCouponParamVal("1", "Total Uses").shouldHave(text("1")
                .because("Checking out the order with coupon applied isn't reflected on the coupons list (Incorrect 'Total Uses' val)."));

    }

    @Test(priority = 11)
    public void currentCarts() throws IOException {

        provideTestData("a cart with a single code coupon applied");
        p = open(adminUrl + "/coupons/", CouponsPage.class);
        p.search(couponId);
        p.getCouponParamVal("1", "Current Carts").shouldHave(text("1")
                .because("Applying coupon to a cart isn't reflected on the coupons list (Incorrect 'Current Carts' val)."));

    }

}
