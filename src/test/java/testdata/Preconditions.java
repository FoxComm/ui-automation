package testdata;

import ru.yandex.qatools.allure.annotations.Step;
import testdata.api.Helpers;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.sleep;

import static testdata.api.collection.Auth.*;
import static testdata.api.collection.Cart.*;
import static testdata.api.collection.Coupons.*;
import static testdata.api.collection.Customers.*;
import static testdata.api.collection.GiftCards.*;
import static testdata.api.collection.Inventory.*;
import static testdata.api.collection.Orders.*;
import static testdata.api.collection.Products.*;
import static testdata.api.collection.Promotions.*;
import static testdata.api.collection.SharedSearch.*;
import static testdata.api.collection.Skus.*;
import static testdata.api.collection.StoreCredits.*;

public class Preconditions extends Helpers {

    @Step("Fulfill preconditions -- <{0}>")
    protected void provideTestData(String testMethodName) throws IOException {

        skuCodes.clear();
        products.clear();
        System.out.println("==== ==== ==== ====");
        loginAsAdmin();

        switch(testMethodName) {

            //------------------------------------- SEARCH FILTERS ------------------------------------//

            case "a customer":
                createCustomer();
                break;

            //----------------------------------------- CART ITEMS -----------------------------------------//

            case "empty cart and 3 active products":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                break;

            case "cart<1 SKU[active, qty: 1]>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                break;

            case "cart with 3 items":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                createSKU_active();
                createProduct_active(skuCode, "test");
                skuCodes.add(skuCode);
                updLineItems_multiple(cartId, skuCodes.get(0), 4, skuCodes.get(1), 5, skuCodes.get(2), 3);
                break;

            case "cart with 1 item, qty: 3":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 3);
                break;

            //------------------------------------- CART SHIPPING ADDRESS -------------------------------------//

            case "cart with empty address book":
                createCustomer();
                createCart(customerId);
                break;

            case "cart with non-empty address book":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with chosen shipping address":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 2 addresses in address book":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            case "cart with 2 addresses and defined default shipping address":
                createCustomer();
                createCart(customerId);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                break;

            case "filled out cart 2 addresses in address book":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                break;

            case "filled out cart":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 10);
                updLineItems(cartId, skuCode, 3);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                break;


            //----------------------------------- CART COUPONS ------------------------------------//

            case "empty cart":
                createCustomer();
                createCart(customerId);
                break;

//            case "cart<empty>; coupon<any, single code>":
//                createCustomer();
//                createCart(customerId);
//                createPromotion_coupon();
//                createCoupon(promotionId);
//                generateSingleCode(couponId);
//                break;

            case "cart<1 SKU>, coupon<any, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 3);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "cart<1 SKU>; coupon<any, bulk generated codes>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 3);
                createPromotion_coupon();
                createCoupon(promotionId);
                bulkGenerateCodes(couponId, "BULK CPNS ", 5, 3);
                break;

            case "cart<1 SKU, coupon applied>; coupon<any, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 3);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                applyCouponCode(cartId, singleCouponCode);
                break;

//            case "cart<empty, coupon applied>; coupon<any, bulk generated codes>":
//                createCustomer();
//                createCart(customerId);
//                createPromotion_coupon();
//                createCoupon(promotionId);
//                bulkGenerateCodes(couponId, "BLK_NWCPN-", 5, 3);
//                applyCouponCode(cartId, bulkCodes.get(0));
//                break;

            case "cart<1 SKU in stock, shipAddress, shipMethod, coupon applied, payMethod[SC]>; coupon<any, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                increaseOnHandQty(skuCode, "Sellable", 1);
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                createAddress(customerId, "Paul Puga", 4177, 234, "Washington", "2101 Green Valley", "200 Suite", "Seattle", "98101", "5551237575", false);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "9879879876", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "cart<1 SKU>; coupon<items -- no qualifier/percent off, single code>":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                createSharedSearch_singleProduct(productTitle);
                createPromotion_coupon_itemsNoQual(searchId);
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            //------------------------------------- CART PAYMENT METHOD -------------------------------------//

            case "cart with 1 item and chosen shipping address":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            case "cart with 1 item && customer with GC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item && customer with SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                updLineItems(cartId, skuCode, 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method, and credit card payment":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with 1 item, shipping method and issued SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method and issued GC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item, shipping method, credit card payment and issued SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item, shipping method, credit card payment and issued GC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueGiftCard(50000, 1);
                break;

            case "cart with 1 item, shipping method, issued SC and GC":
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit(customerId, 50000);
                break;

            case "cart with 1 item && SC onHold":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "onHold");
                break;

            case "cart with 1 item && SC canceled":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                issueStoreCredit(customerId, 50000);
                updateSCState(scId, "canceled");
                break;

            //----------------------------------- CART VALIDATION -----------------------------------//

            case "cart<filled out, payment method: SC>":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 20);
                createCustomer();
                issueStoreCredit(customerId, 20000);
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                setPayment_storeCredit(cartId, 20000);
                listCustomerAddresses(customerId);
                break;

            case "cart<filled out, payment method: GC>":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 20);
                createCustomer();
                issueGiftCard(20000, 1);
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                setPayment_giftCard(cartId, gcCode, 20000);
                break;

            case "cart<filled out, payment method: CC>":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 20);
                createCustomer();
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "filled out cart, product with variants, SC as a payment method":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 20);
                createCustomer();
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            //------------------------------------- ORDER STATE -------------------------------------//

            case "order in remorse hold":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);
                break;

            case "order in remorse hold payed with SC":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                issueStoreCredit(customerId, 50000);
                setPayment_storeCredit(cartId, 10000);
                checkoutCart(cartId);
                break;

            //--------------------------------- CUSTOMER ADDRESS BOOK ---------------------------------//

            case "customer with a shipping address":
                createCustomer();
                createAddress(customerId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                break;

            //--------------------------------- CUSTOMER CREDIT CARDS ---------------------------------//

            case "customer with a credit card":
                createCustomer();
                createAddress(customerId, customerName, 4177, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", false);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                break;

            case "customer with a credit card and 2 addresses":
                createCustomer();
                createAddress(customerId, customerName, 4164, 234, "New York", "545 Narrow Ave", "Suite 15", "New Jersey", "10201", "5551118888", false);
                createAddress(customerId, customerName, 4177, 234, "Washington", "2101 Green Valley", "Suite 300", "Seattle", "98101", "9879879876", false);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                break;

            //----------------------------------- CUSTOMER'S ORDERS -----------------------------------//

            case "customer with 2 orders in remorse hold":

                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                skuCodes.add(skuCode);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                skuCodes.add(skuCode);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                break;

            case "customer with 2 orders in remorse hold and fulfillment started":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                skuCodes.add(skuCode);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 2);
                updLineItems(cartId, skuCode, 2);
                skuCodes.add(skuCode);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_creditCard(cartId, creditCardId);
                checkoutCart(cartId);

                changeOrderState(orderId, "fulfillmentStarted");
                break;

            //----------------------------------- STORE CREDITS -----------------------------------//

            case "a customer with issued SC":
                createCustomer();
                issueStoreCredit(customerId, 50000);
                break;

            case "a customer && GC":
                createCustomer();
                issueGiftCard(12500, 1);
                break;

            case "order in Remorse Hold, payed with SC (CSR Appeasement)":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueStoreCredit(customerId, 50000);
                getCartTotals(cartId);
                setPayment_storeCredit(cartId, total);
                checkoutCart(cartId);
                break;

            case "order in Remorse Hold, payed with SC (GC Transfer)":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                issueGiftCard(50000, 1);
                issueStoreCredit_gcTransfer(customerId, gcCode);
                getCartTotals(cartId);
                setPayment_storeCredit(cartId, total);
                checkoutCart(cartId);
                break;

            //---------------------------------- PROMOTIONS ---------------------------------//

            case "active promotion with auto apply type":
                createPromotion_auto_active();
                break;

            case "inactive promotion with auto apply type":
                createPromotion_auto_inactive();
                break;

            //----------------------------------- COUPONS -----------------------------------//

            case "a promotion":
                createPromotion_coupon();
                sleep(5000);
                break;

            case "coupon with single code":
                createPromotion_coupon();
                sleep(5000);
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "coupon with bulk generated codes":
                createPromotion_coupon();
                sleep(5000);
                createCoupon(promotionId);
                bulkGenerateCodes(couponId, "BLKNWCPN" + couponId + "-", 4, 5);
                break;


            case "order in remorse hold with applied coupon":
                createPromotion_coupon();
                sleep(5000);
                createCoupon(promotionId);
                generateSingleCode(couponId);
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                applyCouponCode(cartId, singleCouponCode);
                checkoutCart(cartId);
                break;

            //----------------------------------- PRODUCTS -----------------------------------//

            case "product in active state":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                break;

            case "product in inactive state":
                createSKU_active();
                createProduct_inactive(skuCode, "sunglasses");
                break;

            case "active SKU":
                createSKU_active();
                break;

            case "inactive SKU":
                createSKU_inactive();
                break;

            case "SKU with no title":
                createSKU_noTitle();
                break;

            case "SKU with no description":
                createSKU_noDescription();
                break;

            case "SKU with no prices set":
                createSKU_noPrices();
                break;

            case "product in active state, active SKU and tag":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                break;

            //----------------
            case "active product, has tag, active SKU":
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                break;

            case "active product, no tag, active SKU":
                createSKU_active();
                createProduct_active_noTag(skuCode);
                break;
            case "inactive product, has tag, active SKU":
                createSKU_active();
                createProduct_inactive(skuCode, "sunglasses");
                break;

            case "inactive product, no tag, active SKU":
                createSKU_active();
                createProduct_inactive_noTag(skuCode);
                break;

            case "active product, has tag, inactive SKU":
                createSKU_inactive();
                createProduct_active(skuCode, "sunglasses");
                break;

            case "active product, no tag, inactive SKU":
                createSKU_inactive();
                createProduct_active_noTag(skuCode);
                break;

            case "inactive product, has tag, inactive SKU":
                createSKU_inactive();
                createProduct_inactive(skuCode, "sunglasses");
                break;

            case "inactive product, no tag, inactive SKU":
                createSKU_inactive();
                createProduct_inactive_noTag(skuCode);
                break;

            //----------------------------------- INVENTORY -----------------------------------//

            case "active SKU for inventory":
                createSKU_active();
                checkInventoryAvailability(skuCode);
                break;

            case "cart with backorder SKU":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Backorder", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with backorder and sellable SKUs":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Backorder", 1);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 2);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "cart with backorder and sellable items of two SKUs":
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Backorder", 1);
                updLineItems(cartId, skuCode, 1);
                createSKU_active();
                createProduct_active(skuCode, "sunglasses");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            //----------------------------------- GIFT CARDS -----------------------------------//

            case "gift card":
                issueGiftCard(5000, 1);
                break;

            case "used gift card":
                issueGiftCard(20000, 1);
                createCustomer();
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, "test");
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId, "John Doe", 4164, 234, "Oregon", "757 Foggy Crow Isle", "200 Suite", "Portland", "97201", "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                setPayment_giftCard(cartId, gcCode, 10000);
                checkoutCart(cartId);
                break;

            case "gift card on hold":
                issueGiftCard(20000, 1);
                setGiftCardState(gcCode, "onHold");
                break;

            //--------------------------------- SEARCH CONTROLS ---------------------------------//

            case "saved search with 1 filter":
                createSharedSearch_oneFilter();
                break;

            case "saved search with 2 filters":
                createSharedSearch_twoFilters();
                break;

            //------------------------------------- SF: AUTH ------------------------------------//

            case "a customer signed up on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                break;

            case "two customers signed up on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                takenEmail = customerEmail;
                signUpCustomer("Test Buddy " + generateRandomID(), "qatest2278+" + generateRandomID() + "@gmail.com");
                break;

            case "a customer ready to checkout":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;


            //------------------------------------- SF: CART ------------------------------------//

            case "registered customer, active product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, "test");
                createCart(customerId);
                updLineItems(cartId, skuCode, 1);
                break;

            case "registered customer, active product on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                break;

            case "registered customer, 2 active products on storefront":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                products.add(productTitle);
                break;

            case "a customer signed up on storefront, product<active>, coupon<any, single code>":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "product<active>, coupon<any, single code>":
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "a customer signed up on storefront with product and coupon<any, single code> in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                applyCouponCode(cartId, singleCouponCode);
                break;

            case "an active product visible on storefront":
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            //---------------------------------- SF: SHIPPING ADDRESS --------------------------------//

            case "a storefront signed up customer with a shipping address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

            case "a storefront signed up customer with 2 shipping addresses":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                createAddress(customerId,
                        "Paul Puga",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                break;

            case "a storefront signed up customer with a product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                break;

            case "a storefront signed up customer with a shipping address and a product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

            case "a storefront signed up customer with 2 shipping addresses, has default address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", true);
                createAddress(customerId,
                        "Paul Puga",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                break;

            //---------------------------------- SF: CHECKOUT --------------------------------//

            case "a storefront signed up customer, a cart with 1 product, 2 shipping addresses, has default address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                createAddress(customerId,
                        "Default Address",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", true);
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                break;

            case "a storefront signed up customer, a cart with 1 product, 2 shipping addresses, NO default address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                createAddress(customerId,
                        "Paul Puga",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                break;

            case "a storefront signed up customer, a cart with 1 product, 2 shipping addresses, HAS default address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createAddress(customerId,
                        "Default Address",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                break;

            case "a storefront signed up customer with active product in cart and applied shipping address":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

            case "a customer ready to checkout, single code coupon code":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "a storefront customer ready for checkout, has 2 credit cards":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                createCreditCard(customerId, customerName, "4242424242424242", 3, 2020, 123, "Visa");
                break;

            case "a storefront signed up customer, with no qualifier coupon code applied":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                applyCouponCode(cartId, singleCouponCode);
                break;

            case "a storefront signed up customer ready for checkout, has 2 credit cards, has default card":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                createCreditCard(customerId, customerName, "4242424242424242", 3, 2020, 123, "Visa");
                setCardAsDefault(customerId, creditCardId);
                break;

            case "a storefront signed up customer, has shipping address and product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                createAddress(customerId,
                        "John Doe",
                        4177, 234,
                        "Washington", "2101 Green Valley",
                        "200 Suite",
                        "Seattle", "98101",
                        "5551237575", false);
                break;

            case "a storefront signed up customer, with shipping address submitted and product in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                break;

            case "happy path":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                break;

            case "a storefront signed up blacklisted customer ready for checkout":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                blacklistCustomer(customerId);
                break;

            case "a customer ready for checkout, gift card is applied to cart as a payment method":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueGiftCard(1000, 1);
                setPayment_giftCard(cartId, gcCode, 1000);
                break;

            case "a customer ready to checkout, a gift card issued":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                issueGiftCard(1000, 1);
                break;

            case "a customer ready to checkout, 2 active products, 1 in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                increaseOnHandQty(skuCode, "Sellable", 1);
                updLineItems(cartId, skuCode, 1);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                setShipAddress(cartId,
                        "John Doe",
                        4177, 234,
                        "Washington", "7500 Roosevelt Way NE",
                        "Block 42",
                        "Seattle", "98115",
                        "5038234000", false);
                listShipMethods(cartId);
                setShipMethod(cartId, shipMethodId);
                listCustomerAddresses(customerId);
                getCustomerAddress(customerId, addressId1);
                createCreditCard(customerId, customerName, "5555555555554444", 3, 2020, 123, "MasterCard");
                setPayment_creditCard(cartId, creditCardId);
                break;

            case "a storefront registered customer, 2 active products, 1 in cart":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                updLineItems(cartId, skuCode, 1);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                break;

            case "an active product, a gift card":
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                issueGiftCard(1000, 1);
                break;

            case "a storefront registered customer, 2 active products, 1 in cart, coupon<no qualifier, 10% off, single code>":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createCart(customerId);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                updLineItems(cartId, skuCode, 1);
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                skuCodes.add(skuCode);
                products.add(productTitle);
                createPromotion_coupon();
                createCoupon(promotionId);
                generateSingleCode(couponId);
                break;

            case "a storefront registered customer, an active product":
                randomId = generateRandomID();
                signUpCustomer("Test Buddy " + randomId, "qatest2278+" + randomId + "@gmail.com");
                createSKU_active();
                createProduct_active(skuCode, storefrontCategory);
                break;

            //------------------------------------- SF: PDP ------------------------------------//

            case "active product with tags <ENTRÉES> and <POULTRY>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "POULTRY");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "active product with tags <ENTRÉES> and <SEAFOOD>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "SEAFOOD");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "active product with tags <ENTRÉES> and <MEAT>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "MEAT");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "active product with tags <ENTRÉES> and <VEGETARIAN>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "VEGETARIAN");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "active product with tags <ENTRÉES> and <>":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "products with tags with entrees subcategories names":
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "POULTRY");
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "SEAFOOD");
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "MEAT");
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                addTag_product(productId, "VEGETARIAN");
                products.add(productTitle);
                createSKU_active();
                createProduct_active(skuCode, "ENTRÉES");
                products.add(productTitle);
                createSKU_active();
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with tpg-specific custom properties":
                createSKU_active();
                createProduct_tpgProps(skuId, skuCode, skuTitle);
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <p> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "p", "Paragraph");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h1> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h1", "Heading One");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h2> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h2", "Heading Two");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h3> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h3", "Heading Three");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h4> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h4", "Heading Four");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h5> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h5", "Heading Five");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <h6> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "h6", "Heading Six");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <strong> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "strong", "Bold Text");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <em> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "em", "Italic Text");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <ins> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "ins", "Underlined Text");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <ul> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "ul", "UL Bullet Point");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;

            case "an active product with <ol> in description":
                randomId = generateRandomID();
                createSKU_active();
                createActiveProduct_styledDescription(skuCode, storefrontCategory, "ol", "OL Point");
                checkProductPresenceInCategoryView("int", "productId", productId);
                break;
        }
    }

}


// 4177 - washington
// 4161 - ny
// 4164 - oregon