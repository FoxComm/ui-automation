package tests.storefront.products;

import org.testng.annotations.*;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;

public class TpgProductAttrsTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("All attributes are displayed correctly (product name, sale price, description)")
    public void productAttributesDisplayedCorrectly() throws IOException {
        provideTestData("an active product with tpg-specific custom properties");
        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontPage.class);

        //Prep
        assertEquals_soft(p.customAttribute("Conventional Oven").text(), "Conventional Oven Value");
        assertEquals_soft(p.customAttribute("Microwave").text(), "Microwave Value");
        assertEquals_soft(p.customAttribute("Pan Fry").text(), "Pan Fry Value");
        assertEquals_soft(p.customAttribute("Steam").text(), "Steam Value");
        assertEquals_soft(p.customAttribute("Grill").text(), "Grill Value");
        assertEquals_soft(p.customAttribute("Defrost").text(), "Defrost Value");

        //Ingredients
        p.switchInfoBlock("Ingredients");
        assertEquals_soft(p.customAttribute("Ingredients").text(), "Ingredients Value");
        assertEquals_soft(p.customAttribute("Allergy Alerts").text(), "Allergy Alerts Value");

        //Nutrition
        p.switchInfoBlock("Nutrition");
        assertEquals_soft(p.customAttribute("Nutritional Information").text(), "Nutritional Information Value");

        soft.assertAll();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}