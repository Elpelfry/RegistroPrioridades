package edu.ucne.registroprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.Room
import edu.ucne.registroprioridades.data.local.database.PrioridadDb
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import edu.ucne.registroprioridades.ui.theme.RegistroPrioridadesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var prioridadDb: PrioridadDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        prioridadDb = Room.databaseBuilder(
            applicationContext,
            PrioridadDb::class.java,
            "Prioridad.db"
        ).fallbackToDestructiveMigration()
            .build()

        setContent {
            RegistroPrioridadesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        PrioridadScreen()
                    }
                }
            }
        }
    }

    @Composable
    fun PrioridadScreen() {

        var descripcion by remember { mutableStateOf("") }
        var diasCompromiso by remember { mutableStateOf("") }
        var errorMessage: String? by remember { mutableStateOf(null) }
        val focusManager = LocalFocusManager.current

        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
                    .clickable { focusManager.clearFocus() },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Prioridades",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0275d8)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    DescripcionTextField(
                        descripcion = descripcion,
                        onDescripcionChange = { descripcion = it }
                    )
                    DiasCompromisoTextField(
                        diasCompromiso = diasCompromiso,
                        onDiasCompromisoChange = { diasCompromiso = it }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                errorMessage?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            descripcion = ""
                            diasCompromiso = ""
                            errorMessage = ""
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
                    val scope = rememberCoroutineScope()

                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0xFF198754)),
                        onClick = {

                            scope.launch {
                                val existingPrioridad = prioridadDb.prioridadDao().findByDescription(descripcion)
                                val dias = diasCompromiso.toIntOrNull()

                                if (existingPrioridad != null) {
                                    errorMessage = "Ya existe esta descripción"
                                    return@launch
                                }else if (descripcion.isBlank() || diasCompromiso.isBlank()) {
                                    errorMessage = "Por favor, complete todos los campos"
                                    return@launch
                                }else if (dias == null) {
                                    errorMessage = "El número de días debe ser un número entero"
                                    return@launch
                                }else{
                                    savePrioridad(
                                        PrioridadEntity(
                                            descripcion = descripcion,
                                            diasCompromiso = dias
                                        )
                                    )
                                    descripcion = ""
                                    diasCompromiso = ""
                                }
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ){
                    val lifecycleOwner = LocalLifecycleOwner.current
                    val prioridadList by prioridadDb.prioridadDao().getall()
                        .collectAsStateWithLifecycle(
                            initialValue = emptyList(),
                            lifecycleOwner = lifecycleOwner,
                            minActiveState = Lifecycle.State.STARTED
                        )
                    PrioridadListScreen(prioridadList)
                }
            }
        }
    }

    @Composable
    fun PrioridadListScreen(prioridadList: List<PrioridadEntity>) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                fontWeight = FontWeight.Bold,
                text = "Lista de Prioridades",
                style = MaterialTheme.typography.bodyLarge,

                )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    text = "Descripción",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    fontWeight = FontWeight.Bold,
                    text = "Días Compromiso",
                    style = MaterialTheme.typography.bodyMedium
                )

            }
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(prioridadList) { prioridad ->
                    PrioridadRow(prioridad)
                }
            }
        }
    }

    @Composable
    private fun PrioridadRow(prioridad: PrioridadEntity) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(4f),
                text = prioridad.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = prioridad.diasCompromiso?.toString() ?: "N/A",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        HorizontalDivider()
    }

    private suspend fun savePrioridad(prioridad: PrioridadEntity) {
        prioridadDb.prioridadDao().save(prioridad)
    }

    @Composable
    fun DescripcionTextField(
        descripcion: String,
        onDescripcionChange: (String) -> Unit
    ) {
        OutlinedTextField(
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            label = { Text(text = "Descripción") },
            value = descripcion,
            onValueChange = { onDescripcionChange(it) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0275d8),
                focusedLabelColor = Color(0xFF0275d8),
                cursorColor = Color(0xFF0275d8)
            )
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DiasCompromisoTextField(
        diasCompromiso: String,
        onDiasCompromisoChange: (String) -> Unit
    ) {

        val rango = (1..31).map { it.toString() }
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = diasCompromiso,
                onValueChange = { onDiasCompromisoChange(it) },
                label = { Text("Días Compromiso") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0275d8),
                    focusedLabelColor = Color(0xFF0275d8)
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.heightIn(max = 170.dp)
            ) {
                rango.forEach { dia ->
                    DropdownMenuItem(
                        text = { Text(dia) },
                        onClick = {
                            onDiasCompromisoChange(dia)
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun PrioridadScreenPreview() {
        RegistroPrioridadesTheme {
            PrioridadScreen()
        }
    }
}