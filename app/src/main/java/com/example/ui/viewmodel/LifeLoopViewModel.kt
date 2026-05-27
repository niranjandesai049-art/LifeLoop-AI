package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.LifeLoopDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.Document
import com.example.data.model.Memory
import com.example.data.model.Reminder
import com.example.data.model.Task
import com.example.data.repository.LifeLoopAction
import com.example.data.repository.LifeLoopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class LifeLoopViewModel(
    application: Application,
    private val repository: LifeLoopRepository
) : AndroidViewModel(application) {

    // --- Tab Navigation & View States ---
    private val _currentTab = MutableStateFlow(0) // 0: Dashboard, 1: Chat Jarvis, 2: Document Box, 3: subscription
    val currentTab = _currentTab.asStateFlow()

    private val _chatInput = MutableStateFlow("")
    val chatInput = _chatInput.asStateFlow()

    private val _isAILoading = MutableStateFlow(false)
    val isAILoading = _isAILoading.asStateFlow()

    // --- Subscription Plan State (SaaS Simulator) ---
    private val _isPremiumUser = MutableStateFlow(false)
    val isPremiumUser = _isPremiumUser.asStateFlow()

    // --- Active Database Flows ---
    val tasks: StateFlow<List<Task>> = repository.allTasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val reminders: StateFlow<List<Reminder>> = repository.allReminders.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val memories: StateFlow<List<Memory>> = repository.allMemories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val documents: StateFlow<List<Document>> = repository.allDocuments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val chatMessages: StateFlow<List<ChatMessage>> = repository.allChatMessages.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Smart Life Analytics State (Live calculations computed dynamically) ---
    val analyticsState: StateFlow<LifeAnalytics> = combine(
        tasks,
        reminders
    ) { taskList, reminderList ->
        calculateAnalytics(taskList, reminderList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LifeAnalytics()
    )

    init {
        // Run bootstrap check
        viewModelScope.launch {
            repository.allChatMessages.collect { messages ->
                if (messages.isEmpty()) {
                    seedStarterDatabase()
                }
            }
        }
    }

    private fun seedStarterDatabase() = viewModelScope.launch {
        // 1. Core Model Welcomer
        repository.insertChatMessage(
            ChatMessage(
                role = "model",
                message = "Greetings, Human. I am Jarvis, core neural grid of LifeLoop AI. Digital, mental, and physical schedules successfully networked.\n\nAll nodes nominal. I have indexed your initial life assets. Ask me to schedule tasks, OCR upload cards, track bio routines, or ask context-aware questions. How can we optimize your neural load today?"
            )
        )

        // 2. Starter Tasks
        repository.insertTask(
            Task(
                title = "Design Stanford Term Project Outline",
                details = "Draft modular NLP pipeline outline including transformer embedding layers.",
                dueDate = System.currentTimeMillis() + (24 * 60 * 60 * 1000), // Tomorrow
                priority = "HIGH",
                category = "Study"
            )
        )
        repository.insertTask(
            Task(
                title = "Settle AWS Cloud Hosting Balance",
                details = "Pending billing period transaction verification. Total $42.10.",
                dueDate = System.currentTimeMillis() + (48 * 60 * 60 * 1000), // In 2 days
                priority = "HIGH",
                category = "Bills"
            )
        )
        repository.insertTask(
            Task(
                title = "Morning High-Intensity Cardio Loop",
                details = "Maintain daily heart rate peak of 145 bpm for 22 minutes.",
                dueDate = System.currentTimeMillis() + (12 * 60 * 60 * 1000), // Tonight / early tomorrow
                priority = "MEDIUM",
                category = "Health",
                isRecurring = true
            )
        )

        // 3. Starter Reminders
        repository.insertReminder(
            Reminder(
                title = "Take Albuterol Dose (Morning Inhaler)",
                timestamp = System.currentTimeMillis() + (10 * 60 * 60 * 1000),
                urgency = "HIGH",
                isAutoDetected = true
            )
        )
        repository.insertReminder(
            Reminder(
                title = "Client Follow-up: Apex Agency Proposal",
                timestamp = System.currentTimeMillis() + (36 * 60 * 60 * 1000),
                urgency = "NORMAL",
                isAutoDetected = false
            )
        )

        // 4. Starter Memories
        repository.insertMemory(
            Memory(textContent = "Stanford Student Registration ID: 228919020", category = "Knowledge")
        )
        repository.insertMemory(
            Memory(textContent = "Emergency Contact: Sarah Jenkins (Wife) +1-415-555-0199", category = "Personal")
        )
    }

    private fun calculateAnalytics(taskList: List<Task>, reminderList: List<Reminder>): LifeAnalytics {
        if (taskList.isEmpty()) return LifeAnalytics()

        val completed = taskList.count { it.isCompleted }
        val total = taskList.size
        val productivity = ((completed.toFloat() / total.toFloat()) * 100).toInt()

        // Stress scoring (dependent on outstanding high priority tasks + snoozed reminders)
        val outstandingHigh = taskList.count { !it.isCompleted && it.priority == "HIGH" }
        val outstandingMed = taskList.count { !it.isCompleted && it.priority == "MEDIUM" }
        val activeSnoozed = reminderList.count { it.status == "SNOOZED" }

        val calculatedStress = ((outstandingHigh * 15 + outstandingMed * 8 + activeSnoozed * 10)
            .coerceIn(0, 100))

        // Focus Score (high percentage if finishing HIGH priority tasks)
        val highTotal = taskList.count { it.priority == "HIGH" }
        val highCompleted = taskList.count { it.isCompleted && it.priority == "HIGH" }
        val focus = if (highTotal > 0) {
            ((highCompleted.toFloat() / highTotal.toFloat()) * 100).toInt().coerceIn(10, 100)
        } else {
            80
        }

        // Efficiency Rating (task status balance)
        val efficiency = (100 - (taskList.count { !it.isCompleted && it.dueDate < System.currentTimeMillis() } * 20))
            .coerceIn(10, 100)

        return LifeAnalytics(
            productivityScore = productivity,
            stressScore = calculatedStress,
            focusIndex = focus,
            efficiencyPercent = efficiency
        )
    }

    // --- Action Handlers ---

    fun changeTab(tabIndex: Int) {
        _currentTab.value = tabIndex
    }

    fun updateChatInput(input: String) {
        _chatInput.value = input
    }

    fun toggleTaskCompletion(task: Task) = viewModelScope.launch {
        repository.updateTask(task.copy(isCompleted = !task.isCompleted))
    }

    fun submitTaskDirectly(title: String, category: String, dueDateOffsetHours: Int, priority: String) = viewModelScope.launch {
        repository.insertTask(
            Task(
                title = title,
                details = "Manually appended",
                dueDate = System.currentTimeMillis() + (dueDateOffsetHours * 60 * 60 * 1000),
                priority = priority,
                category = category
            )
        )
    }

    fun deleteTask(id: Int) = viewModelScope.launch {
        repository.deleteTask(id)
    }

    fun snoozeReminder(reminder: Reminder) = viewModelScope.launch {
        repository.updateReminder(
            reminder.copy(
                timestamp = System.currentTimeMillis() + (15 * 60 * 1000), // Snooze 15m
                status = "SNOOZED"
            )
        )
    }

    fun resolveReminder(reminder: Reminder) = viewModelScope.launch {
        repository.updateReminder(reminder.copy(status = "COMPLETED"))
    }

    fun dismissReminder(id: Int) = viewModelScope.launch {
        repository.deleteReminder(id)
    }

    fun addManualReminder(title: String, dateOffsetHours: Int, urgency: String) = viewModelScope.launch {
        repository.insertReminder(
            Reminder(
                title = title,
                timestamp = System.currentTimeMillis() + (dateOffsetHours * 60 * 60 * 1000),
                urgency = urgency,
                status = "ACTIVE"
            )
        )
    }

    fun deleteMemory(id: Int) = viewModelScope.launch {
        repository.deleteMemory(id)
    }

    fun deleteDocument(id: Int) = viewModelScope.launch {
        repository.deleteDocument(id)
    }

    fun upgradeToSaaSPremium() {
        _isPremiumUser.value = true
    }

    fun clearHistory() = viewModelScope.launch {
        repository.clearChatHistory()
        // Re-greets
        repository.insertChatMessage(
            ChatMessage(role = "model", message = "All memory channels refreshed. Direct link open. How can I serve you?")
        )
    }

    // --- Submitting User Message to Jarvis AI loop ---
    fun sendMessageToAI() {
        val message = _chatInput.value.trim()
        if (message.isEmpty()) return

        _chatInput.value = ""
        _isAILoading.value = true

        viewModelScope.launch {
            val responseResult = repository.askAssistant(
                userMessage = message,
                currentTasks = tasks.value,
                currentReminders = reminders.value,
                currentMemories = memories.value
            )
            _isAILoading.value = false

            // Process automated tool-calls if Jarvis dispatched any
            responseResult.action?.let { action ->
                processAIAction(action)
            }
        }
    }

    private suspend fun processAIAction(action: LifeLoopAction) {
        when (action) {
            is LifeLoopAction.CreateTask -> {
                val delayTime = action.daysFromNow * 24 * 60 * 60 * 1000L
                repository.insertTask(
                    Task(
                        title = action.title,
                        details = action.details,
                        dueDate = System.currentTimeMillis() + delayTime,
                        priority = action.priority,
                        category = action.category
                    )
                )
                repository.insertChatMessage(
                    ChatMessage(
                        role = "model",
                        message = "[LifeLoop Automated Task]: Scheduled task '${action.title}' for ${action.daysFromNow} day(s) from now."
                    )
                )
            }
            is LifeLoopAction.CreateReminder -> {
                val delayTime = (action.daysFromNow * 24 * 3600 + action.hoursFromNow * 3600) * 1000L
                repository.insertReminder(
                    Reminder(
                        title = action.title,
                        timestamp = System.currentTimeMillis() + delayTime,
                        urgency = if (action.urgency == "URGENT") "HIGH" else "NORMAL",
                        isAutoDetected = true
                    )
                )
                repository.insertChatMessage(
                    ChatMessage(
                        role = "model",
                        message = "[LifeLoop Cognitive Automated Reminder]: Set notification alert for '${action.title}' in the queue."
                    )
                )
            }
            is LifeLoopAction.SaveMemory -> {
                repository.insertMemory(
                    Memory(textContent = action.textContent, category = action.category)
                )
                repository.insertChatMessage(
                    ChatMessage(
                        role = "model",
                        message = "[LifeLoop Intelligent Indexer]: Saved new insight securely to secondary database: '${action.textContent}' [${action.category}]."
                    )
                )
            }
        }
    }

    // --- File Capture & OCR workflow ---
    fun simulateFileUpload(fileName: String) {
        _isAILoading.value = true
        _currentTab.value = 1 // Go to chat to show OCR output logs

        viewModelScope.launch {
            repository.insertChatMessage(
                ChatMessage(
                    role = "user",
                    message = "Simulated Upload File: '$fileName'. Processing OCR pipelines and intent classification..."
                )
            )

            val mime = when {
                fileName.endsWith(".pdf", true) -> "application/pdf"
                else -> "image/jpeg"
            }

            // Calls processDocumentWithGemini (which extracts details via LLM or simulated fallback)
            val ocrResponseResult = repository.processDocumentWithGemini(
                title = fileName,
                mimeType = mime,
                base64Data = null // Simulated file in repo
            )

            _isAILoading.value = false

            when (ocrResponseResult) {
                is com.example.data.repository.DocumentProcessResult.Success -> {
                    val p = ocrResponseResult.payload
                    val confirmationText = """
                        [LifeLoop Document Intelligence Engine Active]
                        ✓ Processed: **${p.title}**
                        ✓ Categorized: **${p.category}**
                        ✓ Urgency: **${p.urgency}**
                        ✓ Detected Deadline: **${p.deadlineText ?: "Open"}**
                        
                        **Analysis**: ${p.summary}
                        
                        *Jarvis Action*: Automatically created task "Review/Action: ${p.title}" and loaded scheduled reminders.
                    """.trimIndent()
                    repository.insertChatMessage(ChatMessage(role = "model", message = confirmationText))
                }
                is com.example.data.repository.DocumentProcessResult.FallbackSuccess -> {
                    val p = ocrResponseResult.payload
                    val confirmationText = """
                        [LifeLoop Document Intelligence Engine (Offline Cache)]
                        ✓ Processed: **${p.title}**
                        ✓ Categorized: **${p.category}** (Calculated locally)
                        ✓ Urgency: **${p.urgency}**
                        ✓ Detected Deadline: **${p.deadlineText ?: "Open"}**
                        
                        **Analysis Summary**: ${p.summary}
                        *(Note: Connected to onboard local backup systems for instant offline indexing)*
                    """.trimIndent()
                    repository.insertChatMessage(ChatMessage(role = "model", message = confirmationText))
                }
            }
        }
    }
}

// --- Analytics Data Object ---
data class LifeAnalytics(
    val productivityScore: Int = 0,
    val stressScore: Int = 0,
    val focusIndex: Int = 80,
    val efficiencyPercent: Int = 90
)

// --- ViewModel Factory ---
class LifeLoopViewModelFactory(
    private val application: Application,
    private val repository: LifeLoopRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LifeLoopViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LifeLoopViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
