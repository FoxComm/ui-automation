package tests.coupons;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CouponsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CouponsTest extends DataProvider {

    private CouponsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
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
        click( p.couponsNavMenu() );
        p.waitForDataToLoad();

        assertEquals( p.getCouponParamVal("1", "Name"), "test coupon " + randomId,
                "A just created coupon isn't displayed on the list.");

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
        assertTrue( !p.couponIdVal().equals("new"),
                "Failed to create a new coupon.");

        click( p.saveBtn() );
        click( p.couponsNavMenu() );
        p.waitForDataToLoad();
        assertEquals( p.getCouponParamVal("1", "Name"), "test coupon " + randomId,
                "A just created coupon isn't displayed on the list.");

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
        click( p.saveBtn() );
        sleep(5000);
        click( p.couponsNavMenu() );
        p.waitForDataToLoad();

        assertEquals( p.getCouponParamVal("1", "Name"), "edited coupon " + randomId,
                "Failed to edit coupon's 'Name'.");

    }

    @Test(priority = 6)
    public void editCoupon_storefrontName() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField( p.storefrontNameFld() );
        setFieldVal( p.storefrontNameFld(), "edited SF name" );
        click( p.saveBtn() );
        sleep(5000);
        click( p.couponsNavMenu() );
        p.waitForDataToLoad();

        assertEquals( p.getCouponParamVal("1", "Storefront Name"), "<p>edited SF name</p>",
                "Failed to edit 'Storefront Name'.");

    }

    @Test(priority = 7)
    public void editCoupon_description() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField( p.descriptionFld() );
        setFieldVal( p.descriptionFld(), "edited description" );
        click( p.saveBtn() );
        click( p.couponsNavMenu() );
        p.waitForDataToLoad();
        p.openCoupon(couponName);

        assertEquals( p.descriptionFld().getText(), "edited description",
                "Failed to edit 'Description'.");

    }

    @Test(priority = 8)
    public void editCoupon_details() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField( p.detailsFld() );
        setFieldVal( p.detailsFld(), "edited details" );
        click( p.saveBtn() );
        click( p.couponsNavMenu() );
        p.waitForDataToLoad();
        p.openCoupon(couponName);

        assertEquals( p.detailsFld().getText(), "edited details",
                "Failed to edit 'Details'.");

    }

    @Test(priority = 9)
    public void editCoupon_state() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        p.setState("Inactive");
        click( p.saveBtn() );
        sleep(5000);
        click( p.couponsNavMenu() );
        p.waitForDataToLoad();

        assertEquals( p.getCouponParamVal("1", "State"), "Inactive",
                "Failed to edit 'State'.");

    }

    @Test(priority = 10)
    public void totalUses() throws IOException {

        provideTestData("order in remorse hold with applied coupon");
        p = open(adminUrl + "/coupons/", CouponsPage.class);

        assertEquals( p.getCouponParamVal("1", "Total Uses"), "1",
                "Checking out the order with coupon applied isn't reflected on the coupons list (Incorrect 'Total Uses' val)." );

    }

    @Test(priority = 11)
    public void currentCarts() throws IOException {

        provideTestData("a cart with a single code coupon applied");
        p = open(adminUrl + "/coupons/", CouponsPage.class);

        assertEquals( p.getCouponParamVal("1", "Current Carts"), "1",
                "Applying coupon to a cart isn't reflected on the coupons list (Incorrect 'Current Carts' val).");

    }

}
