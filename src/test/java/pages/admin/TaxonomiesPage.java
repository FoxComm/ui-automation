package pages.admin;

import base.AdminBasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class TaxonomiesPage extends AdminBasePage {

    private SelenideElement addTaxonomyBtn(){
        return $(xpath("//*[contains(@class,'fc-list-page-header')]//span[contains(text(),'Taxonomy')]"));
    }

    private SelenideElement newName(){
        return $(xpath("//*[contains(@class,'fc-form-field')]/input"));
    }

    private SelenideElement removeStartDateBtn() {
        return $(xpath("//a[@id='fct-remove-start-date-btn']"));
    }

    private SelenideElement stateDd() {
        return $(xpath("//div[@id='state-dd']"));
    }

    public SelenideElement name_form() {
        return $(xpath("//*[contains(@class,'page-context')]//*[contains(@class,'__title')]"));
    }

    public SelenideElement name_searchView(String taxonomyName){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + taxonomyName+ "')]"));
    }

    public ElementsCollection names_searchView(String groupName){
        return $$(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + groupName + "')]"));
    }

    public SelenideElement taxonomyName_productForm(String taxonomyName){
        return $(xpath("//*[contains(@class,'taxonomies')]//*[contains(@class,'header')" +
                " and contains(text(),'" + taxonomyName + "')]"));
    }

    public SelenideElement status_searchView(String name, String status){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" +
                name + "')]/..//*[contains(text(),'" + status + "')]"));
    }

    public SelenideElement valuesTabLink(){
        return $(xpath("//*[contains(@class,'link') and contains(text(),'Values')]"));
    }

    public SelenideElement addTaxonValueBtn(){
        return $(xpath("//*[contains(@class,'table__header')]//span[contains(text(),'Value')]"));
    }

    public SelenideElement counterValues_searchView(String taxonomyName){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + taxonomyName+ "')]" +
                "/../*[contains(@class,'valuesCount')]"));
    }

    public SelenideElement counterProducts_searchView(String taxonName) {
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + taxonName+ "')]" +
                "/../*[contains(@class,'productsCount')]"));
    }

    public SelenideElement showTaxonBtn_productForm(String taxonomyName){
        return $(xpath("//*[contains(@class,'aside')]//*[contains(text(),'" + taxonomyName + "')]/button"));
    }

    public SelenideElement enterTaxonFld_productForm(String taxonomyName){
        return $(xpath("//*[contains(@class,'aside')]//*[contains(text(),'" + taxonomyName + "')]/../*//input"));
    }

    public SelenideElement taxonDdItemFld_productForm(String taxonomyName, String taxonName){
        return $(xpath("//*[contains(@class,'aside')]//*[contains(text(),'" + taxonomyName + "')]" +
                "/..//*[contains(@class,'fc-dropdown__item') and contains(text(),'" + taxonName + "')]"));
    }

    public SelenideElement taxonomy_navigationBar(String name){
        return $(xpath("//*[contains(@class,'fc-navigation-item__link')]//*[contains(text(),'"
                + name + "')]"));
    }

    public SelenideElement addedTaxonBar_productForm(String taxonomyName, String taxonName){
        return $(xpath("//*[contains(@class,'aside')]//*[contains(text(),'" +
                taxonomyName + "')]/..//*[contains(text(),'" + taxonName + "')]"));
    }

    public SelenideElement removeTaxonBar_productForm(String taxonomyName, String taxonName){
        return $(xpath("//*[contains(@class,'aside')]//*[contains(text(),'" +
                taxonomyName + "')]/..//*[contains(text(),'" + taxonName + "')]/following-sibling::button"));
    }

    @Step("Click \"Add Taxonomy\" Btn")
    public void clickAddTaxonomyBtn(){
        click(addTaxonomyBtn());
    }

    @Step("Set \"name\" to <{0}>")
    public void setName(String taxonomyName){
        setFieldVal(newName(), taxonomyName);
    }

    @Step("Set Taxonomy state to <{0}>")
    public void setState(String state) {
        switch (state) {
            case "Inactive":
                click(removeStartDateBtn());
                break;
            case "Active":
                setDdVal(stateDd(), state);
                break;
        }
    }

    @Step("Click \"Values\" tab in taxonomy form")
    public void clickValuesTabFld(){
        click(valuesTabLink());
    }

    @Step("Click \"Add value\" btn")
    public void clickAddTaxonValueBtn(){
        click(addTaxonValueBtn());
    }

    @Step("Click \"Add taxon value\" btn")
    public void clickShowTaxonBtn_productForm(String taxonomyName){
        click(showTaxonBtn_productForm(taxonomyName));
    }

    @Step("Click \"Enter taxon\" fld")
    public void clickEnterTaxonFld(String taxonomyName){
        click(enterTaxonFld_productForm(taxonomyName));
    }

    @Step("Enter \"Taxon value\" <{1}> to fld")
    public void enterTaxonFld_productForm(String taxonomyName, String taxonName){
        setFieldVal(enterTaxonFld_productForm(taxonomyName), taxonName);
    }

    @Step("Click \"Taxon value\" <{1}> fld")
    public void clickTaxonValueFld(String taxonomyName, String taxonName){
        click(taxonDdItemFld_productForm(taxonomyName, taxonName));
    }

    @Step("Click search view item <{0}>")
    public void clickSearchViewItem(String name){
        click(name_searchView(name));
    }

    @Step("Click remove taxon button")
    public void clickRemoveTaxonBar_productForm(String taxonomyName, String taxonName){
        click(removeTaxonBar_productForm(taxonomyName, taxonName));
    }

    @Step("Click taxon bar in product form")
    public void clickTaxonBar_productForm(String taxonomyName, String taxonName){
        click(addedTaxonBar_productForm(taxonomyName, taxonName));
    }
}
