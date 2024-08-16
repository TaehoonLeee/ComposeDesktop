import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun App() {
    val l10nScope = rememberCoroutineScope()
    val viewModel = remember { MainViewModel() }

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
                    JsonPrinter(
                        onClickBack = viewModel::onClickBackButton,
                        jsonStringList = remember {
                            derivedStateOf {
                                viewModel.l10nData[content.distribution.key]
                                    ?.toList()
                            }
                        }.value
                    )
                }
            }
        }

    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
