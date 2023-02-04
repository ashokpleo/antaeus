
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
