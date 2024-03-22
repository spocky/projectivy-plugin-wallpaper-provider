package tv.projectivy.plugin.wallpaperprovider.sample

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken

object PreferencesManager {
    private const val IMAGE_URL_KEY = "image_url_key"

    lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun set(key: String, value: Any?) =
        when (value) {
            is String? -> preferences.edit { it.putString(key, value) }
            is Int -> preferences.edit { it.putInt(key, value) }
            is Boolean -> preferences.edit { it.putBoolean(key, value) }
            is Float -> preferences.edit { it.putFloat(key, value) }
            is Long -> preferences.edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }

    inline operator fun <reified T : Any> get(
        key: String,
        defaultValue: T? = null
    ): T =
        when (T::class) {
            String::class -> preferences.getString(key, defaultValue as String? ?: "") as T
            Int::class -> preferences.getInt(key, defaultValue as? Int ?: -1) as T
            Boolean::class -> preferences.getBoolean(key, defaultValue as? Boolean ?: false) as T
            Float::class -> preferences.getFloat(key, defaultValue as? Float ?: -1f) as T
            Long::class -> preferences.getLong(key, defaultValue as? Long ?: -1) as T
            else -> throw UnsupportedOperationException("Not yet implemented")
        }

    var imageUrl: String
        get() = PreferencesManager[IMAGE_URL_KEY, "https://images.pexels.com/photos/462162/pexels-photo-462162.jpeg"]
        set(value) { PreferencesManager[IMAGE_URL_KEY]=value }

    fun export(): String {
        return Gson().toJson(preferences.all)
    }

    fun import(prefs: String): Boolean {
        val gson = GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .create()

        try {
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val map = gson.fromJson<Map<String, Any>>(prefs, type)
            val editor = preferences.edit()
            editor.clear()
            map.forEach { (key: String, value: Any) ->
                when(value) {
                    is Boolean -> editor.putBoolean(key, value)
                    is Double -> editor.putFloat(key, value.toFloat())
                    is Float -> editor.putFloat(key, value)
                    is Int -> editor.putInt(key, value)
                    is Long -> editor.putInt(key, value.toInt())
                    is String -> editor.putString(key, value)
                    is ArrayList<*> -> editor.putStringSet(key, java.util.HashSet(value as java.util.ArrayList<String>))
                    is Set<*> -> editor.putStringSet(key, value as Set<String>)
                }
            }
            editor.apply()
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }
        return true
    }
}