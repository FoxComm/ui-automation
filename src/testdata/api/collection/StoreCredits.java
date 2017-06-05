package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class StoreCredits extends Helpers {

    @Step("[API] Issue store credit in amount of <{1}> for customer <{0}>")
    public static void issueStoreCredit(int customerId, int amount) throws IOException {
        System.out.println("Issuing store credit for customer <" + customerId + ">...");

        JSONObject payload = parseObj("bin/payloads/storecredits/issueStoreCredit.json");
        payload.putOpt("amount", amount);

        Response response = request.post(apiUrl + "/v1/customers/"+customerId+"/payment-methods/store-credit", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            scId = responseJSON.getInt("id");
            System.out.println("Store Credit ID: <" + scId + ">...");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Transfer GC <{1}> to store credits for customer <{0}>")
    public static void issueStoreCredit_gcTransfer(int customerId, String gcNumber) throws IOException {
        System.out.println("Transferring GC <" + gcNumber + "to SC for customer <" + customerId + ">...");

        JSONObject payload = parseObj("bin/payloads/storecredits/issueStoreCredit_gcTransfer.json");
        payload.putOpt("code", gcNumber);

        Response response = request.post(apiUrl + "/v1/gift-cards/"+gcNumber+"/convert/"+customerId, payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            scId = responseJSON.getInt("id");
            System.out.println("Store Credit ID: <" + scId + ">...");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Set store credit <ID:{0}> state to <{1}>")
    public static void setStoreCreditState(int id, String state) throws IOException {
        System.out.println("Setting SC <ID:" + id + "> state to <" + state + ">...");

        JSONObject payload = parseObj("bin/payloads/storecredits/setStoreCreditState.json");
        payload.putOpt("state", state);

        Response response = request.patch(apiUrl + "/v1/store-credits/" + id, payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

}
