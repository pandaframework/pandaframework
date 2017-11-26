package io.polymorphicpanda.faux.core.util

class DynamicGraph<T> {
    data class Node<out T>(val value: T)

    private val nodes = hashSetOf<Node<T>>()
    // <node> is a dependency of <listOf(nodes)>
    private val dependencyOf = mutableMapOf<Node<T>, MutableList<Node<T>>>()
    // <node> depends on <listOf(nodes)>
    private val dependsOn = mutableMapOf<Node<T>, MutableList<Node<T>>>()

    fun addNode(value: T) {
        nodes.add(Node(value))
    }

    fun addEdge(from: T, to: T) {
        val fromNode = Node(from)
        val toNode = Node(to)
        nodes.add(fromNode)
        nodes.add(toNode)

        addDependency(fromNode, toNode)
    }

    fun removeNode(value: T) {
        val node = Node(value)
        nodes.remove(node)
        dependsOn.remove(node)
        dependencyOf[node]?.forEach {
            dependsOn[it]?.remove(node)
        }
    }

    fun getFreeNodes(): Set<T> {
        val nonFree = mutableSetOf<T>()
        nodes.forEach {
            val du = dependsOn[it]
            if (du != null && du.size > 0) {
                nonFree.add(it.value)
            }
        }
        return nodes.map { it.value }
            .filter { !nonFree.contains(it) }
            .toSet()
    }

    private fun addDependency(from: Node<T>, to: Node<T>) {
        dependencyOf.computeIfAbsent(to) { mutableListOf() }
            .add(from)

        dependsOn.computeIfAbsent(from) { mutableListOf() }
            .add(to)
    }
}

fun main(args: Array<String>) {
    val dg = DynamicGraph<String>().apply {
        // a -> b
        // b -> c, d
        // e
        addEdge("a", "b")
        addEdge("b", "c")
        addEdge("b", "d")
        addNode("e")
    }

    println(dg.getFreeNodes())
    dg.removeNode("c")
    println(dg.getFreeNodes())
    dg.removeNode("d")
    println(dg.getFreeNodes())
    dg.removeNode("e")
    println(dg.getFreeNodes())
    dg.removeNode("b")
    println(dg.getFreeNodes())
}
