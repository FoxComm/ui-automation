package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

import static org.json.JSONObject.NULL;

public class Products extends Helpers {

    @Step("[API] Create product; <SKU: auto-created with product>, <state:active>")
    public static void createProduct_newSKU_active() throws IOException {
        System.out.println("Creating a new product... No SKU code is provided, so a new one will be created.");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/products/createProduct_newSKU_active.json");
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setSkuCode_product(payload, "SKU-" + randomId);
        payload = setSkuTitle_product(payload, "SKU-" + randomId + " Title");

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes")
                                        .getJSONObject("title")
                                        .getString("v");
            productSlug = responseJSON.getString("slug");
            skuId = responseJSON.getJSONArray("skus")
                                .getJSONObject(0)
                                .getInt("id");
            skuCode = responseJSON.getJSONArray("skus")
                                    .getJSONObject(0)
                                    .getJSONObject("attributes")
                                    .getJSONObject("code")
                                    .getString("v");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("SKU ID: <" + skuId + ">");
            System.out.println("SKU Code: <" + skuCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create product; <SKU: auto-created with product>, <state:active>, <tag:{0}>")
    public static void createProduct_newSKU_active_hasTag(String tag) throws IOException {
        System.out.println("Creating a new product with tag <" + tag + ">... Will create a new SKU since none is provided");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/products/createProduct_newSKU_active_hasTag.json");
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setSkuCode_product(payload, "SKU-" + randomId);
        payload = setSkuTitle_product(payload, "SKU-" + randomId + " Title");
        payload = setTag_product(payload, tag);

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes")
                    .getJSONObject("title")
                    .getString("v");
            productSlug = responseJSON.getString("slug");
            skuId = responseJSON.getJSONArray("skus")
                    .getJSONObject(0)
                    .getInt("id");
            skuCode = responseJSON.getJSONArray("skus")
                    .getJSONObject(0)
                    .getJSONObject("attributes")
                    .getJSONObject("code")
                    .getString("v");
            System.out.println("Product ID: <" + productId + ">");
            System.out.println("Product name: <" + productTitle + ">");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("SKU ID: <" + skuId + ">");
            System.out.println("SKU Code: <" + skuCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create product; <SKU: auto-created with product>, <state:inactive>, <tag:{0}>")
    public static void createProduct_inactive_newSKU_hasTag(String tag) throws IOException {
        System.out.println("Creating a new product with tag <" + tag + ">... Will create a new SKU since none is provided");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/products/createProduct_inactive_newSKU_hasTag.json");
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setSkuCode_product(payload, "SKU-" + randomId);
        payload = setSkuTitle_product(payload, "SKU-" + randomId + " Title");
        payload = setTag_product(payload, tag);

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes")
                    .getJSONObject("title")
                    .getString("v");
            productSlug = responseJSON.getString("slug");
            skuId = responseJSON.getJSONArray("skus")
                    .getJSONObject(0)
                    .getInt("id");
            skuCode = responseJSON.getJSONArray("skus")
                    .getJSONObject(0)
                    .getJSONObject("attributes")
                    .getJSONObject("code")
                    .getString("v");
            System.out.println("Product ID: <" + productId + ">");
            System.out.println("Product name: <" + productTitle + ">");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("SKU ID: <" + skuId + ">");
            System.out.println("SKU Code: <" + skuCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create product; SKU:<{0}>, Tag:<{1}>, State:<Active>")
    public static void createProduct_active(int skuId, String skuCode, String tag) throws IOException {
        System.out.println("Creating a new product with SKU <ID:" + skuId + ">, <code:" + skuCode + ">, tag:<" + tag + ">...");
        String randomId = generateRandomID();
        tag = tag.toUpperCase();

        JSONObject payload = parseObj("bin/payloads/products/createProduct_active.json");
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setTag_product(payload, tag);
        payload = setSkuCode_product(payload, skuCode);
        payload = setSkuId_product(payload, skuId);

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">");
            System.out.println("Product name: <" + productTitle + ">");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create product for TPG Storefront product props test at PDP")
    public static void createProduct_tpgProps(int skuId, String skuCode, String skuTitle) throws IOException {
        System.out.println("Creating a new product with TPG specific props with SKU <" + skuCode + ">...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/products/tpgProduct_propsTest.json");
        payload.putOpt("slug", "test-product-" + randomId);
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setSkuId_product(payload, skuId);
        payload = setSkuCode_product(payload, skuCode);

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">");
            System.out.println("Product name: <" + productTitle + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create product with styled description <{0}>, content: <{1}>")
    public static void createActiveProduct_styledDescription(int skuId, String skuCode, String tag, String element, String content) throws IOException {
        System.out.println("Creating product with styled description <" + element + ">, content: <" + content + ">");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/products/createProduct_styledDescription.json");
        payload.putOpt("slug", "test-product-" + randomId);
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setSkuId_product(payload, skuId);
        payload = setSkuCode_product(payload, skuCode);

        JSONArray tags = payload.getJSONObject("attributes").getJSONObject("tags").getJSONArray("v");
        tags.put(tags.length(), tag);

        if (element.equals("ul") || element.equals("ol")) {
            payload.getJSONObject("attributes")
                    .getJSONObject("description")
                    .putOpt("v", "<"+element+"><li>" + content + "</li></"+element+">");
        } else {
            payload.getJSONObject("attributes")
                    .getJSONObject("description")
                    .putOpt("v", "<"+element+">" + content + "</"+element+">");
        }

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">");
            System.out.println("Product name: <" + productTitle + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create product; <SKU:{0}> <State:Active>, no tag")
    public static void createProduct_active_noTag(int skuId, String skuCode) throws IOException {
        System.out.println("Creating a new product with SKU <" + skuCode + ">...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/products/createProduct_active_noTag.json");
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setSkuId_product(payload, skuId);
        payload = setSkuCode_product(payload, skuCode);

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create product; <SKU:{0}>, <Tag:{1}>, <State:Inactive>")
    public static void createProduct_inactive(int skuId, String skuCode, String tag) throws IOException {
        System.out.println("Creating a new product with SKU <" + skuCode + ">...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/products/createProduct_inactive.json");
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setTag_product(payload, tag);
        payload = setSkuId_product(payload, skuId);
        payload = setSkuCode_product(payload, skuCode);

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create product; <SKU:{0}>, no tag, <State:Inactive>")
    public static void createProduct_inactive_noTag(int skuId, String skuCode) throws IOException {
        System.out.println("Creating a new product with SKU <" + skuCode + ">...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/products/createProduct_inactive_noTag.json");
        payload = setProductTitle(payload, "Product " + randomId);
        payload = setSkuId_product(payload, skuId);
        payload = setSkuCode_product(payload, skuCode);

        Response response = request.post(apiUrl + "/v1/products/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }

    }

    @Step("[API] Create product; <SKU:{0}>, <Tag:sunglasses>, <Active From:{1}>, <Active To:{2}>")
    private static void createProduct_activeFromTo(String sku, String startDate, String endDate) throws IOException {
        System.out.println("Creating product with active from-to dates; SKU: <" + sku + ">...");
        String randomId = generateRandomID();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\":null,\"productId\":null,\"attributes\":{\"metaDescription\":{\"t\":\"string\",\"v\":null},\"metaTitle\":{\"t\":\"string\",\"v\":null},\"url\":{\"t\":\"string\",\"v\":null},\"description\":{\"t\":\"richText\",\"v\":\"<p>The best thing to buy in 2016!</p>\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"activeFrom\":{\"t\":\"datetime\",\"v\":\"" + startDate + "T07:00:17.729Z\"},\"activeTo\":{\"t\":\"datetime\",\"v\":\"" + endDate + "T19:00:00.810Z\"},\"tags\":{\"t\":\"tags\",\"v\":[\"sunglasses\"]}},\"skus\":[{\"feCode\":\"CIEP39Z2WPQ1ETFVQUXR\",\"attributes\":{\"code\":{\"t\":\"string\",\"v\":\"" + sku + "\"},\"title\":{\"t\":\"string\",\"v\":\"\"},\"retailPrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}},\"salePrice\":{\"t\":\"price\",\"v\":{\"currency\":\"USD\",\"value\":2718}}}}],\"context\":\"\"}");
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

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            productId = String.valueOf(responseJSON.getInt("id"));
            productTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            productSlug = responseJSON.getString("slug");
            System.out.println("Product ID: <" + productId + ">.");
            System.out.println("Product name: <" + productTitle + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }

    }

    @Step("[API] Archive product <ID:{0}>")
    public static void archiveProduct(String productId) throws IOException {
        System.out.println("Archiving product with ID <" + productId + ">...");

        Response response = request.delete(apiUrl + "/v1/products/default/" + productId);

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Archive product <ID:{0}> -- expect 400")
    public static void archiveProduct_expectFail(String productId) throws IOException {
        System.out.println("Archiving product with ID <" + productId + ">...");

        Response response = request.delete(apiUrl + "/v1/products/default/" + productId);

        if (response.code() == 400) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("Product archiving has failed as expected");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Get product slug <ID:{0}>")
    public static void getProductSlug(String productId) throws IOException {
        System.out.println("Getting slug of product <" + productId + ">...");

        Response response = request.get(apiUrl + "/v1/products/default/" + productId);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            productSlug = jsonResponse.getString("slug");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Set product<ID:{0}> slug to <{1}>")
    public static void editProductSlug(String productId, String newSlug) throws IOException {
        System.out.println("Setting product<ID:" + productId + "> slug to <" + newSlug + ">...");

        JSONObject payload = viewProduct(productId);
        payload.putOpt("slug", newSlug);

        Response response = request.patch(apiUrl + "/v1/products/default/" + productId, payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            JSONObject jsonResponse = new JSONObject(responseBody);
            productSlug = jsonResponse.getString("slug");
            System.out.println("Slug: <" + productSlug + ">");
            System.out.println("---- ---- ---- ----");
            waitForProductAppearInEs("string", "slug", productSlug);
        } else {
            failTest(responseBody, response.code(), response.message());
        }

    }

    @Step("[API] Get JSON schema of product <{0}>")
    public static JSONObject viewProduct(String productId) throws IOException {
        Response response = request.get(apiUrl + "/v1/products/default/" + productId);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            return new JSONObject(responseBody);
        } else {
            failTest(responseBody, response.code(), response.message());
            return new JSONObject("{}");
        }
    }

    @Step("[API] Add tag <{1}> to product <{0}>")
    public static void addTag_product(String productId, String tag) throws IOException {
        System.out.println("Adding tag <" + tag + "> to product <ID:" + productId + ">");
        tag = tag.toUpperCase();
        JSONObject payload = viewProduct(productId);
        payload = setTag_product(payload, tag);

        Response response = request.patch(apiUrl + "/v1/products/default/" + productId, payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            String newTags = responseJSON.getJSONObject("attributes").getJSONObject("tags").getJSONArray("v").toString();
            System.out.println("New Tags JSONArray:\n" + newTags);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Set product <ID:{0}> state to <{1}>")
    public static void setProductState(String productId, String newState) throws IOException {
        System.out.println("Setting product <ID:" + productId + "> state to <" + newState + ">...");

        JSONObject payload = viewProduct(productId);
        if (newState.equals("active")) {
            payload.getJSONObject("attributes").getJSONObject("activeFrom").putOpt("v", "2016-09-01T18:06:29.890Z");
        } else {
            payload.getJSONObject("attributes").getJSONObject("activeFrom").putOpt("v", NULL);
            payload.getJSONObject("attributes").getJSONObject("activeTo").putOpt("v", NULL);
        }

        Response response = request.patch(apiUrl + "/v1/products/default/" + productId, payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

}