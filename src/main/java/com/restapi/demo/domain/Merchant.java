package com.restapi.demo.domain;


import io.swagger.annotations.ApiModelProperty;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZonedDateTime;

import java.util.*;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * Merchant DTO
 */

@Entity
@Table(name = "MERCHANTS")
@Valid
public final class Merchant extends AuditModel<String>{

    /**
     * Definitions for DB Entry Merchant
     */
    @Id
    @Column(nullable = false, name = "MERCHANT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Database generated offer id")
    private Long id;


    @NotNull
    @Column(nullable = false, name = "MERCHANT_NAME")
    @Type(type = "nstring")
    @Size(min = 3, max = 120)
    @ApiModelProperty(notes= "Name of the merchant", required = true)
    private String merchantName;

    @Type(type="nstring")
    @Size(min = 6, max = 30)
    @NotNull
    @Column(name = "PHONE_NUMBER")
    @ApiModelProperty(notes = "Phone number associated with merchant, can be null")
    // Alternatively Phonenumber class can be used for validity checks etc
    private String phonenumber;

    @Type(type = "nstring")
    @Size(max = 65)
    @Column(name = "COMPANY_NAME")
    @ApiModelProperty(notes = "Company associated with merchant")
    private String companyName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "merchants", referencedColumnName = "MERCHANT_ID", nullable = false)
    private Set<Offer> offersSet = new HashSet<>();

    //This setter only to be used for entity testing, using it with JPA
    // will cause persistent collection to be overwritten
    public Set<Offer> getOffersSet() {
        return Collections.unmodifiableSet(this.offersSet);
    }

    //JPA to be used with addOffer method only !
    public void addOffer(Offer offer)
    {
        offer.setMerchant(this);
        this.offersSet.add(offer);
    }

    public Merchant setOffersSet(Set<Offer> offersSet) {
        this.offersSet = offersSet;
        return this;
    }

    public Long getId() {
        return id;
    }

    public Merchant setId(Long id) {
        this.id = id;
        return this;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public Merchant setMerchantName(String merchantName) {
        this.merchantName = merchantName;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Merchant setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public Merchant setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Merchant merchant = (Merchant) o;
        return Objects.equals(id, merchant.id) &&
                Objects.equals(merchantName, merchant.merchantName) &&
                Objects.equals(phonenumber, merchant.phonenumber) &&
                Objects.equals(companyName, merchant.companyName) &&
                offersSet.containsAll(merchant.offersSet);

    }

    @Override
    public int hashCode() {
        //Excluding the offer set since since we will end up with infinite recursive call
        return Objects.hash(id, merchantName, phonenumber, companyName, 31);
    }

    @Override
    public String toString() {


        return "Merchant{" +
                "id=" + id +
                ", merchantName='" + merchantName + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", companyName='" + companyName + '\'' +
                ","  + super.toString() + '\''+
                '}';
    }

}