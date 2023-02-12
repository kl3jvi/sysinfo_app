package com.kl3jvi.sysinfo.utils

import com.kl3jvi.sysinfo.utils.UiResult.Error
import com.kl3jvi.sysinfo.utils.UiResult.Idle
import com.kl3jvi.sysinfo.utils.UiResult.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Sealed interface used to represent the states of a UI related operation
 * @param T represents the data type that would be emitted in [Success] state
 * [Success] data class which represents the successful state of a UI operation, it contains the result data of type [T].
 * [Error] data class which represents the error state of a UI operation, it contains the [Throwable] instance which caused the error.
 * [Idle] object which represents the initial state of a UI operation, before any data is emitted.
 */
sealed interface UiResult<out T> {
    data class Success<T>(val data: T) : UiResult<T>
    data class Error(val throwable: Throwable) : UiResult<Nothing>
    object Idle : UiResult<Nothing>
}

/**
 * Map this flow of type [T] to [UiResult] of type [T].
 *
 * This function transforms this flow into a flow of [UiResult], where the original values of type [T]
 * are wrapped into a [UiResult.Success].
 *
 * On the start of the flow, an [UiResult.Idle] value is emitted.
 * In case of an error during the flow emission, an [UiResult.Error] value is emitted with the corresponding error.
 *
 * @return a flow of [UiResult] of type [T].
 */
fun <T> Flow<T>.mapToUiState(): Flow<UiResult<T>> {
    return map<T, UiResult<T>> {
        Success(it)
    }.onStart {
        emit(Idle)
    }.catch {
        emit(Error(it))
    }
}
