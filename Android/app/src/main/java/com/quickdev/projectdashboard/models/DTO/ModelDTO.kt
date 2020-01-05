package com.quickdev.projectdashboard.models.DTO

interface ModelDTO<T> {
    fun toModel(): T
}
