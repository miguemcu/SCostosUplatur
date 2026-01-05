package com.example.fincostos.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DateUtils {
    private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    
    fun todayAsString(): String {
        return LocalDate.now().format(formatter)
    }
    
    fun parseDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, formatter)
    }
    
    fun formatDate(date: LocalDate): String {
        return date.format(formatter)
    }
}
