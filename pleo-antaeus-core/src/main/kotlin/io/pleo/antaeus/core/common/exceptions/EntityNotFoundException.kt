package io.pleo.antaeus.core.common.exceptions

abstract class EntityNotFoundException(entity: String, id: Int) : Exception("$entity '$id' was not found")
