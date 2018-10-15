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
    public static final Long TEST_ID = Long.valueOf("1");

    public static final String  MERCHANT_NAME  = "TEST_USER";
    public static final String  PHONE_NUMBER   = "+44784000000";
    public static final String  COMPANY_NAME   = "TEST_COMPANY";
    public static final String  CREATION_DATE  = "2000-10-07T22:28:39+00:00";
    public static final String  UPDATED_ON     = "2000-10-07T23:00:00+00:00";


    //Constants for Offer object which will be put in Merchant's offerSet
    public static final Long OFFER_ID = Long.valueOf("100");

    public static final String  OFFER_CREATED_AT = "2010-10-07T22:28:39+00:00";
    public static final String  OFFER_UPDATED_AT = "2010-11-07T22:28:39+00:00";
    public static final String  OFFER_EXPIRES_AT = "2010-11-08T22:28:39+00:00";
    public static final String  OFFER_DESCRIPT   = "Some test test string";
    public static final String  OFFER_CURRENCY   = "GBP";

    public static final Long    OFFER_PRICE      =  Long.valueOf("123456");

    public static final OfferState OFFER_STATE   = OfferState.ACTIVE;


    public static Offer getOfferInstanceWithOutMerchant()
    {
        Offer offer = new Offer()
                .setId(OFFER_ID)
                .setValidUntil(TestUtils.dateTimeParser(OFFER_EXPIRES_AT))
                .setPrice(OFFER_PRICE)
                .setCurrency(OFFER_CURRENCY)
                .setDescription(OFFER_DESCRIPT)
                .setOfferState(OFFER_STATE);
        offer.setCreatedAt(TestUtils.dateTimeParser(OFFER_CREATED_AT).toInstant());
        offer.setUpdatedAt(TestUtils.dateTimeParser(OFFER_UPDATED_AT).toInstant());
        return offer;

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
        Merchant m = new Merchant()
                .setId(TEST_ID)
                .setMerchantName(MERCHANT_NAME)
                .setCompanyName(COMPANY_NAME)
                .setPhonenumber(PHONE_NUMBER);
                m.setCreatedAt(TestUtils.dateTimeParser(CREATION_DATE).toInstant());
                m.setUpdatedAt(TestUtils.dateTimeParser(UPDATED_ON).toInstant());

                return m;
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
