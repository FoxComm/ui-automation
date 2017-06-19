package testdata.api;

import testdata.TestNGDataProviders;

import java.util.ArrayList;
import java.util.List;

public class Variables extends TestNGDataProviders {

    protected static int customerId;
    protected static String cartId;
    protected static int subTotal;
    protected static int taxes;
    protected static int shipping;
    protected static int adjustments;
    protected static int total;
    protected static int customersExpenses;

    protected static String orderId;
    protected static String jwt;
    protected static String randomId;

    protected static String customerName;
    protected static String customerEmail;
    protected static String guestOrderEmail;
    protected static String takenEmail;
    protected static int addressId1;
    protected static int addressId2;
    protected static String addressPayload;

    protected static String gcCode;
    protected static int scId;
    protected static int shipMethodId;
    protected static int creditCardId;
    protected static List<Integer> creditCardsIDs = new ArrayList<>();
    protected static String stripeToken;

    protected static String promotionId;
    protected static String couponId;
    protected static String couponName;
    protected static String singleCouponCode;
    protected static List<String> bulkCodes = new ArrayList<>();
    protected static String orderAppliedCoupon;

    protected static String skuCode;
    protected static int skuId;
    protected static List<String> skuCodes = new ArrayList<>();
    protected static String skuTitle;
    protected static int skuId_inventory;
    protected static String productTitle;
    protected static String productSlug;
    protected static List<String> products = new ArrayList<>();
    protected static List<String> productSlugs = new ArrayList<>();
    protected static String productId;
    protected static String variantSKU_1;
    protected static String variantSKU_2;

    protected static int searchId;
    protected static String searchRandomId;
    protected static String searchCode;

    protected static int customerGroupId;
    protected static String customerGroupName;
}
