package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Inventory extends Helpers {

    private static void viewSKU_inventory(String skuCode) throws IOException {

        System.out.println("Viewing inventory summary of SKU <" + skuCode + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/inventory/summary/" + skuCode)
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
            System.out.println(responseCode + " " + responseMsg);
            JSONObject responseJSON = new JSONObject(responseBody);
            JSONObject obj = responseJSON.getJSONArray("summary").getJSONObject(0);
            skuId_inventory = obj.getJSONObject("stockItem").getInt("id");
            System.out.println("SKU ID: <" + skuId_inventory + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    /**
     * Given arg "String type" value should be capitalized, e.g. - "Sellable"
     */
    @Step("[API] Increase amount of sellable unites of <{0}> by <{1}>")
    public static void increaseOnHandQty(String skuCode, String type, Integer qty) throws IOException {

        checkInventoryAvailability(skuCode);
        viewSKU_inventory(skuCode);

        System.out.println("Increase amount of sellable items by <" + qty + "> for SKU <" + skuCode + ">, ID: <" + skuId_inventory + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"qty\":" + qty + "," +
                "\"type\":\"" + type + "\"," +
                "\"status\":\"onHand\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/inventory/stock-items/" + skuId_inventory + "/increment")
                .patch(body)
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 204) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Check if Inventory is available")
    public static void checkInventoryAvailability(String sku) throws IOException {

        System.out.println("Checking if inventory of SKU <" + sku + "> is available...");

        int responseCode = 0;
        String responseBody = "";
        String responseMsg = "";
        long time = System.currentTimeMillis();
        long end = time + 15000;
        int totalTries = 0;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/inventory/summary/" + sku)
                .get()
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        while((System.currentTimeMillis() < end) && (responseCode != 200)) {
            Response response = client.newCall(request).execute();
            responseBody = response.body().string();
            responseCode = response.code();
            responseMsg = response.message();
            totalTries ++;
        }
        System.out.println("totalTries: <" + totalTries + ">");
        System.out.println("Time spent: <" + (System.currentTimeMillis() - time) + " MS>");

        if (responseCode == 200) {
            System.out.println("Inventory is created");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

}
