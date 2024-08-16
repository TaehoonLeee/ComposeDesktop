import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.json.JsonElement

@Composable
fun JsonPrinter(
    onClickBack: () -> Unit,
    jsonStringList: List<Pair<String, JsonElement>>?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            IconButton(onClickBack) {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Default.ArrowBack
                )
            }
        }

        if (jsonStringList == null) {
            item {
                CircularProgressIndicator()
            }
        } else {
            items(jsonStringList.size) {
                val (key, value) = jsonStringList[it]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BasicTextField(
                        value = key,
                        readOnly = true,
                        onValueChange = {},
                    )

                    BasicTextField(
                        readOnly = true,
                        onValueChange = {},
                        value = value.toString(),
                    )
                }
            }
        }
    }
}