package com.restapi.demo.repositories;


import com.restapi.demo.domain.Offer;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.scheduling.annotation.Async;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author G.Nikolov on 07/10/18
 * @project rest-service-basic
 *
 * Repository to provide CRUD operations for
 * {@link com.restapi.demo.domain.Offer} objects
 */

@RepositoryRestResource
@AutoConfigurationPackage
public interface OfferRepo extends JpaRepository<Offer, Long> {

    @Async
    Page<Offer> findAllByMerchantId(Long id, Pageable page);

    @Async
    List<Offer> findAll();

    @Async
    Page<Offer> findAll(Pageable page);

    //Active = time > expiritydate (timenow)
    @Async
    Page<Offer> findAllByValidUntilIsGreaterThan(ZonedDateTime time, Pageable page);

    //Expired offers
    @Async
    Page<Offer> findAllByValidUntilIsLessThan(ZonedDateTime time, Pageable page);

    @Async
    Page<Offer> findAllByMerchantIdAndAndValidUntilLessThan(Long id, ZonedDateTime time,Pageable page);

    @Async
    Page<Offer> findAllByMerchantIdAndValidUntilIsGreaterThan(Long id, ZonedDateTime time,Pageable page);

}
