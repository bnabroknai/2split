package com.example.data

import kotlinx.coroutines.flow.Flow

class TemplateRepository(private val templateDao: TemplateDao) {
    val allTemplates: Flow<List<Template>> = templateDao.getAllTemplates()
    val categories: Flow<List<String>> = templateDao.getCategories()
    val allRelays: Flow<List<Relay>> = templateDao.getAllRelays()

    suspend fun insert(template: Template) = templateDao.insertTemplate(template)
    suspend fun update(template: Template) = templateDao.updateTemplate(template)
    suspend fun delete(template: Template) = templateDao.deleteTemplate(template)

    suspend fun insertRelay(relay: Relay) = templateDao.insertRelay(relay)
    suspend fun clearRelays() = templateDao.clearRelays()
}
