package tests.storefront.products;

import org.testng.annotations.*;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class CatalogTest extends DataProvider {

    private StorefrontPage p;

    @org.testng.annotations.DataProvider
    public Object[][] entreesSubcategories() {
        return new Object[][]{ {"POULTRY"}, {"SEAFOOD"}, {"MEAT"}, {"VEGETARIAN"} };
    }

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("A just created active product with at least 1 active SKU can be found in respective category (defined as a tag)")
    public void productDisplayedInCatalogView() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);

        p.productTitle_catalog(productName).shouldBe(visible);
    }

    @Test(priority = 2)
    @Description("Can add product to cart from catalog without opening PDP")
    public void addProductFromCatalogViewPage() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        p.navigateToCategory(storefrontCategory);
        scrollToElement(p.productTitle_catalog(productName));
        p.clickAddToCartBtn_catalog(productName);

        p.lineItemsAmount().shouldHaveSize(1);
        p.lineItemByName_cart(productName).shouldBe(visible);
        p.closeCart();
        p.cartQty().shouldHave(text("1"));
    }

    @Test(priority = 3, enabled = false)
    @Description("Product description appears on the product image on hover")
    public void hoverProductImageInCatalog_hasImage() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        scrollToElement(p.productImage(productName));
        p.productImage(productName).hover();

        p.additionalDescription(productName).shouldBe(visible);
        p.additionalDescription(productName).shouldHave(text("The best thing to buy in 2016!"));
    }

    @Test(priority = 4, enabled = false)
    @Description("Product description appears on the product image on hover")
    public void hoverProductImageInCatalog_noImage() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        scrollToElement(p.imagePlaceholder(productName));
        p.imagePlaceholder(productName).hover();

        p.additionalDescription(productName).shouldBe(visible);
        p.additionalDescription(productName).shouldHave(text("The best thing to buy in 2016!"));
    }

    @Test(priority = 5)
    @Description("Products with \"ENTRÉES\" category sub-category names tags are displayed in corresponding sub-categories")
    public void entreesSubCategories() throws IOException {
        provideTestData("products with tags with entrees subcategories names");

        p = openPage(storefrontUrl + "/ENTRÉES", StorefrontPage.class);
        p.navigateToSubCategory("All");

        p.productTitle_catalog(products.get(0)).shouldBe(visible);
        p.productTitle_catalog(products.get(1)).shouldBe(visible);
        p.productTitle_catalog(products.get(2)).shouldBe(visible);
        p.productTitle_catalog(products.get(3)).shouldBe(visible);
        p.productTitle_catalog(products.get(4)).shouldBe(visible);
        p.navigateToSubCategory("Meat");
        p.productTitle_catalog(products.get(4)).shouldNotBe(visible);
    }

    @Test(priority = 6, dataProvider = "entreesSubcategories")
    @Description("Products with \"ENTRÉES\" category sub-category names tags are displayed in corresponding sub-categories")
    public void entreesSubCategories(String subCategory) throws IOException {
        provideTestData("active product with tags <ENTRÉES> and <" + subCategory + ">");
        p = openPage(storefrontUrl + "/ENTRÉES" + "/" + subCategory, StorefrontPage.class);
        p.productTitle_catalog(productName).shouldBe(visible);
    }

    @Test(priority = 7)
    @Description("Can access PDP")
    public void canAccessPdp() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        p.openPDP(productName);

        p.description_pdp().shouldBe(visible);
    }

    @Test(priority = 8)
    @Description("Can access PDP via new product slug")
    public void canAccessPdpViaNewSlug() throws IOException {
        randomId = generateRandomID();
        provideTestData("an active product visible on storefront");

        editProductSlug(productId, "new-slug-" + randomId);
        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);

        p.productTitle_pdp().shouldHave(text(productName));
    }

    @Test(priority = 9)
    @Description("Can't access PDP via old product slug")
    public void cantAccessPdpViaOldSlug() throws IOException {
        randomId = generateRandomID();
        provideTestData("an active product visible on storefront");

        String oldSlug = productSlug;
        editProductSlug(productId, "new-slug-" + randomId);
        p = openPage(storefrontUrl + "/products/" + oldSlug, StorefrontPage.class);

        p.notFoundMsg("Product not found").shouldBe(visible);
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
