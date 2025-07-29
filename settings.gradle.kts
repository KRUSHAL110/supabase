pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        // ✅ Add Linphone's Maven repository
        maven {
            url = uri("https://download.linphone.org/releases/maven_repository/")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // ✅ Add Linphone Maven repository
        maven {
            url = uri("https://download.linphone.org/releases/maven_repository/")
        }

        // ✅ Add flatDir for local .aar files (optional)
        flatDir {
            dirs("app/libs")
        }
    }
}

rootProject.name = "supabase"
include(":app")
