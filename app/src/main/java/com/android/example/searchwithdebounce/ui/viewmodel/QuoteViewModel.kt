package com.android.example.searchwithdebounce.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.example.searchwithdebounce.data.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class SearchUiState(
    val isLoading: Boolean = false,
    val result: List<String> = emptyList(),
    val error: String? = null,
)

class QuoteViewModel(
    private val repository: QuoteRepository = QuoteRepository()
) : ViewModel() {

    // 1) User typed query
    private val _query = MutableStateFlow("")
    fun onQueryChanged(q: String) = _query.tryEmit(q)

    // 2) Ui state to expose to the UI
    private val _uiState = MutableStateFlow(value = SearchUiState(isLoading = false))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _query
                .debounce(300L)
                .map { it.trim() }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    flow {
                        emit(SearchUiState(isLoading = true, result = emptyList()))
                        val result = repository.searchQuotes(query = query)
                        emit(SearchUiState(isLoading = false, result = result))
                    }.catch { e ->
                        emit(
                            value = SearchUiState(
                                isLoading = false,
                                result = emptyList(),
                                error = e.message ?: "Unknown"
                            )
                        )
                    }.flowOn(Dispatchers.IO)
                }.collect { state ->
                    _uiState.value = state
                }
        }
        onQueryChanged("")
    }

}