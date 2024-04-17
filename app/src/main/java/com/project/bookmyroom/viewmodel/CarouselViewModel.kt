package com.project.bookmyroom.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.bookmyroom.R

class CarouselViewModel : ViewModel() {
    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val headings = arrayOf(
        R.string.carousel_1_heading,
        R.string.carousel_2_heading,
        R.string.carousel_3_heading
    )

    private val bodyTexts = arrayOf(
        R.string.carousel_1_body_text,
        R.string.carousel_2_body_text,
        R.string.carousel_3_body_text
    )

    private val images = arrayOf(
        R.drawable.img_carousel_1,
        R.drawable.img_carousel_2,
        R.drawable.img_carousel_3
    )

    fun nextImage() {
        val currentIndexValue = _currentIndex.value ?: 0
        _currentIndex.value = (currentIndexValue + 1) % images.size
    }
    fun isLastImage(): Boolean {
        return _currentIndex.value == images.size - 1
    }

    fun getCurrentHeadingResId(): Int {
        return headings[_currentIndex.value ?: 0]
    }

    fun getCurrentBodyTextResId(): Int {
        return bodyTexts[_currentIndex.value ?: 0]
    }

    fun getCurrentImageResId(): Int {
        return images[_currentIndex.value ?: 0]
    }
}
