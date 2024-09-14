package edu.ucne.registroprioridades.presentation.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registroprioridades.presentation.components.NavigationDrawer
import edu.ucne.registroprioridades.presentation.prioridad.PrioridadListScreen
import edu.ucne.registroprioridades.presentation.prioridad.PrioridadScreen
import edu.ucne.registroprioridades.presentation.ticket.TicketListScreen
import edu.ucne.registroprioridades.presentation.ticket.TicketScreen
import kotlinx.coroutines.launch

@Composable
fun TicketNavHost(
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    NavigationDrawer(
        drawerState = drawerState,
        navPrioridadList = { navController.navigate(Screen.PrioridadList) },
        navTicketList = { navController.navigate(Screen.TicketList) }
    ){
        NavHost(
            navController = navController,
            startDestination = Screen.TicketList,
        ) {
            composable<Screen.PrioridadList> {
                PrioridadListScreen(
                    onEdit = {
                        navController.navigate(Screen.Prioridad(it))
                    },
                    onAddPrioridad = {
                        navController.navigate(Screen.Prioridad(0))
                    },
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
            composable<Screen.Prioridad> {
                val args = it.toRoute<Screen.Prioridad>()
                PrioridadScreen(
                    prioridadId = args.prioridadId,
                    goPrioridadList = {
                        navController.navigateUp()
                    },
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
            composable<Screen.TicketList> {
                TicketListScreen(
                    onEdit = {
                        navController.navigate(Screen.Ticket(it))
                    },
                    onAddTicket = {
                        navController.navigate(Screen.Ticket(0))
                    },
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
            composable<Screen.Ticket> {
                val args = it.toRoute<Screen.Ticket>()
                TicketScreen(
                    ticketId = args.ticketId,
                    goTicketList = {
                        navController.navigateUp()
                    },
                    onDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }

        }
    }


}
