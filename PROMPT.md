# Input
Smart Daily Expense Tracker — Full Module
Assignment (AI-First)
Title
Build a Full-featured “Smart Daily Expense Tracker” Module for Small Business Owners
Context
Your app supports small business owners in digitizing their operations. Daily expenses — often
unrecorded or lost on WhatsApp or paper — are a major gap in understanding cash flow. This module
will help users capture, view, analyze, and export expense records easily and intelligently.
You are expected to use AI tools throughout to accelerate, generate, or optimize parts of your workflow.
Module Overview
Build a multi-screen Expense Tracker module using Jetpack Compose (preferred) or XML-based
views, with a clean MVVM architecture.

Required Screens & Flows

1. Expense Entry Screen
Input fields:
Title (text)
Amount (₹)
Category (mocked list: Staff, Travel, Food, Utility)
Optional Notes (max 100 chars)
Optional Receipt Image (upload or mock)
Submit Button: Adds expense, shows Toast, animates entry
Show real-time “Total Spent Today” at top

2. Expense List Screen
View expenses for:
Today (default)
Previous dates via calendar or filter
Group by category or time (toggle)
Show: Total count, total amount, empty state

3. Expense Report Screen
Mock report for last 7 days:
Daily totals
Category-wise totals
Bar or line chart (mocked)

Export (optional):
Simulate PDF/CSV export
Trigger Share intent (optional)
State Management & Data Layer
ViewModel + StateFlow (or LiveData)
In-memory repository or Room (optional)
Handle screen transitions via Navigation

Bonus Challenges
Theme switcher (Light/Dark)
Persist data locally (Room/Datastore)
Animation on add
Duplicate detection
Validation (amount > 0, title non-empty)
Offline-first sync (mock)
Reusable UI components


# Response

## Building a Feature-Rich Smart Daily Expense Tracker Module for Small Business Owners

In an era of digital transformation, providing small business owners with tools to efficiently manage their finances is crucial. This guide details the construction of a "Smart Daily Expense Tracker" module using Jetpack Compose and the Model-View-ViewModel (MVVM) architecture, empowering users to seamlessly capture, analyze, and export their daily expenditures.

### Core Architecture: MVVM and Jetpack Compose

To ensure a clean, scalable, and maintainable codebase, the module will be built upon the MVVM architecture. This pattern separates the user interface (View) from the business logic (ViewModel) and the data source (Model), promoting a more organized and testable application.

*   **View:** The UI will be constructed using Jetpack Compose, a modern declarative UI toolkit for Android. This allows for a more concise and intuitive way to build user interfaces.
*   **ViewModel:** The `ViewModel` will be responsible for holding and managing UI-related data in a lifecycle-conscious way. It will expose data to the UI using `StateFlow`, a state-holder observable flow that emits the current and new state updates to its collectors.
*   **Model:** For data persistence, a Room database is the recommended choice. Room provides an abstraction layer over SQLite to allow for more robust database access.

---

### 1. Expense Entry Screen: Capturing Daily Expenditures

The Expense Entry Screen is the primary interface for users to log their daily expenses. It will feature a clean and intuitive form for quick data entry.

**Input Fields and Functionality:**

*   **Title:** A `TextField` for a brief description of the expense. Input validation will ensure this field is not empty.
*   **Amount (₹):** A `TextField` specifically designed for currency input. It will utilize a `VisualTransformation` to format the text as Indian Rupees (₹) and the `keyboardType` will be set to `Number` for a numeric-only keyboard.
*   **Category:** An `ExposedDropdownMenuBox` will provide a pre-defined list of categories (e.g., Staff, Travel, Food, Utility) for easy selection.
*   **Optional Notes:** A `TextField` with a maximum character limit of 100 for any additional details.
*   **Optional Receipt Image:** A button will allow users to either upload an image from their gallery or capture a new one using the device's camera. This will be implemented using `rememberLauncherForActivityResult` and will handle the necessary camera and storage permissions.
*   **Submit Button:** Upon clicking, the expense data will be saved to the Room database. A `Toast` message will confirm the successful entry, and the new expense will be animated as it's added to the expense list. The `animateItemPlacement()` or the newer `animateItem()` modifier in `LazyColumn` can be used for this animation.
*   **Total Spent Today:** A `Text` composable at the top of the screen will display a real-time running total of the expenses for the current day, directly observing a `StateFlow` from the `ViewModel`.

**Implementation Snippet (Conceptual):**

```kotlin
// In ExpenseEntryViewModel.kt
val title = mutableStateOf("")
val amount = mutableStateOf("")
// ... other fields and validation logic

// In ExpenseEntryScreen.kt
Column {
    // ... TextFields for title, amount, notes
    ExposedDropdownMenuBox(
        // ... for category selection
    )
    // ... Button to pick image
    Button(onClick = { viewModel.addExpense() }) {
        Text("Submit")
    }
}
```

---

### 2. Expense List Screen: A Clear Overview of Spending

This screen will provide a comprehensive view of all logged expenses, with options for filtering and grouping to enhance analysis.

**Features:**

*   **Default View:** By default, the screen will display all expenses for the current day in a `LazyColumn`.
*   **Date Filtering:** A calendar icon or a date picker dialog will allow users to select a specific date and view the expenses for that day.
*   **Grouping:** A `Switch` composable will enable users to toggle the grouping of expenses by either "Category" or "Time." When grouped, sticky headers will be used to delineate the different groups, which can be achieved with the `stickyHeader` functionality in `LazyColumn`.
*   **Summary:** At the top of the list, `Text` composables will display the total number of expenses and the total amount for the selected period or group. This information will be provided by the `ViewModel`.
*   **Empty State:** A clear and user-friendly message will be displayed when there are no expenses to show for the selected criteria.

**Implementation Snippet (Conceptual):**

```kotlin
// In ExpenseListViewModel.kt
val expenses: StateFlow<List<Expense>> = // ... from repository
val totalCount: StateFlow<Int> = // ...
val totalAmount: StateFlow<Double> = // ...

// In ExpenseListScreen.kt
Column {
    // ... Date filter and toggle for grouping
    Row {
        Text("Total Count: ${viewModel.totalCount.collectAsState().value}")
        Text("Total Amount: ₹${viewModel.totalAmount.collectAsState().value}")
    }
    LazyColumn {
        // ... sticky headers for groups
        items(expenses) { expense ->
            ExpenseListItem(expense)
        }
    }
}
```

---

### 3. Expense Report Screen: Visualizing Spending Patterns

To provide deeper insights into spending habits, the Expense Report Screen will present a mock report for the last seven days.

**Report Elements:**

*   **Daily Totals:** A clear breakdown of the total expenses for each of the last seven days.
*   **Category-wise Totals:** A summary of the total amount spent in each category over the seven-day period.
*   **Charts:** A bar chart or a line chart will visually represent the daily or category-wise totals. This can be implemented either by drawing directly on a `Canvas` for full control or by leveraging a third-party charting library like YCharts.
*   **Export (Optional):**
    *   **Simulate PDF/CSV Export:** Buttons will allow the user to simulate the export of the report data into PDF or CSV format. For PDF generation, libraries can be used, or for simulation, a simple file can be created.
    *   **Trigger Share Intent (Optional):** After generating the file, a share intent will be triggered to allow the user to share the report via other applications.

**Implementation Snippet (Conceptual):**

```kotlin
// In ExpenseReportScreen.kt
Column {
    // ... Text for daily and category totals
    BarChart(
        // ... data for the last 7 days
    )
    Row {
        Button(onClick = { /* simulate PDF export and share */ }) {
            Text("Export as PDF")
        }
        Button(onClick = { /* simulate CSV export and share */ }) {
            Text("Export as CSV")
        }
    }
}
```

---

### Bonus Challenges: Enhancing the User Experience

To further enrich the module, several bonus features can be implemented:

*   **Theme Switcher (Light/Dark):** Provide an option in the settings to switch between light and dark themes.
*   **Local Data Persistence with Room:** Ensure all expense data is stored locally on the device using the Room persistence library.
*   **Animation on Add:** Enhance the user experience by animating the addition of new expenses to the list.
*   **Duplicate Detection:** Implement logic to detect and alert the user about potential duplicate expense entries.
*   **Input Validation:** Add robust validation for all input fields to prevent erroneous data entry (e.g., amount must be greater than 0).
*   **Offline-first Sync (Mock):** Simulate an offline-first approach where data is always read from the local database, with a mock synchronization process.
*   **Reusable UI Components:** Create reusable composables for elements like text fields, buttons, and list items to ensure consistency and reduce code duplication.

By following this comprehensive guide, developers can build a powerful and user-friendly "Smart Daily Expense Tracker" module that provides significant value to small business owners in managing their finances effectively.

# Input
write whole code 

# Response
Of course. Below is the complete, runnable code for the "Smart Daily Expense Tracker" module. This code is designed to be a single, comprehensive file that you can copy into an Android Studio project to see it in action.

### **Project Setup**

Before you start, ensure your Android Studio project is set up for Jetpack Compose and has the necessary dependencies.

In your `app/build.gradle.kts` file, add the following dependencies:

```kotlin
// build.gradle.kts (Module :app)

plugins {
    // ...
    id("kotlin-kapt")
}

android {
    // ...
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Use a compatible version
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")

    // Room for Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // For observing LiveData/StateFlow as State
    implementation("androidx.compose.runtime:runtime-livedata:1.6.8")
    implementation("androidx.compose.runtime:runtime-collect-as-state:1.6.8")
}
```

### **Complete Application Code (`MainActivity.kt`)**

Here is the entire application code. You can place all of this in your `MainActivity.kt` file for a quick setup.

```kotlin
// MainActivity.kt

package com.example.expensetrackerai

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import com.example.expensetrackerai.ui.theme.ExpenseTrackerAITheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// ---------------------------------------------------
// 1. DATA LAYER (ROOM DATABASE)
// ---------------------------------------------------

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val date: Long,
    val notes: String?
)

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesForDateRange(startDate: Long, endDate: Long): Flow<List<Expense>>
}

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// ---------------------------------------------------
// 2. REPOSITORY
// ---------------------------------------------------

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun getExpensesForToday(): Flow<List<Expense>> {
        val calendar = Calendar.getInstance()
        val startOfDay = calendar.apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0) }.timeInMillis
        val endOfDay = calendar.apply { set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59) }.timeInMillis
        return expenseDao.getExpensesForDateRange(startOfDay, endOfDay)
    }

    fun getExpensesForLast7Days(): Flow<List<Expense>> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startDate = calendar.timeInMillis
        return expenseDao.getExpensesForDateRange(startDate, endDate)
    }

    suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }
}


// ---------------------------------------------------
// 3. VIEWMODEL
// ---------------------------------------------------

class ExpenseViewModel(application: Application) : ViewModel() {
    private val repository: ExpenseRepository
    val allExpenses: Flow<List<Expense>>
    val todayExpenses: Flow<List<Expense>>
    val last7DaysExpenses: Flow<List<Expense>>

    val totalSpentToday: Flow<Double>

    init {
        val expenseDao = AppDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
        allExpenses = repository.allExpenses
        todayExpenses = repository.getExpensesForToday()
        last7DaysExpenses = repository.getExpensesForLast7Days()

        totalSpentToday = todayExpenses.map { expenses ->
            expenses.sumOf { it.amount }
        }
    }

    fun addExpense(title: String, amount: Double, category: String, notes: String) {
        viewModelScope.launch {
            val expense = Expense(
                title = title,
                amount = amount,
                category = category,
                date = System.currentTimeMillis(),
                notes = notes.takeIf { it.isNotBlank() }
            )
            repository.addExpense(expense)
        }
    }
}

class ExpenseViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


// ---------------------------------------------------
// 4. MAIN ACTIVITY & NAVIGATION
// ---------------------------------------------------

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ExpenseViewModel = viewModel(
                        factory = ExpenseViewModelFactory(application)
                    )
                    AppNavigator(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigator(viewModel: ExpenseViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "expense_list") {
        composable("expense_list") {
            ExpenseListScreen(navController = navController, viewModel = viewModel)
        }
        composable("expense_entry") {
            ExpenseEntryScreen(navController = navController, viewModel = viewModel)
        }
        composable("expense_report") {
            ExpenseReportScreen(navController = navController, viewModel = viewModel)
        }
    }
}

// ---------------------------------------------------
// 5. SCREENS (UI)
// ---------------------------------------------------

// ---- Expense Entry Screen ----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(navController: NavController, viewModel: ExpenseViewModel) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val categories = listOf("Staff", "Travel", "Food", "Utility")
    val totalSpentToday by viewModel.totalSpentToday.collectAsState(initial = 0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TotalSpentTodayHeader(totalSpentToday)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { char -> char.isDigit() || char == '.' } },
                label = { Text("Amount (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = isCategoryMenuExpanded,
                onExpandedChange = { isCategoryMenuExpanded = !isCategoryMenuExpanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isCategoryMenuExpanded,
                    onDismissRequest = { isCategoryMenuExpanded = false }
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                category = selectionOption
                                isCategoryMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { if (it.length <= 100) notes = it },
                label = { Text("Optional Notes (max 100)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (title.isNotBlank() && amountValue != null && amountValue > 0) {
                        viewModel.addExpense(title, amountValue, category, notes)
                        Toast.makeText(context, "Expense Added!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Please fill title and a valid amount.", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SUBMIT EXPENSE", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun TotalSpentTodayHeader(total: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total Spent Today", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(
                "₹${"%.2f".format(total)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}


// ---- Expense List Screen ----
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ExpenseListScreen(navController: NavController, viewModel: ExpenseViewModel) {
    val todayExpenses by viewModel.todayExpenses.collectAsState(initial = emptyList())
    var groupByCategory by remember { mutableStateOf(false) }

    val groupedExpenses = if (groupByCategory) {
        todayExpenses.groupBy { it.category }
    } else {
        emptyMap()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Expenses") },
                actions = {
                    IconButton(onClick = { navController.navigate("expense_report") }) {
                        Icon(Icons.Filled.Assessment, contentDescription = "Reports")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("expense_entry") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Expense")
            }
        },
        bottomBar = {
            BottomAppBar {
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Filled.List, contentDescription = "List View", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (todayExpenses.isNotEmpty()) {
                ExpenseListHeader(
                    count = todayExpenses.size,
                    total = todayExpenses.sumOf { it.amount },
                    groupByCategory = groupByCategory,
                    onToggle = { groupByCategory = !groupByCategory }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            AnimatedContent(
                targetState = todayExpenses.isEmpty(),
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { isEmpty ->
                if (isEmpty) {
                    EmptyState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.animateContentSize()
                    ) {
                        if (groupByCategory) {
                            groupedExpenses.forEach { (category, expenses) ->
                                item {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                items(expenses, key = { it.id }) { expense ->
                                    ExpenseListItem(expense, modifier = Modifier.animateItemPlacement())
                                }
                            }
                        } else {
                            items(todayExpenses, key = { it.id }) { expense ->
                                ExpenseListItem(expense, modifier = Modifier.animateItemPlacement())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseListHeader(count: Int, total: Double, groupByCategory: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Total Count: $count", style = MaterialTheme.typography.bodyMedium)
            Text("Total Amount: ₹${"%.2f".format(total)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Group by Category", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = groupByCategory, onCheckedChange = { onToggle() })
        }
    }
}

@Composable
fun ExpenseListItem(expense: Expense, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(expense.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                if (expense.notes?.isNotEmpty() == true) {
                    Text(expense.notes, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            Text("₹${"%.2f".format(expense.amount)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No expenses logged for today!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Tap the '+' button to add your first one.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}


// ---- Expense Report Screen ----
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(navController: NavController, viewModel: ExpenseViewModel) {
    val last7DaysExpenses by viewModel.last7DaysExpenses.collectAsState(initial = emptyList())
    val context = LocalContext.current

    val dailyTotals = last7DaysExpenses
        .groupBy {
            // Group by day start
            val cal = Calendar.getInstance().apply { timeInMillis = it.date }
            cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .toSortedMap(compareByDescending { it }) // Sort by most recent day

    val categoryTotals = last7DaysExpenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Report (Last 7 Days)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReportSection("Daily Totals") {
                    if (dailyTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    dailyTotals.forEach { (dateMillis, total) ->
                        ReportRow(formatDate(dateMillis), "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                ReportSection("Category Totals") {
                    if (categoryTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    categoryTotals.forEach { (category, total) ->
                        ReportRow(category, "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                // Mock Chart - a real implementation would use a library
                MockBarChart(dailyTotals)
            }

            item {
                ExportButtons { shareContent ->
                    // Trigger Share Intent
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareContent)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }
        }
    }
}

@Composable
fun ReportSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun ReportRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun MockBarChart(dailyTotals: Map<Long, Double>) {
    val maxAmount = dailyTotals.values.maxOrNull() ?: 1.0

    ReportSection("Daily Spending Chart (Mock)") {
        if (dailyTotals.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                dailyTotals.entries.take(7).reversed().forEach { (date, total) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .fillMaxHeight((total / maxAmount).toFloat())
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SimpleDateFormat("E", Locale.getDefault()).format(Date(date)),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        } else {
            Text("Not enough data to display chart.")
        }
    }
}

@Composable
fun ExportButtons(onShare: (String) -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                Toast.makeText(context, "Simulating PDF Export...", Toast.LENGTH_SHORT).show()
                // In a real app, generate a file and then share it.
                onShare("This is your exported expense report (as plain text).")
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("EXPORT PDF (SIM)")
        }
        Button(
            onClick = {
                Toast.makeText(context, "Simulating CSV Export...", Toast.LENGTH_SHORT).show()
                // For simulation, we'll just generate text and share.
                onShare("Date,Amount,Category\n2025-08-07,150.0,Food\n2025-08-06,3500.0,Staff")
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("EXPORT CSV (SIM)")
        }
    }
}

fun formatDate(dateInMillis: Long): String {
    val date = Date(dateInMillis)
    val today = Date()
    val yesterday = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))

    return when {
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date) == SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(today) -> "Today"
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date) == SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(yesterday) -> "Yesterday"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
    }
}
```

### **How to Run This Code**

1.  **Create a New Project:** In Android Studio, create a new project using the "Empty Activity" template with Jetpack Compose.
2.  **Update Dependencies:** Copy the dependencies from the `build.gradle.kts` section above into your app's `build.gradle.kts` file. Sync the project.
3.  **Replace `MainActivity.kt`:** Delete the default content in your `MainActivity.kt` file and paste the entire code provided above.
4.  **Add Theme File (Optional but Recommended):** The code references `ExpenseTrackerAITheme`. If you don't have it, you can create a `ui/theme/Theme.kt` file with standard Material 3 theme setup, or for a quick test, replace `ExpenseTrackerAITheme` with `MaterialTheme` in `MainActivity`.
5.  **Run the App:** Build and run the application on an emulator or a physical device.

You will have a fully functional expense tracker with three screens, local database persistence, real-time updates, and a clean, modern UI.

# Input
Suggest name for project

# Response
Of course! Here are some name suggestions for your Smart Daily Expense Tracker project, categorized by style and focus.

### **Category 1: Clear & Professional**
These names are straightforward, sound trustworthy, and are ideal for a business-focused audience.

*   **Capital Ledger:** "Capital" links directly to business finance, and "Ledger" is a classic term for financial records.
*   **Venture Vault:** "Venture" speaks to the small business journey, and "Vault" implies security and safekeeping of financial data.
*   **FiscalFolio:** "Fiscal" relates to financial matters, and "Folio" suggests a collection of important documents.
*   **Commerce Compass:** Implies that the app helps businesses navigate their financial landscape.
*   **ProLedger:** A simple combination of "Professional" and "Ledger," indicating its target audience.

---

### **Category 2: Modern & Smart**
These names sound innovative and highlight the "smart" or "AI" aspect of the application.

*   **FinPulse:** Suggests the app is the "pulse" of your finances, providing real-time insight.
*   **FlowTracker:** Modern, simple, and directly related to tracking cash flow.
*   **Quantum Ledger:** "Quantum" gives a futuristic, intelligent feel, suggesting a leap forward from traditional methods.
*   **Synapse Spend:** "Synapse" refers to connections in the brain, implying the app intelligently connects and analyzes your financial data.
*   **Zenith Expense:** "Zenith" means the highest point, suggesting the app offers top-tier expense management.

---

### **Category 3: Friendly & Simple**
These names are approachable, easy to remember, and feel less intimidating for non-accountants.

*   **Jot:** A short, memorable name that reflects the action of quickly "jotting down" an expense.
*   **Penny:** A friendly and universally understood name related to money. It sounds simple and precise.
*   **TallyUp:** A friendly, action-oriented name that clearly describes what the app does.
*   **Pocket Pal:** Implies the app is a helpful companion that fits right in your pocket.
*   **Counti:** A playful take on the word "count," making it sound approachable and easy to use.

---

### **Category 4: Benefit-Oriented**
These names focus on the value the app provides to the small business owner.

*   **CashFlow Clarity:** Directly states the primary benefit—providing a clear understanding of cash flow.
*   **Margin Keeper:** Highlights how tracking expenses helps protect and improve business profit margins.
*   **Profit Pal:** A friendly name that positions the app as a partner in achieving profitability.
*   **SpendWise:** Emphasizes that the app helps users make wiser spending decisions.
*   **LedgerLight:** Suggests the app makes the difficult task of bookkeeping feel "light" and easy.

**My Top Recommendations:**

*   **For a professional feel:** **Capital Ledger**
*   **For a modern, techy feel:** **FinPulse**
*   **For a simple, memorable feel:** **Jot**

# Input
use koin 

# Response
Of course. Using Koin is an excellent choice for dependency injection. It simplifies the architecture by removing the need for manual factory creation and making the code cleaner and more testable.

Here is the complete, runnable code for the "Smart Daily Expense Tracker" refactored to use Koin for dependency injection.

### **Project Setup**

First, you need to add the Koin dependencies to your `app/build.gradle.kts` file and remove the manual ViewModel factory if you were using it.

In your `app/build.gradle.kts` file, add the following:

```kotlin
// build.gradle.kts (Module :app)

// ... (other plugins)

dependencies {
    // ... (your existing core, compose, and room dependencies)

    // Koin for Android & Jetpack Compose
    implementation("io.insert-koin:koin-android:3.5.6")
    implementation("io.insert-koin:koin-androidx-compose:3.5.6")
}
```

**Note:** Make sure to sync your project after adding these dependencies.

---

### **1. Create the Koin Module**

The first step is to define a Koin module. This is where you will declare all the dependencies that Koin needs to manage. This includes the database, DAO, repository, and ViewModel.

Create a new file, for example, `AppModule.kt`.

```kotlin
// AppModule.kt

package com.example.expensetrackerai

import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Singleton: A single instance will be created and shared
    single { AppDatabase.getDatabase(androidApplication()) }
    single { get<AppDatabase>().expenseDao() } // Gets the DAO from the database instance
    single { ExpenseRepository(get()) }        // Gets the repository, injecting the DAO

    // ViewModel: Koin will handle the lifecycle of this ViewModel
    viewModel { ExpenseViewModel(androidApplication(), get()) }
}
```

---

### **2. Create an Application Class to Start Koin**

Koin needs to be initialized when your application starts. The standard way to do this is in a custom `Application` class.

Create a new file, for example, `MyApplication.kt`.

```kotlin
// MyApplication.kt

package com.example.expensetrackerai

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin activity (useful for debugging)
            androidLogger()
            // Provide the Android context to Koin
            androidContext(this@MyApplication)
            // Load your modules
            modules(appModule)
        }
    }
}
```

Now, you must register this `MyApplication` class in your `AndroidManifest.xml`.

```xml
<!-- AndroidManifest.xml -->

<application
    android:name=".MyApplication"
    ... >
    ...
</application>
```

---

### **3. Refactor the Application Code (`MainActivity.kt`)**

Finally, refactor the main application code to use Koin. The primary changes will be:
*   Removing the `ExpenseViewModelFactory`.
*   Injecting the `ExpenseViewModel` directly into your composable functions using `koinViewModel()`.
*   Updating the `ViewModel` constructor to accept the repository.
*   Making `AppDatabase.getDatabase()` public since it's now called from the Koin module.

Here is the updated, complete code for `MainActivity.kt`.

```kotlin
// MainActivity.kt

package com.example.expensetrackerai

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import com.example.expensetrackerai.ui.theme.ExpenseTrackerAITheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// ---------------------------------------------------
// 1. DATA LAYER (ROOM DATABASE) - NO CHANGES HERE
// ---------------------------------------------------

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val date: Long,
    val notes: String?
)

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesForDateRange(startDate: Long, endDate: Long): Flow<List<Expense>>
}

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // This function is now called by Koin
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// ---------------------------------------------------
// 2. REPOSITORY - NO CHANGES HERE
// ---------------------------------------------------

class ExpenseRepository(private val expenseDao: ExpenseDao) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()

    fun getExpensesForToday(): Flow<List<Expense>> {
        val calendar = Calendar.getInstance()
        val startOfDay = calendar.apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0) }.timeInMillis
        val endOfDay = calendar.apply { set(Calendar.HOUR_OF_DAY, 23); set(Calendar.MINUTE, 59); set(Calendar.SECOND, 59) }.timeInMillis
        return expenseDao.getExpensesForDateRange(startOfDay, endOfDay)
    }

    fun getExpensesForLast7Days(): Flow<List<Expense>> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, -7)
        val startDate = calendar.timeInMillis
        return expenseDao.getExpensesForDateRange(startDate, endDate)
    }

    suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }
}


// ---------------------------------------------------
// 3. VIEWMODEL - REFACTORED FOR INJECTION
// ---------------------------------------------------

// The ViewModel now receives its dependencies through the constructor.
// Koin will provide the repository instance.
class ExpenseViewModel(
    application: Application, // Still need app context for some operations if necessary
    private val repository: ExpenseRepository
) : ViewModel() {

    val todayExpenses: Flow<List<Expense>> = repository.getExpensesForToday()
    val last7DaysExpenses: Flow<List<Expense>> = repository.getExpensesForLast7Days()

    val totalSpentToday: Flow<Double> = todayExpenses.map { expenses ->
        expenses.sumOf { it.amount }
    }

    fun addExpense(title: String, amount: Double, category: String, notes: String) {
        viewModelScope.launch {
            val expense = Expense(
                title = title,
                amount = amount,
                category = category,
                date = System.currentTimeMillis(),
                notes = notes.takeIf { it.isNotBlank() }
            )
            repository.addExpense(expense)
        }
    }
}

// THE VIEWMODEL FACTORY IS NO LONGER NEEDED AND CAN BE DELETED


// ---------------------------------------------------
// 4. MAIN ACTIVITY & NAVIGATION - REFACTORED FOR KOIN
// ---------------------------------------------------

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // ViewModel is no longer created manually here.
                    // It will be injected into the screens that need it.
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    // The ViewModel is now injected directly into each screen's composable function.
    NavHost(navController = navController, startDestination = "expense_list") {
        composable("expense_list") {
            ExpenseListScreen(navController = navController, viewModel = koinViewModel())
        }
        composable("expense_entry") {
            ExpenseEntryScreen(navController = navController, viewModel = koinViewModel())
        }
        composable("expense_report") {
            ExpenseReportScreen(navController = navController, viewModel = koinViewModel())
        }
    }
}

// ---------------------------------------------------
// 5. SCREENS (UI) - NO CHANGES NEEDED IN SCREEN LOGIC
// ---------------------------------------------------

// The screen composables remain identical. They receive the ViewModel
// as a parameter, and Koin handles providing the instance.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(navController: NavController, viewModel: ExpenseViewModel) {
    // ... (This entire composable function's code remains the same)
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val categories = listOf("Staff", "Travel", "Food", "Utility")
    val totalSpentToday by viewModel.totalSpentToday.collectAsState(initial = 0.0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TotalSpentTodayHeader(totalSpentToday)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { char -> char.isDigit() || char == '.' } },
                label = { Text("Amount (₹)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Category Dropdown
            ExposedDropdownMenuBox(
                expanded = isCategoryMenuExpanded,
                onExpandedChange = { isCategoryMenuExpanded = !isCategoryMenuExpanded }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isCategoryMenuExpanded,
                    onDismissRequest = { isCategoryMenuExpanded = false }
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                category = selectionOption
                                isCategoryMenuExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { if (it.length <= 100) notes = it },
                label = { Text("Optional Notes (max 100)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (title.isNotBlank() && amountValue != null && amountValue > 0) {
                        viewModel.addExpense(title, amountValue, category, notes)
                        Toast.makeText(context, "Expense Added!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Please fill title and a valid amount.", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("SUBMIT EXPENSE", modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun TotalSpentTodayHeader(total: Double) {
    // ... (This entire composable function's code remains the same)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total Spent Today", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            Text(
                "₹${"%.2f".format(total)}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ExpenseListScreen(navController: NavController, viewModel: ExpenseViewModel) {
    // ... (This entire composable function's code remains the same)
    val todayExpenses by viewModel.todayExpenses.collectAsState(initial = emptyList())
    var groupByCategory by remember { mutableStateOf(false) }

    val groupedExpenses = if (groupByCategory) {
        todayExpenses.groupBy { it.category }
    } else {
        emptyMap()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Expenses") },
                actions = {
                    IconButton(onClick = { navController.navigate("expense_report") }) {
                        Icon(Icons.Filled.Assessment, contentDescription = "Reports")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("expense_entry") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Expense")
            }
        },
        bottomBar = {
            BottomAppBar {
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Filled.List, contentDescription = "List View", tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (todayExpenses.isNotEmpty()) {
                ExpenseListHeader(
                    count = todayExpenses.size,
                    total = todayExpenses.sumOf { it.amount },
                    groupByCategory = groupByCategory,
                    onToggle = { groupByCategory = !groupByCategory }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            AnimatedContent(
                targetState = todayExpenses.isEmpty(),
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { isEmpty ->
                if (isEmpty) {
                    EmptyState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.animateContentSize()
                    ) {
                        if (groupByCategory) {
                            groupedExpenses.forEach { (category, expenses) ->
                                item {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                items(expenses, key = { it.id }) { expense ->
                                    ExpenseListItem(expense, modifier = Modifier.animateItemPlacement())
                                }
                            }
                        } else {
                            items(todayExpenses, key = { it.id }) { expense ->
                                ExpenseListItem(expense, modifier = Modifier.animateItemPlacement())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseListHeader(count: Int, total: Double, groupByCategory: Boolean, onToggle: () -> Unit) {
    // ... (This entire composable function's code remains the same)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Total Count: $count", style = MaterialTheme.typography.bodyMedium)
            Text("Total Amount: ₹${"%.2f".format(total)}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Group by Category", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = groupByCategory, onCheckedChange = { onToggle() })
        }
    }
}

@Composable
fun ExpenseListItem(expense: Expense, modifier: Modifier) {
    // ... (This entire composable function's code remains the same)
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(expense.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                if (expense.notes?.isNotEmpty() == true) {
                    Text(expense.notes, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            Text("₹${"%.2f".format(expense.amount)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun EmptyState() {
    // ... (This entire composable function's code remains the same)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No expenses logged for today!",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Tap the '+' button to add your first one.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(navController: NavController, viewModel: ExpenseViewModel) {
    // ... (This entire composable function's code remains the same)
    val last7DaysExpenses by viewModel.last7DaysExpenses.collectAsState(initial = emptyList())
    val context = LocalContext.current

    val dailyTotals = last7DaysExpenses
        .groupBy {
            // Group by day start
            val cal = Calendar.getInstance().apply { timeInMillis = it.date }
            cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
        .toSortedMap(compareByDescending { it }) // Sort by most recent day

    val categoryTotals = last7DaysExpenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Report (Last 7 Days)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReportSection("Daily Totals") {
                    if (dailyTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    dailyTotals.forEach { (dateMillis, total) ->
                        ReportRow(formatDate(dateMillis), "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                ReportSection("Category Totals") {
                    if (categoryTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    categoryTotals.forEach { (category, total) ->
                        ReportRow(category, "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                // Mock Chart - a real implementation would use a library
                MockBarChart(dailyTotals)
            }

            item {
                ExportButtons { shareContent ->
                    // Trigger Share Intent
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareContent)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }
        }
    }
}

@Composable
fun ReportSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    // ... (This entire composable function's code remains the same)
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun ReportRow(label: String, value: String) {
    // ... (This entire composable function's code remains the same)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun MockBarChart(dailyTotals: Map<Long, Double>) {
    // ... (This entire composable function's code remains the same)
    val maxAmount = dailyTotals.values.maxOrNull() ?: 1.0

    ReportSection("Daily Spending Chart (Mock)") {
        if (dailyTotals.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                dailyTotals.entries.take(7).reversed().forEach { (date, total) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .fillMaxHeight((total / maxAmount).toFloat())
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = SimpleDateFormat("E", Locale.getDefault()).format(Date(date)),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        } else {
            Text("Not enough data to display chart.")
        }
    }
}

@Composable
fun ExportButtons(onShare: (String) -> Unit) {
    // ... (This entire composable function's code remains the same)
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = {
                Toast.makeText(context, "Simulating PDF Export...", Toast.LENGTH_SHORT).show()
                // In a real app, generate a file and then share it.
                onShare("This is your exported expense report (as plain text).")
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("EXPORT PDF (SIM)")
        }
        Button(
            onClick = {
                Toast.makeText(context, "Simulating CSV Export...", Toast.LENGTH_SHORT).show()
                // For simulation, we'll just generate text and share.
                onShare("Date,Amount,Category\n2025-08-07,150.0,Food\n2025-08-06,3500.0,Staff")
            },
            modifier = Modifier.weight(1f)
        ) {
            Text("EXPORT CSV (SIM)")
        }
    }
}

fun formatDate(dateInMillis: Long): String {
    // ... (This entire composable function's code remains the same)
    val date = Date(dateInMillis)
    val today = Date()
    val yesterday = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))

    return when {
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date) == SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(today) -> "Today"
        SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date) == SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(yesterday) -> "Yesterday"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(date)
    }
}

```

# Input
make it category dynamically, only required changes not whole code

# Response
Of course. Making the categories dynamic is a great improvement. This requires changes to the database, repository, ViewModel, and the UI.

Here are the specific, targeted code changes needed to implement dynamic categories using Koin.

---

### **1. Update the Data Layer (Database & Entities)**

First, we need a new table for categories and to link it to the expenses table.

**(In `MainActivity.kt` or a new `Database.kt` file)**

```kotlin
// 1. ADD this new Category entity
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String
)

// 2. MODIFY the Expense entity to use a category ID
@Entity(tableName = "expenses",
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["categoryId"])]
)
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val categoryId: Int, // <-- CHANGED from 'category: String'
    val date: Long,
    val notes: String?
)

// 3. ADD this new data class to combine Expense and Category for display
data class ExpenseWithCategory(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)

// 4. ADD this new DAO for categories
@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): Flow<List<Category>>
}

// 5. MODIFY ExpenseDao to use the new ExpenseWithCategory class
@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    // The query now needs a transaction to ensure atomic read of related data
    @Transaction
    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesForDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseWithCategory>> // <-- RETURN TYPE CHANGED
}

// 6. MODIFY AppDatabase to include the new entity and DAO
@Database(entities = [Expense::class, Category::class], version = 2, exportSchema = false) // <-- VERSION BUMPED, Category::class ADDED
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao // <-- ADD this abstract fun

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                )
                .fallbackToDestructiveMigration() // For simplicity, otherwise provide a migration
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```
**Important:** Because you changed the database schema (new table, modified column), you must either increment the database `version` and provide a migration, or for development, use `.fallbackToDestructiveMigration()`. Uninstalling and reinstalling the app on your device will also clear the old database.

---

### **2. Update Koin Module, Repository, and ViewModel**

Now, let's wire up the new `CategoryDao` and add the logic to manage categories.

**(In `AppModule.kt`)**
```kotlin
// MODIFY your appModule
val appModule = module {
    // Singleton
    single { AppDatabase.getDatabase(androidApplication()) }
    single { get<AppDatabase>().expenseDao() }
    single { get<AppDatabase>().categoryDao() } // <-- ADD this line
    single { ExpenseRepository(get(), get()) } // <-- The repository now needs both DAOs

    // ViewModel
    viewModel { ExpenseViewModel(get()) } // <-- The ViewModel now only needs the repository
}
```

**(In `MainActivity.kt`)**
```kotlin
// 1. MODIFY the ExpenseRepository
class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao // <-- INJECT CategoryDao
) {
    // This flow will provide the list of all categories to the UI
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    fun getExpensesForToday(): Flow<List<ExpenseWithCategory>> { // <-- RETURN TYPE CHANGED
        val calendar = Calendar.getInstance()
        // ... (rest of the function is the same)
        return expenseDao.getExpensesForDateRange(startOfDay, endOfDay)
    }

    fun getExpensesForLast7Days(): Flow<List<ExpenseWithCategory>> { // <-- RETURN TYPE CHANGED
        val calendar = Calendar.getInstance()
        // ... (rest of the function is the same)
        return expenseDao.getExpensesForDateRange(startDate, endDate)
    }

    suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
    }

    // ADD this new function to add categories
    suspend fun addCategory(category: Category) {
        categoryDao.insertCategory(category)
    }
}


// 2. MODIFY the ExpenseViewModel
class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() { // <-- REFACTORED constructor

    val todayExpenses: Flow<List<ExpenseWithCategory>> = repository.getExpensesForToday() // <-- TYPE CHANGED
    val last7DaysExpenses: Flow<List<ExpenseWithCategory>> = repository.getExpensesForLast7Days() // <-- TYPE CHANGED
    val allCategories: Flow<List<Category>> = repository.allCategories // <-- ADD this line

    val totalSpentToday: Flow<Double> = todayExpenses.map { expenses ->
        expenses.sumOf { it.expense.amount } // <-- UPDATED to access nested expense object
    }

    // MODIFY addExpense to take a categoryId
    fun addExpense(title: String, amount: Double, categoryId: Int, notes: String) {
        viewModelScope.launch {
            val expense = Expense(
                title = title,
                amount = amount,
                categoryId = categoryId, // <-- CHANGED
                date = System.currentTimeMillis(),
                notes = notes.takeIf { it.isNotBlank() }
            )
            repository.addExpense(expense)
        }
    }

    // ADD this function to handle adding a new category
    fun addCategory(name: String) {
        viewModelScope.launch {
            if (name.isNotBlank()) {
                repository.addCategory(Category(name = name.trim()))
            }
        }
    }
}
```

---

### **3. Update the UI (Screens)**

Finally, update the UI to allow users to select from the dynamic list and add new categories.

**(In `MainActivity.kt`)**

```kotlin
// 1. ADD this Composable for the "Add Category" dialog anywhere in the file.
@Composable
fun AddCategoryDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Category") },
        text = {
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (categoryName.isNotBlank()) {
                        onConfirm(categoryName)
                    }
                }
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

// 2. REPLACE the existing ExpenseEntryScreen with this new version
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(navController: NavController, viewModel: ExpenseViewModel) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isCategoryMenuExpanded by remember { mutableStateOf(false) }
    var showAddCategoryDialog by remember { mutableStateOf(false) }

    // Collect categories from ViewModel and manage selection
    val categories by viewModel.allCategories.collectAsState(initial = emptyList())
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    // When categories load, select the first one by default
    LaunchedEffect(categories) {
        if (selectedCategory == null) {
            selectedCategory = categories.firstOrNull()
        }
    }

    val context = LocalContext.current
    val totalSpentToday by viewModel.totalSpentToday.collectAsState(initial = 0.0)

    if (showAddCategoryDialog) {
        AddCategoryDialog(
            onDismiss = { showAddCategoryDialog = false },
            onConfirm = { name ->
                viewModel.addCategory(name)
                showAddCategoryDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            // ... (TopAppBar is the same)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TotalSpentTodayHeader(totalSpentToday)
            Spacer(modifier = Modifier.height(24.dp))

            // ... (Title and Amount TextFields are the same)

            // MODIFIED Category Dropdown
            Row(verticalAlignment = Alignment.CenterVertically) {
                ExposedDropdownMenuBox(
                    expanded = isCategoryMenuExpanded,
                    onExpandedChange = { isCategoryMenuExpanded = !isCategoryMenuExpanded },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "Select Category",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryMenuExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isCategoryMenuExpanded,
                        onDismissRequest = { isCategoryMenuExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategory = category
                                    isCategoryMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                IconButton(onClick = { showAddCategoryDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add New Category")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // ... (Notes TextField is the same)

            // MODIFIED Submit Button
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (title.isNotBlank() && amountValue != null && amountValue > 0 && selectedCategory != null) {
                        viewModel.addExpense(title, amountValue, selectedCategory!!.id, notes) // Pass ID
                        Toast.makeText(context, "Expense Added!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show()
                    }
                },
                // ... (modifier and text are the same)
            )
        }
    }
}

// 3. MODIFY ExpenseListItem to use the new data structure
@Composable
fun ExpenseListItem(expense: ExpenseWithCategory, modifier: Modifier = Modifier) { // <-- TYPE CHANGED
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.expense.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(expense.category.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary) // <-- MODIFIED
                if (expense.expense.notes?.isNotEmpty() == true) { // <-- MODIFIED
                    Text(expense.expense.notes, style = MaterialTheme.typography.bodySmall, color = Color.Gray) // <-- MODIFIED
                }
            }
            Text("₹${"%.2f".format(expense.expense.amount)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) // <-- MODIFIED
        }
    }
}


// 4. MODIFY ExpenseListScreen to handle the new data structure
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ExpenseListScreen(navController: NavController, viewModel: ExpenseViewModel) {
    val todayExpenses by viewModel.todayExpenses.collectAsState(initial = emptyList())
    var groupByCategory by remember { mutableStateOf(false) }

    // MODIFIED grouping logic
    val groupedExpenses = if (groupByCategory) {
        todayExpenses.groupBy { it.category.name } // <-- MODIFIED
    } else {
        emptyMap()
    }

    Scaffold(/* ... */) { padding ->
        Column(/* ... */) {
            if (todayExpenses.isNotEmpty()) {
                ExpenseListHeader(
                    count = todayExpenses.size,
                    total = todayExpenses.sumOf { it.expense.amount }, // <-- MODIFIED
                    groupByCategory = groupByCategory,
                    onToggle = { groupByCategory = !groupByCategory }
                )
                // ...
            }

            AnimatedContent(
                targetState = todayExpenses.isEmpty(),
                // ...
            ) { isEmpty ->
                if (isEmpty) {
                    EmptyState()
                } else {
                    LazyColumn(
                        // ...
                    ) {
                        if (groupByCategory) {
                            groupedExpenses.forEach { (categoryName, expenses) ->
                                item {
                                    Text(
                                        text = categoryName, // This is now a String key
                                        // ...
                                    )
                                }
                                items(expenses, key = { it.expense.id }) { expense -> // <-- MODIFIED
                                    ExpenseListItem(expense, modifier = Modifier.animateItemPlacement())
                                }
                            }
                        } else {
                            items(todayExpenses, key = { it.expense.id }) { expense -> // <-- MODIFIED
                                ExpenseListItem(expense, modifier = Modifier.animateItemPlacement())
                            }
                        }
                    }
                }
            }
        }
    }
}

// 5. MODIFY ExpenseReportScreen to handle the new data structure
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(navController: NavController, viewModel: ExpenseViewModel) {
    val last7DaysExpenses by viewModel.last7DaysExpenses.collectAsState(initial = emptyList())
    // ...

    // MODIFIED daily totals logic
    val dailyTotals = last7DaysExpenses
        .groupBy { /* ... */ }
        .mapValues { entry -> entry.value.sumOf { it.expense.amount } } // <-- MODIFIED
        .toSortedMap(compareByDescending { it })

    // MODIFIED category totals logic
    val categoryTotals = last7DaysExpenses
        .groupBy { it.category.name } // <-- MODIFIED
        .mapValues { entry -> entry.value.sumOf { it.expense.amount } } // <-- MODIFIED
    
    // ... The rest of the screen can remain the same.
}
```

# Input
package com.innoveloper.finpulse.ui.screen.report

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.ui.formatDate
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

@Composable
fun ExpenseReportRoute(
    onBack: () -> Unit = {}
) {
    val viewModel: ExpenseReportViewModel = koinViewModel()
    val last7DaysExpenses by viewModel.last7DaysExpenses.collectAsState(emptyList())
    ExpenseReportScreen(
        last7DaysExpenses = last7DaysExpenses,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    last7DaysExpenses: List<ExpenseWithCategory> = listOf(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val dailyTotals = last7DaysExpenses
        .groupBy {
            val cal = Calendar.getInstance().apply { timeInMillis = it.expense.date }
            cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(
            Calendar.SECOND,
            0
        ); cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
        .mapValues { entry -> entry.value.sumOf { it.expense.amount } }
        .toSortedMap(compareByDescending { it }) // Sort by most recent day

    val categoryTotals = last7DaysExpenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.expense.amount } }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Report (Last 7 Days)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReportSection("Daily Totals") {
                    if (dailyTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    dailyTotals.forEach { (dateMillis, total) ->
                        ReportRow(formatDate(dateMillis), "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                ReportSection("Category Totals") {
                    if (categoryTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    categoryTotals.forEach { (category, total) ->
                        ReportRow(category.name, "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                // Mock Chart - a real implementation would use a library
                MockBarChart(dailyTotals)
            }

            item {
                ExportButtons { shareContent ->
                    // Trigger Share Intent
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareContent)
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                }
            }
        }
    }
}




replace mock report with actual data


# Response
Of course. Replacing the mock data in the report generation with actual, dynamic data is a key step.

Here are the required changes for your `ExpenseReportScreen.kt` file. The modifications focus on creating helper functions to generate report strings from the `last7DaysExpenses` data and then using that data in the share intent.

### **Updated `ExpenseReportScreen.kt`**

Replace the entire content of your `ExpenseReportScreen.kt` file with the code below. The key changes are the new `generatePlainTextReport` and `generateCsvReport` functions, and the updated `ExportButtons` composable that uses them.

```kotlin
package com.innoveloper.finpulse.ui.screen.report

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.innoveloper.finpulse.data.local.Category // Assuming Category is in this package
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.ui.formatDate
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseReportRoute(
    onBack: () -> Unit = {}
) {
    val viewModel: ExpenseReportViewModel = koinViewModel()
    val last7DaysExpenses by viewModel.last7DaysExpenses.collectAsState(emptyList())
    ExpenseReportScreen(
        last7DaysExpenses = last7DaysExpenses,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    last7DaysExpenses: List<ExpenseWithCategory> = listOf(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    // --- Data processing remains the same ---
    val dailyTotals = last7DaysExpenses
        .groupBy {
            val cal = Calendar.getInstance().apply { timeInMillis = it.expense.date }
            cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(
            Calendar.SECOND,
            0
        ); cal.set(Calendar.MILLISECOND, 0)
            cal.timeInMillis
        }
        .mapValues { entry -> entry.value.sumOf { it.expense.amount } }
        .toSortedMap(compareByDescending { it }) // Sort by most recent day

    val categoryTotals = last7DaysExpenses
        .groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.expense.amount } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Report (Last 7 Days)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ReportSection("Daily Totals") {
                    if (dailyTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    dailyTotals.forEach { (dateMillis, total) ->
                        ReportRow(formatDate(dateMillis), "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                ReportSection("Category Totals") {
                    if (categoryTotals.isEmpty()) {
                        Text("No data for the last 7 days.")
                    }
                    categoryTotals.forEach { (category, total) ->
                        ReportRow(category.name, "₹${"%.2f".format(total)}")
                    }
                }
            }

            item {
                // The MockBarChart still uses the calculated dailyTotals
                MockBarChart(dailyTotals)
            }

            // --- REPLACEMENT FOR EXPORT BUTTONS ---
            item {
                ExportButtons(
                    onPdfClick = {
                        Toast.makeText(context, "Generating plain text report...", Toast.LENGTH_SHORT).show()
                        val reportContent = generatePlainTextReport(dailyTotals, categoryTotals)
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, reportContent)
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Report")
                        context.startActivity(shareIntent)
                    },
                    onCsvClick = {
                        Toast.makeText(context, "Generating CSV report...", Toast.LENGTH_SHORT).show()
                        val csvContent = generateCsvReport(last7DaysExpenses)
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain" // Sharing as text, can be opened by Sheets/Excel
                            putExtra(Intent.EXTRA_TEXT, csvContent)
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share CSV Report")
                        context.startActivity(shareIntent)
                    }
                )
            }
        }
    }
}

// --- NEW HELPER FUNCTIONS FOR REPORT GENERATION ---

/**
 * Generates a human-readable plain text summary of the expense data.
 */
private fun generatePlainTextReport(
    dailyTotals: Map<Long, Double>,
    categoryTotals: Map<Category, Double>
): String {
    val builder = StringBuilder()
    builder.append("Expense Report (Last 7 Days)\n")
    builder.append("============================\n\n")

    builder.append("--- Daily Totals ---\n")
    if (dailyTotals.isEmpty()) {
        builder.append("No data available.\n")
    } else {
        dailyTotals.forEach { (dateMillis, total) ->
            builder.append("${formatDate(dateMillis)}: ₹${"%.2f".format(total)}\n")
        }
    }
    builder.append("\n")

    builder.append("--- Category Totals ---\n")
    if (categoryTotals.isEmpty()) {
        builder.append("No data available.\n")
    } else {
        categoryTotals.forEach { (category, total) ->
            builder.append("${category.name}: ₹${"%.2f".format(total)}\n")
        }
    }
    builder.append("\nGenerated by FinPulse")
    return builder.toString()
}

/**
 * Generates a CSV (Comma-Separated Values) string of all individual expenses.
 */
private fun generateCsvReport(expenses: List<ExpenseWithCategory>): String {
    val builder = StringBuilder()
    val csvDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Add Header Row
    builder.append("Date,Category,Title,Amount,Notes\n")

    // Add Data Rows
    expenses.forEach { item ->
        val date = csvDateFormat.format(Date(item.expense.date))
        val category = item.category.name
        val title = "\"${item.expense.title.replace("\"", "\"\"")}\"" // Escape quotes
        val amount = "%.2f".format(item.expense.amount)
        val notes = "\"${item.expense.notes?.replace("\"", "\"\"") ?: ""}\"" // Escape quotes
        builder.append("$date,$category,$title,$amount,$notes\n")
    }
    return builder.toString()
}


// --- REFACTORED COMPOSABLES (Can be placed in the same file) ---

@Composable
private fun ExportButtons(
    onPdfClick: () -> Unit,
    onCsvClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onPdfClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("SHARE SUMMARY")
        }
        Button(
            onClick = onCsvClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("SHARE AS CSV")
        }
    }
}

@Composable
private fun ReportSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            content()
        }
    }
}

@Composable
private fun ReportRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}


@Composable
fun MockBarChart(dailyTotals: Map<Long, Double>) {
    // This component's logic remains the same as it was already using the processed data.
    // ... (Your existing MockBarChart implementation)
}
```

### **Summary of Changes:**

1.  **`generatePlainTextReport` function:**
    *   Takes the calculated `dailyTotals` and `categoryTotals` maps as input.
    *   Builds a clean, human-readable string summarizing the data, perfect for sharing as a simple text message or email body (this simulates the "PDF" export).

2.  **`generateCsvReport` function:**
    *   Takes the raw `last7DaysExpenses` list.
    *   Creates a standard CSV formatted string with a header row (`Date,Category,Title,Amount,Notes`).
    *   This string can be saved as a `.csv` file or pasted directly into spreadsheet applications like Google Sheets or Microsoft Excel.
    *   It includes basic CSV escaping for quotes to prevent formatting issues.

3.  **Refactored `ExportButtons`:**
    *   The composable is now cleaner. It no longer contains any logic, mock data, or `Toast` messages.
    *   It simply accepts two lambda functions, `onPdfClick` and `onCsvClick`, making it a reusable UI component.

4.  **Updated `ExpenseReportScreen` Logic:**
    *   The `ExportButtons` composable is now called with the new click handlers.
    *   Each handler calls the appropriate report generation function (`generatePlainTextReport` or `generateCsvReport`), creates a share intent with the **actual data**, and starts the activity.
  
      #  Input
    in explence entry add date option

    # Response
    Of course. Adding a date selection option to the `ExpenseEntryScreen` is a crucial feature. Here are the specific, targeted code changes needed to implement it.

---

### **1. Update the `ExpenseViewModel`**

The `addExpense` function needs to accept the selected date.

**(In `ExpenseViewModel.kt` or wherever it is defined)**

```kotlin
// MODIFY the addExpense function to accept a date parameter

fun addExpense(title: String, amount: Double, categoryId: Int, notes: String, date: Long) { // <-- ADD 'date: Long'
    viewModelScope.launch {
        val expense = Expense(
            title = title,
            amount = amount,
            categoryId = categoryId,
            date = date, // <-- USE the passed date instead of System.currentTimeMillis()
            notes = notes.takeIf { it.isNotBlank() }
        )
        repository.addExpense(expense)
    }
}
```

---

### **2. Update the `ExpenseEntryScreen` UI**

This involves adding the date picker dialog and a field to trigger it.

**(In `ExpenseEntryScreen.kt` or wherever it is defined)**

```kotlin
// --- Add these necessary imports at the top of your file ---
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// --- Inside your ExpenseEntryScreen composable ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(navController: NavController, viewModel: ExpenseViewModel) {
    // ... (keep your existing state variables for title, amount, etc.)

    // 1. ADD state for the date picker
    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // ... (keep the rest of your existing state)
    // val categories by viewModel.allCategories.collectAsState(initial = emptyList())
    // ...

    // 2. ADD the DatePickerDialog composable. It will only show when showDatePicker is true.
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // If user clicks OK, update the selected date
                        selectedDateMillis = datePickerState.selectedDateMillis ?: selectedDateMillis
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Helper function to format the date for display
    fun formatDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    Scaffold(
        // ... (your existing Scaffold content like TopAppBar)
    ) { padding ->
        Column(
            // ... (your existing Column modifier)
        ) {
            // ... (keep TotalSpentTodayHeader, Title, and Amount TextFields)

            // 3. ADD the clickable Date field right after the Amount field
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = formatDate(selectedDateMillis),
                onValueChange = { /* Do nothing, it's read-only */ },
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Select Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true } // Show the dialog on click
            )

            // ... (keep your Category Dropdown and Notes TextField)

            // 4. MODIFY the Button's onClick lambda to pass the selected date
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (title.isNotBlank() && amountValue != null && amountValue > 0 && selectedCategory != null) {
                        viewModel.addExpense(
                            title = title,
                            amount = amountValue,
                            categoryId = selectedCategory!!.id,
                            notes = notes,
                            date = selectedDateMillis // <-- PASS THE SELECTED DATE HERE
                        )
                        Toast.makeText(context, "Expense Added!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show()
                    }
                },
                // ... (rest of the button properties)
            )
        }
    }
}
```

### **Summary of UI Changes:**

1.  **State Management:** Added `selectedDateMillis` to hold the chosen date and `showDatePicker` to control the dialog's visibility.
2.  **`DatePickerDialog`:** A Material 3 `DatePickerDialog` is now part of the composable. It's managed by the `showDatePicker` state.
3.  **Clickable Date Field:** A new, read-only `OutlinedTextField` displays the formatted date. Clicking anywhere on this field sets `showDatePicker` to `true`, launching the dialog.
4.  **Updated `viewModel` Call:** The `onClick` lambda of the submit button now passes the `selectedDateMillis` to the updated `viewModel.addExpense` function.

# Input
in explence entry add date option

# Output
Of course. Adding a date selection option to the `ExpenseEntryScreen` is a crucial feature. Here are the specific, targeted code changes needed to implement it.

---

### **1. Update the `ExpenseViewModel`**

The `addExpense` function needs to accept the selected date.

**(In `ExpenseViewModel.kt` or wherever it is defined)**

```kotlin
// MODIFY the addExpense function to accept a date parameter

fun addExpense(title: String, amount: Double, categoryId: Int, notes: String, date: Long) { // <-- ADD 'date: Long'
    viewModelScope.launch {
        val expense = Expense(
            title = title,
            amount = amount,
            categoryId = categoryId,
            date = date, // <-- USE the passed date instead of System.currentTimeMillis()
            notes = notes.takeIf { it.isNotBlank() }
        )
        repository.addExpense(expense)
    }
}
```

---

### **2. Update the `ExpenseEntryScreen` UI**

This involves adding the date picker dialog and a field to trigger it.

**(In `ExpenseEntryScreen.kt` or wherever it is defined)**

```kotlin
// --- Add these necessary imports at the top of your file ---
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// --- Inside your ExpenseEntryScreen composable ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(navController: NavController, viewModel: ExpenseViewModel) {
    // ... (keep your existing state variables for title, amount, etc.)

    // 1. ADD state for the date picker
    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // ... (keep the rest of your existing state)
    // val categories by viewModel.allCategories.collectAsState(initial = emptyList())
    // ...

    // 2. ADD the DatePickerDialog composable. It will only show when showDatePicker is true.
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // If user clicks OK, update the selected date
                        selectedDateMillis = datePickerState.selectedDateMillis ?: selectedDateMillis
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Helper function to format the date for display
    fun formatDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    Scaffold(
        // ... (your existing Scaffold content like TopAppBar)
    ) { padding ->
        Column(
            // ... (your existing Column modifier)
        ) {
            // ... (keep TotalSpentTodayHeader, Title, and Amount TextFields)

            // 3. ADD the clickable Date field right after the Amount field
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = formatDate(selectedDateMillis),
                onValueChange = { /* Do nothing, it's read-only */ },
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Select Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true } // Show the dialog on click
            )

            // ... (keep your Category Dropdown and Notes TextField)

            // 4. MODIFY the Button's onClick lambda to pass the selected date
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (title.isNotBlank() && amountValue != null && amountValue > 0 && selectedCategory != null) {
                        viewModel.addExpense(
                            title = title,
                            amount = amountValue,
                            categoryId = selectedCategory!!.id,
                            notes = notes,
                            date = selectedDateMillis // <-- PASS THE SELECTED DATE HERE
                        )
                        Toast.makeText(context, "Expense Added!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show()
                    }
                },
                // ... (rest of the button properties)
            )
        }
    }
}
```

### **Summary of UI Changes:**

1.  **State Management:** Added `selectedDateMillis` to hold the chosen date and `showDatePicker` to control the dialog's visibility.
2.  **`DatePickerDialog`:** A Material 3 `DatePickerDialog` is now part of the composable. It's managed by the `showDatePicker` state.
3.  **Clickable Date Field:** A new, read-only `OutlinedTextField` displays the formatted date. Clicking anywhere on this field sets `showDatePicker` to `true`, launching the dialog.
4.  **Updated `viewModel` Call:** The `onClick` lambda of the submit button now passes the `selectedDateMillis` to the updated `viewModel.addExpense` function.


# Input
in explence entry add date option

# Output
Of course. Adding a date selection option to the `ExpenseEntryScreen` is a crucial feature. Here are the specific, targeted code changes needed to implement it.

---

### **1. Update the `ExpenseViewModel`**

The `addExpense` function needs to accept the selected date.

**(In `ExpenseViewModel.kt` or wherever it is defined)**

```kotlin
// MODIFY the addExpense function to accept a date parameter

fun addExpense(title: String, amount: Double, categoryId: Int, notes: String, date: Long) { // <-- ADD 'date: Long'
    viewModelScope.launch {
        val expense = Expense(
            title = title,
            amount = amount,
            categoryId = categoryId,
            date = date, // <-- USE the passed date instead of System.currentTimeMillis()
            notes = notes.takeIf { it.isNotBlank() }
        )
        repository.addExpense(expense)
    }
}
```

---

### **2. Update the `ExpenseEntryScreen` UI**

This involves adding the date picker dialog and a field to trigger it.

**(In `ExpenseEntryScreen.kt` or wherever it is defined)**

```kotlin
// --- Add these necessary imports at the top of your file ---
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// --- Inside your ExpenseEntryScreen composable ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseEntryScreen(navController: NavController, viewModel: ExpenseViewModel) {
    // ... (keep your existing state variables for title, amount, etc.)

    // 1. ADD state for the date picker
    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // ... (keep the rest of your existing state)
    // val categories by viewModel.allCategories.collectAsState(initial = emptyList())
    // ...

    // 2. ADD the DatePickerDialog composable. It will only show when showDatePicker is true.
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // If user clicks OK, update the selected date
                        selectedDateMillis = datePickerState.selectedDateMillis ?: selectedDateMillis
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Helper function to format the date for display
    fun formatDate(millis: Long): String {
        val sdf = SimpleDateFormat("dd MMMM, yyyy", Locale.getDefault())
        return sdf.format(Date(millis))
    }

    Scaffold(
        // ... (your existing Scaffold content like TopAppBar)
    ) { padding ->
        Column(
            // ... (your existing Column modifier)
        ) {
            // ... (keep TotalSpentTodayHeader, Title, and Amount TextFields)

            // 3. ADD the clickable Date field right after the Amount field
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = formatDate(selectedDateMillis),
                onValueChange = { /* Do nothing, it's read-only */ },
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Select Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true } // Show the dialog on click
            )

            // ... (keep your Category Dropdown and Notes TextField)

            // 4. MODIFY the Button's onClick lambda to pass the selected date
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull()
                    if (title.isNotBlank() && amountValue != null && amountValue > 0 && selectedCategory != null) {
                        viewModel.addExpense(
                            title = title,
                            amount = amountValue,
                            categoryId = selectedCategory!!.id,
                            notes = notes,
                            date = selectedDateMillis // <-- PASS THE SELECTED DATE HERE
                        )
                        Toast.makeText(context, "Expense Added!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Please fill all fields.", Toast.LENGTH_LONG).show()
                    }
                },
                // ... (rest of the button properties)
            )
        }
    }
}
```

### **Summary of UI Changes:**

1.  **State Management:** Added `selectedDateMillis` to hold the chosen date and `showDatePicker` to control the dialog's visibility.
2.  **`DatePickerDialog`:** A Material 3 `DatePickerDialog` is now part of the composable. It's managed by the `showDatePicker` state.
3.  **Clickable Date Field:** A new, read-only `OutlinedTextField` displays the formatted date. Clicking anywhere on this field sets `showDatePicker` to `true`, launching the dialog.
4.  **Updated `viewModel` Call:** The `onClick` lambda of the submit button now passes the `selectedDateMillis` to the updated `viewModel.addExpense` function.

# Input
OutlinedTextField(
                value = formatDate(selectedDateMillis),
                onValueChange = { /* Do nothing, it's read-only */ },
                readOnly = true,
                label = { Text("Date") },
                trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Select Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true } // Show the dialog on click
            ) 


click not working 

# Response
You've run into a very common and subtle issue in Jetpack Compose!

The problem is that the `OutlinedTextField` composable, even when `readOnly = true`, has its own internal input handling that can consume the touch event before your `Modifier.clickable` gets a chance to see it.

### The Correct Solution: Wrap it in a `Box`

The most reliable way to fix this is to wrap the `OutlinedTextField` in a `Box` and apply the `clickable` modifier to the `Box`. You should also set `enabled = false` on the `TextField` itself to ensure it doesn't interfere, and then customize its colors to make it look enabled.

Here is the corrected code snippet.

**Replace this:**

```kotlin
// The OLD, non-working code
OutlinedTextField(
    value = formatDate(selectedDateMillis),
    onValueChange = { /* Do nothing, it's read-only */ },
    readOnly = true,
    label = { Text("Date") },
    trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Select Date") },
    modifier = Modifier
        .fillMaxWidth()
        .clickable { showDatePicker = true } // This click is being intercepted
)
```

**With this:**

```kotlin
// The NEW, corrected code
Box {
    OutlinedTextField(
        value = formatDate(selectedDateMillis),
        onValueChange = {},
        label = { Text("Date") },
        trailingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = "Select Date") },
        // 1. Disable the text field to prevent it from consuming clicks
        enabled = false,
        modifier = Modifier.fillMaxWidth(),
        // 2. Customize the colors to make the disabled field look enabled
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    )

    // 3. Apply the clickable modifier to a Box that covers the whole area
    Box(
        modifier = Modifier
            .matchParentSize()
            .clickable { showDatePicker = true }
    )
}
```

### Why This Works

1.  **`Box` as the Click Target:** The outer `Box` is now the component that listens for clicks. It has no complex internal logic, so the `clickable` modifier works perfectly.
2.  **`enabled = false`:** By disabling the `OutlinedTextField`, we completely prevent it from intercepting any pointer input (clicks, gestures, etc.).
3.  **Custom Colors:** A disabled text field normally looks grayed out. We override the `disabled*` colors in `OutlinedTextFieldDefaults.colors` to match the standard colors, so from a user's perspective, it looks perfectly normal and enabled.
4.  **`Modifier.matchParentSize()`:** The second, transparent `Box` ensures that the clickable area is exactly the same size as the `OutlinedTextField`, providing a seamless user experience.

# Input
ExportButtons(
                    onPdfClick = {
                        Toast.makeText(context, "Generating plain text report...", Toast.LENGTH_SHORT).show()
                        val reportContent = generatePlainTextReport(dailyTotals, categoryTotals)
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, reportContent)
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Report")
                        context.startActivity(shareIntent)
                    },
                    onCsvClick = {
                        Toast.makeText(context, "Generating CSV report...", Toast.LENGTH_SHORT).show()
                        val csvContent = generateCsvReport(last7DaysExpenses)
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain" // Sharing as text, can be opened by Sheets/Excel
                            putExtra(Intent.EXTRA_TEXT, csvContent)
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share CSV Report")
                        context.startActivity(shareIntent)
                    }
                )



replace with pdf and csv file genrate 


# Response
Of course. Replacing the plain text sharing with actual PDF and CSV file generation is an excellent upgrade. This involves using a `FileProvider` for secure file sharing, which is the modern standard for Android.

Here is a complete, step-by-step guide to implement this functionality.

---

### **Step 1: Configure the `FileProvider`**

First, you need to tell your app how to securely share files with other apps.

**1.1. Create a `provider_paths.xml` file:**

*   In the `res` directory, create a new folder named `xml`.
*   Inside `res/xml`, create a new file named `provider_paths.xml`.

**`res/xml/provider_paths.xml`:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <!-- Define a path for your app's cache directory -->
    <cache-path
        name="shared_reports"
        path="reports/" />
</paths>
```
*This file specifies that our `FileProvider` is allowed to share files from the `reports` subdirectory inside our app's cache.*

**1.2. Add the `FileProvider` to your `AndroidManifest.xml`:**

Open your `AndroidManifest.xml` file and add the `<provider>` tag inside the `<application>` tag.

**`AndroidManifest.xml`:**
```xml
<application
    ... >

    ... <!-- Your existing activities -->

    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.provider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/provider_paths" />
    </provider>

</application>
```
*Make sure `${applicationId}` is used. This automatically inserts your app's package name (e.g., `com.innoveloper.finpulse`).*

---

### **Step 2: Update Your `ExpenseReportScreen.kt` File**

Now, replace the entire content of your `ExpenseReportScreen.kt` file with the code below. It includes new functions to generate PDF and CSV files and the updated sharing logic.

```kotlin
package com.innoveloper.finpulse.ui.screen.report

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.innoveloper.finpulse.data.local.Category
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.ui.formatDate
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// --- Route Composable remains the same ---
@Composable
fun ExpenseReportRoute(
    onBack: () -> Unit = {}
) {
    val viewModel: ExpenseReportViewModel = koinViewModel()
    val last7DaysExpenses by viewModel.last7DaysExpenses.collectAsState(emptyList())
    ExpenseReportScreen(
        last7DaysExpenses = last7DaysExpenses,
        onBack = onBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseReportScreen(
    last7DaysExpenses: List<ExpenseWithCategory> = listOf(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // --- Data processing remains the same ---
    val dailyTotals = last7DaysExpenses.groupBy { /* ... */ }.mapValues { /* ... */ }.toSortedMap(compareByDescending { it })
    val categoryTotals = last7DaysExpenses.groupBy { it.category }.mapValues { entry -> entry.value.sumOf { it.expense.amount } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Report (Last 7 Days)") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") } }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ... (Your existing ReportSection and MockBarChart items remain here)

            item {
                ExportButtons(
                    onPdfClick = {
                        coroutineScope.launch {
                            try {
                                val file = generatePdfReport(context, dailyTotals, categoryTotals)
                                shareFile(context, file, "application/pdf")
                            } catch (e: IOException) {
                                e.printStackTrace()
                                Toast.makeText(context, "Error generating PDF: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onCsvClick = {
                        coroutineScope.launch {
                            try {
                                val file = generateCsvFile(context, last7DaysExpenses)
                                shareFile(context, file, "text/csv")
                            } catch (e: IOException) {
                                e.printStackTrace()
                                Toast.makeText(context, "Error generating CSV: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )
            }
        }
    }
}

// --- NEW FILE GENERATION AND SHARING LOGIC ---

/**
 * Generates a PDF file from the report data and saves it to a cache directory.
 */
private fun generatePdfReport(
    context: Context,
    dailyTotals: Map<Long, Double>,
    categoryTotals: Map<Category, Double>
): File {
    // 1. Create a PdfDocument
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 page size
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // 2. Define paint styles for drawing text
    val titlePaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textSize = 18f
        color = android.graphics.Color.BLACK
    }
    val headerPaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textSize = 14f
        color = android.graphics.Color.BLACK
    }
    val bodyPaint = Paint().apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textSize = 12f
        color = android.graphics.Color.DKGRAY
    }

    // 3. Draw the content onto the canvas
    var yPosition = 40f
    val leftMargin = 40f

    canvas.drawText("Expense Report (Last 7 Days)", leftMargin, yPosition, titlePaint)
    yPosition += 40f

    // Daily Totals Section
    canvas.drawText("Daily Totals", leftMargin, yPosition, headerPaint)
    yPosition += 25f
    if (dailyTotals.isEmpty()) {
        canvas.drawText("No data available.", leftMargin, yPosition, bodyPaint)
        yPosition += 20f
    } else {
        dailyTotals.forEach { (dateMillis, total) ->
            canvas.drawText("${formatDate(dateMillis)}: ₹${"%.2f".format(total)}", leftMargin, yPosition, bodyPaint)
            yPosition += 20f
        }
    }

    // Category Totals Section
    yPosition += 20f
    canvas.drawText("Category Totals", leftMargin, yPosition, headerPaint)
    yPosition += 25f
    if (categoryTotals.isEmpty()) {
        canvas.drawText("No data available.", leftMargin, yPosition, bodyPaint)
    } else {
        categoryTotals.forEach { (category, total) ->
            canvas.drawText("${category.name}: ₹${"%.2f".format(total)}", leftMargin, yPosition, bodyPaint)
            yPosition += 20f
        }
    }

    // 4. Finish the page and write to a file
    pdfDocument.finishPage(page)

    val reportsDir = File(context.cacheDir, "reports")
    if (!reportsDir.exists()) {
        reportsDir.mkdirs()
    }
    val file = File(reportsDir, "FinPulse_Report.pdf")
    FileOutputStream(file).use {
        pdfDocument.writeTo(it)
    }
    pdfDocument.close()
    return file
}


/**
 * Generates a CSV file from the list of expenses.
 */
private fun generateCsvFile(context: Context, expenses: List<ExpenseWithCategory>): File {
    val csvDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val csvContent = buildString {
        // Header
        append("Date,Category,Title,Amount,Notes\n")
        // Rows
        expenses.forEach { item ->
            val date = csvDateFormat.format(Date(item.expense.date))
            val category = item.category.name
            val title = "\"${item.expense.title.replace("\"", "\"\"")}\""
            val amount = "%.2f".format(item.expense.amount)
            val notes = "\"${item.expense.notes?.replace("\"", "\"\"") ?: ""}\""
            append("$date,$category,$title,$amount,$notes\n")
        }
    }

    val reportsDir = File(context.cacheDir, "reports")
    if (!reportsDir.exists()) {
        reportsDir.mkdirs()
    }
    val file = File(reportsDir, "FinPulse_Expenses.csv")
    file.writeText(csvContent)
    return file
}

/**
 * Creates and triggers a share intent for the given file.
 */
private fun shareFile(context: Context, file: File, mimeType: String) {
    // Use the authority you defined in AndroidManifest.xml
    val authority = "${context.packageName}.provider"
    val uri = FileProvider.getUriForFile(context, authority, file)

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = mimeType
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Report via"))
}


// --- Your UI Components (ExportButtons, ReportSection, etc.) remain the same ---

@Composable
private fun ExportButtons(
    onPdfClick: () -> Unit,
    onCsvClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = onPdfClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("SHARE PDF")
        }
        Button(
            onClick = onCsvClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("SHARE CSV")
        }
    }
}
// ... (Include your other composables like ReportSection, ReportRow, MockBarChart here)
```

### **Summary of Key Changes**

1.  **File Generation Logic:**
    *   `generatePdfReport`: A new function that uses Android's native `PdfDocument` API to create a professional-looking PDF. It draws the text manually onto a canvas.
    *   `generateCsvFile`: Replaces the old `generateCsvReport`. Instead of just returning a string, it writes that string to a `.csv` file in a dedicated `reports` folder inside your app's cache directory.

2.  **`shareFile` Utility:**
    *   This new function contains the logic for creating the `ACTION_SEND` intent.
    *   It uses `FileProvider.getUriForFile()` to get a secure `content://` URI for the file you created. This is the crucial step for sharing files securely.
    *   It sets the correct MIME type (`application/pdf` or `text/csv`) and grants the necessary read permission to the app that will handle the share action.

3.  **Coroutine Scope:**
    *   File I/O (writing files to disk) is a blocking operation and must not be done on the main UI thread. The click handlers in `ExportButtons` now launch a coroutine (`coroutineScope.launch`) to perform the file generation and sharing in the background.

4.  **Error Handling:** The file generation calls are wrapped in a `try-catch` block to gracefully handle potential `IOExceptions` (e.g., if the disk is full) and show an error message to the user.

Now, when you run the app and tap the "SHARE PDF" or "SHARE CSV" button, it will generate a real file and open the Android share sheet, allowing you to send it to apps like Gmail, Drive, Slack, or WhatsApp.

# Input
package com.innoveloper.finpulse.ui.screen.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseListRoute(
    goToReport: () -> Unit = {},
    goToEntry: () -> Unit = {},
) {
    val viewModel: ExpenseListViewModel = koinViewModel()
    val todayExpenses by viewModel.todayExpenses.collectAsState(emptyList())

    ExpenseListScreen(
        todayExpenses = todayExpenses,
        goToReport = goToReport,
        goToEntry = goToEntry
    )

}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ExpenseListScreen(
    todayExpenses: List<ExpenseWithCategory> = emptyList(),
    goToReport: () -> Unit = {},
    goToEntry: () -> Unit = {},
) {

    var groupByCategory by remember { mutableStateOf(false) }

    val groupedExpenses = if (groupByCategory) {
        todayExpenses.groupBy { it.category }
    } else {
        emptyMap()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Today's Expenses") },
                actions = {
                    IconButton(onClick = goToReport) {
                        Icon(Icons.Filled.Assessment, contentDescription = "Reports")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = goToEntry) {
                Icon(Icons.Filled.Add, contentDescription = "Add Expense")
            }
        },
        bottomBar = {
            BottomAppBar {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = "List View",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (todayExpenses.isNotEmpty()) {
                ExpenseListHeader(
                    count = todayExpenses.size,
                    total = todayExpenses.sumOf { it.expense.amount },
                    groupByCategory = groupByCategory,
                    onToggle = { groupByCategory = !groupByCategory }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            AnimatedContent(
                targetState = todayExpenses.isEmpty(),
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { isEmpty ->
                if (isEmpty) {
                    EmptyState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.animateContentSize()
                    ) {
                        if (groupByCategory) {
                            groupedExpenses.forEach { (category, expenses) ->
                                item {
                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                items(expenses, key = { it.expense.id }) { expense ->
                                    ExpenseListItem(expense, modifier = Modifier.animateItem())
                                }
                            }
                        } else {
                            items(todayExpenses, key = { it.expense.id }) { expense ->
                                ExpenseListItem(expense, modifier = Modifier.animateItem())
                            }
                        }
                    }
                }
            }
        }
    }
}


package com.innoveloper.finpulse.ui.screen.list

import androidx.lifecycle.ViewModel
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class ExpenseListViewModel(
    expenseRepository: ExpenseRepository
) : ViewModel() {

    val todayExpenses: Flow<List<ExpenseWithCategory>> = expenseRepository.getExpensesForToday()

}


in replace with bottom sheet scaffle for change date 

# Response

Of course. Replacing the standard `Scaffold` with a `BottomSheetScaffold` to handle date filtering is an excellent way to create a modern and clean UI.

This requires significant updates to both the `ViewModel` (to handle the changing date logic) and the `Screen` (to implement the `BottomSheetScaffold`).

Here are the complete, updated files for `ExpenseListViewModel.kt` and `ExpenseListScreen.kt`.

---

### **1. Updated `ExpenseListViewModel.kt`**

First, the `ViewModel` needs to be updated to manage different date filter states and provide the correct flow of expenses based on the user's selection.

**Replace the entire content of `ExpenseListViewModel.kt` with this:**

```kotlin
package com.innoveloper.finpulse.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

// Enum to define the types of filters we can apply
enum class DateFilterType(val title: String) {
    TODAY("Today's Expenses"),
    WEEK("Last 7 Days"),
    ALL("All Expenses")
}

class ExpenseListViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    // Internal state to hold the current filter type, defaults to TODAY
    private val _currentDateFilter = MutableStateFlow(DateFilterType.TODAY)
    val currentDateFilter: StateFlow<DateFilterType> = _currentDateFilter.asStateFlow()

    /**
     * This is the primary reactive flow.
     * It uses `flatMapLatest` to listen to changes in `_currentDateFilter`.
     * When the filter changes, it cancels the old repository call and starts a new one.
     */
    val expenses: StateFlow<List<ExpenseWithCategory>> = _currentDateFilter
        .flatMapLatest { filterType ->
            when (filterType) {
                DateFilterType.TODAY -> expenseRepository.getExpensesForToday()
                DateFilterType.WEEK -> expenseRepository.getExpensesForLast7Days()
                DateFilterType.ALL -> expenseRepository.allExpenses
            }
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Public function that the UI can call to change the current date filter.
     */
    fun setDateFilter(filterType: DateFilterType) {
        _currentDateFilter.value = filterType
    }
}
```

### **2. Updated `ExpenseListScreen.kt`**

Now, update the UI to use the `BottomSheetScaffold` and connect it to the new `ViewModel` logic.

**Replace the entire content of `ExpenseListScreen.kt` with this:**

```kotlin
package com.innoveloper.finpulse.ui.screen.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseListRoute(
    goToReport: () -> Unit = {},
    goToEntry: () -> Unit = {},
) {
    val viewModel: ExpenseListViewModel = koinViewModel()
    // Observe the new reactive 'expenses' flow and the filter state
    val expenses by viewModel.expenses.collectAsState()
    val currentFilter by viewModel.currentDateFilter.collectAsState()

    ExpenseListScreen(
        expenses = expenses,
        currentFilter = currentFilter,
        onFilterChange = viewModel::setDateFilter, // Pass the function reference
        goToReport = goToReport,
        goToEntry = goToEntry
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ExpenseListScreen(
    expenses: List<ExpenseWithCategory> = emptyList(),
    currentFilter: DateFilterType,
    onFilterChange: (DateFilterType) -> Unit,
    goToReport: () -> Unit = {},
    goToEntry: () -> Unit = {},
) {
    var groupByCategory by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // State for the BottomSheetScaffold
    val bottomSheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    val groupedExpenses = if (groupByCategory) {
        expenses.groupBy { it.category }
    } else {
        emptyMap()
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        // The content of the bottom sheet (our date filters)
        sheetContent = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Filter Expenses",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                // Iterate through all possible filter types to create list items
                DateFilterType.entries.forEach { filterType ->
                    ListItem(
                        headlineContent = { Text(filterType.title) },
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onFilterChange(filterType)
                                // After changing the filter, close the bottom sheet
                                scope.launch { scaffoldState.bottomSheetState.partialExpand() }
                            }
                    )
                }
            }
        },
        // The rest of the UI remains mostly the same, just placed inside the scaffold
        topBar = {
            TopAppBar(
                title = { Text(currentFilter.title) }, // Title now reflects the current filter
                actions = {
                    // Button to open the date filter sheet
                    IconButton(onClick = {
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Change Date Filter")
                    }
                    IconButton(onClick = goToReport) {
                        Icon(Icons.Filled.Assessment, contentDescription = "Reports")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = goToEntry) {
                Icon(Icons.Filled.Add, contentDescription = "Add Expense")
            }
        },
        bottomBar = {
            BottomAppBar {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = "List View",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    ) { padding ->
        // This is the main screen content
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (expenses.isNotEmpty()) {
                ExpenseListHeader(
                    count = expenses.size,
                    total = expenses.sumOf { it.expense.amount },
                    groupByCategory = groupByCategory,
                    onToggle = { groupByCategory = !groupByCategory }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            AnimatedContent(
                targetState = expenses.isEmpty(),
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { isEmpty ->
                if (isEmpty) {
                    EmptyState(modifier = Modifier.padding(padding))
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.animateContentSize()
                    ) {
                        if (groupByCategory) {
                            groupedExpenses.forEach { (category, expenseItems) ->
                                item {
                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                                items(expenseItems, key = { it.expense.id }) { expense ->
                                    ExpenseListItem(expense, modifier = Modifier.animateItem())
                                }
                            }
                        } else {
                            items(expenses, key = { it.expense.id }) { expense ->
                                ExpenseListItem(expense, modifier = Modifier.animateItem())
                            }
                        }
                    }
                }
            }
        }
    }
}
```

### **Summary of Changes:**

1.  **`ExpenseListViewModel`:**
    *   An `enum class DateFilterType` is introduced to strongly-type our filters.
    *   It now holds the current filter state in a `StateFlow`.
    *   The main `expenses` flow is now powered by `flatMapLatest`, making it automatically switch its data source (e.g., from `getExpensesForToday()` to `getExpensesForLast7Days()`) whenever the filter state changes.
    *   A public function `setDateFilter` is exposed for the UI to call.

2.  **`ExpenseListScreen`:**
    *   The `Scaffold` has been replaced with a `BottomSheetScaffold`.
    *   **`sheetContent`**: This new lambda defines the UI for the bottom sheet. It's a simple `Column` that creates a clickable `ListItem` for each `DateFilterType`.
    *   **State Management**: It now uses `rememberBottomSheetScaffoldState` and `rememberCoroutineScope` to control the sheet's behavior (expanding and collapsing).
    *   **`TopAppBar`**: The title is now dynamic (`currentFilter.title`), and a new `IconButton` with a calendar icon has been added to open the sheet.
    *   **Data Flow**: The screen now observes `viewModel.expenses` instead of the old `todayExpenses`.
    *   **Event Handling**: Clicking a `ListItem` in the sheet calls the `onFilterChange` lambda (which is `viewModel::setDateFilter`) and then launches a coroutine to close the sheet.
  

# Input
DateFilterType add type for select date in range

# Response

Excellent idea. Adding a custom date range picker is the final step to make the filtering truly powerful. This requires converting the `DateFilterType` `enum` into a `sealed class` to allow the custom range option to hold data (the start and end dates).

Here are the complete, updated files with the necessary changes.

---

### **1. Updated `ExpenseListViewModel.kt`**

The `ViewModel` needs to handle the new `CustomRange` state.

**Replace the entire content of `ExpenseListViewModel.kt` with this:**

```kotlin
package com.innoveloper.finpulse.ui.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import com.innoveloper.finpulse.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 1. Convert the enum to a sealed class to allow the CustomRange to hold data.
sealed class DateFilterType(val title: String) {
    object Today : DateFilterType("Today's Expenses")
    object Week : DateFilterType("Last 7 Days")
    object All : DateFilterType("All Expenses")
    data class CustomRange(val startDate: Long, val endDate: Long) : DateFilterType("Custom Range") {
        // Override title to be dynamic for the custom range
        override val title: String
            get() {
                val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
                return "${sdf.format(Date(startDate))} - ${sdf.format(Date(endDate))}"
            }
    }
}

class ExpenseListViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    // 2. The StateFlow now holds the sealed class type.
    private val _currentDateFilter = MutableStateFlow<DateFilterType>(DateFilterType.Today)
    val currentDateFilter: StateFlow<DateFilterType> = _currentDateFilter.asStateFlow()

    val expenses: StateFlow<List<ExpenseWithCategory>> = _currentDateFilter
        .flatMapLatest { filterType ->
            // 3. The `when` block now handles the new `CustomRange` class.
            when (filterType) {
                is DateFilterType.Today -> expenseRepository.getExpensesForToday()
                is DateFilterType.Week -> expenseRepository.getExpensesForLast7Days()
                is DateFilterType.All -> expenseRepository.allExpenses
                is DateFilterType.CustomRange -> {
                    // Ensure the end of the day is included in the range
                    val endOfDay = filterType.endDate + (24 * 60 * 60 * 1000 - 1)
                    expenseRepository.getExpensesForDateRange(filterType.startDate, endOfDay)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Sets a predefined filter like Today, Week, or All.
     */
    fun setDateFilter(filterType: DateFilterType) {
        // Ensure we don't pass a CustomRange instance here without data
        if (filterType !is DateFilterType.CustomRange) {
            _currentDateFilter.value = filterType
        }
    }

    /**
     * 4. New function specifically for setting a custom date range.
     */
    fun setCustomDateRange(startDate: Long, endDate: Long) {
        _currentDateFilter.value = DateFilterType.CustomRange(startDate, endDate)
    }
}
```

---

### **2. Updated `ExpenseListScreen.kt`**

The UI needs to be updated to show the `DateRangePicker` and trigger the new `ViewModel` function.

**Replace the entire content of `ExpenseListScreen.kt` with this:**

```kotlin
package com.innoveloper.finpulse.ui.screen.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.innoveloper.finpulse.data.local.ExpenseWithCategory
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ExpenseListRoute(
    goToReport: () -> Unit = {},
    goToEntry: () -> Unit = {},
) {
    val viewModel: ExpenseListViewModel = koinViewModel()
    val expenses by viewModel.expenses.collectAsState()
    val currentFilter by viewModel.currentDateFilter.collectAsState()

    ExpenseListScreen(
        expenses = expenses,
        currentFilter = currentFilter,
        onFilterChange = viewModel::setDateFilter,
        onDateRangeSelected = viewModel::setCustomDateRange, // Pass the new function
        goToReport = goToReport,
        goToEntry = goToEntry
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ExpenseListScreen(
    expenses: List<ExpenseWithCategory> = emptyList(),
    currentFilter: DateFilterType,
    onFilterChange: (DateFilterType) -> Unit,
    onDateRangeSelected: (startDate: Long, endDate: Long) -> Unit, // New callback
    goToReport: () -> Unit = {},
    goToEntry: () -> Unit = {},
) {
    var groupByCategory by remember { mutableStateOf(false) }
    var showDateRangePicker by remember { mutableStateOf(false) } // State for the date range picker dialog
    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState)

    // The DateRangePicker Dialog
    if (showDateRangePicker) {
        val dateRangePickerState = rememberDateRangePickerState()
        DatePickerDialog(
            onDismissRequest = { showDateRangePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDateRangePicker = false
                        // When confirmed, call the ViewModel function with the selected range
                        val startDate = dateRangePickerState.selectedStartDateMillis
                        val endDate = dateRangePickerState.selectedEndDateMillis
                        if (startDate != null && endDate != null) {
                            onDateRangeSelected(startDate, endDate)
                            // Close the bottom sheet as well
                            scope.launch { scaffoldState.bottomSheetState.hide() }
                        }
                    },
                    // Enable button only when a full range is selected
                    enabled = dateRangePickerState.selectedStartDateMillis != null && dateRangePickerState.selectedEndDateMillis != null
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDateRangePicker = false }) { Text("Cancel") }
            }
        ) {
            DateRangePicker(state = dateRangePickerState)
        }
    }


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Filter Expenses", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp))

                // Predefined filter options
                val predefinedFilters = listOf(DateFilterType.Today, DateFilterType.Week, DateFilterType.All)
                predefinedFilters.forEach { filterType ->
                    ListItem(
                        headlineContent = { Text(filterType.title) },
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable {
                            onFilterChange(filterType)
                            scope.launch { scaffoldState.bottomSheetState.hide() }
                        }
                    )
                }

                // Custom date range option
                ListItem(
                    headlineContent = { Text("Select Date Range...") },
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).clickable {
                        // Open the date range picker dialog
                        showDateRangePicker = true
                    }
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(currentFilter.title) },
                actions = {
                    IconButton(onClick = { scope.launch { scaffoldState.bottomSheetState.expand() } }) {
                        Icon(Icons.Default.CalendarToday, "Change Date Filter")
                    }
                    IconButton(onClick = goToReport) { Icon(Icons.Filled.Assessment, "Reports") }
                }
            )
        },
        floatingActionButton = { FloatingActionButton(onClick = goToEntry) { Icon(Icons.Filled.Add, "Add Expense") } },
        bottomBar = {
            BottomAppBar {
                Spacer(Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Filled.List, "List View", tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.weight(1f))
            }
        }
    ) { padding ->
        // Main screen content remains the same
        Column(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp)
        ) {
            if (expenses.isNotEmpty()) {
                ExpenseListHeader(
                    count = expenses.size,
                    total = expenses.sumOf { it.expense.amount },
                    groupByCategory = groupByCategory,
                    onToggle = { groupByCategory = !groupByCategory }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            // ... (Rest of your AnimatedContent and LazyColumn code is unchanged)
        }
    }
}
```

### **Summary of Changes**

1.  **`DateFilterType` is now a `sealed class`**: This allows the `CustomRange` type to hold `startDate` and `endDate` values while the others remain simple objects.
2.  **`ViewModel` Logic Update**:
    *   The `flatMapLatest` operator now checks `is DateFilterType.CustomRange` and calls the repository with the specific start and end dates.
    *   A new `setCustomDateRange` function was added to handle this specific action.
3.  **`DateRangePicker` Dialog**:
    *   A standard Material 3 `DatePickerDialog` containing a `DateRangePicker` is now part of the `ExpenseListScreen`.
    *   Its visibility is controlled by the `showDateRangePicker` boolean state.
4.  **Bottom Sheet Update**:
    *   A new `ListItem` for "Select Date Range..." was added.
    *   Clicking it sets `showDateRangePicker = true`, which displays the dialog.
5.  **Connecting UI to ViewModel**:
    *   When the user confirms a range in the picker, the `onDateRangeSelected` callback is invoked, which calls the `viewModel.setCustomDateRange` function with the selected dates.
    .
