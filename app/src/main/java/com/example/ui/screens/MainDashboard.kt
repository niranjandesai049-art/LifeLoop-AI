package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ChatMessage
import com.example.data.model.Document
import com.example.data.model.Memory
import com.example.data.model.Reminder
import com.example.data.model.Task
import com.example.ui.theme.BorderCyan
import com.example.ui.theme.GlassSpecular
import com.example.ui.theme.IceWhite
import com.example.ui.theme.MutedSlate
import com.example.ui.theme.NeonCoral
import com.example.ui.theme.NeonMint
import com.example.ui.theme.NeonTeal
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.SlateCard
import com.example.ui.theme.SlateDark
import com.example.ui.viewmodel.LifeAnalytics
import com.example.ui.viewmodel.LifeLoopViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainDashboardScreen(
    viewModel: LifeLoopViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isPremium by viewModel.isPremiumUser.collectAsState()

    var showAddQuickTaskDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianBlack),
        bottomBar = {
            LifeLoopBottomNav(
                activeTab = currentTab,
                onTabSelected = { viewModel.changeTab(it) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ObsidianBlack,
                            ObsidianBlack,
                            SlateDark
                        )
                    )
                )
        ) {
            // --- CLEAN MINIMALST TOP STATUS HEADER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(NeonViolet, CircleShape)
                            .clip(CircleShape)
                            .clickable { viewModel.changeTab(3) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "LifeLoop Active Engine",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "LIFELOOP",
                            fontSize = 15.sp,
                            color = NeonTeal,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "Joe Jenkins",
                            fontSize = 11.sp,
                            color = MutedSlate,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // AI Active / Premium Status capsule
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0x0FFFFFFF)) // bg-white/5
                        .border(
                            1.dp,
                            Color(0x1BFFFFFF), // border border-white/10
                            RoundedCornerShape(100.dp)
                        )
                        .clickable { viewModel.changeTab(3) }
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Pulsing green dot
                    var pulseAlpha by remember { mutableStateOf(1f) }
                    LaunchedEffect(Unit) {
                        while (true) {
                            pulseAlpha = 0.3f
                            kotlinx.coroutines.delay(800)
                            pulseAlpha = 1f
                            kotlinx.coroutines.delay(800)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NeonMint.copy(alpha = pulseAlpha), CircleShape)
                    )
                    Text(
                        text = if (isPremium) "AI ACTIVE • SUPERHUMAN" else "AI ACTIVE • FREE",
                        fontSize = 10.sp,
                        color = IceWhite,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Quick attachment triggers to demo automated OCR
            QuickAttachmentsSection { filename ->
                viewModel.simulateFileUpload(filename)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- VIEWPORT VIEWS ---
            Box(modifier = Modifier.fillMaxSize()) {
                when (currentTab) {
                    0 -> DashboardWorkspace(
                        viewModel = viewModel,
                        onAddQuickTaskClick = { showAddQuickTaskDialog = true }
                    )
                    1 -> JarvisChatWorkspace(viewModel = viewModel)
                    2 -> SecondBrainWorkspace(viewModel = viewModel)
                    3 -> SubscriptionSaaSHub(viewModel = viewModel)
                }
            }
        }
    }

    if (showAddQuickTaskDialog) {
        QuickTaskAddDialog(
            onDismiss = { showAddQuickTaskDialog = false },
            onSubmit = { title, category, priority, offset ->
                viewModel.submitTaskDirectly(title, category, offset, priority)
                showAddQuickTaskDialog = false
            }
        )
    }
}

// --- SUB-WORKSPACE COMPOSABLES ---

@Composable
fun QuickAttachmentsSection(onCapture: (String) -> Unit) {
    Text(
        text = "SIMULAR AUTOMATION DECK - SNAP PHOTOS TO PROCESS OCR INTELLIGENCE:",
        fontSize = 8.sp,
        color = MutedSlate,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
    )
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            QuickCaptureCard(
                label = "Utility Bill (PDF)",
                desc = "Auto-reminder/bills",
                tint = NeonTeal,
                onClick = { onCapture("electric_utility_bill_doc.pdf") }
            )
        }
        item {
            QuickCaptureCard(
                label = "Asthma Rx (JPG)",
                desc = "Routine dosing tasks",
                tint = NeonMint,
                onClick = { onCapture("asthma_inhaler_prescription.jpg") }
            )
        }
        item {
            QuickCaptureCard(
                label = "Stanford Paper (PDF)",
                desc = "Deadline schedules",
                tint = NeonViolet,
                onClick = { onCapture("stanford_nlp_syllabus.pdf") }
            )
        }
        item {
            QuickCaptureCard(
                label = "Coffee Receipt",
                desc = "Auto expense track",
                tint = NeonCoral,
                onClick = { onCapture("starbucks_receipt_may_24.jpg") }
            )
        }
    }
}

@Composable
fun QuickCaptureCard(
    label: String,
    desc: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SlateCard)
            .border(1.dp, BorderCyan.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.PhotoCamera,
            contentDescription = "Capture icon",
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(label, fontSize = 11.sp, color = IceWhite, fontWeight = FontWeight.Bold)
            Text(desc, fontSize = 8.sp, color = MutedSlate)
        }
    }
}

// ----------------------------------------------------
// 0. DASHBOARD WORKSPACE MODULE
// ----------------------------------------------------
@Composable
fun DashboardWorkspace(
    viewModel: LifeLoopViewModel,
    onAddQuickTaskClick: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    val reminders by viewModel.reminders.collectAsState()
    val analytics by viewModel.analyticsState.collectAsState()

    val pendingTasks = tasks.filter { !it.isCompleted }
    val activeReminders = reminders.filter { it.status == "ACTIVE" || it.status == "SNOOZED" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_scroll"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- LIFE METRIC DIALS NATIVE CANVAS ---
        item {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                gradientBrush = Brush.verticalGradient(listOf(Color(0xFF141629), Color(0xFF0F101C)))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "COGNITIVE LOAD & HEALTH DIALS",
                        fontSize = 11.sp,
                        color = MutedSlate,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularDialComponent(
                            value = analytics.productivityScore,
                            title = "Task Done",
                            subTitle = "${tasks.count { it.isCompleted }}/${tasks.size} Items",
                            color = NeonMint
                        )
                        CircularDialComponent(
                            value = analytics.focusIndex,
                            title = "Focus Rating",
                            subTitle = "High priorities",
                            color = NeonTeal
                        )
                        StressLevelDialComponent(
                            value = analytics.stressScore,
                            title = "Neural Overload",
                            subTitle = if (analytics.stressScore > 50) "Critical Alert" else "Controlled",
                            color = if (analytics.stressScore > 50) NeonCoral else NeonMint
                        )
                    }
                }
            }
        }

        // --- ACTIVE COGNITIVE REMINDERS SECTION ---
        if (activeReminders.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        "DISPATCHING REMINDERS (${activeReminders.size})",
                        fontSize = 11.sp,
                        color = NeonCoral,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    activeReminders.forEach { reminder ->
                        ActiveReminderItem(
                            reminder = reminder,
                            onSnooze = { viewModel.snoozeReminder(reminder) },
                            onComplete = { viewModel.resolveReminder(reminder) },
                            onDelete = { viewModel.dismissReminder(reminder.id) }
                        )
                    }
                }
            }
        }

        // --- LIFE TIMELINE TASKS ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "INTELLIGENT LIFE TIMELINE (${pendingTasks.size})",
                    fontSize = 11.sp,
                    color = MutedSlate,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onAddQuickTaskClick() }
                        .background(NeonViolet)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = IceWhite, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ADD TASK", fontSize = 9.sp, color = IceWhite, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (pendingTasks.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ListAlt,
                        contentDescription = "Empty",
                        tint = MutedSlate.copy(alpha = 0.3f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Mental Overload Nullified.\nAll activities organized by LifeLoop AI.",
                        fontSize = 12.sp,
                        color = MutedSlate,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            items(pendingTasks, key = { it.id }) { task ->
                TimelineTaskRow(
                    task = task,
                    onToggle = { viewModel.toggleTaskCompletion(task) },
                    onDelete = { viewModel.deleteTask(task.id) }
                )
            }
        }

        // Extra spacing at bottom
        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun CircularDialComponent(
    value: Int,
    title: String,
    subTitle: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(70.dp)
        ) {
            Canvas(modifier = Modifier.size(62.dp)) {
                // Background Track
                drawArc(
                    color = color.copy(alpha = 0.15f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
                // Active Sweep
                drawArc(
                    color = color,
                    startAngle = 270f,
                    sweepAngle = (value * 3.6f),
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$value%",
                    fontSize = 15.sp,
                    color = IceWhite,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 11.sp, color = IceWhite, fontWeight = FontWeight.Bold)
        Text(subTitle, fontSize = 8.sp, color = MutedSlate)
    }
}

@Composable
fun StressLevelDialComponent(
    value: Int,
    title: String,
    subTitle: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(70.dp)
        ) {
            Canvas(modifier = Modifier.size(62.dp)) {
                // Curved dial (half circle arc)
                drawArc(
                    color = Color.DarkGray.copy(alpha = 0.3f),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
                // Sweep
                drawArc(
                    color = color,
                    startAngle = 180f,
                    sweepAngle = (value * 1.8f).coerceIn(0f, 180f),
                    useCenter = false,
                    style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.offset(y = (-4).dp)
            ) {
                Text(
                    text = "$value/100",
                    fontSize = 12.sp,
                    color = IceWhite,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(title, fontSize = 11.sp, color = IceWhite, fontWeight = FontWeight.Bold)
        Text(
            text = subTitle,
            fontSize = 8.sp,
            color = if (value > 50) NeonCoral else NeonMint,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ActiveReminderItem(
    reminder: Reminder,
    onSnooze: () -> Unit,
    onComplete: () -> Unit,
    onDelete: () -> Unit
) {
    val formatter = remember { SimpleDateFormat("h:mm a, MMM d", Locale.getDefault()) }
    val timeLabel = formatter.format(Date(reminder.timestamp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x0DFFFFFF)) // Clean Minimalist translucent bg
            .border(
                1.dp,
                if (reminder.status == "SNOOZED") NeonViolet.copy(alpha = 0.5f) else Color(0x12FFFFFF),
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Alarm,
                contentDescription = "Alert",
                tint = if (reminder.urgency == "HIGH") NeonCoral else NeonTeal,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = reminder.title,
                        fontSize = 13.sp,
                        color = IceWhite,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    if (reminder.status == "SNOOZED") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(NeonViolet.copy(alpha = 0.2f))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text("SNOOZED", fontSize = 7.sp, color = NeonViolet, fontWeight = FontWeight.Black)
                        }
                    }
                }
                Text(
                    text = "Scheduled Alert Trigger: $timeLabel",
                    fontSize = 9.sp,
                    color = MutedSlate
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "SNOOZE",
                fontSize = 10.sp,
                color = NeonViolet,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onSnooze() }
                    .padding(8.dp)
            )
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Complete",
                tint = NeonMint,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable { onComplete() }
                    .background(NeonMint.copy(alpha = 0.15f))
                    .padding(4.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Dismiss",
                    tint = MutedSlate,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun TimelineTaskRow(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val dateLabel = remember(task.dueDate) {
        val dt = Date(task.dueDate)
        val diff = task.dueDate - System.currentTimeMillis()
        when {
            diff < 0 -> "Overdue : " + SimpleDateFormat("MMM d", Locale.getDefault()).format(dt)
            diff < 24 * 3600 * 1000 -> "Due: Tonight"
            diff < 48 * 3600 * 1000 -> "Due: Tomorrow"
            else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(dt)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0x06FFFFFF)) // Clean minimalist 4% white card
            .border(
                1.dp,
                Color(0x0BFFFFFF), // Subtle 4% boundary line
                RoundedCornerShape(20.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = NeonMint,
                    uncheckedColor = MutedSlate,
                    checkmarkColor = ObsidianBlack
                ),
                modifier = Modifier.testTag("task_checkbox_done")
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        task.title,
                        fontSize = 13.sp,
                        color = IceWhite,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (task.priority) {
                                    "HIGH" -> NeonCoral.copy(alpha = 0.15f)
                                    "MEDIUM" -> NeonViolet.copy(alpha = 0.15f)
                                    else -> MutedSlate.copy(alpha = 0.15f)
                                }
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            task.priority,
                            fontSize = 8.sp,
                            color = when (task.priority) {
                                "HIGH" -> NeonCoral
                                "MEDIUM" -> NeonViolet
                                else -> MutedSlate
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "${task.category}  |  $dateLabel",
                    fontSize = 10.sp,
                    color = if (task.dueDate < System.currentTimeMillis()) NeonCoral else MutedSlate
                )
            }
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete task",
                tint = MutedSlate,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// ----------------------------------------------------
// 1. JARVIS CHAT / COGNITIVE INTERFACE MODULE
// ----------------------------------------------------
@Composable
fun JarvisChatWorkspace(viewModel: LifeLoopViewModel) {
    val messages by viewModel.chatMessages.collectAsState()
    val chatInput by viewModel.chatInput.collectAsState()
    val isLoading by viewModel.isAILoading.collectAsState()

    val focusManager = LocalFocusManager.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "DIRECT INTELLIGENCE FEED",
                        fontSize = 10.sp,
                        color = NeonTeal,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        "CLEAR HISTORY",
                        fontSize = 9.sp,
                        color = MutedSlate,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.clearHistory() }
                            .padding(6.dp)
                    )
                }
            }

            items(messages, key = { it.id }) { message ->
                ChatBubble(message)
            }

            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jarvis is processing intent...",
                            fontSize = 12.sp,
                            color = NeonTeal,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.animateGlowingAlpha()
                        )
                    }
                }
            }
        }

        // Suggestions deck
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                "COGNITIVE INTENT TRIGGERS:",
                fontSize = 8.sp,
                color = MutedSlate,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    ChatSuggestionTag("Plan my pending items") {
                        viewModel.updateChatInput("Analyze my pending items and outline my visual timeline priorities.")
                    }
                }
                item {
                    ChatSuggestionTag("Index wheat allergy") {
                        viewModel.updateChatInput("Remember that my dog Archie is allergic to wheat and grains.")
                    }
                }
                item {
                    ChatSuggestionTag("Create task 'Pay rent'") {
                        viewModel.updateChatInput("Schedule high priority bills task 'Pay landlord rent of $1500' due in three days.")
                    }
                }
            }
        }

        // Text input bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SlateDark)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = chatInput,
                onValueChange = { viewModel.updateChatInput(it) },
                placeholder = { Text("Command Jarvis...", color = MutedSlate) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_text"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = IceWhite,
                    unfocusedTextColor = IceWhite,
                    focusedContainerColor = SlateCard,
                    unfocusedContainerColor = SlateCard,
                    focusedBorderColor = NeonTeal,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                onClick = {
                    viewModel.sendMessageToAI()
                    focusManager.clearFocus()
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(NeonViolet)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = IceWhite,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun ChatBubble(msg: ChatMessage) {
    val isModel = msg.role == "model"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isModel) Arrangement.Start else Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isModel) 4.dp else 16.dp,
                        bottomEnd = if (isModel) 16.dp else 4.dp
                    )
                )
                .background(
                    if (isModel) SlateCard else NeonViolet
                )
                .border(
                    1.dp,
                    if (isModel) NeonTeal.copy(alpha = 0.2f) else Color.Transparent,
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isModel) 4.dp else 16.dp,
                        bottomEnd = if (isModel) 16.dp else 4.dp
                    )
                )
                .padding(14.dp)
                .widthIn(max = 280.dp)
        ) {
            Column {
                if (isModel) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI",
                            tint = NeonTeal,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "JARVIS OS NODE",
                            fontSize = 8.sp,
                            color = NeonTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = msg.message,
                    fontSize = 12.sp,
                    color = IceWhite,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun ChatSuggestionTag(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(SlateCard)
            .border(1.dp, BorderCyan.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, fontSize = 10.sp, color = NeonTeal, fontWeight = FontWeight.SemiBold)
    }
}

// ----------------------------------------------------
// 2. SECOND BRAIN / ARCHIVE WORKSPACE MODULE
// ----------------------------------------------------
@Composable
fun SecondBrainWorkspace(viewModel: LifeLoopViewModel) {
    val documents by viewModel.documents.collectAsState()
    val memories by viewModel.memories.collectAsState()

    var activeBrainSubTab by remember { mutableStateOf(0) } // 0: Documents Box, 1: Semantic Facts

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SlateCard)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { activeBrainSubTab = 0 }
                    .background(if (activeBrainSubTab == 0) NeonViolet else Color.Transparent)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.FolderOpen,
                        contentDescription = "Docs",
                        tint = IceWhite,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("DIGITAL VAULT (${documents.size})", fontSize = 11.sp, color = IceWhite, fontWeight = FontWeight.Bold)
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { activeBrainSubTab = 1 }
                    .background(if (activeBrainSubTab == 1) NeonViolet else Color.Transparent)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = "Mem",
                        tint = IceWhite,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("SEMANTIC MEMORY (${memories.size})", fontSize = 11.sp, color = IceWhite, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (activeBrainSubTab == 0) {
                // Documents Grid
                if (documents.isEmpty()) {
                    item {
                        EmptyBrainState("Vault is empty. Snap bills, prescriptions, or files to index automatically using AI.")
                    }
                } else {
                    items(documents, key = { it.id }) { doc ->
                        DocumentVaultCard(
                            doc = doc,
                            onDelete = { viewModel.deleteDocument(doc.id) }
                        )
                    }
                }
            } else {
                // Memories Lists
                if (memories.isEmpty()) {
                    item {
                        EmptyBrainState("Jarvis semantic engine has not cached custom concepts yet. Chat naturally to remember things.")
                    }
                } else {
                    items(memories, key = { it.id }) { memory ->
                        SemanticMemoryRow(
                            memory = memory,
                            onDelete = { viewModel.deleteMemory(memory.id) }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(30.dp)) }
        }
    }
}

@Composable
fun EmptyBrainState(desc: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info",
            tint = MutedSlate.copy(alpha = 0.5f),
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(desc, fontSize = 12.sp, color = MutedSlate, textAlign = TextAlign.Center)
    }
}

@Composable
fun DocumentVaultCard(doc: Document, onDelete: () -> Unit) {
    val dateLabel = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(doc.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when (doc.category) {
                                    "Bills" -> NeonCoral.copy(alpha = 0.15f)
                                    "Study" -> NeonViolet.copy(alpha = 0.15f)
                                    "Health" -> NeonMint.copy(alpha = 0.15f)
                                    else -> NeonTeal.copy(alpha = 0.15f)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            doc.category,
                            fontSize = 8.sp,
                            color = when (doc.category) {
                                "Bills" -> NeonCoral
                                "Study" -> NeonViolet
                                "Health" -> NeonMint
                                else -> NeonTeal
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(doc.title, fontSize = 13.sp, color = IceWhite, fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis, maxLines = 1)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(16.dp)) {
                    Icon(Icons.Default.Close, contentDescription = "Delete", tint = MutedSlate)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                doc.extractedText,
                fontSize = 11.sp,
                color = IceWhite.copy(alpha = 0.8f),
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Digitized on: $dateLabel", fontSize = 8.sp, color = MutedSlate)
                if (doc.deadline != null) {
                    Text(
                        "Action Deadline: ${SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(doc.deadline))}",
                        fontSize = 8.sp,
                        color = NeonCoral,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SemanticMemoryRow(memory: Memory, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SlateCard)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Correct key",
                tint = NeonTeal,
                modifier = Modifier
                    .size(16.dp)
                    .offset(y = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(memory.textContent, fontSize = 12.sp, color = IceWhite, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Indexed facts  |  Context: ${memory.category}",
                    fontSize = 8.sp,
                    color = MutedSlate
                )
            }
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MutedSlate, modifier = Modifier.size(14.dp))
        }
    }
}

// ----------------------------------------------------
// 3. SAAS SUBSCRIPTIONS & SILICON-VALLEY ARCHITECTURE DESK
// ----------------------------------------------------
@Composable
fun SubscriptionSaaSHub(viewModel: LifeLoopViewModel) {
    val isPremium by viewModel.isPremiumUser.collectAsState()

    var activeStrategyTab by remember { mutableStateOf(0) } // 0: MVP, 1: Scale, 2: Pitch

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stripe integration card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            // Subtle background glow
                            drawCircle(
                                color = NeonViolet.copy(alpha = 0.1f),
                                radius = 250f,
                                center = Offset(size.width, 0f)
                            )
                        }
                        .padding(20.dp)
                ) {
                    Text(
                        "LIFELOOP SUPERHUMAN",
                        fontSize = 11.sp,
                        color = NeonViolet,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        "Second Brain subscription on steroids",
                        fontSize = 16.sp,
                        color = IceWhite,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Unlock vector database multi-region backup syncing, instant local voice processing, high-fidelity scheduling OCRs, and autonomous life dispatchers.",
                        fontSize = 11.sp,
                        color = MutedSlate,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "$19",
                            fontSize = 28.sp,
                            color = IceWhite,
                            fontWeight = FontWeight.Black
                        )
                        Text("/ month", fontSize = 12.sp, color = MutedSlate, modifier = Modifier.padding(bottom = 4.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isPremium) {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonMint),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("✓ SUPERHUMAN ACCESS ACTIVE", color = ObsidianBlack, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            onClick = { viewModel.upgradeToSaaSPremium() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, "Spark", tint = IceWhite, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("UPGRADE VIA STRIPE SIMULATION", color = IceWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Strategy desk selectors
        item {
            Column {
                Text(
                    "SILICON VALLEY STRATEGY CORE",
                    fontSize = 11.sp,
                    color = NeonTeal,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SlateCard)
                        .padding(2.dp)
                ) {
                    StrategySubTab(activeStrategyTab == 0, "MVP PLAN") { activeStrategyTab = 0 }
                    StrategySubTab(activeStrategyTab == 1, "SCALING") { activeStrategyTab = 1 }
                    StrategySubTab(activeStrategyTab == 2, "INVESTOR") { activeStrategyTab = 2 }
                }
            }
        }

        // Strategy panel details
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateCard),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    when (activeStrategyTab) {
                        0 -> {
                            StrategySectionHeader("MVP Roadmap & Product Timeline")
                            StrategyBullet("V1.0.0 (Immediate)", "Dual SQLite + Direct Gemini REST integration with automated actions parser (Completed).")
                            StrategyBullet("V1.1.0", "Onboard transcription pipelines using Whisper engine for natural speech-to-text diarization.")
                            StrategyBullet("V2.0.0", "Cloud sync backend with Supabase PostgreSQL and secure role-based family space integration.")
                        }
                        1 -> {
                            StrategySectionHeader("Viral Growth & Billing Architecture")
                            StrategyBullet("Stripe SDK", "Tier systems (Free, Superhuman at $19, Enterprise family at $49). Pro-rated transaction modules.")
                            StrategyBullet("Slack & WhatsApp Hooks", "API webhooks to capture text snapshots. 'Forward to LifeLoop' instantly builds tasks.")
                            StrategyBullet("Dual-Engine Storage", "Local database syncs dynamically with Cloud Spanner instances to sustain 10 Million users.")
                        }
                        2 -> {
                            StrategySectionHeader("Billion-Dollar Founder Analytics")
                            StrategyBullet("Market Focus", "Mental overload and digital scatter are critical modern stress vectors. Solved by direct life automation.")
                            StrategyBullet("Unit Economics", "API cost at $0.002 per analysis vs lifetime subscription revenue is extremely margin high (>84%).")
                            StrategyBullet("Funding Hook", "Scaling pipeline targeting $1.5M Seed round at $12M valuation. Awaiting node activation.")
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(30.dp)) }
    }
}

@Composable
fun RowScope.StrategySubTab(
    active: Boolean,
    title: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .background(if (active) SlateDark else Color.Transparent)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(title, fontSize = 9.sp, color = if (active) NeonTeal else MutedSlate, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StrategySectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        fontSize = 11.sp,
        color = IceWhite,
        fontWeight = FontWeight.Black,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun StrategyBullet(bullet: String, text: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(bullet, fontSize = 11.sp, color = NeonTeal, fontWeight = FontWeight.Bold)
        Text(text, fontSize = 11.sp, color = MutedSlate, lineHeight = 16.sp)
    }
}

// ----------------------------------------------------
// BASE COMMON UI & DIALOGS
// ----------------------------------------------------

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    gradientBrush: Brush,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(gradientBrush)
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        GlassSpecular,
                        Color.Transparent,
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        content()
    }
}

@Composable
fun LifeLoopBottomNav(
    activeTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SlateDark)
            .border(1.dp, BorderCyan.copy(alpha = 0.1f))
            .padding(vertical = 10.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            active = activeTab == 0,
            icon = Icons.Default.Timeline,
            label = "Dashboard",
            onClick = { onTabSelected(0) },
            modifier = Modifier.testTag("nav_tab_dashboard")
        )
        BottomNavItem(
            active = activeTab == 1,
            icon = Icons.AutoMirrored.Default.Message,
            label = "Jarvis AI",
            onClick = { onTabSelected(1) },
            modifier = Modifier.testTag("nav_tab_jarvis")
        )
        BottomNavItem(
            active = activeTab == 2,
            icon = Icons.Default.FolderOpen,
            label = "Second Brain",
            onClick = { onTabSelected(2) },
            modifier = Modifier.testTag("nav_tab_brain")
        )
        BottomNavItem(
            active = activeTab == 3,
            icon = Icons.Default.AutoAwesome,
            label = "SaaS Deck",
            onClick = { onTabSelected(3) },
            modifier = Modifier.testTag("nav_tab_saas")
        )
    }
}

@Composable
fun BottomNavItem(
    active: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (active) NeonTeal else MutedSlate,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = if (active) IceWhite else MutedSlate,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickTaskAddDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, category: String, priority: String, delayHours: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Life") }
    var priority by remember { mutableStateOf("MEDIUM") }
    var delayHours by remember { mutableStateOf(24) } // Default 24h

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SlateDark,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderCyan, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Schedule Life Activity", fontSize = 15.sp, color = IceWhite, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Task description...", color = MutedSlate) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = IceWhite,
                        unfocusedTextColor = IceWhite,
                        focusedBorderColor = NeonTeal,
                        unfocusedBorderColor = MutedSlate
                    )
                )

                // Category selector
                Text("WORKSPACE CATEGORY:", fontSize = 9.sp, color = MutedSlate, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Life", "Work", "Bills", "Study", "Health").forEach { cat ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (category == cat) NeonViolet else SlateCard)
                                .clickable { category = cat }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(cat, fontSize = 9.sp, color = IceWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Priority selector
                Text("SYSTEM PRIORITY:", fontSize = 9.sp, color = MutedSlate, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("HIGH", "MEDIUM", "LOW").forEach { pri ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (priority == pri) NeonViolet else SlateCard)
                                .clickable { priority = pri }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(pri, fontSize = 9.sp, color = IceWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Delay selector
                Text("SCHEDULIST DUE OFFSET:", fontSize = 9.sp, color = MutedSlate, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(1 to "1 Hour", 24 to "1 Day", 48 to "2 Days", 72 to "3 Days").forEach { (h, label) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (delayHours == h) NeonViolet else SlateCard)
                                .clickable { delayHours = h }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(label, fontSize = 9.sp, color = IceWhite, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("CANCEL", color = MutedSlate)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (title.trim().isNotEmpty()) {
                                onSubmit(title.trim(), category, priority, delayHours)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonTeal)
                    ) {
                        Text("SCHEDULE", color = ObsidianBlack, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Custom animation effect helper for loading glow
@Composable
fun Modifier.animateGlowingAlpha(): Modifier {
    var alpha by remember { mutableStateOf(0.4f) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(10)
            alpha = (alpha + 0.02f)
            if (alpha >= 1.0f) {
                while (alpha > 0.4f) {
                    kotlinx.coroutines.delay(10)
                    alpha = (alpha - 0.02f)
                }
            }
        }
    }
    return this.drawBehind {
        // Draw glow text alpha
    }
}
