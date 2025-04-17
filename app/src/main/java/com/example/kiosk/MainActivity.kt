package com.example.kiosk

import EmployeeListScreen
import ThreeOptionDeliveryScreen
import VisitorPhotoScreen
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.example.kiosk.Screens.LoginScreen
import com.example.kiosk.Screens.ThreeOptionHomeScreen
import com.example.kiosk.ViewModels.LoginViewModel
import com.example.kiosk.ui.theme.KioskTheme

class MainActivity : ComponentActivity() {

    private val inactivityHandler = Handler(Looper.getMainLooper())
    private val inactivityRunnable = Runnable {
        val navController = NavControllerHolder.navController
        if (navController != null) {
            if (navController?.currentDestination?.route != "threeOptionHomeScreen" && navController.currentDestination?.route != "loginScreen") {
                navController?.navigate("threeOptionHomeScreen") {
                    popUpTo(0) { inclusive = false }
                }
            }
        }
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetInactivityTimer()
    }

    private fun resetInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable)
        inactivityHandler.postDelayed(inactivityRunnable, 45_000L)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            KioskTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Kiosk()
                }
            }
        }

        // Start the timer on initial launch
        resetInactivityTimer()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Kiosk() {
    val navController = rememberNavController()
    NavControllerHolder.navController = navController
    val context = LocalContext.current
    val loginViewModel = LoginViewModel(context)

    val sharedPreferences = LocalContext.current.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false)

    val startDestination = if (hasLoggedIn) {
        "threeOptionHomeScreen"
    } else {
        "loginScreen"
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("loginScreen") {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("hasLoggedIn", true).apply()

                    navController.navigate("threeOptionHomeScreen") {
                        popUpTo("loginScreen") { inclusive = true }
                    }
                }
            )
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
                navigationToVisitorInfoScreen = { personOfInterest, personOfInterestEmail ->
                    navController.navigate("visitorInfoScreen/$personOfInterest/$personOfInterestEmail")
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

            if (personOfInterest != null && personOfInterestEmail != null) {
                VisitorInfoScreen(
                    personOfInterest = personOfInterest,
                    personOfInterestEmail = personOfInterestEmail,
                    navigationBack = { navController.popBackStack() },
                    navigationToVisitorPhotoScreen = { firstName, lastName, email, company, phoneNumber, poi, poiEmail ->
                        navController.navigate("visitorPhotoScreen/$firstName/$lastName/$email/$company/$phoneNumber/$poi/$poiEmail")
                    }
                )
            } else {
                Log.e("NavError", "Missing visitorInfoScreen arguments")
            }
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
            val args = backStackEntry.arguments
            val allArgs = listOf(
                args?.getString("firstName"),
                args?.getString("lastName"),
                args?.getString("email"),
                args?.getString("company"),
                args?.getString("phoneNumber"),
                args?.getString("personOfInterest"),
                args?.getString("personOfInterestEmail")
            )

            if (allArgs.all { it != null }) {
                VisitorPhotoScreen(
                    firstName = allArgs[0]!!,
                    lastName = allArgs[1]!!,
                    email = allArgs[2]!!,
                    company = allArgs[3]!!,
                    phoneNumber = allArgs[4]!!,
                    personOfInterest = allArgs[5]!!,
                    personOfInterestEmail = allArgs[6]!!,
                    navigationBack = { navController.popBackStack() },
                    navigationToCheckInEndScreen = { navController.navigate("checkInEndScreen") }
                )
            } else {
                Log.e("NavError", "Some arguments were null in visitorPhotoScreen")
            }
        }

        composable("checkInEndScreen") {
            CheckInEndScreen(
                navigationToHomeScreen = { navController.navigate("threeOptionHomeScreen") }
            )
        }

        composable("listCheckedInScreen") {
            ListCheckedInScreen(
                navigationBack = { navController.popBackStack() },
                navigationToCheckOutEndScreen = { navController.navigate("checkOutEndScreen") }
            )
        }

        composable("checkOutEndScreen") {
            CheckOutEndScreen(
                navigationToHomeScreen = { navController.navigate("threeOptionHomeScreen") }
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
                navigationToHomeScreen = { navController.navigate("threeOptionHomeScreen") }
            )
        }
    }
}
