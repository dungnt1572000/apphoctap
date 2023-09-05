package com.example.apphoctap.repository

import com.example.apphoctap.model.UserDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val database = Firebase.firestore

    fun getCurrentUser() = auth.currentUser

    fun signIn(userName: String, password: String) =
        auth.signInWithEmailAndPassword(userName, password)

    fun signOut() = auth.signOut()

    fun deleteUser() = auth.currentUser?.delete()

    fun updatePassword(newPassword: String) =
        auth.currentUser?.updatePassword(newPassword)

    fun signUp(userName: String, password: String) =
        auth.createUserWithEmailAndPassword(userName, password)

    fun addUserRank(userId: String, userDetail: UserDetail) =
        database.collection("rank").document(userId).set(
            userDetail
        )

    fun getUserDetail(userId: String) = database.collection("rank").document(userId).get()
}