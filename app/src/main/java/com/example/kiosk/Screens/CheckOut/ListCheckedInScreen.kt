package com.example.kiosk.Screens.CheckOut

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.kiosk.Dto.VisitorDto
import com.example.kiosk.RepeatButtons.ExitButton
import com.example.kiosk.RetrofitClient
import com.example.kiosk.ViewModels.VisitorViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ListCheckedInScreen(navigationBack: () -> Unit,
                        navigationToCheckOutEndScreen: () -> Unit) {
    val viewModel: VisitorViewModel = viewModel()

    // Fetch visitors on initial composition
    LaunchedEffect(Unit) {
        viewModel.fetchVisitors()
    }
    // Display the list of visitors
    VisitorList(visitors = viewModel.visitors, navigationBack = navigationBack, navigationToCheckOutEndScreen = navigationToCheckOutEndScreen)
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VisitorList(visitors: List<VisitorDto>, navigationBack: () -> Unit, navigationToCheckOutEndScreen: () -> Unit) {
    ExitButton {
        navigationBack()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        Arrangement.Bottom,
        Alignment.CenterHorizontally) {
        LazyColumn(modifier = Modifier
            .fillMaxWidth()  // Fill the width
            .height(600.dp) // Adjust height based on content)
        ) {
            items(visitors) { visitor ->
                VisitorItem(
                    visitor,
                    navigationToCheckOutEndScreen = navigationToCheckOutEndScreen
                )
            }
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VisitorItem(visitor: VisitorDto, navigationToCheckOutEndScreen: () -> Unit) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = 4.dp,
        onClick = {
            visitor.id?.let { id ->
                RetrofitClient.visitorApi.checkOutVisitor(id).enqueue(object : Callback<VisitorDto> {
                    override fun onResponse(call: Call<VisitorDto>, response: Response<VisitorDto>) {
                        if (response.isSuccessful) {
                            Log.d("VisitorItem", "Visitor checked out successfully: ${response.body()}")
                            navigationToCheckOutEndScreen()
                        } else {
                            Log.e("VisitorItem", "Error checking out visitor: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<VisitorDto>, t: Throwable) {
                        Log.e("VisitorItem", "API call failed: $t")
                    }
                })
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val byteArray = decodePictureString(visitor.picture)
            imageBitmap = byteArrayToBitmap(byteArray)

            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier.size(64.dp),
                    alignment = Alignment.Center,
                )
            } ?: run {
                Log.e("VisitorItem", "Image bitmap is null or invalid base64 string")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${visitor.firstName} ${visitor.lastName}",
                fontSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(text = "Email: ${visitor.email}", fontSize = 14.sp)
            Text(text = "Company: ${visitor.company}", fontSize = 14.sp)
            Text(text = "Phone: ${visitor.phoneNumber}", fontSize = 14.sp)
        }
    }
}

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

@RequiresApi(Build.VERSION_CODES.O)
fun decodePictureString(pictureString: String?): ByteArray {
    return Base64.getDecoder().decode(pictureString)
}