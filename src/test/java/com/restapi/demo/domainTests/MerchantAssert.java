package com.restapi.demo.domainTests;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.test.utils.TestUtils;
import org.assertj.core.api.AbstractAssert;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 */

final class MerchantAssert extends AbstractAssert<MerchantAssert, Merchant> {

    private MerchantAssert(Merchant actual)
    {
        super(actual, Merchant.class);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

    }

    static MerchantAssert assertThatMerchantEntry(Merchant actual)
    {
        return new MerchantAssert(actual);
    }

    MerchantAssert hasName(String expectedName)
    {
        isNotNull();
        //Set<ConstraintViolation<Merchant>> violations
        //        = validator.validate(player)
        //
        String actualName  = actual.getMerchantName();
        assertThat(actualName)
                .overridingErrorMessage(
                        "Expected name to be <%s> but was <%s>.",
                        expectedName, actualName
        ).isEqualTo(expectedName);

        return this;
    }

    MerchantAssert hasId(Integer expectedId)
    {
        isNotNull();

        Integer actualId = actual.getId();
        assertThat(actualId).overridingErrorMessage(
                "Expected id to be <%d> but was <%d>", expectedId, actualId
        ).isEqualTo(expectedId);

        return this;
    }

    MerchantAssert hasNumber(String expectedNumber)
    {
        String actualNumber = actual.getPhonenumber();
        assertThat(actualNumber).overridingErrorMessage(
                "Expected number to be <%s> but was <%s>",
                expectedNumber, actualNumber
        ).isEqualTo(expectedNumber);
        return this;
    }

    MerchantAssert hasNoNumber()
    {
        isNotNull();

        String actualNumber = actual.getPhonenumber();
        assertThat(actualNumber).overridingErrorMessage(
                "Expected phone number to be <null> or empty but was <%s>", actualNumber
        ).isNullOrEmpty(); //Mby this here should be just null ??

        return this;
    }

    MerchantAssert hasCompanyName(String expectedCompanyName)
    {
        String actualCompanyName = actual.getCompanyName();
        assertThat(actualCompanyName).overridingErrorMessage(
                "Expected company name to be <%s> but was <%s>",
                                expectedCompanyName, actualCompanyName
        ).isEqualTo(expectedCompanyName);

        return this;
    }

    MerchantAssert hasNoCompanyName()
    {
        isNotNull();

        String actualCompName = actual.getCompanyName();
        assertThat(actualCompName).overridingErrorMessage(
                "Expected company name to be <null> or empty but was <%s>",
                                actualCompName
        ).isNullOrEmpty(); // Or mby only null ???

        return this;
    }

    String printSet(Set s)
    {

        return (String) s.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    MerchantAssert hasOffersSet(Set expectedOfferSet)
    {

        isNotNull();
        Set actualOfferSet = actual.getOffersSet();

        assertThat(actualOfferSet).overridingErrorMessage(
                "Expected offers set is different from actual offers set \n " +
                        "Expected: <%s> \n Actual: <%s>", printSet(expectedOfferSet), printSet(actualOfferSet)
        ).isEqualTo(expectedOfferSet);

        return this;
    }

    MerchantAssert hasNoOffersSer()
    {
        Set actualOfferSet = actual.getOffersSet();
        assertThat(actualOfferSet).overridingErrorMessage(
                "Expected offer set to be null but was <%s>",
                printSet(actualOfferSet)
        ).isNullOrEmpty();

        return this;
    }

    public MerchantAssert createdAt(String creationTime)
    {
        isNotNull();

        ZonedDateTime expectedCreationTime = TestUtils.dateTimeParser(creationTime);
        ZonedDateTime actualCreationTime = actual.getCreatedAt();

        assertThat(actualCreationTime).overridingErrorMessage(
                "Expected creation time to be <%s> but was <%s>",
                                expectedCreationTime, actualCreationTime
        ).isEqualTo(expectedCreationTime);

        return this;
    }

    public MerchantAssert updatedAt(String expLastUpdatedTime)
    {
        ZonedDateTime expectedLastUpdatedTime = TestUtils.dateTimeParser(expLastUpdatedTime);
        ZonedDateTime actualLastUpdatedTime = actual.getUpdatedAt();

        assertThat(actualLastUpdatedTime).overridingErrorMessage(
                "Expected last update time was <%s> but actual was <%s>",
                                expectedLastUpdatedTime, actualLastUpdatedTime
        ).isEqualTo(expectedLastUpdatedTime);

        return this;
    }

}