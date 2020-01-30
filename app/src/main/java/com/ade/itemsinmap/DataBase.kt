package com.ade.itemsinmap
import androidx.room.*

@Entity(tableName = "items")
data class Items(
    @PrimaryKey(autoGenerate = false) val uid: Int,
    @ColumnInfo(name = "service") val service: String,
    @ColumnInfo(name = "lat") val lat: Double,
    @ColumnInfo(name = "lng") val lng: Double,
    @ColumnInfo(name = "visible") val visible:Boolean

)
@Dao
interface UserDao {
    @Query("SELECT * FROM items")
    fun getAll(): List<Items>

    @Query("SELECT * FROM items WHERE uid = (:id)")
    fun getAllbyId(id: Int): Items

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg items: Items)

    @Query("UPDATE items set visible = (:visible) WHERE service = (:service)")
    fun updateVisiblebyService(service: String, visible: Boolean)
}

@Database(entities = [Items::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}