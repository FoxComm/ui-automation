package tests.storeadmin.products;
import org.testng.annotations.Test;
import testdata.DataProvider;
import java.io.IOException;

public class ProductsTest extends DataProvider {


    // Active product, active SKU, has tag, has sellable stockitems in MWH
    @Test(priority = 1)
    public void activeProduct_activeSKU_hasTag_hasSellableItems() throws IOException {
        loginAsAdmin();
        createSKU_active();
        createProduct_active(sku, "modern");
        increaseOnHandQty(sku, "Sellable", 20);
    }

    // Active product, active SKU, no tag, has sellable stockitems in MWH
    @Test(priority = 2)
    public void activeProduct_activeSKU_noTag_hasSellableItems() throws IOException {
        loginAsAdmin();
        createSKU_active();
        createProduct_active_noTag(sku);
        increaseOnHandQty(sku, "Sellable", 20);
    }

    // Active product, inactive SKU, has tag
    @Test(priority = 3)
    public void activeProduct_inactiveSKU_noTag() throws IOException {
        loginAsAdmin();
        createSKU_inactive();
        createProduct_active(sku, "modern");
    }

    // Inactive product, active SKU, has tag
    @Test(priority = 4)
    public void inactiveProduct_activeSKU_hasTag() throws IOException {
        loginAsAdmin();
        createSKU_active();
        createProduct_inactive(sku, "modern");
    }

    // Inactive product, inactive SKU, has tag
    @Test(priority = 5)
    public void inactiveProduct_inactiveSKU_hasTag() throws IOException {
        loginAsAdmin();
        createSKU_inactive();
        createProduct_inactive(sku, "modern");
    }

}
