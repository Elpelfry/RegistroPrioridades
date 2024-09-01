package edu.ucne.registroprioridades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import edu.ucne.registroprioridades.data.local.database.PrioridadDb
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


    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        RegistroPrioridadesTheme {
            PrioridadScreen()
        }
    }
}