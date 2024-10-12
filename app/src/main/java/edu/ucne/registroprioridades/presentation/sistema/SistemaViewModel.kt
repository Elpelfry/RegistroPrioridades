package edu.ucne.registroprioridades.presentation.sistema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
import edu.ucne.registroprioridades.data.repository.SistemaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SistemaViewModel @Inject constructor(
    private val sistemaRepository: SistemaRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getSistemas()
    }

    fun onEvent(event: SistemaEvent){
        when(event){
            is SistemaEvent.DescripcionChange -> onDescripcionChanged(event.des)
            is SistemaEvent.NombreChange -> onNombreChanged(event.nom)
            is SistemaEvent.Delete -> deleteSistema(event.id)
            is SistemaEvent.Edit -> editSistema(event.id, event.uiState)
            is SistemaEvent.SelectSistema -> selectSistema(event.id)
            SistemaEvent.Save -> saveSistema()
            SistemaEvent.GetSistemas -> getSistemas()
            SistemaEvent.New -> newSistema()
            SistemaEvent.Validation -> uiState.value.validation = validation()
        }
    }

    private fun getSistemas() {
        viewModelScope.launch {
            try {

                val sistemas = sistemaRepository.getSistemas()
                _uiState.update { it.copy(sistemas = sistemas) }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun selectSistema(id: Int){
        viewModelScope.launch {
            val sistema = sistemaRepository.getSistemas().find { it.sistemaId == id }
            _uiState.update {
                it.copy(
                    sistemaId = sistema?.sistemaId,
                    nombre = sistema?.nombre ?: "",
                    descripcion = sistema?.descripcion ?: ""
                )
            }
        }
    }

    private fun editSistema(id: Int, uiState: UiState){
        viewModelScope.launch {
            sistemaRepository.updateSistemas(id, uiState.toEntity())
            getSistemas()
        }
    }

    private fun deleteSistema(id: Int){
        viewModelScope.launch {
            sistemaRepository.deleteSistemas(id)
            getSistemas()
        }
    }

    private fun onDescripcionChanged(descripcion: String){
        _uiState.update {
            it.copy(
                descripcion = descripcion,
                errorDescripcion = if(descripcion.isEmpty()) "Campo descripción requerida" else ""
            )
        }
    }

    private fun onNombreChanged(nombre: String){
        _uiState.update {
            it.copy(
                nombre = nombre,
                errorNombre = if(nombre.isEmpty()) "Campo nombre requerido" else ""
            )
        }
    }

    private fun saveSistema(){
        viewModelScope.launch {
            if(uiState.value.sistemaId != null && uiState.value.sistemaId != 0){
                sistemaRepository.updateSistemas(uiState.value.sistemaId!!, uiState.value.toEntity())
            }else{
                sistemaRepository.addSistemas(uiState.value.toEntity())
            }
        }
    }

    private fun newSistema(){
        _uiState.update {
            it.copy(
                sistemaId = null,
                nombre = "",
                descripcion = ""
            )
        }
    }

    private fun validation() : Boolean{
        var validacion = true
        getSistemas()
        if(uiState.value.nombre.isEmpty()){
            validacion = false
            _uiState.update {
                it.copy(
                    errorNombre = "Campo nombre requerido"
                )
            }
        }
        if(uiState.value.sistemas.any {
            it.nombre.lowercase().trim() == uiState.value.nombre.lowercase().trim()
           && it.sistemaId != uiState.value.sistemaId }){
            validacion = false
            _uiState.update {
                it.copy(
                    errorNombre = "Nombre ya existe"
                )
            }
        }
        if(uiState.value.descripcion.isEmpty()){
            validacion = false
            _uiState.update {
                it.copy(
                    errorDescripcion = "Campo descripción requerida"
                )
            }
        }
        return validacion
    }
}

fun UiState.toEntity() = SistemaDto(
    sistemaId = sistemaId,
    nombre = nombre,
    descripcion = descripcion
)
