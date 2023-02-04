
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.external.Payments
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.models.*
import java.math.BigDecimal
import kotlin.random.Random

// This will create all schemas and setup initial data
object Bootstrap {
    fun setupInitialData(customerService: CustomerService, invoiceService: InvoiceService) {
        val customers = (1..100).mapNotNull {
            customerService.createCustomer(
                currency = Currency.values()[Random.nextInt(0, Currency.values().size)]
            )
        }

        customers.forEach { customer ->
            (1..10).forEach {
                invoiceService.create(
                    amount = Money(
                        value = BigDecimal(Random.nextDouble(10.0, 500.0)),
                        currency = customer.currency
                    ),
                    customerId = customer.id,
                    currency = customer.currency,
                    status = if (it == 1) InvoiceStatus.PENDING else InvoiceStatus.PAID
                )
            }
        }
    }
}

// This is the mocked instance of the payment provider

class PaymentProvider: Payments {
    /*
    Charge a customer's account the amount from the invoice.

    Returns:
      `True` when the customer account was successfully charged the given amount.
      `False` when the customer account balance did not allow the charge.

    Throws:
      `CustomerNotFoundException`: when no customer has the given id.
      `CurrencyMismatchException`: when the currency does not match the customer account.
      `NetworkException`: when a network error happens.
 */
    override fun charge(invoice: Invoice, customer:Customer?): Boolean {
        if(customer == null)
            throw CustomerNotFoundException(invoice.customerId)
        if (invoice.amount.currency != customer.currency)
            throw CurrencyMismatchException(invoice.id, customer.id)

        // Network Error would be here but won't be adding it

        // Still using a random Boolean so that we can simulate not enough funds or other bank errors
        return Random.nextBoolean()
    }

}
