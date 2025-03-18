package com.example.kiosk

import EmployeeListScreen
import ThreeOptionDeliveryScreen
import VisitorPhotoScreen
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kiosk.Screens.CheckIn.CheckInEndScreen
import com.example.kiosk.Screens.CheckIn.VisitorInfoScreen
import com.example.kiosk.Screens.CheckOut.CheckOutEndScreen
import com.example.kiosk.Screens.CheckOut.ListCheckedInScreen
import com.example.kiosk.Screens.Delivery.DeliveryEndScreen
import com.example.kiosk.Screens.HomeScreen
import com.example.kiosk.Screens.SetupScreen
import com.example.kiosk.Screens.ThreeOptionHomeScreen
import com.example.kiosk.ui.theme.KioskTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KioskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
                    val isFirstLaunch = sharedPreferences.getBoolean("isFirstLaunch", true)

                    if (isFirstLaunch)
                    {
                        SetupScreen()
                        sharedPreferences.edit().putBoolean("isFirstLaunch", false).apply()
                    }
                    else
                    {
                        Kiosk()
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
                navigationToThreeOptionDeliveryScreen = { navController.navigate("threeOptionDeliveryScreen") },
                navigationToThreeOptionCheckOutScreen = { navController.navigate("listCheckedInScreen") },
                navigationToEmployeeListScreen = { navController.navigate("employeeListScreen") }
            )
        }
        composable("employeeListScreen") {
            EmployeeListScreen(
                navigationBack = { navController.popBackStack() },
                navigationToVisitorInfoScreen = { personOfInterest: String, personOfInterestEmail: String ->
                    navController.navigate("visitorInfoScreen/${personOfInterest}/${personOfInterestEmail}")
                }
            )
        }
        composable(
            "visitorInfoScreen/{personOfInterest}/{personOfInterestEmail}",
            arguments = listOf(
                navArgument("personOfInterest") { type = NavType.StringType },
                navArgument("personOfInterestEmail") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val personOfInterest = backStackEntry.arguments?.getString("personOfInterest")
            val personOfInterestEmail = backStackEntry.arguments?.getString("personOfInterestEmail")
            personOfInterest?.let {
                VisitorInfoScreen(
                    personOfInterest = it,
                    personOfInterestEmail = personOfInterestEmail!!,
                    navigationBack = { navController.popBackStack() },
                    navigationToVisitorPhotoScreen = { firstName, lastName, email, company, phoneNumber, personOfInterest, personOfInterestEmail ->
                        navController.navigate("visitorPhotoScreen/$firstName/$lastName/$email/$company/$phoneNumber/${personOfInterest}/${personOfInterestEmail}")
                    }
                )
            } ?: Log.e("NavError", "personOfInterest argument is null")
        }

        composable(
            "visitorPhotoScreen/{firstName}/{lastName}/{email}/{company}/{phoneNumber}/{personOfInterest}/{personOfInterestEmail}",
            arguments = listOf(
                navArgument("firstName") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("email") { type = NavType.StringType },
                navArgument("company") { type = NavType.StringType },
                navArgument("phoneNumber") { type = NavType.StringType },
                navArgument("personOfInterest") { type = NavType.StringType },
                navArgument("personOfInterestEmail") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.let { args ->
                val firstName = args.getString("firstName")
                val lastName = args.getString("lastName")
                val email = args.getString("email")
                val company = args.getString("company")
                val phoneNumber = args.getString("phoneNumber")
                val personOfInterest = args.getString("personOfInterest")
                val personOfInterestEmail = args.getString("personOfInterestEmail")

                if (listOf(
                        firstName,
                        lastName,
                        email,
                        company,
                        phoneNumber,
                        personOfInterest,
                        personOfInterestEmail
                    ).all { it != null }
                ) {
                    VisitorPhotoScreen(
                        firstName = firstName!!,
                        lastName = lastName!!,
                        email = email!!,
                        company = company!!,
                        phoneNumber = phoneNumber!!,
                        personOfInterest = personOfInterest!!,
                        personOfInterestEmail = personOfInterestEmail!!,
                        navigationBack = { navController.popBackStack() },
                        navigationToCheckInEndScreen = { navController.navigate("checkInEndScreen") }
                    )
                } else {
                    Log.e("NavError", "Some arguments were null")
                }
            }
        }

        composable("checkInEndScreen") {
            CheckInEndScreen(
                navigationToHomeScreen = { navController.navigate("homeScreen") }
            )
        }

        composable("listCheckedInScreen") {
            ListCheckedInScreen(
                navigationBack = { navController.popBackStack() },
                navigationToCheckOutEndScreen = { navController.navigate("checkOutEndScreen") }
            )
        }

        composable("checkOutEndScreen"){
            CheckOutEndScreen(
                navigationToHomeScreen = { navController.navigate("homeScreen") }
            )
        }

        composable("threeOptionDeliveryScreen") {
            ThreeOptionDeliveryScreen(
                navigationBack = { navController.popBackStack() },
                navigationToDeliveryEndScreen = { navController.navigate("deliveryEndScreen") }
            )
        }

        composable("deliveryEndScreen") {
            DeliveryEndScreen(
                navigationToHomeScreen = { navController.navigate("homeScreen") }
            )
        }
    }
}

