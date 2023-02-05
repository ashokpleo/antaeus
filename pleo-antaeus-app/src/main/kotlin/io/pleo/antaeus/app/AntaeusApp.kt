/*
    Defines the main() entry point of the app.
    Configures the database and sets up the REST web service.
 */

@file:JvmName("AntaeusApp")

package io.pleo.antaeus.app

import com.google.inject.Guice
import io.pleo.antaeus.core.billing.application.api.BillingService
import io.pleo.antaeus.core.customers.application.api.CustomerService
import io.pleo.antaeus.core.invoices.application.api.InvoiceService
import io.pleo.antaeus.rest.AntaeusRest

fun main() {
    val injector = Guice.createInjector(MainGuiceModule())
    val invoiceService =  injector.getInstance(InvoiceService::class.java)
    val customerService =  injector.getInstance(CustomerService::class.java)
    val billingService =  injector.getInstance(BillingService::class.java)

    Bootstrap.setupInitialData(customerService, invoiceService)

    /*
        If I were to add a schedular, it would be here, using the Billing Inbound Port,
        all the code would already support it as along as the Schedular has the invoice IDs.
        If it does not have the Ids, we can pass in the InvoiceService port as well.
    */

    // Create REST web service
    AntaeusRest(
        invoiceService = invoiceService,
        customerService = customerService,
        billingService = billingService
    ).run()
}
