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
        gradlePluginPortal() // Manteniendo Gradle Plugin Portal
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()       // Repositorio de Google
        mavenCentral() // Repositorio Maven Central
        maven("https://jitpack.io") // Añadido el repositorio JitPack
    }
}

rootProject.name = "ConsumoEnergetico"
include(":app")
