package com.capstone.cendekiaone.ui.screen.register

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
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = viewModel(
        factory = LocalViewModelFactory.provide()
    ),
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observe the loading state from the ViewModel
    val isLoading by registerViewModel.isLoading.observeAsState(initial = false)

    // Observe the registration result from the ViewModel
    val registrationResult by registerViewModel.registrationResult.observeAsState()

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
                text = stringResource(R.string.create_account),
                style = TextStyle(
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = myFont2
                ),
            )
            Text(
                text = stringResource(R.string.regis_desc),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = myFont
                ),
                modifier = Modifier.padding(top = 16.dp)
            )
            Image(
                painter = painterResource(R.drawable.img_regis),
                contentDescription = "image_login",
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.CenterHorizontally)
            )
            OutlinedTextFieldComponent(
                provideText = stringResource(R.string.enter_name),
                icon = painterResource(R.drawable.ic_name_filled),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                value = name,
                onValueChange = { name = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextFieldComponent(
                provideText = stringResource(R.string.enter_email),
                icon = painterResource(R.drawable.ic_email_filled),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                ),
                value = email,
                onValueChange = { email = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            PasswordTextFieldComponent(
                icon = painterResource(R.drawable.ic_password_filled),
                value = password,
                onValueChange = { password = it }
            )
            Spacer(modifier = Modifier.height(24.dp))
            ButtonComponent(
                provideText = stringResource(R.string.signUp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Trigger the registration process in the ViewModel
                registerViewModel.register(name, email, password)
            }
            Row {
                Text(
                    text = stringResource(R.string.have_account),
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = myFont,
                    ),
                    modifier = Modifier.align(CenterVertically)
                )
                TextButtonComponent(
                    provideText = stringResource(R.string.signIn),
                    onClick = { navController.navigate(Screen.Login.route) }
                )
            }
        }

        // Handle registrations result
        registrationResult?.let { result ->
            when (result) {
                is RegisterViewModel.RegistrationResult.Success -> {
                    // Registration is successful, show Toast and navigate to LoginScreen
                    ShowToast(result.message)
                    navController.navigate(Screen.Login.route)
                    // Reset the registration result to allow for future registrations
                    registerViewModel.resetRegistrationResult()
                }
                is RegisterViewModel.RegistrationResult.Error -> {
                    // Handle error if needed, show Toast
                    ShowToast(result.errorMessage)
                    // Reset the registration result to allow for future registrations
                    registerViewModel.resetRegistrationResult()
                }
                is RegisterViewModel.RegistrationResult.NetworkError -> {
                    // Handle network error if needed, show Toast
                    ShowToast("Network Error")
                    // Reset the registration result to allow for future registrations
                    registerViewModel.resetRegistrationResult()
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
    RegisterScreen(
        navController = rememberNavController()
    )
}