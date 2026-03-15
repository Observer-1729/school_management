package com.example.schoolmanagement

fun getSubjectIcon(subject: String): Int {
    return when (subject) {

        "Maths" -> R.drawable.maths
        "English" -> R.drawable.english
        "Hindi" -> R.drawable.hindi
        "Sanskrit" -> R.drawable.sanskrit
        "Science" -> R.drawable.science
        "Physics" -> R.drawable.physics
        "Chemistry" -> R.drawable.chemistry
        "Biology" -> R.drawable.biology
        "Computer" -> R.drawable.computer
        "History" -> R.drawable.history
        "Geography" -> R.drawable.geography
        "Economics" -> R.drawable.economics
        "Civics" -> R.drawable.civics
        "Moral Values" -> R.drawable.moral_values
        "General Knowledge" -> R.drawable.general_knowledge

        else -> R.drawable.general_knowledge
    }
}