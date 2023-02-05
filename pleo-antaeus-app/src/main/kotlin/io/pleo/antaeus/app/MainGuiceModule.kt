package io.pleo.antaeus.app

import com.google.inject.AbstractModule
import io.pleo.antaeus.core.billing.application.api.BillingService
import io.pleo.antaeus.core.billing.application.service.BillingServiceImpl
import io.pleo.antaeus.core.customers.application.api.CustomerService
import io.pleo.antaeus.core.customers.application.service.CustomerServiceImpl
import io.pleo.antaeus.core.billing.application.integration.PleoPayments
import io.pleo.antaeus.core.invoices.application.api.InvoiceService
import io.pleo.antaeus.core.invoices.application.service.InvoiceServiceImpl
import io.pleo.antaeus.core.billing.application.spi.Payments
import io.pleo.antaeus.data.CustomerRepo
import io.pleo.antaeus.data.CustomerRepoImpl
import io.pleo.antaeus.data.InvoiceRepo
import io.pleo.antaeus.data.InvoiceRepoImpl

class MainGuiceModule: AbstractModule() {
    override fun configure() {
        bind(InvoiceRepo::class.java).to(InvoiceRepoImpl::class.java)
        bind(CustomerRepo::class.java).to(CustomerRepoImpl::class.java)
        bind(CustomerService::class.java).to(CustomerServiceImpl::class.java)
        bind(InvoiceService::class.java).to(InvoiceServiceImpl::class.java)
        bind(Payments::class.java).to(PleoPayments::class.java)
        bind(BillingService::class.java).to(BillingServiceImpl::class.java)
    }
}