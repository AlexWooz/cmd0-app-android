package com.cmd0app.cmd0AppAndroid.auth

import android.util.Log
import at.favre.lib.crypto.bcrypt.BCrypt
import com.cmd0app.cmd0AppAndroid.dbOps.DBOps
import org.bson.Document
import org.bson.codecs.ObjectIdGenerator
import org.bson.types.ObjectId

class AuthManager {

    var email = ""
    var name = ""
    var login = false

    fun login(email:String, password:String) {
        val dbOps = DBOps()
        val query = "{email: \"$email\"}"
        val user = dbOps.findDocument(query, "users")
        if (user != null) {
            login = BCrypt.verifyer()
                .verify(password.toCharArray(), user.getString("password").toCharArray()).verified
            if (login) {
                Log.i("AUTH","Login successful")
                this.email = email
                this.name = user.getString("name")
            } else {
                Log.e("AUTH_ERR","Login failed")
            }
        } else {
            Log.w("DB_WARN","User not found")
        }
    }

    fun register(name:String, email: String, password: String) {
        val name = name
        val email = email
        val hash = BCrypt.with(BCrypt.Version.VERSION_2B).hashToString(12, password.toCharArray())
        val document = "{name: \"$name\", email: \"$email\", password: \"$hash\"}"
        val dbOps = DBOps()
        dbOps.insertDocument(Document.parse(document), "users")
        Log.i("AUTH","User data sent to DB")
        login(email, password)
        if (login) {
            Log.i("AUTH","User logged in")
        } else {
            Log.e("AUTH_ERR","Failed to log in user")
        }
    }

    fun logout() {
        this.email = ""
        this.name = ""
    }
}