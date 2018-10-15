package com.restapi.demo.repositories;

import com.restapi.demo.domain.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.scheduling.annotation.Async;


import java.util.List;
import java.util.Optional;

/**
 * @author G.Nikolov on 07/10/18
 * @project rest-service-basic
 *
 * Repository to provide CRUD operations for
 * {@link com.restapi.demo.domain.Merchant} objects
 */
@RepositoryRestResource
@AutoConfigurationPackage
public interface MerchantRepo extends CrudRepository<Merchant, Long> {

    @Async
    Optional<Merchant> findById(Long id);

    @Async
    List<Merchant> findAllByMerchantName(String merchantName);

    @Async
    Page<Merchant> findAllByMerchantName(String merchantName,Pageable page);

    @Async
    List<Merchant> findAllByCompanyName(String companyName);

    @Async
    Iterable<Merchant> findAll();

    @Async
    Page<Merchant> findAllByCompanyName(String companyName, Pageable page);

    @Async
    void delete(Merchant toDelete);

    @Async
    Page<Merchant> findAll(Pageable pageReq);

    void deleteAllByCompanyName(String companyName);

    void deleteAll();


}
