package tests.storefront.cart;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import pages.StorefrontPage;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;

public class CartTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
