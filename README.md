# SpringBootRest
####Known issues 

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

#####Things that could have been handle better

1. Price and currency
+ Currency is currently saved as string value which is quite bad, should have at least made it an enum of to have researched other means to handle it
+ Might have been better if used Monetary type to handle them both I guess

#####Testing
Didn't really built the controllers using TDD approach, I rather implemented and tested them through Swagger since it was faster and I spent quite a low time on this already.

#####Commits

I had the project in another repository, so you can see some commits that are functionality added with no tests, well actually these were partial commits of things I was sure will be used. Only the controllers weren't built using TDD, aplogies.