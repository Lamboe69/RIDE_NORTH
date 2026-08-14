@file:OptIn(ExperimentalMaterial3Api::class)

package com.ridenorth.driver.ui.screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.ElectricRickshaw
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocalTaxi
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridenorth.driver.data.ApplicationStatus
import com.ridenorth.driver.data.DriverApi
import com.ridenorth.driver.data.DriverApplicationDto
import com.ridenorth.driver.data.PhoneOtpRequest
import com.ridenorth.driver.data.SubmitDriverApplicationRequest
import com.ridenorth.driver.data.VerifyOtpRequest
import com.ridenorth.driver.data.VehicleType
import com.ridenorth.driver.ui.theme.AmberTint
import com.ridenorth.driver.ui.theme.Danger
import com.ridenorth.driver.ui.theme.Emerald
import com.ridenorth.driver.ui.theme.ForestDark
import com.ridenorth.driver.ui.theme.ForestGreen
import com.ridenorth.driver.ui.theme.Gold
import com.ridenorth.driver.ui.theme.Mint
import kotlinx.coroutines.launch

private data class ApplicationDraft(
    val fullName: String = "",
    val phoneNumber: String = "",
    val ninNumber: String = "",
    val licenseNumber: String = "",
    val vehicleType: VehicleType? = null,
    val plateNumber: String = "",
    val make: String = "",
    val model: String = "",
    val year: String = "",
    val capacity: Int = 1
)

private data class VehicleChoice(val type: VehicleType, val label: String, val icon: ImageVector)

private val vehicleChoices = listOf(
    VehicleChoice(VehicleType.BODA, "Boda Boda", Icons.Filled.TwoWheeler),
    VehicleChoice(VehicleType.TUKTUK, "Tuk Tuk", Icons.Filled.ElectricRickshaw),
    VehicleChoice(VehicleType.CAR, "Car", Icons.Filled.LocalTaxi),
    VehicleChoice(VehicleType.TRUCK, "Truck", Icons.Filled.LocalShipping),
    VehicleChoice(VehicleType.LORRY, "Lorry", Icons.Filled.DirectionsCar)
)

private fun formatPhone(draft: String) = draft.replace(" ", "").let {
    if (it.startsWith("+256")) it.removePrefix("+256") else if (it.startsWith("0")) it.removePrefix("0") else it
}

@Composable
fun DriverApplicationFlowScreen(onExit: () -> Unit) {
    val scope = rememberCoroutineScope()
    var step by remember { mutableStateOf(0) }
    var draft by remember { mutableStateOf(ApplicationDraft()) }
    var otp by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var submitted by remember { mutableStateOf<DriverApplicationDto?>(null) }
    var checkingStatus by remember { mutableStateOf(false) }

    val steps = listOf("Welcome", "Details", "Verify Phone", "Licence & Vehicle", "Vehicle Details", "Review")

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (step > 0) {
                TopAppBar(
                    title = { Text("Driver Application", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = {
                            if (step == 0) onExit() else {
                                otp = ""
                                error = null
                                step -= 1
                            }
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                if (submitted != null) {
                    ApplicationSuccessScreen(
                        application = submitted!!,
                        onCheckStatus = { checkingStatus = true },
                        onDone = onExit
                    )
                } else {
                    if (step > 0) StepProgress(current = step, total = steps.size)
                    when (step) {
                        0 -> ApplicationIntroScreen(
                            onStart = { step = 1 },
                            onCheckStatus = { checkingStatus = true }
                        )
                        1 -> PersonalDetailsStep(
                            draft = draft,
                            onDraftChange = { draft = it },
                            error = error,
                            onBack = { step = 0 },
                            onNext = {
                                error = null
                                if (draft.fullName.trim().length < 2) error = "Please enter your full name."
                                else if (formatPhone(draft.phoneNumber).length !in 9..9) error = "Enter a valid 9-digit phone number, e.g. 77X XXX XXX."
                                else if (draft.ninNumber.isNotBlank() && draft.ninNumber.length < 10) error = "National ID must be at least 10 digits."
                                else step = 2
                            }
                        )
                        2 -> PhoneVerifyStep(
                            phoneNumber = draft.phoneNumber,
                            otp = otp,
                            onOtpChange = { otp = it },
                            loading = loading,
                            error = error,
                            onBack = { step = 1 },
                            onRequestOtp = {
                                error = null
                                loading = true
                                scope.launch {
                                    try {
                                        DriverApi.service.requestOtp(PhoneOtpRequest(formatPhone(draft.phoneNumber)))
                                    } catch (e: Exception) {
                                        error = "Could not send code. Is the server running? (${e.message?.take(60)})"
                                    } finally {
                                        loading = false
                                    }
                                }
                            },
                            onVerify = {
                                error = null
                                if (otp.length != 6) { error = "Enter the 6-digit code."; return@PhoneVerifyStep }
                                loading = true
                                scope.launch {
                                    try {
                                        DriverApi.service.verifyOtp(VerifyOtpRequest(formatPhone(draft.phoneNumber), otp))
                                        step = 3
                                    } catch (e: Exception) {
                                        error = "Code not accepted. Check the OTP or resend."
                                    } finally {
                                        loading = false
                                    }
                                }
                            }
                        )
                        3 -> LicenceVehicleStep(
                            draft = draft,
                            onDraftChange = { draft = it },
                            error = error,
                            onBack = { step = 2 },
                            onNext = {
                                error = null
                                if (draft.licenseNumber.isBlank()) error = "Enter your driving permit number."
                                else if (draft.vehicleType == null) error = "Select your vehicle type."
                                else step = 4
                            }
                        )
                        4 -> VehicleDetailsStep(
                            draft = draft,
                            onDraftChange = { draft = it },
                            error = error,
                            onBack = { step = 3 },
                            onNext = {
                                error = null
                                if (draft.plateNumber.isBlank()) error = "Enter your plate number."
                                else if (draft.make.isBlank()) error = "Enter the vehicle make."
                                else step = 5
                            }
                        )
                        5 -> ReviewStep(
                            draft = draft,
                            loading = loading,
                            error = error,
                            onBack = { step = 4 },
                            onSubmit = {
                                error = null
                                val vType = draft.vehicleType ?: return@ReviewStep
                                loading = true
                                scope.launch {
                                    try {
                                        val result = DriverApi.service.submitApplication(
                                            SubmitDriverApplicationRequest(
                                                phoneNumber = formatPhone(draft.phoneNumber),
                                                fullName = draft.fullName.trim(),
                                                ninNumber = draft.ninNumber.ifBlank { null },
                                                licenseNumber = draft.licenseNumber.trim().uppercase(),
                                                vehicleType = vType,
                                                plateNumber = draft.plateNumber.trim().uppercase(),
                                                make = draft.make.trim(),
                                                model = draft.model.trim().ifBlank { null },
                                                year = draft.year.ifBlank { null },
                                                capacity = draft.capacity
                                            )
                                        )
                                        submitted = result
                                    } catch (e: Exception) {
                                        error = "Could not submit. ${e.message?.take(80)}"
                                    } finally {
                                        loading = false
                                    }
                                }
                            }
                        )
                    }
                }
            }

            if (checkingStatus) {
                StatusOverlay(
                    onDismiss = { checkingStatus = false },
                    initialPhone = draft.phoneNumber
                )
            }

            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                        Row(Modifier.padding(horizontal = 24.dp, vertical = 18.dp), verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(color = ForestGreen, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(14.dp))
                            Text("Please wait...", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepProgress(current: Int, total: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .clip(CircleShape)
                    .background(if (index < current) ForestGreen else MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun ApplicationIntroScreen(onStart: () -> Unit, onCheckStatus: () -> Unit) {
    Column {
        Spacer(Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(26.dp))
                .background(Brush.verticalGradient(listOf(ForestDark, ForestGreen, Color(0xFF115E59))))
                .padding(26.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                BrandMark(size = 72, icon = Icons.Filled.Badge)
                Spacer(Modifier.height(18.dp))
                Text(
                    text = "Drive with RideNorth",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Join the driver network serving Gulu, Lira & beyond",
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(Modifier.height(18.dp))

        BenefitRow(Icons.Filled.TwoWheeler, "Flexible earnings", "Get matched with riders and freight jobs in your area.")
        BenefitRow(Icons.Filled.Security, "Verified partners", "We verify your documents and keep you protected.")
        BenefitRow(Icons.Filled.Sms, "Instant updates", "Get your approval and login details by SMS.")
        BenefitRow(Icons.Filled.DocumentScanner, "Simple paperwork", "Permit, National ID and your vehicle details - that's it.")

        Spacer(Modifier.height(22.dp))
        GradientButton(text = "Start Application", onClick = onStart)
        Spacer(Modifier.height(10.dp))
        TextButton(onClick = onCheckStatus, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Already applied? Check your status", color = ForestGreen, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun BenefitRow(icon: ImageVector, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Mint),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 6.dp, top = 12.dp)
    )
}

@Composable
private fun PersonalDetailsStep(
    draft: ApplicationDraft,
    onDraftChange: (ApplicationDraft) -> Unit,
    error: String?,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text("Tell us about yourself", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Step 1 of 5 - these details will appear on your driver profile.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))
        FieldLabel("Full Name")
        OutlinedTextField(
            value = draft.fullName,
            onValueChange = { onDraftChange(draft.copy(fullName = it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. John Doe") },
            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = ForestGreen) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        FieldLabel("National ID Number (optional)")
        OutlinedTextField(
            value = draft.ninNumber,
            onValueChange = { if (it.all(Char::isDigit) && it.length <= 20) onDraftChange(draft.copy(ninNumber = it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. CM9400001234") },
            leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null, tint = ForestGreen) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(14.dp)
        )
        FieldLabel("Phone Number")
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Mint)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("+256", fontWeight = FontWeight.Bold, color = ForestGreen)
            }
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = draft.phoneNumber,
                onValueChange = { if (it.length <= 9 && it.all(Char::isDigit)) onDraftChange(draft.copy(phoneNumber = it)) },
                modifier = Modifier.weight(1f),
                placeholder = { Text("7XX XXX XXX") },
                leadingIcon = { Icon(Icons.Filled.PhoneAndroid, contentDescription = null, tint = ForestGreen) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp)
            )
        }
        Text(
            text = "We'll send an SMS code to confirm this number.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
        if (error != null) {
            Spacer(Modifier.height(10.dp))
            ErrorText(error)
        }
        Spacer(Modifier.height(22.dp))
        GradientButton(text = "Continue", onClick = onNext)
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun PhoneVerifyStep(
    phoneNumber: String,
    otp: String,
    onOtpChange: (String) -> Unit,
    loading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onRequestOtp: () -> Unit,
    onVerify: () -> Unit
) {
    Column {
        Text("Verify your phone", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Step 2 of 5 - we sent a 6-digit code to +256 $phoneNumber", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(6) { index ->
                        val char = otp.getOrNull(index)?.toString() ?: ""
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (char.isNotEmpty()) Mint else MaterialTheme.colorScheme.surfaceVariant)
                                .border(
                                    width = 1.5.dp,
                                    color = if (char.isNotEmpty()) ForestGreen else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(char, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onRequestOtp,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen)
                    ) {
                        Icon(Icons.Filled.Sms, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Send Code", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = onVerify,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        enabled = otp.length == 6
                    ) {
                        Text("Verify", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        Text(
            text = "Tip: request the code once, then type the 6 digits. If no SMS arrives in a test environment, check the server logs.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 10.dp)
        )
        if (error != null) {
            Spacer(Modifier.height(10.dp))
            ErrorText(error)
        }
        Spacer(Modifier.height(22.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Back to details")
        }
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun LicenceVehicleStep(
    draft: ApplicationDraft,
    onDraftChange: (ApplicationDraft) -> Unit,
    error: String?,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text("Licence & vehicle", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Step 3 of 5 - what will you drive for RideNorth?", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))
        FieldLabel("Driving Permit Number")
        OutlinedTextField(
            value = draft.licenseNumber,
            onValueChange = { if (it.length <= 20) onDraftChange(draft.copy(licenseNumber = it.uppercase())) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. PM20190001234") },
            leadingIcon = { Icon(Icons.Filled.Badge, contentDescription = null, tint = ForestGreen) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(Modifier.height(18.dp))
        Text("Select vehicle type", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp))
        vehicleChoices.forEach { choice ->
            val selected = draft.vehicleType == choice.type
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (selected) Mint else MaterialTheme.colorScheme.surface)
                    .border(
                        width = 1.5.dp,
                        color = if (selected) ForestGreen else MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onDraftChange(draft.copy(vehicleType = choice.type)) }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected) ForestGreen else MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(choice.icon, contentDescription = null, tint = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(choice.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(2.dp))
                    Text(
                        when (choice.type) {
                            VehicleType.BODA -> "Motorcycle · up to 1 pillion"
                            VehicleType.TUKTUK -> "Three-wheeler · up to 3 passengers"
                            VehicleType.CAR -> "Sedan/SUV · up to 4 passengers"
                            VehicleType.TRUCK -> "Delivery truck · freight"
                            VehicleType.LORRY -> "Heavy goods lorry · freight"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (selected) Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(22.dp))
            }
        }
        if (error != null) {
            Spacer(Modifier.height(10.dp))
            ErrorText(error)
        }
        Spacer(Modifier.height(22.dp))
        GradientButton(text = "Continue", onClick = onNext)
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun VehicleDetailsStep(
    draft: ApplicationDraft,
    onDraftChange: (ApplicationDraft) -> Unit,
    error: String?,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Column {
        Text("Your vehicle", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Step 4 of 5 - the vehicle you'll use for trips.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))
        FieldLabel("Plate Number")
        OutlinedTextField(
            value = draft.plateNumber,
            onValueChange = { if (it.length <= 15) onDraftChange(draft.copy(plateNumber = it.uppercase())) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. UAM 123G") },
            leadingIcon = { Icon(Icons.Filled.BusinessCenter, contentDescription = null, tint = ForestGreen) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            shape = RoundedCornerShape(14.dp)
        )
        FieldLabel("Make")
        OutlinedTextField(
            value = draft.make,
            onValueChange = { onDraftChange(draft.copy(make = it)) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g. Toyota") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Column(Modifier.weight(1f)) {
                FieldLabel("Model (optional)")
                OutlinedTextField(
                    value = draft.model,
                    onValueChange = { onDraftChange(draft.copy(model = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. RAV4") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp)
                )
            }
            Column(Modifier.weight(1f)) {
                FieldLabel("Year (optional)")
                OutlinedTextField(
                    value = draft.year,
                    onValueChange = { if (it.length <= 4 && it.all(Char::isDigit)) onDraftChange(draft.copy(year = it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("2020") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }
        FieldLabel("Seating / Load Capacity")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(1, 2, 3, 4, 6, 10).forEach { c ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (draft.capacity == c) ForestGreen else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onDraftChange(draft.copy(capacity = c)) }
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("$c", color = if (draft.capacity == c) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
                }
            }
        }
        if (error != null) {
            Spacer(Modifier.height(10.dp))
            ErrorText(error)
        }
        Spacer(Modifier.height(22.dp))
        GradientButton(text = "Review Application", onClick = onNext)
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun ReviewStep(
    draft: ApplicationDraft,
    loading: Boolean,
    error: String?,
    onBack: () -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        Text("Review & submit", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Step 5 of 5 - make sure everything is correct.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(6.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                ReviewLine("Full Name", draft.fullName)
                ReviewLine("Phone", "+256 ${draft.phoneNumber}")
                ReviewLine("National ID", draft.ninNumber.ifBlank { "Not provided" })
                ReviewLine("Permit Number", draft.licenseNumber)
                ReviewLine("Vehicle", draft.vehicleType?.let { v -> vehicleChoices.first { it.type == v }.label } ?: "-")
                ReviewLine("Plate Number", draft.plateNumber)
                ReviewLine("Make / Model", "${draft.make} ${draft.model}".trim())
                ReviewLine("Capacity", "${draft.capacity}")
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.FactCheck, contentDescription = null, tint = Gold, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                "By submitting, you confirm the details above are true. Our team reviews every application.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (error != null) {
            Spacer(Modifier.height(10.dp))
            ErrorText(error)
        }
        Spacer(Modifier.height(22.dp))
        GradientButton(text = if (loading) "Submitting..." else "Submit Application", onClick = onSubmit)
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun ReviewLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
    }
    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
}

@Composable
private fun ErrorText(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Danger.copy(alpha = 0.08f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.ErrorOutline, contentDescription = null, tint = Danger, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(message, color = Danger, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ApplicationSuccessScreen(
    application: DriverApplicationDto,
    onCheckStatus: () -> Unit,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Mint),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(56.dp))
        }
        Spacer(Modifier.height(18.dp))
        Text("Application Received!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(6.dp))
        Text(
            text = "We've received ${application.fullName}'s application. Our team will verify your documents and SMS your login details once approved.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(22.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Your Application Reference", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(8.dp))
                Text(
                    application.applicationRef,
                    color = ForestGreen,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Keep this reference to check your status or call support.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(AmberTint)
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text("Status: ${application.status.name}", color = Gold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        OutlinedButton(
            onClick = onCheckStatus,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen)
        ) {
            Text("Check Status Now", fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(10.dp))
        TextButton(onClick = onDone) {
            Text("Back to home", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun StatusOverlay(onDismiss: () -> Unit, initialPhone: String) {
    var phone by remember { mutableStateOf(initialPhone) }
    var result by remember { mutableStateOf<DriverApplicationDto?>(null) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onDismiss) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Close", tint = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(Modifier.width(4.dp))
            Text("Application Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(14.dp))
        Text("Enter the phone number you applied with to see your application status.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Mint)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("+256", fontWeight = FontWeight.Bold, color = ForestGreen)
            }
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { if (it.length <= 9 && it.all(Char::isDigit)) phone = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("7XX XXX XXX") },
                leadingIcon = { Icon(Icons.Filled.PhoneAndroid, contentDescription = null, tint = ForestGreen) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(14.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        GradientButton(text = if (loading) "Checking..." else "Check Status", onClick = {
            if (phone.length == 9) {
                loading = true
                error = null
                scope.launch {
                    try {
                        result = DriverApi.service.getApplicationStatus(formatPhone(phone))
                    } catch (e: Exception) {
                        error = "No application found for this number yet."
                    } finally {
                        loading = false
                    }
                }
            } else {
                error = "Enter a valid 9-digit phone number."
            }
        })
        if (error != null) {
            Spacer(Modifier.height(12.dp))
            ErrorText(error!!)
        }
        result?.let { app ->
            Spacer(Modifier.height(18.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(22.dp)) {
                    when (app.status) {
                        ApplicationStatus.PENDING -> {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(AmberTint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.TwoWheeler, contentDescription = null, tint = Gold, modifier = Modifier.size(28.dp))
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("Under Review", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Application ${app.applicationRef} is with our team. You'll receive an SMS with your login details once approved - usually within 24 hours.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        ApplicationStatus.APPROVED -> {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Mint),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(28.dp))
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("Approved!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ForestGreen)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Application ${app.applicationRef} was approved. Your login details have been sent by SMS. Tap Login on the home screen to start earning.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        ApplicationStatus.REJECTED -> {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Danger.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.ErrorOutline, contentDescription = null, tint = Danger, modifier = Modifier.size(28.dp))
                            }
                            Spacer(Modifier.height(12.dp))
                            Text("Not Approved", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Danger)
                            Spacer(Modifier.height(4.dp))
                            Text(
                                app.rejectionReason ?: "Your application was not approved. Contact RideNorth support for more details.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(Modifier.height(12.dp))
                    ReviewLine("Application Ref", app.applicationRef)
                    ReviewLine("Status", app.status.name)
                    ReviewLine("Vehicle", app.vehicleType.name)
                    ReviewLine("Plate", app.plateNumber)
                }
            }
        }
        Spacer(Modifier.height(28.dp))
    }
}
