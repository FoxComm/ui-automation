package tests.storefront.products;

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

public class PdpTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        clearCache();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("PDP")
    @Description("Product title is displayed")
    public void productTitleDisplayed() throws IOException {
        provideTestData("active product, has tag, active SKU, has sellable stockitems");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontTPGBasePage.class);

        p.productTitle_pdp().shouldBe(visible);
        p.productTitle_pdp().shouldHave(text(productTitle));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("PDP")
    @Description("Product sale price is displayed correctly")
    public void productPriceDisplayed() throws IOException {
        provideTestData("active product, has tag, active SKU, has sellable stockitems");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontTPGBasePage.class);

        p.salePrice().shouldBe(visible);
        p.salePrice().shouldHave(text("$50.00"));
    }

    @Test(priority = 3, dataProvider = "styledText")
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("PDP")
    @Description("Description at PDP: Check if styled text is displayed correctly")
    public void styledDescription(String element, String content) throws IOException {
        provideTestData("an active product with <" + element + "> in description");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontTPGBasePage.class);

        p.description_textStyles(element, content).shouldBe(visible);
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("PDP")
    @Description("Can change product qty on PDP")
    public void changeProductQty() throws IOException {
        provideTestData("active product, has tag, active SKU, has sellable stockitems");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontTPGBasePage.class);
        p.setQty_pdp("3");
        p.clickAddToCartBtn();
        p.closeCart();
        p.cartQty().shouldHave(text("3"));
        p.setQty_pdp("1");
        p.clickAddToCartBtn();
        p.lineItemsAmount().shouldHaveSize(1);
        p.closeCart();
        p.cartQty().shouldHave(text("4"));
    }

}