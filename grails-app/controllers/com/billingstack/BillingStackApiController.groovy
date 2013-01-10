package com.billingstack

import grails.converters.JSON

class BillingStackApiController {

	def hazelcastService

	def billingService

	def authenticate() {
		try {
			def json = request.JSON
			def user
			if(json.api_key && json.api_secret) {
				user = User.findByApiKeyAndApiSecret(json.api_key, json.api_secret)
			} else if (json.username && json.password) {
				user = User.findByUsernameAndPassword(json.username, json.password)
			}
			println user
			if(user) {
				def ur = UserRole.where {
					(customer == null && user == user)
				}.find()
				println ur
				def token = [
					id : (UUID.randomUUID() as String).replaceAll('-',""), 
					endpoint : createLink(params : [merchant : ur.merchant.id], absolute : true) as String,
				]
				def tokens = hazelcastService.map("tokens")
				tokens.put(token.id, token)
				render(text: token as JSON, contentType: 'application/json', encoding:"UTF-8")
			} else {
				println "!!!! 403"
				response.status = 403
				def error = ["error":"Merchant not found"]
				render(text: error as JSON, contentType: 'application/json', encoding:"UTF-8")
			}
    } catch(e) {
        response.status = 500
        def error = ["error":e.message]
        render(text: error as JSON, contentType: 'application/json', encoding:"UTF-8")
        return
    }

	}

	def logout() {
		try {
			def tokens = hazelcastService.map("tokens")
    } catch(e) {
        response.status = 500
        def error = ["error":e.message]
        render(text: error as JSON, contentType: 'application/json', encoding:"UTF-8")
        return
    }	
	}

	def info() {
		try {
			def info = [:]
			render info as JSON
    } catch(e) {
        response.status = 500
        def error = ["error":e.message]
        render(text: error as JSON, contentType: 'application/json', encoding:"UTF-8")
        return
    }
	}
	
	def bill(String merchant, String account, String subscription) {
		try {
			billingService.bill(merchant, account, subscription)
			render(status : 204, text : "")
    } catch(e) {
		log.error(e.message, e)
        response.status = 500
        def error = ["error":e.message]
        render(text: error as JSON, contentType: 'application/json', encoding:"UTF-8")
        return
    }
	}

}