import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContentProviderCompat.requireContext

object SharedPreferencesManager {
    private const val PREFS_NAME = "AppPrefs"
    private const val ACCESS_TOKEN = "accessToken"

    fun saveAccessToken(context: Context, token: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN, token)
        editor.apply()
        println("Token saved: $token")
    }

    fun getAccessToken(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val token = sharedPreferences.getString(ACCESS_TOKEN, null)
        println("Retrieved token: $token")
        return token
    }

    fun printAllSharedPreferences(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Retrieve all entries from SharedPreferences
        val allEntries = sharedPreferences.all

        // Iterate and print each key-value pair
        for ((key, value) in allEntries) {
            println("Key: $key, Value: $value")
        }
    }

}
