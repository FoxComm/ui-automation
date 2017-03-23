package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Cart extends Helpers{

    //-------------- GENERAL
    @Step("[API] Create cart for customer <{0}>")
    public static void createCart(int customerId) throws IOException {
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

    private static void viewOrder(int orderId) throws IOException {

        System.out.println("Viewing order <" + orderId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/orders/BR10116")
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        JSONObject jsonObject = new JSONObject( response.body().string() );
        total = jsonObject.getInt("total");

        System.out.println(response);
        System.out.println("Order Grand Total: <" + total + ">");
        System.out.println("---- ---- ---- ----");

    }

    @Step("[API] Checkout cart <{0}>")
    public static void checkoutCart(String cartId) throws IOException {

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


    //-------------- LINE ITEMS
    @Step("[API] Add line item: <{1}>, <QTY:{2}> to cart: <{0}>")
    public static void updLineItems(String cartId, String SKU, int quantity) throws IOException {
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
    public static void updLineItems_multiple(String cartId, String sku1, int qty1, String sku2, int qty2, String sku3, int qty3) throws IOException {
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


    //-------------- SHIPPING METHOD
    @Step("[API] Add new shipping address and set it as a chosen for cart <{0}>")
    public static void setShipAddress(String cartId, String name, int regionId, int countryId, String region_name, String address1, String address2, String city, String zip, String phoneNumber, boolean isDefault) throws IOException {

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

    @Step("[API] List possible shipping methods for cart <{0}>")
    public static void listShipMethods(String cartId) throws IOException {

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
    public static void setShipMethod(String cartId, int shipMethodId) throws IOException {

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


    //-------------- COUPONS
    @Step("[API] Apply coupon <ID:'{1}'> to cart <{0}>")
    public static void applyCouponCode(String cartId, String couponCode) throws IOException {

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


    //-------------- PAYMENT METHOD
    @Step("[API] Set credit card <{1}> as a payment method for cart <{0}>")
    public static void setPayment_creditCard(String cartId, int creditCardId) throws IOException {

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

    @Step("[API] Set store credits <amount: {1}> as a payment method for cart <{0}>")
    public static void setPayment_storeCredit(String cartId, int amount) throws IOException {

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

    @Step("[API] Set GC <gcNumber: {1}>, <amount: {2}> for cart <{0}>")
    public static void setPayment_giftCard(String cartId, String gcCode, int amount) throws IOException {

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

}
