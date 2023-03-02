package data.local.datastore

import androidx.datastore.preferences.core.preferencesKey
val deviceUUID = preferencesKey<String>("device-uuid")
val authKeyId = preferencesKey<String>("authKey")
