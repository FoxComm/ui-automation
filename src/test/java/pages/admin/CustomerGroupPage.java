package pages.admin;

import base.AdminBasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.By.xpath;

public class CustomerGroupPage extends AdminBasePage {

    //-------------    General       ---------------

    public SelenideElement addCGroupBtn() {
        return $(xpath("//span[text()='Group']/.."));
    }

    public SelenideElement newCGroupName() {
        return $(xpath("//*[@id='nameField'][contains(@class,'fc-customer-group-edit')]"));
    }

    public SelenideElement saveCGroupBtn() {
        return $(xpath("//span[text()='Save Group']/.."));
    }

    public SelenideElement editCGroupBtn(){
        return $(xpath("//*[contains(@class,'group__title')]//span[contains(text(),'Edit Group')]"));
    }

    public SelenideElement groupName_form(){
        return $(xpath("//*[contains(@class,'fc-title')]"));
    }

    public SelenideElement groupType_form(){
        return $(xpath("//*[contains(@class,'fc-customer-group')]//*[contains(text(),'Type')]/following-sibling::span"));
    }

    public SelenideElement groupCounter_form(){
        return $(xpath("//*[contains(@class,'group__title')]//*[contains(@id,'counter-value')]"));
    }

    public SelenideElement name_searchView(String groupName){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + groupName+ "')]"));
    }

    public SelenideElement groupType_searchView(String groupName){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + groupName+ "')]" +
                "/../*[contains(@class,'groupType')]/div/div"));
    }

    public SelenideElement groupCounter_searchView(String groupName){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + groupName+ "')]" +
                "/../*[contains(@class,'customersCount')]"));
    }

    public SelenideElement customerName_customerGroupView(String customerName){
        return $(xpath(""));
    }

    public SelenideElement customersNumber_customerGroupView(){
        return $(xpath(""));
    }

    public SelenideElement archiveCGBtn(){
        return $(xpath("//button[contains(@id,'archive-btn')]"));
    }

    public SelenideElement confirmArchiveCGBtn() {
        return $(xpath("//*[contains(@class,'fc-save-cancel')]//*[contains(text(),'Archive Group')]"));
    }

    @Step("Click \"Add Group\" btn")
    public void clickAddCGroupBtn() {
        click(addCGroupBtn());
    }

    @Step("Set \"Group Name\" to <{0}>")
    public void setCGroupName(String name){
        setFieldVal(newCGroupName(), name);
    }

    @Step("Click \"Save Group\" btn")
    public void clickSaveCGroupBtn() {
        click(saveCGroupBtn());
    }

    @Step("Click \"Edit Group\" btn")
    public void clickEditCGroupBtn() {
        click(editCGroupBtn());
    }

    @Step("Click \"Archive Group\" btn")
    public void clickArchiveGroupBtn(){
        click(archiveCGBtn());
    }

    @Step("Click confirmation for \"Archive Group\"")
    public void clickConfirmArchiveGroupBtn(){
        click(confirmArchiveCGBtn());
    }

    //-------------    Manual Groups ---------------

    public SelenideElement mCGBtn() {
        return $(xpath("(//*[contains(@class,'icon-customer')]/..)[2]"));
    }

    public SelenideElement addCustomersBtn() {
        return $(xpath("//span[contains(text(),'Add Customers')]/.."));
    }

    public SelenideElement customerSearchFld(){
        return $(xpath("//*[contains(@class,'fc-form-field')]//input"));
    }

    public SelenideElement customerNameSearchResultFld(String customerName){
        return $(xpath("//div[contains(@class,'item-name') and contains(text(),'" + customerName + "')]"));
    }

    public SelenideElement customerEmailSearchResultFld(String customerEmail){
        return $(xpath("//div[contains(@class,'item-email') and contains(text(),'" + customerEmail + "')]"));
    }

    public SelenideElement customerSearchNoResultsFld(){
        return $(xpath("//div[contains(@class,'items') and contains(text(),'No results found')]"));
    }

    public SelenideElement saveCustomerBtn(){
        return $(xpath("//*[contains(@class,'fc-save-cancel')]//span[contains(text(),'Save')]"));
    }

    public ElementsCollection groupCustomersCollection(){
        return $$(xpath("//*[contains(@class,'fc-table-body')]//*[contains(@class,'fc-table-tr')]"));
    }

    public SelenideElement searchViewElementChkbx(String index){
        return $(xpath("(//*[contains(@class,'selectColumn')]//label)[" + index + "]"));
    }

    public SelenideElement actionsMenu_searchView(){
        return $(xpath("//*[contains(@class,'fc-table-actions')]//*[contains(@class,'chevron-down')]"));
    }

    public SelenideElement deleteFromTheGroupBtn(){
        return $(xpath("//*[contains(@class,'fc-table-actions')]//*[contains(text(),'Delete From Group')]"));
    }

    public SelenideElement confirmDeleteCustomerBtn(){
        return $(xpath("//*[contains(@class,'fc-save-cancel')]//*[contains(@id,'modal-confirm-btn')]"));
    }

    public SelenideElement removingMessageFld(){
        return $(xpath("//*[contains(@class,'notification__message')]"));
    }

    @Step("Click \"Manual Group\" btn")
    public void clickMCGroupBtn() {
        click(mCGBtn());
    }

    @Step("Click \"Add Customers\" btn")
    public void clickAddCustomersBtn(){
        click(addCustomersBtn());
    }

    @Step("Enter Customer name: <{0}>")
    public void setCustomerSearchFld(String customerName){
        setFieldVal(customerSearchFld(), customerName);
    }

    @Step("Click found customer: <{0}>")
    public void clickFoundCustomerFld(String customerName){
        click(customerNameSearchResultFld(customerName));
    }

    @Step("Click save customer button")
    public void clickSaveCustomerBtn(){
        click(saveCustomerBtn());
    }

    @Step("Select customer with index <{0}>")
    public void clickSearchViewElementChkbx(String index){
        click(searchViewElementChkbx(index));
    }

    @Step("Open \"Actions\" menu")
    public void openActionsMenu_searchView(){
        click(actionsMenu_searchView());
    }

    @Step("Click \"Delete from the group\" btn")
    public void clickDeleteFromGroupBtn(){
        click(deleteFromTheGroupBtn());
    }

    @Step("Click Delete confirmation button")
    public void clickDeleteConfirmBtn(){
        click(confirmDeleteCustomerBtn());
    }

    //-------------    Dynamic Groups ---------------

    public SelenideElement dCGroupBtn() {
        return $(xpath("(//*[contains(@class,'icon-customer')]/..)[1]"));
    }

    public SelenideElement addGroupCriteriaBtn() {
        return $(xpath("//*[contains(@class,'add-criterion')]/button"));
    }

    public SelenideElement setGroupCriteriaDd(String index) {
        return $(xpath("(//*[contains(@class, 'group-builder')]//*[contains(@class, 'field')]//*[contains(@class, 'chevron-down')])["
                + index + "]"));
    }

    public SelenideElement setGroupCriteriaOperatorDd(String index) {
        return $(xpath("(//*[contains(@class, 'group-builder')]//*[contains(@class, 'operator')]//*[contains(@class, 'chevron-down')])["
                + index + "]"));
    }

    public SelenideElement setGroupCriteriaValueFld(String index) {
        return $(xpath("(//*[contains(@class, 'group-builder')]//input[contains(@class, 'value')])[" + index + "]"));
    }

    public SelenideElement criteriaMatcherDd(){
        return $(xpath("//*[contains(@class,'match-dropdown')]//*[contains(@class,'icon-chevron-down')]"));
    }

    public SelenideElement criteriaMatcherVal(){
        return $(xpath("//*[contains(@class,'group__main')]/span"));
    }

    public SelenideElement groupCriteriaVal(String index) {
        return $(xpath("(//*[contains(@class, 'group__criterion')]//*[contains(@class, 'value')])[" + index + "]"));
    }

    public SelenideElement removeCriteriaBtn(String index){
        return $(xpath("(//*[contains(@class,'remove-criterion')])[" + index + "]"));
    }

    public ElementsCollection groupCriteriasCollection(){
        return $$(xpath("//*[contains(@class,'fc-grid')]"));
    }

    @Step("Click \"Dynamic Group\" btn")
    public void clickDCGroupBtn() {
        click(dCGroupBtn());
    }

    @Step("Click \"Add criteria\" btn")
    public void clickAddCriteriaBtn() {
        click(addGroupCriteriaBtn());
    }

    @Step("Click <{0}>-th \"Remove criteria\"btn")
    public void clickRemoveCriteriaBtn(String index) {
        click(removeCriteriaBtn(index));
    }

    @Step("Select criteriaField as <{0}>, operator as <{1}>, set value as <{2}> ")
    public void setCriteria(String index, String criteriaField, String criteriaOperator, String criteriaValue) {
        setDdVal_li(setGroupCriteriaDd(index), criteriaField);
        setDdVal_li(setGroupCriteriaOperatorDd(index), criteriaOperator);
        setFieldVal(setGroupCriteriaValueFld(index), criteriaValue);
    }

    @Step("Select <{0}> group matching option")
    public void setCriteriaMatcher(String option) {
        setDdVal_li(criteriaMatcherDd(), option);
    }
}