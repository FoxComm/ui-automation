package tests.storeadmin.skus;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.SkusPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class SkusTests extends Preconditions {

    private SkusPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can create SKU")
    public void createSKU() {
        p = openPage(adminUrl + "/skus", SkusPage.class);
        String randomId = generateRandomID();
        p.clickAddNewSKU();
        p.createNewSKU(randomId, "Active");
        p.clickSave_wait();
        shouldHaveText(p.skuCodeVal(), "SKU-" + randomId, "Failed to create new SKU");
        p.navigateTo("SKUs");
        p.search(randomId);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getSKUParamVal("1", "Code").shouldHave(text("SKU-" + randomId));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can add customer property fld")
    public void addCustomProp_text() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.addCustomProp("Text", "text fld");
        p.setCustomTextVal("text fld", "test val");
        p.clickSave_wait();
        refresh();

        p.customText("text fld").shouldHave(value("test val"));
    }

    // NOT FIXED
    @Test(priority = 3)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can add customer rich text")
    public void addCustomProp_richText() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.addCustomProp("Rich Text", "richtextfld");
        p.setCustomRichTextVal("richtextfld", "test val");
        p.clickSave_wait();
        refresh();

        p.customRichTextVal("richtextfld").shouldHave(text("test val"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can edit SKU's \"Title\" -- changes are visible on the list in skus_search_view")
    public void editTitle_searchView() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.setTitle("Edited SKU Title");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search( skuCode.substring(4, skuCode.length()) );
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getSKUParamVal("1", "Title").shouldHave(text("Edited SKU Title"));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can edit SKU's \"Title\" -- changes are visible on SKU details page")
    public void editTitle_PDP() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.setTitle("Edited SKU Title");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search( skuCode.substring(4, skuCode.length()) );
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        shouldHaveText(p.getSKUParamVal("1", "Title"), "Edited SKU Title", "SKU title isn't updated on the list.");
        p.openSKU(skuCode);

        p.titleFld().shouldHave(value("Edited SKU Title"));
    }

    @Test(priority = 6)
    @Severity(SeverityLevel.MINOR)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can \"UPC\"")
    public void editUPC() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.setUpc("Edited UPC");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(skuCode.substring(4, skuCode.length()));
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.openSKU(skuCode);

        p.upcFld().shouldHave(value("Edited UPC"));
    }

    @Test(priority = 7)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can edit \"Description\"")
    public void editDescription() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        clearField( p.descriptionFld() );
        p.setDescription("Edited description");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(skuCode.substring(4, skuCode.length()));
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.openSKU(skuCode);

        p.descriptionFld().shouldHave(text("Edited description"));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can edit \"Retail Price\" -- changes are visible in skus_search_view")
    public void editRetailPrice_searchView() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.setRetailPrice("70.00");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(skuCode.substring(4, skuCode.length()));
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getSKUParamVal("1", "Retail Price").shouldHave(text("70.00"));
    }

    @Test(priority = 9)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can edit \"Retail Price\" -- changes are visible on SKU details page")
    public void editRetailPrice_PDP() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.setRetailPrice("70.00");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(skuCode.substring(4, skuCode.length()));
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.getSKUParamVal("1", "Retail Price").shouldHave(text("70.00"));
        p.openSKU(skuCode);

        p.retailPriceFld().shouldHave(value("70.00"));
    }

    @Test(priority = 10)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can edit \"Retail Price\" changes are visible in skus_search_view")
    public void editSalePrice_searchView() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.setSalePrice("70.00");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(skuCode.substring(4, skuCode.length()));
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getSKUParamVal("1", "Sale Price").shouldHave(text("70.00"));
    }

    @Test(priority = 11)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can edit \"Sale Price\" -- changes are visible on SKU details page")
    public void editSalePrice_PDP() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.setSalePrice("70.00");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(skuCode.substring(4, skuCode.length()));
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        shouldHaveText(p.getSKUParamVal("1", "Sale Price"), "70.00", "Sale price isn't updated on the list.");
        p.openSKU(skuCode);

        p.salePriceFld().shouldHave(value("70.00"));
    }

    @Test(priority = 12)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can edit \"State\" -- changes are visible on SKU details page")
    public void editState_PDP() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.setState("Inactive");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(skuCode.substring(4, skuCode.length()));
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.openSKU(skuCode);

        p.stateVal().shouldHave(text("Inactive"));
    }

    //--------------------------------- REGRESSION TESTS ---------------------------------//

    @Test(priority = 13)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Title isn't blanked after creating a product with a pre-created SKU")
    public void titleReset() throws IOException {
        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);
        p.titleFld().shouldHave(value(skuTitle));
    }

    @Test(priority = 14)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can't use skuCode of an existing SKU to create a new one")
    public void useExistingSKUCode_newSKU() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus", SkusPage.class);
        p.clickAddNewSKU();
        p.setSKUCode(skuCode);
        p.clickSave();

        p.errorMsg("already exists").shouldBe(visible);
    }

    @Test(priority = 15)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("SKUs")
    @Description("Can't use skuCode of an existing SKU as a new skuCode for another SKU")
    public void useExistingSKUCode_editSKU() throws IOException {
        provideTestData("active SKU");
        String skuToEdit = skuCode;
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus", SkusPage.class);
        p.addFilter("SKU : Code", skuCode);
        shouldBeVisible(contentOnList(), "Search request returned no results.");
        p.openSKU(skuToEdit);
        p.setSKUCode(skuCode);
        p.clickSave();

        p.errorMsg("already exists").shouldBe(visible);
    }
}