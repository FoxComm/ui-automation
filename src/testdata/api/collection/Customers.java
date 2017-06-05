package testdata.api.collection;

import com.squareup.okhttp.*;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Customers extends Helpers {

    @Step("[API] Create new customer")
    public static void createCustomer() throws IOException {
        System.out.println("Creating a new customer...");
        String randomID = generateRandomID();

        JSONObject payload = parseObj("bin/payloads/customers/createCustomer.json");
        payload.putOpt("name", "Customer " + randomID);
        payload.putOpt("email", "qatest2278+" + randomID + "@gmail.com");

        Response response = request.post(apiUrl + "/v1/customers", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            customerId = responseJSON.getInt("id");
            customerName = responseJSON.getString("name");
            customerEmail = responseJSON.getString("email");
            System.out.println("Customer ID: " + customerId);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Sign up a new customer -- name:<{0}> email:<{1}>")
    public static void signUpCustomer(String name, String email) throws IOException {
        System.out.println("Registering a new customer on Storefront...");

        JSONObject payload = parseObj("bin/payloads/customers/signUpCustomer.json");
        payload.putOpt("name", name);
        payload.putOpt("email", email);

        Response response = request.post(apiUrl + "/v1/public/registrations/new", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            customerId = responseJSON.getInt("id");
            customerName = responseJSON.getString("name");
            customerEmail = responseJSON.getString("email");
            System.out.println("Customer ID: " + customerId);
            System.out.println("Customer Name: " + customerName);
            System.out.println("Customer Email: " + customerEmail);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Blacklist customer ID:<{0}>")
    public static void blacklistCustomer(int customerId) throws IOException {
        System.out.println("Blacklisting customer ID:<" + customerId + ">...");

        Response response = request.post(apiUrl + "/v1/customers/" + customerId + "/blacklist",
                "{\"blacklisted\": true}");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] View customer <{0}>")
    private static void viewCustomer(int customerId) throws IOException {

        System.out.println("Getting name and email of customer <" + customerId + ">...");

        Response response = request.get(apiUrl + "/v1/customers/" + customerId);
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            customerName = responseJSON.getString("name");
            customerEmail = responseJSON.getString("email");
            System.out.println("Customer name: <" + customerName + ">");
            System.out.println("Customer email: <" + customerEmail + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Create new shipping address for customer <{0}>")
    public static void createAddress(int customerId,
                                     String name,
                                     int regionId,
                                     int countryId,
                                     String region_name,
                                     String address1,
                                     String address2,
                                     String city,
                                     String zip,
                                     String phoneNumber,
                                     boolean isDefault
    ) throws IOException {
        System.out.println("Creating a new address for customer " + customerId + "...");

        JSONObject payload = parseObj("bin/payloads/cart/setShipAddress.json");
        payload.putOpt("name", name);
        payload.putOpt("regionId", regionId);
        payload.putOpt("address1", address1);
        payload.putOpt("address2", address2);
        payload.putOpt("city", city);
        payload.putOpt("zip", zip);
        payload.putOpt("isDefault", isDefault);
        payload.putOpt("phoneNumber", phoneNumber);

        Response response = request.post(apiUrl + "/v1/customers/" + customerId + "/addresses", payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Delete address <ID:{1}> from customer <ID:{0}>")
    public static void deleteAddress(int customerId, int addressId) throws IOException {
        System.out.println("Deleting address <ID:" + addressId + "> from customer <ID:" + customerId + "> address book");

        Response response = request.delete(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId);

        if (response.code() == 204) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] List all shipping addresses of customer <{0}>")
    public static void listCustomerAddresses(int customerId) throws IOException {
        System.out.println("Listing all addresses of customer " + customerId + "...");

        Response response = request.get(apiUrl + "/v1/customers/" + customerId + "/addresses");
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONArray responseJSON = new JSONArray(responseBody);
            addressId1 = responseJSON.getJSONObject(0).getInt("id");
            System.out.println("Address 1: <" + addressId1 + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] View address details, customerID: <{0}>, addressID: <{1}>")
    private static String getCustomerAddress(int customerId, int addressId) throws IOException {
        System.out.println("Getting address with ID <" + addressId + "> of customer <" + customerId + ">...");
        listCustomerAddresses(customerId);

        Response response = request.get(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId);

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }

        return response.body().string();
    }

    private static void getStripeToken(int customerId, int addressId, String cardNumber, int expMonth, int expYear, int cvv) throws IOException {

        JSONObject payload = parseObj("bin/payloads/customers/getStripeToken.json");

        // create JSONObject from addressPayload and from "address" obj in .JSON that will be used as a payload
        JSONObject address_tmp = new JSONObject(getCustomerAddress(customerId, addressId));
        JSONObject billAddress = payload.getJSONObject("address");

        // write data from addressPayload JSONObj to "address" obj of payload .JSON
        billAddress.putOpt("name", address_tmp.getString("name"));
        billAddress.putOpt("zip", address_tmp.getString("zip"));
        billAddress.putOpt("city", address_tmp.getString("city"));
        billAddress.putOpt("address1", address_tmp.getString("address1"));
        billAddress.putOpt("regionId", address_tmp.getJSONObject("region").getInt("id"));

        payload.putOpt("customerId", customerId);
        payload.putOpt("address", billAddress);
        payload.putOpt("cardNumber", cardNumber);
        payload.putOpt("expMonth", expMonth);
        payload.putOpt("expYear", expYear);
        payload.putOpt("cvv", cvv);

        Response response = request.post(apiUrl + "/v1/credit-card-token", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            org.json.JSONObject responseJSON = new org.json.JSONObject(responseBody);
            stripeToken = responseJSON.getString("token");
            System.out.println("Stripe token: <" + stripeToken + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Update shipping address details. addressID: <{1}>, customerId: <{0}>")
    protected static void updateCustomerShipAddress(int customerId,
                                                    int addressId1,
                                                    String name,
                                                    int regionId,
                                                    String address1,
                                                    String address2,
                                                    String city,
                                                    String zip,
                                                    boolean isDefault,
                                                    String phoneNumber
    ) throws IOException {
        System.out.println("Updating shipping address ID:<" + addressId1 + "> for customer <" + customerId + ">...");

        JSONObject payload = parseObj("bin/payloads/cart/setShipAddress.json");
        payload.putOpt("name", name);
        payload.putOpt("regionId", regionId);
        payload.putOpt("address1", address1);
        payload.putOpt("address2", address2);
        payload.putOpt("city", city);
        payload.putOpt("zip", zip);
        payload.putOpt("isDefault", isDefault);
        payload.putOpt("phoneNumber", phoneNumber);

        Response response = request.patch(apiUrl + "/v1/customers/" + customerId + "/addresses/" + addressId1, payload.toString());

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Create credit card")
    public static void createCreditCard(int customerId,
                                        String holderName,
                                        String cardNumber,
                                        int expMonth,
                                        int expYear,
                                        int cvv,
                                        String brand,
                                        int addressId
    ) throws IOException {
        System.out.println("Create a new credit card for customer <" + customerId + ">...");

        getStripeToken(customerId, addressId, cardNumber, expMonth, expYear, cvv);

        JSONObject payload = parseObj("bin/payloads/customers/createCreditCard.json");
        JSONObject address = new JSONObject(getCustomerAddress(customerId, addressId));
        JSONObject region = address.getJSONObject("region");

        payload.putOpt("token", stripeToken);
        payload.putOpt("holderName", holderName);
        payload.putOpt("lastFour", cardNumber.substring(12, 16));
        payload.putOpt("expMonth", expMonth);
        payload.putOpt("expYear", expYear);
        payload.putOpt("brand", brand);
        payload.putOpt("billingAddress", address);
        payload.getJSONObject("billingAddress").putOpt("regionId", region.getInt("id"));
        payload.getJSONObject("billingAddress").putOpt("state", region.getString("name"));
        payload.getJSONObject("billingAddress").putOpt("country", "United States");

        Response response = request.post(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);
            creditCardId = responseJSON.getInt("id");
            System.out.println("Credit Card ID: <" + creditCardId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] List all credit cards of customer ID:<{0}>")
    protected static void listCreditCards(String customerId) throws IOException {
        System.out.println("List all credit cards of customer ID:<{0}");

        Response response = request.get(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards");
        String responseBody = response.body().string();

        if (response.code() == 200) {
            JSONArray responseJSON = new JSONArray(responseBody);
            for (int i = 0; i < responseJSON.length(); i++) {
                creditCardId = responseJSON.getJSONObject(i).getInt("id");
                creditCardsIDs.add(creditCardId);
            }
            printIntList(creditCardsIDs);

            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Set credit card ID:<{1}> as default card for customer ID:<{0}>")
    public static void setCardAsDefault(int customerId, int creditCardId) throws IOException {
        System.out.println("Set credit card ID:<" + customerId + "> as default for customer ID:<" + creditCardId + ">...");

        Response response = request.post(apiUrl + "/v1/customers/"+customerId+"/payment-methods/credit-cards/"+creditCardId+"/default",
                "{\"isDefault\": true}");

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("Success!");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(response.body().string(), response.code(), response.message());
        }
    }

    @Step("[API] Delete credit cart <ID:{1} of customer <ID:{0}>")
    protected static void deleteCreditCard(int customerId, int creditCardId) throws IOException {

        System.out.println("Deleting credit cart <ID:{1} of customer <ID:{0}>...");

        Response response = request.delete(apiUrl + "/v1/customers/" + customerId + "/payment-methods/credit-cards/" + creditCardId);
        String responseBody = response.body().string();

        if (response.code() == 204) {
            System.out.println(response.code() + " " + response.message());
            org.json.JSONObject responseJSON = new org.json.JSONObject(responseBody);
            creditCardId = responseJSON.getInt("id");
            System.out.println("Credit Card ID: <" + creditCardId + ">");
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }

    }

}