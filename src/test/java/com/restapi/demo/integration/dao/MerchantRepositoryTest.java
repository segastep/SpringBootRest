package com.restapi.demo.integration.dao;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.repositories.MerchantRepo;
import com.restapi.demo.testutils.TestObjectsFactory;

import org.assertj.core.util.IterableUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.Assert.*;

/**
 * @author G.Nikolov on 14/10/18
 * @project rest-service-basic
 */

/*
 * Annotation below horrible :(
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableJpaRepositories(basePackages = "com.restapi.demo.repositories")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ComponentScan(basePackages = {"com.restapi.demo"})
@Transactional
@RunWith(SpringRunner.class)

/**
 * This annotation here is not quite the one that should be used,
 * this test should be marked annotated as Jpa data tests, but if
 * done so the context configuration fails, tried ways around it
 * but couldn't get it quite right either the AuditModel was failing
 * or the @Autowire was not working for merchant repository apologies,
 * haven't really used spring boot until now.
 *
 * Also couldn't disable the annoying tons of logs due to the same
 * reason, if I set application-test.properties under /test/resources/
 * spring would read the configuration but ignores the logging levels
 * set
 *
 * Perhaps I should have created configuration classes and then inject
 * them in @ContextConfiguration
 *
 * Couldn't get it working with any different setup but this one.
 * Dirties context is quite expensive operation slowing down the test
 * since it forces the whole application context to be re-created
 * before each test is run
 */
public class MerchantRepositoryTest {

    private Merchant testObj;

    @Autowired
    private MerchantRepo merchantRepo;


    @Before
    public void setUp()
    {
        testObj = TestObjectsFactory.getMerchantInstance();
    }

    @After
    public void tearDown()
    {
        testObj = null;
        merchantRepo = null;
    }

    @Test
    public void testCreateMerchant()
    {
        assertNull(testObj.setId(null).getId());
        testObj = merchantRepo.save(testObj);
        assertNotNull(testObj.getId());
        assertTrue(testObj.getId() > 0);
    }

    @Test
    public void testFindById()
    {
        merchantRepo.save(testObj);
        assertTrue(merchantRepo.findById(new Long(1)).isPresent());
    }

    @Test
    public void findAllByUsername()
    {
        merchantRepo.save(testObj.setMerchantName("NAME1"));
        merchantRepo.save(TestObjectsFactory.getMerchantInstance().setId(null).setMerchantName("NAME1"));
        assertEquals(2,merchantRepo.findAllByMerchantName("NAME1").size());
    }

    public void persistTestData()
    {
        merchantRepo.save(testObj);
        merchantRepo.save(TestObjectsFactory.getMerchantInstance().setId(null));
        merchantRepo.save(TestObjectsFactory.getMerchantInstance().setId(null));
    }

    @Test
    public void testFindAllByUsernamePaginated()
    {
        persistTestData();
        int pageSize = 3;
        Pageable pageable = PageRequest.of(0,pageSize);
        Page<Merchant> merchants = merchantRepo.findAllByMerchantName(TestObjectsFactory.MERCHANT_NAME, pageable);
        assertEquals(merchants.getNumberOfElements(), pageSize);
    }

    @Test
    public void testFindAllPaginated()
    {
        persistTestData();

        int pageSize = 3;
        Pageable pageable = PageRequest.of(0,pageSize);
        Page<Merchant> merchants = merchantRepo.findAll(pageable);
        assertEquals(merchants.getSize(), pageSize);
    }

    @Test
    public void findAllByCompanyNamePaginated()
    {
        persistTestData();
        int pageSize = 3;
        Pageable pageable = PageRequest.of(0,pageSize);
        Page<Merchant> merchants = merchantRepo.findAllByCompanyName(TestObjectsFactory.COMPANY_NAME,pageable);
        for (int i = 1; i < merchants.getSize(); i++)
        {
            assertEquals(merchants.getContent().get(i-1).getId(), Long.valueOf(i));
        }
        assertEquals(merchants.getSize(), pageSize);
    }

    @Test
    public void findAllByCompanyName()
    {
        persistTestData();
        List<Merchant> merchants = merchantRepo.findAllByCompanyName(TestObjectsFactory.COMPANY_NAME);
        assertEquals(merchants.size(),3);
    }

    @Test
    public void testDelete()
    {
        merchantRepo.save(testObj);
        merchantRepo.delete(testObj);
        assertFalse(merchantRepo.findById(TestObjectsFactory.TEST_ID).isPresent());
        assertFalse(merchantRepo.findAll().iterator().hasNext());
    }

    @Test
    @Transactional
    public void testDeleteAllByCompanyName()
    {
        persistTestData();
        merchantRepo.deleteAllByCompanyName(TestObjectsFactory.COMPANY_NAME);
        assertEquals(0, IterableUtil.sizeOf(merchantRepo.findAll()));
    }

}
