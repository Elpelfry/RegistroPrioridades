package edu.ucne.registroprioridades.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    var badgeCount: Double? = null
)
@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    navPrioridadList: () -> Unit,
    navTicketList: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val items = listOf(
        NavigationItem(
            title = "Prioridades",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        ),
        NavigationItem(
            title = "Tickets",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        )
    )
    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Registro Tickets",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0275d8),
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = if (item == selectedItem.value) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(text = item.title)
                        },
                        selected = item == selectedItem.value,
                        onClick = {
                            selectedItem.value = item
                            scope.launch { drawerState.close() }
                            when (item.title) {
                                "Prioridades" -> {navPrioridadList()}
                                "Tickets" -> {navTicketList()}
                            }
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    ){
        content()
    }
}
