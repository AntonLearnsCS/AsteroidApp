package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.asteroidComposed.R
import com.squareup.picasso.Picasso

@BindingAdapter("statusAdapter")
fun statusAdapter(imageView: ImageView, isHazardous : Boolean)
{
    if(isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    }
    else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}
@BindingAdapter("picassoAdapter")
fun picassoAdapter(imageView: ImageView, pictureOfDay: PictureOfDay?)
{
    if (pictureOfDay != null && pictureOfDay.mediaType.equals("image"))
        //removed 'with' in Picasso.with(context) as it is depracated: https://stackoverflow.com/questions/50201476/kotlin-unresolved-reference-with
    Picasso.get().load(pictureOfDay.url).into(imageView)
    else
        Picasso.get().load(R.drawable.ic_help_circle).into(imageView)
}

/*@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(data)
}*/


@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}
