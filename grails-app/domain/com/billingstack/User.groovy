package com.billingstack

class User extends BillingEntity {

    String username
    String password

    String language

    String apiKey
    String apiSecret

    static constraints = {
    	username(unique : true)
    	password(nullable : true)

        language(nullable : true)

    	apiKey(nullable : true)
    	apiSecret(nullable : true)

    }

    def serialize(detail) {
        def json = [
            'id' : id,
            'username' : username,
            'language' : language,
            
        ]
		def contactInformation = ContactInformation.findByUser(this)
		if(contactInformation) {
			json.contact_information = contactInformation.serialize()
		}
        if(detail) {
            json.api_key = apiKey
            json.api_secret = apiSecret
        }
        json
    }

}
