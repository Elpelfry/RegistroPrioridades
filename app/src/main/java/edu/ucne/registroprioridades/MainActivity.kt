package edu.ucne.registroprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import edu.ucne.registroprioridades.data.local.database.PrioridadDb
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity
import edu.ucne.registroprioridades.ui.theme.RegistroPrioridadesTheme

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
    fun GreetingPreview() {
        RegistroPrioridadesTheme {
            PrioridadScreen()
        }
    }
}