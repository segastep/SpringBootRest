package com.restapi.demo.domainTests.merchant;

import com.restapi.demo.domain.AuditModel;
import com.restapi.demo.domain.Merchant;

import com.restapi.demo.testutils.TestObjectsFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MerchantEntityValidationTest.class)
public class MerchantEntityValidationTest {

    /**
     * Also tests the super class of {@link com.restapi.demo.domain.Merchant}
     * which is {@link AuditModel}
     *<p>
     *     Note: Not fully implemented, individual field constrains can be tested as described in
     *     https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/
     *     but I didn't see a point doing this since if anything is violated it will
     *     result in exception which can be easily tracked
     *</p>
     *
     */


    //https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/?v=6.0#_requesting_groups
    private Logger log = LogManager.getLogger(this.getClass());
    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

    }

    @After
    public void close() {
        validatorFactory.close();
    }

    @Test
    public void shouldHaveNoViolations() {
        Set<ConstraintViolation<Merchant>> violations
                = validator.validate(TestObjectsFactory.getMerchantInstanceEmptyOfferSet());
        validator.getConstraintsForClass(Merchant.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldHaveMerchantNameViolation()
    {
        Set<ConstraintViolation<Merchant>> violations
                = validator.validate(TestObjectsFactory
                .getAmerchantInstanceWithOfferSet(1)
                .setMerchantName(""));
        validator.getConstraintsForClass(Merchant.class);
        //log.info(violations.iterator().next().getPropertyPath());
        assertEquals( 1, violations.size() );
        assertEquals("size must be between 3 and 120",violations.iterator().next().getMessage() );
    }

    @Test
    //If @Validate bean is removed from merchant one of
    // the tests in MerchanTest class will fail, but this one should cover for it
    public void nullMerchantNameViolation()
    {
        Set<ConstraintViolation<Merchant>> violations
                = validator.validate(TestObjectsFactory
                .getAmerchantInstanceWithOfferSet(1)
                .setMerchantName(null));
        validator.getConstraintsForClass(Merchant.class);
        //log.info(violations.iterator().next().getPropertyPath());
        assertEquals( 1, violations.size() );
        assertEquals("must not be null",violations.iterator().next().getMessage() );
    }
}
