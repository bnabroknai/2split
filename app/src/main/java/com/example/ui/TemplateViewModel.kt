package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.Relay
import com.example.data.Template
import com.example.data.TemplateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TemplateViewModel(application: Application) : AndroidViewModel(application) {
    private val database = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "template_database"
    )
    .fallbackToDestructiveMigration() // Simple for development
    .build()
    
    private val repository = TemplateRepository(database.templateDao())
    
    val allTemplates: StateFlow<List<Template>> = repository.allTemplates
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    val categories: StateFlow<List<String>> = repository.categories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allRelays: StateFlow<List<Relay>> = repository.allRelays
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addTemplate(name: String, content: String, category: String) {
        viewModelScope.launch {
            repository.insert(Template(name = name, content = content, category = category))
        }
    }

    fun updateTemplate(template: Template) {
        viewModelScope.launch {
            repository.update(template)
        }
    }

    fun deleteTemplate(template: Template) {
        viewModelScope.launch {
            repository.delete(template)
        }
    }

    fun addRelay(content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.insertRelay(Relay(content = content))
        }
    }

    fun clearRelays() {
        viewModelScope.launch {
            repository.clearRelays()
        }
    }
}
