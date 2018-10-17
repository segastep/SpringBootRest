package com.restapi.demo.controllers.v1;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.domain.Offer;
import com.restapi.demo.enums.OfferState;
import com.restapi.demo.exceptions.ResourceNotFoundException;
import com.restapi.demo.repositories.MerchantRepo;
import com.restapi.demo.repositories.OfferRepo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 */
@RestController
@RequestMapping(path = "/api/v1/merchants")
@Api(value="Merchant controller", description = "Operations on merchants")
@EnableSpringDataWebSupport
public class OfferController {

    /**
     * pre and post operations should have been implemented to manage
     * the offers validity time, but found out too late about this concept
     * or should have used mongoDB which supports TTL
     *
     * Controllers were tested on the go since swagger is used ...
     * not the way to go but the it was the fastest way to tackle issues
     */

    @Autowired
    private MerchantRepo merchantRepo;

    @Autowired
    private OfferRepo offerRepo;

    @ApiOperation(value = "View a list of available offers",response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success."),
            @ApiResponse(code = 401, message = "Unauthorized."),
            @ApiResponse(code = 403, message = "Forbidden."),
            @ApiResponse(code = 404, message = "Not found")
    }
    )
    @GetMapping("/offers")
    public Page<Offer> getAllOffers(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                       @RequestParam(value = "size", defaultValue = "50", required = false) Integer size,
                                       @RequestParam(value= "sort", defaultValue = "id", required = false) String sort)

    {
        return offerRepo.findAll(PageRequest.of(page, size, Sort.Direction.ASC, sort));
    }

    @GetMapping("/{merchantId}/offers")
    public Page<Offer> getAllOffersByMerchantId(@PathVariable(value = "merchantId") Long merchnatId,
                                                @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                @RequestParam(value = "size", defaultValue = "50", required = false) Integer size,
                                                @RequestParam(value= "sort", defaultValue = "id", required = false) String sort
                                                )
    {
        return offerRepo.findAllByMerchantId(merchnatId, PageRequest.of(page, size, Sort.Direction.ASC, sort));
    }
    private static Logger logger = LogManager.getLogger(OfferController.class);

    @PostMapping("/{merchantId}/offers")
    @ApiOperation(value = "Create offer")
    public Offer createOffer(@PathVariable (value = "merchantId") Long merchantId,
                               @Valid @RequestBody Offer offer)
    {
        //Offer updatedOffer = offer.getValidUntil().isBefore(ZonedDateTime.now()) ?
         //       offer.setOfferState(OfferState.ACTIVE) : offer.setOfferState(OfferState.ACTIVE);

        return merchantRepo.findById(merchantId).map(merchant -> {
            logger.info("Merchant: " +  merchant.toString());
            logger.info(offer.toString());
            offer.setMerchant(merchant);
            logger.info(offer.toString());
            return offerRepo.save(offer);
        }).orElseThrow(() -> new ResourceNotFoundException("MerchantId " + merchantId + " not found"));
    }

    //@PutMapping("/posts/{postId}/comments/{commentId}")
    //public Comment updateOffer(@PathVariable (value = "postId") Long postId,
    //                             @PathVariable (value = "commentId") Long commentId,
    //                             @Valid @RequestBody Comment commentRequest) {
    //    if(!postRepository.existsById(postId)) {
    //        throw new ResourceNotFoundException("PostId " + postId + " not found");
    //    }


}
