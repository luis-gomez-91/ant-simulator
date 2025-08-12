package com.luisdev.antsimulator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.compose.AppTheme
import com.luisdev.antsimulator.core.utils.MainViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import com.luisdev.antsimulator.core.navigation.NavigationWrapper

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {
    val mainViewModel: MainViewModel = koinViewModel()
    val themeSelect by mainViewModel.selectedTheme.collectAsState()
    val selectedFontSize by mainViewModel.selectedFontSize.collectAsState()

    AppTheme (
        theme = themeSelect,
        fontSizeOption = selectedFontSize
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            NavigationWrapper()
        }
    }
}