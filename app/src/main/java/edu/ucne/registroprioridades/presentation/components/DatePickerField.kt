package edu.ucne.registroprioridades.presentation.components

import android.app.DatePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun DatePickerField(
    value: String,
    onChange: (String) -> Unit,
    text: String,
    isError: Boolean = false,
    errorMessage: String = "",
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current
    val expanded = remember { mutableStateOf(false) }

    val clickModifier = Modifier.clickable { expanded.value = true }
    Spacer(modifier = Modifier.height(8.dp))
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(clickModifier)
            .border(1.dp, Color(0xFF0275d8), RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = value.ifEmpty { text },
                color = if (value.isEmpty()) Color(0xFF0275d8) else Color.Black,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            trailingIcon?.invoke() ?: Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Calendar Icon",
                tint = Color(0xFF0275d8)
            )
        }
    }
    if (isError) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = errorMessage,
            color = Color.Red,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (expanded.value) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selected = "$dayOfMonth/${month + 1}/$year"
                onChange(selected)
                expanded.value = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        LaunchedEffect(datePickerDialog) {
            datePickerDialog.setOnDismissListener {
                expanded.value = false
            }
            datePickerDialog.show()
        }
    }
}
