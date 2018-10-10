package com.restapi.demo.domainTests;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.domain.Offer;
import com.restapi.demo.domain.TimestampModel;
import com.restapi.demo.enums.OfferState;
import com.restapi.demo.test.utils.TestUtils;
import org.apache.commons.logging.Log;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.SQLOutput;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 */
@RunWith(JUnit4.class) //TODO Change me to Spring runner
public class MerchantEntityValidationTest {

    /**
     * Also tests the super class of {@link com.restapi.demo.domain.Merchant}
     * which is {@link TimestampModel}
     */

    //Constants used to build Merchant object
    private static final Integer TEST_ID = 1;

    private static final String  MERCHANT_NAME  = "TEST_USER";
    private static final String  PHONE_NUMBER   = "+44784000000";
    private static final String  COMPANY_NAME   = "TEST_COMPANY";
    private static final String  CREATION_DATE  = "2000-10-07T22:28:39+00:00";
    private static final String  UPDATED_ON     = "2000-10-07T23:00:00+00:00";


    //Constants for Offer object which will be put in Merchant's offerSet
    private static final Integer OFFER_ID = 100;

    private static final String  OFFER_CREATED_AT = "2010-10-07T22:28:39+00:00";
    private static final String  OFFER_UPDATED_AT = "2010-11-07T22:28:39+00:00";
    private static final String  OFFER_EXPIRES_AT = "2010-11-08T22:28:39+00:00";
    private static final String  OFFER_DESCRIPT   = "Some test test string";
    private static final String  OFFER_CURRENCY   = "GBP";

    private static final Long    OFFER_PRICE      =  Long.valueOf("123456");

    private static final OfferState OFFER_STATE   = OfferState.ACTIVE;


    private static Merchant merchantTestObject;


    private static Set<Offer> offerSet;
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeTest
    public static void setUp()
    {
        //Set up test objects
        merchantTestObject = new Merchant()
                            .setId(TEST_ID)
                            .setMerchantName(MERCHANT_NAME)
                            .setCompanyName(COMPANY_NAME)
                            .setPhonenumber(PHONE_NUMBER)
                            .setCreatedAt(TestUtils.dateTimeParser(CREATION_DATE))
                            .setUpdatedAt(TestUtils.dateTimeParser(UPDATED_ON));

        //Must use a deep copy here
        Merchant merchantOfferCopy = (Merchant) TestUtils.deepCopy(merchantTestObject);

        Offer offerTestObject = new Offer()
                              .setId(OFFER_ID)
                              .setCreatedAt(TestUtils.dateTimeParser(OFFER_CREATED_AT))
                              .setUpdatedAt(TestUtils.dateTimeParser(OFFER_UPDATED_AT))
                              .setValidUntil(TestUtils.dateTimeParser(OFFER_EXPIRES_AT))
                              .setOfferState(OFFER_STATE)
                              .setMerchant(merchantOfferCopy);

        //Create offers set and add it to merchantTestObj
        offerSet = new HashSet<>();
        offerSet.add(offerTestObject);
        merchantTestObject.setOffersSet(offerSet);



    }
    @BeforeTest
    public static void createValidator() {

        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

    }

    @AfterTest
    public static void close() {
        validatorFactory.close();
        merchantTestObject = null;
        offerSet = null;


    }
    @Test
    public void shouldHaveNoViolations() {

        //This will all go to scrap and MerchantAssert will be modified to be initialised with
        // validator object and all tests in MerchantAssert will be using the validator api
        // and then will be executed in this test suite here

        offerSet = new HashSet<>();
        //offerSet.add(


        Merchant copyMerchantObject = testMerchantObject;

        Offer o = Offer.getOfferBuilder()
                .getOfferFromBuilder(TEST_OFFER) //Interpolate record from initial object
                .merchant().build();
        offerSet.add(o);

        testMerchantObject = Merchant.getBuilder()
                .id(TEST_ID)
                .merchantName(MERCHANT_NAME)
                .phoneNumber(PHONE_NUMBER)
                .companyName(COMPANY_NAME)
                .createdAt(TestUtils.dateTimeParser(CREATION_DATE))
                .updatedAt(TestUtils.dateTimeParser(UPDATED_ON_DATE))
                .offerSet(offerSet).build();

        System.out.println(o.toString());

        //Merchant merchant = Merchant.getBuilder()
        //        .merchantName("TEST")
        //        .id(1)
        //        .offerSet(null)
        //        .phoneNumber("+4467890")
        //        .companyName("TEST_COMPANY")
        //        .createdAt(TestUtils.dateTimeParser("2000-10-07T22:28:39+00:00"))
        //        .updatedAt(TestUtils.dateTimeParser("2000-10-08T22:28:39+00:00"))
        //        .build();

        //System.out.println(Offer.getOfferBuilder()
         //       .getMerchantFromBuilder(TEST_OFFER) //Interpolate record from initial object
         //       .merchant(testMerchantObject).build().toString());

        Set<ConstraintViolation<Merchant>> violations
                = validator.validate(testMerchantObject);

        validator.getConstraintsForClass(Merchant.class);

        assertTrue(violations.isEmpty());
    }
}
