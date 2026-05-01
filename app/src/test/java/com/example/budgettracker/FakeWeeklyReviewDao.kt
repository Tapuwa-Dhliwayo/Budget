package com.example.budgettracker

import com.example.budgettracker.data.dao.WeeklyReviewDao
import com.example.budgettracker.data.entity.WeeklyReviewEntity

class FakeWeeklyReviewDao : WeeklyReviewDao {
    private val reviews = mutableMapOf<String, WeeklyReviewEntity>()

    override suspend fun getReview(weekStartDate: String): WeeklyReviewEntity? {
        return reviews[weekStartDate]
    }

    override suspend fun upsertReview(review: WeeklyReviewEntity) {
        reviews[review.weekStartDate] = review
    }
}
