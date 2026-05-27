package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "templates")
data class Template(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val content: String,
    val category: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "relays")
data class Relay(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface TemplateDao {
    @Query("SELECT * FROM templates ORDER BY createdAt DESC")
    fun getAllTemplates(): Flow<List<Template>>

    @Query("SELECT DISTINCT category FROM templates")
    fun getCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: Template)

    @Update
    suspend fun updateTemplate(template: Template)

    @Delete
    suspend fun deleteTemplate(template: Template)

    @Query("SELECT * FROM relays ORDER BY timestamp DESC LIMIT 50")
    fun getAllRelays(): Flow<List<Relay>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRelay(relay: Relay)

    @Query("DELETE FROM relays")
    suspend fun clearRelays()
}

@Database(entities = [Template::class, Relay::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun templateDao(): TemplateDao
}
