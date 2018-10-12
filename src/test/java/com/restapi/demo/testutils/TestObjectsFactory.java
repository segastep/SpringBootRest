package com.restapi.demo.testutils;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.domain.Offer;
import com.restapi.demo.enums.OfferState;

import java.util.HashSet;
import java.util.Set;

/**
 * @author G.Nikolov on 11/10/18
 * @project rest-service-basic
 *
 * A factory class to return instance of
 * @see com.restapi.demo.domain.Merchant
 * and
 * @see com.restapi.demo.domain.Offer
 */
public class TestObjectsFactory {
    //Constants used to build Merchant object
    private static final Long TEST_ID = Long.valueOf("1");

    private static final String  MERCHANT_NAME  = "TEST_USER";
    private static final String  PHONE_NUMBER   = "+44784000000";
    private static final String  COMPANY_NAME   = "TEST_COMPANY";
    private static final String  CREATION_DATE  = "2000-10-07T22:28:39+00:00";
    private static final String  UPDATED_ON     = "2000-10-07T23:00:00+00:00";


    //Constants for Offer object which will be put in Merchant's offerSet
    private static final Long OFFER_ID = Long.valueOf("100");

    private static final String  OFFER_CREATED_AT = "2010-10-07T22:28:39+00:00";
    private static final String  OFFER_UPDATED_AT = "2010-11-07T22:28:39+00:00";
    private static final String  OFFER_EXPIRES_AT = "2010-11-08T22:28:39+00:00";
    private static final String  OFFER_DESCRIPT   = "Some test test string";
    private static final String  OFFER_CURRENCY   = "GBP";

    private static final Long    OFFER_PRICE      =  Long.valueOf("123456");

    private static final OfferState OFFER_STATE   = OfferState.ACTIVE;


    public static Offer getOfferInstanceWithOutMerchant()
    {
        return new Offer()
                .setId(OFFER_ID)
                .setCreatedAt(TestUtils.dateTimeParser(OFFER_CREATED_AT))
                .setUpdatedAt(TestUtils.dateTimeParser(OFFER_UPDATED_AT))
                .setValidUntil(TestUtils.dateTimeParser(OFFER_EXPIRES_AT))
                .setPrice(OFFER_PRICE)
                .setCurrency(OFFER_CURRENCY)
                .setDescription(OFFER_DESCRIPT)
                .setOfferState(OFFER_STATE);
    }

    public static Offer getOfferInstanceWithMerchant(Merchant merchant)
    {
        return getOfferInstanceWithOutMerchant().setMerchant(merchant);
    }

    public static Offer getOfferInstanceWithMerchantAndCustomId(Merchant merchant,Long id)
    {
        return getOfferInstanceWithMerchant(merchant).setId(id);
    }

    public static Merchant getMerchantInstanceEmptyOfferSet()
    {
        return new Merchant()
                .setId(TEST_ID)
                .setMerchantName(MERCHANT_NAME)
                .setCompanyName(COMPANY_NAME)
                .setPhonenumber(PHONE_NUMBER)
                .setCreatedAt(TestUtils.dateTimeParser(CREATION_DATE))
                .setUpdatedAt(TestUtils.dateTimeParser(UPDATED_ON));
    }

    public static Merchant getAmerchantInstanceWithOfferSet(int numberOfOffers)
    {

        //Merchant merchantOfferCopy = (Merchant) TestUtils.deepCopy(merchantTestObject);
        Merchant merchantTestObject = getMerchantInstanceEmptyOfferSet();
        Offer offerTestObject = getOfferInstanceWithMerchant(merchantTestObject).setMerchant(merchantTestObject);

        Set<Offer> o = new HashSet<>();

        for(int i = 1; i <= numberOfOffers; i++)
        {
            o.add(offerTestObject.setId(new Long(i)));

        }
        merchantTestObject.setOffersSet(o);

        return merchantTestObject;
    }

}
