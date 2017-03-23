package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Products extends Helpers {

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
    public static void createProduct_active(String sku, String tag) throws IOException {

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
    public static void createActiveProduct_styledDescription(String skuCode, String tag, String element, String content) throws IOException {
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
    public static void createProduct_active_noTag(String sku) throws IOException {

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
    public static void createProduct_inactive(String sku, String tag) throws IOException {

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
    public static void createProduct_inactive_noTag(String sku) throws IOException {

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

}
