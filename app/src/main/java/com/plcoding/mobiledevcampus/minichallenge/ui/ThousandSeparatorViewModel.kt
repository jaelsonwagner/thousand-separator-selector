package com.plcoding.mobiledevcampus.minichallenge.ui

import androidx.lifecycle.ViewModel
import com.plcoding.mobiledevcampus.minichallenge.domain.ThousandSeparator
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


internal data class UiState(
    val thousandSeparatorOptions: ImmutableSet<ThousandSeparator> = persistentSetOf<ThousandSeparator>(),
    val selectedThousandSeparator: ThousandSeparator? = null
)

class ThousandSeparatorViewModel : ViewModel() {
    private val staticThousandSeparatorOptions = persistentSetOf(
        ThousandSeparator("1.000"),
        ThousandSeparator("1,000"),
        ThousandSeparator("1 000"),
    )

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            thousandSeparatorOptions = staticThousandSeparatorOptions,
            selectedThousandSeparator = staticThousandSeparatorOptions.first()
        )
    )

    internal val uiState: StateFlow<UiState> = _uiState

    internal fun setSelectedThousandSeparator(thousandSeparator: ThousandSeparator) {
        _uiState.update {
            uiState.value.copy(selectedThousandSeparator = thousandSeparator)
        }
    }
}