package com.mayankwadhwa.github_client.repository

sealed class Resource<T>(
        var data: T? = null,
        val message: String? = null
) {
    class Success<T>(data: T?) : Resource<T>(data) {
        override fun equals(other: Any?): Boolean {
            if (this.data == (other as? Success<T>)?.data) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    class Loading<T>(data: T? = null) : Resource<T>(data) {
        override fun equals(other: Any?): Boolean {
            if (this.data == (other as? Loading<T>)?.data) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message) {
        override fun equals(other: Any?): Boolean {
            if (this.message == (other as? Error<T>)?.message) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }
}