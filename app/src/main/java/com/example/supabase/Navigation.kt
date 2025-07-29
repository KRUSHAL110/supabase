package com.example.supabase

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(onStartCall: Function<Unit>) {
    val navController: NavHostController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Access the Application instance from CompositionLocal
    val application = LocalAppInstance.current

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                authViewModel = authViewModel
            )
        }

        composable("signup") {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.popBackStack("login", inclusive = false)
                    true
                },
                authViewModel = authViewModel
            )
        }

        composable("home") {
            HomeScreen(
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onGoToChat = {
                    navController.navigate("chat_list")
                },
                onGoToCall = {
                    navController.navigate("dialer")
                }
            )
        }

        composable("chat_list") {
            ChatListAndChatScreenController()
        }

        composable("dialer") {
            DialerScreen(
                onStartCall = { selectedNumber ->
                    LinphoneManager.registerSipAccount(
                        application = application,
                        username = "user1",
                        password = "password1",
                        domain = "192.168.7.120"
                    )
                    navController.navigate("call/$selectedNumber")
                }
            )
        }

        composable("call/{callee}") { backStackEntry ->
            val callee = backStackEntry.arguments?.getString("callee") ?: "Unknown"
            CallScreen(
                calleeName = callee,
                onEndCall = {
                    LinphoneManager.endCall()
                    navController.popBackStack()
                }
            )
        }
    }
}
