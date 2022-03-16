package com.example.mobile_development_project_group_1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

const val HOME_ROUTE = "home"
const val NOTE_ROUTE = "note"
const val SIGNUP_ROUTE = "signUp"
const val PROFILE_ROUTE = "profile"
const val ADMIN_ROUTE = "ADMIN"
const val MANAGER_ROUTE = "MANAGER"
const val USER_ROUTE = "USER"

//@Composable
//fun MainView() {
//    val userVM = viewModel<UserViewModel>()
//    val fAuth = Firebase.auth
//
//    if (fAuth.currentUser?.uid?.isEmpty() == false) {
//        MainScaffoldView()
//    } else {
//        LoginView(userVM = viewModel())
//    }
//}

@Composable
fun MainScaffoldView() {
    val navController = rememberNavController()
    val scState = rememberScaffoldState( rememberDrawerState(DrawerValue.Closed) )

    Scaffold(
        scaffoldState = scState,
        topBar = { TopBarView(navController, scState) },
        bottomBar = { BottomBarView(navController) },
        content = { MainContentView(navController) },
        drawerContent = { DrawerLayoutView(navController, scState) }
    )
}

@Composable
fun TopBarView(navController: NavHostController, scState: ScaffoldState) {

    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFF5722))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource( R.drawable.ic_menu ),
            contentDescription = "note",
            modifier = Modifier.clickable {
                scope.launch {
                    scState.drawerState.open()
                }
            }
        )
        Icon(
            painter = painterResource( R.drawable.ic_login ),
            contentDescription = "note",
            modifier = Modifier.clickable { navController.navigate(SIGNUP_ROUTE) }
        )
    }
}

@Composable
fun MainContentView(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HOME_ROUTE ) {
        composable(route = HOME_ROUTE) { HomeView() }
        composable(route = NOTE_ROUTE) { NoteView() }
        composable(route = SIGNUP_ROUTE) { LoginView(UserViewModel(), navController) }
        composable(route = PROFILE_ROUTE) { ProfilePageView() }
    }
}

@Composable
fun HomeView() {
    val fireStore = Firebase.firestore
    val fAuth = Firebase.auth
    var currentUserRoute by remember { mutableStateOf("") }

    fireStore
        .collection("users")
        .document(fAuth.currentUser?.uid.toString())
        .get()
        .addOnSuccessListener {
            currentUserRoute = it.get("route").toString()
        }

    Column {
        Text(text = "Main page")
        when (currentUserRoute) {
            ADMIN_ROUTE -> Text(text = "You are $currentUserRoute")
            MANAGER_ROUTE -> Text(text = "You are $currentUserRoute")
            USER_ROUTE -> Text(text = "You are $currentUserRoute")
        }
    }
}

@Composable
fun NoteView() {
    Text(text = "AAA")
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
fun DrawerLayoutView(navController: NavHostController, scState: ScaffoldState) {

    val userVM = viewModel<UserViewModel>()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedButton(
            onClick = {
                navController.navigate(PROFILE_ROUTE)
                scope.launch {
                    scState.drawerState.close()
                }
            },
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(text = "Profile page")
        }

        OutlinedButton(onClick = {
            userVM.logout()
            scope.launch {
                scState.drawerState.close()
            }
            navController.navigate(HOME_ROUTE)
        }) {
            Text(text = "Log out")
        }
    }

}

@Composable
fun ProfilePageView() {
    val fireStore = Firebase.firestore
    val fAuth = Firebase.auth

    var currentUserFirstName by remember { mutableStateOf("") }
    var currentUserLastName by remember { mutableStateOf("") }
    var currentUserAddress by remember { mutableStateOf("") }
    var currentUserPhoneNumber by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    fireStore
        .collection("users")
        .document(fAuth.currentUser?.uid.toString())
        .get()
        .addOnSuccessListener {
            currentUserFirstName = it.get("first_name").toString()
            currentUserLastName = it.get("last_name").toString()
            currentUserAddress = it.get("address").toString()
            currentUserPhoneNumber = it.get("phone_num").toString()
        }

    Column() {
        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = { Text(text = currentUserFirstName) }
        )
        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = { Text(text = currentUserLastName) }
        )
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            placeholder = { Text(text = currentUserAddress) }
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = { Text(text = currentUserPhoneNumber) }
        )
    }
}


@Composable
fun LoginView(userVM: UserViewModel, navController: NavHostController) {
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
            onClick = {
                userVM.loginUser(email, pw)
                navController.navigate(HOME_ROUTE)
            },
            modifier = Modifier
                .padding(10.dp)
        ) {
            Text(text = "Login")
        }
    }
}