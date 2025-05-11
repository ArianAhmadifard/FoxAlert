package com.arian.foxalert.presentation.calendar

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arian.foxalert.data.model.CategoryEntity
import com.arian.foxalert.data.model.EventEntity
import com.arian.foxalert.ui.theme.FoxColor40
import com.arian.foxalert.ui.theme.FoxColor80
import com.arian.foxalert.ui.theme.FoxGrey80
import com.arian.foxalert.ui.theme.Pink80
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.DayPosition
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    categories: List<CategoryEntity>,
    onAddEventClick: (event: EventEntity) -> Unit,
) {

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    var showAddEventDialog by remember { mutableStateOf(false) }

    val showToast = remember { mutableStateOf(false) }
    val toastMessage = remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(showToast.value) {
        if (showToast.value) {
            kotlinx.coroutines.delay(2000)
            showToast.value = false
        }
    }

    LaunchedEffect(showToast.value, toastMessage.value) {
        if (showToast.value && toastMessage.value.isNotBlank()) {
            Toast.makeText(context, toastMessage.value, Toast.LENGTH_SHORT).show()
        }
    }



    if (showAddEventDialog && selectedDate != null) {
        AddEventDialog(
            selectedDate = selectedDate!!,
            categories = categories,
            onDismissRequest = { showAddEventDialog = false },
            onEventCreated = { title, description, categoryName ->
                val newEvent = EventEntity(
                    title = title,
                    description = description,
                    date = selectedDate!!,
                    categoryName = categoryName
                )
                onAddEventClick(newEvent)
                toastMessage.value = "Event '${title}' created!"
                showToast.value = true
                showAddEventDialog = false
            }
        )
    }

    Column {

        HorizontalCalendarView(
            selectedDate = selectedDate,
            onDateSelected = { date -> selectedDate = date }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                showAddEventDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .heightIn(min = 54.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = FoxColor80)
        ) {
            Text("New Event", color = Color.White)
        }


    }
}

@Composable
fun HorizontalCalendarView(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit
) {
    val today = remember { LocalDate.now() }
    val startMonth = remember { YearMonth.now().minusMonths(12) }
    val endMonth = remember { YearMonth.of(2040, Month.DECEMBER) }
    val currentMonth = remember { YearMonth.now() }
    val coroutineScope = rememberCoroutineScope()

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstDayOfWeek = DayOfWeek.MONDAY,
        firstVisibleMonth = currentMonth,
    )

    Column {
        Button(
            onClick = {
                coroutineScope.launch {
                    calendarState.scrollToMonth(YearMonth.from(today))
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = FoxColor80)
        ) {
            Text("Today", color = Color.White)
        }

        HorizontalCalendar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(FoxGrey80),
            state = calendarState,
            dayContent = { day ->

                val isSelected = day.date == selectedDate
                val isToday = day.date == today

                val backgroundColor = when {
                    isSelected -> Pink80
                    isToday -> FoxColor80
                    else -> Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            backgroundColor
                        )
                        .clickable {
                            onDateSelected(day.date)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (day.position == DayPosition.MonthDate) {
                            if (isToday || isSelected) {
                                Color.White
                            } else {
                                Pink80
                            }
                        } else {
                            Color.Gray
                        }
                    )
                }
            },

            monthHeader = { month ->
                Text(
                    text = month.yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = FoxColor80,
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventDialog(
    selectedDate: LocalDate,
    categories: List<CategoryEntity>,
    onDismissRequest: () -> Unit,
    onEventCreated: (title: String, description: String, categoryName: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedCategory: CategoryEntity? by remember { mutableStateOf(categories.firstOrNull()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    Log.i("damoon", "AddEventDialog: damoon: $categories ")
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("New Event on ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        errorMessage = null
                        title = it
                    },
                    label = { Text("Title", color = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FoxColor80,
                        unfocusedBorderColor = FoxColor40,
                        errorBorderColor = Color.Red,
                    ),
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)", color = MaterialTheme.colorScheme.onSurface) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedBorderColor = FoxColor80,
                        unfocusedBorderColor = FoxColor40,
                        errorBorderColor = Color.Red,
                    ),
                )
                Spacer(Modifier.height(8.dp))

                // --- Category Selection Spinner (ExposedDropdownMenuBox) ---
                Box(modifier = Modifier.fillMaxWidth()) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            println("DEBUG: ExposedDropdownMenuBox clicked! Toggling expanded from $expanded to ${!expanded}") // <-- Add this line
                            expanded = !expanded
                        }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            readOnly = true,
                            value = selectedCategory?.name
                                ?: "Select Category (Required)",
                            onValueChange = { },
                            label = { Text("Category", color = MaterialTheme.colorScheme.onSurface) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedBorderColor = FoxColor80,
                                unfocusedBorderColor = FoxColor40,
                                errorBorderColor = Color.Red,
                            ),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },

                            ) {
                            // Option for no category
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    contentPadding = PaddingValues(4.dp),
                                    modifier = Modifier,
                                    text = {
                                        Text(
                                            text = category.name,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                        errorMessage = null
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                }
                errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color.Red, // Use theme's error color
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val isTitleValid = title.isNotBlank()
                    val isCategorySelected =
                        selectedCategory != null


                    if (isTitleValid && isCategorySelected) {
                        errorMessage = null
                        onEventCreated(title, description, selectedCategory?.name?:"")
                    } else {
                        errorMessage = when {
                            !isTitleValid && !isCategorySelected -> "Title and Category are required."
                            !isTitleValid -> "Title is required."
                            !isCategorySelected -> "Category is required."
                            else -> null
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = FoxColor80)
            ) {
                Text("Save", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismissRequest,
                colors = ButtonDefaults.buttonColors(containerColor = FoxColor80)
            ) {
                Text("Cancel", color = Color.White)
            }
        }
    )
}


