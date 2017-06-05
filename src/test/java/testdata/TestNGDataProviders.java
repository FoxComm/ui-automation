package testdata;

import base.BaseTest;
import org.testng.annotations.DataProvider;

public class TestNGDataProviders extends BaseTest {

    @DataProvider
    public Object[][] parametrizedTest() {
        return new Object[][]{
                {"fail", "fail@fail.com", "fail"},
                {"tenant", "admin@admin.com", "password"}
        };
    }

    @DataProvider
    public Object[][] styledText() {
        return new Object[][] {
                {"p", "Paragraph"},
                {"h1", "Heading One"},
                {"h2", "Heading Two"},
                {"h3", "Heading Three"},
                {"h4", "Heading Four"},
                {"h5", "Heading Five"},
                {"h6", "Heading Six"},
                {"strong", "Bold Text"},
                {"em", "Italic Text"},
                {"ins", "Underlined Text"},
                {"ul", "UL Bullet Point"},
                {"ol", "OL Point"},
                {"ul", "UL Bullet Point"}
        };
    }

    @DataProvider
    public Object[][] entreesSubcategories() {
        return new Object[][] { {"POULTRY"}, {"SEAFOOD"}, {"MEAT"}, {"VEGETARIAN"} };
    }

    @DataProvider
    public Object[][] productCatalogViewDisplayed() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, no sellable stockitems"},
                {"active product, has tag, new SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive product"},
                {"active product, has tag, inactive SKU"}
        };
    }

    @DataProvider
    public Object[][] productCatalogViewNotDisplayed() {
        return new Object[][] {
                {"active product, no tag, active SKU, has sellable stockitems"},
                {"active product, no tag, active SKU, no sellable stockitems"},
                {"inactive product, has tag, active SKU"},
                {"inactive product, has tag, inactive SKU"},
                {"inactive product, has tag, new SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive product"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive product"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive product"},
                {"inactive product, has tag, inactive SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems > product state goes inactive"},
                {"active product, has tag, active SKU, has sellable stockitems > SKU state goes inactive"}
        };
    }

    @DataProvider
    public Object[][] productCanBeSearched() {
        return new Object[][] {
                {"active product, has tag, inactive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, no sellable stockitems"},
                {"active product, no tag, active SKU, has sellable stockitems"},
                {"active product, no tag, active SKU, no sellable stockitems"},
                {"active product, has tag, new SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive product"}
        };
    }

    @DataProvider
    public Object[][] productCannotBeSearched() {
        return new Object[][] {
                {"inactive product, has tag, active SKU"},
                {"inactive product, has tag, inactive SKU"},
                {"inactive product, has tag, new SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive product"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive product"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive product"},
                {"inactive product, has tag, inactive SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems > product state goes inactive"},
                {"active product, has tag, active SKU, has sellable stockitems > SKU state goes inactive"}
        };
    }

    @DataProvider
    public Object[][] canAccessPDP() {
        return new Object[][] {
                {"active product, has tag, inactive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, no sellable stockitems"},
                {"active product, no tag, active SKU, has sellable stockitems"},
                {"active product, no tag, active SKU, no sellable stockitems"},
                {"active product, has tag, new SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive product"},

        };
    }

    @DataProvider
    public Object[][] cannotAccessPDP() {
        return new Object[][] {
                {"inactive product, has tag, active SKU"},
                {"inactive product, has tag, inactive SKU"},
                {"inactive product, has tag, new SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive product"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive product"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive product"},
                {"inactive product, has tag, inactive SKU, has sellable stockitems"}
        };
    }

    @DataProvider
    public Object[][] productCanPassCheckout() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems"},
                {"active product, no tag, active SKU, has sellable stockitems"},
                {"active product, has tag, new SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive product"}
        };
    }

    @DataProvider
    public Object[][] productCannotPassCheckout() {
        return new Object[][] {
                {"active product, has tag, active SKU, no sellable stockitems"},
                {"active product, no tag, active SKU, no sellable stockitems"}
        };
    }

    @DataProvider
    public Object[][] canAddProductToCart_storefront() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems"},
                {"active product, no tag, active SKU, has sellable stockitems"},
                {"active product, has tag, new SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, no sellable stockitems"},
                {"active product, no tag, active SKU, no sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive product"}
        };
    }

    @DataProvider
    public Object[][] canAddProductToCart_admin() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems"},
                {"active product, no tag, active SKU, has sellable stockitems"},
                {"active product, has tag, new SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, no sellable stockitems"},
                {"active product, no tag, active SKU, no sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive product"}
        };
    }

    @DataProvider
    public Object[][] productNotDisplayedLineItemsSearchView() {
        return new Object[][] {
                {"active product, has tag, inactive SKU"},
                {"inactive product, has tag, active SKU"},
                {"inactive product, has tag, inactive SKU"},
                {"inactive product, has tag, new SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive product"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive product"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive product"},
                {"inactive product, has tag, inactive SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems > product state goes inactive"},
                {"active product, has tag, active SKU, has sellable stockitems > SKU state goes inactive"}
        };
    }

    @DataProvider
    public Object[][] canAddSkuToCart_admin() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, no sellable stockitems"},
                {"active product, no tag, active SKU, has sellable stockitems"},
                {"active product, no tag, active SKU, no sellable stockitems"},
                {"active product, has tag, new SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive product"}
        };
    }

    @DataProvider
    public Object[][] skuNotDisplayedLineItemsSearchView() {
        return new Object[][] {
                {"active product, has tag, inactive SKU"},
                {"inactive product, has tag, active SKU"},
                {"inactive product, has tag, inactive SKU"},
                {"inactive product, has tag, new SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems > archive product"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive product"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive product"},
                {"inactive product, has tag, inactive SKU, has sellable stockitems"},
                {"active product, has tag, active SKU, has sellable stockitems > product state goes inactive"},
                {"active product, has tag, active SKU, has sellable stockitems > SKU state goes inactive"}
        };
    }

    @DataProvider
    public Object[][] skuCreatedAlongWithProduct() {
        return new Object[][] {
                {"active product, has tag, new SKU, has sellable stockitems"},
                {"inactive product, has tag, new SKU"}
        };
    }

    @DataProvider
    public Object[][] newSkuInheritsProductState() {
        return new Object[][] {
                {"active product, has tag, new SKU, has sellable stockitems", "active"},
                {"inactive product, has tag, new SKU", "inactive"}
        };
    }

    @DataProvider
    public Object[][] productIsNotArchived() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems > archive SKU"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive product"}
        };
    }

    @DataProvider
    public Object[][] skuIsNotArchived() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems > archive product"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive product"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive product"},
                {"active product, has tag, active SKU, has sellable stockitems, is present in at least 1 cart > archive SKU"}
        };
    }

    @DataProvider
    public Object[][] archivedSkuRemovedFromSkusSearchView() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems > archive SKU"},
                {"active product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive SKU"},
                {"active product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive SKU"},

        };
    }

    @DataProvider
    public Object[][] archivedProductRemovedFromProductsSearchView() {
        return new Object[][] {
                {"active product, has tag, active SKU, has sellable stockitems > archive product"},
                {"еactive product, has tag, active SKU, no sellable stockitems, not present in any carts, has purchases > archive product"},
                {"еactive product, has tag, active SKU, has sellable stockitems, not present in any carts, has purchases > archive product"}
        };
    }


    @DataProvider
    public Object[][] homePageBtns() {
        return new Object[][] {
//                {"btnTitle", "btnRedirectSlug", "expectedUrl"}
                {"Shop Now", "/NEW", storefrontUrl + "/NEW"},
                {"Shop Now", "/SPRING", storefrontUrl + "/SPRING"},
                {"Learn more", "/about", storefrontUrl + "/about"}
        };
    }

    @DataProvider
    public Object[][] stripeTest() {
        return new Object[][] {
//                {"testData", "expPaymentState"}
                {}
        };
    }

}