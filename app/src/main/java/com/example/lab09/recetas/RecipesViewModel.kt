package com.example.lab09.recetas

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Los tres estados posibles de una pantalla que espera datos de internet
sealed interface RecetasUiState {
    data object Cargando : RecetasUiState
    data class Listo(val recetas: List<Recipe>) : RecetasUiState
    data class Error(val mensaje: String) : RecetasUiState
}

class RecipesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<RecetasUiState>(RecetasUiState.Cargando)
    val uiState: StateFlow<RecetasUiState> = _uiState.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private var trabajoBusqueda: Job? = null

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _uiState.value = RecetasUiState.Cargando
            _uiState.value = try {
                RecetasUiState.Listo(RecipeRepository.search(_busqueda.value))
            } catch (e: Exception) {
                RecetasUiState.Error("No se pudieron cargar las recetas. Revisa tu conexión.")
            }
        }
    }

    // Busca mientras escribo, esperando un poquito para no llamar a la API en cada letra
    fun onBusquedaChange(texto: String) {
        _busqueda.value = texto
        trabajoBusqueda?.cancel()
        trabajoBusqueda = viewModelScope.launch {
            delay(400)
            cargar()
        }
    }
}

sealed interface DetalleUiState {
    data object Cargando : DetalleUiState
    data class Listo(val receta: Recipe) : DetalleUiState
    data class Error(val mensaje: String) : DetalleUiState
}

// El id llega desde la ruta de navegacion "receta/{id}"
class RecipeDetailViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = try {
                DetalleUiState.Listo(RecipeRepository.getRecipe(id))
            } catch (e: Exception) {
                DetalleUiState.Error("No se pudo cargar la receta.")
            }
        }
    }
}
