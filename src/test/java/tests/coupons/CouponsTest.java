package tests.coupons;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CouponsPage;
import pages.LoginPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;

public class CouponsTest extends DataProvider {

    private CouponsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    @Test(priority = 1)
    public void createCoupon_singleCode_active() throws IOException {

        provideTestData("a promotion");
        p = open(adminUrl + "/coupons/", CouponsPage.class);
        String randomId = generateRandomID();

        p.clickAddNewCouponBtn();
        p.setCouponName("test coupon " + randomId);
        p.setStorefrontName("that's a storefront name");
        p.setDescription("that's nothing but another test coupon");
        p.setDetails("get 10% off for any order");
        p.setPromotion(promotionId);
        p.generateCodes_single("NWCPN-" + randomId);
        p.clickSave();
        p.navigateTo("Coupons");
        p.waitForDataToLoad();

        p.search("test coupon " + randomId);
        p.getCouponParamVal("1", "Name").shouldHave(text("test coupon " + randomId)
                .because("A just created coupon isn't displayed on the list."));

    }

    @Test(priority = 2)
    public void createCoupon_bulkCodes_active() throws IOException {

        provideTestData("a promotion");
        p = open(adminUrl + "/coupons/", CouponsPage.class);
        String randomId = generateRandomID();

        p.clickAddNewCouponBtn();
        p.setCouponName("test coupon " + randomId);
        p.setStorefrontName("that's a storefront name");
        p.setDescription("that's nothing but another test coupon");
        p.setDetails("get 10% off for any order");
        p.setPromotion(promotionId);
        p.generateCodes_bulk(4, "BULKCPN_" + randomId + "-", 5);
        p.setState("Active");
        shouldNotHaveText(p.couponIdVal(), "new", "Failed to create a new coupon");
        p.clickSave();

        p.navigateTo("Coupons");
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

        p.setCouponName("edited coupon " + randomId);
        p.clickSave();
        p.navigateTo("Coupons");
        p.search(couponId);

        p.getCouponParamVal("1", "Name").shouldHave(text("edited coupon " + randomId)
                .because("Failed to edit coupon's 'Name'."));

    }

    @Test(priority = 6)
    public void editCoupon_storefrontName() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField( p.storefrontNameFld() );
        p.setStorefrontName("edited SF name");
        p.clickSave();
        p.navigateTo("Coupons");
        p.search(couponId);

        p.getCouponParamVal("1", "Storefront Name").shouldHave(text("<p>edited SF name</p>")
                .because("Failed to edit 'Storefront Name'."));

    }

    @Test(priority = 7)
    public void editCoupon_description() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField(p.descriptionFld());
        p.setDescription("edited description");
        p.clickSave();
        p.navigateTo("Coupons");
        p.search(couponId);
//        System.out.println("Coupon name: <" + couponName + ">");
        p.openCoupon(couponName);

        p.descriptionFld().shouldHave(text("edited description")
                .because("Failed to edit 'Description'."));

    }

    @Test(priority = 8)
    public void editCoupon_details() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        clearField(p.detailsFld());
        p.setDetails("edited details");
        p.clickSave();
        p.navigateTo("Coupons");
        p.search(couponId);
        p.openCoupon(couponName);

        p.detailsFld().shouldHave(text("edited details")
                .because("Failed to edit 'Details'."));

    }

    @Test(priority = 9)
    public void editCoupon_state() throws IOException {

        provideTestData("coupon with single code");
        p = open(adminUrl + "/coupons/" + couponId, CouponsPage.class);

        p.setState("Inactive");
        p.clickSave();
        p.navigateTo("Coupons");
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
