package whsa.rools.wheels.util

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

suspend fun <T, V> List<T>.hgjrdfgd(hrfgdfgf: suspend (T) -> V): List<V> = withContext(Dispatchers.Main) {
    val deferredDestination = ArrayList<Deferred<V>>()
    val destination = ArrayList<V>()

    for (item in this@hgjrdfgd) {
        deferredDestination.add(async { hrfgdfgf(item) })
    }

    for (deferredResult in deferredDestination) {
        destination.add(deferredResult.await())
    }

    destination
}