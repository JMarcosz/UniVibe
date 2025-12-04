package com.example.univibe.domain.use_case.event

import com.example.univibe.domain.model.Event
import java.util.*
import javax.inject.Inject

/**
 * Caso de uso para filtrar eventos por fecha.
 */
class FilterEventsByDateUseCase @Inject constructor() {

    operator fun invoke(events: List<Event>, filter: DateFilter): List<Event> {
        return when (filter) {
            DateFilter.ALL -> events
            DateFilter.TODAY -> events.filter { isToday(it.creationDate.toDate()) }
            DateFilter.TOMORROW -> events.filter { isTomorrow(it.creationDate.toDate()) }
            DateFilter.THIS_WEEK -> events.filter { isThisWeek(it.creationDate.toDate()) }
            DateFilter.NEXT_MONTH -> events.filter { isNextMonth(it.creationDate.toDate()) }
        }
    }

    private fun isToday(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val calendarDate = Calendar.getInstance()
        calendarDate.time = date

        return calendar.get(Calendar.YEAR) == calendarDate.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == calendarDate.get(Calendar.DAY_OF_YEAR)
    }

    private fun isTomorrow(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 1)

        val calendarDate = Calendar.getInstance()
        calendarDate.time = date

        return calendar.get(Calendar.YEAR) == calendarDate.get(Calendar.YEAR) &&
                calendar.get(Calendar.DAY_OF_YEAR) == calendarDate.get(Calendar.DAY_OF_YEAR)
    }

    private fun isThisWeek(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        val currentWeek = calendar.get(Calendar.WEEK_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        val calendarDate = Calendar.getInstance()
        calendarDate.time = date
        val dateWeek = calendarDate.get(Calendar.WEEK_OF_YEAR)
        val dateYear = calendarDate.get(Calendar.YEAR)

        return currentYear == dateYear && currentWeek == dateWeek
    }

    private fun isNextMonth(date: Date): Boolean {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, 1)
        val nextMonth = calendar.get(Calendar.MONTH)
        val nextYear = calendar.get(Calendar.YEAR)

        val calendarDate = Calendar.getInstance()
        calendarDate.time = date
        val dateMonth = calendarDate.get(Calendar.MONTH)
        val dateYear = calendarDate.get(Calendar.YEAR)

        return nextYear == dateYear && nextMonth == dateMonth
    }
}

enum class DateFilter {
    ALL,
    TODAY,
    TOMORROW,
    THIS_WEEK,
    NEXT_MONTH
}

