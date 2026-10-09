package edu.virginia.cs4720.uva_bucket_list
import android.os.Parcelable

import android.R


data class BucketItem(
    val id: Int,
    val item_name: String,
    val due_date: String,
    val completed: Boolean,
    val completed_date: String?
);


