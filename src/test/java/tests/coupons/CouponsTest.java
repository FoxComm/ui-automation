package tests.coupons;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CouponsPage;
import pages.LoginPage;
import ru.yandex.qatools.allure.annotations.Description;
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
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    @Test(priority = 1)
    public void createCoupon_singleCode_active() throws IOException {
        provideTestData("a promotion");
        String randomId = generateRandomID();

        p = openPage(adminUrl + "/coupons/", CouponsPage.class);
        p.clickAddNewCouponBtn();
        p.setPromotion(promotionId);
        p.setCouponName("test coupon " + randomId);
        p.setStorefrontName("that's a storefront name");
        p.setDescription("that's nothing but another test coupon");
        p.setDetails("get 10% off for any order");
        p.generateCodes_single("NWCPN-" + randomId);
        p.clickSave_wait();
        shouldNotHaveText(p.couponIdVal(), "new", "Failed to create new coupon");

        p.navigateTo("Marketing", "Coupons");
        p.waitForDataToLoad();
        p.search("test coupon " + randomId);
        p.getCouponParamVal("1", "Name").shouldHave(text("test coupon " + randomId));
    }

    @Test(priority = 2)
    public void createCoupon_bulkCodes_active() throws IOException {
        provideTestData("a promotion");
        String randomId = generateRandomID();

        p = openPage(adminUrl + "/coupons/", CouponsPage.class);
        p.clickAddNewCouponBtn();
        p.setPromotion(promotionId);
        p.setCouponName("test coupon " + randomId);
        p.setStorefrontName("that's a storefront name");
        p.setDescription("that's nothing but another test coupon");
        p.setDetails("get 10% off for any order");
        p.generateCodes_bulk(4, "BULKCPN_" + randomId + "-", 5);
        p.setState("Active");
        p.clickSave_wait();
        shouldNotHaveText(p.couponIdVal(), "new", "Failed to create new coupon");

        p.navigateTo("Marketing", "Coupons");
        p.waitForDataToLoad();
        p.getCouponParamVal("1", "Name").shouldHave(text("test coupon " + randomId));
    }

    @Test(priority = 3)
    public void codesDisplayedInCodesTab_bulkGenerated() throws IOException {
        provideTestData("coupon with bulk generated codes");
        p = openPage(adminUrl + "/coupons/" + couponId + "/codes", CouponsPage.class);
        p.assertCodesGenerated(5);
    }

    @Test(priority = 4)
    public void codesDisplayedInCodesTab_singleCode() throws IOException {
        provideTestData("coupon with single code");
        p = openPage(adminUrl + "/coupons/" + couponId + "/codes", CouponsPage.class);
        p.assertCodesGenerated(1);
    }

    @Test(priority = 5)
    public void editCoupon_name() throws IOException {
        provideTestData("coupon with single code");
        String randomId = generateRandomID();

        p = openPage(adminUrl + "/coupons/" + couponId, CouponsPage.class);
        p.setCouponName("edited coupon " + randomId);
        p.clickSave_wait();
        p.navigateTo("Marketing", "Coupons");
        p.search("edited coupon " + randomId);

        p.getCouponParamVal("1", "Name").shouldHave(text("edited coupon " + randomId));
    }

    @Test(priority = 6)
    public void editCoupon_storefrontName() throws IOException {
        provideTestData("coupon with single code");

        p = openPage(adminUrl + "/coupons/" + couponId, CouponsPage.class);
        clearField( p.storefrontNameFld() );
        p.setStorefrontName("edited SF name");
        p.clickSave_wait();
        p.navigateTo("Marketing", "Coupons");
        p.search(couponId);

        p.getCouponParamVal("1", "Storefront Name").shouldHave(text("<p>edited SF name</p>"));
    }

    @Test(priority = 7)
    public void editCoupon_description() throws IOException {
        provideTestData("coupon with single code");

        p = openPage(adminUrl + "/coupons/" + couponId, CouponsPage.class);
        clearField(p.descriptionFld());
        p.setDescription("edited description");
        p.clickSave_wait();
        p.navigateTo("Marketing", "Coupons");
        p.search(couponId);
        p.openCoupon(couponId);

        p.descriptionFld().shouldHave(text("edited description"));
    }

    @Test(priority = 8)
    public void editCoupon_details() throws IOException {
        provideTestData("coupon with single code");

        p = openPage(adminUrl + "/coupons/" + couponId, CouponsPage.class);
        clearField(p.detailsFld());
        p.setDetails("edited details");
        p.clickSave_wait();
        p.navigateTo("Marketing", "Coupons");
        p.search(couponId);
        p.openCoupon(couponId);

        p.detailsFld().shouldHave(text("edited details"));
    }

    @Test(priority = 9)
    public void editCoupon_state() throws IOException {
        provideTestData("coupon with single code");

        p = openPage(adminUrl + "/coupons/" + couponId, CouponsPage.class);
        p.setState("Inactive");
        p.clickSave_wait();
        p.navigateTo("Marketing", "Coupons");
        p.search(couponId);

        p.getCouponParamVal("1", "State").shouldHave(text("Inactive"));
    }

    @Description("Fails due to bug with applying coupon to cart")
    @Test(priority = 10)
    public void totalUses() throws IOException {
        provideTestData("order in remorse hold with applied coupon");

        p = openPage(adminUrl + "/coupons/", CouponsPage.class);
        p.search(couponId);

        p.getCouponParamVal("1", "Total Uses").shouldHave(text("1"));
    }

    @Description("Fails due to bug with applying coupon to cart")
    @Test(priority = 11)
    public void currentCarts() throws IOException {
        provideTestData("cart<1 SKU, coupon applied>; coupon<any, single code>");

        p = openPage(adminUrl + "/coupons/", CouponsPage.class);
        p.search(couponId);

        p.getCouponParamVal("1", "Current Carts").shouldHave(text("1"));
    }
}
