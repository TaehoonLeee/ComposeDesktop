import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import network.ApiExecutor
import network.Distribution
import network.DistributionList

sealed class MainContent {
    object FileList: MainContent()
    data class JsonPrinter(val distribution: Distribution): MainContent()
}

sealed class MainState {
    object Idle : MainState()
    object Loading : MainState()
    data class Error(val exception: Exception) : MainState()
    data class List(val distributionList: DistributionList) : MainState()
}

class MainViewModel {

    var content: MainContent by mutableStateOf(MainContent.FileList)
        private set

    var state: MainState by mutableStateOf(MainState.Idle)
        private set

    private val _l10nData = mutableStateMapOf<String, Map<String, JsonElement>>()
    val l10nData: Map<String, Map<String, JsonElement>> get() = _l10nData

    private val jsonParserClient = HttpClient(CIO)
    suspend fun fetch(phase: String = "dev") {
        state = MainState.Loading

        state = try {
            MainState.List(ApiExecutor.fetch(phase))
        } catch (e: Exception) {
            MainState.Error(e)
        }

        if (state is MainState.List) {
            val distributions = (state as? MainState.List)?.distributionList?.distributions?: return

            val threadPool = newFixedThreadPoolContext(distributions.size, "JsonParser")
            CoroutineScope(threadPool).launch {
                distributions.map {
                    async {
                        val jsonString = jsonParserClient
                            .request<HttpStatement>(it.url)
                            .receive<String>()

                        _l10nData[it.key] = Json.parseToJsonElement(jsonString).jsonObject
                    }
                }.forEach {
                    it.await()
                }
            }
        }
    }

    fun onClickDistribution(distribution: Distribution) {
        content = MainContent.JsonPrinter(distribution)
    }

    fun onClickBackButton() {
        content = MainContent.FileList
    }
}