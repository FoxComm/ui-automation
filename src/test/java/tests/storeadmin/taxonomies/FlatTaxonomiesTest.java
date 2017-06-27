package tests.storeadmin.taxonomies;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.TaxonomiesPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;

public class FlatTaxonomiesTest extends Preconditions {
    private TaxonomiesPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() {

        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    @Test(priority = 1, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can create active empty flat taxonomy - form, search_view, product form")
    public void createEmptyFlatTaxonomy() throws IOException {
        String tName = "Taxonomy " + generateRandomID();

        p = openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
        p.clickAddTaxonomyBtn();
        p.setName(tName);
        p.setState("Active");
        p.clickSave();

        p.name_form().shouldHave(text(tName));

        openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
        p.name_searchView(tName).shouldBe(visible);

        openPage(adminUrl + "/products/default/new", TaxonomiesPage.class);
        p.taxonomyName_productForm(tName).shouldBe(visible);
        p.taxonomy_navigationBar(tName).shouldBe(visible);
    }

    @Test(priority = 2, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can edit empty flat taxonomy - form, search_view, product form")
    public void editEmptyFlatTaxonomy() throws IOException {
        provideTestData("create empty flat taxonomy");

        String changedName = taxonomyName + " Edited";
        p = openPage(adminUrl + "/taxonomies/default/" + taxonomyId, TaxonomiesPage.class);
        p.setName(changedName);
        p.clickSave();

        p.name_form().shouldHave(text(changedName));

        openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
        p.name_searchView(changedName).shouldBe(visible);

        openPage(adminUrl + "/products/default/new", TaxonomiesPage.class);
        p.taxonomyName_productForm(changedName).shouldBe(visible);
        p.taxonomy_navigationBar(changedName).shouldBe(visible);
    }

    @Test(priority = 3, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Inactive flat taxonomy is shown in search view with inactive label")
    public void inactiveTaxonomy_searchView() throws IOException{
        provideTestData("create empty flat taxonomy");

        p = openPage(adminUrl + "/taxonomies/default/" + taxonomyId, TaxonomiesPage.class);
        p.setState("Inactive");
        p.clickSave();

        openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
        p.status_searchView(taxonomyName, "Inactive");
    }

    @Test(priority = 4, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can add values to flat taxonomies - form_title, form_values, search_view, product form")
    public void canAddValuesToFlatTaxonomy() throws IOException {
        provideTestData("create empty flat taxonomy");

        p = openPage(adminUrl + "/taxonomies/default/" + taxonomyId, TaxonomiesPage.class);
        String taxonName = "Taxon value " +generateRandomID();
        p.clickValuesTabFld();
        p.clickAddTaxonValueBtn();
        p.setState("Active");
        p.setName(taxonName);
        p.clickSave();

        p.name_form().shouldHave(text(taxonName));

        openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values", TaxonomiesPage.class);
        p.name_searchView(taxonName).shouldBe(visible);

        openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
        p.counterValues_searchView(taxonomyName).shouldHave(text("1"));

        openPage(adminUrl + "/products/default/new", TaxonomiesPage.class);
        p.clickShowTaxonBtn_productForm(taxonomyName);
        p.clickEnterTaxonFld(taxonomyName);
        p.taxonDdItemFld_productForm(taxonomyName, taxonName).shouldBe(visible);
    }

    @Test(priority = 5, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can edit values for flat taxonomies - form_title, form_values, search_view, product form")
    public void canEditValuesFor_FlatTaxonomy() throws IOException {
        provideTestData("create taxonomy with taxon");

        p = openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values/" + taxonId, TaxonomiesPage.class);
        String tName = taxonName + " Edited";
        p.setName(tName);
        p.clickSave();

        p.name_form().shouldHave(text(tName));

        openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values", TaxonomiesPage.class);
        p.name_searchView(tName).shouldBe(visible);

        openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
        p.counterValues_searchView(taxonomyName).shouldHave(text("1"));

        openPage(adminUrl + "/products/default/new", TaxonomiesPage.class);
        p.clickShowTaxonBtn_productForm(taxonomyName);
        p.clickEnterTaxonFld(taxonomyName);
        p.taxonDdItemFld_productForm(taxonomyName, tName).shouldBe(visible);
    }

    @Test(priority = 6, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can change value of taxon for Flat taxonomy to inactive - search_view")
    public void inactiveTaxon_searchView() throws IOException {
        provideTestData("create taxonomy with taxon");

        p = openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values/" + taxonId, TaxonomiesPage.class);
        p.setState("Inactive");
        p.clickSave();

        openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values", TaxonomiesPage.class);
        p.status_searchView(taxonName, "Inactive");
    }

    @Test(priority = 7, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can archive flat taxonomy - will not be shown in search_view, product_form")
    public void archiveTaxonomy() throws IOException {
        provideTestData("create taxonomy with taxon");
        p = openPage(adminUrl + "/taxonomies/default/" + taxonomyId, TaxonomiesPage.class);

        p.clickArchiveElement("Taxonomy");
        p.clickSaveBtn_modalCxt("Confirm Archive taxonomy");
        openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);

        p.name_searchView(taxonomyName).shouldNotBe(visible);

        openPage(adminUrl + "/products/default/new", TaxonomiesPage.class);
        p.taxonomyName_productForm(taxonomyName).shouldNotBe(visible);
        p.taxonomy_navigationBar(taxonomyName).shouldNotBe(visible);
    }

    @Test(priority = 8, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can archive taxon in flat taxonomy - will not be shown in search_view, product_form")
    public void archiveTaxon() throws IOException {
        provideTestData("create taxonomy with taxon");

        p = openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values/" + taxonId, TaxonomiesPage.class);
        p.clickArchiveElement("Taxon");
        p.clickSaveBtn_modalCxt("Confirm Archive taxon");

        openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values", TaxonomiesPage.class);
        p.name_searchView(taxonName).shouldNotBe(visible);

        openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
        p.counterValues_searchView(taxonomyName).shouldHave(text("0"));

        openPage(adminUrl + "/products/default/new", TaxonomiesPage.class);
        p.clickShowTaxonBtn_productForm(taxonomyName);
        p.clickEnterTaxonFld(taxonomyName);
        p.taxonDdItemFld_productForm(taxonomyName, taxonName).shouldNotBe(visible);
    }

    @Test(priority = 9, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can add taxon values to product - search_view_values_counter, product_form")
    public void addTaxonToProduct() throws IOException {
        provideTestData("create taxonomy with taxon");
        provideTestData("product in active state");

        p = openPage(adminUrl + "/products/default/" + productId, TaxonomiesPage.class);
        p.clickShowTaxonBtn_productForm(taxonomyName);
        p.clickEnterTaxonFld(taxonomyName);
        p.clickTaxonValueFld(taxonomyName, taxonName);

        p.addedTaxonBar_productForm(taxonomyName, taxonName).shouldBe(visible);

        openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values", TaxonomiesPage.class);
        p.counterProducts_searchView(taxonName).shouldHave(text("1"));
    }

    @Test(priority = 10, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can remove taxon value from product - search_view_values_counter, product_form")
    public void removeTaxonFromProduct() throws IOException {
        provideTestData("create taxonomy with taxon added to created product");

        p = openPage(adminUrl + "/products/default/" + productId, TaxonomiesPage.class);
        p.clickRemoveTaxonBar_productForm(taxonomyName, taxonName);

        p.addedTaxonBar_productForm(taxonomyName, taxonName).shouldNotBe(visible);

        openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values", TaxonomiesPage.class);
        p.counterProducts_searchView(taxonName).shouldHave(text("0"));
    }

    @Test(priority = 11, enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can edit taxon value added to product - search_view_values_counter, product_form")
    public void editTaxonAddedToProduct() throws IOException {
        provideTestData("create taxonomy with taxon added to created product");

        p = openPage(adminUrl + "/taxonomies/default/" + taxonomyId + "/values/" + taxonId, TaxonomiesPage.class);
        String tName = taxonName + " Edited";
        p.setName(tName);
        p.clickSave();

        p = openPage(adminUrl + "/products/default/" + productId, TaxonomiesPage.class);
        p.addedTaxonBar_productForm(taxonomyName, tName).shouldBe(visible);
    }

    @Test(priority = 12, enabled = true)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Taxonomies : Flat")
    @Description("Can open taxon value from product form")
    public void canOpenTaxon_productForm() throws IOException {
//        provideTestData("create taxonomy with taxon added to created product");
//
//        p = openPage(adminUrl + "/products/default/" + productId, TaxonomiesPage.class);
//        p.clickTaxonBar_productForm(taxonomyName, taxonName);
//
//        p.name_form().shouldHave(text(taxonName));
    }

//    to be improved
    @AfterClass(enabled = true)
    public void cleanData(){
        System.out.println("Here we will Archive all created taxonomies, in order not to spam product form");
        p = openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
        sleep(5000);
        ElementsCollection listOfTaxonomies = p.names_searchView("Taxonomy");
        System.out.println(listOfTaxonomies);
        System.out.println("---------------------");
        for (SelenideElement element : listOfTaxonomies ){
            System.out.println(element.getText());
            p.name_searchView(element.getText()).waitUntil(visible, 20000);
            p.clickSearchViewItem(element.getText());
            p.clickArchiveElement("Taxonomy");
            p.clickSaveBtn_modalCxt("Confirm Archive taxonomy");
            openPage(adminUrl + "/taxonomies", TaxonomiesPage.class);
            sleep(10000);
        }
    }

}
