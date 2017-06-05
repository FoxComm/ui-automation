package testdata.api.collection;

import com.squareup.okhttp.Response;
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

        Response response = request.post(apiUrl + "/v1/orders", "{\"customerId\": " + customerId + "}");
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            cartId = responseJSON.getString("referenceNumber");
            System.out.println("Cart ID: <" + cartId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Get email used for guest checkout, order:<{0}>")
    public static void getGuestOrderEmail(String orderId) throws IOException {
        System.out.println("Getting email used for guest checkout, order:<" + orderId + ">...");

        Response response = request.get(apiUrl + "/v1/orders/" + orderId);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            guestOrderEmail = responseJSON.getJSONObject("result").getJSONObject("customer").getString("email");
            System.out.println("Cart ID: <" + cartId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Get all totals of cart <{0}>")
    public static void getCartTotals(String cartId) throws IOException {
        System.out.println("Get all totals of cart <" + cartId + ">...");

        Response response = request.get(apiUrl + "/v1/carts/" + cartId);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            JSONObject totals = responseJSON.getJSONObject("result").getJSONObject("totals");
            subTotal = totals.getInt("subTotal");
            taxes = totals.getInt("taxes");
            shipping = totals.getInt("shipping");
            adjustments = totals.getInt("adjustments");
            customersExpenses = totals.getInt("customersExpenses");
            total = totals.getInt("total");
            System.out.println("Grand Total: <" + total + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Checkout cart <{0}>")
    public static void checkoutCart(String cartId) throws IOException {
        System.out.println("Checking out order <" + cartId + ">...");

        Response response = request.post(apiUrl + "/v1/orders/"+cartId+"/checkout", "");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            orderId = cartId;
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }


    //-------------- LINE ITEMS
    @Step("[API] Add line item: <{1}>, <QTY:{2}> to cart: <{0}>")
    public static void updLineItems(String cartId, String skuCode, int quantity) throws IOException {
        System.out.println("Updating SKUs: setting <" + skuCode + "> quantity to <" + quantity + ">...");

        JSONArray payload = parseArr("bin/payloads/cart/updLineItems.json");
        payload.getJSONObject(0).putOpt("sku", skuCode);
        payload.getJSONObject(0).putOpt("quantity", quantity);

        Response response = request.post(apiUrl + "/v1/carts/"+cartId+"/line-items", payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Add line item: <{1}>, <QTY:{2}> to cart: <{0}>")
    public static void updLineItems_multiple(String cartId,
                                             String sku1, int qty1,
                                             String sku2, int qty2,
                                             String sku3, int qty3
    ) throws IOException {
        System.out.println("Updating line items in cart <"+cartId+">: <"+sku1+":"+qty1+">; <"+sku2+":"+qty2+">; <"+sku3+":"+qty3+">");

        JSONArray payload = parseArr("bin/payloads/cart/updLineItems_multiple.json");
        payload.getJSONObject(0).putOpt("sku", sku1);
        payload.getJSONObject(0).putOpt("quantity", qty1);

        payload.getJSONObject(1).putOpt("sku", sku2);
        payload.getJSONObject(1).putOpt("quantity", qty2);

        payload.getJSONObject(2).putOpt("sku", sku3);
        payload.getJSONObject(2).putOpt("quantity", qty3);

        Response response = request.post(apiUrl + "/v1/orders/"+cartId+"/line-items", payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }


    //-------------- SHIPPING METHOD
    @Step("[API] Add new shipping address and set it as a chosen for cart <{0}>")
    public static void setShipAddress(String cartId,
                                      String name,
                                      int regionId,
                                      int countryId,
                                      String region_name,
                                      String address1,
                                      String address2,
                                      String city,
                                      String zip,
                                      String phoneNumber,
                                      boolean isDefault
    ) throws IOException {
        System.out.println("Adding new shipping address and setting it as a chosen for <" + cartId + "> cart...");

        JSONObject payload = parseObj("bin/payloads/cart/setShipAddress.json");
        payload.putOpt("name", name);
        payload.putOpt("regionId", regionId);
        payload.putOpt("address1", address1);
        payload.putOpt("address2", address2);
        payload.putOpt("city", city);
        payload.putOpt("zip", zip);
        payload.putOpt("isDefault", isDefault);
        payload.putOpt("phoneNumber", phoneNumber);

        Response response = request.post(apiUrl + "/v1/orders/"+cartId+"/shipping-address", payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] List possible shipping methods for cart <{0}>")
    public static void listShipMethods(String cartId) throws IOException {
        System.out.println("Getting possible shipping methods for order <" + cartId + ">...");

        Response response = request.get(apiUrl + "/v1/shipping-methods/"+cartId);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONArray responseJSON = new JSONArray(responseBody);
            shipMethodId = responseJSON.getJSONObject(0).getInt("id");
            System.out.println("shipMethodId: <" + shipMethodId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Set shipping method <{1}> for cart <{0}>")
    public static void setShipMethod(String cartId, int shipMethodId) throws IOException {
        System.out.println("Setting shipping method for order <" + cartId + ">...");

        Response response = request.patch(apiUrl + "/v1/orders/"+cartId+"/shipping-method",
                "{\"shippingMethodId\": " + shipMethodId + "}");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }


    //-------------- COUPONS
    @Step("[API] Apply coupon <ID:{1}> to cart <{0}>")
    public static void applyCouponCode(String cartId, String couponCode) throws IOException {
        System.out.println("Applying coupon code <" + couponCode + "> to order <" + cartId + ">...");

        Response response = request.post(apiUrl + "/v1/orders/"+cartId+"/coupon/" + couponCode, "{}");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }


    //-------------- PAYMENT METHOD
    @Step("[API] Set credit card <{1}> as a payment method for cart <{0}>")
    public static void setPayment_creditCard(String cartId, int creditCardId) throws IOException {
        System.out.println("Setting credit card with id <" + creditCardId + "> as a payment method for order <" + cartId + ">...");

        Response response = request.post(apiUrl + "/v1/orders/"+cartId+"/payment-methods/credit-cards",
                "{\"creditCardId\":" + creditCardId + "}");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Set store credits <amount: {1}> as a payment method for cart <{0}>")
    public static void setPayment_storeCredit(String cartId, int amount) throws IOException {
        System.out.println("Setting store credit in amount of <" + amount + "> as a payment for order <" + cartId + ">...");

        Response response = request.post(apiUrl + "/v1/orders/"+cartId+"/payment-methods/store-credit",
                "{\"amount\":" + amount + "}");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Set GC <gcNumber: {1}>, <amount: {2}> for cart <{0}>")
    public static void setPayment_giftCard(String cartId, String gcCode, int amount) throws IOException {
        System.out.println("Setting gift card <"+ gcCode + "> in amount of <" + amount + "> as a payment for order <" + cartId + ">...");

        JSONObject payload = parseObj("bin/payloads/cart/setPayment_giftCard.json");
        payload.putOpt("code", gcCode);
        payload.putOpt("amount", amount);

        Response response = request.post(apiUrl + "/v1/orders/"+cartId+"/payment-methods/gift-cards", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

}