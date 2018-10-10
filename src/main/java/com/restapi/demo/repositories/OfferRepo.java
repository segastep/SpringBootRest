package com.restapi.demo.repositories;


import com.restapi.demo.domain.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author G.Nikolov on 07/10/18
 * @project rest-service-basic
 *
 * Repository to provide CRUD operations for
 * {@link com.restapi.demo.domain.Offer} objects
 */

//@RepositoryRestResource
public interface OfferRepo extends CrudRepository<Offer, Integer> {

}
