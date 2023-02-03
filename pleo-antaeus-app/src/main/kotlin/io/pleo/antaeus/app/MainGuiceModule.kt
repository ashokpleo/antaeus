package io.pleo.antaeus.app

import com.google.inject.AbstractModule
import io.pleo.antaeus.core.services.*
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.data.AntaeusDalImpl

class MainGuiceModule: AbstractModule() {
    override fun configure() {
        bind(CustomerService::class.java).to(CustomerServiceImpl::class.java)
        bind(InvoiceService::class.java).to(InvoiceServiceImpl::class.java)
        bind(AntaeusDal::class.java).to(AntaeusDalImpl::class.java)
    }
}