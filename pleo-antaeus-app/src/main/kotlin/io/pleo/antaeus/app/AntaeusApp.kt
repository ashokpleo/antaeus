/*
    Defines the main() entry point of the app.
    Configures the database and sets up the REST web service.
 */

@file:JvmName("AntaeusApp")

package io.pleo.antaeus.app

import com.google.inject.Guice
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.rest.AntaeusRest

fun main() {
    val injector = Guice.createInjector(MainGuiceModule())
    val invoiceService =  injector.getInstance(InvoiceService::class.java)
    val customerService =  injector.getInstance(CustomerService::class.java)

    // Create REST web service
    AntaeusRest(
        invoiceService = invoiceService,
        customerService = customerService
    ).run()
}
