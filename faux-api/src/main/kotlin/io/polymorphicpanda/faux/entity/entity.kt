package io.polymorphicpanda.faux.entity

import io.polymorphicpanda.faux.blueprint.Blueprint
import io.polymorphicpanda.faux.util.Try

typealias Entity = Int
interface EntityEditor

interface Context {
    fun create(blueprint: Blueprint? = null): Entity
    fun manage(entity: Entity): Try<EntityEditor>
}
