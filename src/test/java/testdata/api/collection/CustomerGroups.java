package testdata.api.collection;

import com.squareup.okhttp.Response;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class CustomerGroups extends Helpers{

    @Step("[API] Create new manual customer group")
    public static void createManualCustomerGroup(String name) throws IOException{
        System.out.println("Creating a new manual customer group");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/customergroups/createManualCustomerGroup.json");
        payload.putOpt("name", name + " " + randomId);
        Response response = request.post(apiUrl + "/v1/customer-groups", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            customerGroupId = responseJSON.getInt("id");
            customerGroupName = responseJSON.getString("name");
            System.out.println("CustomerGroup ID: " + customerGroupId);
            System.out.println("CustomerGroup Name: " + customerGroupName);
            System.out.println("---- ---- ---- ----");
        }else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create new dynamic customer group with one criteria")
         public static void createDynamicCustomerGroupWithOneCriteria(String name) throws IOException{
        System.out.println("Creating a new dynamic customer group with one criteria");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/customergroups/createDynamicCustomerGroup.json");
        payload.putOpt("name", name + " " + randomId);
        Response response = request.post(apiUrl + "/v1/customer-groups", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            customerGroupId = responseJSON.getInt("id");
            customerGroupName = responseJSON.getString("name");
            System.out.println("CustomerGroup ID: " + customerGroupId);
            System.out.println("CustomerGroup Name: " + customerGroupName);
            System.out.println("---- ---- ---- ----");
        }else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create new dynamic customer group with two criterias")
    public static void createDynamicCustomerGroupWithTwoCriterias(String name) throws IOException{
        System.out.println("Creating a new dynamic customer group with two criterias");
        String randomId = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/customergroups/createDynamicCustomerGroupWithTwoCriterias.json");
        payload.putOpt("name", name + " " + randomId);
        Response response = request.post(apiUrl + "/v1/customer-groups", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            customerGroupId = responseJSON.getInt("id");
            customerGroupName = responseJSON.getString("name");
            System.out.println("CustomerGroup ID: " + customerGroupId);
            System.out.println("CustomerGroup Name: " + customerGroupName);
            System.out.println("---- ---- ---- ----");
        }else {
            failTest(responseBody, response.code(), response.message());
        }
    }


}
