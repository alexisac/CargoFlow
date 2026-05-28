package com.example.officeapp.models.user

data class GetAllUsersResponse(
    val users: List<UserSummary>,
    val pageNumber: Int,
    val pageSize: Int,
    val lastPage: Boolean
)