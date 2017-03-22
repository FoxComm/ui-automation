package tests.storefront.products;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

public class AbstractPdpTest extends DataProvider {

    private StorefrontPage p;

    @org.testng.annotations.DataProvider
    public Object[][] styledText() {
        return new Object[][] {
                {"p", "Paragraph"},
                {"h1", "Heading One"},
                {"h2", "Heading Two"},
                {"h3", "Heading Three"},
                {"h4", "Heading Four"},
                {"h5", "Heading Five"},
                {"h6", "Heading Six"},
                {"strong", "Bold Text"},
                {"em", "Italic Text"},
                {"ins", "Underlined Text"},
                {"ul", "UL Bullet Point"},
                {"ol", "OL Point"},
                {"ul", "UL Bullet Point"}
        };
    }

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Product title is displayed")
    public void productTitleDisplayed() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);

        p.productTitle_pdp().shouldBe(visible);
        p.productTitle_pdp().shouldHave(text(productName));
    }

    @Test(priority = 2)
    @Description("Product sale price is displayed correctly")
    public void productPriceDisplayed() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);

        p.salePrice().shouldBe(visible);
        p.salePrice().shouldHave(text("$50.00"));
    }

    @Test(priority = 3, dataProvider = "styledText")
    @Description("Description at PDP: Check if styled text is displayed correctly")
    public void styledDescription(String element, String content) throws IOException {
        provideTestData("an active product with <" + element + "> in description");

        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);

        p.description_textStyles(element, content).shouldBe(visible);
    }

    @Test(priority = 4)
    @Description("Can change product qty on PDP")
    public void changeProductQty() throws IOException {
        provideTestData("an active product visible on storefront");

        p = openPage(storefrontUrl + storefrontCategory, StorefrontPage.class);
        p.openPDP(productName);
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

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}