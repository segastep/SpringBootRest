package com.restapi.demo.repositories;

import com.restapi.demo.domain.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * @author G.Nikolov on 07/10/18
 * @project rest-service-basic
 *
 * Repository to provide CRUD operations for
 * {@link com.restapi.demo.domain.Merchant} objects
 */
//@RepositoryRestResource
public interface MerchantRepo extends CrudRepository<Merchant, Integer> {

    void delete(Merchant toDelete);

    List<Merchant> findAll();

    /**
     *
     * @param //pageReq Number of page to be returned
     * @return  A page of Merchant entries
     */
    //Page<Merchant> findAllPaginated(Pageable pageReq);

    Optional<Merchant> findById(Integer id);

    void flush();

    Merchant save(Merchant persist);

}
