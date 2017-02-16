import base.BasePage;
import base.BaseTest;
import com.codeborne.selenide.Condition;
import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import pages.SkusPage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;

public class TestClass extends BaseTest {

//    protected static double cutDecimal(double numb) {
//
//        DecimalFormat cutDecimals = new DecimalFormat(("#.##"));
//        return Double.valueOf(cutDecimals.format(numb));
//
//    }

    private SkusPage p;

    private static int customerId;
    private static String orderId;
    private static int orderTotal;
    private static String jwt;

    private static String customerName;     // stored from viewCustomer()
    private static String customerEmail;    // stored from viewCustomer()
    private static int addressId1;          // stored from listCustomerAddresses()
    private static int addressId2;          // stored from listCustomerAddresses()
    private static String addressPayload;   // stored from listCustomerAddresses()
    private static String gcCode;         // stored from issueGiftCard()
    private static int scId;                // stored from issueStoreCredit()
    private static int shipMethodId;        // stored from listShipMethods()
    private static int creditCardId;        // stored from createCreditCard()

    private static String promotionId;
    private static String couponId;
    protected static String couponName;
    private static String singleCouponCode;
    private static List<String> bulkCodes = new ArrayList<>();

    private static String sku;                      // stored from createSKU();
    protected static int skuId_inventory;                     // stored from viewSKU();
    protected static String productId;              // stored from createProduct_<..>() methods
    protected static String productName;            // stored from createProduct_<..>() methods

    private static int searchId;                    // stored from createSharedSearch() methods
    private static String searchCode;               // stored from createSharedSearch() methods
    protected static int adminId;                   // stored from getAdminId() method

    private static void failTest(String responseBody, int responseCode, String responseMsg) throws IOException {
        System.out.println("Response: " + responseCode + " " + responseMsg);
        System.out.println(responseBody);
        System.out.println("--------");
        assertEquals(responseCode, 200, "API call failed to succeed.");
    }

    private static void loginAsAdmin() throws IOException {

        System.out.println("Authorizing as an admin...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"email\": \"admin@admin.com\"," +
                "\n    \"password\": \"password\"," +
                "\n    \"kind\": \"admin\"\n}");

        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/public/login")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            jwt = response.header("JWT");
            System.out.println("--------");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    private static void createNewCustomer() throws IOException {

        System.out.println("Creating a new customer...");

        String randomID = generateRandomID();
        customerName = "Test Customer-" + randomID;
        customerEmail = "testcustomer" + randomID;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"name\": \"" + customerName + "\"," +
                "\n    \"email\": \"" + customerEmail + "@mail.com\"\n}");

        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/customers")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("authorization", "Basic YWRtaW5AYWRtaW4uY29tOnBhc3N3b3Jr")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        customerId = Integer.valueOf(responseBody.substring(6, responseBody.indexOf(",")));

        System.out.println(response);
        System.out.println("Customer ID: " + customerId);
        System.out.println("--------");

    }

    private static void createCart(int customerId) throws IOException {

        System.out.println("Creating a cart for customer " + customerId + "...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"customerId\": " + customerId + "\n}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders")
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
        System.out.println("--------");

    }

    private static void viewOrder(String orderId) throws IOException {

        System.out.println("Viewing order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders/" + orderId)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
//        JSONObject reponseBodyObj = new JSONObject( response.body().string() );
//        String totals = reponseBodyObj.getString("totals");
//        JSONObject totalsObj = new JSONObject(totals);
//        orderTotal = totalsObj.getInt("total");

        System.out.println(response);
        System.out.println("Order Grand Total: <" + orderTotal + ">");
        System.out.println("---- ---- ---- ----");

    }

    private static void updSKULineItems(String orderRefNum, String SKU, int quantity) throws IOException {

        System.out.println("Updating SKUs: setting '" + SKU + "' quantity to '" + quantity + "'...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[{\n    \"sku\": \"" + SKU + "\"," +
                "\n    \"quantity\": " + quantity + "\n}]");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders/" + orderRefNum + "/line-items")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response);
        System.out.println("--------");

    }

    private static void viewCustomer(int customerId) throws IOException {

        System.out.println("Getting name and email of customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/customers/" + customerId)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        customerName = responseBody.substring(57, 76);
        customerEmail = responseBody.substring(20, 47);

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Customer name: <" + customerName + ">");
        System.out.println("Customer email: <" + customerEmail + ">");
        System.out.println("--------");

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
                .url(adminUrl + "/api/v1/customers/" + customerId + "/addresses")
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
        System.out.println("--------");

    }

    private static void setShipAddress(String orderId, String name, int regionId, int countryId, String region_name, String address1, String address2, String city, String zip, String phoneNumber, boolean isDefault) throws IOException {

        System.out.println("Adding new shipping address and setting it as a chosen for " + orderId + " order...");
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
                .url(adminUrl + "/api/v1/orders/" + orderId + "/shipping-address")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response);
        System.out.println("--------");

    }

    private static void listCustomerAddresses(int customerId) throws IOException {

        System.out.println("Listing all addresses of customer " + customerId + "...");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/customers/" + customerId + "/addresses")
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONArray jsonData = new JSONArray(responseBody);
            addressId1 = jsonData.getJSONObject(0).getInt("id");
//            addressId1 = Integer.valueOf(responseBody.substring(7, responseBody.indexOf(",", 7)));
            System.out.println("Address 1: <" + addressId1 + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    private static void listShipMethods(String orderId) throws IOException {

        System.out.println("Getting possible shipping methods for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/shipping-methods/" + orderId)
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
        System.out.println("--------");

    }

    private static void setShipMethod(String orderId, int shipMethodId) throws IOException {

        System.out.println("Setting shipping method for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"shippingMethodId\": " + shipMethodId + "\n}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders/" + orderId + "/shipping-method")
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
        System.out.println("--------");

    }

    private static void getCustomerAddress(int customerId, int addressId) throws IOException {

        System.out.println("Getting address with ID <" + addressId + "> of customer <" + customerId + ">...");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/customers/" + customerId + "/addresses/" + addressId)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
//            addressId1 = Integer.valueOf(responseBody.substring(7, responseBody.indexOf(",", 7)));
            addressPayload = responseBody;
            System.out.println("Address 1: <" + addressId1 + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }



    private static void createCreditCard(String holderName, String lastFour, int expMonth, int expYear, String brand) throws IOException {

        System.out.println("Create a new credit card for customer <" + customerId + ">...");
        JSONObject jsonObj = new JSONObject(addressPayload);
        JSONObject region = jsonObj.getJSONObject("region");

//        System.out.println("{" +
//                "\n  \"token\":\"tok_18t2CWAVdiXyWQ8c4PnlJZAJ\"," +
//                "\n  \"holderName\":\"" + holderName + "\"," +
//                "\n  \"lastFour\":\"" + lastFour + "\"," +
//                "\n  \"expMonth\":" + expMonth + "," +
//                "\n  \"expYear\":" + expYear + "," +
//                "\n  \"brand\":\"" + brand + "\"," +
//                "\n  \"addressIsNew\":false," +
//                "\n  \"billingAddress\":" + addressPayload.substring(0, addressPayload.length() - 1) + "," +
//                "\n  \"regionId\":" + region.getInt("id") + "," +
//                "\n  \"state\":\"" + region.getString("name") + "\"," +
//                "\n  \"country\":\"United States\"}}");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"token\":\"tok_18t2CWAVdiXyWQ8c4PnlJZAJ\"," +
                "\n  \"holderName\":\"" + holderName + "\"," +
                "\n  \"lastFour\":\"" + lastFour + "\"," +
                "\n  \"expMonth\":" + expMonth + "," +
                "\n  \"expYear\":" + expYear + "," +
                "\n  \"brand\":\"" + brand + "\"," +
                "\n  \"addressIsNew\":false," +
                "\n  \"billingAddress\":" + addressPayload.substring(0, addressPayload.length() - 1) + "," +
                "\n  \"regionId\":" + region.getInt("id") + "," +
                "\n  \"state\":\"" + region.getString("name") + "\"," +
                "\n  \"country\":\"United States\"}}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/customers/" + customerId + "/payment-methods/credit-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            org.json.JSONObject jsonData = new org.json.JSONObject(responseBody);
            creditCardId = jsonData.getInt("id");
            System.out.println("Credit Card ID: <" + creditCardId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    private static void setPayment_creditCard(String orderId, int creditCardId) throws IOException {

        System.out.println("Setting credit card with id <" + creditCardId + "> as a payment method for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"creditCardId\": " + creditCardId + "\n}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders/" + orderId + "/payment-methods/credit-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
        System.out.println("--------");

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
                .url(adminUrl + "/api/v1/customers/" + customerId + "/payment-methods/store-credit")
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
                .url(adminUrl + "/api/v1/orders/" + orderId + "/payment-methods/store-credit")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
        System.out.println("--------");

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
                .url(adminUrl + "/api/v1/gift-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONArray jsonData = new JSONArray(responseBody);
            JSONObject gcInfo = jsonData.getJSONObject(0).getJSONObject("giftCard");
            gcCode = gcInfo.getString("code");
//            gcCode = responseBody.substring(86, 102);
            System.out.println("GC code: <" + gcCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    protected static void setPayment_giftCard(String orderId, String gcNumber, int amount) throws IOException {

        System.out.println("Setting gift card <"+ gcNumber + "> in amount of <" + amount + "> as a payment for order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"code\": \"" + gcNumber + "\"," +
                "\n  \"amount\": " + amount + "}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders/" + orderId + "/payment-methods/gift-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
        System.out.println("--------");

    }

    private static void checkoutOrder(String orderId) throws IOException {

        System.out.println("Checking out order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders/" + orderId + "/checkout")
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
        System.out.println("--------");

    }

    private static void changeOrderState(String orderId, String newState) throws IOException {

        System.out.println("Change state of order <" + orderId + "> to <" + newState + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"state\":\"" + newState + "\"\n}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders/" + orderId)
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
        System.out.println("--------");

    }

    private static void createPromotion_coupon() throws IOException {

        System.out.println("Creating a new promotion...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"applyType\": \"coupon\",\n  \"form\": {\n    \"id\": 243,\n    \"attributes\": {\n      \"eaa8440703\": \"new promo\",\n      \"25e24d5d0f\": \"<p>new promo</p>\"\n    },\n    \"discounts\": [\n      {\n        \"id\": 244,\n        \"attributes\": {\n          \"bb0b82afad\": {\n            \"orderAny\": {}\n          },\n          \"3db8e5c670\": {\n            \"orderPercentOff\": {\n              \"discount\": 10\n            }\n          }\n        },\n        \"createdAt\": \"2016-06-27T21:17:32.671Z\"\n      }\n    ],\n    \"createdAt\": \"2016-06-27T21:17:32.660Z\"\n  },\n  \"shadow\": {\n    \"id\": 298,\n    \"formId\": 243,\n    \"attributes\": {\n      \"name\": {\n        \"type\": \"string\",\n        \"ref\": \"eaa8440703\"\n      },\n      \"storefrontName\": {\n        \"type\": \"richText\",\n        \"ref\": \"25e24d5d0f\"\n      },\n      \"description\": {\n        \"type\": \"richText\",\n        \"ref\": \"eaa8440703\"\n      },\n      \"details\": {\n        \"type\": \"richText\",\n        \"ref\": \"25e24d5d0f\"\n      }\n    },\n    \"discounts\": [\n      {\n        \"id\": 244,\n        \"attributes\": {\n          \"qualifier\": {\n            \"type\": \"qualifier\",\n            \"ref\": \"bb0b82afad\"\n          },\n          \"offer\": {\n            \"type\": \"offer\",\n            \"ref\": \"3db8e5c670\"\n          }\n        },\n        \"createdAt\": \"2016-06-27T21:17:32.671Z\"\n      }\n    ],\n    \"createdAt\": \"2016-06-27T21:17:32.660Z\"\n  }\n}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/promotions/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        promotionId = responseBody.substring(35, 38);

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("--------");

    }

    private static void createCoupon(String promotionId) throws IOException {

        System.out.println("Creating a new coupon with promotion <" + promotionId + ">...");
        String randomId = generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"form\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"usageRules\":{\"isExclusive\":false,\"isUnlimitedPerCode\":false,\"usesPerCode\":1,\"isUnlimitedPerCustomer\":false,\"usesPerCustomer\":1},\"name\":\"test coupon " + randomId + "\",\"storefrontName\":\"storefront name " + randomId + "\",\"description\":\"<p>test description</p>\",\"details\":\"<p>test details</p>\",\"storefront Name\":\"<p>storefront name 77777</p>\",\"activeFrom\":\"2016-07-30T18:59:10.402Z\",\"activeTo\":null}},\"shadow\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"usageRules\":{\"type\":\"usageRules\",\"ref\":\"usageRules\"},\"name\":{\"type\":\"string\",\"ref\":\"name\"},\"storefrontName\":{\"type\":\"richText\",\"ref\":\"storefrontName\"},\"description\":{\"type\":\"richText\",\"ref\":\"description\"},\"details\":{\"type\":\"richText\",\"ref\":\"details\"},\"storefront Name\":{\"type\":\"richText\",\"ref\":\"storefront Name\"},\"activeFrom\":{\"type\":\"2016-07-30T18:59:10.402Z\",\"ref\":\"activeFrom\"},\"activeTo\":{\"type\":null,\"ref\":\"activeTo\"}}},\"promotion\":" + promotionId + "}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/coupons/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONObject jsonData = new JSONObject(responseBody);
            couponId = String.valueOf(jsonData.getInt("id"));
//            couponId = responseBody.substring(6, responseBody.indexOf(",", 6));
            couponName = "test coupon " + randomId;
            System.out.println("Coupon ID: " + couponId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    private static void generateSingleCode(int couponId) throws IOException {

        System.out.println("Generating a single code for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/coupons/codes/generate/" + couponId + "/newcpn-" + generateRandomID())
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
        System.out.println("--------");

    }

    private static void bulkGenerateCodes(String couponId, String prefix, int codeLength, int quantity) throws IOException {

        int length = prefix.length() + codeLength;

        System.out.println("Bulk generating coupon codes for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"prefix\":\"" + prefix + "\"," +
                "\n    \"length\":" + length + "," +
                "\n    \"quantity\":" + quantity + "\n}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/coupons/codes/generate/" + couponId)
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
//            int startIndex = 2;
//            for (int i = 0; i < quantity; i++) {
//                String code = responseBody.substring(startIndex, startIndex + length);
//                bulkCodes.add(code);
//                startIndex += (length + 3);
//            }
            JSONArray jsonData = new JSONArray(responseBody);
            for (int i = 0; i < jsonData.length(); i++) {
                bulkCodes.add(jsonData.getString(i));
            }
            System.out.println("Number of codes generated: <" + bulkCodes.size() + ">");
            assertEquals(bulkCodes.size(), quantity,
                    "Amount of generated codes is lower than requested amount.");
            printStringList(bulkCodes);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    private static void applyCouponCode(String orderId, String couponCode) throws IOException {

        System.out.println("Applying coupon code <" + couponCode + "> to order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/orders/" + orderId + "/coupon/" + couponCode)
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
        System.out.println("--------");

    }

    private static void updateSCState(int scId, String state) throws IOException {

        System.out.println("Updating state of store credit with Id <" + scId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n  \"state\": \"" + state + "\"," +
                "\n  \"reasonId\": 1\n}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/store-credits/" + scId)
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

    private static void createSKU() throws IOException {

        System.out.println("Creating a new SKU...");
        String skuCode = "SKU-#" + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"activeFrom\":{\"v\":\"2016-07-04T17:43:44.295+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/skus/default")
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
        System.out.println("--------");

    }

    private static void createProduct_noSKU() throws IOException {

        System.out.println("Creating a new product... No SKU code is provided, so a new one will be created.");
        String productName = "Test Product #" + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName + "\"},\"activeFrom\":{\"v\":\"2016-07-04T17:22:44.388+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"F0QGTGBBINBQF5V53TYB9\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"SKU-TST\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"27.18\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"27.18\"}}}}]}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        productId = responseBody.substring(6, 9);

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println("Product name: <" + productName + ">.");
        System.out.println("Product ID: <" + productId + ">.");
        System.out.println("--------");

    }

    private static void createProduct(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName = "Test Product #" + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName + "\"},\"activeFrom\":{\"v\":\"2016-07-04T17:22:44.388+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"F0QGTGBBINBQF5V53TYB9\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"27.18\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"27.18\"}}}}]}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/products/default")
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
        System.out.println("Product name: <" + productName + ">.");
        System.out.println("--------");


    }

    private static void createSKU_active() throws IOException {

        System.out.println("Creating a new SKU, options: ACTIVE state...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product " + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2000\"}},\"activeFrom\":{\"v\":\"2016-07-04T17:43:44.295+00:00\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"A TITLE\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Test description</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"" + getDate() + "T00:03:26.685Z\"},\"activeTo\":{\"t\":\"datetime\",\"v\":null}}}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/skus/default")
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

    private static void createProduct_active(String sku, String tag) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"tags\":{\"t\":\"tags\",\"v\":[\"" + tag + "\"]},\"activeFrom\": {\"t\": \"date\",\"v\": \"2016-07-26T14:48:12.493Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"1PZ1Z6FEB42BFOCYJH5MI\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"productId\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\":{\"v\":\"2016-09-01T18:06:29.890Z\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"},\"tags\":{\"t\":\"tags\",\"v\":[\"sunglasses\"]}},\"skus\":[{\"feCode\":\"FCA7PEP20CNLWDISH5MI\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"title\":{\"v\":\"THATS A TITLE\"},\"context\":{\"v\":\"default\"}}}],\"context\":{\"name\":\"default\"}}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONObject jsonData = new JSONObject(responseBody);
            productId = String.valueOf(jsonData.getInt("id"));
//            productId = responseBody.substring(6, responseBody.indexOf(",", 6));
            productName = productName_local;
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    protected static void shareSearch(String searchCode, String adminName) throws IOException {

        getAdminId(adminName);

        System.out.println("Sharing saved search with admin <" + adminName + ">, adminId <" + adminId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"associates\": [" + adminId + "]\n}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/shared-search/" + searchCode + "/associate")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    protected static void getAdminId(String adminName) throws IOException {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"query\":{\"bool\":{}},\"sort\":[{\"createdAt\":{\"order\":\"desc\"}}]}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/search/admin/store_admins_search_view/_search?size=50")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);

            JSONObject jsonData = new JSONObject(responseBody);
            JSONArray jsonArray = jsonData.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object     = jsonArray.getJSONObject(i);
                String adminName_local = object.getString("name");
                if (adminName_local.equals(adminName)) {
                    adminId = object.getInt("id");
                    System.out.println("<" + adminName + "> ID: " + adminId);
                }
            }

            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    private static void createSharedSearch_oneFilter() throws IOException {

        System.out.println("Creating a new shared search...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"title\":\"Test Search\",\"query\":[{\"display\":\"Order : Total : > : $0\",\"term\":\"grandTotal\",\"operator\":\"gt\",\"value\":{\"type\":\"currency\",\"value\":\"000\"}}],\"scope\":\"ordersScope\",\"rawQuery\":{\"query\":{\"bool\":{\"filter\":[{\"range\":{\"grandTotal\":{\"gt\":\"000\"}}}]}}}}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/shared-search")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            searchId = Integer.valueOf( responseBody.substring(6, responseBody.indexOf(",", 6)) );
            searchCode = responseBody.substring(16, responseBody.indexOf("\"", 16));
            System.out.println("Search ID: <" + searchId + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    public static void getAllSavedSearches() throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/shared-search?scope=ordersScope")
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        System.out.println(responseBody);
        System.out.println("---- ---- ---- ----");

    }

    private static void createProduct_activeFromTo(String sku, String startDate, String endDate) throws IOException {

        System.out.println("Creating product with active from-to dates; SKU: <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"productId\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"<p>The best thing to buy in 2016!</p>\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"" + startDate + "T07:00:17.729Z\"},\"activeTo\":{\"t\":\"datetime\",\"v\":\"" + endDate + "T19:00:00.810Z\"},\"tags\":{\"t\":\"tags\",\"v\":[\"sunglasses\"]}},\"skus\":[{\"feCode\":\"CIEP39Z2WPQ1ETFVQUXR\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}}}}],\"context\":\"\"}");
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
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            productName = productName_local;
            productId = responseBody.substring(6, responseBody.indexOf(",", 6));
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    private static void createProduct_active_noTag(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\": {\"t\": \"date\",\"v\": \"2016-07-26T14:48:12.493Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"F0QGTGBBINBQF5V53TYB9\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
//        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"productId\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\":{\"v\":\"2016-09-01T18:06:29.890Z\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"},\"tags\":{\"t\":\"tags\",\"v\":[\"" + tag + "\"]}},\"skus\":[{\"feCode\":\"FCA7PEP20CNLWDISH5MI\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"title\":{\"v\":\"THATS A TITLE\"},\"context\":{\"v\":\"default\"}}}],\"context\":{\"name\":\"default\"}}");
        Request request = new Request.Builder()
                .url(adminUrl + "/api/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            productName = productName_local;
            JSONObject jsonData = new JSONObject(responseBody);
            productId = String.valueOf(jsonData.getInt("id"));
//            productId = responseBody.substring(6, responseBody.indexOf(",", 6));
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    private static void archiveProduct(String productId) throws IOException {

        System.out.println("Archiving product with ID <" + productId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/products/default/" + productId)
                .delete(null)
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    private static void viewSKU_inventory(String skuCode) throws IOException {

        System.out.println("Viewing SKU with code <" + skuCode + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/inventory/summary/" + skuCode)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONObject jsonData = new JSONObject(responseBody);
            JSONObject obj = jsonData.getJSONArray("summary").getJSONObject(0);
            skuId_inventory = obj.getJSONObject("stockItem").getInt("id");
            System.out.println("SKU ID: <" + skuId_inventory + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

//    @Test
//    public void testTest() {
//
//        open(adminUrl);
//        if ( (Objects.equals(getUrl(), adminUrl + "/login")) ) {
//            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
//            loginPage.login("admin@admin.com", "password");
//            loginPage.userMenuBtn().shouldBe(visible);
//        }
//
//
//        sleep(12000);
//
//        p = open("http://admin.stage.foxcommerce.com/skus/SKU-ZYA", SkusPage.class);
//
//        p.sideMenu("SKUs").click();
//        p.addNewSKUBtn().shouldBe(visible);
//
//    }

    public static void main(String[] args) throws IOException {

    }

}