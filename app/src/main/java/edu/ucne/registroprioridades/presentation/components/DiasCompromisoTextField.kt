package edu.ucne.registroprioridades.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.ucne.registroprioridades.presentation.prioridad.PrioridadUIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiasCompromisoTextField(
    uiState: PrioridadUIState,
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
            value = uiState.diasCompromiso?.toString() ?: "",
            onValueChange = onDiasCompromisoChange,
            label = { Text("Días Compromiso") },
            isError = (uiState.errorDias != ""),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF0275d8),
                focusedLabelColor = Color(0xFF0275d8),
                unfocusedLabelColor = Color(0xFF0275d8),
                unfocusedBorderColor = Color(0xFF0275d8),
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
    if(uiState.errorDias != "") {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = uiState.errorDias,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth()
        )
    }
}