package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

import static org.json.JSONObject.NULL;

public class Skus extends Helpers {

    @Step("[API] View SKU <{0}>")
    public static JSONObject viewSKU(String skuCode) throws IOException {
        Response response = request.get(apiUrl + "/v1/skus/default/" + skuCode);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            return new JSONObject(responseBody);
        } else {
            failTest(response.body().string(), response.code(), response.message());
            return new JSONObject("{}");
        }
    }

    @Step("[API] Create SKU in <State:'Active'>")
    public static void createSKU_active() throws IOException {
        System.out.println("Creating a new SKU, options: ACTIVE state...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/skus/createSKU_active.json");
        payload = setSkuCode_SKUs(payload, "SKU-" + randomId);
        payload = setSkuTitle_SKUs(payload, "SKU-" + randomId + " Title");

        Response response = request.post(apiUrl + "/v1/skus/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create SKU in <State: 'Inactive'>")
    public static void createSKU_inactive() throws IOException {
        System.out.println("Creating a new SKU, options: INACTIVE state...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/skus/createSKU_inactive.json");
        payload = setSkuCode_SKUs(payload, "SKU-" + randomId);
        payload = setSkuTitle_SKUs(payload, "SKU-" + randomId + " Title");

        Response response = request.post(apiUrl + "/v1/skus/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create SKU with empty 'Title'")
    public static void createSKU_noTitle() throws IOException {
        System.out.println("Creating a new SKU, options: no title...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/skus/createSKU_noTitle.json");
        payload = setSkuCode_SKUs(payload, "SKU-" + randomId);

        Response response = request.post(apiUrl + "/v1/skus/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create SKU with empty 'Description'")
    public static void createSKU_noDescription() throws IOException {
        System.out.println("Creating a new SKU, options: no description...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/skus/createSKU_noDescription.json");
        payload = setSkuCode_SKUs(payload, "SKU-" + randomId);
        payload = setSkuTitle_SKUs(payload, "SKU-" + randomId + " Title");

        Response response = request.post(apiUrl + "/v1/skus/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create SKU without specifying the prices")
    public static void createSKU_noPrices() throws IOException {
        System.out.println("Creating a new SKU, options: all prices equals <0>...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/skus/createSKU_noPrices.json");
        payload = setSkuCode_SKUs(payload, "SKU-" + randomId);
        payload = setSkuTitle_SKUs(payload, "SKU-" + randomId + " Title");

        Response response = request.post(apiUrl + "/v1/skus/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId = responseJSON.getInt("id");
            skuCode = responseJSON.getJSONObject("attributes").getJSONObject("code").getString("v");
            skuTitle = responseJSON.getJSONObject("attributes").getJSONObject("title").getString("v");
            System.out.println("SKU code: <" + skuCode + ">.");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Archive SKU <{0}>")
    public static void archiveSKU(String skuCode) throws IOException {
        System.out.println("Archiving SKU <" + skuCode + ">...");

        Response response = request.delete(apiUrl + "/v1/skus/default/" + skuCode);

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Archive SKU <{0}> -- expect 400")
    public static void archiveSKU_expectFail(String skuCode) throws IOException {
        System.out.println("Archiving SKU <" + skuCode + ">...");

        Response response = request.delete(apiUrl + "/v1/skus/default/" + skuCode);

        if (response.code() == 400) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("SKU archiving has failed as expected");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Set SKU <ID:{0}> state to <{1}>")
    public static void setSkuState(String skuCode, String newState) throws IOException {
        System.out.println("Setting product <ID:" + skuCode + "> state to <" + newState + ">...");

        JSONObject payload = viewSKU(skuCode);
        if (newState.equals("active")) {
            payload.getJSONObject("attributes").getJSONObject("activeFrom").putOpt("v", "2016-09-01T18:06:29.890Z");
        } else {
            payload.getJSONObject("attributes").getJSONObject("activeFrom").putOpt("v", NULL);
            payload.getJSONObject("attributes").getJSONObject("activeTo").putOpt("v", NULL);
        }

        Response response = request.patch(apiUrl + "/v1/skus/default/" + skuCode, payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

}