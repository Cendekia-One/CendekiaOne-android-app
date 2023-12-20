package com.capstone.cendekiaone.ui.screen.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.capstone.cendekiaone.R
import com.capstone.cendekiaone.data.helper.LocalViewModelFactory
import com.capstone.cendekiaone.ui.component.ButtonComponent
import com.capstone.cendekiaone.ui.component.OutlinedTextFieldComponent
import com.capstone.cendekiaone.ui.component.PasswordTextFieldComponent
import com.capstone.cendekiaone.ui.component.TextButtonComponent
import com.capstone.cendekiaone.ui.navigation.Screen
import com.capstone.cendekiaone.ui.theme.myFont
import com.capstone.cendekiaone.ui.theme.myFont2

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observe the loading state from the ViewModel
    val isLoading by loginViewModel.isLoading.observeAsState(initial = false)

    // Observe the registration result from the ViewModel
    val loginResult by loginViewModel.loginResult.observeAsState()

    Box(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.welcome_back),
                style = TextStyle(
                    fontSize = 28.sp, textAlign = TextAlign.Center, fontFamily = myFont2
                ),
            )
            Text(
                text = stringResource(R.string.login_desc), style = TextStyle(
                    textAlign = TextAlign.Center, fontFamily = myFont
                ), modifier = Modifier.padding(top = 16.dp)
            )
            Image(
                painter = painterResource(R.drawable.img_login),
                contentDescription = "image_login",
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.CenterHorizontally)
            )
            OutlinedTextFieldComponent(
                provideText = stringResource(R.string.enter_email),
                icon = painterResource(R.drawable.ic_email_filled),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextFieldComponent(
                icon = painterResource(R.drawable.ic_password_filled),
                value = password,
                onValueChange = { password = it }
            )
            Spacer(modifier = Modifier.height(24.dp))
            ButtonComponent(
                provideText = stringResource(R.string.signIn),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Trigger the registration process in the ViewModel
                loginViewModel.login(email, password)
            }
            Row {
                Text(
                    text = stringResource(R.string.dont_have_account), style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = myFont,
                    ), modifier = Modifier.align(CenterVertically)
                )
                TextButtonComponent(provideText = stringResource(R.string.signUp),
                    onClick = { navController.navigate(Screen.Register.route) }
                )
            }
        }

        // Handle login result
        loginResult?.let { result ->
            when (result) {
                is LoginViewModel.LoginResult.Success -> {
                    // Registration is successful, show Toast and navigate to LoginScreen
                    ShowToast("Login Success")
                    navController.navigate(Screen.Home.route)
                    // Reset the registration result to allow for future registrations
                    loginViewModel.resetLoginResult()
                }
                is LoginViewModel.LoginResult.Error -> {
                    // Handle error if needed, show Toast
                    ShowToast("Login Failed")
                    // Reset the registration result to allow for future registrations
                    loginViewModel.resetLoginResult()
                }
                is LoginViewModel.LoginResult.NetworkError -> {
                    // Handle network error if needed, show Toast
                    ShowToast("Network Error")
                    // Reset the registration result to allow for future registrations
                    loginViewModel.resetLoginResult()
                }
            }
        }

        // Loading indicator
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                strokeWidth = 5.dp
            )
        }
    }
}

@Composable
private fun ShowToast(message: String) {
    Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true, device = "id:pixel_4")
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}