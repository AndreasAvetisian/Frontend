package com.example.mobile_development_project_group_1

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

const val HOME_ROUTE = "home"
const val NOTE_ROUTE = "note"

@Composable
fun MainView() {
    val userVM = viewModel<UserViewModel>()

    if (userVM.username.value.isEmpty()) {
        LoginView(userVM)
    } else {
        MainScaffoldView()
    }
}

@Composable
fun MainScaffoldView() {

    val navController = rememberNavController()

    Scaffold(
        topBar = { TopBarView() },
        bottomBar = { BottomBarView(navController) },
        content = { MainContentView(navController) }
    )
}

@Composable
fun TopBarView() {
    val userVM = viewModel<UserViewModel>()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF5722))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = userVM.username.value)
        OutlinedButton(onClick = { userVM.logout() }) {
            Text(text = "Log out")
        }
    }
}

@Composable
fun MainContentView(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HOME_ROUTE ) {
        composable(route = HOME_ROUTE) { HomeView() }
        composable(route = NOTE_ROUTE) { NoteView() }
    }
}

@Composable
fun HomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(text = "This is home page")
    }
}

@Composable
fun NoteView() {

    var noteText by remember { mutableStateOf("") }
    val noteVM = viewModel<NoteViewModel>(LocalContext.current as ComponentActivity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text(text = "Todo note") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                noteVM.addNote( Note(noteText) )
            }
        ) {
            Text(text = "Add todo note")
        }

        Spacer(modifier = Modifier.height(10.dp))

        noteVM.notes.value.forEach {
            Divider(thickness = 2.dp)
            Text(text = it.message)
        }
        Divider(thickness = 2.dp)

    }
}

@Composable
fun BottomBarView(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF5722))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            painter = painterResource( R.drawable.ic_home ),
            contentDescription = "home",
            modifier = Modifier.clickable { navController.navigate(HOME_ROUTE) }
        )
        Icon(
            painter = painterResource( R.drawable.ic_note ),
            contentDescription = "note",
            modifier = Modifier.clickable { navController.navigate(NOTE_ROUTE) }
        )
    }
}


@Composable
fun LoginView(userVM: UserViewModel) {
    var email by remember { mutableStateOf("") }
    var pw by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") }
        )
        OutlinedTextField(
            value = pw,
            onValueChange = { pw = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        OutlinedButton(
            onClick = { userVM.loginUser(email, pw) },
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(text = "Login")
        }
    }
}