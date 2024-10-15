package com.example.kiosk

import EmployeeListScreen
import VisitorPhotoScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kiosk.Screens.CheckIn.CheckInEndScreen
import com.example.kiosk.Screens.CheckIn.VisitorInfoScreen
import com.example.kiosk.Screens.Delivery.DeliveryEndScreen
import com.example.kiosk.Screens.Delivery.ThreeOptionDeliveryScreen
import com.example.kiosk.Screens.HomeScreen
import com.example.kiosk.Screens.ThreeOptionHomeScreen
import com.example.kiosk.ui.theme.KioskTheme
import androidx.navigation.NavType
import androidx.navigation.navArgument


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KioskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Kiosk()
                }
            }
        }
    }
}

@Composable
fun Kiosk() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") {
            HomeScreen {
                navController.navigate("threeOptionHomeScreen")
            }
        }
        composable("threeOptionHomeScreen") {
            ThreeOptionHomeScreen(
                navigationBack = { navController.popBackStack() },
                navigationToThreeOptionDeliveryScreen = { navController.navigate("threeOptionDeliveryScreen")},
                navigationToEmployeeListScreen = { navController.navigate("employeeListScreen")}
            )
        }
        composable("employeeListScreen") {
            EmployeeListScreen(
                navigationBack = { navController.popBackStack() },
                navigationToVisitorInfoScreen = { navController.navigate("visitorInfoScreen")}
            )
        }
        composable("visitorInfoScreen") {
            VisitorInfoScreen(
                navigationBack = { navController.popBackStack() },
                navigationToVisitorPhotoScreen = { navController.navigate("visitorPhotoScreen/{firstName}/{lastName}/{email}/{company}/{phoneNumber}")}
            )
        }
        composable(
            "visitorPhotoScreen/{firstName}/{lastName}/{email}/{company}/{phoneNumber}",
            arguments = listOf(
                navArgument("firstName") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("company") { type = NavType.StringType },
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val firstName = backStackEntry.arguments?.getString("firstName")
            val lastName = backStackEntry.arguments?.getString("lastName")
            val email = backStackEntry.arguments?.getString("email")
            val company = backStackEntry.arguments?.getString("company")
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber")

            // If all the values are non-null, then proceed to show VisitorPhotoScreen
            listOf(firstName, lastName, email, company, phoneNumber).let { params ->
                if (params.all { it != null }) {
                    VisitorPhotoScreen(
                        firstName = firstName!!,
                        lastName = lastName!!,
                        email = email!!,
                        company = company!!,
                        phoneNumber = phoneNumber!!,
                        navigationBack = { navController.popBackStack() },
                        navigationToCheckInEndScreen = { navController.navigate("checkInEndScreen") }
                    )
                } else {
                    // Handle error case if necessary, for example by showing an error message
                    Log.e("NavError", "Some arguments were null: $params")
                }
            }
        }

        composable("checkInEndScreen") {
            CheckInEndScreen(
                navigationToHomeScreen = { navController.navigate("homeScreen")}
            )
        }

        composable("threeOptionDeliveryScreen") {
            ThreeOptionDeliveryScreen(
                navigationBack = { navController.popBackStack() },
                navigationToDeliveryEndScreen = { navController.navigate("deliveryEndScreen")}
            )
        }

        composable("deliveryEndScreen") {
            DeliveryEndScreen(
                navigationToHomeScreen = { navController.navigate("homeScreen")}
            )
        }
    }
}