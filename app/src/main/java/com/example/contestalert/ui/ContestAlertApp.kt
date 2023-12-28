@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.contestalert.ui


import com.example.contestalert.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contestalert.ui.screens.ContestViewModel
import com.example.contestalert.ui.screens.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContestAlertApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val contestViewModel: ContestViewModel =
                viewModel(factory = ContestViewModel.Factory)
            HomeScreen(
                contestUiState = contestViewModel.contestUiState,
                retryAction = contestViewModel::getAlert,
                modifier = Modifier.fillMaxSize(),
                contentPadding = it
            )
        }
    }
}