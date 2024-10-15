package edu.ucne.registroprioridades.presentation.prioridad

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import edu.ucne.registroprioridades.presentation.components.TopBarComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun PrioridadListScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    onEdit: (Int) -> Unit,
    onAddPrioridad: () -> Unit,
    onDrawer: () -> Unit
)  {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadListBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onEdit = onEdit,
        onAddPrioridad = onAddPrioridad,
        onDrawer = onDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioridadListBody(
    uiState: UiState,
    onEvent: (PrioridadEvent) -> Unit,
    onEdit: (Int) -> Unit,
    onAddPrioridad: () -> Unit,
    onDrawer: () -> Unit
) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                title = "Prioridades",
                onMenuClick = { onDrawer() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPrioridad,
                containerColor = Color(0xFF415f91),
                contentColor = Color(0xFFd6e3ff)
            ) {
                Icon(Icons.Filled.Add, "Agregar nueva entidad")
            }
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(uiState.prioridades) { prioridad ->

                    val coroutineScope = rememberCoroutineScope()
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { state ->
                            if (state == SwipeToDismissBoxValue.EndToStart) {
                                coroutineScope.launch {
                                    delay(0.5.seconds)
                                    onEvent(PrioridadEvent.Delete(prioridad.prioridadId))
                                }
                                true
                            } else {
                                false
                            }
                        }
                    )
                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        backgroundContent = {
                            val color by animateColorAsState(
                                when (dismissState.targetValue) {
                                    SwipeToDismissBoxValue.Settled -> Color.Transparent
                                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFCB4238)
                                    SwipeToDismissBoxValue.StartToEnd -> TODO()
                                }, label = "Changing color"
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color, shape = RoundedCornerShape(8.dp))
                                    .padding(16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                        },
                        modifier = Modifier
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    prioridad.prioridadId?.let { it1 -> onEdit(prioridad.prioridadId) }
                                }
                                .border(1.dp, Color(0xFF001b3e), RoundedCornerShape(8.dp)),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(
                                Color(0xFFd6e3ff),
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
                                        color = Color(0xFF001b3e)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Días Compromiso: ${prioridad.diasCompromiso?.toString() ?: "N/A"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF565f71),
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                            }
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 35.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                            )
                        }
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
        uiState = UiState(prioridades = prioridades),
        onEvent = {},
        onEdit = {},
        onAddPrioridad = {},
        onDrawer = {}
    )
}