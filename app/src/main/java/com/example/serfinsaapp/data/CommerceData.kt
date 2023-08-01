package com.example.serfinsaapp.data

object CommerceData {
    data class CommerceItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}