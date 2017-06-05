package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Orders extends Helpers {

    @Step("[API] Change order <{0}> state to <{1}>")
    public static void changeOrderState(String orderId, String newState) throws IOException {
        System.out.println("Change state of order <" + orderId + "> to <" + newState + ">...");

        Response response = request.patch(apiUrl + "/v1/orders/" + orderId, "{\"state\":\"" + newState + "\"}");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    public static String getAppliedCouponCode(String orderId) throws IOException {
        System.out.println("Getting coupon code applied to order <" + orderId + ">...");

        Response response = request.get(apiUrl + "/v1/orders/" + orderId);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

//            orderAppliedCoupon = responseJSON.getJSONObject();
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
        return orderAppliedCoupon;
    }

}
