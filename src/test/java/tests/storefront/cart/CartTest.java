package tests.storefront.cart;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.StorefrontTPGBasePage;
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

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        clearCache();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Cart : General Behavior")
    @Description("Cart is blanked after log out")
    public void cartBlankedOnLogOut() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openProfile();
        p.logOut();

        p.logInLnk().shouldBe(visible);
        p.cartQty().shouldHave(text("0"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(0);
    }

}
