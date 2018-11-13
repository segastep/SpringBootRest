# SpringBootRest

Demo RESTApi built with Spring boot, Hibernate and Swagger.

TBD: Schemas verification

#### Known issues 

Post requests will render the full schema rather than the fields, which are needed to post an offer.
This is due to swagger lacking support of multiples schemas for different requests.

Schema that swagger shows on post is:
```$xslt
{
  "currency": "string",
  "description": "string",
  "id": 0,
  "merchant": {
    "companyName": "string",
    "merchantName": "string",
    "phonenumber": "string"
  },
  "price": 0,
  "validUntil": "2018-10-19T12:42:13.180Z"
}
```

But actually the one needed to create an offer is:

```$xslt
{
  "currency": "string",
  "description": "string",
  "price": 0,
  "validUntil": "2018-10-19T12:42:13.180Z"
}
```
