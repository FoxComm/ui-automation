package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Skus extends Helpers {

    @Step("[API] Create SKU in <State:'Active'>")
    public static void createSKU_active() throws IOException {

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
    public static void createSKU_inactive() throws IOException {

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
    public static void createSKU_noTitle() throws IOException {

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
    public static void createSKU_noDescription() throws IOException {

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
    public static void createSKU_noPrices() throws IOException {

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

}