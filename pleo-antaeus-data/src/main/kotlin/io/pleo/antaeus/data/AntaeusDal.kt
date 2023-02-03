/*
    Implements the data access layer (DAL).
    The data access layer generates and executes requests to the database.

    See the `mappings` module for the conversions between database rows and Kotlin objects.
 */

package io.pleo.antaeus.data

import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Customer
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.math.BigDecimal
import java.sql.Connection
import kotlin.random.Random

interface AntaeusDal {
    fun fetchInvoice(id: Int): Invoice?
    fun fetchInvoices(): List<Invoice>
    fun createInvoice(amount: Money, customer: Customer, status: InvoiceStatus = InvoiceStatus.PENDING): Invoice?
    fun fetchCustomer(id: Int): Customer?
    fun fetchCustomers(): List<Customer>
    fun createCustomer(currency: Currency): Customer?
}

class AntaeusDalImpl(): AntaeusDal {

    private val tables = arrayOf(InvoiceTable, CustomerTable)

    private val dbFile: File = File.createTempFile("antaeus-db", ".sqlite")

    // Connect to the database and create the needed tables. Drop any existing data.
    private val db = Database
        .connect(
            url = "jdbc:sqlite:${dbFile.absolutePath}",
            driver = "org.sqlite.JDBC",
            user = "root",
            password = ""
        )
        .also {
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
            transaction(it) {
                addLogger(StdOutSqlLogger)
                // Drop all existing tables to ensure a clean slate on each run
                SchemaUtils.drop(*tables)
                // Create all tables
                SchemaUtils.create(*tables)
            }
        }

    init {
        setupInitialData()
    }

    private fun setupInitialData() {
        val customers = (1..100).mapNotNull {
            createCustomer(
                currency = Currency.values()[Random.nextInt(0, Currency.values().size)]
            )
        }

        customers.forEach { customer ->
            (1..10).forEach {
                createInvoice(
                    amount = Money(
                        value = BigDecimal(Random.nextDouble(10.0, 500.0)),
                        currency = customer.currency
                    ),
                    customer = customer,
                    status = if (it == 1) InvoiceStatus.PENDING else InvoiceStatus.PAID
                )
            }
        }
    }


    override fun fetchInvoice(id: Int): Invoice? {
        // transaction(db) runs the internal query as a new database transaction.
        return transaction(db) {
            // Returns the first invoice with matching id.
            InvoiceTable
                .select { InvoiceTable.id.eq(id) }
                .firstOrNull()
                ?.toInvoice()
        }
    }

    override fun fetchInvoices(): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .selectAll()
                .map { it.toInvoice() }
        }
    }

    override fun createInvoice(amount: Money, customer: Customer, status: InvoiceStatus): Invoice? {
        val id = transaction(db) {
            // Insert the invoice and returns its new id.
            InvoiceTable
                .insert {
                    it[this.value] = amount.value
                    it[this.currency] = amount.currency.toString()
                    it[this.status] = status.toString()
                    it[this.customerId] = customer.id
                } get InvoiceTable.id
        }

        return fetchInvoice(id)
    }

    override fun fetchCustomer(id: Int): Customer? {

        return transaction(db) {
            CustomerTable
                .select { CustomerTable.id.eq(id) }
                .firstOrNull()
                ?.toCustomer()
        }
    }

    override fun fetchCustomers(): List<Customer> {
        return transaction(db) {
            CustomerTable
                .selectAll()
                .map { it.toCustomer() }
        }
    }

    override fun createCustomer(currency: Currency): Customer? {
        val id = transaction(db) {
            // Insert the customer and return its new id.
            CustomerTable.insert {
                it[this.currency] = currency.toString()
            } get CustomerTable.id
        }

        return fetchCustomer(id)
    }
}
