import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.kiosk.Dto.VisitorDto
import com.example.kiosk.R
import com.example.kiosk.RepeatButtons.ExitButton
import com.example.kiosk.RetrofitClient.visitorApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun VisitorPhotoScreen(
    navigationBack: () -> Unit,
    navigationToCheckInEndScreen: () -> Unit,
    firstName: String,
    lastName: String,
    email: String,
    company: String,
    phoneNumber: String
) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    // PreviewView that will display the camera feed
    var previewView: PreviewView? = remember { PreviewView(context) }

    // Initialize ImageCapture
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }

    // Request permission
    LaunchedEffect(cameraPermissionState.status) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        } else {
            setupCameraPreview(context, lifecycleOwner, previewView, imageCapture) { bitmap, error ->
                imageBitmap = bitmap
                error?.let { Log.e("CameraXScreen", "Error: $it") }
            }
        }
    }

    Scaffold(
        content = {
            ExitButton { navigationBack() }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sagenet_logo),
                    contentDescription = "",
                    modifier = Modifier.size(150.dp)
                )

                // Circular Box for the camera preview or "No Image" text
                Box(
                    modifier = Modifier
                        .size(450.dp)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                        .border(2.dp, Color.Black, CircleShape)
                ) {
                    if (previewView == null) {
                        // Display "No Image" if preview is not ready
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No Image",
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        // Display the camera preview
                        AndroidView(
                            factory = { ctx ->
                                previewView?.also { it } ?: PreviewView(ctx).also { previewView = it }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button to capture image
                Button(onClick = {
                    captureImage(context, imageCapture) {bitmap, error ->
                        if (error != null) {
                            Log.e("CameraXScreen", "Error capturing image: $error")
                            return@captureImage
                        }
                    }
                    // Convert Bitmap to ByteArray after imageBitmap is set
                    val pictureByteArray = bitmap?.let { bitmapToByteArray(it) } ?: ByteArray(0)
                    Log.i("ByteCaptured", "${pictureByteArray.size}")
                    // Create VisitorDto after image is captured
                    val visitorDto = VisitorDto(
                        id = null, // or provide an ID if available
                        firstName = firstName, // replace with actual data
                        lastName = lastName,   // replace with actual data
                        email = email, // replace with actual data
                        checkedIn = true, // or true, depending on the status
                        company = company, // replace with actual data
                        phoneNumber = phoneNumber, // replace with actual data
                        picture = pictureByteArray // Pass the ByteArray here
                    )

                    // Make the API call and handle the response asynchronously
                    visitorApi.createVisitor(visitorDto).enqueue(object : Callback<VisitorDto> {
                        override fun onResponse(call: Call<VisitorDto>, response: Response<VisitorDto>) {
                            if (response.isSuccessful) {
                                val createdVisitor = response.body()
                                Log.d("VisitorApi", "Visitor created successfully: $createdVisitor")
                                navigationToCheckInEndScreen() // Navigate on success
                            } else {
                                Log.e("VisitorApi", "Error creating visitor: ${response.code()} - ${response.errorBody()?.string()}")
                                Log.e("VisitorApi", "Request: $call")
                                Log.e("VisitorApi", "Response: ${response.message()}")
                            }
                        }

                        override fun onFailure(call: Call<VisitorDto>, t: Throwable) {
                            Log.e("VisitorApi", "API call failed: $t")
                        }
                    })
                    navigationToCheckInEndScreen()
                }) {
                    Text(text = "Capture Image")
                }
            }
        }
    )
}

// Function to convert Bitmap to ByteArray
private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    ByteArrayOutputStream().use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}

private fun setupCameraPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView?,
    imageCapture: ImageCapture,
    onImageCaptured: (Bitmap?, String?) -> Unit
) {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    cameraProviderFuture.addListener({
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView?.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA // Front-facing camera

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture // Use the initialized instance
            )
        } catch (exc: Exception) {
            onImageCaptured(null, exc.message)
        }
    }, ContextCompat.getMainExecutor(context))
}

private fun captureImage(
    context: Context,
    imageCapture: ImageCapture,
    onImageCaptured: (Bitmap?, String?) -> Unit
) {
    imageCapture.takePicture(ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val bitmap = image.toBitmap()
            onImageCaptured(bitmap, null)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            onImageCaptured(null, exception.message)
        }
    })
}

private fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

