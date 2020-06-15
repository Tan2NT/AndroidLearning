package com.tantnt.android.runstatistic.ui.recycler_view_item

import com.tantnt.android.runstatistic.R
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.header_title.*


/**
 * Header Item
 */
class HeaderItem(
    val title: String,
    val description : String
) : Item(), ExpandableItem {

    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply {
            header_title.text = title
            header_description.text = description
            header_arrow.setImageResource(getRotateIconResId())

            header_arrow.setOnClickListener {
                expandableGroup.onToggleExpanded()
                header_arrow.setImageResource(getRotateIconResId())
            }
        }
    }

    override fun getLayout(): Int {
        return R.layout.header_title
    }

    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }

    private fun getRotateIconResId(): Int {
        if (expandableGroup.isExpanded)
            return R.drawable.ic_keyboard_arrow_up_black_24dp
        else
            return R.drawable.ic_keyboard_arrow_down_black_24dp
    }
}