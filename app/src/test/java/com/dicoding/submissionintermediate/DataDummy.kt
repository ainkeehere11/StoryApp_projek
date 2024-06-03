package com.dicoding.submissionintermediate

import com.dicoding.submissionintermediate.viewmodel.Story

object DataDummy {
    fun generateDummyStory(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val story = Story(
                "date $i",
                "description $i",
                "$i",
                i.toDouble(),
                i.toDouble(),
                "name $i",
                "url $i"
            )
            items.add(story)
        }
        return items
    }
}
