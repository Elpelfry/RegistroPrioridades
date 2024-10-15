package edu.ucne.registroprioridades.presentation.sistema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registroprioridades.data.remote.dto.SistemaDto
import edu.ucne.registroprioridades.data.repository.SistemaRepository
import edu.ucne.registroprioridades.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
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
            is SistemaEvent.SelectSistema -> selectSistema(event.id)
            SistemaEvent.Save -> saveSistema()
            SistemaEvent.GetSistemas -> getSistemas()
            SistemaEvent.New -> newSistema()
            SistemaEvent.Validation -> uiState.value.validation = validation()
        }
    }
    private fun getSistemas() {
        viewModelScope.launch {
            sistemaRepository.getSistemas().collectLatest { result ->
                when(result){
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                sistemas = result.data ?: emptyList(),
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = it.errorMessage,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun selectSistema(id: Int){
        viewModelScope.launch {
            sistemaRepository.find(id).collectLatest { result ->
                when(result){
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                sistemaId = result.data?.sistemaId,
                                nombre = result.data?.nombre ?: "",
                                descripcion = result.data?.descripcion ?: ""
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteSistema(id: Int){
        viewModelScope.launch {
            sistemaRepository.deleteSistemas(id).let { result ->
                when(result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                sistemas = it.sistemas.filterNot { sistema -> sistema.sistemaId == id },
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                        getSistemas()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
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

    private fun saveSistema() {
        viewModelScope.launch {
            val sistema = uiState.value.toEntity()
            val result = if (uiState.value.sistemaId != null && uiState.value.sistemaId != 0) {
                sistemaRepository.updateSistemas(uiState.value.sistemaId!!, sistema)
            } else {
                sistemaRepository.addSistemas(sistema)
            }
            
            result.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = ""
                            )
                        }
                        getSistemas()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = resource.message ?: "Error desconocido"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun newSistema(){
        _uiState.update {
            it.copy(
                sistemaId = null,
                nombre = "",
                descripcion = "",
                errorNombre = "",
                errorDescripcion = "",
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