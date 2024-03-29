package testdata.api.collection;

import com.squareup.okhttp.Response;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;
import tests.storefront.StripeTest;

import java.io.IOException;

public class CustomerGroups extends Helpers {

    @Step("[API] Create new manual customer group : <{0}>")
    public static void mCGroup(String name) throws IOException{
        System.out.println("Creating a new manual customer group");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/customergroups/createMCG.json");
        payload.putOpt("name", name + " " + randomId);
        Response response = request.post(apiUrl + "/v1/customer-groups", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            customerGroupId = responseJSON.getInt("id");
            customerGroupName = responseJSON.getString("name");
            customerGroupType = responseJSON.getString("groupType");
            System.out.println("CustomerGroup ID: " + customerGroupId);
            System.out.println("CustomerGroup Name: " + customerGroupName);
            System.out.println("CustomerGroup Type: " + customerGroupType);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Add customer <{0}> to customer group <{1}>:")
    public static void addCustomerToGroup(int customerToAddId, int customerGroupForAddId) throws IOException{
        System.out.println("Adding customer to a manual customer group");

        JSONObject payload = parseObj("bin/payloads/customergroups/addCustomerToMCG.json");
        int[]arr = {customerToAddId};
        payload.putOpt("toAdd", arr);
        Response response = request.post(apiUrl + "/v1/customer-groups/" + customerGroupForAddId + "/customers", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 204) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("Customer: " + customerToAddId + " is added to group : " + customerGroupForAddId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create new dynamic customer group with one criteria : <{0}>")
         public static void dCGroup_oneCriteria(String name) throws IOException{
        System.out.println("Creating a new dynamic customer group with one criteria");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/customergroups/createDCG.json");
        payload.putOpt("name", name + " " + randomId);
        Response response = request.post(apiUrl + "/v1/customer-groups", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            customerGroupId = responseJSON.getInt("id");
            customerGroupName = responseJSON.getString("name");
            customerGroupType = responseJSON.getString("groupType");
            System.out.println("CustomerGroup ID: " + customerGroupId);
            System.out.println("CustomerGroup Name: " + customerGroupName);
            System.out.println("CustomerGroup Type: " + customerGroupType);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create new dynamic customer group with two criterias : <{0}>")
    public static void dCGroup_twoCriterias(String name) throws IOException{
        System.out.println("Creating a new dynamic customer group with two criterias");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/customergroups/createDCG_twoEmptyCriterias.json");
        payload.putOpt("name", name + " " + randomId);
        Response response = request.post(apiUrl + "/v1/customer-groups", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            customerGroupId = responseJSON.getInt("id");
            customerGroupName = responseJSON.getString("name");
            customerGroupType = responseJSON.getString("groupType");
            System.out.println("CustomerGroup ID: " + customerGroupId);
            System.out.println("CustomerGroup Name: " + customerGroupName);
            System.out.println("CustomerGroup Type: " + customerGroupType);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create new dynamic customer group with defined name contains criteria : <{0}>")
    public static void dCGroup_definedNameCriteria(String nameCriteria) throws IOException{

        System.out.println("Creating a new dynamic customer group" + generateRandomID() + "with defined criteria - Name contains=" + nameCriteria);

        JSONObject payload = parseObj("bin/payloads/customergroups/createDCG_OneCriteria.json");
        String groupName = "DCG " + generateRandomID() + " criteria." + nameCriteria;
        payload.putOpt("name", groupName);
        payload.getJSONObject("clientState").getJSONArray("conditions").
                put(0, new String[]{"name", "containsNotAnalyzed", nameCriteria});
        payload.getJSONObject("elasticRequest").getJSONObject("query")
                .getJSONObject("bool").getJSONArray("must").getJSONObject(0)
                .getJSONObject("match").getJSONObject("name").putOpt("query", nameCriteria);

        Response response = request.post(apiUrl + "/v1/customer-groups", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            customerGroupId = responseJSON.getInt("id");
            customerGroupName = responseJSON.getString("name");
            customerGroupType = responseJSON.getString("groupType");
            System.out.println("CustomerGroup ID: " + customerGroupId);
            System.out.println("CustomerGroup Name: " + customerGroupName);
            System.out.println("CustomerGroup Type: " + customerGroupType);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

}
