package io.pleo.antaeus.app

import com.google.inject.AbstractModule
import io.pleo.antaeus.core.integration.PleoPayments
import io.pleo.antaeus.core.services.*
import io.pleo.antaeus.core.spi.Payments
import io.pleo.antaeus.data.CustomerRepo
import io.pleo.antaeus.data.CustomerRepoImpl
import io.pleo.antaeus.data.InvoiceRepo
import io.pleo.antaeus.data.InvoiceRepoImpl

class MainGuiceModule: AbstractModule() {
    override fun configure() {
        bind(CustomerService::class.java).to(CustomerServiceImpl::class.java)
        bind(InvoiceService::class.java).to(InvoiceServiceImpl::class.java)
        bind(InvoiceRepo::class.java).to(InvoiceRepoImpl::class.java)
        bind(CustomerRepo::class.java).to(CustomerRepoImpl::class.java)
        bind(Payments::class.java).to(PleoPayments::class.java)
        bind(BillingService::class.java).to(BillingServiceImpl::class.java)
    }
}