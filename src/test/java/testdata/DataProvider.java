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
    protected static int orderGrandTotal;
    protected static String orderId;
    protected static int orderTotal;
    private static String jwt;
    protected static String randomId;

    protected static String customerName;
    public static String customerEmail;
    public static String takenEmail;
    protected static int addressId1;
    private static int addressId2;
    private static String addressPayload;

    protected static String gcCode;
    protected static int scId;
    private static int shipMethodId;
    protected static int creditCardId;
    private static String stripeToken;

    protected static String promotionId;
    protected static String couponId;
    protected static String couponName;
    public static String singleCouponCode;
    public static List<String> bulkCodes = new ArrayList<>();

    protected static String sku;
    protected static List<String> skus = new ArrayList<>();
    protected static String skuTitle;
    protected static int skuId_inventory;
    protected static String productName;
    protected static String productId;
    protected static String variantSKU_1;
    protected static String variantSKU_2;

    protected static int searchId;
    protected static String searchRandomId;
    protected static String searchCode;
    private static int adminId;

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
        if (!(responseCode == 200) || !(responseCode == 204)) {
            throw new RuntimeException(responseBody +
            "\nExpected:[200], Actual:[" + responseCode + "]");
        }
    }

    @Step("[API] Log in as admin")
    protected static void loginAsAdmin() throws IOException {

        System.out.println("Authorizing as an admin...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"email\": \"" + adminEmail + "\"," +
                "\n    \"password\": \"" + adminPassword + "\"," +
                "\n    \"org\": \"" + adminOrg + "\"\n}");

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

    @Step("[API] Create new customer")
    private static void createCustomer() throws IOException {

        System.out.println("Creating a new customer...");

        String randomID = generateRandomID();
        customerName = "Test Buddy-" + randomID;
        customerEmail = "qatest2278+" + randomID + "@mail.com";

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

    @Step("[API] Sign up a new customer -- name:<{0}> email:<{1}>")
    private static void signUpCustomer(String name, String email) throws IOException {
        System.out.println("Registering a new customer on Storefront...");

//        JSONObject jsonObj = parse("bin/payloads/signUpCustomer.json");
//        jsonObj.putOpt("email", email);
//        jsonObj.putOpt("name", name);
//        jsonObj.putOpt("password", "78qa22!#");
//        String payload = jsonObj.toString();

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"email\": \""+email+"\",\"name\": \""+name+"\",\"password\": \"78qa22!#\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/public/registrations/new")
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
            customerId = jsonData.getInt("id");
            customerName = jsonData.getString("name");
            customerEmail = jsonData.getString("email");
            System.out.println("Customer ID: " + customerId);
            System.out.println("Customer Name: " + customerName);
            System.out.println("Customer Email: " + customerEmail);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create cart for customer <{0}>")
    private static void createCart(int customerId) throws IOException {
        System.out.println("Creating a cart for customer <" + customerId + ">...");
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"customerId\": " + customerId + "}");
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

    @Step("[API] Get \"Grand Total\" of cart <{0}>")
    private static void getGrandTotal(String cartId) throws IOException {
        System.out.println("Getting Grand Total of cart <" + cartId + ">...");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/carts/" + cartId)
                .get()
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
            orderGrandTotal = jsonData.getJSONObject("result").getJSONObject("totals").getInt("total");
            System.out.println("Grand Total: <" + orderGrandTotal + ">");
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

    @Step("[API] Add line item: <{1}>, <QTY:{2}> to cart: <{0}>")
    private static void updLineItems(String cartId, String SKU, int quantity) throws IOException {
        System.out.println("Updating SKUs: setting <" + SKU + "> quantity to <" + quantity + ">...");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[" +
                "{\"sku\":\"" + SKU + "\"," +
                "\"quantity\":" + quantity + "}]");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/line-items")
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

    @Step("[API] Add line item: <{1}>, <QTY:{2}> to cart: <{0}>")
    private static void updLineItems_multiple(String cartId, String sku1, int qty1,String sku2, int qty2, String sku3, int qty3) throws IOException {
        System.out.println("Updating SKUs: <{1}:{2}>; <{3}:{4}>; <{5}:{6}>");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[{\n" +
                "  \"sku\": \"" + sku1 + "\",\n" +
                "  \"quantity\": " + qty1 + "\n" +
                "}, {\n" +
                "  \"sku\": \"" + sku2 + "\",\n" +
                "  \"quantity\": " + qty2 + "\n" +
                "}, {\n" +
                "  \"sku\": \"" + sku3 + "\",\n" +
                "  \"quantity\": " + qty3 + "\n" +
                "}]\n");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + cartId + "/line-items")
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

    @Step("[API] View customer <{0}>")
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

    @Step("[API] Create new shipping address for customer <{0}>")
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

    @Step("[API] Delete address <ID:{1}> from customer <ID:{0}>")
    protected static void deleteAddress(int customerId, int addressId) throws IOException {
        System.out.println("Deleting address <ID:" + addressId + "> from customer <ID:" + customerId + "> address book");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId)
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

        if (responseCode == 204) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Add new shipping address and set it as a chosen for cart <{0}>")
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

    @Step("[API] List all shipping addresses of customer <{0}>")
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

    @Step("[API] View address details, customerID: <{0}>, addressID: <{1}>")
    private static void getCustomerAddress(int customerId, int addressId) throws IOException {

        listCustomerAddresses(customerId);
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

    @Step("[API] Update shipping address details. addressID: <{1}>, customerId: <{0}>")
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


    @Step("[API] List possible shipping methods for cart <{0}>")
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
            shipMethodId = jsonData.getJSONObject(1).getInt("id");
            System.out.println("shipMethodId: <" + shipMethodId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Set shipping method <{1}> for cart <{1}>")
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

    @Step("[API] Create credit card")
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

    @Step("[API] Delete credit cart <ID:{1} of customer <ID:{0}>")
    protected void deleteCreditCard(int customerId, int creditCardId) throws IOException {

        System.out.println("Deleting credit cart <ID:{1} of customer <ID:{0}>...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards/" + creditCardId)
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

        if (responseCode == 204) {
            System.out.println(responseCode + " " + responseMsg);
            org.json.JSONObject jsonData = new org.json.JSONObject(responseBody);
            creditCardId = jsonData.getInt("id");
            System.out.println("Credit Card ID: <" + creditCardId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Set credit card <{1}> as a payment method for cart <{0}>")
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

    @Step("[API] Issue store credit in amount of <{1}> for customer <{0}>")
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

    @Step("[API] Transfer GC <{1}> to store credits for customer <{0}>")
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

    @Step("[API] Set store credit <ID:{0}> state to <{1}>")
    protected static void setStoreCreditState(int id, String state) throws IOException {

        System.out.println("Setting SC <ID:" + id + "> state to <" + state + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"state\": \"" + state + "\",\n  \"reasonId\": 1\n}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/store-credits/" + id)
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

    @Step("[API] Set store credits <amount: {1}> as a payment method for cart <{0}>")
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

    @Step("[API] Issue GC <balance: {0}>, <QTY: {1}>")
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
            JSONObject jsonData = new JSONObject(responseBody);
            gcCode = jsonData.getString("code");
            System.out.println("GC code: <" + gcCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    /**
     * Possible state values: active, onHold, canceled
     */
    @Step("[API] Set GC <{0}> state to <{1}>")
    protected static void setGiftCardState(String gcNumber, String state) throws IOException {

        System.out.println("Putting <" + gcNumber + "> on hold..." );

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"state\": \"" + state + "\"," +
                "\"reasonId\": 1}");
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

    @Step("[API] Set GC <gcNumber: {1}>, <amount: {2}> for cart <{0}>")
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

    @Step("[API] Checkout cart <{0}>")
    protected static void checkoutCart(String cartId) throws IOException {

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

    @Step("[API] Change order <{0}> state to <{1}>")
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

    @Step("[API] Create promotion -- <Apply type: 'Coupon'>")
    private static void createPromotion_coupon() throws IOException {
        System.out.println("Creating a new promotion...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\": null,\"createdAt\": null,\"attributes\": {\"name\": {\"t\": \"string\",\"v\": \"new promo\"},\"storefrontName\": {\"t\": \"richText\",\"v\": \"<p>SF new promo</p>\"},\"description\": {\"t\": \"text\",\"v\": \"new promo Description\"},\"details\": {\"t\": \"richText\",\"v\": \"<p>new promo 775 Details</p>\"\n    }\n  },\n  \"discounts\": [{\n    \"id\": null,\n    \"createdAt\": null,\n    \"attributes\": {\n      \"qualifier\": {\n        \"t\": \"qualifier\",\n        \"v\": {\n          \"orderAny\": {}}},\"offer\": {\"t\": \"offer\",\"v\": {\"orderPercentOff\": {\"discount\": 10}}}}}],\"applyType\": \"coupon\"}");
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
            promotionId = String.valueOf(jsonData.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Create promotion -- <Apply type: 'Coupon'>")
    private static void createPromotion_coupon_itemsNoQual(int searchId) throws IOException {
        System.out.println("Creating a new promotion...");
        String randomId = generateRandomID();

        JSONObject jsonObj = parse("bin/payloads/createPromotion_coupon_itemsNoQual.json");
        jsonObj.getJSONArray("discounts")
                .getJSONObject(0)
                .getJSONObject("attributes")
                .getJSONObject("qualifier")
                .getJSONObject("v")
                .getJSONObject("itemsAny")
                .getJSONArray("search")
                .getJSONObject(0)
                .putOpt("productSearchId", searchId);
        jsonObj.getJSONObject("attributes")
                .getJSONObject("name")
                .putOpt("v", "Test Promo" + randomId);
        jsonObj.getJSONObject("attributes")
                .getJSONObject("storefrontName")
                .putOpt("v", "SF Test Promo" + randomId);
        String payload = jsonObj.toString();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
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
            promotionId = String.valueOf(jsonData.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Create promotion -- <Apply type: 'Auto'>, <State: 'Inactive'>")
    private static void createPromotion_auto_inactive() throws IOException {

        System.out.println("Creating a new promotion...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"createdAt\":null,\"attributes\":{\"name\":{\"t\":\"string\",\"v\":\"new promo " + generateRandomID() + "\"},\"storefrontName\":{\"t\":\"richText\",\"v\":\"<p>sf new promo</p>\"},\"description\":{\"t\":\"text\",\"v\":\"promo description\"},\"details\":{\"t\":\"richText\",\"v\":\"<p>promo details</p>\"}},\"discounts\":[{\"id\":null,\"createdAt\":null,\"attributes\":{\"qualifier\":{\"t\":\"qualifier\",\"v\":{\"orderAny\":{}}},\"offer\":{\"t\":\"offer\",\"v\":{\"orderPercentOff\":{\"discount\":10}}}}}],\"applyType\":\"auto\"}");
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
            promotionId = String.valueOf(jsonData.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create promotion -- <Apply type: 'Auto'>, <State: 'Active'>")
    private static void createPromotion_auto_active() throws IOException {

        System.out.println("Creating a new promotion...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\": null,\"createdAt\": null,\"attributes\": {\"name\": {\"t\": \"string\",\"v\": \"new promo " + generateRandomID() + "\"},\"storefrontName\": {\"t\": \"richText\",\"v\": \"<p>sf name</p>\"},\"description\": {\"t\": \"text\",\"v\": \"promo description\"},\"details\": {\"t\": \"richText\",\"v\": \"<p>promo details</p>\"},\"activeFrom\": {\"v\": \"" + getDate() + "T13:53:00.092Z\",\"t\": \"datetime\"},\"activeTo\": {\"v\": null,\"t\": \"datetime\"}},\"discounts\": [{\"id\": null,\"createdAt\": null,\"attributes\": {\"qualifier\": {\"t\": \"qualifier\",\"v\": {\"orderAny\": {}}},\"offer\": {\"t\": \"offer\",\"v\": {\"orderPercentOff\": {\"discount\": 10}}}}}],\"applyType\": \"auto\"}");
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
            promotionId = String.valueOf(jsonData.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create coupon with promotion <ID:{0}>")
    private static void createCoupon(String promotionId) throws IOException {

        System.out.println("Creating a new coupon with promotion <" + promotionId + ">...");
        String randomId = generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\": null,\"createdAt\": null,\"attributes\": {\"usageRules\": {\"t\": \"usageRules\",\"v\": {\"isExclusive\": false,\"isUnlimitedPerCode\": true,\"usesPerCode\": 1,\"isUnlimitedPerCustomer\": true,\"usesPerCustomer\": 1}},\"name\": {\"t\": \"string\",\"v\": \"test coupon " + randomId + "\"},\"storefrontName\": {\"t\": \"richText\",\"v\": \"storefront name " + randomId + "\"},\"description\": {\"t\": \"text\",\"v\": \"test description\"},\"details\": {\"t\": \"richText\",\"v\": \"<p>test details</p>\"},\"activeFrom\": {\"v\": \"2017-01-24T22:03:58.698Z\",\"t\": \"datetime\"},\"activeTo\": {\"v\": null,\"t\": \"datetime\"}},\"promotion\": " + promotionId + "}");
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

    @Step("[API] Generate a single code for coupon <ID:'{0}>'")
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

    @Step("[API] Bulk generate codes for coupon <ID:{0}>; [prefix:{1}, codeLength:{2}, QTY:{3}]")
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
//            DEBUG
//            int startIndex = 2;
//            for (int i = 0; i < quantity; i++) {
//                String code = responseBody.substring(startIndex, startIndex + length);
//                bulkCodes.add(code);
//                startIndex += (length + 3);
//            }
            bulkCodes.clear();
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

    @Step("[API] Apply coupon <ID:'{1}'> to cart <{0}>")
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

    @Step("[API] Change store credit <ID:'{0}'> state to <{1}>")
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

    @Step("[API] Create SKU in <State:'Active'>")
    protected static void createSKU_active() throws IOException {

        System.out.println("Creating a new SKU, options: ACTIVE state...");
        String randomId = generateRandomID();
        String skuCode = "SKU-" + randomId;
        String title = "SKU Test Title " + randomId;

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + skuCode + "\"},\"title\":{\"t\":\"string\",\"v\":\"" + title + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Test description</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"" + getDate() + "T00:03:26.685Z\"},\"activeTo\":{\"t\":\"datetime\",\"v\":null}}}");
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

    @Step("[API] Create SKU in <State: 'Inactive'>")
    protected static void createSKU_inactive() throws IOException {

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

    @Step("[API] Create SKU with empty 'Title'")
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

    @Step("[API] Create SKU with empty 'Description'")
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

    @Step("[API] Create SKU without specifying the prices")
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

    @Step("[API] Check if Inventory is available")
    protected static void checkInventoryAvailability(String sku) throws IOException {

        System.out.println("Checking if inventory of SKU <" + sku + "> is available...");

        int responseCode = 0;
        String responseBody = "";
        String responseMsg = "";
        long time = System.currentTimeMillis();
        long end = time + 10000;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/inventory/summary/" + sku)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        while((System.currentTimeMillis() < end) && (responseCode != 200)) {
            Response response = client.newCall(request).execute();
            responseBody = response.body().string();
            responseCode = response.code();
            responseMsg = response.message();
        }

        if (responseCode == 200) {
            System.out.println("Inventory is created");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create product; <SKU: auto-created with product>, <State:'Active'>")
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

    @Step("[API] Create product; <SKU:'{0}'>, <Tag:'{1}'>, <State:'Active'>")
    protected static void createProduct_active(String sku, String tag) throws IOException {

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

    @Step("[API] Create product; <SKU:'{0}'> <State:'Active'>, no tag")
    protected static void createProduct_active_noTag(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"activeFrom\": {\"t\": \"date\",\"v\": \"2016-07-26T14:48:12.493Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}},\"skus\":[{\"feCode\":\"F0QGTGBBINBQF5V53TYB9\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}}}}]}");
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

    @Step("[API] Create product; <SKU:'{0}'>, <Tag:'{1}'>, <State:'Inactive'>")
    protected static void createProduct_inactive(String sku, String tag) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"},\"tags\":{\"t\":\"tags\",\"v\":[\"" + tag + "\"]}},\"skus\":[{\"feCode\":\"JBV96IF5QRNZM9KQ33DI\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}}}}]}");
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

    @Step("[API] Create product; <SKU:'{0}'>, no tag, <State:'Inactive'>")
    private static void createProduct_inactive_noTag(String sku) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"The best thing to buy in 2016!\"},\"title\":{\"t\":\"string\",\"v\":\"" + productName_local + "\"}},\"skus\":[{\"feCode\":\"KB0SOK5PSSBEPP5H4CXR\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}}}}]}");
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

    @Step("[API] Create product; <SKU:'{0}'>, <Tag:'sunglasses'>, <Active From:'{1}'>, <Active To:'{2}'>")
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

    /**
     * Product is active -- activeFrom equals the time of prod creation, activeTo is null
     * Title -- parametrized
     * 1 option with 2 values -- hardcoded
     * 2 SKUs for each variant -- not parametrized, created using generateRandomId(), stored in variables
     * SKU prices -- hardcoded
     */
    @Step("[API] Create product with variants")
    private static void createProduct_variants(String title) throws IOException {

        System.out.println("Creating a product with 2 variants...");
        System.out.println("* * * * * * * * * *");
        // create SKU, then store its code to a local var to use it in prod creation payload
        createSKU_active();
        String sku_1 = sku;
        createSKU_active();
        String sku_2 = sku;
        System.out.println("* * * * * * * * * *");

        JSONObject jsonObj = parse("bin/payloads/productWithVariants.json");
        jsonObj.getJSONObject("attributes").getJSONObject("title").put("v", title);

        JSONArray tempJSONArray;
        // Store JSONArray with SKU code to variable, delete its only value, put a new value SKU code instead
        // (check productWithVariants.json for schema details)
        tempJSONArray = jsonObj.getJSONArray("variants").getJSONObject(0).getJSONArray("values").getJSONObject(0).getJSONArray("skuCodes");
        tempJSONArray.remove(0);
        tempJSONArray.put(sku_1);

        tempJSONArray = jsonObj.getJSONArray("variants").getJSONObject(0).getJSONArray("values").getJSONObject(1).getJSONArray("skuCodes");
        tempJSONArray.remove(0);
        tempJSONArray.put(sku_2);

        String payload = jsonObj.toString();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();
        // TODO: COMPLETE IT -- need to store variantSKU_1 and variantSKU_2 from response
        JSONObject jsonData = new JSONObject(responseBody);
        productName = jsonData.getJSONObject("attributes").getJSONObject("title").getString("v");
        variantSKU_1 = (String) jsonData.getJSONArray("variants").getJSONObject(0).getJSONArray("values").getJSONObject(0).getJSONArray("skuCodes").get(0);
        variantSKU_1 = (String) jsonData.getJSONArray("variants").getJSONObject(0).getJSONArray("values").getJSONObject(1).getJSONArray("skuCodes").get(0);
        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Archive product <ID:'{0}'>")
    protected static void archiveProduct(String productId) throws IOException {

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

    @Step("[API] Create shared search with one search filter")
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

    @Step("[API] Create shared search with a single product: <{0}>")
    private static void createSharedSearch_singleProduct(String productName) throws IOException {

        System.out.println("Creating a new shared search...");
        searchRandomId = generateRandomID();

        JSONObject jsonObj = parse("bin/payloads/createSharedSearch_singleProduct.json");
        jsonObj.putOpt("title", "Search " + searchRandomId);
        jsonObj.getJSONArray("query").getJSONObject(1).putOpt("display", "Product : Name : " + productName);
        jsonObj.getJSONArray("query").getJSONObject(1).getJSONObject("value").putOpt("value", productName);
        jsonObj.getJSONObject("rawQuery").getJSONObject("query").getJSONObject("bool")
                .getJSONArray("must").getJSONObject(0).getJSONObject("match").getJSONObject("title")
                .putOpt("query", productName);
        String payload = jsonObj.toString();
        System.out.println(payload);

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
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
            searchId = jsonData.getInt("id");
            searchCode = jsonData.getString("code");
            System.out.println("Search ID: <" + searchId + ">");
            System.out.println("Search code: <" + searchCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create shared search, search filter: <{0}>, title: <{1}>")
    private static void createSharedSearch(String filterVal, String title) throws IOException {
        System.out.println("Creating a new shared search...");
        searchRandomId = generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"title\": \"" + title + "\",\"query\": [{\"term\": \"archivedAt\",\"hidden\": true,\"operator\": \"missing\",\"value\": {\"type\": \"exists\"}}, {\"display\": \"" + filterVal + "\",\"term\": \"_all\",\"operator\": \"eq\",\"value\": {\"type\": \"string\",\"value\": \"" + filterVal + "\"}}],\"scope\": \"productsScope\",\"rawQuery\": {\"query\": {\"bool\": {\"filter\": [{\"missing\": {\"field\": \"archivedAt\"}}],\"must\": [{\"match\": {\"_all\": {\"query\": \"" + filterVal + "\",\"analyzer\": \"standard\",\"operator\": \"and\"}}}]}}}}");
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

    @Step("[API] Create shared search with two search filters")
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

    @Step("[API] Share search <searchCode:'{0}'> with <{1}>")
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

    @Step("[API] Delete search <searchId:'{0}'>")
    protected static void deleteSearch(String searchCode) throws IOException {
        System.out.println("Deleting saved search with id <" + searchCode + ">.");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/shared-search/" + searchCode)
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

        if (responseCode == 204) {
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

    /**
     * Given arg "String type" value should be capitalized, e.g. - "Sellable"
     */
    @Step("[API] Increase amount of sellable unites of <{0}> by <{1}>")
    protected static void increaseOnHandQty(String skuCode, String type, Integer qty) throws IOException {

        checkInventoryAvailability(sku);
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

    @Step("[API] Archive SKU <{0}>")
    protected static void archiveSKU(String skuCode) throws IOException {

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

    @Step("Create test data using API -- <{0}>")
    protected void provideTestData(String testMethodName) throws IOException {

        skus.clear();
        System.out.println("==== ==== ==== ====");
        loginAsAdmin();

        switch(testMethodName) {

            //------------------------------------- SEARCH FILTERS ------------------------------------//

            case "a customer":
                createCustomer();
                break;

            //----------------------------------------- CART ITEMS -----------------------------------------//

            case "empty cart and 3 active products":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                createSKU_active();
                createProduct_active(sku, "test");
                skus.add(sku);
                createSKU_active();
                createProduct_active(sku, "test");
                skus.add(sku);
                createSKU_active();
                createProduct_active(sku, "test");
                skus.add(sku);
                break;

            case "cart<1 SKU[active, qty: 1]>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                updLineItems(cartId, sku, 1);
                break;

            case "cart with 3 items":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                skus.add(sku);
                createSKU_active();
                createProduct_active(sku, "test");
                skus.add(sku);
                createSKU_active();
                createProduct_active(sku, "test");
                skus.add(sku);
                updLineItems_multiple(cartId, skus.get(0), 4, skus.get(1), 5, skus.get(2), 3);
                break;

            case "cart with 1 item, qty: 3":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                updLineItems(cartId, sku, 3);
                break;

            //------------------------------------- CART SHIPPING ADDRESS -------------------------------------//

            case "cart with empty address book":
                createCustomer();
                createCart(customerId);
                break;

            case "cart with non-empty address book":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with chosen shipping address":
                createCustomer();
                createCart(customerId);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 2 addresses in address book":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            case "cart with 2 addresses and defined default shipping address":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            case "filled out cart 2 addresses in address book":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                break;

            case "filled out cart":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 10);
                updLineItems(cartId, sku, 3);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                break;


            //----------------------------------- CART COUPONS ------------------------------------//

            case "empty cart":
                createCustomer();
                createCart(customerId);
                break;

//            case "cart<empty>; coupon<any, single code>":
//                createCustomer();
//                createCart(customerId);
//                createPromotion_coupon();
//                createCoupon(promotionId);
//                generateSingleCode(couponId);
//                break;

            case "cart<1 SKU>, coupon<any, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                updLineItems(cartId, sku, 3);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "cart<1 SKU>; coupon<any, bulk generated codes>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                updLineItems(cartId, sku, 3);
                createPromotion_coupon();
                createCoupon(promotionId);
                bulkGenerateCodes(couponId, "BULK CPNS ", 5, 3);
                break;

            case "cart<1 SKU, coupon applied>; coupon<any, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                updLineItems(cartId, sku, 3);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                applyCouponCode(cartId, singleCouponCode);
                break;

//            case "cart<empty, coupon applied>; coupon<any, bulk generated codes>":
//                createCustomer();
//                createCart(customerId);
//                createPromotion_coupon();
//                createCoupon(promotionId);
//                bulkGenerateCodes(couponId, "BLK_NWCPN-", 5, 3);
//                applyCouponCode(cartId, bulkCodes.get(0));
//                break;

            case "cart<1 SKU in stock, shipAddress, shipMethod, coupon applied, payMethod[SC]>; coupon<any, single code>":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                createSKU_active();
                createProduct_active(sku, "test");
                updLineItems(cartId, sku, 1);
                increaseOnHandQty(sku, "Sellable", 1);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "cart<1 SKU>; coupon<items -- no qualifier/percent off, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                updLineItems(cartId, sku, 1);
                createSharedSearch_singleProduct(productName);
                createPromotion_coupon_itemsNoQual(searchId);
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            //------------------------------------- CART PAYMENT METHOD -------------------------------------//

            case "cart with 1 item and chosen shipping address":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 1 item && customer with GC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item && customer with SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                updLineItems(cartId, sku, 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method, and credit card payment":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with 1 item, shipping method and issued SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method and issued GC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item, shipping method, credit card payment and issued SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
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
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
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
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item && SC onHold":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "onHold");
                break;

            case "cart with 1 item && SC canceled":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "canceled");
                break;

            //----------------------------------- CART VALIDATION -----------------------------------//

            case "cart<filled out, payment method: SC>":
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Sellable", 20);
                createCustomer();
                issueStoreCredit(customerId, 20000);
                createCart(customerId);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                setPayment_storeCredit(cartId, 20000);
                listCustomerAddresses(customerId);
                break;

            case "cart<filled out, payment method: GC>":
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Sellable", 20);
                createCustomer();
                issueGiftCard(20000, 1);
                createCart(customerId);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                setPayment_giftCard(cartId, gcCode, 20000);
                break;

            case "cart<filled out, payment method: CC>":
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Sellable", 20);
                createCustomer();
                createCart(customerId);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "filled out cart, product with variants, SC as a payment method":
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Sellable", 20);
                createCustomer();
                createCart(customerId);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            //------------------------------------- ORDER STATE -------------------------------------//

            case "order in remorse hold":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
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
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                checkoutCart(cartId);
                break;

            //--------------------------------- CUSTOMER ADDRESS BOOK ---------------------------------//

            case "customer with a shipping address":
                createCustomer();
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            //--------------------------------- CUSTOMER CREDIT CARDS ---------------------------------//

            case "customer with a credit card":
                createCustomer();
                createAddress(customerId, customerName, 4177, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", false);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                break;

            case "customer with a credit card and 2 addresses":
                createCustomer();
                createAddress(customerId, customerName, 4164, 234, "New York", "545 Narrow Ave", "Suite 15", "New Jersey", "10201", "5551118888", false);
                createAddress(customerId, customerName, 4177, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", false);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                break;

            //----------------------------------- CUSTOMER'S ORDERS -----------------------------------//

            case "customer with 2 orders in remorse hold":

                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                skus.add(sku);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                skus.add(sku);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                break;

            case "customer with 2 orders in remorse hold and fulfillment started":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                skus.add(sku);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 2);
                updLineItems(cartId, sku, 2);
                skus.add(sku);
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
                createCustomer();
                issueStoreCredit(customerId, 50000);
                break;

            case "a customer && GC":
                createCustomer();
                issueGiftCard(12500, 1);
                break;

            case "order in Remorse Hold, payed with SC (CSR Appeasement)":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueStoreCredit(customerId, 50000);
                getGrandTotal(cartId);
                setPayment_storeCredit(cartId, orderGrandTotal);
                checkoutCart(cartId);
                break;

            case "order in Remorse Hold, payed with SC (GC Transfer)":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit_gcTransfer(customerId, gcCode);
                getGrandTotal(cartId);
                setPayment_storeCredit(cartId, orderGrandTotal);
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
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
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
                checkInventoryAvailability(sku);
                break;

            case "cart with backorder SKU":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Backorder", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with backorder and sellable SKUs":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Backorder", 1);
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 2);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with backorder and sellable items of two SKUs":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Backorder", 1);
                updLineItems(cartId, sku, 1);
                createSKU_active();
                createProduct_active(sku, "sunglasses");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
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
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, "test");
                increaseOnHandQty(sku, "Sellable", 1);
                updLineItems(cartId, sku, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_giftCard(cartId, gcCode, 10000);
                checkoutCart(cartId);
                break;

            case "gift card on hold":
                issueGiftCard(20000, 1);
                setGiftCardState(gcCode, "onHold");
                break;

            //--------------------------------- SEARCH CONTROLS ---------------------------------//

            case "saved search with 1 filter":
                createSharedSearch_oneFilter();
                break;

            case "saved search with 2 filters":
                createSharedSearch_twoFilters();
                break;

            //------------------------------------- SF: AUTH ------------------------------------//

            case "a customer signed up on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                break;

            case "two customers signed up on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                takenEmail = customerEmail;
                signUpCustomer("Test Buddy " + generateRandomID(), "qatest2278+" + generateRandomID() + "@gmail.com");
                break;

            //------------------------------------- SF: CART ------------------------------------//

            case "registered customer, active product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(sku, "test");
                createCart(customerId);
                updLineItems(cartId, sku, 1);
                break;

            case "registered customer, active product on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(sku, storefrontCategory);
                break;

            //---------------------------------- SF: SHIPPING ADDRESS --------------------------------//

            case "a storefront signed up customer with a shipping address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

            case "a storefront signed up customer with 2 shipping addresses":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                createAddress(customerId,
                        "Paul Puga",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                break;

            case "a storefront signed up customer with a product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, storefrontCategory);
                updLineItems(cartId, sku, 1);
                break;

            case "a storefront signed up customer with a shipping address and a product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(sku, storefrontCategory);
                updLineItems(cartId, sku, 1);
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

        }
    }

}


// 4177 - washington
// 4161 - ny
// 4164 - oregon