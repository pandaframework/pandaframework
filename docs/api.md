## API

The engine is ECS based (Entity - Component - System) with a very simple state machine implementation.

### Try, Option
```kotlin
sealed class Option<out T> {
    data class Some<out T> (val result: T): Option<T>()
    object None: Option<Nothing>()

    fun <K> map(transform: (T) -> K): Option<K> = when (this) {
        is Some<T> -> Some(transform(result))
        is None -> None
    }
}

typealias Some<T> = Option.Some<T>
typealias None = Option.None

sealed class Try<out T> {
    data class Success<out T>(val result: T): Try<T>()
    data class Failure(val error: Throwable): Try<Nothing>()

    fun <K> map(transform: (T) -> K): Try<K> = when (this) {
        is Success<T> -> Success(transform(result))
        is Failure -> this
    }
}

typealias Success<T> = Try.Success<T>
typealias Failure = Try.Failure


```

### ECS
```kotlin
typealias Entity = Int

interface Component {
    fun detached() { /* do nothing by default */ }
}

abstract class EntityEditor {
    abstract fun <T: Component> get(componentType: KClass<T>) = Option<T>
    abstract fun <T: Component> contains(componentType: KClass<T>): Boolean
    
    inline fun <reified T: Component> get() = get(T::class)
    inline fun <reified T: Component> contains() = contains(T::class) 
}

abstract class EditScope {
    // EntityEditor
    abstract fun <T: Component> EntityEditor.add(componentType: KClass<T>, component: T)
    abstract fun <T: Component> EntityEditor.remove(componentType: KClass<T>): Option<T>
    inline fun <reified T: Component> EntityEditor.add(component: T) { add(T::class, component) }
    inline fun <reified T: Component> EntityEditor.remove() = remove(T::class)
}

interface Context {
    fun manage(entity: Entity): Result<EntityEditor, EntityNotFoundError>
    fun changeSet(edit: EditScope.() -> Unit)
}

interface WorldContext: Context
interface SystemContext: Context {
    fun entities(): List<Entity>
}

data class Aspect(val included: List<KClass<out Component>>, val excluded: List<KClass<out Component>>)

interface System {
    val aspect: Aspect
    
    suspend fun CoroutineScope.process(duration: Double, systemContext: SystemContext)
}

class World {
    fun step(duration: Double) {  }
}
```
### LifeCycle
```kotlin
fun tick(duration: Double) = runBlocking {
    val job = launch(ourContext) {
        currentState?.let {
            with(it) {
                this@launch.update(duration)
            }
        }
        
        for (layer in layers) {
            with(layer) {
                this@launch.execute(duration, contextProvider)
                worldContext.resolveChangeSets()
            }
        }
    }
    
    job.join()
    changeSetResolver.join()
}
```


### StateManager
```kotlin
data class Progress(val progress: Double, val status: String)

interface State {
    suspend fun ProducerScope<Progress>.init() 
}
```

### Editor specific APIs
TODO

### Entry Point
TODO

### Project Structure
TODO
