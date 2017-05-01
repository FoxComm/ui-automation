package tests.storeadmin.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.ProductsPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.refresh;
import static testdata.api.collection.Inventory.checkInventoryAvailability;

public class NewProductFormTest extends Preconditions {

    private ProductsPage p;

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
    public void skuIsRequired() {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.setTitle("SKU-TST");
        p.clickSave();

        p.errorMsg("SKU Code").shouldBe(visible);
    }

    // TODO: change error message after it'll be changed with something more user friendly
    @Test(priority = 2)
    public void titleIsRequired() throws IOException {
        provideTestData("active SKU");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.addExistingSKU(skuCode);
        p.clickSave();

        p.errorMsg("title is a required field").shouldBe(visible);
    }

    @Test(priority = 3)
    public void addOption() {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.clickAddBtn_option();
        p.setOptionName("color");
        p.clickSaveBtn_modal();

        p.option("color").shouldBe(visible);
    }

    @Test(priority = 4)
    public void addOptionValue() {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.clickAddBtn_option();
        p.setOptionName("color");
        p.clickSaveBtn_modal();
        p.addOptionValue("color", "red");

        p.optionValue_SKUsTable("color", "red").shouldBe(visible);
    }

    @Test(priority = 5)
    @Description("An empty SKU record is created for each added option value after after the second one")
    public void skuIsAddedWithOptionValue() {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.addOption("color");
        p.addOptionValue("color", "red");
        p.addOptionValue("color", "blue");

        p.assertAmountOfSKUs(2);
        p.skuFld(2).shouldHave(value("")
                .because("A just added SKU line isn't empty"));
    }

    @Test(priority = 6)
    @Description("Removing option value also removes SKU related to that variant")
    public void delOptionValue_skuIsAutoremoved() {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.addOption("color");
        p.addOptionValue("color", "red");
        p.addOptionValue("color", "blue");
        p.removeOptionValue("blue");

        p.sku_byVariant("blue").shouldNotBe(visible);
    }

    @Test(priority = 7)
    public void editValueTest() {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.addOption("color");
        p.addOptionValue("color", "red");
        p.editValue("red", "green");

        p.sku_byVariant("red").shouldNot(exist);
        p.sku_byVariant("green").shouldBe(visible);
    }

    @Test(priority = 8)
    @Description("Assert that new SKU line is added for each possible option value")
    public void possibleValuesCoverageWithSKUs() {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.addOption("color");
        p.addOptionValue("color", "red");
        p.addOptionValue("color", "blue");
        p.addOption("size");
        p.addOptionValue("size", "small");
        p.addOptionValue("size", "medium");
        p.assertAmountOfSKUs(4);
        p.addOptionValue("size", "large");

        p.assertAmountOfSKUs(6);
    }

    @Test(priority = 9)
    public void removeSKU() {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.addOption("color");
        p.addOptionValue("color", "red");
        p.addOptionValue("color", "blue");
        p.addOption("size");
        p.addOptionValue("size", "small");
        p.removeSKU("1");

        p.sku_byVariant("red").shouldNotBe(visible);
    }

    @Test(priority = 10)
    @Description("Removed SKU related to a specific option values combination can be re-added")
    public void reAddVariantSpecificSKU() throws IOException {
        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.addOption("color");
        p.addOptionValue("color", "red");
        p.addOptionValue("color", "blue");
        p.addOption("size");
        p.addOptionValue("size", "small");
        p.removeSKU("2");
        p.reAddSKU("blue", "small");

        p.sku_byVariants("blue", "small").shouldBe(visible);
    }

    @Test(priority = 11)
    @Description("Options are saved correctly")
    public void optionsAreSaved() throws IOException {
        provideTestData("active SKU");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.setTitle("Product " + generateRandomID());
        p.addOption("color");
        p.addOptionValue("color", "red");
        p.addExistingSKU(skuCode);
        p.clickSave_wait();
        shouldNotBeVisible(p.breadcrumb("new"), "");
        refresh();

        p.option("color").shouldBe(visible);
        p.optionValue("color", "red").shouldBe(visible);
    }

    @Test(priority = 12)
    @Description("Options are saved correctly")
    public void variantSpecificSkuIsSaved() throws IOException {
        provideTestData("active SKU");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/new", ProductsPage.class);
        p.setTitle("Product " + generateRandomID());
        p.addOption("color");
        p.addOptionValue("color", "red");
        p.addExistingSKU(skuCode);
        p.clickSave_wait();
        shouldNotBeVisible(p.breadcrumb("new"), "");
        refresh();

        p.sku(skuCode).shouldBe(visible);
        p.sku_byVariant("red").shouldBe(visible);
    }

}