package com.billingstack

import grails.converters.JSON

class PlansApiController {

    def plansService

    def list() {
        try {
      def query = [:]
          if(params.merchant) {
              query['merchant.id'] = params.merchant
          }
            render Plan.findAllWhere(query).collect { it.serialize() } as JSON
        } catch(e) {
            response.status = 500
            def error = ["error":e.message]
            render error as JSON
            return
        }
    }

    def create(String merchant) {
      try {
        render plansService.create(merchant, request.JSON).serialize() as JSON
      } catch(e) {
        log.error(e.message,e)
        response.status = 500
        def error = ["error":e.message]
        render error as JSON
        return
      }  
    }

    def show(String merchant, String id) {
        try {
            render Plan.get(id).serialize() as JSON
        } catch(e) {
            response.status = 500
            def error = ["error":e.message]
            render error as JSON
            return
        }
      
    }

    def update(String merchant, String id) { 
        try {
            render plansService.update(merchant, id, request.JSON).serialize() as JSON
        } catch(e) {
            response.status = 500
            def error = ["error":e.message]
            render error as JSON
            return
        }
      
    }

    def delete(String merchant, String id) {
				try {
            plansService.delete(id)
            render(status : 204)
        } catch(e) {
            response.status = 500
            def error = ["error":e.message]
            render error as JSON
            return
        }
    }

}
