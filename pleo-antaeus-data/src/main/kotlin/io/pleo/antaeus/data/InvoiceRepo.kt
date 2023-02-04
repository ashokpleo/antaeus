/*
    Implements the data access layer (DAL).
    The data access layer generates and executes requests to the database.

    See the `mappings` module for the conversions between database rows and Kotlin objects.
 */

package io.pleo.antaeus.data

import io.pleo.antaeus.models.Customer
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.sql.Connection

interface InvoiceRepo {
    fun fetchInvoice(id: Int): Invoice?
    fun fetchInvoices(): List<Invoice>
    fun fetchUnpaidInvoices(): List<Invoice>
    fun createInvoice(amount: Money, customer: Customer, status: InvoiceStatus = InvoiceStatus.PENDING): Invoice?
    fun updateStatus(invoiceId: Int, invoiceStatus: InvoiceStatus): Int // TODO: PS: 04/02:2023 Ideally this should return the updated invoice.
}

class InvoiceRepoImpl(): InvoiceRepo {

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
                SchemaUtils.drop(InvoiceTable)
                // Create all tables
                SchemaUtils.create(InvoiceTable)
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

    override fun fetchUnpaidInvoices(): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .selectAll().filter { it.toInvoice().status != InvoiceStatus.PAID }
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

    override fun updateStatus(invoiceId: Int, invoiceStatus: InvoiceStatus): Int {
        return transaction(db) {
            InvoiceTable.update({ InvoiceTable.id eq invoiceId })
                    { it[status] = invoiceStatus.name }
        }
    }
}
