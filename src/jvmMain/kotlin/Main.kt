import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun App(viewModel: MainViewModel) {
    val l10nScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.fetch()
    }

    MaterialTheme {
        AnimatedContent(viewModel.content) { content ->
            when (content) {
                MainContent.FileList -> {
                    FileList(
                        state = viewModel.state,
                        onRetry = {
                            l10nScope.launch {
                                viewModel.fetch()
                            }
                        },
                        onClickDistribution = viewModel::onClickDistribution
                    )
                }

                is MainContent.JsonPrinter -> {
                    var search by remember {
                        mutableStateOf("")
                    }

                    val searchFocusRequester = remember(::FocusRequester)

                    LaunchedEffect(Unit) {
                        viewModel.searchKeyEvent.collect {
                            searchFocusRequester.requestFocus()
                        }
                    }

                    JsonPrinter(
                        search = search,
                        onSearchChange = { search = it },
                        focusRequester = searchFocusRequester,
                        onClickBack = viewModel::onClickBackButton,
                        jsonStringList = remember {
                            derivedStateOf {
                                viewModel.l10nData[content.distribution.key]
                                    ?.toList()
                                    ?.let {
                                        if (search.isNotEmpty()) {
                                            it.filter {
                                                it.first.contains(search, true) ||
                                                        it.second.toString().contains(search, true)
                                            }
                                        } else {
                                            it
                                        }
                                    }
                            }
                        }.value
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val keyEventScope = rememberCoroutineScope()
    val viewModel = remember { MainViewModel() }

    Window(
        onCloseRequest = ::exitApplication,
        onKeyEvent = {
            val isMacOS = System.getProperty("os.name").contains("Mac")
            if (
                if (isMacOS) it.isMetaPressed else it.isCtrlPressed
                        && it.key == Key.F
                        && it.type == KeyEventType.KeyUp
                ) {
                keyEventScope.launch {
                    viewModel.onSearchKeyEvent()
                }
                true
            } else {
                false
            }
        }
    ) {
        App(viewModel)
    }
}
