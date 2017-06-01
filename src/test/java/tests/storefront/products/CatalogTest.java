package tests.storefront.products;

import org.testng.annotations.*;
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
import static testdata.api.collection.Products.editProductSlug;

public class CatalogTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_after() {
        restartBrowser();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("A just created active product with at least 1 active SKU can be found in respective category (defined as a tag)")
    public void productDisplayedInCatalogView() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.navigateToCategory(storefrontCategory);

        p.productTitle_catalog(productTitle).shouldBe(visible);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("Can add product to cart from catalog without opening PDP")
    public void addProductFromCatalogViewPage() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontTPGBasePage.class);
        p.navigateToCategory(storefrontCategory);
        scrollToElement(p.productTitle_catalog(productTitle));
        p.clickAddToCartBtn_catalog(productTitle);

        p.lineItemsAmount().shouldHaveSize(1);
        p.lineItemByName_cart(productTitle).shouldBe(visible);
        p.closeCart();
        p.cartQty().shouldHave(text("1"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("Product description appears on the product image on hover")
    public void hoverProductImageInCatalog_hasImage() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontTPGBasePage.class);
        scrollToElement(p.productImage(productTitle));
        p.productImage(productTitle).hover();

        p.additionalDescription(productTitle).shouldBe(visible);
        p.additionalDescription(productTitle).shouldHave(text("The best thing to buy in 2016!"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("Product description appears on the product image on hover [Disabled until DB clean]")
    public void hoverProductImageInCatalog_noImage() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontTPGBasePage.class);
        scrollToElement(p.imagePlaceholder(productTitle));
        p.imagePlaceholder(productTitle).hover();

        p.additionalDescription(productTitle).shouldBe(visible);
        p.additionalDescription(productTitle).shouldHave(text("The best thing to buy in 2016!"));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("Products with \"ENTRÉES\" category sub-category names tags are displayed in corresponding sub-categories")
    public void entreesSubCategories() throws IOException {
        provideTestData("products with tags with entrees subcategories names");

        p = openPage(storefrontUrl + "/ENTRÉES", StorefrontTPGBasePage.class);
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
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("Products with \"ENTRÉES\" category sub-category names tags are displayed in corresponding sub-categories")
    public void entreesSubCategories(String subCategory) throws IOException {
        provideTestData("active product with tags <ENTRÉES> and <" + subCategory + ">");
        p = openPage(storefrontUrl + "/ENTRÉES/" + subCategory, StorefrontTPGBasePage.class);
        p.productTitle_catalog(productTitle).shouldBe(visible);
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.BLOCKER)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("Can access PDP")
    public void canAccessPdp() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontTPGBasePage.class);
        p.openPDP(productTitle);

        p.description_pdp().shouldBe(visible);
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("Can access PDP via new product slug")
    public void canAccessPdpViaNewSlug() throws IOException {
        randomId = generateRandomID();
        provideTestData("an active product visible on storefront");

        editProductSlug(productId, "new-slug-" + randomId);
        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontTPGBasePage.class);

        p.productTitle_pdp().shouldHave(text(productTitle));
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Product Catalog")
    @Description("Can't access PDP via old product slug")
    public void cantAccessPdpViaOldSlug() throws IOException {
        randomId = generateRandomID();
        provideTestData("an active product visible on storefront");

        String oldSlug = productSlug;
        editProductSlug(productId, "new-slug-" + randomId);
        p = openPage(storefrontUrl + "/products/" + oldSlug, StorefrontTPGBasePage.class);

        p.notFoundMsg("Product not found").shouldBe(visible);
    }

}
