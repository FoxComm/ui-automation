package tests.storefront.products;

import org.testng.annotations.AfterMethod;
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
import static com.codeborne.selenide.Selenide.sleep;

public class ProductsBehaviorTest extends Preconditions {

    private StorefrontPage p;

    @Test(priority = 1, dataProvider = "productCatalogViewDisplayed", enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products Behavior on Storefront")
    @Description("Product should be displayed in the catalog view on storefront [Disabled until DB clean]")
    public void productDisplayedInCatalogView(String testData) throws IOException {
        provideTestData(testData);
        waitForProductAppearInEs("int", "productId", productId);

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);

        p.productTitle_catalog(productTitle).shouldBe(visible);
    }

    @Test(priority = 2, dataProvider = "productCatalogViewNotDisplayed", enabled = false)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products Behavior on Storefront")
    @Description("Product should not be displayed in the catalog view on storefront [Disabled until DB clean]")
    public void productNotDisplayedInCatalogView(String testData) throws IOException {
        provideTestData(testData);
        sleep(3000);
        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);

        p.productTitle_catalog(productTitle).shouldNotBe(visible);
    }

    @Test(priority = 3, dataProvider = "productCanBeSearched")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products Behavior on Storefront")
    @Description("Product can be found by search on storefront")
    public void productCanBeSearched(String testData) throws IOException {
        provideTestData(testData);

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickSearchIcon();
        p.submitSearchQuery(productTitle);

        p.productTitle_catalog(productTitle).shouldBe(visible);
    }

    @Test(priority = 4, dataProvider = "productCannotBeSearched")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products Behavior on Storefront")
    @Description("Product cannot be found in search on storefront")
    public void productCannotBeSearched(String testData) throws IOException {
        provideTestData(testData);
        sleep(3000);

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickSearchIcon();
        p.submitSearchQuery(productTitle);

        p.productTitle_catalog(productTitle).shouldNotBe(visible);
    }

    @Test(priority = 5, dataProvider = "canAccessPDP")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products Behavior on Storefront")
    @Description("PDP can be accessed using direct link")
    public void canAccessPDP(String testData) throws IOException {
        provideTestData(testData);

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);
        p.productTitle_pdp().shouldHave(text(productTitle));

        p.salePrice().shouldHave(text("$50.00"));
    }

    @Test(priority = 6, dataProvider = "cannotAccessPDP")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products Behavior on Storefront")
    @Description("PDP can't be accessed using direct link - 'NO PRODUCT FOUND' should be displayed")
    public void cannotAccessPDP(String testData) throws IOException {
        provideTestData(testData);
        sleep(3000);

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);
        p.notFoundMsg("Product not found").shouldBe(visible);
    }

    //    TODO: hangs Xvfb in BK

//    @Test(priority = 7, dataProvider = "productCanPassCheckout")
//    @Severity(SeverityLevel.CRITICAL)
//    @Features("Storefront-TPG")
//    @Stories("Products Behavior on Storefront")
//    @Description("Cart with this product can pass checkout")
//    public void productCanPassCheckout(String testData) throws IOException {
//        provideTestData(testData);
//        String randomId = generateRandomID();
//        signUpCustomer("Customer " + randomId, "qatest2278+" + randomId + "@gmail.com");
//
//        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
//        p.logIn(customerEmail, "78qa22!#");
//        openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);
//        p.clickAddToCartBtn();
//        p.clickCheckoutBtn_cart();
//        p.clickContinueBtn();
//        p.clickPlaceOrderBtn();
//
//        p.confirmationOrderNumber().shouldBe(visible);
//    }

    //    TODO: hangs Xvfb in BK

//    @Test(priority = 8, dataProvider = "productCannotPassCheckout")
//    @Severity(SeverityLevel.CRITICAL)
//    @Features("Storefront-TPG")
//    @Stories("Products Behavior on Storefront")
//    @Description("Cart with this product can't pass checkout")
//    public void productCannotPassCheckout(String testData) throws IOException {
//        provideTestData(testData);
//        String randomId = generateRandomID();
//        signUpCustomer("Customer " + randomId, "qatest2278+" + randomId + "@gmail.com");
//
//        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
//        p.logIn(customerEmail, "78qa22!#");
//        p.clickAddToCartBtn();
//        p.clickCheckoutBtn_cart();
//        p.clickContinueBtn();
//        p.clickPlaceOrderBtn();
//
//        p.errorMsg("Oops!").shouldBe(visible);
//    }

    @Test(priority = 9, dataProvider = "canAddProductToCart_storefront")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products Behavior on Storefront")
    @Description("Can add product to cart on storefront")
    public void canAddProductToCart_storefront(String testData) throws IOException {
        provideTestData(testData);

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);
        p.clickAddToCartBtn();

        p.lineItemByName_cart(productTitle).shouldBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp();
    }

}
