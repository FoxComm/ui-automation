package tests.skus;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SkusPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static org.testng.Assert.assertEquals;

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
        assertEquals( p.skuCodeVal(), "SKU-" + randomId, "Failed to create new SKU.");
        click( p.sideMenu("SKUs") );
        p.waitForDataToLoad();

        p.search(randomId);
        assertEquals( p.getSKUParamVal("1", "Code"), "SKU-" + randomId,
                "A just created SKU isn't displayed on the list." );

    }

    @Test(priority = 2)
    public void addCustomProp_text() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.addCustomProp("Text", "text fld");
        setFieldVal( p.customTextFld("text fld"), "test val" );
        p.clickSave();
        refresh();

        p.customTextFld("text fld").shouldBe(visible);
        assertEquals( p.customTextFld("text fld").getValue(), "test val", "Customer property isn't saved." );

    }

    @Test(priority = 3)
    public void addCustomProp_richText() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.addCustomProp("Rich Text", "richText fld");
        p.customRichTextFld().val("test val");
        p.clickSave();
        refresh();

        assertEquals( p.customRichTextFld().text(), "test val", "Customer property isn't saved." );

    }

    @Test(priority = 4)
    public void editTitle() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        setFieldVal( p.titleFld(), "Edited SKU Title" );
        p.clickSave();
        click( p.sideMenu("SKUs") );

        p.search( sku.substring(4, sku.length()) );
        assertEquals( p.getSKUParamVal("1", "Title"), "Edited SKU Title",
                "SKU title isn't updated on the list." );
        p.openSKU(sku);

        p.descriptionFld().shouldBe(visible);
        assertEquals( p.titleFld().getValue(), "Edited SKU Title",
                "Failed to edit SKU title." );

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

        p.descriptionFld().shouldBe(visible);
        assertEquals( p.upcFld().getValue(), "Edited UPC",
                "Failed to edit UPC field." );

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

        p.descriptionFld().shouldBe(visible);
        assertEquals( p.descriptionFld().text(), "Edited description",
                "Failed to edit description." );

    }

    @Test(priority = 7)
    public void editRetailPrice() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        setFieldVal( p.retailPriceFld(), "70.00" );
        p.clickSave();
        click( p.sideMenu("SKUs") );
        p.search( sku.substring(4, sku.length()) );
        assertEquals( p.getSKUParamVal("1", "Retail Price"), "70.00",
                "Retail price isn't updated on the list.");
        p.openSKU(sku);

        p.retailPriceFld().shouldBe(visible);
        assertEquals( p.retailPriceFld().getValue(), "70.00",
                "Failed to edit retail price." );

    }

    @Test(priority = 8)
    public void editSalePrice() throws IOException {

        provideTestData("active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        setFieldVal( p.salePriceFld(), "70.00" );
        p.clickSave();
        click( p.sideMenu("SKUs") );
        p.search( sku.substring(4, sku.length()) );
        assertEquals( p.getSKUParamVal("1", "Sale Price"), "70.00",
                "Sale price isn't updated on the list.");
        p.openSKU(sku);

        p.salePriceFld().shouldBe(visible);
        assertEquals( p.salePriceFld().getValue(), "70.00",
                "Failed to edit sale price." );

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

        p.skuFld().shouldBe(visible);
        assertEquals( p.stateVal(), "Inactive",
                "Failed to edit SKU state." );

    }

    @Test(priority = 99)
    public void titleReset_regressionTest() throws IOException {

        provideTestData("active product, has tag, active SKU");
        p = open(adminUrl + "/skus/" + sku, SkusPage.class);

        p.titleFld().shouldBe(visible);
        assertEquals( p.titleFld().text(), "SKU Test Title");

    }

}
