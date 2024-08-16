import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import network.Distribution

@Composable
fun FileList(
    state: MainState,
    onRetry: () -> Unit,
    onClickDistribution: (Distribution) -> Unit,
) {
    Box(Modifier.fillMaxSize().padding(12.dp)) {
        when (state) {
            MainState.Idle -> Unit
            MainState.Loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            is MainState.Error -> {
                Column(Modifier.align(Alignment.Center)) {
                    Text(state.exception.message?: "Error")
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }
            is MainState.List -> {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    state.distributionList.distributions.forEach { distribution ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onClickDistribution(distribution) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BasicTextField(
                                maxLines = 1,
                                readOnly = true,
                                onValueChange = {},
                                value = distribution.key,
                                modifier = Modifier.weight(1f, false)
                            )

                            BasicTextField(
                                maxLines = 1,
                                readOnly = true,
                                onValueChange = {},
                                value = distribution.url,
                            )
                        }
                    }
                }
            }
        }
    }
}