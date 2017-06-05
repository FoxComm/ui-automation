package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class GiftCards extends Helpers {

    @Step("[API] Issue GC <balance: {0}>, <QTY: {1}>")
    public static void createGiftCard(int balance, int quantity) throws IOException {
        System.out.println("Creating new gift card...");
        JSONObject payload = parseObj("bin/payloads/giftcards/createGiftCard.json");
        payload.putOpt("balance", balance);
        payload.putOpt("quantity", quantity);

        Response response = request.post(apiUrl + "/v1/gift-cards", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            gcCode = responseJSON.getString("code");
            System.out.println("GC code: <" + gcCode + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    /**
     * Possible state values: active, onHold, canceled
     */
    @Step("[API] Set GC <{0}> state to <{1}>")
    public static void setGiftCardState(String gcNumber, String state) throws IOException {
        System.out.println("Putting <" + gcNumber + "> on hold..." );

        JSONObject payload = parseObj("bin/payloads/giftcards/setGiftCardState.json");
        payload.putOpt("state", state);

        Response response = request.patch(apiUrl + "/v1/gift-cards/" + gcNumber, payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }

    }

}
