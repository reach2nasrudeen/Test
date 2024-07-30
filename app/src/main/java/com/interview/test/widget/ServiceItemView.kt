package com.interview.test.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.interview.test.R

class ServiceItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val iconImageView: AppCompatImageView?
    private val textView: AppCompatTextView?
    private val cardView: CardView?

    init {
        LayoutInflater.from(context).inflate(R.layout.item_service, this, true)
        iconImageView = findViewById(R.id.image)
        textView = findViewById(R.id.text)
        cardView = findViewById(R.id.card)

        // Custom attributes can be retrieved here if you need
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomItemView, 0, 0)
            val text = typedArray.getString(R.styleable.CustomItemView_text)
            val icon = typedArray.getDrawable(R.styleable.CustomItemView_icon)

            val cardBg = typedArray.getColor(
                R.styleable.CustomItemView_card_bg,
                ContextCompat.getColor(getContext(), android.R.color.transparent)
            )

            textView?.text = text
            iconImageView?.setImageDrawable(icon)
            cardView?.setCardBackgroundColor(cardBg)
            typedArray.recycle()
        }
    }
}