package data.local

import androidx.datastore.preferences.core.preferencesKey
val deviceUUID = preferencesKey<String>("device-uuid")
val authKeyId = preferencesKey<String>("authKey")
