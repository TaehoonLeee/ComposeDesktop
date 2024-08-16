import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.json.JsonElement

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JsonPrinter(
    search: String,
    onClickBack: () -> Unit,
    focusRequester: FocusRequester,
    onSearchChange: (String) -> Unit,
    jsonStringList: List<Pair<String, JsonElement>>?
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        stickyHeader {
            BasicTextField(
                value = search,
                onValueChange = onSearchChange,
                modifier = Modifier.focusRequester(focusRequester)
            )
        }

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
                ) {
                    BasicTextField(
                        value = key,
                        readOnly = true,
                        onValueChange = {},
                        modifier = Modifier.weight(1f),
                    )

                    BasicTextField(
                        readOnly = true,
                        onValueChange = {},
                        value = value.toString(),
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}