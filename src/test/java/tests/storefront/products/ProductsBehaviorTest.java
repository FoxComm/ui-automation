package tests.storefront.products;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.sleep;
import static testdata.api.collection.Customers.signUpCustomer;

public class ProductsBehaviorTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1, dataProvider = "productCategoryViewDisplayed")
    @Description("Product should be displayed in the category view on storefront")
    public void productDisplayedInCategoryView(String testData) throws IOException {
        provideTestData(testData);
        waitForProductAppearInEs("int", "productId", productId);

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);

        p.productTitle_catalog(productTitle).shouldBe(visible);
    }

    @Test(priority = 2, dataProvider = "productCategoryViewNotDisplayed")
    @Description("Product should not be displayed in the category view on storefront")
    public void productNotDisplayedInCategoryView(String testData) throws IOException {
        provideTestData(testData);
        sleep(3000);
        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);

        p.productTitle_catalog(productTitle).shouldNotBe(visible);
    }

    @Test(priority = 3, dataProvider = "productCanBeSearched")
    @Description("Product can be found by search on storefront")
    public void productCanBeSearched(String testData) throws IOException {
        provideTestData(testData);

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.clickSearchIcon();
        p.submitSearchQuery(productTitle);

        p.productTitle_catalog(productTitle).shouldBe(visible);
    }

    @Test(priority = 4, dataProvider = "productCannotBeSearched")
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
    @Description("PDP can be accessed using direct link")
    public void canAccessPDP(String testData) throws IOException {
        provideTestData(testData);

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);
        p.productTitle_pdp().shouldHave(text(productTitle));

        p.salePrice().shouldHave(text("$50.00"));
    }

    @Test(priority = 6, dataProvider = "cannotAccessPDP")
    @Description("PDP can't be accessed using direct link - 'NO PRODUCT FOUND' should be displayed")
    public void cannotAccessPDP(String testData) throws IOException {
        provideTestData(testData);
        sleep(3000);

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);
        p.notFoundMsg("Product not found").shouldBe(visible);
    }

    @Test(priority = 7, dataProvider = "productCanPassCheckout")
    @Description("Cart with this product can pass checkout")
    public void productCanPassCheckout(String testData) throws IOException {
        provideTestData(testData);
        String randomId = generateRandomID();
        signUpCustomer("Customer " + randomId, "qatest2278+" + randomId + "@gmail.com");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.clickPlaceOrderBtn();

        p.confirmationOrderNumber().shouldBe(visible);
    }

    @Test(priority = 8, dataProvider = "productCannotPassCheckout")
    @Description("Cart with this product can't pass checkout")
    public void productCannotPassCheckout(String testData) throws IOException {
        provideTestData(testData);
        String randomId = generateRandomID();
        signUpCustomer("Customer " + randomId, "qatest2278+" + randomId + "@gmail.com");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.clickAddToCartBtn();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();
        p.clickPlaceOrderBtn();

        p.errorMsg("Oops!").shouldBe(visible);
    }

    // Commented out because it was causing Xvfb hang in BK
//    @Test(priority = 9, dataProvider = "canAddProductToCart_storefront")
//    @Description("Can add product to cart on storefront")
//    public void canAddProductToCart_storefront(String testData) throws IOException {
//        provideTestData(testData);
//
//        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);
//        p.clickAddToCartBtn();
//
//        p.lineItemByName_cart(productTitle).shouldBe(visible);
//    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
