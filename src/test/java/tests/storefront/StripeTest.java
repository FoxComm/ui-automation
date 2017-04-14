package tests.storefront;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static testdata.api.collection.Customers.signUpCustomer;

public class StripeTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1, dataProvider = "stripeTest")
    @Description("Checkout the cart and then check Payment Status at Stripe dashboard")
    public void stripeTest(String testData, String expPaymentState) throws IOException {
        String randomId = generateRandomID();
        signUpCustomer("Customer " + randomId, "qatest2278+" + expPaymentState + "." + randomId);
        provideTestData(testData);
        printTestData();
    }


    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
