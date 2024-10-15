@file:Suppress("UNREACHABLE_CODE")

package edu.ucne.registroprioridades.presentation.prioridad

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroprioridades.presentation.components.TextFieldComponent
import edu.ucne.registroprioridades.presentation.components.DiasCompromisoTextField
import edu.ucne.registroprioridades.presentation.components.TopBarComponent

@Composable
fun PrioridadScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    prioridadId: Int?,
    goPrioridadList: () -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        if (prioridadId != null) {
            viewModel.onEvent(PrioridadEvent.SelectPrioridad(prioridadId))
        }
    }
    PrioridadBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goPrioridadList = goPrioridadList,
        onDrawer= onDrawer
    )
}

@Composable
fun PrioridadBody(
    uiState: UiState,
    onEvent: (PrioridadEvent) -> Unit,
    goPrioridadList: () -> Unit,
    onDrawer: () -> Unit

) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                title = "Registrar Prioridad",
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {

                TextFieldComponent(
                    value = uiState.descripcion,
                    text = "Descripción",
                    error = (uiState.errorDescripcion != ""),
                    errorMessage = uiState.errorDescripcion,
                    onChange = { onEvent(PrioridadEvent.DescripcionChange(it)) }
                )

                DiasCompromisoTextField(
                    uiState = uiState,
                    onChange = { onEvent(PrioridadEvent.DiasChange(it)) }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onEvent(PrioridadEvent.New)
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF565f71))
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
                        onEvent(PrioridadEvent.Validation)
                        if(uiState.validation){
                            onEvent(PrioridadEvent.Save)
                            goPrioridadList()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                    Text(text = "Guardar")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrioridadScreenPreview() {
    PrioridadBody(
        uiState = UiState(),
        onEvent = {},
        goPrioridadList = {},
        onDrawer = {}
    )
}