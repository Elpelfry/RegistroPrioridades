package edu.ucne.registroprioridades.presentation.components

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

@Composable
fun TextFieldNumberComponent(
    value: String,
    text: String,
    error: Boolean,
    errorMessage: String,
    onChange: (String) -> Unit
) {
    OutlinedTextField(
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text(text = text) },
        value = value,
        onValueChange = onChange,
        isError = error,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF415f91),
            focusedLabelColor = Color(0xFF415f91),
            cursorColor = Color(0xFF415f91),
            unfocusedLabelColor = Color(0xFF415f91),
            unfocusedBorderColor = Color(0xFF415f91),
        )
    )
    if(error) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage,
            color = Color(0xFFCB4238),
            modifier = Modifier.fillMaxWidth()
        )
    }
}