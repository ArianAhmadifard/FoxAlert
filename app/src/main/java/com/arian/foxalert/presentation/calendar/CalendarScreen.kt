package com.arian.foxalert.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
fun CalendarScreen() {
    Column {
        HorizontalCalendarView()
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {

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
fun HorizontalCalendarView() {
    val today = remember { LocalDate.now() }
    val startMonth = remember { YearMonth.now().minusMonths(12) }
    val endMonth = remember { YearMonth.of(2040, Month.DECEMBER) } // Max = Dec 2040
    val currentMonth = remember { YearMonth.now() }
    val coroutineScope = rememberCoroutineScope()
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }

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
                val isSelected = day.date == selectedDate.value
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
                        .clickable {  selectedDate.value = day.date },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day.date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (day.position == DayPosition.MonthDate) {
                            if (isToday || isSelected){
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


