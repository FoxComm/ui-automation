package tests.storeadmin.products;
import org.openqa.selenium.By;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.admin.CartPage;
import pages.admin.LoginPage;
import pages.admin.ProductsPage;
import pages.admin.SkusPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static testdata.api.collection.Cart.createCart;
import static testdata.api.collection.Customers.createCustomer;

public class ProductsTest extends Preconditions {

    private CartPage cartPage;
    private SkusPage skusPage;
    private ProductsPage productsPage;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        open(adminUrl);
        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login(adminOrg, adminEmail, adminPassword);
            shouldBeVisible(loginPage.userMenuBtn(), "Failed to log in");
        }
    }

    //--------------------- CART
    @Test(priority = 1, dataProvider = "canAddProductToCart_admin")
    @Description("Product is displayed in line items search view in admin and can be added to cart")
    public void canAddProductToCart_admin(String testData) throws IOException {
        provideTestData(testData);

        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        cartPage.clickEditBtn("Line Items");
        cartPage.searchForItem(productTitle);
        cartPage.addFoundItem(productTitle);

        cartPage.lineItem_editing(productTitle).shouldBe(visible);
    }

    @Test(priority = 2, dataProvider = "productNotDisplayedLineItemsSearchView")
    @Description("Product isn't displayed in line items search view in admin")
    public void productNotDisplayedLineItemsSearchView(String testData) throws IOException {
        provideTestData(testData);
        createCustomer();
        createCart(customerId);

        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        cartPage.clickEditBtn("Line Items");
        cartPage.searchForItem(productTitle);

        cartPage.lineItemSearchView_byName(productTitle).shouldNotBe(visible);
    }

    @Test(priority = 3, dataProvider = "canAddSkuToCart_admin")
    @Description("SKU is found in line items search view in admin and can be added to cart")
    public void canAddSkuToCart_admin(String testData) throws IOException {
        provideTestData(testData);
        createCustomer();
        createCart(customerId);

        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        cartPage.clickEditBtn("Line Items");
        cartPage.searchForItem(skuCode);
        cartPage.addFoundItem(skuCode);

        cartPage.lineItem_editing(skuCode).shouldBe(visible);
    }

    @Test(priority = 4, dataProvider = "skuNotDisplayedLineItemsSearchView")
    @Description("SKU is not found in line items search view in admin and can be added to cart")
    public void skuNotDisplayedLineItemsSearchView(String testData) throws IOException {
        provideTestData(testData);
        createCustomer();
        createCart(customerId);

        cartPage = openPage(adminUrl + "/carts/" + cartId, CartPage.class);
        cartPage.clickEditBtn("Line Items");
        cartPage.searchForItem(skuCode);

        cartPage.lineItemSearchView_byName(skuCode).shouldNotBe(visible);
    }

    //--------------------- SKUS
    @Test(priority = 5, dataProvider = "skuCreatedAlongWithProduct")
    @Description("SKU is created along with the product")
    public void skuCreatedAlongWithProduct(String testData) throws IOException {
        provideTestData(testData);


        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.search(skuCode);
        skusPage.openSKU(skuCode);

        skusPage.breadcrumb().shouldHave(text(skuCode));
    }

    @Test(priority = 6, dataProvider = "newSkuInheritsProductState")
    @Description("SKU created along with product inherits product's state")
    public void newSkuInheritsProductState(String testData, String expState) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus/" + skuCode, SkusPage.class);

        skusPage.stateVal().shouldHave(text(expState));
    }

    @Test(priority = 7, dataProvider = "skuIsNotArchived")
    @Description("SKU is not archived")
    public void skuIsNotArchived(String testData) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.search(skuCode);
        skusPage.openSKU(skuCode);

        skusPage.breadcrumb().shouldHave(text(skuCode));
    }

    @Test(priority = 8, dataProvider = "archivedSkuRemovedFromGeneralCategoryView")
    @Description("Archived SKU is not displayed on the category view in admin")
    public void archivedSkuRemovedFromGeneralCategoryView(String testData) throws IOException {
        provideTestData(testData);

        skusPage = openPage(adminUrl + "/skus", SkusPage.class);
        skusPage.search(skuCode);
        skusPage.noSearchResultsMsg().shouldBe(visible);

        skusPage.switchToSearchTab("Archived");
        click(skusPage.searchFld());
        click(skusPage.counter());

        $(By.xpath("//*[text()='" + skuCode + "']")).shouldBe(visible);
        assertTwice($(By.xpath("//*[text()='" + skuCode + "']")), "should be visible", "");
    }


    //--------------------- PRODUCTS
    @Test(priority = 9, dataProvider = "productIsNotArchived")
    @Description("Product is not archived")
    public void productIsNotArchived(String testData) throws IOException {
        provideTestData(testData);

        productsPage = openPage(adminUrl + "/products", ProductsPage.class);

        productsPage.search(productTitle);
        productsPage.openProduct(productTitle);

        productsPage.breadcrumb().shouldHave(text(productId));
    }

    @Test(priority = 10, dataProvider = "archivedProductRemovedFromGeneralCategoryView")
    @Description("Archived product is not displayed on the category view in admin")
    public void archivedProductRemovedFromGeneralCategoryView(String testData) throws IOException {
        provideTestData(testData);

        productsPage = openPage(adminUrl + "/products", ProductsPage.class);
        productsPage.search(productTitle);
        productsPage.noSearchResultsMsg().shouldBe(visible);

        productsPage.switchToSearchTab("Archived");
        click(productsPage.searchFld());
        click(productsPage.counter());

        assertTwice($(By.xpath("//*[text()='" + productTitle + "']")), "should be visible", "");
    }

}