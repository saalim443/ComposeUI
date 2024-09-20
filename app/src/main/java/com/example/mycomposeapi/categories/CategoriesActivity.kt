package com.example.mycomposeapi.categories

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mycomposeapi.categories.ui.theme.MyComposeAPITheme
import com.example.mycomposeapi.util.BaseResponse
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState



class CategoriesActivity : ComponentActivity() {
    private val viewModel by viewModels<CategoriesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set the token directly here, but consider better management (e.g., from secure storage)
        val token = "2456|68ZO85dMQJUtpPMrN1zNE34CpTjzbPdJnpuT1JGqbfe3df7e"
        viewModel.fetchCategories(token) // Fetch categories on creation

        setContent {
            MyComposeAPITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CategoryUI(modifier = Modifier.padding(innerPadding), viewModel = viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryUI(modifier: Modifier = Modifier, viewModel: CategoriesViewModel) {
    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    var showPermissionDialog by remember { mutableStateOf(false) }

    // Permission Request Launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasLocationPermission = isGranted
        }
    )

    // Check for Location Permission
    LaunchedEffect(Unit) {
        when {
            context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                hasLocationPermission = true
            }

            else -> {
                showPermissionDialog = true
            }
        }
    }

    // Show permission dialog if needed
    if (showPermissionDialog) {
        LaunchedEffect(Unit) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            showPermissionDialog = false // Avoid re-launching
        }
    }

    // Default location (e.g., New York City)
    val defaultLocation = LatLng(40.7128, -74.0060)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            if (hasLocationPermission) {
                // Google Map Composable
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(myLocationButtonEnabled = true),
                    properties = MapProperties(isMyLocationEnabled = true)
                ) {
                    // Marker at current location
                    Marker(
                        state = MarkerState(position = defaultLocation),
                        title = "Current Location",
                        snippet = "This is your current location"
                    )
                }
            } else {
                Text(
                    text = "Location permission required",
                    modifier = Modifier.align(Alignment.Center), // Center the text within the Box
                    color = Color.Red
                )
            }
        }

        // Card Composable takes up the other half of the screen
        Column(
            modifier = Modifier
                .weight(1f) // Take up half of the screen
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            // List of Posts
            PostListCategory(viewModel)
        }
    }
}


@Composable
fun PostListCategory(viewModel: CategoriesViewModel) {
    val context = LocalContext.current
    val categoriesState by viewModel.categoriesResult.observeAsState(BaseResponse.Loading())

    // Ensure handling of data loading
    LaunchedEffect(categoriesState) {
        when (categoriesState) {
            is BaseResponse.Error -> {
                Toast.makeText(
                    context,
                    (categoriesState as BaseResponse.Error).msg,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {
                // Handle other states if needed
            }
        }
    }

    // Use actual data from the API response
    val posts = (categoriesState as? BaseResponse.Success)?.data?.data ?: emptyList()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(posts) { post ->
            PostListCategoryPostItem(post)
        }
    }
}

@Composable
fun PostListCategoryPostItem(post: Data) {
    val context = LocalContext.current
    var isClicked by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .clickable {
                isClicked = !isClicked
                // Handle click event (if needed)
                // context.startActivity(Intent(context, ConfirmPickupActivity::class.java))
            },
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Profile Picture and Name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Picture
                    Image(
                        painter = rememberAsyncImagePainter(model = post.image), // Correct usage of image URL
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    // Name and Location
                    Column(
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = post.name, // Use actual post data
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Location info", // Replace with dynamic location info if needed
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

//                // Right side: Notification Icon
//                IconButton(
//                    onClick = { /* Handle notification click */ },
//                    modifier = Modifier.size(24.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.eye), // Replace with your actual icon resource
//                        contentDescription = "Notifications",
//                        tint = Color.Red
//                    )
//                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryUI() {
    MyComposeAPITheme {
        // Use a mock ViewModel for preview
        CategoryUI(
            modifier = Modifier.fillMaxSize(),
            viewModel = CategoriesViewModel(Application())
        )
    }
}
