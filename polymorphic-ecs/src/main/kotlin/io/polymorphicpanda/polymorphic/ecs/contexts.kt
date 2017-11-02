package io.polymorphicpanda.polymorphic.ecs

interface Context {
    fun manage(entity: Entity): EntityEditor
    fun changeset(cs: ChangeSet.() -> Unit)
}

interface WorldContext: Context

interface SystemContext: Context {
    fun entities(): List<Entity>
}
