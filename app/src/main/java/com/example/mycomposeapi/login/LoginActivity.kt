package com.example.mycomposeapi.login

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.example.mycomposeapi.MainActivity
import com.example.mycomposeapi.R
import com.example.mycomposeapi.categories.CategoriesActivity
import com.example.mycomposeapi.ui.theme.MyComposeAPITheme
import com.example.mycomposeapi.util.BaseResponse

class LoginActivity : ComponentActivity() {
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyComposeAPITheme {
                // Define the UI
                LoginScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val context = LocalContext.current
    // Define a brush with a linear gradient
    val brush = remember {
        Brush.linearGradient(
            colors = listOf(Color.Red, Color.Blue), // Define your gradient colors here
            start = Offset(0f, 0f), // Start point of the gradient
            end = Offset(1000f, 1000f) // End point of the gradient
        )
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val loginState by viewModel.loginResult.observeAsState(BaseResponse.Loading())

    LaunchedEffect(loginState) {
        when (loginState) {
            is BaseResponse.Success -> {
                context.startActivity(Intent(context, CategoriesActivity::class.java))
            }
            is BaseResponse.Error -> {
                Toast.makeText(context, (loginState as BaseResponse.Error).msg, Toast.LENGTH_SHORT).show()
            }
            else -> {
                // Handle loading or other states
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Email Field using OutlinedTextField

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(brush = brush)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password Field with Visibility Toggle
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisible) R.drawable.eye else R.drawable.eyeoff),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            textStyle = TextStyle(brush = brush),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Login Button
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.loginUser(email = email, pwd = password)
                } else {
                    Toast.makeText(context, "Please enter both email and password", Toast.LENGTH_SHORT).show()
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Set background color
                contentColor = Color.White // Set text color to white
            )
        ) {
            Text("Login", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Forgot Password
        ClickableText(
            text = AnnotatedString("Forgot Password?"),
            onClick = { /* Handle Forgot Password */ },
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Sign Up Option
        ClickableText(
            text = AnnotatedString("Don't have an account? Sign Up"),
            onClick = { /* Handle Sign Up Navigation */ },
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyComposeAPITheme {
        LoginScreen(viewModel = LoginViewModel(Application())) // Preview requires a non-null ViewModel
    }
}
