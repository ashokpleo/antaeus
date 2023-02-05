package io.pleo.antaeus.core.invoices.application.exceptions

import io.pleo.antaeus.core.common.exceptions.EntityNotFoundException

class InvoiceNotFoundException(id: Int) : EntityNotFoundException("Invoice", id)
