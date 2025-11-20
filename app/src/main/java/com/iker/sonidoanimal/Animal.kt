package com.iker.sonidoanimal

data class Animal (
    val id: Int,
    val imageRes: Int,
    val name: String,
    val soundRes: Int,
    var isCorrect: Boolean = false,
    var isSelected: Boolean = false,
    var isDisabled: Boolean = false
                  )