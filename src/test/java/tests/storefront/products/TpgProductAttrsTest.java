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

public class TpgProductAttrsTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        clearCache();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Products : TPGs Custom PDP")
    @Description("All attributes are displayed correctly (product name, sale price, description)")
    public void productAttributesDisplayedCorrectly() throws IOException {
        provideTestData("an active product with tpg-specific custom properties");
        p = openPage(storefrontUrl + "/products/" + productSlug, StorefrontTPGBasePage.class);

        //Prep
        p.switchInfoBlock("Prep");
        assertEquals_soft(p.customAttributeVal("Conventional Oven"), "Conventional Oven Value");
        assertEquals_soft(p.customAttributeVal("Microwave"), "Microwave Value");
        assertEquals_soft(p.customAttributeVal("Pan Fry"), "Pan Fry Value");
        assertEquals_soft(p.customAttributeVal("Steam"), "Steam Value");
        assertEquals_soft(p.customAttributeVal("Grill"), "Grill Value");
        assertEquals_soft(p.customAttributeVal("Defrost"), "Defrost Value");

        //Ingredients
        p.switchInfoBlock("Ingredients");
        assertEquals_soft(p.customAttributeVal("Ingredients"), "Ingredients Value");
        assertEquals_soft(p.customAttributeVal("Allergy Alerts"), "Allergy Alerts Value");

        //Nutrition
        p.switchInfoBlock("Nutrition");
        assertEquals_soft(p.customAttributeVal("Nutritional Information"), "Nutritional Information Value");

        soft.assertAll();
    }

}