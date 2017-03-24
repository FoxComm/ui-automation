package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Auth extends Helpers {

    @Step("[API] Log in as admin")
    public static void loginAsAdmin() throws IOException {
        System.out.println("Authorizing as an admin...");

        JSONObject payload = parseObj("bin/payloads/auth/loginAsAdmin.json");
        payload.putOpt("email", adminEmail);
        payload.putOpt("password", adminPassword);
        payload.putOpt("org", adminOrg);

        Response response = request.post_noJWT(apiUrl + "/v1/public/login", payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            jwt = response.header("JWT");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

}