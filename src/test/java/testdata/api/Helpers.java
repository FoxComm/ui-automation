package testdata.api;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Helpers extends Variables {

    protected static JSONObject parse(String rout) throws IOException {
        String jsonData = "";
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(rout));
        while ((line = bufferedReader.readLine()) != null) {
            jsonData += line + "\n";
        }
        bufferedReader.close();
        return new JSONObject(Objects.requireNonNull(jsonData));
    }

    protected static boolean assertProductExists_es(String jsonAttrType, String attrName, String attrVal, String responseBody) {
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

    protected static void failTest(String responseBody, int responseCode, String responseMsg) throws IOException {
        System.out.println("Response: " + responseCode + " " + responseMsg);
        System.out.println(responseBody);
        System.out.println("--------");
        if (!(responseCode == 200) || !(responseCode == 204)) {
            throw new RuntimeException(responseBody +
                    "\nExpected:[200], Actual:[" + responseCode + "]");
        }
    }

    protected static void getStripeToken(int customerId, String cardNumber, int expMonth, int expYear, int cvv) throws IOException {

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
    protected static void checkProductPresenceInCategoryView(String attrType, String attrName, String attrVal) throws IOException {
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

}