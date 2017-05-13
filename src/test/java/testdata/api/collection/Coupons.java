package testdata.api.collection;

import com.squareup.okhttp.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class Coupons extends Helpers {

    @Step("[API] Create coupon with promotion ID:<{0}>")
    public static void createCoupon(String promotionId) throws IOException {
        System.out.println("Creating a new coupon with promotion <" + promotionId + ">...");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/coupons/createCoupon.json");
        payload.getJSONObject("attributes").getJSONObject("name").putOpt("v", "test coupon " + randomId);
        payload.getJSONObject("attributes").getJSONObject("storefrontName").putOpt("v", "storefront name " + randomId);
        payload.putOpt("promotion", Integer.valueOf(promotionId));

        Response response = request.post(apiUrl + "/v1/coupons/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            couponId = String.valueOf(responseJSON.getInt("id"));
            couponName = "test coupon " + randomId;
            System.out.println("Coupon ID: " + couponId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Generate a single code for coupon ID:<{0}>")
    public static void generateSingleCode(String couponId) throws IOException {
        System.out.println("Generating a single code for coupon <" + couponId + ">...");

        Response response = request.post(apiUrl + "/v1/coupons/codes/generate/"+couponId +"/NWCPN-"+generateRandomID(), "{}");
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            singleCouponCode = responseBody.substring(1, responseBody.length() - 1);
            System.out.println("Single coupon code: " + singleCouponCode);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Bulk generate codes for coupon <ID:{0}>; [prefix:{1}, length:{2}, QTY:{3}]")
    public static void bulkGenerateCodes(String couponId, String prefix, int length, int quantity) throws IOException {
        System.out.println("Bulk generating coupon codes for coupon <" + couponId + ">...");

        JSONObject payload = parseObj("bin/payloads/coupons/bulkGenerateCodes.json");
        payload.putOpt("prefix", prefix);
        payload.putOpt("length", length);
        payload.putOpt("quantity", quantity);

        Response response = request.post(apiUrl + "/v1/coupons/codes/generate/" + couponId, payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONArray responseJSON = new JSONArray(responseBody);
            for (int i = 0; i < responseJSON.length(); i++) {
                bulkCodes.add(responseJSON.getString(i));
            }
            System.out.println("Number of codes generated: <" + bulkCodes.size() + ">");
            printStringList(bulkCodes);
            assertEquals(bulkCodes.size(), quantity);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }

    }

    @Step("[API] Set coupon <ID:{0}> state to <{1}>")
    public static void setCouponState(String couponId, String newState) {
        System.out.println("Setting coupon<ID:" + couponId + "> state to <" + newState + ">...");

        //TODO: finish this once discounts re-implementaion will land
    }

    @Step("[API] Archive coupon <ID:{0}>")
    public static void archiveCoupon(String couponId) {
        System.out.println("Archiving coupon<ID:" + couponId + ">...");

        //TODO: finish this once discounts re-implementaion will land
    }

}
