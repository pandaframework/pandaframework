## API

The engine is ECS based (Entity - Component - System) with a very simple state machine implementation.

### Result, Option
```kotlin
sealed class Option<T: Any> {
    class Some<T: Any> (val result: T): Option<T>()
    object None: Option<Unit>()
}

sealed class Result<T: Any, E: Throwable> {
    class Some<T: Any>(val result: T): Result<T, Throwable>()
    class Error<E: Throwable>(val error: E): Result<Unit, E>()
}

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

### Entry Point
TODO

### Project Structure
TODO
