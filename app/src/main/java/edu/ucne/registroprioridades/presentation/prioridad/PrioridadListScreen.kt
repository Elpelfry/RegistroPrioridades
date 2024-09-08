package edu.ucne.registroprioridades.presentation.prioridad

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
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
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity

@Composable
fun PrioridadListScreen(
    viewModel: PrioridadViewModel,
    prioridadList: List<PrioridadEntity>,
    onEdit: (PrioridadEntity) -> Unit,
    onAddPrioridad: () -> Unit,
    onDeletePrioridad:(PrioridadEntity) -> Unit
)  {
    val prioridades by viewModel.prioridad.collectAsStateWithLifecycle()

    PrioridadListBody(
        prioridadList = prioridadList,
        onEdit = onEdit,
        onAddPrioridad = onAddPrioridad,
        onDeletePrioridad = onDeletePrioridad
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListBody(
    prioridadList: List<PrioridadEntity>,
    onEdit: (PrioridadEntity) -> Unit,
    onAddPrioridad: () -> Unit,
    onDeletePrioridad:(PrioridadEntity) -> Unit
) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Prioridades",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0275d8)
                        )
                    }

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPrioridad,
                containerColor = Color(0xFF0275d8),
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Agregar nueva entidad")
            }
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(prioridadList) { prioridad ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                prioridad.prioridadId?.let { it1 -> onEdit(prioridad) }
                            }
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
                                    text = prioridad.descripcion,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0275d8)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Días Compromiso: ${prioridad.diasCompromiso?.toString() ?: "N/A"}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            IconButton(
                                onClick = {
                                    prioridad.prioridadId?.let { it1 -> onDeletePrioridad(prioridad) }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar prioridad",
                                    tint = Color(0xFFb42f2f)
                                )
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PrioridadListPreview() {

    val prioridades = listOf(
        PrioridadEntity(
            prioridadId = 1,
            descripcion = "Alta",
            diasCompromiso = 1
        ),
        PrioridadEntity(
            prioridadId = 2,
            descripcion = "Media",
            diasCompromiso = 3
        ),
        PrioridadEntity(
            prioridadId = 3,
            descripcion = "Baja",
            diasCompromiso = 7
        )
    )
    PrioridadListBody(
        prioridadList = prioridades,
        onEdit = {},
        onAddPrioridad = {},
        onDeletePrioridad = {}
    )
}