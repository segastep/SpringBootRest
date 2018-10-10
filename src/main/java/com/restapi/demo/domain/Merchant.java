package com.restapi.demo.domain;


import io.swagger.annotations.ApiModelProperty;


import org.hibernate.annotations.Type;
import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * Merchant DTO
 */

@Entity
@Table(name = "MERCHANTS")
public final class Merchant extends TimestampModel {

    /**
     * Definitions for DB Entry Merchant
     */

    @Id
    @NotNull
    @Column(nullable = false, name = "MERCHANT_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "Database generated offer id")
    private Integer id;


    @NotNull
    @Column(nullable = false, name = "MERCHANT_NAME")
    @Type(type = "nstring")
    @Size(min = 3, max = 120)
    @ApiModelProperty(notes= "Name of the merchant", required = true)
    private String merchantName;


    @Type(type="nstring")
    @Size(min = 6, max = 30)
    @Column(name = "PHONE_NUMBER")
    @ApiModelProperty(notes = "Phone number associated with merchant, can be null")
    // Alternatively Phonenumber class can be used for validity checks etc
    private String phonenumber;

    @Type(type = "nstring")
    @Size(max = 65)
    @Column(name = "COMPANY_NAME")
    @ApiModelProperty(notes = "Company associated with merchant")
    private String companyName;


    //One may argue @Valid annotation is needed here for the offerSet but
    //there might be cases where there are no offers associated to a merchant
    // and such scenario would result in exception thrown by @Valid mapping
    //however since Offer model entity checking is enforced any invalid
    //fields within an offer object will be caught by the Offer model
    //before they even make it to this set here
    @OneToMany(cascade = CascadeType.ALL,
                fetch = FetchType.EAGER,
                mappedBy = "merchant")
    @ApiModelProperty(notes = "List of associated offers with this merchant")
    private Set<Offer> offersSet;

    public Integer getId() {
        return id;
    }

    public Merchant setId(Integer id) {
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

    public Set<Offer> getOffersSet() {
        return offersSet;
    }

    public Merchant setCreatedAt(ZonedDateTime createdAt)
    {
        super.setCreatedAt(createdAt);
        return this;
    }

    public Merchant setUpdatedAt(ZonedDateTime updatedAt)
    {
        super.setUpdatedAt(updatedAt);
        return this;
    }

    public Merchant setOffersSet(Set<Offer> offersSet) {
        this.offersSet = offersSet;
        return this;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", merchantName='" + merchantName + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", companyName='" + companyName + '\'' +
                ", offersSet=" + offersSet + '\'' +
                ","  + super.toString() + '\''+
                '}';
    }

}