package com.restapi.demo.domainTests.merchant;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.domain.Offer;
import com.restapi.demo.testutils.TestObjectsFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;


/**
 * @author G.Nikolov on 11/10/18
 * @project rest-service-basic
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MerchantTest.class)
public class MerchantTest {

    private static Logger logger = LogManager.getLogger(MerchantTest.class);
    private Merchant testObj;
    private static final Long TEST_ID = Long.valueOf("1");

    private static final String  MERCHANT_NAME  = "TEST_USER";
    private static final String  PHONE_NUMBER   = "+44784000000";
    private static final String  COMPANY_NAME   = "TEST_COMPANY";
    private static final String  CREATION_DATE  = "2000-10-07T22:28:39+00:00";
    private static final String  UPDATED_ON     = "2000-10-07T23:00:00+00:00";


    @Before
    public void setUp()
    {
        testObj = TestObjectsFactory.getMerchantInstanceEmptyOfferSet();
    }

    @After
    public void tearDown()
    {
        testObj = null;
    }

    @Test(expected = NullPointerException.class)
    @Ignore
    public void whenNameIsNullShouldThrowException()
    {
        testObj.setMerchantName(null);
    }

    @Test
    public void merchantHasId()
    {
        MerchantAssert.assertThatMerchantEntry(testObj).hasId(TEST_ID);
    }

    @Test
    public void merchantHasName()
    {
        MerchantAssert.assertThatMerchantEntry(testObj).hasName(MERCHANT_NAME);
    }

    @Test
    public void merchantHasCompanyName()
    {
        MerchantAssert.assertThatMerchantEntry(testObj).hasCompanyName(COMPANY_NAME);
    }

    @Test(expected = AssertionError.class)
    public void merchantHasDifferentName()
    {
        MerchantAssert.assertThatMerchantEntry(testObj).hasCompanyName("BAD_NAME");
    }

    @Test
    public void merchantHasPhoneNumber()
    {
        MerchantAssert.assertThatMerchantEntry(testObj).hasNumber(PHONE_NUMBER);
        //logger.info(testObj.toString());
    }

    @Test
    public void merchantHasNoPhoneNumber()
    {
        testObj.setPhonenumber(null);
        MerchantAssert.assertThatMerchantEntry(testObj).hasNoNumber();
    }

    @Test
    public void merchantHasNoCompanyName()
    {
        testObj.setCompanyName(null);
        MerchantAssert.assertThatMerchantEntry(testObj).hasNoCompanyName();
    }

    @Test
    public void merchantWasCreatedOn()
    {
        MerchantAssert.assertThatMerchantEntry(testObj).createdAt(CREATION_DATE);
    }

    @Test
    public void merchantWasUpdatedOn()
    {
        MerchantAssert.assertThatMerchantEntry(testObj).updatedAt(UPDATED_ON);
    }

    @Test
    public void merchantHashOfferSet()
    {
        Set<Offer> offers = new HashSet<>();
        offers.add(TestObjectsFactory.getOfferInstanceWithMerchantAndCustomId(testObj, Long.valueOf(1)));
        testObj.setOffersSet(offers);
        //logger.info(testObj.getOffersSet().toString());
        MerchantAssert.assertThatMerchantEntry(testObj
        ).hasOffersSet(TestObjectsFactory.getAmerchantInstanceWithOfferSet(1));
    }








}
