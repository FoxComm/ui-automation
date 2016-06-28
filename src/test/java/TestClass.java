import base.BaseTest;
import com.squareup.okhttp.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertEquals;

public class TestClass extends BaseTest {

//    protected static double cutDecimal(double numb) {
//
//        DecimalFormat cutDecimals = new DecimalFormat(("#.##"));
//        return Double.valueOf(cutDecimals.format(numb));
//
//    }

    private static int customerId;
    private static String orderId;
    private static String jwt;

    private static String customerName;     // stored from viewCustomer()
    private static String customerEmail;    // stored from viewCustomer()
    private static int addressId1;          // stored from listCustomerAddresses()
    private static int addressId2;          // stored from listCustomerAddresses()
    private static String gcNumber;           // stored from issueGiftCard()
    private static int shipMethodId;        // stored from listShipMethods()
    private static int creditCardId;        // stored from create createCreditCard()

    protected static int promotionId;
    protected static int couponId;
    protected static String singleCouponCode;
    public static List<String> bulkCodes = new ArrayList<>();

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
        System.out.println("--------");

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
        customerId = Integer.valueOf(responseBody.substring(6, 10));

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
                .url("http://admin.stage.foxcommerce.com/api/v1/orders")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        orderId = responseBody.substring(30, 37);

        System.out.println(response);
        System.out.println("Order ID: <" + orderId + ">");
        System.out.println("--------");

    }

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
        System.out.println(response);
        System.out.println("--------");

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
                .url("http://admin.stage.foxcommerce.com/api/v1/orders/" + orderId + "/shipping-address")
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
                .url("http://admin.stage.foxcommerce.com/api/v1/customers/" + customerId + "/addresses")
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        String responseBody = response.body().string();
        addressId1 = Integer.valueOf(responseBody.substring(17, 21));
//        addressId2 = Integer.valueOf(addresses.substring(216, 220));
//        addressId3 = Integer.valueOf(addresses.substring(415, 419));

        System.out.println(response);
        System.out.println("Address 1: <" + addressId1 + ">");
        System.out.println("Address 2: <" + addressId2 + ">");
//        System.out.println("Addres 3: <" + addressId3 + ">");
        System.out.println("--------");

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
        System.out.println("--------");

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
        System.out.println("--------");

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
        System.out.println("--------");

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
                .url("http://admin.stage.foxcommerce.com/api/v1/customers/" + customerId + "/payment-methods/store-credit")
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
        System.out.println("--------");

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
        System.out.println("--------");

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
        System.out.println("--------");

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
        System.out.println(responseBody);
        System.out.println("--------");

    }

    private static void createCoupon(int promotionId) throws IOException {

        System.out.println("Creating a new coupon...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":247,\"form\":{\"id\":247,\"attributes\":{\"cafec63e4b\":{\"isExclusive\":false,\"usesPerCode\":1,\"usesPerCustomer\":1,\"isUnlimitedPerCode\":false,\"isUnlimitedPerCustomer\":false},\"267d79dd58\":\"coupon one\",\"2be88ca424\":null,\"8cee41dd60\":\"<p>coupon one</p>\",\"c23781011c\":\"2016-06-26T21:42:23.804+00:00\"},\"createdAt\":\"2016-06-27T21:29:30.861Z\"},\"shadow\":{\"id\":302,\"formId\":247,\"attributes\":{\"name\":{\"type\":\"coupon one\",\"ref\":\"267d79dd58\"},\"details\":{\"type\":\"<p>coupon one</p>\",\"ref\":\"8cee41dd60\"},\"activeTo\":{\"type\":null,\"ref\":\"2be88ca424\"},\"activeFrom\":{\"type\":\"2016-06-26T21:42:23.804+00:00\",\"ref\":\"c23781011c\"},\"usageRules\":{\"type\":\"usageRules\",\"ref\":\"cafec63e4b\"},\"description\":{\"type\":\"coupon one\",\"ref\":\"267d79dd58\"},\"storefrontName\":{\"type\":\"<p>coupon one</p>\",\"ref\":\"8cee41dd60\"}},\"createdAt\":\"2016-06-27T21:29:30.861Z\"},\"promotion\":" + promotionId + "}");
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

        System.out.println(response);
        System.out.println(responseBody);
        System.out.println(couponId);
        System.out.println("--------");

    }

    private static void generateSingleCode(int couponId) throws IOException {

        System.out.println("Generating a single code for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url("http://admin.stage.foxcommerce.com/api/v1/coupons/codes/generate/" + couponId + "/newcpn-" + generateRandomID())
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
        printList(bulkCodes);

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
        System.out.println("--------");

    }

    public static void main(String[] args) throws IOException {

//        loginAsAdmin();
//        createNewCustomer();
//        createCart(customerId);
//
//        updSKULineItems(orderId, "SKU-YAX", 1);
//
//        setShipAddress(orderId, customerName, 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
//
//        listShipMethods(orderId);
//        setShipMethod(orderId, shipMethodId);
//
//        createAddress(customerId, customerName,4161, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", true);
//        listCustomerAddresses(customerId);
//        createCreditCard("John Doe", "5555555555554444", "999", 4, 2020, addressId1);
//        setPayment_creditCard(orderId, creditCardId);
////
//////        issueStoreCredit(customerId, 10000);
//////        setPayment_storeCredit(orderId, 10000);
////
//////        issueGiftCard(500, 1);
//////        setPayment_giftCard(orderId, gcNumber, 10000);
//
//        checkoutOrder(orderId);

        //---------------

        loginAsAdmin();
        createNewCustomer();
        createCart(customerId);
        updSKULineItems(orderId, "SKU-YAX", 1);
//        setShipAddress(orderId, customerName, 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
//        listShipMethods(orderId);
//        setShipMethod(orderId, shipMethodId);
//        listCustomerAddresses(customerId);
//        createCreditCard("John Doe", "5555555555554444", "999", 4, 2020, addressId1);
//        setPayment_creditCard(orderId, creditCardId);
//        checkoutOrder(orderId);
//
//        createCart(customerId);
//        updSKULineItems(orderId, "SKU-BRO", 2);
//        setShipAddress(orderId, customerName, 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
//        listShipMethods(orderId);
//        setShipMethod(orderId, shipMethodId);
//        setPayment_creditCard(orderId, creditCardId);
//        checkoutOrder(orderId);
//
//        changeOrderState(orderId, "fulfillmentStarted");

//        createPromotion_coupon();
//        createCoupon(promotionId);
//        generateSingleCode(couponId);
        bulkGenerateCodes(263, "bulkcpn", 4, 4);
        applyCouponCode(orderId, bulkCodes.get(2));

    }

}


//Test Customer-12345
//testcustomer.12345@mail.com