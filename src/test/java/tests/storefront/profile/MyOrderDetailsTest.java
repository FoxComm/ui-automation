package tests.storefront.profile;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

public class MyOrderDetailsTest extends Preconditions {

    private StorefrontPage p;

    @Test(priority = 1)
    @Description("Messing around")
    public void testTest() throws IOException {

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp();
    }

}
