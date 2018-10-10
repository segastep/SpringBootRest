package com.restapi.demo.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.restapi.demo.enums.OfferState;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-
 * Offer DTO
 */
@Entity
@Table(name = "OFFERS")
public final class Offer extends TimestampModel {

    @Id
    @NotNull
    @Column(nullable = false, name = "OFFER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "Database generated offer id")
    private Integer id;


    @Lob
    @NotNull
    @Column(nullable = false, name = "DESCRIPTION")
    @Type(type ="org.hibernate.type.StringType")
    @ApiModelProperty(notes= "Offer's description", required = true)
    private String description;

    //In general here we can handle this using Money object,
    // but splitting the entry into price and currency
    // allows independent updates for each value and hence it's easier to implement
    @NotNull
    @Column(nullable = false, name = "PRICE", precision = 6)
    @ApiModelProperty(notes = "Price of product")
    private Long price;

    //It would be more convenient currency to be changed either to currency type
    // in real life application or as explained above to be handled by Money type
    @NotNull
    @Column(nullable = false, name = "CURRENCY")
    @ApiModelProperty(notes = "Price's currency")
    @Type(type = "org.hibernate.type.CurrencyType")
    @Size(min=3, max=3)
    private String currency;

    @NotNull
    //@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "VALID_UNTIL")
    @ApiModelProperty(notes = "Offer expiry time")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime validUntil;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "OFFER_STATE")
    @ApiModelProperty(notes = "Status of offer")
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private OfferState offerState;


    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity = Merchant.class)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private Merchant merchant;

    public Integer getId() {
        return id;
    }

    public Offer setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Offer setDescription(String description) {
        this.description = description;
        return this;
    }

    public ZonedDateTime getValidUntil() {
        return validUntil;
    }

    public Offer setValidUntil(ZonedDateTime validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    public OfferState getOfferState() {
        return offerState;
    }

    public Offer setOfferState(OfferState offerState) {
        this.offerState = offerState;
        return this;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Offer setMerchant(Merchant merchant) {
        this.merchant = merchant;
        return this;
    }

    public Long getPrice() {
        return price;
    }

    public Offer setPrice(Long price) {
        this.price = price;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Offer setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Offer setCreatedAt(ZonedDateTime createdAt)
    {
        super.setCreatedAt(createdAt);
        return this;
    }

    public Offer setUpdatedAt(ZonedDateTime updatedAt)
    {
        super.setUpdatedAt(updatedAt);
        return this;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", validUntil=" + validUntil + '\'' +
                ", offerState=" + offerState + '\'' +
                ", merchant=" + merchant + '\'' +
                "," + super.toString() + '\''+
                '}';
    }

}
