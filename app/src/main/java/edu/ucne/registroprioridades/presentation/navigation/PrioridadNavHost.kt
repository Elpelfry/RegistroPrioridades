package edu.ucne.registroprioridades.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registroprioridades.data.repository.PrioridadRepository
import edu.ucne.registroprioridades.presentation.prioridad.PrioridadDeleteScreen
import edu.ucne.registroprioridades.presentation.prioridad.PrioridadListScreen
import edu.ucne.registroprioridades.presentation.prioridad.PrioridadScreen
import edu.ucne.registroprioridades.presentation.prioridad.PrioridadViewModel

@Composable
fun PrioridadNavHost(
    navController: NavHostController,
    repository: PrioridadRepository
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val prioridadList by repository.getPrioridades()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )
    NavHost(
        navController = navController,
        startDestination = Screen.PrioridadList
    ) {
        composable<Screen.PrioridadList> {
            PrioridadListScreen(
                viewModel = viewModel {
                    PrioridadViewModel(repository,0)
                },
                prioridadList = prioridadList,
                onEdit = {prioridad->
                    navController.navigate(Screen.Prioridad(prioridad.prioridadId?:0))
                },
                onAddPrioridad = {
                    navController.navigate(Screen.Prioridad(0))
                },
                onDeletePrioridad = {prioridad->
                    navController.navigate(Screen.PrioridadDelete(prioridad.prioridadId?:0))
                }
            )
        }
        composable<Screen.Prioridad> {
            val args = it.toRoute<Screen.Prioridad>()
            PrioridadScreen(
                viewModel = viewModel {
                    PrioridadViewModel(repository, args.prioridadId)
                },
                goPrioridadList = {
                    navController.navigateUp()
                }
            )
        }
        composable<Screen.PrioridadDelete>{
            val args = it.toRoute<Screen.PrioridadDelete>()
            PrioridadDeleteScreen(
                viewModel = viewModel {
                    PrioridadViewModel(repository, args.prioridadId)
                },
                goPrioridadList = {
                    navController.navigateUp()
                }
            )
        }
    }
}
