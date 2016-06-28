package tests.orderstests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.open;

public class CouponsTest extends DataProvider {

    private OrderDetailsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open("http://admin.stage.foxcommerce.com/");
        if ( (Objects.equals(getUrl(), "http://admin.stage.foxcommerce.com/login")) ) {
            LoginPage loginPage = open("http://admin.stage.foxcommerce.com/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }

    }

    @Test(priority = 1)
    public void addCoupon() throws IOException {

        provideTestData("a cart and a single code coupon");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

//        click( p.editBtn_coupons() );
//        setFieldVal( p.couponCode(), "newcpn-12345" );
//        click( p.applyBtn() );
//        click(  p.doneBtn_coupons );

        // assert that coupon is displayed in 'Coupons' block
        // assert that coupon is displayed in 'Discounts' block

    }

    @Test(priority = 2)
    public void addCoupon_checkTotal() throws IOException {

        // add coupon to order through the API
        provideTestData("cart with single code coupon");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

        //assert that coupon affects order's grand total

    }

    @Test(priority = 2)
    public void removeCoupon() throws IOException {

        // add coupon to order through the API
        provideTestData("cart with single code coupon");
        p = open("http://admin.stage.foxcommerce.com/orders/" + orderId, OrderDetailsPage.class);

//        click( p.editBtn_coupons() );
//        click( p.deleteBtn_coupons() );
//        click( p.doneBtn_coupons() );

        // assert order's Grand Total
        // assert that 'Discounts' is cleared

    }


}
