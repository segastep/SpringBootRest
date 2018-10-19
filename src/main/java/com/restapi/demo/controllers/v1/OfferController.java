package com.restapi.demo.controllers.v1;

import com.restapi.demo.domain.Merchant;
import com.restapi.demo.domain.Offer;
import com.restapi.demo.enums.OfferState;
import com.restapi.demo.exceptions.NoResourceAvailable;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @GetMapping("/offers")
    public Page<Offer> getAllOffers(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                    @RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
                                    @RequestParam(value= "sort", defaultValue = "id", required = false) String sort)


    {
        Page<Offer> res = offerRepo.findAll(PageRequest.of(page, size, Sort.Direction.ASC, sort));
        if(res.getTotalElements() < 1){ throw new ResourceNotFoundException("No offers found !");}
        return res.map(o -> {
            //Is before compares down to instant
            if (o.getOfferState() != OfferState.CANCELLED && ZonedDateTime.now().isAfter(o.getValidUntil())) {
                if (o.getOfferState() == OfferState.ACTIVE) {
                    o.setOfferState(OfferState.EXPIRED);
                    logger.info("Updating offer from find all");
                    return offerRepo.save(o);
                }
            }
            logger.info("No need to update returning offer for this findall by merchant");
            return o;
        });

    }

    @GetMapping("/{merchantId}/offers")
    public Page<Offer> getAllOffersByMerchantId(@PathVariable(value = "merchantId") Long merchnatId,
                                                @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                                @RequestParam(value = "size", defaultValue = "50", required = false) Integer size,
                                                @RequestParam(value= "sort", defaultValue = "id", required = false) String sort
                                                )
    {
        Page<Offer> res = offerRepo.findAllByMerchantId(
                                        merchnatId, PageRequest.of(page, size, Sort.Direction.ASC, sort));
        if(res.getTotalElements() < 1) throw new ResourceNotFoundException("No offers found for merchant with ID: " + merchnatId);
        return res.map(o -> {
            //Is before compares down to instant
            if (o.getOfferState() != OfferState.CANCELLED && ZonedDateTime.now().isAfter(o.getValidUntil())) {
                if (o.getOfferState() == OfferState.ACTIVE) {
                    o.setOfferState(OfferState.EXPIRED);
                    logger.info("Updating offer from find all by merchant call ...");
                    return offerRepo.save(o);
                }
            }
            logger.info("No need to update returning offer for this findall by merchant");
            return o;
        });

    }
    private static Logger logger = LogManager.getLogger(OfferController.class);

    @PostMapping("/{merchantId}/offers")
    @ApiOperation(value = "Create offer, valid until field is in ZonedDateTime e.g UTC")
    public Offer createOffer(@PathVariable (value = "merchantId") Long merchantId,
                               @Valid @RequestBody Offer offer)
    {
        OfferState state = ZonedDateTime.now().isBefore(offer.getValidUntil()) ?
              OfferState.ACTIVE : OfferState.EXPIRED;

        return merchantRepo.findById(merchantId).map(merchant ->
             offerRepo.save(offer.setMerchant(merchant).setOfferState(state))
        ).orElseThrow(() -> new ResourceNotFoundException("MerchantId " + merchantId + " not found"));
    }

    /**
     * With account authorization the id of the user can be
     * used rather than passing merchId from a mapping as
     * a parameter, mapping then can be changed to
     * "/offers/{offerId} where the update method will
     * be fetch the specified offer ID and check if it
     * belongs to that user and if does only then will
     * perform update
     */
    @PutMapping("/{merchantId}/offers/{offerId}")
    public Offer updateOffer(@PathVariable (value = "merchantId") Long merchantId,
                             @PathVariable (value = "offerId") Long offerId,
                             @Valid @RequestBody Offer offerRequest)
    {
        Optional<Merchant> merchant = merchantRepo.findById(merchantId);
        if(!merchant.isPresent())
        {
            throw new ResourceNotFoundException("No merchant with id " + merchantId + "found!");
        }
        return offerRepo.findById(offerId).map(offer ->
          {

              OfferState state = offerRequest.getValidUntil().isBefore(ZonedDateTime.now()) ?
                                    OfferState.ACTIVE : OfferState.EXPIRED;
                offer.setMerchant(merchant.get())
                        .setValidUntil(offerRequest.getValidUntil())
                        .setDescription(offerRequest.getDescription())
                        .setOfferState(state)
                        .setPrice(offerRequest.getPrice())
                        .setCurrency(offerRequest.getCurrency());
                return offerRepo.save(offer);
          }).orElseThrow(() -> new ResourceNotFoundException("On offer with id: " + offerId + "exists!"));
    }

    @ApiOperation(value = "Delete an offer")
    @DeleteMapping("/offers/{offerId}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long offerId)
    {
        return offerRepo.findById(offerId).map(offer -> {
            offerRepo.delete(offer);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new ResourceNotFoundException("Cannot find offer with id: " +
                offerId + " to delete"));
    }

    @ApiOperation(value = "Delete all offers")
    @DeleteMapping("/offers")
    public ResponseEntity<?> deleteAllOffers()
    {
        offerRepo.deleteAll();
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Delete all offers belonging to merchant")
    @DeleteMapping("/{merchantId}/offers")
    public ResponseEntity<?> deleteAllOffersByMerchant(@PathVariable (value = "merchantId") Long merchantId)
    {
        merchantId = Long.valueOf("1");
        offerRepo.deleteAllByMerchantId(merchantId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get all active offers")
    @GetMapping("/offers/active")
    public Page<Offer> getAllActive(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                    @RequestParam(value = "size", defaultValue = "50", required = false) Integer size,
                                    @RequestParam(value= "sort", defaultValue = "id", required = false) String sort)
    {
        Page<Offer> res = offerRepo.findAllByValidUntilIsGreaterThan(ZonedDateTime.now(),
                                    PageRequest.of(page, size, Sort.Direction.ASC, sort));

        if(res.getTotalElements() < 1) throw new NoResourceAvailable("No active offers found, nothing to show!");
        return res.map(offer -> {
            //Is before compares down to instant
            if (offer.getOfferState() != OfferState.CANCELLED && ZonedDateTime.now().isAfter(offer.getValidUntil())) {
                if (offer.getOfferState() == OfferState.ACTIVE) {
                    offer.setOfferState(OfferState.EXPIRED);
                    return offerRepo.save(offer);
                }
            }

            return offer;
        });
    }

    @ApiOperation(value = "Get all inanctive offers")
    @GetMapping("/offers/inactive")
    public Page<Offer> getAllInactive(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
                                    @RequestParam(value = "size", defaultValue = "50", required = false) Integer size,
                                    @RequestParam(value= "sort", defaultValue = "id", required = false) String sort)
    {
        Page<Offer> res = offerRepo.findAllByValidUntilIsLessThan(ZonedDateTime.now(),
                PageRequest.of(page, size, Sort.Direction.ASC, sort));

        if(res.getTotalElements() < 1) throw new NoResourceAvailable("No inactive offers found, nothing to show!");
        return res.map(offer -> {
            if (offer.getOfferState() != OfferState.CANCELLED) {
                if (offer.getOfferState() == OfferState.ACTIVE) {
                    return ZonedDateTime.now().isAfter(offer.getValidUntil()) ?
                            offerRepo.save(offer.setOfferState(OfferState.EXPIRED)) : offer;
                }
            }
            return offer;
        });
    }

    @ApiOperation(value = "Cancel offer by id")
    @PutMapping("/offers/cancelled/{offerId}")
    public Offer cencellOffer(@PathVariable (value = "offerId") Long offerId)
    {
        Optional<Offer> offer = offerRepo.findById(offerId);
        if(!offer.isPresent()) throw new ResourceNotFoundException("No offer existing with id: " + offerId);
        return offerRepo.save(offer.get().setOfferState(OfferState.CANCELLED));
    }

    @ApiOperation(value = "Cancel all offers belonging to certain merchant")
    @PutMapping("/{merchantId}/offers/cancelled")
    public ResponseEntity<?> cancellAllByMerchnat(@PathVariable (value = "merchantId") Long merchantId)
    {
        List<Offer> offersFound = offerRepo.findAllByMerchantId(merchantId);
        if(offersFound.isEmpty()) throw new ResourceNotFoundException(
                                    "No offers found for merchant with ID:" + merchantId);
        try {
            offersFound.forEach(offer -> offerRepo.save(offer.setOfferState(OfferState.CANCELLED)));
            return ResponseEntity.ok(offersFound.size() + " offers were successfully cancelled");
        }catch (Exception e)
        {
           throw new RuntimeException("Failed to cancel offers, stacktrace:  " + e.getMessage());
        }
    }

}
