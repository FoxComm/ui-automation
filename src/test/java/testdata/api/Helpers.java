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

    //------------------------------------- JSON HELPERS -------------------------------------

    protected static JSONObject parseObj(String rout) throws IOException {
        String jsonData = "";
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(rout));
        while ((line = bufferedReader.readLine()) != null) {
            jsonData += line + "\n";
        }
        bufferedReader.close();
        return new JSONObject(Objects.requireNonNull(jsonData));
    }

    protected static JSONArray parseArr(String rout) throws IOException {
        String jsonData = "";
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(rout));
        while ((line = bufferedReader.readLine()) != null) {
            jsonData += line + "\n";
        }
        bufferedReader.close();
        return new JSONArray(Objects.requireNonNull(jsonData));
    }

    protected static boolean assertProductAppearInEs(String jsonAttrType, String attrName, String attrVal, String responseBody) {
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

    protected static JSONObject setProductTitle(JSONObject payload, String title) {
        payload.getJSONObject("attributes")
                .getJSONObject("title")
                .putOpt("v", title);
        return payload;
    }

    /**
     * Only for single SKU product
     */
    protected static JSONObject setSkuId_product(JSONObject payload, int skuId) {
        payload.getJSONArray("skus")
                .getJSONObject(0)
                .putOpt("id", skuId);
        return payload;
    }

    /**
     * Only for single SKU product
     */
    protected static JSONObject setSkuCode_product(JSONObject payload, String skuCode) {
        payload.getJSONArray("skus")
                .getJSONObject(0)
                .getJSONObject("attributes")
                .getJSONObject("code")
                .putOpt("v", skuCode);
        return payload;
    }

    /**
     * Only for single SKU product
     */
    protected static JSONObject setSkuTitle_product(JSONObject payload, String skuTitle) {
        payload.getJSONArray("skus")
                .getJSONObject(0)
                .getJSONObject("attributes")
                .getJSONObject("title")
                .putOpt("v", skuTitle);
        return payload;
    }

    /**
     * Only for single tag product
     */
    protected static JSONObject setTag_product(JSONObject payload, String tag) {
        JSONArray tagsArr = payload
                .getJSONObject("attributes")
                .getJSONObject("tags")
                .getJSONArray("v");
        tagsArr.put(tagsArr.length(), tag);
        return payload;
    }

    protected static JSONObject setSkuCode_SKUs(JSONObject payload, String skuCode) {
        payload.getJSONObject("attributes")
                .getJSONObject("code")
                .putOpt("v", skuCode);
        return payload;
    }

    protected static JSONObject setName_promo(JSONObject payload, String name) {
        payload.getJSONObject("attributes")
                .getJSONObject("name")
                .putOpt("v", name);
        return payload;
    }

    protected static JSONObject setSfName_promo(JSONObject payload, String sfName) {
        payload.getJSONObject("attributes")
                .getJSONObject("storefrontName")
                .putOpt("v", sfName);
        return payload;
    }

    protected static JSONObject setSkuTitle_SKUs(JSONObject payload, String title) {
        payload.getJSONObject("attributes")
                .getJSONObject("title")
                .putOpt("v", title);
        return payload;
    }

    @Step("[API] Wait for <{1}> orders to apeear in ES")
    protected static void waitForOrdersToAppearInES(int customerId, int ordersExpectedAmount) throws IOException {
        System.out.println("Wait for <" + ordersExpectedAmount + "> orders to appear in ES");

        int responseCode = 0;
        String responseBody = "";
        String responseMsg = "";
        long time = System.currentTimeMillis();
        long end = time + 15000;
        int ordersInEs = 0;
        int totalTries = 0;

        JSONObject payload = parseObj("bin/payloads/helpers/esSearchCustomer.json");
        payload.getJSONObject("query")
                .getJSONObject("bool")
                .getJSONArray("filter")
                .getJSONObject(0)
                .getJSONObject("nested")
                .getJSONObject("query")
                .getJSONObject("term")
                .putOpt("customer.id", customerId);

        while((System.currentTimeMillis() < end) && (ordersInEs != ordersExpectedAmount)) {
            Response response = request.post(apiUrl + "/search/admin/orders_search_view/_search?size=50", payload.toString());
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

    /**
     * options for attrName:
     *  (int) id
     *  (string) title
     *  (string) slug
     */
    @Step("[API] Check if product<({0}) {1}: {2}> is present in category view on storefront")
    protected static void waitForProductAppearInEs(String attrType, String attrName, String attrVal) throws IOException {
        System.out.println("Checking if product<"+attrName+": "+attrVal+"> is present in category view on storefront...");

        int responseCode = 0;
        String responseBody = "";
        String responseMsg = "";
        long time = System.currentTimeMillis();
        long end = time + 15000;
        int totalTries = 0;
        boolean productIsInEs = false;

        JSONObject jsonObj = parseObj("bin/payloads/helpers/esCatalogView.json");
        String payload = jsonObj.toString();

        while((System.currentTimeMillis() < end) && (productIsInEs != true)) {
            Response response = request.post(apiUrl + "/search/public/products_catalog_view/_search?size=1000", payload.toString());
            responseBody = response.body().string();
            responseCode = response.code();
            responseMsg = response.message();

            totalTries ++;
            productIsInEs = assertProductAppearInEs(attrType, attrName, attrVal, responseBody);
        }
        System.out.println("total tries: <" + totalTries + ">");
        System.out.println("Time spent: <" + (System.currentTimeMillis() - time) + " MS>");

        if (responseCode == 200 && productIsInEs) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("Product is present in ES: <" + productIsInEs + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            System.out.println("Timed out on finding order in ES");
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    protected static class request {

        public static Response get(String url) throws IOException {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("content-type", "application/json")
                    .addHeader("accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("JWT", jwt)
                    .build();

            return client.newCall(request).execute();
        }

        public static Response post_noJWT(String url, String payload) throws IOException {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, payload);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            return client.newCall(request).execute();
        }

        public static Response post(String url, String payload) throws IOException {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, payload);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("JWT", jwt)
                    .build();

            return client.newCall(request).execute();
        }

        public static Response patch(String url, String payload) throws IOException {
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");

            RequestBody body = RequestBody.create(mediaType, payload);
            Request request = new Request.Builder()
                    .url(url)
                    .patch(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("JWT", jwt)
                    .build();

            return client.newCall(request).execute();
        }

        public static Response delete(String url) throws IOException {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .delete(null)
                    .addHeader("content-type", "application/json")
                    .addHeader("accept", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("JWT", jwt)
                    .build();

            return client.newCall(request).execute();
        }

    }

}