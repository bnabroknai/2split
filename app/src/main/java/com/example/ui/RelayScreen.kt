package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.*

@Composable
fun RelayScreen(viewModel: TemplateViewModel = viewModel()) {
    val relays by viewModel.allRelays.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current
    
    var showSaveAsTemplateDialog by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GridBackground()
        
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "◈ SYSTEM :: UNIVERSAL RELAY",
                        style = MaterialTheme.typography.labelSmall,
                        color = CosmicGreen,
                        letterSpacing = 4.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Clipboard Sync Buffer",
                        style = MaterialTheme.typography.titleMedium,
                        color = CosmicTextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                IconButton(onClick = { viewModel.clearRelays() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear All", tint = CosmicRed)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = { 
                    clipboardManager.getText()?.let { 
                        viewModel.addRelay(it.text) 
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CosmicSurface, contentColor = CosmicGreen),
                border = androidx.compose.foundation.BorderStroke(1.dp, CosmicGreen.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("IMPORT FROM CLIPBOARD", fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(relays) { relay ->
                    RelayItem(
                        content = relay.content,
                        onCopyToClipboard = { clipboardManager.setText(AnnotatedString(it)) },
                        onSaveAsTemplate = { showSaveAsTemplateDialog = it }
                    )
                }
            }
        }
    }
    
    if (showSaveAsTemplateDialog != null) {
        EditTemplateDialog(
            initialTemplate = com.example.data.Template(name = "", content = showSaveAsTemplateDialog!!, category = "General"),
            onDismiss = { showSaveAsTemplateDialog = null },
            onSave = { name, content, category ->
                viewModel.addTemplate(name, content, category)
                showSaveAsTemplateDialog = null
            }
        )
    }
}

@Composable
fun RelayItem(content: String, onCopyToClipboard: (String) -> Unit, onSaveAsTemplate: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, CosmicBorder, RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = CosmicTextAction,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = { onCopyToClipboard(content) },
                    colors = ButtonDefaults.textButtonColors(contentColor = CosmicGreen)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy", fontSize = 12.sp)
                }
                
                TextButton(
                    onClick = { onSaveAsTemplate(content) },
                    colors = ButtonDefaults.textButtonColors(contentColor = CosmicBlue)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save Template", fontSize = 12.sp)
                }
            }
        }
    }
}
