package tests.storefront.profile;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.StorefrontTPGBasePage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

public class MyOrderDetailsTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.restartBrowser();
    }

    @Test(priority = 1)
    @Description("Messing around")
    public void testTest() throws IOException {

    }

}
