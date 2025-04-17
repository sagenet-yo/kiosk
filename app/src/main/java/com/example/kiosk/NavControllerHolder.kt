package com.example.kiosk

import android.annotation.SuppressLint
import androidx.navigation.NavController

object NavControllerHolder {
    @SuppressLint("StaticFieldLeak")
    var navController: NavController? = null
}
