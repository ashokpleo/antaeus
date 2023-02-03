
import io.pleo.antaeus.core.external.Payments
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import java.math.BigDecimal
import kotlin.random.Random

// This will create all schemas and setup initial data


// This is the mocked instance of the payment provider

class PaymentProvider: Payments {
    override fun charge(invoice: Invoice): Boolean {
        return Random.nextBoolean()
    }

}

internal fun getPaymentProvider(): Payments {
    return object : Payments {
        override fun charge(invoice: Invoice): Boolean {
            return Random.nextBoolean()
        }
    }
}
