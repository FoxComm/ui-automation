import base.BasePage;
import base.BaseTest;
import com.codeborne.selenide.Condition;
import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.OrderDetailsPage;
import pages.SkusPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.testng.Assert.assertEquals;

public class TestClass extends DataProvider {

    @Test
    public void testTest() throws IOException {

        System.out.println("[ " + apiUrl + " | " + adminEmail + " | " + adminPassword + " | " + adminOrg + " ]");

        loginAsAdmin();
        createSKU_active();
        checkInventoryAvailability(sku);

    }

}