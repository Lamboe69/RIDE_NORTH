@file:OptIn(ExperimentalMaterial3Api::class)

package com.ridenorth.rider.ui.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ridenorth.rider.ui.theme.AmberTint
import com.ridenorth.rider.ui.theme.Danger
import com.ridenorth.rider.ui.theme.Emerald
import com.ridenorth.rider.ui.theme.ForestDark
import com.ridenorth.rider.ui.theme.ForestGreen
import com.ridenorth.rider.ui.theme.Gold
import com.ridenorth.rider.ui.theme.LeafGreen
import com.ridenorth.rider.ui.theme.Mint

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
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    isAuthenticated = false
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

// ---------------- Shared components ----------------

@Composable
private fun BrandMark(size: Int = 48) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.3f).dp))
            .background(
                Brush.linearGradient(listOf(Color.White, Color(0xFFE7F8F5)))
            )
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape((size * 0.3f).dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.LocalTaxi,
            contentDescription = "RideNorth logo",
            tint = ForestGreen,
            modifier = Modifier.size((size * 0.5f).dp)
        )
    }
}

@Composable
private fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    container: List<Color> = listOf(ForestGreen, Emerald)
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color(0xFFCCD8D0),
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        ),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(container), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(text = text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun FeaturePill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.14f))
            .border(1.dp, Color.White.copy(alpha = 0.18f), RoundedCornerShape(50))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ---------------- Splash ----------------

@Composable
fun SplashScreen(onNavigateToAuth: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(ForestDark, ForestGreen, Color(0xFF115E59)))
            )
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BrandMark(size = 96)
            Spacer(Modifier.height(28.dp))
            Text(
                text = "RideNorth",
                color = Color.White,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Your Mobility Partner for Northern Uganda",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(30.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FeaturePill("Rides")
                FeaturePill("Freight")
                FeaturePill("Scheduled")
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Button(
                onClick = onNavigateToAuth,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = ForestGreen
                )
            ) {
                Text("Get Started", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Serving Gulu · Lira · Arua & beyond",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ---------------- Auth ----------------

@Composable
fun AuthScreen(onAuthSuccess: () -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var step by remember { mutableStateOf("phone") }
    var sending by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(248.dp)
                    .background(
                        Brush.verticalGradient(listOf(ForestDark, ForestGreen, Color(0xFF115E59)))
                    )
                    .clip(RoundedCornerShape(bottomStart = 34.dp, bottomEnd = 34.dp))
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .statusBarsPadding()
                        .padding(28.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BrandMark(size = 44)
                        Spacer(Modifier.width(14.dp))
                        Text(
                            text = "RideNorth",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(Modifier.height(22.dp))
                    Text(
                        text = if (step == "phone") "Welcome to RideNorth" else "Verify your number",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (step == "phone")
                            "Enter your phone number to get started"
                        else "Enter the 4-digit code sent to +256 $phoneNumber",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
            ) {
                if (step == "phone") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(Modifier.padding(22.dp)) {
                            Text(
                                text = "Phone Number",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { if (it.length <= 9 && it.all(Char::isDigit)) phoneNumber = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("7XX XXX XXX") },
                                leadingIcon = {
                                    Text(
                                        text = "+256",
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreen,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                shape = RoundedCornerShape(14.dp)
                            )
                            Spacer(Modifier.height(18.dp))
                            GradientButton(
                                text = "Request OTP",
                                onClick = {
                                    sending = true
                                    step = "otp"
                                    sending = false
                                },
                                loading = sending
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Send,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "We'll send a one-time code via SMS",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    OtpCard(
                        otp = otp,
                        onOtpChange = { otp = it },
                        onVerify = onAuthSuccess,
                        onResend = { },
                        onBack = { step = "phone" },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Spacer(Modifier.height(24.dp))
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun OtpCard(
    otp: String,
    onOtpChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(Modifier.padding(22.dp)) {
            Text(
                text = "One-Time Code",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(4) { index ->
                    val char = otp.getOrNull(index)?.toString() ?: ""
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (char.isNotEmpty()) Mint else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (char.isNotEmpty()) ForestGreen else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        BasicTextField(
                            value = char,
                            onValueChange = { new ->
                                val cleaned = new.filter(Char::isDigit)
                                if (cleaned.length <= 1) {
                                    val updated = otp.toCharArray().toMutableList()
                                    if (index < updated.size) updated[index] = cleaned.firstOrNull() ?: ' '
                                    else if (cleaned.isNotEmpty()) updated.add(cleaned.first())
                                    onOtpChange(updated.joinToString("").replace(" ", ""))
                                    if (cleaned.isNotEmpty() && index < 3) {
                                        focusManager.moveFocus(FocusDirection.Next)
                                    } else if (cleaned.isEmpty() && index > 0) {
                                        focusManager.moveFocus(FocusDirection.Previous)
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            textStyle = TextStyle(
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            GradientButton(text = "Verify & Continue", onClick = onVerify)
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Didn't get it? ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(onClick = onResend) {
                    Text("Resend code", color = ForestGreen, fontWeight = FontWeight.Bold)
                }
            }
            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Change number", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

// ---------------- Home ----------------

@Composable
fun HomeScreen(onNavigateToBooking: () -> Unit, onNavigateToProfile: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Filled.LocalTaxi, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ForestGreen,
                        selectedTextColor = ForestGreen,
                        indicatorColor = Mint
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToBooking,
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Book") },
                    label = { Text("Book") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ForestGreen,
                        selectedTextColor = ForestGreen,
                        indicatorColor = Mint
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToProfile,
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ForestGreen,
                        selectedTextColor = ForestGreen,
                        indicatorColor = Mint
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Good morning, Erick",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Where to today?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(ForestGreen, Emerald)))
                        .clickable(onClick = onNavigateToProfile),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Person, contentDescription = "Profile", tint = Color.White)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(218.dp)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                MapPlaceholder()
                Card(
                    onClick = onNavigateToBooking,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(12.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = ForestGreen
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Where are you going?",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            Icons.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                text = "Choose a ride",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VehicleCard(
                    icon = Icons.Filled.TwoWheeler,
                    name = "Boda",
                    eta = "2 min away",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToBooking
                )
                VehicleCard(
                    icon = Icons.Filled.ElectricRickshaw,
                    name = "Tuk Tuk",
                    eta = "3 min away",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToBooking
                )
                VehicleCard(
                    icon = Icons.Filled.DirectionsCar,
                    name = "Car",
                    eta = "5 min away",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToBooking
                )
            }

            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ServiceBanner(
                    icon = Icons.Filled.LocalShipping,
                    title = "Freight & Cargo",
                    subtitle = "Move produce across the north",
                    modifier = Modifier.weight(1f)
                )
                ServiceBanner(
                    icon = Icons.Filled.CalendarMonth,
                    title = "Scheduled Coaches",
                    subtitle = "Gulu → Lira → Kampala",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MapPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(listOf(Color(0xFFE9F2EC), Color(0xFFD8E8DD)))
            )
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val stroke = 14f
            val road = Color.White.copy(alpha = 0.95f)
            drawLine(road, Offset(0f, size.height * 0.42f), Offset(size.width, size.height * 0.28f), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(road, Offset(size.width * 0.30f, 0f), Offset(size.width * 0.20f, size.height), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(road, Offset(size.width * 0.72f, 0f), Offset(size.width * 0.82f, size.height), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(road, Offset(0f, size.height * 0.78f), Offset(size.width, size.height * 0.60f), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(Color(0xFFD2E4D8), Offset(size.width * 0.50f, 0f), Offset(size.width * 0.42f, size.height), strokeWidth = 8f, cap = StrokeCap.Round)

            val greens = listOf(
                Color(0xFFBBD8C5).copy(alpha = 0.7f),
                Color(0xFFC4DECE).copy(alpha = 0.7f)
            )
            drawCircle(greens[0], radius = size.minDimension * 0.16f, center = Offset(size.width * 0.14f, size.height * 0.16f))
            drawCircle(greens[1], radius = size.minDimension * 0.13f, center = Offset(size.width * 0.86f, size.height * 0.85f))
        }

        val pulse = rememberInfiniteTransition(label = "pulse")
        val scale by pulse.animateFloat(
            initialValue = 0.6f,
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(tween(1400, easing = LinearEasing)),
            label = "scale"
        )
        Box(Modifier.align(Alignment.Center), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size((26 * scale).dp)
                    .clip(CircleShape)
                    .background(ForestGreen.copy(alpha = 0.18f))
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(ForestGreen)
                    .border(2.5.dp, Color.White, CircleShape)
            )
        }
    }
}

@Composable
private fun VehicleCard(
    icon: ImageVector,
    name: String,
    eta: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Mint),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = name, tint = ForestGreen, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(eta, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ServiceBanner(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF9EF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AmberTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = Gold, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        }
    }
}

// ---------------- Booking ----------------

@Composable
fun BookingScreen(onBack: () -> Unit) {
    var pickup by remember { mutableStateOf("") }
    var dropoff by remember { mutableStateOf("") }
    var selectedVehicle by remember { mutableStateOf("Boda") }
    var paymentMethod by remember { mutableStateOf("MoMo") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Book a Ride", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Row {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
                        ) {
                            Box(
                                Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreen)
                            )
                            Box(
                                Modifier
                                    .width(2.dp)
                                    .height(26.dp)
                                    .background(ForestGreen.copy(alpha = 0.35f))
                            )
                            Box(
                                Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(Danger)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = pickup,
                                onValueChange = { pickup = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Pickup location") },
                                placeholder = { Text("Current location") },
                                leadingIcon = { Icon(Icons.Filled.MyLocation, contentDescription = null, tint = ForestGreen) },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp)
                            )
                            Spacer(Modifier.height(10.dp))
                            OutlinedTextField(
                                value = dropoff,
                                onValueChange = { dropoff = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text("Dropoff location") },
                                placeholder = { Text("Where are you going?") },
                                leadingIcon = { Icon(Icons.Filled.Place, contentDescription = null, tint = Danger) },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(22.dp))
            Text(
                text = "Select vehicle",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            VehicleOption(
                icon = Icons.Filled.TwoWheeler,
                name = "Boda Boda",
                eta = "Pickup in 2 min",
                fare = "from UGX 1,500",
                selected = selectedVehicle == "Boda",
                onClick = { selectedVehicle = "Boda" }
            )
            Spacer(Modifier.height(10.dp))
            VehicleOption(
                icon = Icons.Filled.ElectricRickshaw,
                name = "Tuk Tuk",
                eta = "Pickup in 3 min",
                fare = "from UGX 2,500",
                selected = selectedVehicle == "Tuk Tuk",
                onClick = { selectedVehicle = "Tuk Tuk" }
            )
            Spacer(Modifier.height(10.dp))
            VehicleOption(
                icon = Icons.Filled.DirectionsCar,
                name = "Private Car",
                eta = "Pickup in 5 min",
                fare = "from UGX 4,000",
                selected = selectedVehicle == "Car",
                onClick = { selectedVehicle = "Car" }
            )

            Spacer(Modifier.height(22.dp))
            Text(
                text = "Payment method",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                PaymentChip(
                    label = "MTN MoMo",
                    selected = paymentMethod == "MoMo",
                    onClick = { paymentMethod = "MoMo" }
                )
                PaymentChip(
                    label = "Cash",
                    icon = Icons.Filled.Money,
                    selected = paymentMethod == "Cash",
                    onClick = { paymentMethod = "Cash" }
                )
            }

            Spacer(Modifier.height(28.dp))
            GradientButton(
                text = "Find Ride",
                onClick = { /* TODO: Create ride request */ }
            )
            Spacer(Modifier.height(6.dp))
            TextButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Back", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun VehicleOption(
    icon: ImageVector,
    name: String,
    eta: String,
    fare: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Mint else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selected) ForestGreen else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = name,
                    tint = if (selected) Color.White else ForestGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(eta, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(fare, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                if (selected) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = "Selected", tint = ForestGreen, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Selected", style = MaterialTheme.typography.labelSmall, color = ForestGreen, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    Box(
                        Modifier
                            .size(18.dp)
                            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label, fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium) },
        leadingIcon = if (icon != null) {
            { Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp)) }
        } else null,
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
            selectedContainerColor = ForestGreen,
            selectedLabelColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

// ---------------- Profile ----------------

@Composable
fun ProfileScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("My Profile", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Brush.verticalGradient(listOf(ForestGreen, Emerald)))
                    .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.18f))
                            .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = "Avatar", tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                    Spacer(Modifier.width(18.dp))
                    Column {
                        Text(
                            text = "Erick Ojok",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text("+256 700 000 000", color = Color.White.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFDE68A),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Spacer(Modifier.width(6.dp))
                            Text("4.8", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-26).dp)
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileStat("Trips", "128", Icons.Filled.LocalTaxi, Modifier.weight(1f))
                ProfileStat("Rating", "4.8", Icons.Filled.Star, Modifier.weight(1f))
                ProfileStat("Wallet", "UGX 25k", Icons.Filled.AccountBalanceWallet, Modifier.weight(1f))
            }

            Spacer(Modifier.height(2.dp))
            Column(Modifier.padding(horizontal = 20.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column {
                        ProfileMenuItem(Icons.Filled.AccountBalanceWallet, "Payment Methods", "MoMo · Cash")
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), modifier = Modifier.padding(start = 58.dp))
                        ProfileMenuItem(Icons.Filled.Place, "Saved Places", "Home · Work · Market")
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), modifier = Modifier.padding(start = 58.dp))
                        ProfileMenuItem(Icons.Filled.NotificationsActive, "Notifications", "Ride alerts & offers")
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), modifier = Modifier.padding(start = 58.dp))
                        ProfileMenuItem(Icons.Filled.HealthAndSafety, "SOS & Safety", "Emergency contacts")
                        Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), modifier = Modifier.padding(start = 58.dp))
                        ProfileMenuItem(Icons.Filled.HelpOutline, "Help & Support", "FAQs, contact us")
                    }
                }

                Spacer(Modifier.height(20.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            tint = Gold,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Text(
                            "Report an issue",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(Modifier.height(20.dp))
                OutlinedButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Danger.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Danger)
                ) {
                    Icon(Icons.Filled.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Logout", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(28.dp))
            }
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ProfileMenuItem(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(Mint),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = title, tint = ForestGreen, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
