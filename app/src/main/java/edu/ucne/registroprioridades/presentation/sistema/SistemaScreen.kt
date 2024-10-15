package edu.ucne.registroprioridades.presentation.sistema

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroprioridades.presentation.components.TextFieldComponent
import edu.ucne.registroprioridades.presentation.components.TopBarComponent

@Composable
fun SistemaScreen(
    viewModel: SistemaViewModel = hiltViewModel(),
    sistemaId: Int?,
    goSistemaList: () -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        if (sistemaId != null && sistemaId != 0) {
            viewModel.onEvent(SistemaEvent.SelectSistema(sistemaId))
        }
    }
    SistemaBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goSistemaList = goSistemaList,
        onDrawer= onDrawer
    )
}

@Composable
fun SistemaBody(
    uiState: UiState,
    onEvent: (SistemaEvent) -> Unit,
    goSistemaList: () -> Unit,
    onDrawer: () -> Unit

) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                title = "Registrar Sistema",
                onMenuClick = { onDrawer() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = Color(0xFF415f91)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                TextFieldComponent(
                    value = uiState.nombre,
                    text = "Nombre",
                    error = (uiState.errorNombre != ""),
                    errorMessage = uiState.errorNombre,
                    onChange = { onEvent(SistemaEvent.NombreChange(it)) }
                )
                TextFieldComponent(
                    value = uiState.descripcion,
                    text = "Descripción",
                    error = (uiState.errorDescripcion != ""),
                    errorMessage = uiState.errorDescripcion,
                    onChange = { onEvent(SistemaEvent.DescripcionChange(it)) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onEvent(SistemaEvent.New)
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF565f71)),
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "New"
                    )
                    Text(text = "Nuevo")
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xFF415f91)),
                    onClick = {
                        onEvent(SistemaEvent.Validation)
                        if(uiState.validation){
                            onEvent(SistemaEvent.Save)
                            goSistemaList()
                        }
                    },
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                    Text(text = "Guardar")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                if(uiState.errorMessage.isNotEmpty()){
                    Toast.makeText(
                        LocalContext.current,
                        uiState.errorMessage,
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SistemaScreenPreview() {
    SistemaBody(
        uiState = UiState(),
        onEvent = {},
        goSistemaList = {},
        onDrawer = {}
    )
}