package com.zoku.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.zoku.login.LoginScreen
import androidx.navigation.compose.composable
import com.zoku.login.LoginViewModel
import com.zoku.util.ScreenDestinations

fun NavController.navigateToLogin() = navigate(route = ScreenDestinations.login.route)

fun NavGraphBuilder.loginScreen(onLoginSuccess: () -> Unit, viewModel: LoginViewModel) {
    composable(route = ScreenDestinations.login.route) {
        LoginScreen(onLoginSuccess = onLoginSuccess, viewModel)
    }
}