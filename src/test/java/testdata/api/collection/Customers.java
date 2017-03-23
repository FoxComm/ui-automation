package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Customers extends Helpers {

    @Step("[API] Create new customer")
    protected static void createCustomer() throws IOException {

        System.out.println("Creating a new customer...");

        String randomID = generateRandomID();
        customerName = "Test Buddy-" + randomID;
        customerEmail = "qatest2278+" + randomID + "@gmail.com";

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\n    \"name\": \"" + customerName + "\"," +
                "\n    \"email\": \"" + customerEmail + "\"\n}");

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("authorization", "Basic YWRtaW5AYWRtaW4uY29tOnBhc3N3b3Jr")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONObject responseJSON = new JSONObject(responseBody);
            customerId = responseJSON.getInt("id");
            System.out.println("Customer ID: " + customerId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Sign up a new customer -- name:<{0}> email:<{1}>")
    protected static void signUpCustomer(String name, String email) throws IOException {
        System.out.println("Registering a new customer on Storefront...");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"email\": \""+email+"\"," +
                "\"name\": \""+name+"\"," +
                "\"password\": \"78qa22!#\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/public/registrations/new")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONObject responseJSON = new JSONObject(responseBody);
            customerId = responseJSON.getInt("id");
            customerName = responseJSON.getString("name");
            customerEmail = responseJSON.getString("email");
            System.out.println("Customer ID: " + customerId);
            System.out.println("Customer Name: " + customerName);
            System.out.println("Customer Email: " + customerEmail);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Blacklist customer ID:<{0}>")
    protected static void blacklistCustomer(int customerId) throws IOException {
        System.out.println("Blacklisting customer ID:<" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"blacklisted\": true\n}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/blacklist")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] View customer <{0}>")
    private static void viewCustomer(int customerId) throws IOException {

        System.out.println("Getting name and email of customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            customerName = responseBody.substring(55, 73);
            customerEmail = responseBody.substring(20, 45);
            System.out.println("Customer name: <" + customerName + ">");
            System.out.println("Customer email: <" + customerEmail + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create new shipping address for customer <{0}>")
    protected static void createAddress(int customerId, String name, int regionId, int countryId, String region_name, String address1, String address2, String city, String zip, String phoneNumber, boolean isDefault)
            throws IOException {

        System.out.println("Creating a new address for customer " + customerId + "...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"name\":\"" + name + "\"," +
                "\"regionId\":" + regionId + "," +
                "\"countryId\":" + countryId + "," +
                "\"region_name\":\"" + region_name + "\"," +
                "\"address1\":\"" + address1 + "\"," +
                "\"address2\":\"" + address2 + "\"," +
                "\"city\":\""+ city + "\"," +
                "\"zip\":\"" + zip + "\"," +
                "\"phoneNumber\":\"" + phoneNumber + "\"," +
                "\"isDefault\":" + isDefault + "}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Delete address <ID:{1}> from customer <ID:{0}>")
    public static void deleteAddress(int customerId, int addressId) throws IOException {
        System.out.println("Deleting address <ID:" + addressId + "> from customer <ID:" + customerId + "> address book");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId)
                .delete(null)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 204) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] List all shipping addresses of customer <{0}>")
    protected static void listCustomerAddresses(int customerId) throws IOException {

        System.out.println("Listing all addresses of customer " + customerId + "...");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses")
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            JSONArray responseJSON = new JSONArray(responseBody);
            addressId1 = responseJSON.getJSONObject(0).getInt("id");
            System.out.println("Address 1: <" + addressId1 + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] View address details, customerID: <{0}>, addressID: <{1}>")
    protected static void getCustomerAddress(int customerId, int addressId) throws IOException {

        listCustomerAddresses(customerId);
        System.out.println("Getting address with ID <" + addressId + "> of customer <" + customerId + ">...");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId)
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            addressPayload = responseBody;
            System.out.println("Address 1: <" + addressId1 + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Update shipping address details. addressID: <{1}>, customerId: <{0}>")
    protected static void updateCustomerShipAddress(int customerId, int addressId1, String customerName, int regionId, String address1, String address2, String city, String zip, String phoneNumber) throws IOException {

        System.out.println("Updating shipping address ID:<" + addressId1 + "> for customer <" + customerId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{" +
                "\"name\":\"" + customerName + "\"," +
                "\"regionId\":" + regionId + "," +
                "\"address1\":\"" + address1 + "\"," +
                "\"address2\":\"" + address2 + "\"," +
                "\"city\":\"" + city + "\"," +
                "\"zip\":\"" + zip + "\"," +
                "\"isDefault\":false," +
                "\"phoneNumber\":\"" + phoneNumber + "\"}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId1)
                .patch(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

    @Step("[API] Create credit card")
    protected static void createCreditCard(int customerId, String holderName, String cardNumber, int expMonth, int expYear, int cvv, String brand) throws IOException {

        getStripeToken(customerId, cardNumber, expMonth, expYear, cvv);

        System.out.println("Create a new credit card for customer <" + customerId + ">...");

        JSONObject jsonObj = parse("bin/payloads/createCreditCard.json");
        JSONObject address = new JSONObject(addressPayload);
        JSONObject region = address.getJSONObject("region");

        jsonObj.putOpt("token", stripeToken);
        jsonObj.putOpt("holderName", holderName);
        jsonObj.putOpt("lastFour", cardNumber.substring(12, 16));
        jsonObj.putOpt("expMonth", expMonth);
        jsonObj.putOpt("expYear", expYear);
        jsonObj.putOpt("brand", brand);
        jsonObj.putOpt("billingAddress", address);
        jsonObj.getJSONObject("billingAddress").putOpt("regionId", region.getInt("id"));
        jsonObj.getJSONObject("billingAddress").putOpt("state", region.getString("name"));
        jsonObj.getJSONObject("billingAddress").putOpt("country", "United States");
        String payload = jsonObj.toString();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, payload);
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            org.json.JSONObject responseJSON = new org.json.JSONObject(responseBody);
            creditCardId = responseJSON.getInt("id");
            System.out.println("Credit Card ID: <" + creditCardId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] List all credit cards of customer ID:<{0}>")
    protected static void listCreditCards(String customerId) throws IOException {
        System.out.println("List all credit cards of customer ID:<{0}");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards")
                .get()
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            JSONArray responseJSON = new JSONArray(responseBody);

            for (int i = 0; i < responseJSON.length(); i++) {
                creditCardId = responseJSON.getJSONObject(i).getInt("id");
                creditCardsIDs.add(creditCardId);
            }
            printIntList(creditCardsIDs);

            System.out.println(responseCode + " " + responseMsg);
            System.out.println("Success!");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Set credit card ID:<{1}> as default card for customer ID:<{0}>")
    protected static void setCardAsDefault(int customerId, int creditCardId) throws IOException {
        System.out.println("Set credit card ID:<" + customerId + "> as default for customer ID:<" + creditCardId + ">...");

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"isDefault\": true\n}");
        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards/" + creditCardId + "/default")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 200) {
            System.out.println(responseCode + " " + responseMsg);
            System.out.println("Success!");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }
    }

    @Step("[API] Delete credit cart <ID:{1} of customer <ID:{0}>")
    protected static void deleteCreditCard(int customerId, int creditCardId) throws IOException {

        System.out.println("Deleting credit cart <ID:{1} of customer <ID:{0}>...");

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards/" + creditCardId)
                .delete(null)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("JWT", jwt)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        int responseCode = response.code();
        String responseMsg = response.message();

        if (responseCode == 204) {
            System.out.println(responseCode + " " + responseMsg);
            org.json.JSONObject responseJSON = new org.json.JSONObject(responseBody);
            creditCardId = responseJSON.getInt("id");
            System.out.println("Credit Card ID: <" + creditCardId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, responseCode, responseMsg);
        }

    }

}
