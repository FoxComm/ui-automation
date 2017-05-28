package tests.storefront.cart;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class CartTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart Behavior")
    @Description("Cart is blanked after log out")
    public void cartBlankedOnLogOut() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.selectInUserMenu("LOG OUT");

        p.logInLnk().shouldBe(visible);
        p.cartQty().shouldHave(text("0"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(0);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
