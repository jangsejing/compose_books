package com.jess.compose.books.presentation.main

import android.os.Bundle
import android.util.Size
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size.Companion.ORIGINAL
import com.jess.compose.books.R
import com.jess.compose.books.data.entity.BookItem
import com.jess.compose.books.ui.theme.BooksTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BooksTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainCompose()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MainCompose(mainViewModel: MainViewModel = viewModel()) {

    var input: String? = null

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(Dp(8f)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainSearchView(
                modifier = Modifier.weight(1f, true),
                input = {
                    input = it
                }
            )
            TextButton(
                onClick = {
                    mainViewModel.request(input)
                },
                modifier = Modifier
                    .width(Dp(100f))
                    .align(Alignment.CenterVertically),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary,
                    contentColor = MaterialTheme.colors.primary
                )
            ) {
                Text(text = "Search")
            }
        }

        BookListView()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainSearchView(
    modifier: Modifier,
    input: (String) -> Unit,
    mainViewModel: MainViewModel = viewModel()
) {
    val name = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = name.value,
        onValueChange = {
            name.value = it
            input(it)
            mainViewModel.request(it)
        },
        modifier = modifier,
        keyboardActions = KeyboardActions(onDone = {
            mainViewModel.request(name.value)
            keyboardController?.hide()
        }),
        label = {
            Text(text = "Input")
        }
    )
}

@ExperimentalMaterialApi
@Composable
fun BookListView(mainViewModel: MainViewModel = viewModel()) {
    val list: List<BookItem> by mainViewModel.list.observeAsState(listOf())
    LazyColumn(
        contentPadding = PaddingValues(Dp(8f))
    ) {
        items(list) { item ->
            BookListItem(item = item)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BookListItem(
    item: BookItem,
    mainViewModel: MainViewModel = viewModel()
) {
    Card(
        modifier = Modifier
            .padding(Dp(8f))
            .fillMaxWidth(),
        onClick = {
            mainViewModel.onItemClick(item)
        })
    {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.thumbnail)
                .build()
        )
        Column {
            Row {
                Image(
                    painter = painter,
                    contentDescription = stringResource(R.string.app_name),
                    modifier = Modifier
                        .width(Dp(100f))
                        .height(Dp(100f))
                )
                Text(item.title.orEmpty())
            }
            Text(item.contents.orEmpty())
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BooksTheme {
        Column {
            MainCompose()
            BookListItem(
                BookItem(
                    "제목",
                    "컨텐츠",
                    "3",
                    "4"
                )
            )
        }
    }
}