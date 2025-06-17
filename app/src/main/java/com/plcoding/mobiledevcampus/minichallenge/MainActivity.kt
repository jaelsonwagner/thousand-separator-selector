package com.plcoding.mobiledevcampus.minichallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.mobiledevcampus.minichallenge.ui.ThousandSeparatorViewModel
import com.plcoding.mobiledevcampus.minichallenge.ui.component.ThousandsSeparatorSelector
import com.plcoding.mobiledevcampus.minichallenge.ui.theme.ThousandsSeparatorPickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val viewModel: ThousandSeparatorViewModel by viewModels()

        setContent {
            ThousandsSeparatorPickerTheme(dynamicColor = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

                    ThousandsSeparatorSelector(
                        options = uiState.value.thousandSeparatorOptions,
                        selectedOption = uiState.value.selectedThousandSeparator,
                        onOptionSelected = { viewModel.setSelectedThousandSeparator(it) },
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}