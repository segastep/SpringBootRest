package com.restapi.demo.domainTests.offer;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.domain.Offer;
import com.restapi.demo.testutils.TestObjectsFactory;
import com.restapi.demo.testutils.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * @author G.Nikolov on 12/10/18
 * @project rest-service-basic
 *
 * Entity validation tests for {@link com.restapi.demo.domain.Offer}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OfferEntityValidationTest.class)
public class OfferEntityValidationTest {

    private static final int DESCRIPTION_MIN_SIZE = 10;
    private static ValidatorFactory validatorFactory;
    private static Set<ConstraintViolation<Offer>> violations;
    private static Validator validator;


    @Before
    public void setUp()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

    }


    private void getViolationObj(Offer testObj)
    {
        violations = validator.validate(testObj);
        validator.getConstraintsForClass(Offer.class);
    }

    private String getViolationMessage()
    {
        return violations.iterator().next().getMessage();
    }

    @After
    public void close() {
        violations = null;
        validator = null;
        validatorFactory.close();
    }

    @Test
    public void shouldHaveNoViolations() {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant());
        assertTrue(violations.isEmpty());
    }

    @Test
    public void nullIDconstraintViolation()
    {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant()
        .setId(null));
        assertEquals(1, violations.size());
        assertEquals("Id cannot be null", getViolationMessage());
    }

    @Test
    public void shouldHaveDescriptionNullViolation()
    {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant()
        .setDescription(null));
        assertEquals(1, violations.size());
        assertEquals("Description cannot be null",getViolationMessage());
    }

    @Test
    public void shouldHaveDescriptionLenghtConstraingViolation()
    {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant()
        .setDescription("ab"));
        assertEquals(1, violations.size());
        assertEquals(String.format("Description should be at least %d characters long"
                ,DESCRIPTION_MIN_SIZE),
                getViolationMessage());
    }

    @Test
    public void shouldHavePriceViolation()
    {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant().setPrice(null));
        assertEquals(1, violations.size());
        assertEquals("Price cannot be null", getViolationMessage());
    }

    @Test
    public void shouldHaveCurrencyViolation()
    {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant().setCurrency(null));
        assertEquals(1, violations.size());
        assertEquals("Currency type cannot be null", getViolationMessage());
    }

    @Test
    public void shouldHaveCurrencyLengthViolation()
    {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant().setCurrency("GB"));
        assertEquals(1, violations.size());
        assertEquals("Currency type must be of fixed length 3", getViolationMessage());
    }

    @Test
    public void shouldHaveValidUntilViolation()
    {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant().setValidUntil(null));
        assertEquals(1, violations.size());
        assertEquals("Valid until field cannot be null", getViolationMessage());
    }

    @Test
    public void shouldHaveOfferStateViolation()
    {
        getViolationObj(TestObjectsFactory.getOfferInstanceWithOutMerchant().setOfferState(null));
        assertEquals(1, violations.size());
        assertEquals("Offer state cannot be null !", getViolationMessage());
    }
}
