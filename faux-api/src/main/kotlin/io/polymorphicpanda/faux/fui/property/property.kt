package io.polymorphicpanda.faux.fui.property


interface ValueChangeSubscriber<T> {
    fun valueChanged(old: T, new: T)
}

interface Property<T> {
    fun getValue(): T
    fun subscribe(subscriber: ValueChangeSubscriber<T>)
    fun unsubscribe(subscriber: ValueChangeSubscriber<T>)
}

interface MutableProperty<T>: Property<T> {
    fun setValue(value: T)
    fun bind(property: Property<T>)
    fun unbind()
    fun asReadonlyProperty(): Property<T>
}

class SimpleProperty<T>(initial: T): MutableProperty<T> {
    private var value: T = initial
    private val subscribers = mutableSetOf<ValueChangeSubscriber<T>>()
    private var bindingSubscriber: BindingSubscriber? = null
    private var readOnlyView: Property<T>? = null

    override fun getValue() = value

    override fun setValue(value: T) {
        if (bindingSubscriber != null) {
            throw IllegalStateException("Property is bounded.")
        }

        if (value != this.value) {
            val old = this.value
            this.value = value
            notifySubscribers(old, value)
        }
    }

    override fun subscribe(subscriber: ValueChangeSubscriber<T>) {
        subscribers.add(subscriber)
    }

    override fun unsubscribe(subscriber: ValueChangeSubscriber<T>) {
        subscribers.remove(subscriber)
    }

    override fun bind(property: Property<T>) {
        if (bindingSubscriber != null) {
            throw IllegalStateException("Property is already bounded.")
        }

        bindingSubscriber = BindingSubscriber(property)
        bindingSubscriber!!.subscribe()
    }

    override fun unbind() {
        if (bindingSubscriber == null) {
            throw IllegalStateException("Property not bound.")
        }

        bindingSubscriber!!.unsubscribe()
        bindingSubscriber = null
    }

    override fun asReadonlyProperty(): Property<T> {
        if (readOnlyView == null) {
            val self = this
            readOnlyView = object: Property<T> {
                override fun getValue() = self.getValue()

                override fun subscribe(subscriber: ValueChangeSubscriber<T>) {
                    self.subscribe(subscriber)
                }

                override fun unsubscribe(subscriber: ValueChangeSubscriber<T>) {
                    self.unsubscribe(subscriber)
                }
            }
        }
        return readOnlyView!!
    }

    private fun notifySubscribers(old: T, new: T) {
        subscribers.forEach { subscriber ->
            subscriber.valueChanged(old, new)
        }
    }

    inner class BindingSubscriber(val property: Property<T>): ValueChangeSubscriber<T> {
        override fun valueChanged(old: T, new: T) {
            val oldValue = this@SimpleProperty.value
            this@SimpleProperty.value = property.getValue()
            notifySubscribers(oldValue, new)
        }

        fun subscribe() {
            property.subscribe(this)
            val oldValue = this@SimpleProperty.value
            this@SimpleProperty.value = property.getValue()
            notifySubscribers(oldValue, property.getValue())
        }

        fun unsubscribe() {
            property.unsubscribe(this)
        }
    }
}
