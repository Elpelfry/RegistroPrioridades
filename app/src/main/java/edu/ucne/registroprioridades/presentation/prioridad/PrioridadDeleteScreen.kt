package edu.ucne.registroprioridades.presentation.prioridad

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PrioridadDeleteScreen(
    viewModel: PrioridadViewModel,
    goPrioridadList: () -> Unit
 ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.prioridad.collectAsStateWithLifecycle()

    PrioridadDeleteBody(
        uiState = uiState,
        goPrioridadList = goPrioridadList,
        onDelete = viewModel::deletePrioridad
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadDeleteBody(
    uiState: PrioridadUIState,
    goPrioridadList: () -> Unit,
    onDelete:() -> Unit
) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Eliminar Prioridad",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFb42f2f),
                ) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            goPrioridadList()
                        }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Lista"
                        )
                    }
                },

            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(0.5.dp, Color(0xFF0275d8), RoundedCornerShape(8.dp)),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(
                    Color(0xFFf4eeec),
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(4f),
                    ) {
                        Text(
                            text = "Descripción: ${uiState.descripcion}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Días Compromiso: ${uiState.diasCompromiso?.toString() ?: "N/A"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        goPrioridadList()
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF0275d8))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                    Text(text = "Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    colors = ButtonDefaults.buttonColors(Color(0xFFb42f2f)),
                    onClick = {
                        onDelete()
                        goPrioridadList()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                    Text(text = "Borrar")
                }
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrioridadDeleteScreenPreview() {
    PrioridadDeleteBody(
        uiState = PrioridadUIState(),
        goPrioridadList = {},
        onDelete = {}
    )
}