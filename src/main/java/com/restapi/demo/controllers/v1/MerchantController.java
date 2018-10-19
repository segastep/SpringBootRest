package com.restapi.demo.controllers.v1;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.domain.Offer;
import com.restapi.demo.enums.OfferState;
import com.restapi.demo.exceptions.ResourceNotFoundException;
import com.restapi.demo.repositories.MerchantRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.models.auth.In;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 */
@RestController
@RequestMapping(path = "/api/v1")
@Api(value="Merchant controller", description = "Operations on merchants")
@EnableSpringDataWebSupport
public class MerchantController{

    /**
     *
     * Some operations from Merchant repository were left not implemented on purpose, implement
     * them if needed.
     */
    private static Logger logger = LogManager.getLogger(MerchantController.class);

    private MerchantRepo merchantRepo;

    @Autowired
    public void setRepo(MerchantRepo merchantRepo)
    {
       this.merchantRepo = merchantRepo;
    }

    @ApiOperation(value = "View a list of available merchants")
    @GetMapping("/merchants")
    public Page<Merchant> getAllMerchants(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                          @RequestParam(value = "size", defaultValue = "50", required = false) Integer size,
                                          @RequestParam(value= "sort", defaultValue = "id", required = false) String sort)
    {
        return merchantRepo.findAll(PageRequest.of(page, size, Sort.Direction.ASC, sort));
    }

    @ApiOperation(value = "Find merchant by ID")
    @GetMapping("/merchants/{id}")
    public Merchant getMerchantById(@RequestParam Long merchantId)
    {
        return merchantRepo.findById(merchantId).orElseThrow(
                () -> new ResourceNotFoundException("No merchant with id: " + merchantId + " found!"));
    }

    @ApiOperation(value = "Delete all merchants")
    @DeleteMapping("/merchants")
    public ResponseEntity<?> deleteAllMerchants()
    {
        merchantRepo.deleteAll();
        return ResponseEntity.ok().build();
    }


    @ApiOperation(value = "Create new merchant")
    @PostMapping("/merchants")
    public Merchant createMerchant(@Valid @RequestBody Merchant merchant)
    {

        return merchantRepo.save(merchant);
    }

    @ApiOperation(value = "Update existing merchant")
    @PutMapping("/merchants/{merchnantId}")
    public Merchant updateMerchant(@RequestParam Long merchantId, @Valid @RequestBody Merchant postRequest)
    {
        return merchantRepo.findById(merchantId).map(m ->{
            m.setMerchantName(postRequest.getMerchantName());
            m.setPhonenumber(postRequest.getPhonenumber());
            m.setCompanyName(postRequest.getCompanyName());
            return merchantRepo.save(m);
        }).orElseThrow(() -> new ResourceNotFoundException("MerchantId: " + merchantId + "not found"));
    }

    @ApiOperation(value = "Delete a merchant")
    @DeleteMapping("/merchants/{merchantId}")
    public ResponseEntity<?> deleteMerchant(@PathVariable Long merchantId)
    {
        return merchantRepo.findById(merchantId).map(m -> {
            merchantRepo.delete(m);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("MerchantId: " + merchantId + "not found"));
    }
}
