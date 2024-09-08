package edu.ucne.registroprioridades.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.ucne.registroprioridades.presentation.prioridad.PrioridadUIState

@Composable
fun DescripcionTextField(
    uiState: PrioridadUIState,
    onDescripcionChange: (String) -> Unit
) {
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
        label = { Text(text = "Descripción") },
        value = uiState.descripcion,
        onValueChange = onDescripcionChange,
        isError = (uiState.errorDescripcion != ""),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF0275d8),
            focusedLabelColor = Color(0xFF0275d8),
            cursorColor = Color(0xFF0275d8),
            unfocusedLabelColor = Color(0xFF0275d8),
            unfocusedBorderColor = Color(0xFF0275d8),
        )
    )
    if(uiState.errorDescripcion != "") {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = uiState.errorDescripcion,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth()

        )
    }
}