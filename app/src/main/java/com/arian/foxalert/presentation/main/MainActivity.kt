package com.arian.foxalert.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.arian.foxalert.presentation.NavigationItem
import com.arian.foxalert.presentation.Screen
import com.arian.foxalert.presentation.calendar.CalendarScreen
import com.arian.foxalert.presentation.calendar.CalendarViewModel
import com.arian.foxalert.presentation.category.CategoryScreen
import com.arian.foxalert.presentation.category.CategoryViewModel
import com.arian.foxalert.presentation.events.EventScreen
import com.arian.foxalert.presentation.events.EventViewModel
import com.arian.foxalert.ui.theme.FoxAlertTheme
import com.arian.foxalert.ui.theme.FoxColor80
import com.arian.foxalert.ui.theme.FoxGrey80
import com.arian.foxalert.ui.theme.Pink80
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()
            FoxAlertTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        FoxBottomNavigationBar(navController)
                    }
                ) { innerPadding ->

                    val graph =
                        navController.createGraph(startDestination = Screen.Calendar.route) {
                            composable(route = Screen.Event.route) {
                                val eventViewModel: EventViewModel = hiltViewModel()
                                val allEvents by eventViewModel.allEvent.collectAsState()
                                EventScreen(allEvents, onDeleteClick = {
                                    eventViewModel.deleteEventIfExists(it)
                                })
                            }
                            composable(route = Screen.Calendar.route) {
                                val calendarViewModel: CalendarViewModel = hiltViewModel()
                                val categories by calendarViewModel.categories.collectAsState()
                                Log.i("damoon", "category size: ${categories.size}")
                                CalendarScreen(categories) {
                                    calendarViewModel.insertEvent(it)
                                }
                            }
                            composable(route = Screen.Category.route) {
                                val categoryViewModel: CategoryViewModel = hiltViewModel()
                                val categories by categoryViewModel.categories.collectAsState()
                                CategoryScreen(categories, onDeleteClick = {
                                    categoryViewModel.deleteCategoryIfExists(it)
                                }, onAddCategoryClick = {
                                    categoryViewModel.insertCategoryIfNotExists(it)
                                })

                            }

                        }
                    NavHost(
                        navController = navController,
                        graph = graph,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun FoxBottomNavigationBar(navController: NavHostController) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) FoxGrey80 else Color.White
    val selectedLabelColor = if (isDark) Color.White else Color.Black
    val unselectedLabelColor = if (isDark) Color.LightGray else Color.Gray

    val navigationItems = listOf(
        NavigationItem(
            title = "Event",
            icon = Icons.Default.Notifications,
            route = Screen.Event.route
        ),
        NavigationItem(
            title = "Calendar",
            icon = Icons.Default.DateRange,
            route = Screen.Calendar.route
        ),
        NavigationItem(
            title = "Category",
            icon = Icons.Default.Star,
            route = Screen.Category.route
        ),
    )

    val selectedNavigationIndex = rememberSaveable {
        mutableIntStateOf(1)
    }

    NavigationBar(
        containerColor = backgroundColor
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedNavigationIndex.intValue == index,
                onClick = {
                    selectedNavigationIndex.intValue = index
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title)
                },
                label = {
                    Text(
                        item.title,
                        color = if (index == selectedNavigationIndex.intValue)
                            selectedLabelColor
                        else unselectedLabelColor
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if(isDark) FoxColor80 else Color.Black ,
                    indicatorColor = if (isDark) Pink80 else FoxColor80
                )
            )
        }
    }
}



