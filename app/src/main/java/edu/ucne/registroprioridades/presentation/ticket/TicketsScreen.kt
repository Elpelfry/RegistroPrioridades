package edu.ucne.registroprioridades.presentation.ticket

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import edu.ucne.registroprioridades.presentation.components.DatePickerField
import edu.ucne.registroprioridades.presentation.components.InputSelectPrioridad
import edu.ucne.registroprioridades.presentation.components.TextFieldComponent
import edu.ucne.registroprioridades.presentation.components.TopBarComponent

@Composable
fun TicketScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    ticketId: Int?,
    goTicketList: () -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        if (ticketId != null) {
            viewModel.onEvent(TicketEvent.SelectTicket(ticketId))
        }
    }
    TicketBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goTicketList = goTicketList,
        onDrawer = onDrawer
    )
}

@Composable
fun TicketBody(
    uiState: UiState,
    onEvent: (TicketEvent) -> Unit,
    goTicketList: () -> Unit,
    onDrawer: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                title = "Registrar Ticket",
                onMenuClick = { onDrawer() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextFieldComponent(
                        value = uiState.cliente,
                        text = "Cliente",
                        error = (uiState.errorCliente != ""),
                        errorMessage = uiState.errorCliente,
                        onChange = { onEvent(TicketEvent.ClienteChange(it)) }
                    )
                    TextFieldComponent(
                        value = uiState.asunto,
                        text = "Asunto",
                        error = (uiState.errorAsunto != ""),
                        errorMessage = uiState.errorAsunto,
                        onChange = { onEvent(TicketEvent.AsuntoChange(it)) }
                    )
                    TextFieldComponent(
                        value = uiState.descripcion,
                        text = "Descripción",
                        error = (uiState.errorDescripcion != ""),
                        errorMessage = uiState.errorDescripcion,
                        onChange = { onEvent(TicketEvent.DescripcionChange(it)) }
                    )
                    DatePickerField(
                        value = uiState.fecha,
                        onChange = { onEvent(TicketEvent.FechaChange(it)) },
                        text = "Fecha",
                        isError = (uiState.errorFecha != ""),
                        errorMessage = uiState.errorFecha
                    )
                    InputSelectPrioridad(
                        prioridades = uiState.prioridades,
                        value = uiState.prioridadId,
                        text = "Prioridad",
                        error = (uiState.errorPrioridad != ""),
                        errorMessage = uiState.errorPrioridad,
                        onChange = { onEvent(TicketEvent.PrioridadChange(it.toString())) }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onEvent(TicketEvent.New)
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
                            onEvent(TicketEvent.Validation)
                            if (uiState.validation) {
                                onEvent(TicketEvent.Save)
                                goTicketList()
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TicketScreenPreview() {
    TicketBody(
        uiState = UiState(),
        onEvent = {},
        goTicketList = {},
        onDrawer = {}
    )
}