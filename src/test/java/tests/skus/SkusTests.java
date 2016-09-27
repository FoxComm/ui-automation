package tests.skus;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SkusPage;
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
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    @Test(priority = 1)
    public void createSKU() {

        p = open(adminUrl + "/skus", SkusPage.class);
        String randomId = generateRandomID();

        p.clickAddNewSKU();
        p.createNewSKU(randomId, "Active");
        p.clickSave();
        shouldHaveText(p.skuCodeVal(), "SKU-" + randomId, "Failed to create new SKU");
        p.sideMenu("SKUs");
        p.waitForDataToLoad();

        p.search(randomId);
        p.getSKUParamVal("1", "Code").shouldHave(text("SKU-" + randomId)
                .because("A just created SKU isn't displayed on the list."));

    }

    @Test(priority = 2)
    public void addCustomProp_text() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.addCustomProp("Text", "text fld");
        p.setCustomProp_text("text fld", "test val");
        p.clickSave();
        refresh();

        p.customTextFld("text fld").shouldHave(value("test val")
                .because("Customer property isn't saved."));

    }

    @Test(priority = 3)
    public void addCustomProp_richText() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.addCustomProp("Rich Text", "richText fld");
        p.setCustomProp_richText("richText fld", "test val");
        p.clickSave();
        refresh();

        p.customRichTextFld("richText fld").shouldHave(text("test val")
                .because("Customer property isn't saved."));

    }

    @Test(priority = 4)
    public void editTitle() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setTitle("Edited SKU Title");
        p.clickSave();
        p.sideMenu("SKUs");

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
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setUpc("Edited UPC");
        p.clickSave();
        p.sideMenu("SKUs");
        p.search( sku.substring(4, sku.length()) );
        p.openSKU(sku);

        p.upcFld().shouldHave(value("Edited UPC")
                .because("Failed to edit UPC field."));

    }

    @Test(priority = 6)
    public void editDescription() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        clearField( p.descriptionFld() );
        p.setDescription("Edited description");
        p.clickSave();
        p.sideMenu("SKUs");
        p.search( sku.substring(4, sku.length()) );
        p.openSKU(sku);

        p.descriptionFld().shouldHave(text("Edited description")
                .because("Failed to edit description."));

    }

    /*
     * Split test into 2: PDP and category checks
     */
    @Test(priority = 7)
    public void editRetailPrice() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setRetailPrice("70.00");
        p.clickSave();
        p.sideMenu("SKUs");
        p.search( sku.substring(4, sku.length()) );
        p.getSKUParamVal("1", "Retail Price").shouldHave(text("70.00")
                .because("Retail price isn't updated on the list."));
        p.openSKU(sku);

        p.retailPriceFld().shouldHave(value("70.00")
                .because("Failed to edit retail price."));

    }

    @Test(priority = 8)
    public void editSalePrice() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setSalePrice("70.00");
        p.clickSave();
        p.sideMenu("SKUs");
        p.search( sku.substring(4, sku.length()) );
        shouldHaveText(p.getSKUParamVal("1", "Sale Price"), "70.00", "Sale price isn't updated on the list.");
        p.openSKU(sku);

        p.salePriceFld().shouldHave(value("70.00")
                .because("Failed to edit sale price." ));

    }

    @Test(priority = 9)
    public void editState() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.setState("Inactive");
        p.clickSave();
        click( p.sideMenu("SKUs") );
        p.sideMenu("SKUs");
        p.search( sku.substring(4, sku.length()) );
        p.openSKU(sku);

        p.stateVal().shouldHave(text("Inactive")
                .because("Failed to edit SKU state."));

    }

    @Test(priority = 10)
    public void titleReset_regressionTest() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.titleFld().shouldHave(attribute("value", skuTitle)
                .because("SKU Title has been wiped after a product with this SKU was created."));

    }

    @Test(priority = 10)
    public void useExistingSKUCode_regressionTest() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus", SkusPage.class);

        click( p.addNewSKUBtn() );
        p.setSKUCode( sku );
        p.setTitle("Test Title");
        p.setUpc("Test UPC");
        p.setDescription("Just another test SKU.");
        p.setRetailPrice("50.00");
        p.setSalePrice("32.00");
        p.setUnitCost("32.00");
        p.clickSave();

        p.skuFld().shouldNotBe(empty
                .because("'SKU' field is empty - probably form has been blanked."));
        p.titleFld().shouldNotBe(empty
                .because("'Title' field is empty - probably form has been blanked."));
        p.upcFld().shouldNotBe(empty
                .because("'UPC' field is empty - probably form has been blanked."));
        p.retailPriceFld().shouldNotBe(empty
                .because("'Retail Price' field is empty - probably form has been blanked."));
        p.salePriceFld().shouldNotBe(empty
                .because("'Sale Price' field is empty - probably form has been blanked."));
        p.unitCostFld().shouldNotBe(empty
                .because("'Unit Price' field is empty - probably form has been blanked."));

    }
}