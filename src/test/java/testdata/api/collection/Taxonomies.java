package testdata.api.collection;

import com.squareup.okhttp.Response;
import org.json.JSONObject;
import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

public class Taxonomies extends Helpers{

    @Step("[API] Create new flat taxonomy : <{0}>")
    public static void createFlatTaxonomy() throws IOException {
        System.out.println("Creating a new flat taxonomy");
        String randomId = generateRandomID();
        String tName = "Taxonomy " + randomId;

        JSONObject payload = parseObj("bin/payloads/taxonomies/createTaxonomy.json");
        payload.getJSONObject("attributes").getJSONObject("name").putOpt("v", tName);
        Response response = request.post(apiUrl + "/v1/taxonomies/default", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            taxonomyId = responseJSON.getInt("id");
            taxonomyName = responseJSON.getJSONObject("attributes").getJSONObject("name").getString("v");

            System.out.println("Taxonomy ID: " + taxonomyId);
            System.out.println("Taxonomy Name: " + taxonomyName);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Add taxon to flat taxonomy :<{0}>")
    public static void addTaxonToTaxonomy(int taxonomy_Id) throws IOException {
        System.out.println("Adding taxon to flat taxonomy");
        String randomId = generateRandomID();
        String taxon_Name = "Taxon " + randomId;

        JSONObject payload = parseObj("bin/payloads/taxonomies/createTaxon.json");
        payload.getJSONObject("attributes").getJSONObject("name").putOpt("v", taxon_Name);
        Response response = request.post(apiUrl + "/v1/taxonomies/default/" + String.valueOf(taxonomy_Id) +
                "/taxons", payload.toString());
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            JSONObject responseJSON = new JSONObject(responseBody);

            taxonId = responseJSON.getInt("id");
            taxonName = responseJSON.getJSONObject("attributes").getJSONObject("name").getString("v");

            System.out.println("Taxon ID: " + taxonId);
            System.out.println("Taxonomy Name: " + taxonName);
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }

    @Step("[API] Add taxon <{0}> to product <{1}>")
    public static void addTaxonToProduct(String productId, int taxonId) throws IOException{
        System.out.println("Adding taxon to product");

        Response response = request.patch(apiUrl + "/v1/taxons/default/" + taxonId + "/product/" + productId, "");
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("---- ---- ---- ----");
        } else {
            failTest(responseBody, response.code(), response.message());
        }
    }
}
