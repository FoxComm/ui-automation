package tests.skus;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SkusPage;
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
            loginPage.login("tenant", "admin@admin.com", "password");
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
        p.navigateTo("Catalog", "SKUs");

        p.search(randomId);
        p.getSKUParamVal("1", "Code").shouldHave(text("SKU-" + randomId)
                .because("A just created SKU isn't displayed on the list."));

    }

    @Test(priority = 2)
    public void addCustomProp_text() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);

        p.addCustomProp("Text", "text fld");
        p.setCustomProp_text("text fld", "test val");
        p.clickSave_wait();
        refresh();

        p.customTextFld("text fld").shouldHave(value("test val")
                .because("Customer property isn't saved."));

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

        p.customRichTextFldVal("richtextfld").shouldHave(text("test val")
                .because("Customer property isn't saved."));

    }

    //TODO: Split test into 2 -- PDP and category_view specific checks
    @Test(priority = 4)
    public void editTitle() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setTitle("Edited SKU Title");
        p.clickSave_wait();
        p.navigateTo("Catalog", "SKUs");

        // seraches for SKU by its uid
        p.search( sku.substring(4, sku.length()) );
        shouldHaveText(p.getSKUParamVal("1", "Title"), "Edited SKU Title",
                "SKU title isn't updated on the list.");
        p.openSKU(sku);

        p.titleFld().shouldHave(value("Edited SKU Title")
                .because("Failed to edit SKU title."));

    }

    @Test(priority = 5)
    public void editUPC() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setUpc("Edited UPC");
        p.clickSave_wait();
        p.navigateTo("Catalog", "SKUs");
        p.search(sku.substring(4, sku.length()));
        p.openSKU(sku);

        p.upcFld().shouldHave(value("Edited UPC")
                .because("Failed to edit UPC field."));

    }

    @Test(priority = 6)
    public void editDescription() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);

        clearField( p.descriptionFld() );
        p.setDescription("Edited description");
        p.clickSave_wait();
        p.navigateTo("Catalog", "SKUs");
        p.search( sku.substring(4, sku.length()) );
        p.openSKU(sku);

        p.descriptionFld().shouldHave(text("Edited description")
                .because("Failed to edit description."));

    }

    //TODO: Split test into 2 -- PDP and category_view specific checks
    @Test(priority = 7)
    public void editRetailPrice() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setRetailPrice("70.00");
        p.clickSave_wait();
        p.navigateTo("Catalog", "SKUs");
        p.search(sku.substring(4, sku.length()));
        p.getSKUParamVal("1", "Retail Price").shouldHave(text("70.00")
                .because("Retail price isn't updated on the list."));
        p.openSKU(sku);

        p.retailPriceFld().shouldHave(value("70.00")
                .because("Failed to edit retail price."));

    }

    //TODO: Split test into 2 -- PDP and category_view specific checks
    @Test(priority = 8)
    public void editSalePrice() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setSalePrice("70.00");
        p.clickSave_wait();
        p.navigateTo("Catalog", "SKUs");
        p.search(sku.substring(4, sku.length()));
        shouldHaveText(p.getSKUParamVal("1", "Sale Price"), "70.00", "Sale price isn't updated on the list.");
        p.openSKU(sku);

        p.salePriceFld().shouldHave(value("70.00")
                .because("Failed to edit sale price." ));

    }

    //TODO: Split test into 2 -- PDP and category_view specific checks
    @Test(priority = 9)
    public void editState() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setState("Inactive");
        p.clickSave_wait();
        p.navigateTo("Catalog", "SKUs");
        p.search(sku.substring(4, sku.length()));
        p.openSKU(sku);

        p.stateVal().shouldHave(text("Inactive")
                .because("Failed to edit SKU state."));

    }

    //--------------------------------- REGRESSION TESTS ---------------------------------//

    @Test(priority = 10)
    @Description("Title isn't reset after creating a product with a pre-created SKU")
    public void titleReset() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus/" + sku, SkusPage.class);

        p.titleFld().shouldHave(value(skuTitle)
                .because("SKU Title has been wiped after a product with this SKU was created."));

    }

    @Test(priority = 11)
    @Description("Can't use skuCode of an existing SKU to create a new one")
    public void useExistingSKUCode() throws IOException {

        provideTestData("active SKU");
        p = openPage(adminUrl + "/skus", SkusPage.class);

        p.clickAddNewSKU();
        p.setSKUCode(sku);
        p.clickSave();
        p.errorMsg("already exists").shouldBe(visible);

    }
}