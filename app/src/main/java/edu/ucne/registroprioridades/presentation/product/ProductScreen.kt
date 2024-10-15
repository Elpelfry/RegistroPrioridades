package edu.ucne.registroprioridades.presentation.product

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registroprioridades.presentation.components.TextFieldComponent
import edu.ucne.registroprioridades.presentation.components.TextFieldNumberComponent
import edu.ucne.registroprioridades.presentation.components.TopBarComponent

@Composable
fun ProductScreen(
    viewModel: ProductViewModel = hiltViewModel(),
    productId: Int?,
    goProductList: () -> Unit,
    onDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        if (productId != null && productId != 0) {
            viewModel.onEvent(ProductEvent.SelectProduct(productId))
        }
    }
    ProductBody(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        goProductList = goProductList,
        onDrawer = onDrawer
    )
}

@Composable
fun ProductBody(
    uiState: UiState,
    onEvent: (ProductEvent) -> Unit,
    goProductList: () -> Unit,
    onDrawer: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarComponent(
                title = "Register Product",
                onMenuClick = { onDrawer() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            item {
                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp),
                            color = Color(0xFF415f91)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {

                    TextFieldComponent(
                        value = uiState.title,
                        text = "Title",
                        error = (uiState.errorTitle != ""),
                        errorMessage = uiState.errorTitle,
                        onChange = { onEvent(ProductEvent.TitleChange(it)) }
                    )

                    TextFieldComponent(
                        value = uiState.description,
                        text = "Description",
                        error = (uiState.errorDescription != ""),
                        errorMessage = uiState.errorDescription,
                        onChange = { onEvent(ProductEvent.DescriptionChange(it)) }
                    )

                    TextFieldNumberComponent(
                        value = uiState.price.toString(),
                        text = "Price",
                        error = (uiState.errorPrice != ""),
                        errorMessage = uiState.errorPrice,
                        onChange = { onEvent(ProductEvent.PriceChange(it)) }
                    )

                    TextFieldNumberComponent(
                        value = uiState.discount.toString(),
                        text = "Discount",
                        error = (uiState.errorDiscount != ""),
                        errorMessage = uiState.errorDiscount,
                        onChange = { onEvent(ProductEvent.DiscountChange(it)) }
                    )

                    TextFieldNumberComponent(
                        value = uiState.units.toString(),
                        text = "Units",
                        error = (uiState.errorUnits != ""),
                        errorMessage = uiState.errorUnits,
                        onChange = { onEvent(ProductEvent.UnitsChange(it)) }
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onEvent(ProductEvent.New)
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF565f71)),
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "New"
                        )
                        Text(text = "New")
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        colors = ButtonDefaults.buttonColors(Color(0xFF415f91)),
                        onClick = {
                            onEvent(ProductEvent.Validation)
                            if (uiState.validation) {
                                onEvent(ProductEvent.Save)
                                goProductList()
                            }
                        },
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                        Text(text = "Save")
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    if (uiState.errorMessage.isNotEmpty()) {
                        Toast.makeText(
                            LocalContext.current,
                            uiState.errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductScreenPreview() {
    ProductBody(
        uiState = UiState(),
        onEvent = {},
        goProductList = {},
        onDrawer = {}
    )
}