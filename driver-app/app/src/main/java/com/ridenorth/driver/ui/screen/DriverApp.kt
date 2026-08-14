@file:OptIn(ExperimentalMaterial3Api::class)

package com.ridenorth.driver.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.ridenorth.driver.ui.theme.AmberTint
import com.ridenorth.driver.ui.theme.Danger
import com.ridenorth.driver.ui.theme.Emerald
import com.ridenorth.driver.ui.theme.ForestDark
import com.ridenorth.driver.ui.theme.ForestGreen
import com.ridenorth.driver.ui.theme.Gold
import com.ridenorth.driver.ui.theme.Mint

sealed class DriverScreen(val route: String) {
    object Splash : DriverScreen("splash")
    object Auth : DriverScreen("auth")
    object Dashboard : DriverScreen("dashboard")
    object Jobs : DriverScreen("jobs")
    object Earnings : DriverScreen("earnings")
    object Application : DriverScreen("application")
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
            DriverSplashScreen(
                onNavigateToAuth = {
                    isAuthenticated = false
                    navController.navigate(DriverScreen.Auth.route) {
                        popUpTo(DriverScreen.Splash.route) { inclusive = true }
                    }
                },
                onApplyToDrive = {
                    navController.navigate(DriverScreen.Application.route)
                }
            )
        }
        composable(DriverScreen.Auth.route) {
            DriverAuthScreen(
                onAuthSuccess = {
                    isAuthenticated = true
                    navController.navigate(DriverScreen.Dashboard.route) {
                        popUpTo(DriverScreen.Auth.route) { inclusive = true }
                    }
                },
                onApplyToDrive = {
                    navController.navigate(DriverScreen.Application.route)
                }
            )
        }
        composable(DriverScreen.Application.route) {
            DriverApplicationFlowScreen(onExit = { navController.popBackStack() })
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

// ---------------- Shared components ----------------

@Composable
fun BrandMark(size: Int = 48, icon: ImageVector = Icons.Filled.DirectionsCar) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(RoundedCornerShape((size * 0.3f).dp))
            .background(Brush.linearGradient(listOf(Color.White, Color(0xFFE7F8F5))))
            .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape((size * 0.3f).dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "RideNorth logo",
            tint = ForestGreen,
            modifier = Modifier.size((size * 0.5f).dp)
        )
    }
}

@Composable
fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    container: List<Color> = listOf(ForestGreen, Emerald)
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(container), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}

// ---------------- Splash ----------------

@Composable
fun DriverSplashScreen(onNavigateToAuth: () -> Unit, onApplyToDrive: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ForestDark, ForestGreen, Color(0xFF115E59))))
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
                text = "RideNorth Driver",
                color = Color.White,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Start Earning Today",
                color = Color.White.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(30.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FeaturePill("Boda")
                FeaturePill("Tuk Tuk")
                FeaturePill("Car")
                FeaturePill("Truck")
            }
            Spacer(Modifier.height(34.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.NotificationsActive,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "New ride requests sent straight to you",
                    color = Color.White.copy(alpha = 0.75f),
                    fontSize = 13.sp
                )
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
                Text("Login", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onApplyToDrive,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.5f)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
            ) {
                Icon(Icons.Filled.Badge, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Apply to Drive", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Powered by RideNorth · Gulu, Lira & beyond",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
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

// ---------------- Auth ----------------

@Composable
fun DriverAuthScreen(onAuthSuccess: () -> Unit, onApplyToDrive: () -> Unit) {
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var step by remember { mutableStateOf("phone") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(248.dp)
                    .background(Brush.verticalGradient(listOf(ForestDark, ForestGreen, Color(0xFF115E59))))
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
                        Column {
                            Text(
                                text = "RideNorth Driver",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Spacer(Modifier.height(22.dp))
                    Text(
                        text = if (step == "phone") "Driver Login" else "Verify your number",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = if (step == "phone")
                            "Enter the phone number linked to your driver account"
                        else "Enter the 4-digit code sent to +256 $phoneNumber",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
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
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Phone),
                                shape = RoundedCornerShape(14.dp)
                            )
                            Spacer(Modifier.height(18.dp))
                            GradientButton(
                                text = "Request OTP",
                                onClick = { step = "otp" }
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
                            text = "New driver? ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextButton(onClick = onApplyToDrive) {
                            Text("Apply to Drive", color = ForestGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    DriverOtpCard(
                        otp = otp,
                        onOtpChange = { otp = it },
                        onVerify = onAuthSuccess,
                        onResend = { },
                        onBack = { step = "phone" },
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DriverOtpCard(
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
                            .background(if (char.isNotEmpty()) Mint else MaterialTheme.colorScheme.surfaceVariant)
                            .border(
                                width = 1.5.dp,
                                color = if (char.isNotEmpty()) ForestGreen else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.foundation.text.BasicTextField(
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
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
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

// ---------------- Dashboard ----------------

@Composable
fun DriverDashboardScreen(onNavigateToJobs: () -> Unit, onNavigateToEarnings: () -> Unit) {
    var isOnline by remember { mutableStateOf(true) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Driver Dashboard", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
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
            // Identity card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(ForestGreen, Emerald))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = "Avatar", tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Erick Ojok", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = Gold, modifier = Modifier.size(15.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("4.9", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                            Text("  ·  Boda Boda", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(if (isOnline) Mint else MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(if (isOnline) ForestGreen else MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = if (isOnline) "Online" else "Offline",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isOnline) ForestGreen else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Online toggle
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOnline) ForestGreen else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isOnline = !isOnline }
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .background(if (isOnline) Color.White.copy(alpha = 0.18f) else Mint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isOnline) Icons.Filled.CheckCircle else Icons.Filled.Timer,
                            contentDescription = null,
                            tint = if (isOnline) Color.White else ForestGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = if (isOnline) "You are online" else "Go online to receive jobs",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isOnline) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = if (isOnline) "Tap to go offline" else "Turn on to start receiving ride requests",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isOnline) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Switch(
                        checked = isOnline,
                        onCheckedChange = { isOnline = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color.White.copy(alpha = 0.35f),
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }

            Spacer(Modifier.height(14.dp))

            // Earnings banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(Brush.horizontalGradient(listOf(ForestGreen, Emerald)))
                    .clickable(onClick = onNavigateToEarnings)
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Today's Earnings",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "UGX 45,000",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .padding(bottom = 6.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color.White.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("+18%", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        StatChip("8", "Trips")
                        StatChip("6h 20m", "Online")
                        StatChip("4.9", "Rating")
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionCard(
                    icon = Icons.Filled.TwoWheeler,
                    title = "Available Jobs",
                    subtitle = "3 new requests",
                    badge = "3",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToJobs
                )
                ActionCard(
                    icon = Icons.Filled.AccountBalanceWallet,
                    title = "My Earnings",
                    subtitle = "Wallet: UGX 180k",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToEarnings
                )
            }

            Spacer(Modifier.height(14.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF9EF)),
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
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AmberTint),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.LocalShipping, contentDescription = null, tint = Gold, modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Harvest season is here", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            "Freight jobs pay up to 2× standard fares. Enable freight mode in settings.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Gold)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatChip(value: String, label: String) {
    Column {
        Text(value, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
    }
}

@Composable
private fun ActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    badge: String? = null,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Box {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Mint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = title, tint = ForestGreen, modifier = Modifier.size(22.dp))
                }
                if (badge != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-6).dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Danger)
                            .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(badge, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ---------------- Jobs ----------------

@Composable
fun DriverJobsScreen(onBack: () -> Unit) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Available Jobs", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(ForestGreen)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("3 jobs near you", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.weight(1f))
                Text("Gulu City", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(16.dp))

            JobCard(
                icon = Icons.Filled.TwoWheeler,
                title = "Boda Boda ride",
                eta = "2 min away · 1.2 km",
                fare = "UGX 3,000",
                pickup = "Citi Link",
                dropoff = "Gulu Market",
                onAccept = { },
                onDecline = { }
            )
            Spacer(Modifier.height(14.dp))

            JobCard(
                icon = Icons.Filled.ElectricRickshaw,
                title = "Tuk Tuk ride",
                eta = "4 min away · 2.8 km",
                fare = "UGX 6,500",
                pickup = "Senior Quarters",
                dropoff = "Gulu Stadium",
                onAccept = { },
                onDecline = { }
            )
            Spacer(Modifier.height(14.dp))

            JobCard(
                icon = Icons.Filled.LocalShipping,
                title = "Parcel delivery",
                eta = "3 min away · 1.6 km",
                fare = "UGX 4,500",
                pickup = "Cargo Depot",
                dropoff = "Laroo Pece",
                onAccept = { },
                onDecline = { }
            )
            Spacer(Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.SearchOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(34.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text("No more new requests", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Stay online and we'll notify you when a rider nearby books.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun JobCard(
    icon: ImageVector,
    title: String,
    eta: String,
    fare: String,
    pickup: String,
    dropoff: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Mint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = title, tint = ForestGreen, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(2.dp))
                    Text(eta, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Mint)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(fare, color = ForestGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(14.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
            Spacer(Modifier.height(12.dp))
            Row {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(ForestGreen)
                    )
                    Box(
                        Modifier
                            .width(2.dp)
                            .height(18.dp)
                            .background(ForestGreen.copy(alpha = 0.35f))
                    )
                    Box(
                        Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(Danger)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(pickup, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text(dropoff, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onDecline,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.outlineVariant),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    Text("Decline", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onAccept,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
                ) {
                    Text("Accept", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------------- Earnings ----------------

@Composable
fun DriverEarningsScreen(onBack: () -> Unit) {
    val week = listOf(45 to "Mon", 62 to "Tue", 40 to "Wed", 78 to "Thu", 90 to "Fri", 58 to "Sat", 30 to "Sun")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text("Earnings", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Brush.horizontalGradient(listOf(ForestGreen, Emerald)))
                    .padding(22.dp)
            ) {
                Column {
                    Text(
                        text = "Wallet Balance",
                        color = Color.White.copy(alpha = 0.85f),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "UGX 180,000",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(13.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = ForestGreen)
                        ) {
                            Icon(Icons.Filled.Payments, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Withdraw", fontWeight = FontWeight.Bold)
                        }
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp),
                            shape = RoundedCornerShape(13.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.6f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                        ) {
                            Text("History", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EarningsStat(
                    label = "Today",
                    value = "UGX 45,000",
                    delta = "+18%",
                    modifier = Modifier.weight(1f)
                )
                EarningsStat(
                    label = "This Week",
                    value = "UGX 320,000",
                    delta = "+9%",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("This Week", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(2.dp))
                            Text("UGX 320,000 total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(Mint)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text("Best: Fri", color = ForestGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(Modifier.height(18.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        week.forEach { (pct, day) ->
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text("$pct", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(((pct * 0.9f)).dp)
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(if (pct >= 80) ForestGreen else ForestGreen.copy(alpha = 0.45f))
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(day, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    Text(
                        text = "Recent Earnings",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(18.dp)
                    )
                    EarningsRow("Boda Boda · Gulu Market", "UGX 3,000", "Today, 09:12")
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), modifier = Modifier.padding(start = 18.dp))
                    EarningsRow("Tuk Tuk · Stadium", "UGX 6,500", "Today, 10:45")
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), modifier = Modifier.padding(start = 18.dp))
                    EarningsRow("Parcel · Laroo Pece", "UGX 4,500", "Today, 12:03")
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f), modifier = Modifier.padding(start = 18.dp))
                    EarningsRow("Freight quote bonus", "UGX 15,000", "Yesterday, 18:22")
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EarningsStat(label: String, value: String, delta: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(6.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.TrendingUp, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(delta, color = ForestGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun EarningsRow(title: String, amount: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            Text(time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(amount, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = ForestGreen)
    }
}
