package tests.storeadmin.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.LoginPage;
import pages.admin.ProductsPage;
import pages.admin.StorefrontCategoryPage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static testdata.api.collection.Inventory.checkInventoryAvailability;

public class EditProductTest extends Preconditions {

    private ProductsPage p;
    private StorefrontCategoryPage sf;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            loginPage.userMenuBtn().shouldBe(visible);
        }
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can edit product's \"Title\" -- changes are visible on PDP in Ashes")
    public void editTitle_admin_PDP() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);
        String uid = productTitle.substring(productTitle.indexOf(" ") + 1);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        clearField(p.titleFld());
        p.setTitle("Edited Product " + uid);
        p.clickSave_wait();
        refresh();

        p.titleFld().shouldHave(attribute("value", "Edited Product " + uid));
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can edit product's \"Title\" -- changes are visible in search_view")
    public void editTitle_admin_list() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);
        String uid = productTitle.substring(productTitle.indexOf(" ") + 1);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        clearField(p.titleFld());
        p.setTitle("Edited Product " + uid);
        p.clickSave_wait();
        p.navigateTo("Products");
        p.search(uid);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getProductParamVal("1", "Title").shouldHave(text("Edited Product " + uid));

    }

//    @Test(priority = 3)
//    public void editTitle_storefront_PDP() throws IOException {
//
//        provideTestData("product in active state");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        String uid = productTitle.substring(13, 20);
//
//        clearField(p.titleFld());
//        p.setTitle("Edited Product " + uid);
//        p.clickSave();
//
//        sf = open(storefrontUrl + "/products/" + productId, StorefrontCategoryPage.class);
//        sf.titleVal().shouldHave(text("Edited Product " + uid)
//                .because("Failed to edit product title - incorrect product title is displayed on storefront PDP." ));
//
//    }
//
//    @Test(priority = 4)
//    public void editTitle_storefront_category() throws IOException {
//
//        provideTestData("product in active state");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        String uid = productTitle.substring(13, 20);
//
//        clearField(p.titleFld());
//        p.setTitle("Edited Product " + uid);
//        p.clickSave();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        sf.product("Edited Product " + uid).shouldBe(visible
//                .because("Failed to edit product title - product isn't found by a new title on storefront category page."));
//
//    }

    @Test(priority = 5)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can edit \"Description\" -- changes are visible on PDP")
    public void editDescription_admin_PDP() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        clearField(p.descriptionFld());
        p.setDescription("Edited Description");
        p.clickSave_wait();
        refresh();

        p.descriptionFld().shouldHave(text("Edited Description"));
    }

//    @Test(priority = 6)
//    public void editDescription_storefront_PDP() throws IOException {
//
//        provideTestData("product in active state");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//
//        clearField(p.descriptionFld());
//        p.setDescription("Edited Description");
//        p.clickSave_wait();
//
//        sf = open(storefrontUrl + "/products/" + productId, StorefrontCategoryPage.class);
//        sf.descriptionVal().shouldHave(text("Edited Description")
//                .because("Failed to edit description - incorrect description is displayed on storefront PDP."));
//
//    }

    @Test(priority = 7)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can edit \"State\" -- changes are visible on PDP")
    public void editState_admin_PDP() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.setState("Inactive");
        p.clickSave_wait();
        refresh();
        shouldBeVisible(p.stateDd(),
                "Waiting for \"State\" dd to become visible after refreshing the page has failed");

        p.stateVal().shouldHave(text("Inactive"));
    }

    @Test(priority = 8)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can edit \"State\" -- changes are visible in search_view")
    public void editState_admin_category() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);
        String uid = productTitle.substring(productTitle.indexOf(" ") + 1);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.setState("Inactive");
        p.clickSave_wait();
        p.navigateTo("Products");
        p.navigateTo("Products");
        p.search(uid);
        shouldBeVisible(contentOnList(), "Search request returned no results.");

        p.getProductParamVal("1", "State").shouldHave(text("Inactive"));
    }

//    @Test(priority = 9)
//    public void editState_storefront() throws IOException {
//
//        provideTestData("product in inactive state");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//
//        p.setState("Active");
//        p.clickSave();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        sf.product(productTitle).shouldBe(visible
//                .because("Failed to edit product state - product isn't displayed on the category page on storefront."));
//
//    }

    @Test(priority = 10)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can edit \"Retail Price\" -- changes are visible on PDP")
    public void editRetailPrice_admin_PDP() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.setRetailPrice("1", "35.18");
        p.clickSave_wait();
        refresh();

        p.skuRetailPriceFld("1").shouldHave(value("35.18"));
    }

    @Test(priority = 11)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can edit \"Sale Price\" -- changes are visible on PDP")
    public void editSalePrice_admin_PDP() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.setSalePrice("1", "35.18");
        p.clickSave_wait();
        refresh();

        p.skuSalePriceFld("1").shouldHave(value("35.18"));
    }

//    @Test(priority = 12)
//    public void editSalePrice_storefront_PDP() throws IOException {
//
//        provideTestData("product in active state");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//
//        p.setSalePrice("35.18");
//        p.clickSave();
//
//        sf = open(storefrontUrl + "/products/" + productId, StorefrontCategoryPage.class);
//        sf.priceVal().shouldHave(text("$35.18")
//                .because("Failed to edit sale price."));
//
//    }

    @Test(priority = 13)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can edit tag -- changes are visible on PDP")
    public void editTag_admin_PDP() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.removeTag("sunglasses");
        p.addTag("eyeglasses");
        p.clickSave_wait();
        refresh();
        p.addTagBtn().shouldBe(visible);
        shouldBeVisible(p.addTagBtn(),
                "Waiting for \"Add Tag\" btn to become visible after refreshing the page has failed");

        p.tag("eyeglasses").shouldBe(visible);
        p.tag("sunglasses").shouldNotBe(visible);
    }

//    @Test(priority = 14)
//    public void editTag_storefront() throws IOException {
//
//        provideTestData("product in active state");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//
//        p.removeTag("sunglasses");
//        p.addTag("eyeglasses");
//        p.clickSave();
//
//        sf = open(storefrontUrl + "/sunglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//        sf.product(productTitle).shouldBe(visible
//                .because("Product isn't displayed on storefront category page."));
//
//    }

    @Test(priority = 15)
    @Severity(SeverityLevel.NORMAL)
    @Features("Ashes")
    @Stories("Products : Edit")
    @Description("Can remove tag -- changes are visible in admin")
    public void removeTag_admin() throws IOException {
        provideTestData("product in active state");
        checkInventoryAvailability(skuCode);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.removeTag("sunglasses");
        p.clickSave_wait();
        refresh();
        shouldBeVisible(p.addTagBtn(),
                "Waiting for \"Add Tag\" btn to become visible after refreshing the page has failed");

        p.tag("sunglasses").shouldNotBe(visible);
    }

//    @Test(priority = 16)
//    public void removeTag_storefront() throws IOException {
//
//        provideTestData("product in active state");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//
//        p.removeTag("sunglasses");
//        p.clickSave();
//
//        sf = open(storefrontUrl + "/eyeglasses?type=men", StorefrontCategoryPage.class);
//        sf.waitForDataToLoad_sf();
//
//        assertTwice(sf.product(productTitle), "should not be visible",
//                "Product is displayed on the storefront category (it shouldn't)");
//
//    }

}
