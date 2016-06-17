package testdata;

import base.BaseTest;
import com.squareup.okhttp.*;
import pages.OrdersPage;

import java.io.IOException;

public class DataProvider extends BaseTest {

    private static String jwt;
    private static int customerId;
    public static String customerName;
    public static String customerEmail;
    public static String orderId;
    private static int addressId1;
    private static int addressId2;
    private static int addressId3;

    private static void loginAsAdmin() throws IOException {

        System.out.println("Authorizing as an admin...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"email\": \"admin@admin.com\"," +
                "\n    \"password\": \"password\"," +
                "\n    \"kind\": \"admin\"\n}");

        Request request = new Request.Builder()
                .url("http://10.240.0.7:9090/v1/public/login")
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

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"name\": \"Test Customer-" + randomID + "\"," +
                "\n    \"email\": \"testcustomer." + randomID + "@mail.com\"\n}");

        Request request = new Request.Builder()
                .url("http://10.240.0.7:9090/v1/customers")
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
        System.out.println("Customer ID: <" + customerId + ">");
        System.out.println("--------");


    }

    public static void viewCustomer(int customerId) throws IOException {

        System.out.println("Getting name and email of customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.240.0.7:9090/v1/customers/" + customerId)
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

    private static void createCart(int customerId) throws IOException {

        System.out.println("Creating a cart for customer " + customerId + "...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"customerId\": " + customerId + "\n}");
        Request request = new Request.Builder()
                .url("http://10.240.0.7:9090/v1/orders")
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

    private static void updateSKULineItems(String orderRefNum, String SKU, int quantity) throws IOException {

        System.out.println("Updating SKUs: setting '" + SKU + "' quantity to '" + quantity + "'...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[{\n    \"sku\": \"" + SKU + "\"," +
                "\n    \"quantity\": " + quantity + "\n}]");
        Request request = new Request.Builder()
                .url("http://10.240.0.7:9090/v1/orders/" + orderRefNum + "/line-items")
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
                .url("http://10.240.0.7:9090/v1/customers/" + customerId + "/addresses")
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        String addresses = response.body().string();
        addressId1 = Integer.valueOf(addresses.substring(37, 41));
        addressId2 = Integer.valueOf(addresses.substring(216, 220));
//        addressId3 = Integer.valueOf(addresses.substring(415, 419));

        System.out.println(response);
        System.out.println("Address 1: <" + addressId1 + ">");
        System.out.println("Address 2: <" + addressId2 + ">");
//        System.out.println("Addres 3: <" + addressId3 + ">");
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
                .url("http://10.240.0.7:9090/v1/customers/" + customerId + "/addresses")
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

    private static void deleteAddress(int customerId, int addressId) throws IOException {

        System.out.println("Deleting an address for customer " + customerId + "...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.240.0.7:9090/v1/customers/" + customerId + "/addresses/" + addressId)
                .delete(null)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();

        System.out.println(response);
        System.out.println("--------");

    }

    private static void setShippingAddress(String orderId, String name, int regionId, int countryId, String region_name, String address1, String address2, String city, String zip, String phoneNumber, boolean isDefault) throws IOException {

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
                .url("http://10.240.0.7:9090/v1/orders/" + orderId + "/shipping-address")
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



    protected void provideTestData(String testMethodName) throws IOException {

        loginAsAdmin();
        createNewCustomer();

        OrdersPage ordersPage;

        switch(testMethodName) {

            //------------------------------------- SEARCH FILTERS ------------------------------------//

            case "customerName_filter":
                viewCustomer(customerId);
                break;

            //----------------------------------------- ITEMS -----------------------------------------//

            case "empty cart":
                createCart(customerId);
                break;

            case "cart with 1 item":
                createCart(customerId);
                updateSKULineItems(orderId, "SKU-TRL", 1);
                break;

            case "cart with 3 items":
                createCart(customerId);
                updateSKULineItems(orderId, "SKU-TRL", 3);
                updateSKULineItems(orderId, "SKU-BRO", 2);
                updateSKULineItems(orderId, "SKU-ZYA", 4);
                break;

            case "cart with 1 item, qty: 3":
                createCart(customerId);
                updateSKULineItems(orderId, "SKU-TRL", 3);
                break;

            //------------------------------------- SHIPPING ADDRESS -------------------------------------//

            case "cart with empty address book":
                createCart(customerId);
                break;

            case "cart with non-empty address book":
                createCart(customerId);
                createAddress(customerId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with chosen shipping address":
                createCart(customerId);
                setShippingAddress(orderId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 2 addresses in address book":
                createCart(customerId);
                createAddress(customerId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", true);
                createAddress(customerId, "Paul Puga", 4161, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            case "cart with 2 addresses and defined default shipping address":
                createCart(customerId);
                createAddress(customerId, "John Doe", 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4161, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", true);
                break;

//            case "":
//                ordersPage = open("http://admin.stage.foxcommerce.com/orders", OrdersPage.class);
//                ordersPage.addFilterQuick("Order", "State", "Cart");
//                ordersPage.addFilterQuick("Shipping", "State", "Washington");
//                ordersPage.openOrder();
//                break;

        }

    }

}
