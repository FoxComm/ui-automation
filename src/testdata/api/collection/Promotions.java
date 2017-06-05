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
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/promotions/createPromotion_coupon.json");
        payload = setName_promo(payload, "Test Promo " + randomId);

        Response response = request.post(apiUrl + "/v1/promotions/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            promotionId = String.valueOf(responseJSON.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create promotion -- <Apply type: 'Coupon'>")
    public static void createPromotion_coupon_itemsNoQual(int searchId) throws IOException {
        System.out.println("Creating a new promotion...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/promotions/createPromotion_coupon_itemsNoQual.json");
        payload = setName_promo(payload, "Test Promo " + randomId);
        payload = setSfName_promo(payload, "SF Test Promo" + randomId);
        payload.getJSONArray("discounts")
                .getJSONObject(0)
                .getJSONObject("attributes")
                .getJSONObject("qualifier")
                .getJSONObject("v")
                .getJSONObject("itemsAny")
                .getJSONArray("search")
                .getJSONObject(0)
                .putOpt("productSearchId", searchId);

        Response response = request.post(apiUrl + "/v1/promotions/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            promotionId = String.valueOf(responseJSON.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create promotion -- <Apply type: 'Auto'>, <State: 'Inactive'>")
    public static void createPromotion_auto_inactive() throws IOException {
        System.out.println("Creating a new promotion...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/promotions/createPromotion_auto_inactive.json");
        payload = setName_promo(payload, "Test Promo " + randomId);
        payload = setSfName_promo(payload, "SF Test Promo" + randomId);

        Response response = request.post(apiUrl + "/v1/promotions/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            promotionId = String.valueOf(responseJSON.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create promotion -- <Apply type: 'Auto'>, <State: 'Active'>")
    public static void createPromotion_auto_active() throws IOException {
        System.out.println("Creating a new promotion...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/promotions/createPromotion_auto_active.json");
        payload = setName_promo(payload, "Test Promo " + randomId);
        payload = setSfName_promo(payload, "SF Test Promo" + randomId);

        Response response = request.post(apiUrl + "/v1/promotions/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            promotionId = String.valueOf(responseJSON.getInt("id"));
            System.out.println("Promotion ID: " + promotionId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
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
