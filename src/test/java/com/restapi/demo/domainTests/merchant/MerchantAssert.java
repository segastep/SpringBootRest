package com.restapi.demo.domainTests.merchant;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.testutils.TestUtils;
import org.assertj.core.api.AbstractAssert;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 * Api like class to ease writing Merchant assertions
 */

final class MerchantAssert extends AbstractAssert<MerchantAssert, Merchant> {

    private MerchantAssert(Merchant actual)
    {
        super(actual, MerchantAssert.class);

    }

    public static MerchantAssert assertThatMerchantEntry(Merchant actual)
    {
        return new MerchantAssert(actual);
    }

    MerchantAssert hasName(String expectedName)
    {
        isNotNull();

        String actualName  = actual.getMerchantName();
        assertThat(actualName)
                .overridingErrorMessage(
                        "Expected name to be <%s> but was <%s>.",
                        expectedName, actualName
        ).isEqualTo(expectedName);

        return this;
    }

    MerchantAssert hasId(Long expectedId)
    {
        isNotNull();

        Long actualId = actual.getId();
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

    MerchantAssert hasOffersSet(Merchant expectedOfferSet)
    {

        isNotNull();

       assertThat(actual.getOffersSet()).overridingErrorMessage(
               "Expected offers set is different from actual offers set \n " +
                       "Expected: <%s> \n Actual: <%s>", printSet(expectedOfferSet.getOffersSet()), printSet(actual.getOffersSet())
       ).isEqualTo(expectedOfferSet.getOffersSet());
        //assertEquals(expectedOfferSet.getOffersSet(),actual.getOffersSet());
        return this;
    }

    MerchantAssert hasNoOffersSet()
    {
        Set actualOfferSet = actual.getOffersSet();
        assertThat(actualOfferSet).overridingErrorMessage(
                "Expected offer set to be null but was <%s>",
                printSet(actualOfferSet)
        ).isNullOrEmpty();

        return this;
    }

     MerchantAssert createdAt(String creationTime)
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