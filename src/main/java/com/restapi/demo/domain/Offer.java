package com.restapi.demo.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.deser.key.ZonedDateTimeKeyDeserializer;
import com.restapi.demo.enums.OfferState;
import com.restapi.demo.serializers.ZonedDateTimeDeserializer;
import com.restapi.demo.serializers.ZonedDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import org.springframework.core.Constants;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static org.hibernate.type.descriptor.java.JdbcDateTypeDescriptor.DATE_FORMAT;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-
 *
 */
@Entity
@Table(name = "OFFERS")
@Valid
public final class Offer extends AuditModel<String>{

    @Id
    @Column(nullable = false, name = "OFFER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Database generated offer id")
    private Long id;

    @Lob
    @NotNull(message = "Description cannot be null")
    @Column(nullable = false, name = "DESCRIPTION")
    @Size(min = 10, message = "Description should be at least {min} characters long")
    @ApiModelProperty(notes= "Offer's description", required = true)
    private String description;

    //In general here we can handle this using MonetaryAmount object,
    // but splitting the entry into price and currency
    // allows independent updates for each value and hence it's easier to implement
    //And validated with CurrencyValidatorForMonetaryAmount
    @NotNull(message = "Price cannot be null")
    @Column(nullable = false, name = "PRICE", precision = 6)
    @ApiModelProperty(notes = "Price of product")
    private Long price;

    //It would be more convenient currency to be changed either to currency type
    // in real life application or as explained above to be handled by Money/MonetaryAmount type
    @NotNull(message = "Currency type cannot be null")
    @Column(nullable = false, name = "CURRENCY")
    @ApiModelProperty(notes = "Price's currency")
    @Type(type = "nstring")
    @Size(min=3, max=3, message = "Currency type must be of fixed length 3")
    private String currency;

    @NotNull(message = "Valid until field cannot be null")
    @Column(name = "VALID_UNTIL")
    @ApiModelProperty(notes = "Offer expiry time")
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime validUntil;

    //@NotNull(message = "Offer state cannot be null !")
    @Enumerated(EnumType.STRING)
    @Column(name = "OFFER_STATE", nullable = false)
    @ApiModelProperty(notes = "Status of offer", readOnly = true)
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private OfferState offerState;

    public Long getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //Alternatively, uncomment the lines below to show the Merchant reference just as an ID
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ApiModelProperty(notes = "The id of the merchant owning the offer", readOnly = true)
    protected Merchant merchant;

    public Offer setId(Long id) {
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
        this.validUntil = ZonedDateTime.ofInstant(validUntil.toInstant(), ZoneId.of("UTC"));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id) &&
                Objects.equals(description, offer.description) &&
                Objects.equals(price, offer.price) &&
                Objects.equals(currency, offer.currency) &&
                Objects.equals(validUntil, offer.validUntil) &&
                offerState == offer.offerState &&
                //Only use ID or we will end in infinite recursion
                Objects.equals(merchant.getId(), offer.merchant.getId());
    }

    @Override
    public int hashCode() {
        //using id for the same reason as above
        return Objects.hash(id, description, price, currency,
                validUntil, offerState, merchant.getId());
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
