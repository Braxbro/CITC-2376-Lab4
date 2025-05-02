@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.userdirectory.ui.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.userdirectory.R
import com.example.userdirectory.data.types.UserProfile
import com.example.userdirectory.ui.view.UserDirUiState.*
import kotlinx.coroutines.delay


// And now for the easy part. The visuals.
@Composable
fun UserDirApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { UserDirTopBar(scrollBehavior) }
    ) {
        val userDirViewModel: UserDirViewModel =
            viewModel(factory = UserDirViewModel.Factory)
        Surface(Modifier.fillMaxSize().padding(it)) {
            StateScreen(
                uiState = userDirViewModel.uiState,
                retryAction = userDirViewModel::getUserData,
            )
        }
    }
}

@Composable
fun UserDirTopBar(scrollBehavior: TopAppBarScrollBehavior, modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    )
}

@Composable
fun StateScreen(
    uiState: UserDirUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (uiState) {
        // no need for padding here because the spacing from alignment is enough by itself
        is Loading -> LoadScreen(modifier)
        is Error -> ErrScreen(retryAction, modifier)
        // padding goes here for outside the list
        is Success -> UserListScreen(uiState.users.response, modifier, contentPadding = PaddingValues(16.dp))
    }
}

@Composable
fun LoadScreen(modifier: Modifier = Modifier) {
    val baseText: String = stringResource(R.string.loading)
    val delayInMs = 200L
    var loadText by remember {
        mutableStateOf("")
    }
    // I animated the loading screen. It was unnecessary...
    // ...but I needed something whimsical to stave off insanity.
    // adding another 'fuck kotlin' to this project's comments
    LaunchedEffect(baseText) {
        var i = 0
        while(true) {
            i = (i + 1) % 4 // loop between 0 and 4 periods while loading
            loadText = baseText + " .".repeat(i)
            delay(delayInMs)
        }
    }
    Box(contentAlignment = Alignment.Center) {
        Text(loadText, modifier = modifier)
    }

}

@Composable
fun ErrScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun UserListScreen(
    users: List<UserProfile>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(modifier = modifier, contentPadding = contentPadding) {
        itemsIndexed(users) { _, user: UserProfile ->
            UserCard(user)
        }
    }
}

@Composable
fun UserCard(user: UserProfile, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(4.dp)) {
        Row() {
            Card(Modifier.size(80.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(user.picture.large).crossfade(true).build(),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = user.name.first + " " + user.name.last + "'s profile image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(modifier.fillMaxSize().padding(16.dp)) {
                Text(
                    text = user.name.first + " " + user.name.last,
                    style = (MaterialTheme.typography.bodyLarge),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                if (user.email != null) {
                    Text(
                        text = user.email,
                        style = (MaterialTheme.typography.bodySmall)
                    )
                }
            }

        }
    }
}


