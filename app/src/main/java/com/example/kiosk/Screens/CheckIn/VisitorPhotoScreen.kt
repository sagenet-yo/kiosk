import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.kiosk.Dto.CheckInDto
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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Base64


@RequiresApi(Build.VERSION_CODES.O)
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
    phoneNumber: String,
    personOfInterest: String
) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var visitorDto by remember { mutableStateOf<VisitorDto?>(null) }
    var isNavigating by remember { mutableStateOf(false) }
    var isConfirming by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val sharedPreferences = context.getSharedPreferences("Kiosk", MODE_PRIVATE)
    val location = sharedPreferences.getString("location", "Default Value") ?: ""

    var previewView: PreviewView? = remember { PreviewView(context) }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }

    LaunchedEffect(cameraPermissionState.status) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        } else if (cameraPermissionState.status.isGranted) {
            setupCameraPreview(
                context,
                lifecycleOwner,
                previewView,
                imageCapture
            ) { bitmap, error ->
                imageBitmap = bitmap
                error?.let { Log.e("CameraXScreen", "Error: $it") }
            }
        } else {
            Log.e("CameraXScreen", "Camera permission denied")
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

                if (isConfirming && imageBitmap != null) {
                    // Display the captured image for confirmation
                    Image(
                        bitmap = imageBitmap!!.asImageBitmap(),
                        contentDescription = "",
                        modifier = Modifier
                            .size(450.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirmation buttons
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            isConfirming = false
                            imageBitmap = null
                        }) {
                            Text(text = "Retake")
                        }
                        Button(onClick = {
                            val pictureByteArray = imageBitmap?.let { bitmapToByteArray(it) } ?: ByteArray(0)
                            val pictureString: String = Base64.getEncoder().encodeToString(pictureByteArray)

                            visitorDto = VisitorDto(
                                id = null,
                                firstName = firstName,
                                lastName = lastName,
                                email = email,
                                checkedIn = true,
                                company = company,
                                phoneNumber = phoneNumber,
                                picture = pictureString,
                                personOfInterest = personOfInterest,
                                checkInTime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")) + " " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")),
                                checkOutTime = ""
                            )

                            val checkInDto = CheckInDto(visitorDto!!.personOfInterest, location, visitorDto!!)

                            visitorApi.checkInEmail(checkInDto)
                                .enqueue(object : Callback<String> {
                                    override fun onResponse(call: Call<String>, response: Response<String>) {
                                        if (response.isSuccessful) {
                                            Log.i("API_SUCCESS", "Email sent successfully")
                                        } else {
                                            Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<String>, t: Throwable) {
                                        Log.e("VisitorPhotoScreen", "API call failed", t)
                                    }
                                })

                            visitorApi.printLabel(visitorDto!!)
                                .enqueue(object : Callback<String> {
                                    override fun onResponse(call: Call<String>, response: Response<String>) {
                                        if (response.isSuccessful) {
                                            Log.i("API_SUCCESS", "Label printed successfully")
                                        } else {
                                            Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<String>, t: Throwable) {
                                        Log.e("VisitorPhotoScreen", "API call failed", t)
                                    }
                                })

                            isNavigating = true
                        }) {
                            Text(text = "Confirm")
                        }
                    }
                } else {
                    // Display the camera preview
                    Box(
                        modifier = Modifier
                            .size(450.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                previewView ?: PreviewView(ctx).also { previewView = it }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to capture image
                    Button(onClick = {
                        captureImage(context, imageCapture) { bitmap, error ->
                            if (error != null) {
                                Log.e("CameraXScreen", "Error capturing image: $error")
                                return@captureImage
                            }
                            imageBitmap = bitmap
                            isConfirming = true
                        }
                    }) {
                        Text(text = "Capture Image")
                    }
                }

                if (isNavigating && visitorDto != null) {
                    VisitorUploadScreen(visitorDto!!) {
                        navigationToCheckInEndScreen()
                        isNavigating = false
                    }
                }
            }
        }
    )
}

@Composable
fun VisitorUploadScreen(visitorDto: VisitorDto, onSuccess: () -> Unit) {
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(visitorDto) {
        isLoading = true // Show loader

        visitorApi.create(visitorDto).enqueue(object : Callback<VisitorDto> {
            override fun onResponse(call: Call<VisitorDto>, response: Response<VisitorDto>) {
                isLoading = false // Hide loader
                if (response.isSuccessful) {
                    Log.d("VisitorApi", "Visitor created successfully: ${response.body()}")
                    onSuccess() // Call onSuccess when upload is successful
                } else {
                    Log.e(
                        "VisitorApi",
                        "Error creating visitor: ${response.code()} - ${
                            response.errorBody()?.string()
                        }"
                    )
                }
            }

            override fun onFailure(call: Call<VisitorDto>, t: Throwable) {
                isLoading = false // Hide loader
                Log.e("VisitorApi", "API call failed: $t")
            }
        })
    }

    // Show loading indicator if isLoading is true
    if (isLoading) {
        CircularProgressIndicator() // Show loader
    } else {
        // Optionally show some UI after the upload
        Text("Visitor uploaded.")
    }
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
    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = image.toBitmap()
                val croppedBitmap = cropToSquare(bitmap)
                val compressedBitmap = compressBitmap(croppedBitmap, 5) // Compress to 5% quality
                onImageCaptured(compressedBitmap, null)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                onImageCaptured(null, exception.message)
            }
        })
}

private fun cropToSquare(bitmap: Bitmap): Bitmap {
    val dimension = minOf(bitmap.width, bitmap.height)
    val xOffset = (bitmap.width - dimension) / 2
    val yOffset = (bitmap.height - dimension) / 2
    return Bitmap.createBitmap(bitmap, xOffset, yOffset, dimension, dimension)
}

private fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    val byteArray = outputStream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

private fun ImageProxy.toBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

