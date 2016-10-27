package tests.cart;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class OrderCouponsTest extends DataProvider {

    private CartPage p = openPage(adminUrl + "/orders/" + orderId, CartPage.class);

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }

    }

//    @Test(priority = 1)
//    public void addCoupon() throws IOException {
//
//        provideTestData("a cart and a single code coupon");
//        p = openPage(adminUrl + "/orders/" + orderId, CartPage.class);
//
//        click( p.editBtn_coupons() );
//        setFieldVal( p.couponCode(), "newcpn-12345" );
//        click( p.applyBtn() );
//        click(  p.doneBtn_coupons );
//
//        assertTrue( p. )
//        // assert that coupon is displayed in 'Discounts' block
//
//    }
//
//    @Test(priority = 2)
//    public void addCoupon_checkTotal() throws IOException {
//
//        // add coupon to order through the API
//        provideTestData("cart with single code coupon");
//        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
//
//        //assert that coupon affects order's grand total
//
//    }
//
//    @Test(priority = 2)
//    public void removeCoupon() throws IOException {
//
//        // add coupon to order through the API
//        provideTestData("cart with single code coupon");
//        p = open(adminUrl + "/orders/" + orderId, OrderDetailsPage.class);
//
////        click( p.editBtn_coupons() );
////        click( p.deleteBtn_coupons() );
////        click( p.doneBtn_coupons() );
//
//        // assert order's Grand Total
//        // assert that 'Discounts' is cleared
//
//    }


}
