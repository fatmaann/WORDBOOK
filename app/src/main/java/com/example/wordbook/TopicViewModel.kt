import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.wordbook.Topic
import com.example.wordbook.Word
import com.example.wordbook.WordBookDatabase

import kotlinx.coroutines.launch

class TopicViewModel : ViewModel() {
    val topics: MutableList<Topic> = mutableListOf(
        Topic(0, "Ошибки", Color.WHITE),
        Topic(1, "Выученные", Color.WHITE)
    )

    val words: MutableList<Word> = mutableListOf()

    fun addWord(word: Word) {
        words.add(word)
    }

    fun getWordsForTopic(topicId: Int): List<Word> {
        return words.filter { it.topicId == topicId }
    }
}
