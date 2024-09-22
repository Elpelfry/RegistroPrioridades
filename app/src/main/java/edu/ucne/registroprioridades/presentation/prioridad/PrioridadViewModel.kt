package edu.ucne.registroprioridades.presentation.prioridad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import edu.ucne.registroprioridades.data.repository.PrioridadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrioridadViewModel @Inject constructor(
    private val prioridadRepository: PrioridadRepository
) :ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    fun onEvent(event: PrioridadEvent) {
        when (event) {
            is PrioridadEvent.DescripcionChange -> onDescripcionChanged(event.des)
            is PrioridadEvent.DiasChange -> onDiasCompromisoChanged(event.dia)
            is PrioridadEvent.SelectPrioridad -> selectedPrioridad(event.prioridadId)
            PrioridadEvent.Save -> savePrioridad()
            is PrioridadEvent.Delete -> deletePrioridad(event.prioridadId)
            PrioridadEvent.New -> newPrioridad()
            PrioridadEvent.Validation -> uiState.value.validation = validation()
        }
    }

   private fun savePrioridad( ) {
        viewModelScope.launch {
            prioridadRepository.save(uiState.value.toEntity())
            getPrioridades()

        }
    }

    private fun deletePrioridad(prioridadId: Int?) {
        viewModelScope.launch {
            val prioridad = prioridadRepository.find(prioridadId!!)
            if (prioridad != null) {
                prioridadRepository.delete(prioridad)
                getPrioridades()
            }
        }
    }

    private fun newPrioridad() {
        _uiState.update {
            it.copy(
                prioridadId = null,
                descripcion = "",
                diasCompromiso = null,
                errorDescripcion = "",
                errorDias = "",
                validation = false
            )
        }
    }

    private fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getAll().collect{ prioridad ->
                _uiState.update {
                    it.copy(prioridades = prioridad)
                }
            }
        }
    }

    private fun selectedPrioridad(prioridadId: Int){
        viewModelScope.launch {
            if(prioridadId > 0){
                val prioridad = prioridadRepository.find(prioridadId)
                    _uiState.update {
                        it.copy(
                            prioridadId = prioridad?.prioridadId,
                            descripcion = prioridad?.descripcion ?: "",
                            diasCompromiso = prioridad?.diasCompromiso
                        )
                    }

            }
        }
    }

    private fun onDiasCompromisoChanged(diasCompromisoStr: String) {
        val diasCompromiso = diasCompromisoStr.toIntOrNull()
        val errorMessage = if (diasCompromiso == null || diasCompromiso <= 0) {
            "Días debe ser mayor a 0."
        } else null
        _uiState.update {
            it.copy(
                diasCompromiso = diasCompromiso,
                errorDias = errorMessage ?: ""
            )
        }
    }

    private fun onDescripcionChanged(descripcion: String){
        val errorMessage = if (descripcion.isEmpty()) "Descripción obligatoria." else null
        _uiState.update {
            it.copy(
                descripcion = descripcion,
                errorDescripcion = errorMessage ?: ""
            )
        }
    }

    private fun validation(): Boolean {
        var descripcionEmpty = uiState.value.descripcion.isEmpty()
        val diasCompromisoInvalid = uiState.value.diasCompromiso == null || uiState.value.diasCompromiso!! <= 0

        if (descripcionEmpty) {
            _uiState.update { it.copy(errorDescripcion = "Descripción obligatoria.") }
        }
        else{
            descripcionEmpty = _uiState.value.prioridades.
            any{it.descripcion.trim().lowercase() == uiState.value.descripcion.trim().lowercase()
                    && it.prioridadId != uiState.value.prioridadId}
            if (descripcionEmpty) {
                _uiState.update { it.copy(errorDescripcion = "Descripción ya existe.") }
            }
        }
        if (diasCompromisoInvalid) {
            _uiState.update { it.copy(errorDias = "Días debe ser mayor a 0.") }
        }

        return !descripcionEmpty && !diasCompromisoInvalid
    }
}

fun  UiState.toEntity(): PrioridadEntity {
    return PrioridadEntity(
        prioridadId = prioridadId,
        descripcion = descripcion,
        diasCompromiso = diasCompromiso
    )
}