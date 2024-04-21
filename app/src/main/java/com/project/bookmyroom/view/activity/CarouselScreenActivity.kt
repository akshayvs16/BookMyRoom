package com.project.bookmyroom.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.bookmyroom.R
import com.project.bookmyroom.databinding.ActivityCarouselScreenBinding
import com.project.bookmyroom.viewmodel.CarouselViewModel

class CarouselScreenActivity : AppCompatActivity() {
    private val viewModel: CarouselViewModel by lazy { ViewModelProvider(this)[CarouselViewModel::class.java] }
    private lateinit var binding: ActivityCarouselScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarouselScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Observe currentIndex changes to update UI
        viewModel.currentIndex.observe(this) { index ->
            binding.imageViewCarousel.setImageResource(viewModel.getCurrentImageResId())
            binding.textViewHeading.setText(viewModel.getCurrentHeadingResId())
            binding.textViewBodyText.setText(viewModel.getCurrentBodyTextResId())
            updateDots(index)
        }

        binding.nextButton.setOnClickListener {
            if (viewModel.isLastImage()) {
                navigateToLoginActivity()
            } else {
                viewModel.nextImage()
            }
        }
        binding.skipButton.setOnClickListener {
            navigateToLoginActivity()
        }
    }

    private fun updateDots(activeIndex: Int) {
        // Update SliderIndicator dots based on activeIndex
        val dotArray = arrayOf(binding.dot1, binding.dot2, binding.dot3)
        dotArray.forEachIndexed { index, imageView ->
            imageView.setImageResource(if (index == activeIndex) R.drawable.dot_active else R.drawable.dot_inactive)
        }
    }
    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish this activity so that pressing back from LoginActivity doesn't return here
    }

}
