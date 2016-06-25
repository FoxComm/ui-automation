import base.BaseTest;
import com.squareup.okhttp.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

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

//    private static String generateRandomID() {
//
//        Random rand = new Random();
//        String randomId = "";
//
//        for (int i = 0; i < 5; i++) {
//            // generates random int between 0 and 9
//            int randomNum = rand.nextInt(9 + 1);
//
//            String strRandomNum = String.valueOf(randomNum);
//            randomId = randomId.concat(strRandomNum);
//        }
//
//        return randomId;
//
//    }

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
                .url("http://admin.stage.foxcommerce.com/api/v1/tests.customers")
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
                .url("http://admin.stage.foxcommerce.com/api/v1/tests.customers/" + customerId)
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
                .url("http://admin.stage.foxcommerce.com/api/v1/tests.customers/" + customerId + "/addresses")
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
                .url("http://admin.stage.foxcommerce.com/api/v1/tests.customers/" + customerId + "/addresses")
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
                .url("http://admin.stage.foxcommerce.com/api/v1/tests.customers/" + customerId + "/payment-methods/credit-cards")
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
                .url("http://admin.stage.foxcommerce.com/api/v1/tests.customers/" + customerId + "/payment-methods/store-credit")
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

    @Test
    public void test() {
        open("https://www.google.com.ua");

        try {
            $(By.xpath("//div[@class='randomrandom']")).waitUntil(visible, 10000);
        } catch (NoSuchElementException e) {
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {

        loginAsAdmin();
        createNewCustomer();
//        createNewCustomer();
//        createCart(customerId);
//
//        setShipAddress(orderId, customerName, 4161, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
//
//        listShipMethods(orderId);
//        setShipMethod(orderId, shipMethodId);
//
//        listCustomerAddresses(customerId);
//        createCreditCard("John Doe", "5555555555554444", "999", 4, 2020, addressId1);
//        setPayment_creditCard(orderId, creditCardId);
//
////        issueStoreCredit(customerId, 10000);
////        setPayment_storeCredit(orderId, 10000);
//
////        issueGiftCard(500, 1);
////        setPayment_giftCard(orderId, gcNumber, 10000);



    }

}


//Test Customer-12345
//testcustomer.12345@mail.com