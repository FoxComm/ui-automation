package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Inventory extends Helpers {

    @Step("Get inventory id of <{0}>")
    private static void viewSKU_inventory(String skuCode) throws IOException {
        System.out.println("Viewing inventory summary of SKU <" + skuCode + ">...");

        Response response = request.get(apiUrl + "/v1/inventory/summary/" + skuCode);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            skuId_inventory = responseJSON.getJSONArray("summary")
                                            .getJSONObject(0)
                                            .getJSONObject("stockItem")
                                            .getInt("id");
            System.out.println("skuId_inventory: <" + skuId_inventory + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    /**
     * Given arg "String type" value should be capitalized, e.g. - "Sellable"
     */
    @Step("[API] Increase amount of sellable units of <{0}> by <{1}>")
    public static void increaseOnHandQty(String skuCode, String type, Integer qty) throws IOException {
        checkInventoryAvailability(skuCode);
        viewSKU_inventory(skuCode);
        System.out.println("Increase amount of sellable items by <" + qty + "> for SKU <" + skuCode + ">, inventoryId: <" + skuId_inventory + ">...");

        JSONObject payload = parseObj("bin/payloads/inventory/increaseOnHandQty.json");
        payload.putOpt("qty", qty);
        payload.putOpt("type", type);

        Response response = request.patch(apiUrl + "/v1/inventory/stock-items/" + skuId_inventory + "/increment", payload.toString());

        if (response.code() == 204) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
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

        while((System.currentTimeMillis() < end) && (responseCode != 200)) {
            Response response = request.get(apiUrl + "/v1/inventory/summary/" + sku);
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
