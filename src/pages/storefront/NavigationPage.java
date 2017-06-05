package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class NavigationPage extends CheckoutPage {

    //-------------------------------------------- ELEMENTS --------------------------------------------

    private SelenideElement categoryTitle(String category) {
        return $(xpath("//div[contains(@class, 'navigation')]//a[text()='" + category + "']"));
    }

    private SelenideElement subCategoryTitle(String subCategory) {
        return $(xpath("//div[contains(@class, '_item_') and text()='" + subCategory + "']"));
    }

    private SelenideElement categoryRedirectBtn(String btnTitle, String btnRedirectSlug) {
        return $(xpath("//div[contains(@class, 'header')]//a[text()='" + btnTitle + "' and @href='" + btnRedirectSlug + "']"));
    }

    //---------------------------------------------- STEPS ----------------------------------------------

    @Step("Navigate to category <{0}>")
    public void navigateToCategory(String category) {
        click(categoryTitle(category));
    }

    @Step("Navigate to sub-category <{0}>")
    public void navigateToSubCategory(String subCategory) {
        click(subCategoryTitle(subCategory));
    }

    @Step("Click {0}[{1}] btn at the home page")
    public void clickBtnAtHome(String btnTitle, String btnRedirectSlug) {
        click(categoryRedirectBtn(btnTitle, btnRedirectSlug));
    }

}