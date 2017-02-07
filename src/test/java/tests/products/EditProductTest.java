package tests.products;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import pages.StorefrontCategoryPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class EditProductTest extends DataProvider {

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
    public void editTitle_admin_PDP() throws IOException {
        provideTestData("product in active state");
        String uid = productName.substring(13, 20);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        clearField(p.titleFld());
        p.setTitle("Edited Product " + uid);
        p.clickSave_wait();
        refresh();

        p.titleFld().shouldHave(attribute("value", "Edited Product " + uid));
    }

    @Test(priority = 2)
    public void editTitle_admin_list() throws IOException {
        provideTestData("product in active state");
        String uid = productName.substring(13, 20);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        clearField(p.titleFld());
        p.setTitle("Edited Product " + uid);
        p.clickSave_wait();
        p.navigateTo("Catalog", "Products");
        p.search(uid);

        p.getProductParamVal("1", "Title").shouldHave(text("Edited Product " + uid));

    }

//    @Test(priority = 3)
//    public void editTitle_storefront_PDP() throws IOException {
//
//        provideTestData("product in active state");
//        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
//        String uid = productName.substring(13, 20);
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
//        String uid = productName.substring(13, 20);
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
    public void editDescription_admin_PDP() throws IOException {
        provideTestData("product in active state");

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
    public void editState_admin_PDP() throws IOException {
        provideTestData("product in active state");

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.setState("Inactive");
        p.clickSave_wait();
        refresh();
        shouldBeVisible(p.stateDd(),
                "Waiting for \"State\" dd to become visible after refreshing the page has failed");

        p.stateVal().shouldHave(text("Inactive"));
    }

    @Test(priority = 8)
    public void editState_admin_category() throws IOException {
        provideTestData("product in active state");
        String uid = productName.substring(13, 20);

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.setState("Inactive");
        p.clickSave_wait();
        p.navigateTo("Catalog", "Products");
        p.search(uid);

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
//        sf.product(productName).shouldBe(visible
//                .because("Failed to edit product state - product isn't displayed on the category page on storefront."));
//
//    }

    @Test(priority = 10)
    public void editRetailPrice_admin_PDP() throws IOException {
        provideTestData("product in active state");

        p = openPage(adminUrl + "/products/default/" + productId, ProductsPage.class);
        p.setRetailPrice("1", "35.18");
        p.clickSave_wait();
        refresh();

        p.skuRetailPriceFld("1").shouldHave(value("35.18"));
    }

    @Test(priority = 11)
    public void editSalePrice_admin_PDP() throws IOException {
        provideTestData("product in active state");

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
    public void editTag_admin() throws IOException {
        provideTestData("product in active state");

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
//        sf.product(productName).shouldBe(visible
//                .because("Product isn't displayed on storefront category page."));
//
//    }

    @Test(priority = 15)
    public void removeTag_admin() throws IOException {
        provideTestData("product in active state");

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
//        assertTwice(sf.product(productName), "should not be visible",
//                "Product is displayed on the storefront category (it shouldn't)");
//
//    }

}
