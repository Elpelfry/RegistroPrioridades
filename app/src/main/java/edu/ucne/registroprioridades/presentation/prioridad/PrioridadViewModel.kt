package edu.ucne.registroprioridades.presentation.prioridad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import edu.ucne.registroprioridades.data.repository.PrioridadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PrioridadViewModel(
    private val repository: PrioridadRepository,
    private val prioridadId: Int
):ViewModel() {
    var uiState = MutableStateFlow(PrioridadUIState())
        private set

    val prioridad = repository.getPrioridades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            val prioridad = repository.getPrioridad(prioridadId)
            prioridad?.let {
                uiState.update {
                    it.copy(
                        prioridadId = prioridad.prioridadId,
                        descripcion = prioridad.descripcion ?: "",
                        diasCompromiso = prioridad.diasCompromiso
                    )
                }
            }
        }
    }

    fun onDiasCompromisoChanged(diasCompromisoStr: String) {
        val diasCompromiso = diasCompromisoStr.toIntOrNull()
        val errorMessage = if (diasCompromiso == null || diasCompromiso <= 0) {
            "Días debe ser mayor a 0."
        } else null
        uiState.update { currentState ->
            currentState.copy(
                diasCompromiso = diasCompromiso,
                errorDias = errorMessage ?: ""
            )
        }
    }

    fun onDescripcionChanged(descripcion: String){
        val errorMessage = if (descripcion.isEmpty()) "Descripción obligatoria." else null
        uiState.update { currentState ->
            currentState.copy(
                descripcion = descripcion,
                errorDescripcion = errorMessage ?: ""
            )
        }
    }

    fun savePrioridad() {
        viewModelScope.launch {
            repository.savePrioridad(uiState.value.toEntity())
            newPrioridad()
        }
    }

    fun deletePrioridad() {
        viewModelScope.launch {
            repository.deletePrioridad(uiState.value.toEntity())
        }
    }

    fun newPrioridad() {
        viewModelScope.launch {
            uiState.value = PrioridadUIState()
        }
    }

    fun validation(): Boolean {
        var descripcionEmpty = uiState.value.descripcion.isEmpty()
        val diasCompromisoInvalid = uiState.value.diasCompromiso == null || uiState.value.diasCompromiso!! <= 0

        if (descripcionEmpty) {
            uiState.update { it.copy(errorDescripcion = "Descripción obligatoria.") }
        }
        else{
            descripcionEmpty = prioridad.value.
            any{it.descripcion.trim().lowercase() == uiState.value.descripcion.trim().lowercase()
                    && it.prioridadId != uiState.value.prioridadId}
            if (descripcionEmpty) {
                uiState.update { it.copy(errorDescripcion = "Descripción ya existe.") }
            }
        }
        if (diasCompromisoInvalid) {
            uiState.update { it.copy(errorDias = "Días debe ser mayor a 0.") }
        }
        return !descripcionEmpty && !diasCompromisoInvalid
    }
}

data class PrioridadUIState(
    val prioridadId: Int? = null,
    val descripcion: String = "",
    val errorDias: String = "",
    val diasCompromiso: Int? = null,
    val errorDescripcion: String = "",
)

fun  PrioridadUIState.toEntity(): PrioridadEntity {
    return PrioridadEntity(
        prioridadId = prioridadId,
        descripcion = descripcion,
        diasCompromiso = diasCompromiso
    )
}