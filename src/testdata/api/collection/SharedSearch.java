package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class SharedSearch extends Helpers {

    @Step("[API] Create shared search with one search filter")
    public static void createSharedSearch_oneFilter() throws IOException {
        System.out.println("Creating a new shared search...");
        searchRandomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/sharedsearch/createSharedSearch_oneFilter.json");
        payload.putOpt("title", "Search " + searchRandomId);

        Response response = request.post(apiUrl + "/v1/shared-search", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());

            JSONObject responseJSON = new JSONObject(responseBody);
            searchCode = responseJSON.getString("code");
            searchId = responseJSON.getInt("id");

            System.out.println("Search ID: <" + searchId + ">");
            System.out.println("Search code: <" + searchCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create shared search with a single product: <{0}>")
    public static void createSharedSearch_singleProduct(String productName) throws IOException {
        System.out.println("Creating a new shared search...");
        searchRandomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/sharedsearch/createSharedSearch_singleProduct.json");
        payload.putOpt("title", "Search " + searchRandomId);
        payload.getJSONArray("query")
                .getJSONObject(1)
                .putOpt("display", "Product : Name : " + productName);
        payload.getJSONArray("query")
                .getJSONObject(1)
                .getJSONObject("value")
                .putOpt("value", productName);
        payload.getJSONObject("rawQuery")
                .getJSONObject("query")
                .getJSONObject("bool")
                .getJSONArray("must")
                .getJSONObject(0)
                .getJSONObject("match")
                .getJSONObject("title")
                .putOpt("query", productName);

        Response response = request.post(apiUrl + "/v1/shared-search", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());

            JSONObject responseJSON = new JSONObject(responseBody);
            searchId = responseJSON.getInt("id");
            searchCode = responseJSON.getString("code");

            System.out.println("Search ID: <" + searchId + ">");
            System.out.println("Search code: <" + searchCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create shared search with two search filters")
    public static void createSharedSearch_twoFilters() throws IOException {
        System.out.println("Creating a new shared search...");
        searchRandomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/sharedsearch/createSharedSearch_twoFilters.json");
        payload.putOpt("title", "Search " + searchRandomId);

        Response response = request.post(apiUrl + "/v1/shared-search", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());

            JSONObject responseJSON = new JSONObject(responseBody);
            searchCode = responseJSON.getString("code");
            searchId = responseJSON.getInt("id");

            System.out.println("Search ID: <" + searchId + ">");
            System.out.println("Search code: <" + searchCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Share search <searchCode:'{0}'> with <{1}>")
    protected static void shareSearch(String searchCode, String adminName) throws IOException {
        int adminId = getAdminId(adminName);
        System.out.println("Sharing saved search (code <" + searchCode + ">) with admin <" + adminName + ">, adminId <" + adminId + ">...");

        Response response = request.post(apiUrl + "/v1/shared-search/" + searchCode + "/associate",
                "{\"associates\": [" + adminId + "]}");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Delete search <searchId:'{0}'>")
    public static void deleteSearch(String searchCode) throws IOException {
        System.out.println("Deleting saved search with id <" + searchCode + ">.");

        Response response = request.delete(apiUrl + "/v1/shared-search/" + searchCode);

        if (response.code() == 204) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    private static int getAdminId(String adminName) throws IOException {
        System.out.println("Getting ID of <" + adminName + "> admin");
        int adminId_result = 0;

        JSONObject payload = parseObj("bin/payloads/sharedsearch/getAdminId.json");

        Response response = request.post(apiUrl + "/search/admin/store_admins_search_view/_search?size=50", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());

            JSONObject responseJSON = new JSONObject(responseBody);
            JSONArray resultArr = responseJSON.getJSONArray("result");

            for (int i = 0; i < resultArr.length(); i++) {
                JSONObject adminObj = resultArr.getJSONObject(i);
                String adminName_found = adminObj.getString("name");

                if (adminName_found.equals(adminName)) {
                    adminId_result = adminObj.getInt("id");
                    System.out.println("<" + adminName + "> ID: " + adminId_result);
                }
            }

            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }

        return adminId_result;
    }
}