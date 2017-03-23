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

public class Preconditions extends ApiRequests {

    protected static int customerId;
    protected static String cartId;
    protected static int subTotal;
    protected static int taxes;
    protected static int shipping;
    protected static int adjustments;
    protected static int total;
    protected static int customersExpenses;

    protected static String orderId;
    private static String jwt;
    protected static String randomId;

    protected static String customerName;
    public static String customerEmail;
    public static String guestOrderEmail;
    public static String takenEmail;
    protected static int addressId1;
    private static int addressId2;
    private static String addressPayload;

    protected static String gcCode;
    protected static int scId;
    private static int shipMethodId;
    protected static int creditCardId;
    protected static List<Integer> creditCardsIDs = new ArrayList<>();
    private static String stripeToken;

    protected static String promotionId;
    protected static String couponId;
    protected static String couponName;
    public static String singleCouponCode;
    public static List<String> bulkCodes = new ArrayList<>();

    protected static String skuCode;
    protected static int skuId;
    protected static List<String> skuCodes = new ArrayList<>();
    protected static String skuTitle;
    protected static int skuId_inventory;
    protected static String productTitle;
    protected static String productSlug;
    protected static List<String> products = new ArrayList<>();
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

    private static boolean assertProductExists_es(String jsonAttrType, String attrName, String attrVal, String responseBody) {
        JSONObject responseJSON = new JSONObject(responseBody);
        JSONArray productsArr = responseJSON.getJSONArray("result");
        jsonAttrType = jsonAttrType.toLowerCase();

        if (jsonAttrType.equals("string")) {
            for (int i = 0; i < productsArr.length(); i++) {
                String foundAttr = productsArr.getJSONObject(i).getString(attrName);
                if (foundAttr.equals(attrVal)) {
                    return true;
                }
            }
        } else if (jsonAttrType.equals("int")) {
            int intAttrVal = Integer.valueOf(attrVal);
            for (int i = 0; i < productsArr.length(); i++) {
                int foundAttr = productsArr.getJSONObject(i).getInt(attrName);
                if (foundAttr == intAttrVal) {
                    return true;
                }
            }
        }

        return false;
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
        customerEmail = "qatest2278+" + randomID + "@gmail.com";

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
            JSONObject responseJSON = new JSONObject(responseBody);
            customerId = responseJSON.getInt("id");
            System.out.println("Customer ID: " + customerId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Sign up a new customer -- name:<{0}> email:<{1}>")
    private static void signUpCustomer(String name, String email) throws IOException {
        System.out.println("Registering a new customer on Storefront...");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"email\": \""+email+"\"," +
                                                          "\"name\": \""+name+"\"," +
                                                          "\"password\": \"78qa22!#\"}");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            customerId = responseJSON.getInt("id");
            customerName = responseJSON.getString("name");
            customerEmail = responseJSON.getString("email");
            System.out.println("Customer ID: " + customerId);
            System.out.println("Customer Name: " + customerName);
            System.out.println("Customer Email: " + customerEmail);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Blacklist customer ID:<{0}>")
    public static void blacklistCustomer(int customerId) throws IOException {
        System.out.println("Blacklisting customer ID:<" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"blacklisted\": true\n}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/blacklist")
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
            JSONObject responseJSON = new JSONObject(responseBody);
            cartId = responseJSON.getString("referenceNumber");
//            orderId = responseBody.substring(20, responseBody.indexOf(",", 20));
            System.out.println("Cart ID: <" + cartId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Get email used for guest checkout, order:<{0}>")
    public static void getGuestOrderEmail(String orderId) throws IOException {
        System.out.println("Getting email used for guest checkout, order:<" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/" + orderId)
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
            JSONObject responseJSON = new JSONObject(responseBody);
            guestOrderEmail = responseJSON.getJSONObject("result").getJSONObject("customer").getString("email");
            System.out.println("Cart ID: <" + cartId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Get all totals of cart <{0}>")
    public static void getCartTotals(String cartId) throws IOException {
        System.out.println("Get all totals of cart <" + cartId + ">...");

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
            JSONObject responseJSON = new JSONObject(responseBody);
            subTotal = responseJSON.getJSONObject("result").getJSONObject("totals").getInt("subTotal");
            taxes = responseJSON.getJSONObject("result").getJSONObject("totals").getInt("taxes");
            shipping = responseJSON.getJSONObject("result").getJSONObject("totals").getInt("shipping");
            adjustments = responseJSON.getJSONObject("result").getJSONObject("totals").getInt("adjustments");
            customersExpenses = responseJSON.getJSONObject("result").getJSONObject("totals").getInt("customersExpenses");
            total = responseJSON.getJSONObject("result").getJSONObject("totals").getInt("total");
            System.out.println("Grand Total: <" + total + ">");
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
            JSONArray responseJSON = new JSONArray(responseBody);
            addressId1 = responseJSON.getJSONObject(0).getInt("id");
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
            JSONArray responseJSON = new JSONArray(responseBody);
            shipMethodId = responseJSON.getJSONObject(0).getInt("id");
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
            org.json.JSONObject responseJSON = new org.json.JSONObject(responseBody);
            stripeToken = responseJSON.getString("token");
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
            org.json.JSONObject responseJSON = new org.json.JSONObject(responseBody);
            creditCardId = responseJSON.getInt("id");
            System.out.println("Credit Card ID: <" + creditCardId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] List all credit cards of customer ID:<{0}>")
    protected void listCreditCards(String customerId) throws IOException {
        System.out.println("List all credit cards of customer ID:<{0}");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards")
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
            JSONArray responseJSON = new JSONArray(responseBody);

            for (int i = 0; i < responseJSON.length(); i++) {
                creditCardId = responseJSON.getJSONObject(i).getInt("id");
                creditCardsIDs.add(creditCardId);
            }
            printIntList(creditCardsIDs);

            System.out.println(responseCode + " " + responseMsg);
            System.out.println("Success!");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Set credit card ID:<{1}> as default card for customer ID:<{0}>")
    protected void setCardAsDefault(int customerId, int creditCardId) throws IOException {
        System.out.println("Set credit card ID:<" + customerId + "> as default for customer ID:<" + creditCardId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"isDefault\": true\n}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards/" + creditCardId + "/default")
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
            System.out.println("Success!");
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
            org.json.JSONObject responseJSON = new org.json.JSONObject(responseBody);
            creditCardId = responseJSON.getInt("id");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            scId = responseJSON.getInt("id");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            scId = responseJSON.getInt("id");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            gcCode = responseJSON.getString("code");
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
    protected static void setPayment_giftCard(String cartId, String gcCode, int amount) throws IOException {

        System.out.println("Setting gift card <"+ gcCode + "> in amount of <" + amount + "> as a payment for order <" + cartId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"code\":\"" + gcCode + "\"," +
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

    @Step("[API] Wait for <{1}> orders to apeear in ES")
    public static void waitForOrdersToAppearInES(int customerId, int ordersExpectedAmount) throws IOException {
        System.out.println("Wait for <" + ordersExpectedAmount + "> orders to appear in ES");

        JSONObject jsonObj = parse("bin/payloads/esSearchCustomer.json");
        jsonObj.getJSONObject("query")
                .getJSONObject("bool")
                .getJSONArray("filter")
                .getJSONObject(0)
                .getJSONObject("nested")
                .getJSONObject("query")
                .getJSONObject("term")
                .putOpt("customer.id", customerId);
        String payload = jsonObj.toString();

        int responseCode = 0;
        String responseBody = "";
        String responseMsg = "";
        long time = System.currentTimeMillis();
        long end = time + 15000;
        int ordersInEs = 0;
        int totalTries = 0;

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/search/admin/orders_search_view/_search?size=50")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        while((System.currentTimeMillis() < end) && (ordersInEs != ordersExpectedAmount)) {
            Response response = client.newCall(request).execute();
            responseBody = response.body().string();
            responseCode = response.code();
            responseMsg = response.message();

            JSONObject jsonResponse = new JSONObject(responseBody);
            try {
                ordersInEs = jsonResponse.getJSONArray("result").length();
            } catch(org.json.JSONException ignored) {}
            totalTries ++;
        }
        System.out.println("total tries: <" + totalTries + ">");

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("Orders in ES: <" + ordersInEs + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Check if product<({0}) {1}: {2}> is present in category view on storefront")
    private static void checkProductPresenceInCategoryView(String attrType, String attrName, String attrVal) throws IOException {
        System.out.println("Checking if product<("+attrType+") "+attrName+": "+attrVal+"> is present in category view on storefront...");

        JSONObject jsonObj = parse("bin/payloads/esCatalogView.json");
        String payload = jsonObj.toString();

        int responseCode = 0;
        String responseBody = "";
        String responseMsg = "";
        long time = System.currentTimeMillis();
        long end = time + 15000;
        int totalTries = 0;
        boolean productInEs = false;

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/search/public/products_catalog_view/_search?size=1000")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        while((System.currentTimeMillis() < end) && (productInEs != true)) {
            Response response = client.newCall(request).execute();
            responseBody = response.body().string();
            responseCode = response.code();
            responseMsg = response.message();

            totalTries ++;
            productInEs = assertProductExists_es(attrType, attrName, attrVal, responseBody);
        }
        System.out.println("total tries: <" + totalTries + ">");
        System.out.println("Time spent: <" + (System.currentTimeMillis() - time) + " MS>");

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("Orders in ES: <" + productInEs + ">");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            promotionId = String.valueOf(responseJSON.getInt("id"));
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
            JSONObject responseJSON = new JSONObject(responseBody);
            promotionId = String.valueOf(responseJSON.getInt("id"));
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
            JSONObject responseJSON = new JSONObject(responseBody);
            promotionId = String.valueOf(responseJSON.getInt("id"));
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
            JSONObject responseJSON = new JSONObject(responseBody);
            promotionId = String.valueOf(responseJSON.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create coupon with promotion ID:<{0}>")
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
            JSONObject responseJSON = new JSONObject(responseBody);
            couponId = String.valueOf(responseJSON.getInt("id"));
            couponName = "test coupon " + randomId;
            System.out.println("Coupon ID: " + couponId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Generate a single code for coupon ID:<{0}>")
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
            JSONArray responseJSON = new JSONArray(responseBody);
            for (int i = 0; i < responseJSON.length(); i++) {
                bulkCodes.add(responseJSON.getString(i));
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

    @Step("[API] Set promo <ID:{0}> state to <{1}>")
    public static void setPromoState(String promotionId, String newState) {
        System.out.println("Setting promotion<ID:" + promotionId + "> state to <" + newState + ">...");

        //TODO: finish this once discounts re-implementaion will land
    }

    @Step("[API] Set coupon <ID:{0}> state to <{1}>")
    public static void setCouponState(String couponId, String newState) {
        System.out.println("Setting coupon<ID:" + couponId + "> state to <" + newState + ">...");

        //TODO: finish this once discounts re-implementaion will land
    }

    @Step("[API] Archive promo <ID:{0}>")
    public static void archivePromo(String promotionId) {
        System.out.println("Archiving promo<ID:" + promotionId + ">...");

        //TODO: finish this once discounts re-implementaion will land
    }

    @Step("[API] Archive coupon <ID:{0}>")
    public static void archiveCoupon(String couponId) {
        System.out.println("Archiving coupon<ID:" + couponId + ">...");

        //TODO: finish this once discounts re-implementaion will land
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

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"SKU-" + randomId + "\"},\"title\":{\"t\":\"string\",\"v\":\"SKU Test Title " + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Test description</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"" + getDate() + "T00:03:26.685Z\"},\"activeTo\":{\"t\":\"datetime\",\"v\":null}}}");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
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

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"SKU-" + randomId + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":5000}}}}");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
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

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"SKU-" + randomId + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1215}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1215}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1000}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"2016-07-29T00:03:26.685Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
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

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"SKU-" + randomId + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1215}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1215}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":1000}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"2016-07-29T00:03:26.685Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
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

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"SKU-" + randomId + "\"},\"title\":{\"t\":\"string\",\"v\":\"Test Product #" + randomId + "\"},\"upc\":{\"t\":\"string\",\"v\":\"Test UPC\"},\"description\":{\"t\":\"richText\",\"v\":\"<p>Just another test SKU.</p>\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":null}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":null}},\"unitCost\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":null}},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"2016-07-29T00:03:26.685Z\"},\"activeTo\":{\"v\":null,\"t\":\"datetime\"}}}");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
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
        long end = time + 15000;
        int totalTries = 0;

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
            totalTries ++;
        }
        System.out.println("totalTries: <" + totalTries + ">");
        System.out.println("Time spent: <" + (System.currentTimeMillis() - time) + " MS>");

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
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create product; SKU:<{0}>, Tag:<{1}>, State:<Active>")
    protected static void createProduct_active(String sku, String tag) throws IOException {

        System.out.println("Creating a new product with SKU <" + sku + ">...");
        String productName_local = "Test Product " + generateRandomID();
        tag = tag.toUpperCase();

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
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">");
            System.out.println("Product name: <" + productTitle + ">");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create product for TPG Storefront product props test at PDP")
    public static void createProduct_tpgProps(int skuId, String skuCode, String skuTitle) throws IOException {
        System.out.println("Creating a new product with TPG specific props with SKU <" + skuCode + ">...");
        randomId = generateRandomID();

        JSONObject jsonObj = parse("bin/payloads/tpgProduct_propsTest.json");
        jsonObj.getJSONObject("attributes")
                .getJSONObject("title")
                .putOpt("v", "Test Product " + randomId);
        jsonObj.putOpt("slug", "test-product-" + randomId);
        jsonObj.getJSONArray("skus")
                .getJSONObject(0)
                .putOpt("id", skuId);
        jsonObj.getJSONArray("skus")
                .getJSONObject(0)
                .getJSONObject("attributes")
                .getJSONObject("code")
                .putOpt("v", skuCode);
        jsonObj.getJSONArray("skus")
                .getJSONObject(0)
                .getJSONObject("attributes")
                .getJSONObject("title")
                .putOpt("v", skuTitle);
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

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">");
            System.out.println("Product name: <" + productTitle + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create product with styled description <{0}>, content: <{1}>")
    private static void createActiveProduct_styledDescription(String skuCode, String tag, String element, String content) throws IOException {
        System.out.println("Creating product with styled description <" + element + ">, content: <" + content + ">");

        JSONObject jsonObj = parse("bin/payloads/createProduct_styledDescription.json");

        jsonObj.getJSONObject("attributes")
                .getJSONObject("title")
                .putOpt("v", "Test Product " + randomId);
        jsonObj.putOpt("slug", "test-product-" + randomId);

        jsonObj.getJSONArray("skus")
                .getJSONObject(0)
                .putOpt("id", skuId);
        jsonObj.getJSONArray("skus")
                .getJSONObject(0)
                .getJSONObject("attributes")
                .getJSONObject("code")
                .putOpt("v", skuCode);
        jsonObj.getJSONArray("skus")
                .getJSONObject(0)
                .getJSONObject("attributes")
                .getJSONObject("title")
                .putOpt("v", skuTitle);

        JSONArray tags = jsonObj.getJSONObject("attributes").getJSONObject("tags").getJSONArray("v");
        tags.put(tags.length(), tag);

        if (element.equals("ul") || element.equals("ol")) {
            jsonObj.getJSONObject("attributes")
                    .getJSONObject("description")
                    .putOpt("v", "<"+element+"><li>" + content + "</li></"+element+">");
        } else {
            jsonObj.getJSONObject("attributes")
                    .getJSONObject("description")
                    .putOpt("v", "<"+element+">" + content + "</"+element+">");
        }

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

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">");
            System.out.println("Product name: <" + productTitle + ">");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
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

    public static void getProductSlug(String productId) throws IOException {
        System.out.println("Getting slug of product <" + productId + ">...");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default/" + productId)
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
            JSONObject jsonResponse = new JSONObject(responseBody);
            productSlug = jsonResponse.getString("slug");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Set product<ID:{0}> slug to <{1}>")
    public static void editProductSlug(String productId, String newSlug) throws IOException {
        System.out.println("Setting product<ID:" + productId + "> slug to <" + newSlug + ">...");

        JSONObject jsonObj = viewProduct(productId);
        jsonObj.putOpt("slug", newSlug);
        String payload = jsonObj.toString();

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default/" + productId)
                .patch(body)
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            productSlug = jsonResponse.getString("slug");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("---- ---- ---- ----");
            checkProductPresenceInCategoryView("string", "slug", productSlug);
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Get JSON schema of product <{0}>")
    public static JSONObject viewProduct(String productId) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default/" + productId)
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
            return new JSONObject(responseBody);
        } else {
            failTest(responseBody, responseCode, responseMsg);
            return new JSONObject("{}");
        }
    }

    @Step("[API] Add tag <{1}> to product <{0}>")
    public static void addTag_product(String productId, String tag) throws IOException {
        tag = tag.toUpperCase();
        JSONObject jsonObj = viewProduct(productId);
        JSONArray tags = jsonObj.getJSONObject("attributes").getJSONObject("tags").getJSONArray("v");
        tags.put(tags.length(), tag);
        String payload = jsonObj.toString();

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/products/default/" + productId)
                .patch(body)
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
            JSONObject responseJSON = new JSONObject(responseBody);
            String newTags = responseJSON.getJSONObject("attributes").getJSONObject("tags").getJSONArray("v").toString();
            System.out.println("New Tags JSONArray:\n" + newTags);
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
        RequestBody body = RequestBody.create(mediaType, "{\"title\":\"Search " + searchRandomId + "\",\"query\":[{\"display\":\"Order : Total : > : $0\",\"term\":\"total\",\"operator\":\"gt\",\"value\":{\"type\":\"currency\",\"value\":\"000\"}}],\"scope\":\"ordersScope\",\"rawQuery\":{\"query\":{\"bool\":{\"filter\":[{\"range\":{\"total\":{\"gt\":\"000\"}}}]}}}}");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            searchCode = responseJSON.getString("code");
            searchId = responseJSON.getInt("id");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            searchId = responseJSON.getInt("id");
            searchCode = responseJSON.getString("code");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            searchCode = responseJSON.getString("code");
            searchId = responseJSON.getInt("id");
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
        RequestBody body = RequestBody.create(mediaType, "{\"title\":\"Search " + searchRandomId + "\",\"query\":[{\"display\":\"Order : State : Remorse Hold\",\"term\":\"state\",\"operator\":\"eq\",\"value\":{\"type\":\"enum\",\"value\":\"remorseHold\"}},{\"display\":\"Order : Total : > : $1\",\"term\":\"total\",\"operator\":\"gt\",\"value\":{\"type\":\"currency\",\"value\":\"100\"}}],\"scope\":\"ordersScope\",\"rawQuery\":{\"query\":{\"bool\":{\"filter\":[{\"term\":{\"state\":\"remorseHold\"}},{\"range\":{\"total\":{\"gt\":\"100\"}}}]}}}}");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            searchCode = responseJSON.getString("code");
            searchId = responseJSON.getInt("id");
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

            JSONObject responseJSON = new JSONObject(responseBody);
            JSONArray jsonArray = responseJSON.getJSONArray("result");
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
            JSONObject responseJSON = new JSONObject(responseBody);
            JSONObject obj = responseJSON.getJSONArray("summary").getJSONObject(0);
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

        checkInventoryAvailability(skuCode);
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

        skuCodes.clear();
        products.clear();
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
                createProduct_active(skuCode, "test");
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                break;

            case "cart<1 SKU[active, qty: 1]>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                break;

            case "cart with 3 items":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                updLineItems_multiple(cartId, skuCodes.get(0), 4, skuCodes.get(1), 5, skuCodes.get(2), 3);
                break;

            case "cart with 1 item, qty: 3":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 3);
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
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
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
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                break;

            case "filled out cart":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 10);
                updLineItems(cartId, skuCode, 3);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
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
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 3);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "cart<1 SKU>; coupon<any, bulk generated codes>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 3);
                createPromotion_coupon();
                createCoupon(promotionId);
                bulkGenerateCodes(couponId, "BULK CPNS ", 5, 3);
                break;

            case "cart<1 SKU, coupon applied>; coupon<any, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 3);
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
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                increaseOnHandQty(skuCode, "Sellable", 1);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "cart<1 SKU>; coupon<items -- no qualifier/percent off, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                createSharedSearch_singleProduct(productTitle);
                createPromotion_coupon_itemsNoQual(searchId);
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            //------------------------------------- CART PAYMENT METHOD -------------------------------------//

            case "cart with 1 item and chosen shipping address":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 1 item && customer with GC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item && customer with SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method, and credit card payment":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method and issued GC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item, shipping method, credit card payment and issued SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "onHold");
                break;

            case "cart with 1 item && SC canceled":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "canceled");
                break;

            //----------------------------------- CART VALIDATION -----------------------------------//

            case "cart<filled out, payment method: SC>":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 20);
                createCustomer();
                issueStoreCredit(customerId, 20000);
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                setPayment_storeCredit(cartId, 20000);
                listCustomerAddresses(customerId);
                break;

            case "cart<filled out, payment method: GC>":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 20);
                createCustomer();
                issueGiftCard(20000, 1);
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                setPayment_giftCard(cartId, gcCode, 20000);
                break;

            case "cart<filled out, payment method: CC>":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 20);
                createCustomer();
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 20);
                createCustomer();
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                skuCodes.add(skuCode);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                skuCodes.add(skuCode);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                skuCodes.add(skuCode);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 2);
                updLineItems(cartId, skuCode, 2);
                skuCodes.add(skuCode);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueStoreCredit(customerId, 50000);
                getCartTotals(cartId);
                setPayment_storeCredit(cartId, total);
                checkoutCart(cartId);
                break;

            case "order in Remorse Hold, payed with SC (GC Transfer)":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit_gcTransfer(customerId, gcCode);
                getCartTotals(cartId);
                setPayment_storeCredit(cartId, total);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "sunglasses");
                break;

            case "product in inactive state":
                createSKU_active();
                createProduct_inactive(skuCode, "sunglasses");
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
                createProduct_active(skuCode, "sunglasses");
                break;

            //----------------
            case "active product, has tag, active SKU":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                break;

            case "active product, no tag, active SKU":
                createSKU_active();
                createProduct_active_noTag(skuCode);
                break;
            case "inactive product, has tag, active SKU":
                createSKU_active();
                createProduct_inactive(skuCode, "sunglasses");
                break;

            case "inactive product, no tag, active SKU":
                createSKU_active();
                createProduct_inactive_noTag(skuCode);
                break;

            case "active product, has tag, inactive SKU":
                createSKU_inactive();
                createProduct_active(skuCode, "sunglasses");
                break;

            case "active product, no tag, inactive SKU":
                createSKU_inactive();
                createProduct_active_noTag(skuCode);
                break;

            case "inactive product, has tag, inactive SKU":
                createSKU_inactive();
                createProduct_inactive(skuCode, "sunglasses");
                break;

            case "inactive product, no tag, inactive SKU":
                createSKU_inactive();
                createProduct_inactive_noTag(skuCode);
                break;

            //----------------------------------- INVENTORY -----------------------------------//

            case "active SKU for inventory":
                createSKU_active();
                checkInventoryAvailability(skuCode);
                break;

            case "cart with backorder SKU":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Backorder", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Backorder", 1);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 2);
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
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Backorder", 1);
                updLineItems(cartId, skuCode, 1);
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
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

            case "a customer ready to checkout":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;


            //------------------------------------- SF: CART ------------------------------------//

            case "registered customer, active product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, "test");
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
                break;

            case "registered customer, active product on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                break;

            case "registered customer, 2 active products on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                products.add(productTitle);
                break;

            case "a customer signed up on storefront, product<active>, coupon<any, single code>":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "product<active>, coupon<any, single code>":
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "a customer signed up on storefront with product and coupon<any, single code> in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                applyCouponCode(cartId, singleCouponCode);
                break;

            case "an active product visible on storefront":
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                checkProductPresenceInCategoryView("int", "productId", productId);
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
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                break;

            case "a storefront signed up customer with a shipping address and a product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

            case "a storefront signed up customer with 2 shipping addresses, has default address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", true);
                createAddress(customerId,
                        "Paul Puga",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                break;

            //---------------------------------- SF: CHECKOUT --------------------------------//

            case "a storefront signed up customer, a cart with 1 product, 2 shipping addresses, has default address":
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
                        "Default Address",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", true);
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                break;

            case "a storefront signed up customer, a cart with 1 product, 2 shipping addresses, NO default address":
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
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                break;

            case "a storefront signed up customer, a cart with 1 product, 2 shipping addresses, HAS default address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "Default Address",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                break;

            case "a storefront signed up customer with active product in cart and applied shipping address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

            case "a customer ready to checkout, single code coupon code":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "a storefront customer ready for checkout, has 2 credit cards":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                createCreditCard(customerId, customerName, "4242424242424242", 3, 2020, 123, "Visa");
                break;

            case "a storefront signed up customer, with no qualifier coupon code applied":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                applyCouponCode(cartId, singleCouponCode);
                break;

            case "a storefront signed up customer ready for checkout, has 2 credit cards, has default card":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                createCreditCard(customerId, customerName, "4242424242424242", 3, 2020, 123, "Visa");
                setCardAsDefault(customerId, creditCardId);
                break;

            case "a storefront signed up customer, has shipping address and product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                break;

            case "a storefront signed up customer, with shipping address submitted and product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

            case "happy path":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                break;

            case "a storefront signed up blacklisted customer ready for checkout":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                blacklistCustomer(customerId);
                break;

            case "a customer ready for checkout, gift card is applied to cart as a payment method":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueGiftCard(1000, 1);
                setPayment_giftCard(cartId, gcCode, 1000);
                break;

            case "a customer ready to checkout, a gift card issued":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueGiftCard(1000, 1);
                break;

            case "a customer ready to checkout, 2 active products, 1 in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "a storefront registered customer, 2 active products, 1 in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                updLineItems(cartId, skuCode, 1);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                break;

            case "an active product, a gift card":
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                issueGiftCard(1000, 1);
                break;

            case "a storefront registered customer, 2 active products, 1 in cart, coupon<no qualifier, 10% off, single code>":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                updLineItems(cartId, skuCode, 1);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "a storefront registered customer, an active product":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                break;

            //------------------------------------- SF: PDP ------------------------------------//

            case "active product with tags <ENTRÉES> and <POULTRY>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "POULTRY");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "active product with tags <ENTRÉES> and <SEAFOOD>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "SEAFOOD");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "active product with tags <ENTRÉES> and <MEAT>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "MEAT");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "active product with tags <ENTRÉES> and <VEGETARIAN>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "VEGETARIAN");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "active product with tags <ENTRÉES> and <>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "products with tags with entrees subcategories names":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "POULTRY");
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "SEAFOOD");
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "MEAT");
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "VEGETARIAN");
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                products.add(productTitle);
                createSKU_active();
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with tpg-specific custom properties":
                createSKU_active();
                createProduct_tpgProps(skuId, skuCode, skuTitle);
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <p> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "p", "Paragraph");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h1> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h1", "Heading One");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h2> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h2", "Heading Two");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h3> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h3", "Heading Three");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h4> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h4", "Heading Four");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h5> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h5", "Heading Five");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h6> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h6", "Heading Six");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <strong> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "strong", "Bold Text");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <em> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "em", "Italic Text");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <ins> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "ins", "Underlined Text");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <ul> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "ul", "UL Bullet Point");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <ol> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "ol", "OL Point");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;
        }
    }

}


// 4177 - washington
// 4161 - ny
// 4164 - oregon