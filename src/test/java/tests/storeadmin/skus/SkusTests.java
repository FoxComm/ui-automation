package tests.storeadmin.skus;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.SkusPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;

public class SkusTests extends DataProvider {

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
    public void createSKU() {
        p = openPage(adminUrl + "/skus", SkusPage.class);
        String randomId = generateRandomID();
        p.clickAddNewSKU();
        p.createNewSKU(randomId, "Active");
        p.clickSave_wait();
        shouldHaveText(p.skuCodeVal(), "SKU-" + randomId, "Failed to create new SKU");
        p.navigateTo("SKUs");
        p.search(randomId);

        p.getSKUParamVal("1", "Code").shouldHave(text("SKU-" + randomId));
    }

    @Test(priority = 2)
    public void addCustomProp_text() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.addCustomProp("Text", "text fld");
        p.setCustomProp_text("text fld", "test val");
        p.clickSave_wait();
        refresh();

        p.customTextFld("text fld").shouldHave(value("test val"));
    }

    // NOT FIXED
    @Test(priority = 3)
    public void addCustomProp_richText() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.addCustomProp("Rich Text", "richtextfld");
        p.setCustomProp_richText("richtextfld", "test val");
        p.clickSave_wait();
        refresh();

        p.customRichTextFldVal("richtextfld").shouldHave(text("test val"));
    }

    @Test(priority = 4)
    public void editTitle_categoryView() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.setTitle("Edited SKU Title");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search( sku.substring(4, sku.length()) );

        p.getSKUParamVal("1", "Title").shouldHave(text("Edited SKU Title"));
    }

    @Test(priority = 5)
    public void editTitle_PDP() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.setTitle("Edited SKU Title");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search( sku.substring(4, sku.length()) );
        shouldHaveText(p.getSKUParamVal("1", "Title"), "Edited SKU Title", "SKU title isn't updated on the list.");
        p.openSKU(sku);

        p.titleFld().shouldHave(value("Edited SKU Title"));
    }

    @Test(priority = 6)
    public void editUPC() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.setUpc("Edited UPC");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(sku.substring(4, sku.length()));
        p.openSKU(sku);

        p.upcFld().shouldHave(value("Edited UPC"));
    }

    @Test(priority = 7)
    public void editDescription() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        clearField( p.descriptionFld() );
        p.setDescription("Edited description");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(sku.substring(4, sku.length()));
        p.openSKU(sku);

        p.descriptionFld().shouldHave(text("Edited description"));
    }

    @Test(priority = 8)
    public void editRetailPrice_categoryView() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.setRetailPrice("70.00");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(sku.substring(4, sku.length()));

        p.getSKUParamVal("1", "Retail Price").shouldHave(text("70.00"));
    }

    @Test(priority = 9)
    public void editRetailPrice_PDP() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.setRetailPrice("70.00");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(sku.substring(4, sku.length()));
        p.getSKUParamVal("1", "Retail Price").shouldHave(text("70.00"));
        p.openSKU(sku);

        p.retailPriceFld().shouldHave(value("70.00"));
    }

    @Test(priority = 10)
    public void editSalePrice_categoryView() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.setSalePrice("70.00");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(sku.substring(4, sku.length()));

        p.getSKUParamVal("1", "Sale Price").shouldHave(text("70.00"));
    }

    @Test(priority = 11)
    public void editSalePrice_PDP() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.setSalePrice("70.00");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(sku.substring(4, sku.length()));
        shouldHaveText(p.getSKUParamVal("1", "Sale Price"), "70.00", "Sale price isn't updated on the list.");
        p.openSKU(sku);

        p.salePriceFld().shouldHave(value("70.00"));
    }

    @Test(priority = 12)
    public void editState_PDP() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.setState("Inactive");
        p.clickSave_wait();
        p.navigateTo("SKUs");
        p.search(sku.substring(4, sku.length()));
        p.openSKU(sku);

        p.stateVal().shouldHave(text("Inactive"));
    }

    //--------------------------------- REGRESSION TESTS ---------------------------------//

    @Test(priority = 13)
    @Description("Title isn't blanked after creating a product with a pre-created SKU")
    public void titleReset() throws IOException {
        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);
        p.titleFld().shouldHave(value(skuTitle));
    }

    @Test(priority = 14)
    @Description("Can't use skuCode of an existing SKU to create a new one")
    public void useExistingSKUCode_newSKU() throws IOException {
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus", SkusPage.class);
        p.clickAddNewSKU();
        p.setSKUCode(sku);
        p.clickSave();

        p.errorMsg("already exists").shouldBe(visible);
    }

    @Test(priority = 15)
    @Description("Can't use skuCode of an existing SKU as a new skuCode for another SKU")
    public void useExistingSKUCode_editSKU() throws IOException {
        provideTestData("active SKU");
        String skuToEdit = sku;
        provideTestData("active SKU");

        p = openPage(adminUrl + "/skus", SkusPage.class);
        p.search(skuToEdit);
        p.openSKU(skuToEdit);
        p.setSKUCode(sku);
        p.clickSave();

        p.errorMsg("already exists").shouldBe(visible);
    }
}