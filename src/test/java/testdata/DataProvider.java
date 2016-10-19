package testdata;

import base.BaseTest;
import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;

public class DataProvider extends BaseTest {

    protected static int customerId;
    protected static String cartId;
    protected static String orderId;            // responseBody.indexOf(",") not added
    protected static int orderTotal;
    private static String jwt;

    protected static String customerName;       // stored from viewCustomer()
    protected static String customerEmail;      // stored from viewCustomer()
    private static int addressId1;              // stored from listCustomerAddresses()
    private static int addressId2;              // stored from listCustomerAddresses()
    private static String addressPayload;   // stored from listCustomerAddresses()

    protected static String gcCode;           // stored from issueGiftCard()
    private static int scId;                    // stored from issueStoreCredit()
    private static int shipMethodId;            // stored from listShipMethods()
    private static int creditCardId;            // stored from create createCreditCard()
    private static String stripeToken;          // stored from getStripeToken()

    protected static String promotionId;
    protected static String couponId;
    protected static String couponName;
    private static String singleCouponCode;
    private static List<String> bulkCodes = new ArrayList<>();

    protected static String sku;                    // stored from createSKU_active();
    protected static String skuTitle;               // stored from createSKU_active();
    protected static int skuId_inventory;                     // stored from viewSKU();
    protected static String productName;            // stored from createProduct_<..>() methods
    protected static String productId;                 // stored from createProduct_<..>() methods

    private static int searchId;                    // stored from createSharedSearch() methods
    public static String searchRandomId;            // stored at createSharedSearch() methods
    protected static String searchCode;             // stored from createSharedSearch() methods
    protected static int adminId;                   // stored from getAdminId() method

    private static JSONObject parse(String rout) throws IOException {
        String jsonData = "";
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(rout));
        while ((line = bufferedReader.readLine()) != null) {
            jsonData += line + "\n";
        }
        bufferedReader.close();
        return new JSONObject(Objects.requireNonNull(jsonData));
    }

    private static void failTest(String responseBody, int responseCode, String responseMsg) throws IOException {
        System.out.println("Response: " + responseCode + " " + responseMsg);
        System.out.println(responseBody);
        System.out.println("--------");
        if (!(responseCode == 200)) {
            throw new RuntimeException(responseBody +
            "\nExpected:[200], Actual:[" + responseCode + "]");
        }
    }

    @Step("Log in as admin")
    private static void loginAsAdmin() throws IOException {

        System.out.println("Authorizing as an admin...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"email\": \"admin@admin.com\"," +
                "\n    \"password\": \"password\"," +
                "\n    \"org\": \"tenant\"\n}");

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/public/login")
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
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create new customer")
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
                .url(apiUrl + "/v1/customers")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("authorization", "Basic YWRtaW5AYWRtaW4uY29tOnBhc3N3b3Jr")
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
            customerId = jsonData.getInt("id");
            System.out.println("Customer ID: " + customerId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create cart for customer <{0}>")
    private static void createCart(int customerId) throws IOException {

        System.out.println("Creating a cart for customer " + customerId + "...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"customerId\": " + customerId + "\n}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders")
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
            cartId = jsonData.getString("referenceNumber");
//            orderId = responseBody.substring(20, responseBody.indexOf(",", 20));
            System.out.println("Cart ID: <" + cartId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

//    private static void viewOrder(int orderId) throws IOException {
//
//        System.out.println("Viewing order <" + orderId + ">...");
//
//        OkHttpClient client = new OkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(apiUrl + "/v1/orders/BR10116")
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

    @Step("Add line item: <{1}>, <QTY:{2}> to cart: <{0}>")
    private static void updSKULineItems(String orderRefNum, String SKU, int quantity) throws IOException {

        System.out.println("Updating SKUs: setting <" + SKU + "> quantity to <" + quantity + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[" +
                "{\"sku\":\"" + SKU + "\"," +
                "\"quantity\":" + quantity + "}]");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + orderRefNum + "/line-items")
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

    @Step("View customer <{0}>")
    private static void viewCustomer(int customerId) throws IOException {

        System.out.println("Getting name and email of customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId)
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
            customerName = responseBody.substring(55, 73);
            customerEmail = responseBody.substring(20, 45);
            System.out.println("Customer name: <" + customerName + ">");
            System.out.println("Customer email: <" + customerEmail + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create new shipping address for customer <{0}>")
    private static void createAddress(int customerId, String name, int regionId, int countryId, String region_name, String address1, String address2, String city, String zip, String phoneNumber, boolean isDefault)
            throws IOException {

        System.out.println("Creating a new address for customer " + customerId + "...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"name\":\"" + name + "\"," +
                "\"regionId\":" + regionId + "," +
                "\"countryId\":" + countryId + "," +
                "\"region_name\":\"" + region_name + "\"," +
                "\"address1\":\"" + address1 + "\"," +
                "\"address2\":\"" + address2 + "\"," +
                "\"city\":\""+ city + "\"," +
                "\"zip\":\"" + zip + "\"," +
                "\"phoneNumber\":\"" + phoneNumber + "\"," +
                "\"isDefault\":" + isDefault + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses")
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

    @Step("Add new shipping address and set it as a chosen for cart <{0}>")
    private static void setShipAddress(String cartId, String name, int regionId, int countryId, String region_name, String address1, String address2, String city, String zip, String phoneNumber, boolean isDefault) throws IOException {

        System.out.println("Adding new shipping address and setting it as a chosen for <" + cartId + "> order...");
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"name\":\"" + name + "\"," +
                "\"regionId\":" + regionId + "," +
                "\"address1\":\"" + address1 + "\"," +
                "\"address2\":\"" + address2 + "\"," +
                "\"city\":\"" + city + "\"," +
                "\"zip\":\"" + zip + "\"," +
                "\"isDefault\":" + isDefault + "," +
                "\"phoneNumber\":\"" + phoneNumber + "\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/shipping-address")
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

    @Step("List all shipping addresses of customer <{0}>")
    private static void listCustomerAddresses(int customerId) throws IOException {

        System.out.println("Listing all addresses of customer " + customerId + "...");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses")
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
            System.out.println("Address 1: <" + addressId1 + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("View address details, customerID: <{0}>, addressID: <{1}>")
    private static void getCustomerAddress(int customerId, int addressId) throws IOException {

        System.out.println("Getting address with ID <" + addressId + "> of customer <" + customerId + ">...");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId)
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
            addressPayload = responseBody;
            System.out.println("Address 1: <" + addressId1 + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Update shipping address details. addressID: <{1}>, customerId: <{0}>")
    private static void updateCustomerShipAddress(int customerId, int addressId1, String customerName, int regionId, String address1, String address2, String city, String zip, String phoneNumber) throws IOException {

        System.out.println("Updating shipping address ID:<" + addressId1 + "> for customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"name\":\"" + customerName + "\"," +
                "\"regionId\":" + regionId + "," +
                "\"address1\":\"" + address1 + "\"," +
                "\"address2\":\"" + address2 + "\"," +
                "\"city\":\"" + city + "\"," +
                "\"zip\":\"" + zip + "\"," +
                "\"isDefault\":false," +
                "\"phoneNumber\":\"" + phoneNumber + "\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId1)
                .patch(body)
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


    @Step("List possible shipping methods for cart <{0}>")
    private static void listShipMethods(String cartId) throws IOException {

        System.out.println("Getting possible shipping methods for order <" + cartId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/shipping-methods/" + cartId)
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
            shipMethodId = jsonData.getJSONObject(0).getInt("id");
            System.out.println("shipMethodId: <" + shipMethodId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Set shipping method <{1}> for cart <{1}>")
    private static void setShipMethod(String cartId, int shipMethodId) throws IOException {

        System.out.println("Setting shipping method for order <" + cartId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"shippingMethodId\": " + shipMethodId + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/shipping-method")
                .patch(body)
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

    private static void getStripeToken(int customerId, String cardNumber, int expMonth, int expYear, int cvv) throws IOException {

        JSONObject jsonObj = parse("bin/payloads/getStripeToken.json");

        // Create JSONObject from addressPayload and from "address" obj in .JSON that will be used as a payload
        JSONObject address_tmp = new JSONObject(addressPayload);
        JSONObject address = jsonObj.getJSONObject("address");

        // Merge data from addressPayload JSONObj into "address" obj of payload .JSON
        address.putOpt("name", address_tmp.getString("name"));
        address.putOpt("zip", address_tmp.getString("zip"));
        address.putOpt("city", address_tmp.getString("city"));
        address.putOpt("address1", address_tmp.getString("address1"));
        address.putOpt("regionId", address_tmp.getJSONObject("region").getInt("id"));

        jsonObj.putOpt("customerId", customerId);
        jsonObj.putOpt("address", address);
        jsonObj.putOpt("cardNumber", cardNumber);
        jsonObj.putOpt("expMonth", expMonth);
        jsonObj.putOpt("expYear", expYear);
        jsonObj.putOpt("cvv", cvv);
        String payload = jsonObj.toString();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/credit-card-token")
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
            org.json.JSONObject jsonData = new org.json.JSONObject(responseBody);
            stripeToken = jsonData.getString("token");
            System.out.println("Stripe token: <" + stripeToken + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create credit card")
    private static void createCreditCard(int customerId, String holderName, String cardNumber, int expMonth, int expYear, int cvv, String brand) throws IOException {

        getStripeToken(customerId, cardNumber, expMonth, expYear, cvv);

        System.out.println("Create a new credit card for customer <" + customerId + ">...");

        JSONObject jsonObj = parse("bin/payloads/createCreditCard.json");
        JSONObject address = new JSONObject(addressPayload);
        JSONObject region = address.getJSONObject("region");

        jsonObj.putOpt("token", stripeToken);
        jsonObj.putOpt("holderName", holderName);
        jsonObj.putOpt("lastFour", cardNumber.substring(12, 16));
        jsonObj.putOpt("expMonth", expMonth);
        jsonObj.putOpt("expYear", expYear);
        jsonObj.putOpt("brand", brand);
        jsonObj.putOpt("billingAddress", address);
        jsonObj.getJSONObject("billingAddress").putOpt("regionId", region.getInt("id"));
        jsonObj.getJSONObject("billingAddress").putOpt("state", region.getString("name"));
        jsonObj.getJSONObject("billingAddress").putOpt("country", "United States");
        String payload = jsonObj.toString();
        System.out.println(payload);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards")
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

    @Step("Set credit card <{1}> as a payment method for cart <{0}>")
    private static void setPayment_creditCard(String cartId, int creditCardId) throws IOException {

        System.out.println("Setting credit card with id <" + creditCardId + "> as a payment method for order <" + cartId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"creditCardId\":" + creditCardId + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/payment-methods/credit-cards")
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

    @Step("Issue store credit in amount of <{1}> for customer <{0}>")
    private static void issueStoreCredit(int customerId, int amount) throws IOException {

        System.out.println("Issuing store credit for customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"amount\":" + amount + "," +
                "\"reasonId\":1," +
                "\"subReasonId\":1," +
                "\"currency\":\"USD\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/store-credit")
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
            scId = jsonData.getInt("id");
            System.out.println("Store Credit ID: <" + scId + ">...");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Transfer GC <{1}> to store credits for customer <{0}>")
    private static void issueStoreCredit_gcTransfer(int customerId, String gcNumber) throws IOException {

        System.out.println("Transferring GC <" + gcNumber + "to SC for customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"reasonId\":3," +
                "\"code\":\"" + gcNumber + "\"," +
                "\"subReasonId\":null}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/gift-cards/" + gcNumber + "/convert/" + customerId)
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
            scId = jsonData.getInt("id");
            System.out.println("Store Credit ID: <" + scId + ">...");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }


    }

    @Step("Set store credits <amount: {1}> as a payment method for cart <{0}>")
    protected static void setPayment_storeCredit(String cartId, int amount) throws IOException {

        System.out.println("Setting store credit in amount of <" + amount + "> as a payment for order <" + cartId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"amount\":" + amount + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/payment-methods/store-credit")
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

    @Step("Issue GC <balance: {0}>, <QTY: {1}>")
    private static void issueGiftCard(int balance, int quantity) throws IOException {

        System.out.println("Creating new gift card...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"balance\": " + balance + "," +
                "\"quantity\": " + quantity + "," +
                "\"reasonId\": 1}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/gift-cards")
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
            System.out.println("GC code: <" + gcCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Put GC <{0}> on hold")
    private static void putGCOnHold(String gcNumber) throws IOException {

        System.out.println("Puting <" + gcNumber + "> on hold..." );

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"state\":\"onHold\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/gift-cards/" + gcNumber)
                .patch(body)
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

    @Step("Set GC <gcNumber: {1}>, <amount: {2}> for cart <{0}>")
    protected static void setPayment_giftCard(String cartId, String gcNumber, int amount) throws IOException {

        System.out.println("Setting gict card <"+ gcNumber + "> in amount of <" + amount + "> as a payment for order <" + cartId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"code\":\"" + gcNumber + "\"," +
                "\"amount\":" + amount + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/payment-methods/gift-cards")
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

    @Step("Checkout cart <{0}>")
    private static void checkoutCart(String cartId) throws IOException {

        System.out.println("Checking out order <" + cartId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/checkout")
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
            orderId = cartId;
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Change order <{0}> state to <{1}>")
    private static void changeOrderState(String orderId, String newState) throws IOException {

        System.out.println("Change state of order <" + orderId + "> to <" + newState + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"state\":\"" + newState + "\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + orderId)
                .patch(body)
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

    @Step("Create promotion -- <Apply type: 'Coupon'>")
    private static void createPromotion_coupon() throws IOException {

        System.out.println("Creating a new promotion...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"applyType\":\"coupon\",\"form\":{\"id\":248,\"attributes\":{\"eaa8440703\":\"new promo " + generateRandomID() + "\",\"25e24d5d0f\":\"<p>new promo</p>\"},\"discounts\":[{\"id\":249,\"attributes\":{\"bb0b82afad\":{\"orderAny\":{}},\"3db8e5c670\":{\"orderPercentOff\":{\"discount\":10}}},\"createdAt\":\"2016-06-27T22:27:43.938Z\"}],\"createdAt\":\"2016-06-27T22:27:43.915Z\"},\"shadow\":{\"id\":303,\"formId\":248,\"attributes\":{\"name\":{\"type\":\"string\",\"ref\":\"eaa8440703\"},\"storefrontName\":{\"type\":\"richText\",\"ref\":\"25e24d5d0f\"},\"description\":{\"type\":\"richText\",\"ref\":\"eaa8440703\"},\"details\":{\"type\":\"richText\",\"ref\":\"25e24d5d0f\"}},\"discounts\":[{\"id\":249,\"attributes\":{\"qualifier\":{\"type\":\"qualifier\",\"ref\":\"bb0b82afad\"},\"offer\":{\"type\":\"offer\",\"ref\":\"3db8e5c670\"}},\"createdAt\":\"2016-06-27T22:27:43.938Z\"}],\"createdAt\":\"2016-06-27T22:27:43.915Z\"}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/promotions/default")
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
            promotionId = String.valueOf(jsonData.getJSONObject("form").getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create promotion -- <Apply type: 'Auto'>, <State: 'Inactive'>")
    private static void createPromotion_auto_inactive() throws IOException {

        System.out.println("Creating a new promotion...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"form\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"name\":\"new promo " + generateRandomID() + "\",\"storefrontName\":\"<p>sf name</p>\",\"description\":\"<p>descr</p>\",\"details\":\"<p>details</p>\"},\"discounts\":[{\"id\":null,\"createdAt\":null,\"attributes\":{\"qualifier\":{\"orderAny\":{}},\"offer\":{\"orderPercentOff\":{\"discount\":10}}}}]},\"shadow\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"name\":{\"type\":\"string\",\"ref\":\"name\"},\"storefrontName\":{\"type\":\"richText\",\"ref\":\"storefrontName\"},\"description\":{\"type\":\"richText\",\"ref\":\"description\"},\"details\":{\"type\":\"richText\",\"ref\":\"details\"}},\"discounts\":[{\"id\":null,\"createdAt\":null,\"attributes\":{\"qualifier\":{\"type\":\"qualifier\",\"ref\":\"qualifier\"},\"offer\":{\"type\":\"offer\",\"ref\":\"offer\"}}}]},\"applyType\":\"auto\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/promotions/default")
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
            promotionId = String.valueOf(jsonData.getJSONObject("form").getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create promotion -- <Apply type: 'Auto'>, <State: 'Active'>")
    private static void createPromotion_auto_active() throws IOException {

        System.out.println("Creating a new promotion...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"form\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"name\":\"new promo " + generateRandomID() + "\",\"storefrontName\":\"\",\"description\":\"<p>descr</p>\",\"details\":\"<p>details</p>\",\"storefront Name\":\"<p>sf name</p>\",\"activeFrom\":\"2016-07-30T18:12:27.938Z\",\"activeTo\":null},\"discounts\":[{\"id\":null,\"createdAt\":null,\"attributes\":{\"qualifier\":{\"orderAny\":{}},\"offer\":{\"orderPercentOff\":{\"discount\":10}}}}]},\"shadow\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"name\":{\"type\":\"string\",\"ref\":\"name\"},\"storefrontName\":{\"type\":\"richText\",\"ref\":\"storefrontName\"},\"description\":{\"type\":\"richText\",\"ref\":\"description\"},\"details\":{\"type\":\"richText\",\"ref\":\"details\"},\"storefront Name\":{\"type\":\"richText\",\"ref\":\"storefront Name\"},\"activeFrom\":{\"type\":\"2016-07-30T18:12:27.938Z\",\"ref\":\"activeFrom\"},\"activeTo\":{\"type\":null,\"ref\":\"activeTo\"}},\"discounts\":[{\"id\":null,\"createdAt\":null,\"attributes\":{\"qualifier\":{\"type\":\"qualifier\",\"ref\":\"qualifier\"},\"offer\":{\"type\":\"offer\",\"ref\":\"offer\"}}}]},\"applyType\":\"auto\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/promotions/default")
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
            promotionId = String.valueOf(jsonData.getJSONObject("form").getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create coupon with promotion <ID:'{0}>'")
    private static void createCoupon(String promotionId) throws IOException {

        System.out.println("Creating a new coupon with promotion <" + promotionId + ">...");
        String randomId = generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"form\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"usageRules\":{\"isExclusive\":false,\"isUnlimitedPerCode\":false,\"usesPerCode\":1,\"isUnlimitedPerCustomer\":false,\"usesPerCustomer\":1},\"name\":\"test coupon " + randomId + "\",\"storefrontName\":\"storefront name " + randomId + "\",\"description\":\"<p>test description</p>\",\"details\":\"<p>test details</p>\",\"storefront Name\":\"<p>storefront name 77777</p>\",\"activeFrom\":\"2016-07-30T18:59:10.402Z\",\"activeTo\":null}},\"shadow\":{\"id\":null,\"createdAt\":null,\"attributes\":{\"usageRules\":{\"type\":\"usageRules\",\"ref\":\"usageRules\"},\"name\":{\"type\":\"string\",\"ref\":\"name\"},\"storefrontName\":{\"type\":\"richText\",\"ref\":\"storefrontName\"},\"description\":{\"type\":\"richText\",\"ref\":\"description\"},\"details\":{\"type\":\"richText\",\"ref\":\"details\"},\"storefront Name\":{\"type\":\"richText\",\"ref\":\"storefront Name\"},\"activeFrom\":{\"type\":\"2016-07-30T18:59:10.402Z\",\"ref\":\"activeFrom\"},\"activeTo\":{\"type\":null,\"ref\":\"activeTo\"}}},\"promotion\":" + promotionId + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/coupons/default")
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
            couponName = "test coupon " + randomId;
            System.out.println("Coupon ID: " + couponId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Generate a single code for coupon <ID:'{0}>'")
    private static void generateSingleCode(String couponId) throws IOException {

        System.out.println("Generating a single code for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/coupons/codes/generate/" + couponId + "/NWCPN-" + generateRandomID())
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
            singleCouponCode = responseBody.substring(1, responseBody.length() - 1);
            System.out.println("Single coupon code: " + singleCouponCode);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Bulk generate codes for coupon <ID:'{0}'>; [prefix:'{1}', codeLength:'{2}', QTY:'{3}']")
    private static void bulkGenerateCodes(String couponId, String prefix, int codeLength, int quantity) throws IOException {

        int length = prefix.length() + codeLength;

        System.out.println("Bulk generating coupon codes for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"prefix\":\"" + prefix + "\"," +
                "\"length\":" + length + "," +
                "\"quantity\":" + quantity + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/coupons/codes/generate/" + couponId)
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
//            METHOD DEBUG
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

    @Step("Apply coupon <ID:'{1}'> to cart <{0}>")
    private static void applyCouponCode(String cartId, String couponCode) throws IOException {

        System.out.println("Applying coupon code <" + couponCode + "> to order <" + cartId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/coupon/" + couponCode)
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

    @Step("Change store credit <ID:'{0}'> state to <{1}>")
    private static void updateSCState(int scId, String state) throws IOException {

        System.out.println("Updating state of store credit with Id <" + scId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"state\":\"" + state + "\"," +
                "\"reasonId\":1}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/store-credits/" + scId)
                .patch(body)
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
            System.out.println("Update stated: <" + state + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create SKU in <State:'Active'>")
    private static void createSKU_active() throws IOException {

        System.out.println("Creating a new SKU, options: ACTIVE state...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;
        String title = "SKU Test Title " + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"" + title + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Test description</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"5000\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"" + getDate() + "T00:03:26.685Z\"},\"activeTo\":{\"t\":\"datetime\",\"v\":null}}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/skus/default")
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
            sku = skuCode;
            skuTitle = title;
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create SKU in <State: 'Inactive'>")
    private static void createSKU_inactive() throws IOException {

        System.out.println("Creating a new SKU, options: INACTIVE state...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}}}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/skus/default")
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
            sku = skuCode;
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create SKU with empty 'Title'")
    private static void createSKU_noTitle() throws IOException {

        System.out.println("Creating a new SKU, options: no title...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1215}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1215}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1000}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"2016-07-29T00:03:26.685Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/skus/default")
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
            sku = skuCode;
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create SKU with empty 'Description'")
    private static void createSKU_noDescription() throws IOException {

        System.out.println("Creating a new SKU, options: no description...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1215}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1215}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1000}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"2016-07-29T00:03:26.685Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/skus/default")
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
            sku = skuCode;
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create SKU without specifying the prices")
    private static void createSKU_noPrices() throws IOException {

        System.out.println("Creating a new SKU, options: all prices equals <0>...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":null}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":null}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":null}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"2016-07-29T00:03:26.685Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/skus/default")
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
            sku = skuCode;
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create product; <SKU: auto-created with product>, <State:'Active'>")
    private static void createProduct_noSKU_active() throws IOException {

        System.out.println("Creating a new product... No SKU code is provided, so a new one will be created.");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\":{\"v\":\"2016-07-27T23:47:27.518Z\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"},\"tags\":{\"t\":\"tags\",\"v\":[\"sunglasses\"]}},\"skus\":[{\"feCode\":\"ODB4UPHFJ11UK4HOLXR\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"SKU-TST\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}}}}]}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default")
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
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create product; <SKU:'{0}'>, <Tag:'{1}'>, <State:'Active'>")
    private static void createProduct_active(String sku, String tag) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"productId\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\":{\"v\":\"2016-09-01T18:06:29.890Z\",\"t\":\"datetime\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"},\"tags\":{\"t\":\"tags\",\"v\":[\"" + tag + "\"]}},\"skus\":[{\"feCode\":\"FCA7PEP20CNLWDISH5MI\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"title\":{\"v\":\"THATS A TITLE\"},\"context\":{\"v\":\"default\"}}}],\"context\":{\"name\":\"default\"}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default")
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
            productName = productName_local;
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create product; <SKU:'{0}'> <State:'Active'>, no tag")
    private static void createProduct_active_noTag(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\": {\"t\": \"date\",\"v\": \"2016-07-26T14:48:12.493Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"F0QGTGBBINBQF5V53TYB9\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default")
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
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create product; <SKU:'{0}'>, <Tag:'{1}'>, <State:'Inactive'>")
    private static void createProduct_inactive(String sku, String tag) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"tags\":{\"t\":\"tags\",\"v\":[\"" + tag + "\"]}},\"skus\":[{\"feCode\":\"JBV96IF5QRNZM9KQ33DI\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default")
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
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create product; <SKU:'{0}'>, no tag, <State:'Inactive'>")
    private static void createProduct_inactive_noTag(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"}},\"skus\":[{\"feCode\":\"KB0SOK5PSSBEPP5H4CXR\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":\"2718\"}}}}]}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default")
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
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create product; <SKU:'{0}'>, <Tag:'sunglasses'>, <Active From:'{1}'>, <Active To:'{2}'>")
    private static void createProduct_activeFromTo(String sku, String startDate, String endDate) throws IOException {

        System.out.println("Creating product with active from-to dates; SKU: <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"productId\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"<p>The best thing to buy in 2016!</p>\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"" + startDate + "T07:00:17.729Z\"},\"activeTo\":{\"t\":\"datetime\",\"v\":\"" + endDate + "T19:00:00.810Z\"},\"tags\":{\"t\":\"tags\",\"v\":[\"sunglasses\"]}},\"skus\":[{\"feCode\":\"CIEP39Z2WPQ1ETFVQUXR\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}}}}],\"context\":\"\"}");
        Request request = new Request.Builder()
        .url(apiUrl + "/v1/products/default")
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
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productName + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Archive product <ID:'{0}'>")
    private static void archiveProduct(String productId) throws IOException {

        System.out.println("Archiving product with ID <" + productId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default/" + productId)
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

    @Step("Create shared search with one search filter")
    private static void createSharedSearch_oneFilter() throws IOException {

        System.out.println("Creating a new shared search...");
        searchRandomId = generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"title\":\"Search " + searchRandomId + "\",\"query\":[{\"display\":\"Order : Total : > : $0\",\"term\":\"grandTotal\",\"operator\":\"gt\",\"value\":{\"type\":\"currency\",\"value\":\"000\"}}],\"scope\":\"ordersScope\",\"rawQuery\":{\"query\":{\"bool\":{\"filter\":[{\"range\":{\"grandTotal\":{\"gt\":\"000\"}}}]}}}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/shared-search")
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
            searchCode = jsonData.getString("code");
            searchId = jsonData.getInt("id");
            System.out.println("Search ID: <" + searchId + ">");
            System.out.println("Search code: <" + searchCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create shared search with two search filters")
    private static void createSharedSearch_twoFilters() throws IOException {

        System.out.println("Creating a new shared search...");
        searchRandomId = generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"title\":\"Search " + searchRandomId + "\",\"query\":[{\"display\":\"Order : State : Remorse Hold\",\"term\":\"state\",\"operator\":\"eq\",\"value\":{\"type\":\"enum\",\"value\":\"remorseHold\"}},{\"display\":\"Order : Total : > : $1\",\"term\":\"grandTotal\",\"operator\":\"gt\",\"value\":{\"type\":\"currency\",\"value\":\"100\"}}],\"scope\":\"ordersScope\",\"rawQuery\":{\"query\":{\"bool\":{\"filter\":[{\"term\":{\"state\":\"remorseHold\"}},{\"range\":{\"grandTotal\":{\"gt\":\"100\"}}}]}}}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/shared-search")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        response.body().charStream();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);

//            searchId = Integer.valueOf( responseBody.substring(6, responseBody.indexOf(",", 6)) );
            JSONObject jsonData = new JSONObject(responseBody);
            searchCode = jsonData.getString("code");
            searchId = jsonData.getInt("id");
            System.out.println("Search ID: <" + searchId + ">");
            System.out.println("Search code: <" + searchCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Share search <searchCode:'{0}'> with <{1}>")
    protected static void shareSearch(String searchCode, String adminName) throws IOException {

        getAdminId(adminName);

        System.out.println("Sharing saved search (code <" + searchCode + ">) with admin <" + adminName + ">, adminId <" + adminId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"associates\": [" + adminId + "]\n}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/shared-search/" + searchCode + "/associate")
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

    private static void getAdminId(String adminName) throws IOException {

        System.out.println("Getting ID of <" + adminName + "> admin");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"query\":{\"bool\":{}},\"sort\":[{\"createdAt\":{\"order\":\"desc\"}}]}");
        Request request = new Request.Builder()
                .url(apiUrl + "/search/admin/store_admins_search_view/_search?size=50")
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
                JSONObject object = jsonArray.getJSONObject(i);
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

    @Step("Delete search <searchId:'{0}'>")
    protected static void deleteSearch(String searchId) throws IOException {

        System.out.println("Deleting saved search with id <" + searchId + ">.");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/shared-search/" + searchId)
                .delete(null)
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

    private static void viewSKU_inventory(String skuCode) throws IOException {

        System.out.println("Viewing inventory summary of SKU <" + skuCode + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/inventory/summary/" + skuCode)
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

    @Step("Increase amount of sellable unites of <{0}> by <{1}>")
    private static void increaseOnHandQty(String skuCode, String type, Integer qty) throws IOException {

        sleep(10000);
        viewSKU_inventory(skuCode);

        System.out.println("Increase amount of sellable items by <" + qty + "> for SKU <" + skuCode + ">, ID: <" + skuId_inventory + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"qty\":" + qty + "," +
                "\"type\":\"" + type + "\"," +
                "\"status\":\"onHand\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/inventory/stock-items/" + skuId_inventory + "/increment")
                .patch(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 204) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create SKU specifically for search filter tests")
    private static void createSKU_search() throws IOException {

        System.out.println("Creating a new SKU, options: ACTIVE state...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;
        String title = "SKU Test Title " + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"" + title + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Test description</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":90000}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":90000}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":90000}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"" + getDate() + "T00:03:26.685Z\"},\"activeTo\":{\"t\":\"datetime\",\"v\":null}}}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/skus/default")
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
            sku = skuCode;
            skuTitle = title;
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Archive SKU <{0}>")
    private static void archiveSKU(String skuCode) throws IOException {

        System.out.println("Archiving SKU <" + skuCode + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/skus/default/" + skuCode)
                .delete(null)
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
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create test data using API")
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
                updSKULineItems(cartId, "SKU-TRL", 1);
                break;

            case "cart with 2 items":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-TRL", 1);
                updSKULineItems(cartId, "SKU-TRL", 3);
                break;

            case "cart with 3 items":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-TRL", 3);
                updSKULineItems(cartId, "SKU-BRO", 2);
                updSKULineItems(cartId, "SKU-ZYA", 4);
                break;

            case "cart with 1 item, qty: 3":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-TRL", 3);
                break;

            //------------------------------------- SHIPPING ADDRESS -------------------------------------//

            case "cart with empty address book":
                createNewCustomer();
                createCart(customerId);
                break;

            case "cart with non-empty address book":
                createNewCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with chosen shipping address":
                createNewCustomer();
                createCart(customerId);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 2 addresses in address book":
                createNewCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            case "cart with 2 addresses and defined default shipping address":
                createNewCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            //----------------------------------- ORDERS WITH COUPONS ------------------------------------//

//            case "a cart && a single code coupon":
//                createCart(customerId);
//                createPromotion_coupon();
//                sleep(5000);
//                createCoupon(promotionId);
//                generateSingleCode(couponId);
//                break;

            case "a cart with a single code coupon applied":
                createCart(customerId);
                createPromotion_coupon();
                sleep(5000);
                createCoupon(promotionId);
                generateSingleCode(couponId);
                applyCouponCode(cartId, singleCouponCode);
                break;
//
//            case "a cart && coupon with bulk generated codes":
//                createCart(customerId);
//                createPromotion_coupon();
//                sleep(5000);
//                createCoupon(promotionId);
//                generateCodes_bulk(couponId);
//                applyCouponCode(bulkCodes.get(0));
//                break;

            case "a cart with a bulk generated code applied":
                createCart(customerId);
                createPromotion_coupon();
                sleep(5000);
                createCoupon(promotionId);
                bulkGenerateCodes(couponId, "BLK_NWCPN-", 5, 3);
                applyCouponCode(cartId, bulkCodes.get(0));
                break;

            //------------------------------------- PAYMENT METHOD -------------------------------------//

            case "cart with 1 item and chosen shipping address":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-BRO", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 1 item && customer with GC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-BRO", 1);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item && customer with SC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-BRO", 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method, and credit card payment":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with 1 item, shipping method and issued SC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method and issued GC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item, shipping method, credit card payment and issued SC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method, credit card payment and issued GC":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item, shipping method, issued SC and GC":
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item && SC onHold":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "onHold");
                break;

            case "cart with 1 item && SC canceled":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "canceled");
                break;

            //------------------------------------- ORDER STATE -------------------------------------//

            case "order in remorse hold":
                createNewCustomer();
                createCart(customerId);
                increaseOnHandQty("SKU-YAX", "Sellable", 1);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                break;

            case "order in remorse hold payed with SC":
                createNewCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Sellable", 1);
                updSKULineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 30000);
                setPayment_storeCredit(cartId, 10000);
                checkoutCart(cartId);
                break;

            //--------------------------------- CUSTOMER ADDRESS BOOK ---------------------------------//

            case "customer with a shipping address":
                createNewCustomer();
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            //--------------------------------- CUSTOMER CREDIT CARDS ---------------------------------//

            case "customer with a credit card":
                createNewCustomer();
                createAddress(customerId, customerName, 4177, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", false);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                break;

            case "customer with a credit card and 2 addresses":
                createNewCustomer();
                createAddress(customerId, customerName, 4164, 234, "New York", "545 Narrow Ave", "Suite 15", "New Jersey", "10201", "5551118888", false);
                createAddress(customerId, customerName, 4177, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", false);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                break;

            //----------------------------------- CUSTOMER'S ORDERS -----------------------------------//

            case "customer with 2 orders in remorse hold":

                createNewCustomer();
                createCart(customerId);
                increaseOnHandQty("SKU-YAX", "Sellable", 1);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                createCart(customerId);
                increaseOnHandQty("SKU-BRO", "Sellable", 2);
                updSKULineItems(cartId, "SKU-BRO", 2);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                break;

            case "customer with 2 orders in remorse hold and fulfillment started":
                createNewCustomer();
                createCart(customerId);
                increaseOnHandQty("SKU-YAX", "Sellable", 1);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                createCart(customerId);
                increaseOnHandQty("SKU-BRO", "Sellable", 2);
                updSKULineItems(cartId, "SKU-BRO", 2);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

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
                increaseOnHandQty("SKU-YAX", "Sellable", 1);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 3727);
                checkoutCart(cartId);
                break;

            case "order in Remorse Hold, payed with SC (GC Transfer)":
                createNewCustomer();
                createCart(customerId);
                increaseOnHandQty("SKU-YAX", "Sellable", 1);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit_gcTransfer(customerId, gcCode);
                setPayment_storeCredit(cartId, 3727);
                checkoutCart(cartId);
                break;

            //---------------------------------- PROMOTIONS ---------------------------------//

            case "active promotion with auto apply type":
                createPromotion_auto_active();
                break;

            case "inactive promotion with auto apply type":
                createPromotion_auto_inactive();
                break;

            //----------------------------------- COUPONS -----------------------------------//

            case "a promotion":
                createPromotion_coupon();
                sleep(5000);
                break;

            case "coupon with single code":
                createPromotion_coupon();
                sleep(5000);
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "coupon with bulk generated codes":
                createPromotion_coupon();
                sleep(5000);
                createCoupon(promotionId);
                bulkGenerateCodes(couponId, "BLKNWCPN" + couponId + "-", 4, 5);
                break;


            case "order in remorse hold with applied coupon":
                createPromotion_coupon();
                sleep(5000);
                createCoupon(promotionId);
                generateSingleCode(couponId);

                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                applyCouponCode(cartId, singleCouponCode);
                checkoutCart(cartId);
                break;

            //----------------------------------- PRODUCTS -----------------------------------//

            case "product in active state":
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                break;

            case "product in inactive state":
                createSKU_active();
                createProduct_inactive(sku, "sunglasses");
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

            //----------------------------------- INVENTORY -----------------------------------//

            case "active SKU for inventory":
                createSKU_active();
                sleep(5000);
                break;

            case "cart with backorder SKU":
                createNewCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Backorder", 1);
                updSKULineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with backorder and sellable SKUs":
                createNewCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Backorder", 1);
                increaseOnHandQty(sku, "Sellable", 1);
                updSKULineItems(cartId, sku, 2);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with backorder and sellable items of two SKUs":
                createNewCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Backorder", 1);
                updSKULineItems(cartId, sku, 1);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Sellable", 1);
                updSKULineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            //----------------------------------- GIFT CARDS -----------------------------------//

            case "gift card":
                issueGiftCard(5000, 1);
                break;

            case "used gift card":
                issueGiftCard(20000, 1);
                createNewCustomer();
                createCart(customerId);
                increaseOnHandQty("SKU-YAX", "Sellable", 1);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_giftCard(cartId, gcCode, 10000);
                checkoutCart(cartId);
                break;

            case "gift card on hold":
                issueGiftCard(20000, 1);
                putGCOnHold(gcCode);
                break;

            //--------------------------------- SEARCH CONTROLS ---------------------------------//

            case "saved search with 1 filter":
                createSharedSearch_oneFilter();
                break;

            case "saved search with 2 filters":
                createSharedSearch_twoFilters();
                break;

            //--------------------------------- SEARCH FILTERS ---------------------------------//

            case "order state: remorse hold":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                break;

            case "order state: manual hold":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                changeOrderState(orderId, "manualHold");
                break;

            case "order state: fraud hold":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                changeOrderState(orderId, "fraudHold");
                break;

            case "order state: fulfilment started":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                changeOrderState(orderId, "fulfilmentStarted");
                break;

            case "order state: shipped":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                changeOrderState(orderId, "shipped");
                break;

            case "order state: partially shipped":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                changeOrderState(orderId, "partiallyShipped");
                break;

            case "order state: canceled":
                createNewCustomer();
                createCart(customerId);
                updSKULineItems(cartId, "SKU-YAX", 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                changeOrderState(orderId, "canceled");
                break;

            case "customer with a billing address":
                createNewCustomer();
                createAddress(customerId, customerName, 4164, 234, "Oregon", "2101 Green Valley", "Suite 777", "Portland", "07097", "5557778686", false);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                updateCustomerShipAddress(customerId, addressId1, customerName, 4177, "4020 Green Valley", "Suite 4020", "Seattle", "98101", "9879879876");
                break;

            case "product for search tests":
                createSKU_active();
                createProduct_activeFromTo(sku, getDate(), getTomorrowDate());
                break;

            case "archived product":
                createSKU_active();
                createProduct_activeFromTo(sku, getDate(), getTomorrowDate());
                archiveProduct(productId);
                break;

            case "SKU for search tests":
                createSKU_search();
                break;

            case "archived SKU":
                createSKU_search();
                archiveSKU(sku);
                break;

        }

    }

    @Step("Create SKU in <State:'Active'>")
    private static void createSKU_test() throws IOException {

        System.out.println("Creating a new SKU, options: ACTIVE state...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;
        String title = "SKU Test Title " + randomId;

        JSONObject jsonObject = parse("bin/payloads/activeSKU.json");
        jsonObject.getJSONObject("attributes").getJSONObject("code").putOpt("v", skuCode);
        jsonObject.getJSONObject("attributes").getJSONObject("title").putOpt("v", title);
        String payload = jsonObject.toString();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/api/v1/skus/default")
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
            sku = skuCode;
            skuTitle = title;
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("Create product; <SKU:'{0}'>, <Tag:'{1}'>, <State:'Active'>")
    private static void createProduct_test(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        JSONObject jsonObject = parse("bin/payloads/activeProduct.json");
        jsonObject.getJSONArray("skus").getJSONObject(0).getJSONObject("attributes").getJSONObject("code").putOpt("v", sku);
        jsonObject.getJSONObject("attributes").getJSONObject("title").putOpt("v", productName_local);
        String payload = jsonObject.toString();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/api/v1/products/default")
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

    @Step("Increase amount of sellable unites of <{0}> by <{1}>")
    private static void increaseOnHandQty_test(String skuCode, String type, Integer qty) throws IOException {

        sleep(5000);
        viewSKU_inventory(skuCode);

        System.out.println("Increase amount of <" + type + "> items by <" + qty + "> for SKU <" + skuCode + ">, ID: <" + skuId_inventory + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"qty\":1,\n\t\"type\":\"Sellable\",\n\t\"status\":\"onHand\"\n}");
        Request request = new Request.Builder()
                .url("https://stage.foxcommerce.com/api/v1/inventory/stock-items/100/increment")
                .patch(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 204) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    public static void main(String[] args) throws IOException {
        loginAsAdmin();
        getCustomerAddress(450, 1038);
        JSONObject jsonObj = parse("bin/payloads/getStripeToken.json");
        JSONObject address_tmp = new JSONObject(addressPayload);
        JSONObject address = jsonObj.getJSONObject("address");

        address.putOpt("name", address_tmp.getString("name"));
        address.putOpt("zip", address_tmp.getString("zip"));
        address.putOpt("city", address_tmp.getString("city"));
        address.putOpt("address1", address_tmp.getString("address1"));
        address.putOpt("regionId", address_tmp.getJSONObject("region").getInt("id"));

        jsonObj.putOpt("customerId", 450);
        jsonObj.putOpt("address", address);
        jsonObj.putOpt("cardNumber", "4242424242424242");
        jsonObj.putOpt("expMonth", 10);
        jsonObj.putOpt("expYear", 2020);
        jsonObj.putOpt("cvv", 777);
        String str = jsonObj.toString();
        System.out.println(str);
    }

}


// 4177 - washington
// 4161 - ny
// 4164 - oregon
