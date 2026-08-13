package com.ridenorth.rider.ui.screen

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

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Home : Screen("home")
    object Booking : Screen("booking")
    object Profile : Screen("profile")
}

@Composable
fun RideNorthApp() {
    val navController = rememberNavController()
    var isAuthenticated by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) Screen.Home.route else Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onNavigateToAuth = {
                isAuthenticated = false
                navController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Auth.route) {
            AuthScreen(onAuthSuccess = {
                isAuthenticated = true
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToBooking = { navController.navigate(Screen.Booking.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }
        composable(Screen.Booking.route) {
            BookingScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Profile.route) {
            ProfileScreen(onLogout = {
                isAuthenticated = false
                navController.navigate(Screen.Auth.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            })
        }
    }
}

@Composable
fun SplashScreen(onNavigateToAuth: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "RideNorth", modifier = Modifier.padding(16.dp))
        Text(text = "Your Mobility Partner for Northern Uganda", modifier = Modifier.padding(bottom = 32.dp))
        Button(onClick = onNavigateToAuth) {
            Text("Get Started")
        }
    }
}

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var step by remember { mutableStateOf("phone") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (step == "phone") {
            Text(text = "Enter Phone Number", modifier = Modifier.padding(16.dp))
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
fun HomeScreen(onNavigateToBooking: () -> Unit, onNavigateToProfile: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "RideNorth", modifier = Modifier.padding(16.dp))
        Text(text = "Where do you want to go?", modifier = Modifier.padding(16.dp))
        Button(onClick = onNavigateToBooking, modifier = Modifier.padding(16.dp)) {
            Text("Book a Ride")
        }
        Button(onClick = onNavigateToProfile, modifier = Modifier.padding(16.dp)) {
            Text("My Profile")
        }
    }
}

@Composable
fun BookingScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Book a Ride", modifier = Modifier.padding(16.dp))
        androidx.compose.material3.OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Pickup Location") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        androidx.compose.material3.OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Dropoff Location") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )
        Button(onClick = { /* TODO: Create ride request */ }, modifier = Modifier.padding(16.dp)) {
            Text("Find Ride")
        }
        Button(onClick = onBack, modifier = Modifier.padding(16.dp)) {
            Text("Back")
        }
    }
}

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Profile", modifier = Modifier.padding(16.dp))
        Text(text = "Phone: +256 700 000 000", modifier = Modifier.padding(8.dp))
        Text(text = "Rating: 4.8", modifier = Modifier.padding(8.dp))
        Button(onClick = onLogout, modifier = Modifier.padding(16.dp)) {
            Text("Logout")
        }
    }
}
