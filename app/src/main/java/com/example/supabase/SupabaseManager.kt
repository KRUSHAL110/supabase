package com.example.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseManager {
    val client = createSupabaseClient(
        supabaseUrl = "https://nfwcoqkaqezackpvxlev.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im5md2NvcWthcWV6YWNrcHZ4bGV2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTM0MjM3MzIsImV4cCI6MjA2ODk5OTczMn0.eSlHgKqWWlm6IAwOmwGhviocGN9LX7M7hnejM8H6oTE"
    ) {
        install(Auth)
        install(Postgrest)
    }
}
