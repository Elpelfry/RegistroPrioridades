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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroprioridades.presentation.components.DescripcionTextField
import edu.ucne.registroprioridades.presentation.components.DiasCompromisoTextField

@Composable
fun PrioridadScreen(
    viewModel: PrioridadViewModel,
    goPrioridadList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.prioridad.collectAsStateWithLifecycle()

    PrioridadBody(
        uiState = uiState,
        onDiasCompromisoChanged = viewModel::onDiasCompromisoChanged,
        onDescripcionChanged = viewModel::onDescripcionChanged,
        onSavePrioridad = {
           viewModel.savePrioridad()
        },
        onNewPrioridad = {
            viewModel.newPrioridad()
        },
        goPrioridadList = goPrioridadList,
        onValidation = viewModel::validation
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadBody(
    uiState: PrioridadUIState,
    onDiasCompromisoChanged: (String) -> Unit,
    onDescripcionChanged: (String) -> Unit,
    onSavePrioridad: () -> Unit,
    onNewPrioridad: () -> Unit,
    goPrioridadList: () -> Unit,
    onValidation: () -> Boolean

) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Registro Prioridades",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0275d8)
                ) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNewPrioridad()
                            goPrioridadList()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Lista"
                        )
                    }
                }
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

                DescripcionTextField(
                    uiState = uiState,
                    onDescripcionChange = onDescripcionChanged
                )

                DiasCompromisoTextField(
                    uiState = uiState,
                    onDiasCompromisoChange = onDiasCompromisoChanged
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        onNewPrioridad()
                    },
                    colors = ButtonDefaults.buttonColors(Color.DarkGray)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "New"
                    )
                    Text(text = "Nuevo")
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xFF198754)),
                    onClick = {
                        if(onValidation()){
                            onSavePrioridad()
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
        uiState = PrioridadUIState(),
        onDiasCompromisoChanged = {},
        onDescripcionChanged = {},
        onSavePrioridad = {},
        onNewPrioridad = {},
        goPrioridadList = {},
        onValidation = {false}
    )
}