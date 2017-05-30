package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.storefront.TempSfPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class SfBeforeAfterStepsTest extends Preconditions {

    private TempSfPage p;

    @Test(priority = 1)
    @Features("Before/After Tests")
    @Stories("Before")
    public void beforeMethod() {
        p = openPage(storefrontUrl, TempSfPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 2)
    @Features("Storefront-TPG")
    @Stories("Cart : Line Items")
    @Description("Cart is blanked after log out")
    public void cartBlankedOnLogOut() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, TempSfPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.selectInUserMenu("LOG OUT");

        p.logInLnk().shouldBe(visible);
        p.cartQty().shouldHave(text("0"));
        p.openCart();
        p.lineItemsAmount().shouldHaveSize(0);
    }

    @Test(priority = 3)
    @Features("Before/After Tests")
    @Stories("After")
    public void afterMethod() {
        p.cleanUp_afterMethod();
    }

    @Test(priority = 4)
    @Features("Before/After Tests")
    @Stories("Before")
    public void beforeMethod1() {
        p = openPage(storefrontUrl, TempSfPage.class);
        p.cleanUp_beforeMethod();
    }
//
//    @Test(priority = 5)
//    @Severity(SeverityLevel.CRITICAL)
//    @Features("Storefront-TPG")
//    @Stories("Cart : Line Items")
//    @Description("Line items units indicator shows correct amount of total line items units in cart")
//    public void addProductToCart_indicatorUpdated() throws IOException {
//        provideTestData("registered customer, 2 active products on storefront");
//
//        p = openPage(storefrontUrl+"/products/"+productSlugs.get(0), TempSfPage.class);
//        p.logIn(customerEmail, "78qa22!#");
//        p.setQty_pdp("2");
//        p.clickAddToCartBtn();
//        p.closeCart();
//        p = openPage(storefrontUrl+"/products/"+productSlugs.get(1), TempSfPage.class);
//        p.setQty_pdp("3");
//        p.clickAddToCartBtn();
//        p.closeCart();
//        p.cartQty().shouldHave(text("5"));
//
//        p.openCart();
//        p.removeLineItem("1");
//        p.closeCart();
//        p.cartQty().shouldNotHave(text("5"));
//    }

    @Test(priority = 6)
    @Features("Before/After Tests")
    @Stories("After")
    public void afterMethod1() {
        p.cleanUp_afterMethod();
    }

    @Test(priority = 9, dataProvider = "canAddProductToCart_storefront")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products Behavior on Storefront")
    @Description("Can add product to cart on storefront")
    public void canAddProductToCart_storefront(String testData) throws IOException {
        provideTestData(testData);

        p = openPage(storefrontUrl + "/products/" + productSlug, TempSfPage.class);
        p.clickAddToCartBtn();

        p.lineItemByName_cart(productTitle).shouldBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
