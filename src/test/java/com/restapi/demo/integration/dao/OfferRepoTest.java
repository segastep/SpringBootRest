package com.restapi.demo.integration.dao;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.domain.Offer;
import com.restapi.demo.enums.OfferState;
import com.restapi.demo.repositories.MerchantRepo;
import com.restapi.demo.repositories.OfferRepo;
import com.restapi.demo.testutils.TestObjectsFactory;

import com.restapi.demo.testutils.TestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static org.junit.Assert.*;
/**
 * @author G.Nikolov on 14/10/18
 * @project rest-service-basic
 */
@SpringBootTest
@EnableJpaRepositories(basePackages = "com.restapi.demo.repositories")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ComponentScan(basePackages = {"com.restapi.demo"})
@RunWith(SpringRunner.class)
public class OfferRepoTest {

    private static Logger logger = LogManager.getLogger(OfferRepoTest.class);
    private Merchant merchantObj;
    private Offer offerObj;
    @Autowired
    OfferRepo offerRepo;

    @Autowired
    MerchantRepo merchantRepo;

    @Before
    public void setUp()
    {
        merchantObj = TestObjectsFactory.getMerchantInstance();
        offerObj = TestObjectsFactory.getOfferInstanceWithOutMerchant();
        merchantRepo.save(merchantObj);

    }

    @Test
    public void testSave()
    {
        Offer o = new Offer().setId(null).setValidUntil(
                TestUtils.dateTimeParser(TestObjectsFactory.OFFER_EXPIRES_AT)
                )
                .setMerchant(merchantObj)
                .setCurrency("GBP")
                .setDescription("Awesome description")
                .setPrice(new Long(100))
                .setOfferState(OfferState.ACTIVE);

        Offer persisted = offerRepo.save(o);
        assertTrue(persisted.getId() > 0);
    }

    @Test
    public void auditingTest()
    {
        assertNotNull(
                offerRepo.save(
                        TestObjectsFactory.getOfferInstanceWithMerchant(merchantObj)
                ).getCreatedAt()
        );
    }

    public void persistNumberOfOffers(int number)
    {
        //Offers are auto persisted by merchant entity
        for(int i = 1; i <= number; i++)
        {

            Offer o = TestObjectsFactory
                    .getOfferInstanceWithMerchantAndCustomId
                            (merchantObj, Long.valueOf(i));

            offerRepo.save(o);
        }
    }
    @Test
    public void testFindAllOffers()
    {

        persistNumberOfOffers(10);
        assertEquals(10,offerRepo.findAll().size());
    }

    @Test
    public void testFindAllByMerchantIdPaginated()
    {
        persistNumberOfOffers(12);
        int pageSize = 5;
        Pageable pageable = PageRequest.of(2,pageSize);
        Page<Offer> fetchedOffers = offerRepo.findAllByMerchantId(merchantObj.getId(),pageable);
        assertEquals(2,fetchedOffers.getContent().size());
    }

    @Test
    public void testFindAllPaginated()
    {
        persistNumberOfOffers(2);
        int pageSize = 1;
        Pageable pageable = PageRequest.of(1,pageSize);
        Page<Offer> fetchedOffers = offerRepo.findAll(pageable);
        assertEquals(1,fetchedOffers.getContent().size());
    }

    @Test
    public void testfindAllByValidIsGreaterThan()
    {
        ZonedDateTime criteria = TestUtils.dateTimeParser("2018-10-09T22:28:39+00:00");
        Offer valid = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-10T22:28:39+00:00"))
                .setId(null); //Id will be auto generated
        Offer valid1 = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-10T22:29:39+00:00"))
                .setId(null);
        Offer valid2 = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-10T22:30:00+00:00"))
                .setId(null);
        Offer expired = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-09T22:28:39+00:00"))
                .setOfferState(OfferState.EXPIRED).setId(null);
        offerRepo.save(valid.setMerchant(merchantObj));
        offerRepo.save(valid1.setMerchant(merchantObj));
        offerRepo.save(valid2.setMerchant(merchantObj));
        offerRepo.save(expired.setMerchant(merchantObj));
        int pageSize = 3;
        Pageable pageable = PageRequest.of(0,pageSize);
        assertEquals(3,offerRepo.findAllByValidUntilIsGreaterThan(criteria, pageable).getContent().size());
    }

    @Test
    public void testFindAllByValidUntilIsLessThan()
    {
        ZonedDateTime criteria = TestUtils.dateTimeParser("2018-10-09T22:28:39+00:00");
        Offer invalid = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-07T22:28:39+00:00"))
                .setId(null).setOfferState(OfferState.EXPIRED); //Id will be auto generated
        Offer invalid1 = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-06T22:29:39+00:00"))
                .setId(null).setOfferState(OfferState.EXPIRED);
        Offer invalid2 = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-06T22:30:00+00:00"))
                .setId(null).setOfferState(OfferState.EXPIRED);
        Offer valid = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-09T22:28:55+00:00"))
                .setOfferState(OfferState.ACTIVE).setId(null);
        offerRepo.save(invalid.setMerchant(merchantObj));
        offerRepo.save(invalid1.setMerchant(merchantObj));
        offerRepo.save(invalid2.setMerchant(merchantObj));
        offerRepo.save(valid.setMerchant(merchantObj));
        int pageSize = 3;
        Pageable pageable = PageRequest.of(0,pageSize);
        assertEquals(3,offerRepo.findAllByValidUntilIsLessThan(criteria, pageable).getContent().size());
    }


    @Test
    public void testfindAllByMerchantIdValidIsGreaterThan()
    {
        ZonedDateTime criteria = TestUtils.dateTimeParser("2018-10-09T22:28:39+00:00");
        Offer valid = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-10T22:28:39+00:00"))
                .setId(null); //Id will be auto generated
        Offer valid1 = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-10T22:29:39+00:00"))
                .setId(null);
        Offer valid2 = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-10T22:30:00+00:00"))
                .setId(null);
        Offer expired = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-09T22:28:39+00:00"))
                .setOfferState(OfferState.EXPIRED).setId(null);
        offerRepo.save(valid.setMerchant(merchantObj));
        offerRepo.save(valid1.setMerchant(merchantObj));
        offerRepo.save(valid2.setMerchant(merchantObj));
        offerRepo.save(expired.setMerchant(merchantObj));
        int pageSize = 3;
        Pageable pageable = PageRequest.of(0,pageSize);
        assertEquals(3,offerRepo.findAllByMerchantIdAndValidUntilIsGreaterThan(Long.valueOf(1),criteria, pageable).getContent().size());
    }

    @Test
    public void testFindAllByMerchantIdAndValidUntilIsLessThan()
    {
        ZonedDateTime criteria = TestUtils.dateTimeParser("2018-10-09T22:28:39+00:00");
        Offer invalid = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-07T22:28:39+00:00"))
                .setId(null).setOfferState(OfferState.EXPIRED); //Id will be auto generated
        Offer invalid1 = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-06T22:29:39+00:00"))
                .setId(null).setOfferState(OfferState.EXPIRED);
        Offer invalid2 = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-06T22:30:00+00:00"))
                .setId(null).setOfferState(OfferState.EXPIRED);
        Offer valid = TestObjectsFactory.getOfferInstanceWithOutMerchant()
                .setValidUntil(TestUtils.dateTimeParser("2018-10-09T22:28:55+00:00"))
                .setOfferState(OfferState.ACTIVE).setId(null);
        offerRepo.save(invalid.setMerchant(merchantObj));
        offerRepo.save(invalid1.setMerchant(merchantObj));
        offerRepo.save(invalid2.setMerchant(merchantObj));
        offerRepo.save(valid.setMerchant(merchantObj));

        int pageSize = 3;
        Pageable pageable = PageRequest.of(0,pageSize);
        Pageable pageable1 = PageRequest.of(1,pageSize);
        //One extra offer from setUp method
        assertEquals(3,offerRepo.findAllByMerchantIdAndAndValidUntilLessThan(merchantObj.getId(),criteria, pageable).getContent().size());
    }


}
