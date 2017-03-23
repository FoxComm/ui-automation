package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Promotions extends Helpers {

    @Step("[API] Create promotion -- <Apply type: 'Coupon'>")
    public static void createPromotion_coupon() throws IOException {
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
    public static void createPromotion_coupon_itemsNoQual(int searchId) throws IOException {
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
    public static void createPromotion_auto_inactive() throws IOException {

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
    public static void createPromotion_auto_active() throws IOException {

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

    @Step("[API] Set promo <ID:{0}> state to <{1}>")
    public static void setPromoState(String promotionId, String newState) {
        System.out.println("Setting promotion<ID:" + promotionId + "> state to <" + newState + ">...");

        //TODO: finish this once discounts re-implementaion will land
    }

    @Step("[API] Archive promo <ID:{0}>")
    public static void archivePromo(String promotionId) {
        System.out.println("Archiving promo<ID:" + promotionId + ">...");

        //TODO: finish this once discounts re-implementaion will land
    }

}
