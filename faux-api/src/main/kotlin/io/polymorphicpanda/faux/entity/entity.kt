package io.polymorphicpanda.faux.entity

import io.polymorphicpanda.faux.blueprint.Blueprint

typealias Entity = Int
interface EntityEditor

interface Context {
    fun create(blueprint: Blueprint? = null): Entity
    fun manage(entity: Entity): EntityEditor
}
