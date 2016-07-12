package testdata;

import base.BaseTest;
import com.squareup.okhttp.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class DataProvider extends BaseTest {

    protected static int customerId;
    protected static String orderId;
    protected static int orderTotal;
    private static String jwt;

    protected static String customerName;       // stored from viewCustomer()
    protected static String customerEmail;      // stored from viewCustomer()
    private static int addressId1;              // stored from listCustomerAddresses()
    private static int addressId2;              // stored from listCustomerAddresses()
    protected static String gcNumber;           // stored from issueGiftCard()
    private static int scId;                    // stored from issueStoreCredit()
    private static int shipMethodId;            // stored from listShipMethods()
    private static int creditCardId;            // stored from create createCreditCard()

    protected static int promotionId;
    protected static int couponId;
    protected static String couponName;
    private static String singleCouponCode;
    private static List<String> bulkCodes = new ArrayList<>();

    protected static String sku;                 // stored from createSKU_active();
    protected static String productName;         // stored from createProduct_<..>() methods
    protected static int productId;              // stored from createProduct_<..>() methods

    private static void loginAsAdmin() throws IOException {

        System.out.println("Authorizing as an admin...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"email\": \"admin@admin.com\"," +
                "\n    \"password\": \"password\"," +
                "\n    \"kind\": \"admin\"\n}");

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/public/login")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        jwt = response.header("JWT");

        System.out.println(response);
        System.out.println("---- ---- ---- ----");

    }

    private static void createNewCustomer() throws IOException {

        System.out.println("Creating a new customer...");

        String randomID = generateRandomID();
        customerName = "Test Buddy-" + randomID;
        customerEmail = "testbuddy" + randomID + "@mail.com";

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"name\": \"" + customerName + "\"," +
                "\n    \"email\": \"" + customerEmail + "\"\n}");

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/customers")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("authorization", "Basic YWRtaW5AYWRtaW4uY29tOnBhc3N3b3Jr")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        customerId = Integer.valueOf(responseBody.substring(6, responseBody.indexOf(",")));

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Customer ID: " + customerId);
        System.out.println("---- ---- ---- ----");

    }

    private static void createCart(int customerId) throws IOException {

        System.out.println("Creating a cart for customer " + customerId + "...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"customerId\": " + customerId + "\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        orderId = responseBody.substring(20, 27);

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Order ID: <" + orderId + ">");
        System.out.println("---- ---- ---- ----");

    }

//    private static void viewOrder(int orderId) throws IOException {
//
//        System.out.println("Viewing order <" + orderId + ">...");
//
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url("http://admin.stage.foxcommerce.com/api/v1/orders/BR10116")
//                .get()
//                .addHeader("content-type", "application/json")
//                .addHeader("accept", "application/json")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("JWT", jwt)
//                .build();
//
//        Response response = client.newCall(request).execute();
//        JSONObject jsonObject = new JSONObject( response.body().string() );
//        orderTotal = jsonObject.getInt("total");
//
//        System.out.println(response);
//        System.out.println("Order Grand Total: <" + orderTotal + ">");
//        System.out.println("---- ---- ---- ----");
//
//    }

    private static void updSKULineItems(String orderRefNum, String SKU, int quantity) throws IOException {

        System.out.println("Updating SKUs: setting '" + SKU + "' quantity to '" + quantity + "'...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[{\n    \"sku\": \"" + SKU + "\"," +
                "\n    \"quantity\": " + quantity + "\n}]");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderRefNum + "/line-items")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("---- ---- ---- ----");

    }

    private static void viewCustomer(int customerId) throws IOException {

        System.out.println("Getting name and email of customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/customers/" + customerId)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        customerName = responseBody.substring(55, 73);
        customerEmail = responseBody.substring(20, 45);

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Customer name: <" + customerName + ">");
        System.out.println("Customer email: <" + customerEmail + ">");
        System.out.println("---- ---- ---- ----");

    }

    private static void createAddress(int customerId, String name, int regionId, int countryId, String region_name, String address1, String address2, String city, String zip, String phoneNumber, boolean isDefault)
            throws IOException {

        System.out.println("Creating a new address for customer " + customerId + "...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"name\": \"" + name + "\"," +
                "\n    \"regionId\": " + regionId + "," +
                "\n    \"countryId\": " + countryId + "," +
                "\n    \"region_name\": \"" + region_name + "\"," +
                "\n    \"address1\": \"" + address1 + "\"," +
                "\n    \"address2\": \"" + address2 + "\"," +
                "\n    \"city\": \""+ city + "\"," +
                "\n    \"zip\": \"" + zip + "\"," +
                "\n    \"phoneNumber\": \"" + phoneNumber + "\"," +
                "\n    \"isDefault\": " + isDefault + "\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/customers/" + customerId + "/addresses")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("---- ---- ---- ----");

    }

    private static void setShipAddress(String orderId, String name, int regionId, int countryId, String region_name, String address1, String address2, String city, String zip, String phoneNumber, boolean isDefault) throws IOException {

        System.out.println("Adding new shipping address and setting it as a chosen for <" + orderId + "> order...");
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"name\": \"" + name + "\"," +
                "\n  \"regionId\": " + regionId + "," +
                "\n  \"address1\": \"" + address1 + "\"," +
                "\n  \"address2\": \"" + address2 + "\"," +
                "\n  \"city\": \"" + city + "\"," +
                "\n  \"zip\": \"" + zip + "\"," +
                "\n  \"isDefault\": " + isDefault + "," +
                "\n  \"phoneNumber\": \"" + phoneNumber + "\"\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId + "/shipping-address")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("---- ---- ---- ----");

    }

    private static void listCustomerAddresses(int customerId) throws IOException {

        System.out.println("Listing all addresses of customer " + customerId + "...");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/customers/" + customerId + "/addresses")
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();
        addressId1 = Integer.valueOf(responseBody.substring(7, 11));
//        addressId2 = Integer.valueOf(addresses.substring(216, 220));
//        addressId3 = Integer.valueOf(addresses.substring(415, 419));

        System.out.println(response);
        System.out.println("Address 1: <" + addressId1 + ">");
        System.out.println("Address 2: <" + addressId2 + ">");
//        System.out.println("Addres 3: <" + addressId3 + ">");
        System.out.println("---- ---- ---- ----");

    }

    private static void listShipMethods(String orderId) throws IOException {

        System.out.println("Getting possible shipping methods for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/shipping-methods/" + orderId)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();
        String strShipMethodId = responseBody.substring(7, 8);
        shipMethodId = Integer.valueOf(strShipMethodId);

        System.out.println(response);
        System.out.println("shipMethodId: <" + shipMethodId + ">");
        System.out.println("---- ---- ---- ----");

    }

    private static void setShipMethod(String orderId, int shipMethodId) throws IOException {

        System.out.println("Setting shipping method for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"shippingMethodId\": " + shipMethodId + "\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId + "/shipping-method")
                .patch(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);

        System.out.println(response);
        System.out.println("---- ---- ---- ----");

    }

    private static void createCreditCard(String holderName, String cardNumber,String cvv, int expMonth, int expYear, int addressId) throws IOException {

        System.out.println("Create a new credit card for customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"holderName\": \"" + holderName + "\"," +
                "\n  \"cardNumber\": \"" + cardNumber + "\"," +
                "\n  \"cvv\": \"" + cvv + "\"," +
                "\n  \"expMonth\": " + expMonth + "," +
                "\n  \"expYear\": " + expYear + "," +
                "\n  \"addressId\": " + addressId + "," +
                "\n  \"isDefault\": false\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/customers/" + customerId + "/payment-methods/credit-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        System.out.println(response);
//        System.out.println(responseBody);
        creditCardId = Integer.valueOf(responseBody.substring(6, 10));
        System.out.println("Credit Card ID: <" + responseBody.substring(6, 10) + ">");
        System.out.println("---- ---- ---- ----");

    }

    private static void setPayment_creditCard(String orderId, int creditCardId) throws IOException {

        System.out.println("Setting credit card with id <" + creditCardId + "> as a payment method for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"creditCardId\": " + creditCardId + "\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId + "/payment-methods/credit-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
        System.out.println("---- ---- ---- ----");

    }

    private static void issueStoreCredit(int customerId, int amount) throws IOException {

        System.out.println("Issuing store credit for customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"amount\": " + amount + "," +
                "\n  \"reasonId\": 1," +
                "\n  \"subReasonId\": 1," +
                "\n  \"currency\": \"USD\"\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/customers/" + customerId + "/payment-methods/store-credit")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        scId = Integer.valueOf(responseBody.substring(6, 10));

        System.out.println(response);
        System.out.println("Store Credit ID: <" + scId + ">...");
        System.out.println("--------");

    }

    private static void issueStoreCredit_gcTransfer(int customerId, String gcNumber) throws IOException {

        System.out.println("Transferring GC <" + gcNumber + "to SC for customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"reasonId\":3," +
                "\n  \"code\":\"CF13858F49E16CE7\"," +
                "\n  \"subReasonId\":null\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/gift-cards/" + gcNumber + "/convert/" + customerId)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        scId = Integer.valueOf(responseBody.substring(6, 10));

        System.out.println(response);
        System.out.println("Store Credit ID: <" + scId + ">...");
        System.out.println("--------");


    }

    protected static void setPayment_storeCredit(String orderId, int amount) throws IOException {

        System.out.println("Setting store credit in amount of <" + amount + "> as a payment for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"amount\": " + amount + "\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId + "/payment-methods/store-credit")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
        System.out.println("---- ---- ---- ----");

    }

    private static void issueGiftCard(int balance, int quantity) throws IOException {

        System.out.println("Creating new gift card...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"balance\": " + balance + "," +
                "\n  \"quantity\": " + quantity + "," +
                "\n  \"reasonId\": 1\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/gift-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        gcNumber = responseBody.substring(86, 102);

        System.out.println(response);
        System.out.println("GC code: <" + gcNumber + ">");
        System.out.println("---- ---- ---- ----");

    }

    protected static void setPayment_giftCard(String orderId, String gcNumber, int amount) throws IOException {

        System.out.println("Setting gict card <"+ gcNumber + "> in amount of <" + amount + "> as a payment for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"code\": \"" + gcNumber + "\"," +
                "\n  \"amount\": " + amount + "}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId + "/payment-methods/gift-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
        System.out.println("---- ---- ---- ----");

    }

    private static void checkoutOrder(String orderId) throws IOException {

        System.out.println("Checking out order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId + "/checkout")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("---- ---- ---- ----");

    }

    private static void changeOrderState(String orderId, String newState) throws IOException {

        System.out.println("Change state of order <" + orderId + "> to <" + newState + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"state\":\"" + newState + "\"\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId)
                .patch(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
        System.out.println("---- ---- ---- ----");

    }

    private static void createPromotion_coupon() throws IOException {

        System.out.println("Creating a new promotion...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"applyType\":\"coupon\",\"form\":{\"id\":248,\"attributes\":{\"eaa8440703\":\"new promo " + generateRandomID() + "\",\"25e24d5d0f\":\"<p>new promo</p>\"},\"discounts\":[{\"id\":249,\"attributes\":{\"bb0b82afad\":{\"orderAny\":{}},\"3db8e5c670\":{\"orderPercentOff\":{\"discount\":10}}},\"createdAt\":\"2016-06-27T22:27:43.938Z\"}],\"createdAt\":\"2016-06-27T22:27:43.915Z\"},\"shadow\":{\"id\":303,\"formId\":248,\"attributes\":{\"name\":{\"type\":\"string\",\"ref\":\"eaa8440703\"},\"storefrontName\":{\"type\":\"richText\",\"ref\":\"25e24d5d0f\"},\"description\":{\"type\":\"richText\",\"ref\":\"eaa8440703\"},\"details\":{\"type\":\"richText\",\"ref\":\"25e24d5d0f\"}},\"discounts\":[{\"id\":249,\"attributes\":{\"qualifier\":{\"type\":\"qualifier\",\"ref\":\"bb0b82afad\"},\"offer\":{\"type\":\"offer\",\"ref\":\"3db8e5c670\"}},\"createdAt\":\"2016-06-27T22:27:43.938Z\"}],\"createdAt\":\"2016-06-27T22:27:43.915Z\"}}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/promotions/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        promotionId = Integer.valueOf(responseBody.substring(35, 38));

        System.out.println(response);
//        System.out.println(responseBody);
        System.out.println("Promotion ID: " + promotionId);
        System.out.println("---- ---- ---- ----");

    }

    private static void createCoupon(int promotionId) throws IOException {

        System.out.println("Creating a new coupon with promotion <" + promotionId + ">...");
        String randomId = generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"form\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"usageRules\":{\"isExclusive\":false,\"isUnlimitedPerCode\":false,\"usesPerCode\":1,\"isUnlimitedPerCustomer\":false,\"usesPerCustomer\":1},\"name\":\"test coupon " + randomId + "\",\"storefrontName\":\"storefront name " + randomId + "\",\"description\":\"test description\",\"details\":\"test details\",\"activeFrom\":\"2016-07-01T21:31:56.701+00:00\",\"activeTo\":null}},\"shadow\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"usageRules\":{\"type\":\"usageRules\",\"ref\":\"usageRules\"},\"name\":{\"type\":\"string\",\"ref\":\"name\"},\"storefrontName\":{\"type\":\"richText\",\"ref\":\"storefrontName\"},\"description\":{\"type\":\"richText\",\"ref\":\"description\"},\"details\":{\"type\":\"richText\",\"ref\":\"details\"},\"activeFrom\":{\"type\":\"2016-07-01T21:31:56.701+00:00\",\"ref\":\"activeFrom\"},\"activeTo\":{\"type\":null,\"ref\":\"activeTo\"}}},\"promotion\":" + promotionId + "}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/coupons/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        couponId = Integer.valueOf(responseBody.substring(6, 9));
        couponName = "test coupon " + randomId;

        System.out.println(response);
//        System.out.println(responseBody);
        System.out.println("Coupon ID: " + couponId);
        System.out.println("---- ---- ---- ----");

    }

    private static void generateSingleCode(int couponId) throws IOException {

        System.out.println("Generating a single code for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/coupons/codes/generate/" + couponId + "/NWCPN-" + generateRandomID())
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        singleCouponCode = responseBody.substring(1, responseBody.length() - 1);

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Single coupon code: " + singleCouponCode);
        System.out.println("---- ---- ---- ----");

    }

    private static void bulkGenerateCodes(int couponId, String prefix, int codeLength, int quantity) throws IOException {

        int length = prefix.length() + codeLength;

        System.out.println("Bulk generating coupon codes for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"prefix\":\"" + prefix + "\"," +
                "\n    \"length\":" + length + "," +
                "\n    \"quantity\":" + quantity + "\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/coupons/codes/generate/" + couponId)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        int startIndex = 2;
        for (int i = 0; i < quantity; i++) {
            String code = responseBody.substring(startIndex, startIndex + length);
            bulkCodes.add(code);
            startIndex += (length + 3);
        }
        System.out.println("Number of codes generated: <" + bulkCodes.size() + ">");
        assertEquals(bulkCodes.size(), quantity,
                "Amount of generated codes is lower than requested amount.");
        printStringList(bulkCodes);

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("--------");

    }

    private static void applyCouponCode(String orderId, String couponCode) throws IOException {

        System.out.println("Applying coupon code <" + couponCode + "> to order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId + "/coupon/" + couponCode)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("---- ---- ---- ----");

    }

    private static void updateSCState(int scId, String state) throws IOException {

        System.out.println("Updating state of store credit with Id <" + scId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"state\": \"" + state + "\"," +
                "\n  \"reasonId\": 1\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/store-credits/" + scId)
                .patch(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Update state: <" + state + ">...");
        System.out.println("--------");

    }

    private static void createSKU_active() throws IOException {

        System.out.println("Creating a new SKU, options: ACTIVE state...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product " + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"activeFrom\":{\"v\":\"2016-07-04T17:43:44.295+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"THATS A TITLE OVER HERE\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Test description</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"activeFrom\":{\"v\":\"2016-07-05T22:19:54.918+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/skus/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        sku = skuCode;
        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("SKU code: <" + skuCode + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createSKU_inactive() throws IOException {

        System.out.println("Creating a new SKU, options: INACTIVE state...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}}}}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/skus/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        sku = skuCode;
        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("SKU code: <" + skuCode + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createSKU_noTitle() throws IOException {

        System.out.println("Creating a new SKU, options: no title...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"1215\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"1215\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"0\"}},\"activeFrom\":{\"v\":\"2016-07-04T18:25:59.745+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/skus/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        sku = skuCode;
        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("SKU code: <" + skuCode + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createSKU_noDescription() throws IOException {

        System.out.println("Creating a new SKU, options: no description...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"1215\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"1215\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"0\"}},\"activeFrom\":{\"v\":\"2016-07-04T18:25:59.745+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/skus/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        sku = skuCode;
        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("SKU code: <" + skuCode + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createSKU_noPrices() throws IOException {

        System.out.println("Creating a new SKU, options: all prices equals <0>...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"0\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"0\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"0\"}},\"activeFrom\":{\"v\":\"2016-07-04T18:25:59.745+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/skus/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        sku = skuCode;
        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("SKU code: <" + skuCode + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createProduct_noSKU_active() throws IOException {

        System.out.println("Creating a new product... No SKU code is provided, so a new one will be created.");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\":{\"v\":\"2016-07-04T17:22:44.388+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"F0QGTGBBINBQF5V53TYB9\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"SKU-TST\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        productName = productName_local;
        productId = Integer.valueOf(responseBody.substring(6, 9));

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Product ID: <" + productId + ">.");
        System.out.println("Product name: <" + productName + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createProduct_active(String sku, String tag) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"tags\":{\"t\":\"tags\",\"v\":[\"" + tag + "\"]},\"activeFrom\":{\"v\":\"2016-07-06T17:39:45.277+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"1PZ1Z6FEB42BFOCYJH5MI\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        productId = Integer.valueOf(responseBody.substring(6, 9));
        productName = productName_local;

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Product ID: <" + productId + ">.");
        System.out.println("Product name: <" + productName + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createProduct_active_noTag(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\":{\"v\":\"2016-07-04T17:22:44.388+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"F0QGTGBBINBQF5V53TYB9\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        productName = productName_local;
        productId = Integer.valueOf(responseBody.substring(6, 9));

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Product ID: <" + productId + ">.");
        System.out.println("Product name: <" + productName + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createProduct_inactive(String sku, String tag) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"tags\":{\"t\":\"tags\",\"v\":[\"" + tag + "\"]}},\"skus\":[{\"feCode\":\"JBV96IF5QRNZM9KQ33DI\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        productName = productName_local;
        productId = Integer.valueOf(responseBody.substring(6, 9));

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Product ID: <" + productId + ">.");
        System.out.println("Product name: <" + productName + ">.");
        System.out.println("---- ---- ---- ----");

    }

    private static void createProduct_inactive_noTag(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"}},\"skus\":[{\"feCode\":\"KB0SOK5PSSBEPP5H4CXR\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        productName = productName_local;
        productId = Integer.valueOf(responseBody.substring(6, 9));

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Product ID: <" + productId + ">.");
        System.out.println("Product name: <" + productName + ">.");
        System.out.println("---- ---- ---- ----");

    }




    protected void provideTestData(String testMethodName) throws IOException {

        loginAsAdmin();

        switch(testMethodName) {

            //------------------------------------- SEARCH FILTERS ------------------------------------//

            case "a customer":
                createNewCustomer();
                break;

            //----------------------------------------- ITEMS -----------------------------------------//

            case "empty cart":
                createNewCustomer();
                createCart(customerId);
                break;

            case "cart with 1 item":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-TRL", 1);
                break;

            case "cart with 3 items":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-TRL", 3);
                updSKULineItems(orderId, "SKU-BRO", 2);
                updSKULineItems(orderId, "SKU-ZYA", 4);
                break;

            case "cart with 1 item, qty: 3":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-TRL", 3);
                break;

            //------------------------------------- SHIPPING ADDRESS -------------------------------------//

            case "cart with empty address book":
                createNewCustomer();
                createCart(customerId);
                break;

            case "cart with non-empty address book":
                createNewCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with chosen shipping address":
                createNewCustomer();
                createCart(customerId);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 2 addresses in address book":
                createNewCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4161, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            case "cart with 2 addresses and defined default shipping address":
                createNewCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4161, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            //----------------------------------- ORDERS WITH COUPONS ------------------------------------//

//            case "a cart && a single code coupon":
//                createCart(customerId);
//                createPromotion_coupon();
//                createCoupon(promotionId);
//                generateSingleCode(couponId);
//                break;

            case "a cart with a single code coupon applied":
                createCart(customerId);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                applyCouponCode(orderId, singleCouponCode);
                break;
//
//            case "a cart && coupon with bulk generated codes":
//                createCart(customerId);
//                createPromotion_coupon();
//                createCoupon(promotionId);
//                bulkGenerateCodes(couponId);
//                applyCouponCode(bulkCodes.get(0));
//                break;

            case "a cart with a bulk generated code applied":
                createCart(customerId);
                createPromotion_coupon();
                createCoupon(promotionId);
                bulkGenerateCodes(couponId, "BLK_NWCPN-", 5, 3);
                applyCouponCode(orderId, bulkCodes.get(0));
                break;

            //------------------------------------- PAYMENT METHOD -------------------------------------//

            case "cart with 1 item and chosen shipping address":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-BRO", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 1 item && customer with GC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-BRO", 1);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item && customer with SC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-BRO", 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method, and credit card payment":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                createCreditCard(customerName, "5555555555554444", "777", 3, 2020, addressId1);
                setPayment_creditCard(orderId, creditCardId);
                break;

            case "cart with 1 item, shipping method and issued SC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method and issued GC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item, shipping method, credit card payment and issued SC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                createCreditCard(customerName, "5555555555554444", "777", 3, 2020, addressId1);
                setPayment_creditCard(orderId, creditCardId);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method, credit card payment and issued GC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                createCreditCard(customerName, "5555555555554444", "777", 3, 2020, addressId1);
                setPayment_creditCard(orderId, creditCardId);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item, shipping method, issued SC and GC":
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item && SC onHold":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "onHold");
                break;

            case "cart with 1 item && SC canceled":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "canceled");
                break;

            //------------------------------------- ORDER STATE -------------------------------------//

            case "order in remorse hold":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                createCreditCard(customerName, "5555555555554444", "777", 3, 2020, addressId1);
                setPayment_creditCard(orderId, creditCardId);
                checkoutOrder(orderId);
                break;

            //--------------------------------- CUSTOMER ADDRESS BOOK ---------------------------------//

            case "customer with a shipping address":
                createNewCustomer();
                createAddress(customerId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            //--------------------------------- CUSTOMER CREDIT CARDS ---------------------------------//

            case "customer with a credit card":
                createNewCustomer();
                createAddress(customerId, customerName, 4161, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", false);
                listCustomerAddresses(customerId);
                createCreditCard("John Doe", "5555555555554444", "999", 4, 2020, addressId1);
                break;

            case "customer with a credit card and 2 addresses":
                createNewCustomer();
                createAddress(customerId, customerName, 4161, 234, "New York", "545 Narrow Ave", "Suite 15", "New Jersey", "10201", "5551118888", false);
                createAddress(customerId, customerName, 4161, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", false);
                listCustomerAddresses(customerId);
                createCreditCard("John Doe", "5555555555554444", "999", 4, 2020, addressId1);
                break;

            //----------------------------------- CUSTOMER'S ORDERS -----------------------------------//

            case "customer with 2 orders in remorse hold":

                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                createCreditCard(customerName, "5555555555554444", "777", 3, 2020, addressId1);
                setPayment_creditCard(orderId, creditCardId);
                checkoutOrder(orderId);

                createCart(customerId);
                updSKULineItems(orderId, "SKU-BRO", 2);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_creditCard(orderId, creditCardId);
                checkoutOrder(orderId);

                break;

            case "customer with 2 orders in remorse hold and fulfillment started":

                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                createCreditCard(customerName, "5555555555554444", "777", 3, 2020, addressId1);
                setPayment_creditCard(orderId, creditCardId);
                checkoutOrder(orderId);

                createCart(customerId);
                updSKULineItems(orderId, "SKU-BRO", 2);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_creditCard(orderId, creditCardId);
                checkoutOrder(orderId);

                changeOrderState(orderId, "fulfillmentStarted");
                break;

            //----------------------------------- STORE CREDITS -----------------------------------//

            case "a customer with issued SC":
                createNewCustomer();
                issueStoreCredit(customerId, 50000);
                break;

            case "a customer && GC":
                createNewCustomer();
                issueGiftCard(12500, 1);
                break;

            case "order in Remorse Hold, payed with SC (CSR Appeasement)":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(orderId, 3727);
                checkoutOrder(orderId);
                break;

            case "order in Remorse Hold, payed with SC (GC Transfer)":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit_gcTransfer(customerId, gcNumber);
                setPayment_storeCredit(orderId, 3727);
                checkoutOrder(orderId);
                break;

            //----------------------------------- COUPONS -----------------------------------//

            case "a promotion":
                createPromotion_coupon();
                break;

            case "coupon with single code":
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "coupon with bulk generated codes":
                createPromotion_coupon();
                createCoupon(promotionId);
                bulkGenerateCodes(couponId, "BLKNWCPN" + couponId + "-", 4, 5);
                break;


            case "order in remorse hold with applied coupon":
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);

                createNewCustomer();
                createCart(customerId);
                updSKULineItems(orderId, "SKU-YAX", 1);
                setShipAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(orderId);
                setShipMethod(orderId, shipMethodId);
                listCustomerAddresses(customerId);
                createCreditCard(customerName, "5555555555554444", "777", 3, 2020, addressId1);
                setPayment_creditCard(orderId, creditCardId);
                applyCouponCode(orderId, singleCouponCode);
                checkoutOrder(orderId);
                break;

            //----------------------------------- PRODUCTS -----------------------------------//

            case "product in active state":
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                break;

            case "active SKU":
                createSKU_active();
                break;

            case "inactive SKU":
                createSKU_inactive();
                break;

            case "SKU with no title":
                createSKU_noTitle();
                break;

            case "SKU with no description":
                createSKU_noDescription();
                break;

            case "SKU with no prices set":
                createSKU_noPrices();
                break;

            case "product in active state, active SKU and tag":
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                break;

            //----------------
            case "active product, has tag, active SKU":
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                break;

            case "active product, no tag, active SKU":
                createSKU_active();
                createProduct_active_noTag(sku);
                break;
            case "inactive product, has tag, active SKU":
                createSKU_active();
                createProduct_inactive(sku, "sunglasses");
                break;

            case "inactive product, no tag, active SKU":
                createSKU_active();
                createProduct_inactive_noTag(sku);
                break;

            case "active product, has tag, inactive SKU":
                createSKU_inactive();
                createProduct_active(sku, "sunglasses");
                break;

            case "active product, no tag, inactive SKU":
                createSKU_inactive();
                createProduct_active_noTag(sku);
                break;

            case "inactive product, has tag, inactive SKU":
                createSKU_inactive();
                createProduct_inactive(sku, "sunglasses");
                break;

            case "inactive product, no tag, inactive SKU":
                createSKU_inactive();
                createProduct_inactive_noTag(sku);
                break;

        }

    }

}