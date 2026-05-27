package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.Template
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptLibraryScreen(viewModel: TemplateViewModel = viewModel()) {
    val templates by viewModel.allTemplates.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var templateToEdit by remember { mutableStateOf<Template?>(null) }
    var selectedCategory by remember { mutableStateOf("All") }

    Box(modifier = Modifier.fillMaxSize()) {
        GridBackground()
        
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "◈ SYSTEM :: PROMPT LIBRARY",
                style = MaterialTheme.typography.labelSmall,
                color = CosmicGreen,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category Filter
            ScrollableTabRow(
                selectedTabIndex = if (selectedCategory == "All") 0 else 1 + (categories.indexOf(selectedCategory).coerceAtLeast(0)),
                containerColor = Color.Transparent,
                divider = {},
                indicator = {},
                edgePadding = 0.dp
            ) {
                CategoryTab(selected = selectedCategory == "All", label = "All") { selectedCategory = "All" }
                categories.forEach { category ->
                    CategoryTab(selected = selectedCategory == category, label = category) { selectedCategory = category }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            val filteredTemplates = if (selectedCategory == "All") templates else templates.filter { it.category == selectedCategory }
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredTemplates) { template ->
                    TemplateItem(
                        template = template,
                        onEdit = { templateToEdit = it },
                        onDelete = { viewModel.deleteTemplate(it) }
                    )
                }
            }
        }
        
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = CosmicGreen,
            contentColor = CosmicBackground
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Template")
        }
    }
    
    if (showAddDialog) {
        EditTemplateDialog(
            onDismiss = { showAddDialog = false },
            onSave = { name, content, category ->
                viewModel.addTemplate(name, content, category)
                showAddDialog = false
            }
        )
    }
    
    if (templateToEdit != null) {
        EditTemplateDialog(
            initialTemplate = templateToEdit,
            onDismiss = { templateToEdit = null },
            onSave = { name, content, category ->
                viewModel.updateTemplate(templateToEdit!!.copy(name = name, content = content, category = category))
                templateToEdit = null
            }
        )
    }
}

@Composable
fun CategoryTab(selected: Boolean, label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) CosmicGreen else CosmicSurface)
            .border(1.dp, if (selected) CosmicGreen else CosmicBorder, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = if (selected) CosmicBackground else CosmicTextSecondary,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun TemplateItem(template: Template, onEdit: (Template) -> Unit, onDelete: (Template) -> Unit) {
    var showCopyDialog by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, CosmicBorder, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.Serif),
                    color = CosmicTextPrimary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onEdit(template) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = CosmicTextSecondary, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = { onDelete(template) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = CosmicRed.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                }
            }
            
            Text(
                text = template.content,
                style = MaterialTheme.typography.bodySmall,
                color = CosmicTextSecondary,
                maxLines = 3,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .background(CosmicBlue.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = template.category, style = MaterialTheme.typography.labelSmall, color = CosmicBlue)
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { 
                        if (template.content.contains("{{") && template.content.contains("}}")) {
                            showCopyDialog = true
                        } else {
                            clipboardManager.setText(AnnotatedString(template.content))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CosmicGreen, contentColor = CosmicBackground),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy", fontSize = 12.sp)
                }
            }
        }
    }
    
    if (showCopyDialog) {
        PlaceholderFillDialog(
            content = template.content,
            onDismiss = { showCopyDialog = false },
            onCopy = { filledContent ->
                clipboardManager.setText(AnnotatedString(filledContent))
                showCopyDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTemplateDialog(initialTemplate: Template? = null, onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf(initialTemplate?.name ?: "") }
    var content by remember { mutableStateOf(initialTemplate?.content ?: "") }
    var category by remember { mutableStateOf(initialTemplate?.category ?: "General") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CosmicSurface,
        title = { Text(if (initialTemplate == null) "New Template" else "Edit Template", color = CosmicGreen) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = CosmicBorder, 
                        focusedBorderColor = CosmicGreen, 
                        focusedTextColor = CosmicTextPrimary,
                        unfocusedTextColor = CosmicTextPrimary
                    )
                )
                OutlinedTextField(
                    value = category, onValueChange = { category = it },
                    label = { Text("Category") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = CosmicBorder, 
                        focusedBorderColor = CosmicGreen, 
                        focusedTextColor = CosmicTextPrimary,
                        unfocusedTextColor = CosmicTextPrimary
                    )
                )
                OutlinedTextField(
                    value = content, onValueChange = { content = it },
                    label = { Text("Content (Use {{var}} for placeholders)") },
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = CosmicBorder, 
                        focusedBorderColor = CosmicGreen, 
                        focusedTextColor = CosmicTextPrimary,
                        unfocusedTextColor = CosmicTextPrimary
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(name, content, category) }, enabled = name.isNotBlank() && content.isNotBlank()) {
                Text("Save", color = CosmicGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = CosmicTextSecondary) }
        }
    )
}

@Composable
fun PlaceholderFillDialog(content: String, onDismiss: () -> Unit, onCopy: (String) -> Unit) {
    val placeholders = remember(content) {
        val regex = Regex("\\{\\{(.*?)\\}\\}")
        regex.findAll(content).map { it.groupValues[1] }.distinct().toList()
    }
    
    val values = remember { mutableStateMapOf<String, String>().apply { placeholders.forEach { put(it, "") } } }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CosmicSurface,
        title = { Text("Fill Placeholders", color = CosmicGreen) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                placeholders.forEach { placeholder ->
                    OutlinedTextField(
                        value = values[placeholder] ?: "",
                        onValueChange = { values[placeholder] = it },
                        label = { Text(placeholder) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = CosmicBorder, 
                            focusedBorderColor = CosmicGreen, 
                            focusedTextColor = CosmicTextPrimary,
                            unfocusedTextColor = CosmicTextPrimary
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                var filledContent = content
                placeholders.forEach { placeholder ->
                    filledContent = filledContent.replace("{{$placeholder}}", values[placeholder] ?: "")
                }
                onCopy(filledContent)
            }) {
                Text("Copy Filled", color = CosmicGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = CosmicTextSecondary) }
        }
    )
}
