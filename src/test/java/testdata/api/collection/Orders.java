package testdata.api.collection;

import com.squareup.okhttp.*;
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

}
