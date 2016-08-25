package tests.skus;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SkusPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
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
        }

    }

    @Test(priority = 1)
    public void createSKU() {

        p = open(adminUrl + "/skus", SkusPage.class);
        String randomId = generateRandomID();

        click( p.addNewSKUBtn() );
        p.createNewSKU(randomId, "Active");
        p.clickSave();
        p.skuCodeVal().shouldHave(text("SKU-" + randomId)
                .because("Failed to create new SKU."));
        click( p.sideMenu("SKUs") );
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
        setFieldVal( p.customTextFld("text fld"), "test val" );
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
        p.customRichTextFld().val("test val");
        p.clickSave();
        refresh();

        p.customRichTextFld().shouldHave(text("test val")
                .because("Customer property isn't saved.");

    }

    @Test(priority = 4)
    public void editTitle() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        setFieldVal( p.titleFld(), "Edited SKU Title" );
        p.clickSave();
        click( p.sideMenu("SKUs") );

        p.search( sku.substring(4, sku.length()) );
        p.getSKUParamVal("1", "Title").shouldHave(text("Edited SKU Title")
                .because("SKU title isn't updated on the list."));
        p.openSKU(sku);

        p.titleFld().shouldHave(value("Edited SKU Title")
                .because("Failed to edit SKU title."));

    }

    @Test(priority = 5)
    public void editUPC() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        setFieldVal( p.upcFld(), "Edited UPC" );
        p.clickSave();
        click( p.sideMenu("SKUs") );
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
        setFieldVal( p.descriptionFld(), "Edited description" );
        p.clickSave();
        click( p.sideMenu("SKUs") );
        p.search( sku.substring(4, sku.length()) );
        p.openSKU(sku);

        p.descriptionFld().shouldHave(text("Edited description")
                .because("Failed to edit description."));

    }

    @Test(priority = 7)
    public void editRetailPrice() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        setFieldVal( p.retailPriceFld(), "70.00" );
        p.clickSave();
        click( p.sideMenu("SKUs") );
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

        setFieldVal( p.salePriceFld(), "70.00" );
        p.clickSave();
        click( p.sideMenu("SKUs") );
        p.search( sku.substring(4, sku.length()) );
        p.getSKUParamVal("1", "Sale Price").shouldHave(text("70.00")
                .because("Sale price isn't updated on the list."));
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
        p.search( sku.substring(4, sku.length()) );
        p.openSKU(sku);

        p.stateVal().shouldHave(text("Inactive")
                .because("Failed to edit SKU state."));

    }

    @Test(priority = 99)
    public void titleReset_regressionTest() throws IOException {

        provideTestData("active product, has tag, active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.titleFld().shouldHave(text("SKU Test Title")
                .because("SKU Title has been wiped after a product with this SKU was created."));

    }

}
