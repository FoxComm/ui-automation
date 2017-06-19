package pages.admin;

import base.AdminBasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;
import tests.storefront.StripeTest;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.openqa.selenium.By.xpath;

public class CustomerGroupPage extends AdminBasePage {


    //-------------    General       ---------------
    public SelenideElement addCustomerGroupBtn() {
        return $(xpath("//span[text()='Group']/.."));
    }

    public SelenideElement newCustomerGroupName() {
        return $(xpath("//*[@id='nameField'][contains(@class,'fc-customer-group-edit')]"));
    }

    public SelenideElement saveGroupBtn() {
        return $(xpath("//span[text()='Save Group']/.."));
    }

    public SelenideElement groupNameInFormFld(){
        return $(xpath("//*[contains(@class,'fc-title')]"));
    }

    public SelenideElement groupTypeInFormFld(){
        return $(xpath("//*[contains(@class,'fc-customer-group')]//*[contains(text(),'Type')]/following-sibling::span"));
    }

    public SelenideElement groupNameInSearchView(String groupName){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + groupName+ "')]"));
    }

    public SelenideElement groupTypeInSearchView(String groupName){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + groupName+ "')]" +
                "/../*[contains(@class,'groupType')]/div/div"));
    }

    public SelenideElement groupCounterInSearchView(String groupName){
        return $(xpath("//*[contains(@class,'fc-table-td') and contains(text(),'" + groupName+ "')]" +
                "/../*[contains(@class,'customersCount')]"));
    }

    public SelenideElement editGroupBtn(){
        return $(xpath("//*[contains(@class,'group__title')]//span[contains(text(),'Edit Group')]"));
    }

    public SelenideElement groupCounterInForm(){
        return $(xpath("//*[contains(@class,'group__title')]//*[contains(@id,'counter-value')]"));
    }

    @Step("Click \"Add Group\" btn")
    public void clickAddGroupBtn() {
        click(addCustomerGroupBtn());
    }

    @Step("Set \"Group Name\" to <{0}>")
    public void setCustomerGroupName(String name){
        setFieldVal(newCustomerGroupName(), name);
    }

    @Step("Click \"Save Group\" btn")
    public void clickSaveGroupBtn() {
        click(saveGroupBtn());
    }

    @Step("Click \"Edit Group\" btn")
    public void clickEditGroupBtn() {
        click(editGroupBtn());
    }

    //-------------    Manual Groups ---------------
    public SelenideElement manualGroupBtn() {
        return $(xpath("(//*[contains(@class,'icon-customer')]/..)[2]"));
    }

    @Step("Click \"Manual Group\" btn")
    public void clickMGroupBtn() {
        click(manualGroupBtn());
    }

    //-------------    Dynamic Groups ---------------
    public SelenideElement dynamicGroupBtn() {
        return $(xpath("(//*[contains(@class,'icon-customer')]/..)[1]"));
    }

    public SelenideElement addGroupCriteriaBtn() {
        return $(xpath("//*[contains(@class,'add-criterion')]/button"));
    }

    public SelenideElement selectGroupCriteriaField(String index) {
        return $(xpath("(//*[contains(@class, 'group-builder')]//*[contains(@class, 'field')]//*[contains(@class, 'chevron-down')])["
                + index + "]"));
    }

    public SelenideElement selectGroupCriteriaOperator(String index) {
        return $(xpath("(//*[contains(@class, 'group-builder')]//*[contains(@class, 'operator')]//*[contains(@class, 'chevron-down')])["
                + index + "]"));
    }

    public SelenideElement setGroupCriteriaValue(String index) {
        return $(xpath("(//*[contains(@class, 'group-builder')]//input[contains(@class, 'value')])[" + index + "]"));
    }

    public SelenideElement chooseAllOrAnyForCriterias(){
        return $(xpath("//*[contains(@class,'match-dropdown')]//*[contains(@class,'icon-chevron-down')]"));
    }

    public SelenideElement getMatchGroupOption(){
        return $(xpath("//*[contains(@class,'group__main')]/span"));
    }

    public SelenideElement getGroupCriteriaValue(String index) {
        return $(xpath("(//*[contains(@class, 'group__criterion')]//*[contains(@class, 'value')])[" + index + "]"));
    }

    public SelenideElement removeCriteriaBtn(String index){
        return $(xpath("(//*[contains(@class,'remove-criterion')])[" + index + "]"));
    }

    public ElementsCollection groupCriteriasCollection(){
        return $$(xpath("//*[contains(@class,'fc-grid')]"));
    }

    @Step("Click \"Dynamic Group\" btn")
    public void clickDGroupBtn() {
        click(dynamicGroupBtn());
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
    public void selectCriteria(String index, String criteriaField, String criteriaOperator, String criteriaValue) {
        setDdVal_li(selectGroupCriteriaField(index), criteriaField);
        setDdVal_li(selectGroupCriteriaOperator(index), criteriaOperator);
        setFieldVal(setGroupCriteriaValue(index), criteriaValue);
    }

    @Step("Select <{0}> group matching option")
    public void selectMatchingOption(String option) {
        setDdVal_li(chooseAllOrAnyForCriterias(), option);
    }
}
