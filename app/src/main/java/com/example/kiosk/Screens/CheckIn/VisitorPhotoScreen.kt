import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
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
import com.brother.ptouch.sdk.Printer
import com.brother.ptouch.sdk.PrinterInfo
import com.brother.sdk.lmprinter.Channel
import com.brother.sdk.lmprinter.OpenChannelError
import com.brother.sdk.lmprinter.PrintError
import com.brother.sdk.lmprinter.PrinterDriverGenerator
import com.brother.sdk.lmprinter.PrinterModel
import com.brother.sdk.lmprinter.setting.PrintImageSettings
import com.brother.sdk.lmprinter.setting.PrintSettings
import com.brother.sdk.lmprinter.setting.QLPrintSettings
import com.example.kiosk.Dto.CheckInDto
import com.example.kiosk.Dto.VisitorDto
import com.example.kiosk.R
import com.example.kiosk.RepeatButtons.ExitButton
import com.example.kiosk.RetrofitClient.visitorApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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
    personOfInterest: String,
    personOfInterestEmail: String
) {
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var visitorDto by remember { mutableStateOf<VisitorDto?>(null) }
    var isNavigating by remember { mutableStateOf(false) }
    var isConfirming by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val location: String = sharedPreferences.getString("location", "none") ?: "none"
    val printerIp: String = sharedPreferences.getString("printerIp", "none") ?: "none"

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
            ExitButton(onClick = { navigationBack() })

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
                                checkOutTime = "",
                                location = location
                            )

                            val checkInDto = CheckInDto(personOfInterestEmail, location, visitorDto!!)

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
                                        Log.e("Failure", "API call failed", t)
                                    }
                                })

                            val bitmap = createLabel(visitorDto!!, imageBitmap, context)
                            printLabelOverWiFi(bitmap, printerIp, context)

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

fun printLabelOverWiFi(bitmap: Bitmap, printerIp: String, context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        val channel: Channel = Channel.newWifiChannel(printerIp)
        val result = PrinterDriverGenerator.openChannel(channel)

        if (result.error.code != OpenChannelError.ErrorCode.NoError) {
            Log.e("BrotherPrinter", "Error - Open Channel: ${result.error.code}, Message: ${result.error.code}")
            return@launch
        }

        Log.d("BrotherPrinter", "Success - Open Channel")
        val printerDriver = result.driver

        // Configure printer settings using QLPrintSettings
        val printSettings = QLPrintSettings(PrinterModel.QL_810W).apply {
            // Set the necessary properties for your print settings here
            labelSize = QLPrintSettings.LabelSize.RollW62
            isAutoCut = true
            workPath = context.filesDir.absolutePath // Set the work path
            printOrientation = PrintImageSettings.Orientation.Landscape
            resolution = PrintImageSettings.Resolution.High
            halftone = PrintImageSettings.Halftone.PatternDither
            halftoneThreshold = 10
        }

        try {
            val printResult = printerDriver.printImage(bitmap, printSettings)
            if (printResult.code != PrintError.ErrorCode.NoError) {
                Log.e("BrotherPrinter", "Print failed with error code: ${printResult.errorDescription}")
            } else {
                Log.d("BrotherPrinter", "Print successful!")
            }
        } finally {
            printerDriver.closeChannel()
        }
    }
}


fun createLabel(visitorDto: VisitorDto, visitorPhoto: Bitmap?, context: Context): Bitmap {
    val labelWidth = 800
    val labelHeight = 500

    val labelBitmap = Bitmap.createBitmap(labelWidth, labelHeight, Bitmap.Config.RGB_565)
    val canvas = Canvas(labelBitmap)

    val paint = Paint().apply {
        color = android.graphics.Color.BLACK
        textSize = 40f
        typeface = Typeface.DEFAULT
        isAntiAlias = true
    }

    canvas.drawColor(android.graphics.Color.WHITE)

    // Decode the Sagenet logo from resources
    val sagenetLogo = BitmapFactory.decodeResource(context.resources, R.drawable.sagenet_color_logo)
    val scaledLogo = Bitmap.createScaledBitmap(sagenetLogo, 150, 50, true)
    canvas.drawBitmap(scaledLogo, 10f, 10f, null)

    paint.textSize = 30f
    canvas.drawText("Visitor", 500f, 50f, paint)

    paint.textSize = 50f
    canvas.drawText("${visitorDto.firstName} ${visitorDto.lastName}", 350f, 200f, paint)

    paint.textSize = 30f
    canvas.drawText("From: ${visitorDto.company}", 350f, 300f, paint)
    canvas.drawText("Issued On: ${visitorDto.checkInTime}", 100f, 450f, paint)

    visitorPhoto?.let {
        val scaledPhoto = Bitmap.createScaledBitmap(it, 300, 300, true)
        canvas.drawBitmap(scaledPhoto, 10f, 100f, null)
    }

    return labelBitmap
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
                val compressedBitmap = compressBitmap(croppedBitmap, 10) // Compress to 10% quality
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

