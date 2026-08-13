package com.ridenorth.driver.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class DriverScreen(val route: String) {
    object Splash : DriverScreen("splash")
    object Auth : DriverScreen("auth")
    object Dashboard : DriverScreen("dashboard")
    object Jobs : DriverScreen("jobs")
    object Earnings : DriverScreen("earnings")
}

@Composable
fun DriverApp() {
    val navController = rememberNavController()
    var isAuthenticated by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) DriverScreen.Dashboard.route else DriverScreen.Splash.route
    ) {
        composable(DriverScreen.Splash.route) {
            DriverSplashScreen(onNavigateToAuth = {
                isAuthenticated = false
                navController.navigate(DriverScreen.Auth.route) {
                    popUpTo(DriverScreen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(DriverScreen.Auth.route) {
            DriverAuthScreen(onAuthSuccess = {
                isAuthenticated = true
                navController.navigate(DriverScreen.Dashboard.route) {
                    popUpTo(DriverScreen.Auth.route) { inclusive = true }
                }
            })
        }
        composable(DriverScreen.Dashboard.route) {
            DriverDashboardScreen(
                onNavigateToJobs = { navController.navigate(DriverScreen.Jobs.route) },
                onNavigateToEarnings = { navController.navigate(DriverScreen.Earnings.route) }
            )
        }
        composable(DriverScreen.Jobs.route) {
            DriverJobsScreen(onBack = { navController.popBackStack() })
        }
        composable(DriverScreen.Earnings.route) {
            DriverEarningsScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun DriverSplashScreen(onNavigateToAuth: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "RideNorth Driver", modifier = Modifier.padding(16.dp))
        Text(text = "Start Earning Today", modifier = Modifier.padding(bottom = 32.dp))
        Button(onClick = onNavigateToAuth) {
            Text("Login")
        }
    }
}

@Composable
fun DriverAuthScreen(onAuthSuccess: () -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var step by remember { mutableStateOf("phone") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (step == "phone") {
            Text(text = "Driver Login", modifier = Modifier.padding(16.dp))
            androidx.compose.material3.OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Button(onClick = { step = "otp" }, modifier = Modifier.padding(16.dp)) {
                Text("Request OTP")
            }
        } else {
            Text(text = "Enter OTP", modifier = Modifier.padding(16.dp))
            androidx.compose.material3.OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("OTP") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
            Button(onClick = onAuthSuccess, modifier = Modifier.padding(16.dp)) {
                Text("Verify & Continue")
            }
        }
    }
}

@Composable
fun DriverDashboardScreen(onNavigateToJobs: () -> Unit, onNavigateToEarnings: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Driver Dashboard", modifier = Modifier.padding(16.dp))
        Text(text = "Status: Online", modifier = Modifier.padding(8.dp))
        Text(text = "Today's Earnings: UGX 45,000", modifier = Modifier.padding(8.dp))
        Button(onClick = onNavigateToJobs, modifier = Modifier.padding(16.dp)) {
            Text("Available Jobs")
        }
        Button(onClick = onNavigateToEarnings, modifier = Modifier.padding(16.dp)) {
            Text("My Earnings")
        }
    }
}

@Composable
fun DriverJobsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Available Jobs", modifier = Modifier.padding(16.dp))
        Text(text = "No new job requests", modifier = Modifier.padding(8.dp))
        Button(onClick = onBack, modifier = Modifier.padding(16.dp)) {
            Text("Back")
        }
    }
}

@Composable
fun DriverEarningsScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Earnings", modifier = Modifier.padding(16.dp))
        Text(text = "Today: UGX 45,000", modifier = Modifier.padding(8.dp))
        Text(text = "This Week: UGX 320,000", modifier = Modifier.padding(8.dp))
        Text(text = "Wallet Balance: UGX 180,000", modifier = Modifier.padding(8.dp))
        Button(onClick = onBack, modifier = Modifier.padding(16.dp)) {
            Text("Back")
        }
    }
}
