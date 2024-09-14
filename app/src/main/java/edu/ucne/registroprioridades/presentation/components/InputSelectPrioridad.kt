package edu.ucne.registroprioridades.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.unit.dp
import edu.ucne.registroprioridades.data.local.entities.PrioridadEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSelectPrioridad(
    prioridades: List<PrioridadEntity>,
    value: Int?,
    text: String,
    error: Boolean,
    errorMessage: String,
    onChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedPriority = prioridades.find { it.prioridadId == value }?.descripcion ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedPriority,
            onValueChange = {},
            label = { Text(text) },
            isError = error,
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
            prioridades.forEach { prioridad ->
                DropdownMenuItem(
                    text = { Text(prioridad.descripcion) },
                    onClick = {
                        prioridad.prioridadId?.let { onChange(it) }
                        expanded = false
                    }
                )
            }
        }
    }
    if (error) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
