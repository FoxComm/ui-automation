package testdata.api.collection;

import com.squareup.okhttp.*;
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

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"id\": null,\"createdAt\": null,\"attributes\": {\"usageRules\": {\"t\": \"usageRules\",\"v\": {\"isExclusive\": false,\"isUnlimitedPerCode\": true,\"usesPerCode\": 1,\"isUnlimitedPerCustomer\": true,\"usesPerCustomer\": 1}},\"name\": {\"t\": \"string\",\"v\": \"test coupon " + randomId + "\"},\"storefrontName\": {\"t\": \"richText\",\"v\": \"storefront name " + randomId + "\"},\"description\": {\"t\": \"text\",\"v\": \"test description\"},\"details\": {\"t\": \"richText\",\"v\": \"<p>test details</p>\"},\"activeFrom\": {\"v\": \"2017-01-24T22:03:58.698Z\",\"t\": \"datetime\"},\"activeTo\": {\"v\": null,\"t\": \"datetime\"}},\"promotion\": " + promotionId + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/coupons/default")
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
            couponId = String.valueOf(responseJSON.getInt("id"));
            couponName = "test coupon " + randomId;
            System.out.println("Coupon ID: " + couponId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Generate a single code for coupon ID:<{0}>")
    public static void generateSingleCode(String couponId) throws IOException {

        System.out.println("Generating a single code for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/coupons/codes/generate/" + couponId + "/NWCPN-" + generateRandomID())
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
            singleCouponCode = responseBody.substring(1, responseBody.length() - 1);
            System.out.println("Single coupon code: " + singleCouponCode);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Bulk generate codes for coupon <ID:{0}>; [prefix:{1}, codeLength:{2}, QTY:{3}]")
    public static void bulkGenerateCodes(String couponId, String prefix, int codeLength, int quantity) throws IOException {

        int length = prefix.length() + codeLength;

        System.out.println("Bulk generating coupon codes for coupon <" + couponId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"prefix\":\"" + prefix + "\"," +
                "\"length\":" + length + "," +
                "\"quantity\":" + quantity + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/coupons/codes/generate/" + couponId)
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
//            DEBUG
//            int startIndex = 2;
//            for (int i = 0; i < quantity; i++) {
//                String code = responseBody.substring(startIndex, startIndex + length);
//                bulkCodes.add(code);
//                startIndex += (length + 3);
//            }
            bulkCodes.clear();
            JSONArray responseJSON = new JSONArray(responseBody);
            for (int i = 0; i < responseJSON.length(); i++) {
                bulkCodes.add(responseJSON.getString(i));
            }
            System.out.println("Number of codes generated: <" + bulkCodes.size() + ">");
            assertEquals(bulkCodes.size(), quantity,
                    "Amount of generated codes is lower than requested amount.");
            printStringList(bulkCodes);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
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
