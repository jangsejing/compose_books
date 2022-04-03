package com.jess.compose.books.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jess.compose.books.data.entity.BookItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _list = MutableLiveData<List<BookItem>>()
    val list: LiveData<List<BookItem>> get() = _list

    private val _text = MutableLiveData<String>()
    val text: LiveData<String> get() = _text

    fun request(text: String?) {
        if (text.isNullOrEmpty()) {
            return
        }

        viewModelScope.launch {
            _text.value = text.orEmpty()

            val amount = (10..100).random()

            val list = arrayListOf<BookItem>().apply {
                for (i in 0 until amount) {
                    add(
                        BookItem(
                            title = "title $i",
                            contents = "contents $i",
                            url = "https://search.daum.net/search?w=bookpage&bookId=1639095&tab=introduction&DA=LB2&q=%EC%B1%85%EA%B2%80%EC%83%89",
                            thumbnail = "https://search1.kakaocdn.net/thumb/C116x164.q85/?fname=http%3A%2F%2Ft1.daumcdn.net%2Flbook%2Fimage%2F1639095%3Ftimestamp%3D20220318172846%3Fmoddttm=202204031559"
                        )
                    )
                }
            }
            _list.postValue(list)
        }
    }
}