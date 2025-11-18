plugins {
    id("java")
    id("application")
}

group = "src.main.java.com"
version = "1.0-SNAPSHOT"

// Configuration Java
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {
    // JUnit 5 (Jupiter)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")

    // Mockito pour les mocks (optionnel mais utile)
    testImplementation("org.mockito:mockito-core:5.5.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.5.0")

    // AssertJ pour des assertions plus lisibles (optionnel)
    testImplementation("org.assertj:assertj-core:3.24.2")
}

// ✅ Configuration de l'application (syntaxe correcte pour Kotlin DSL)
tasks.named<JavaExec>("run") {
    mainClass.set("src.main.java.com.Main")
}

tasks.test {
    useJUnitPlatform()

    // Configuration des logs de test
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = false
        showStackTraces = true
    }

    // Options JVM pour les tests
    jvmArgs("-Xmx1024m")
}

// Configuration des répertoires source
sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/java"))
        }
        resources {
            setSrcDirs(listOf("src/main/resources"))
        }
    }
    test {
        java {
            setSrcDirs(listOf("src/test/java"))
        }
        resources {
            setSrcDirs(listOf("src/test/resources"))
        }
    }
}

// Tâche personnalisée pour exécuter seulement les tests d'une classe
tasks.register<Test>("testClass") {
    useJUnitPlatform()

    // Utilisation: ./gradlew testClass -PtestClass=BaliseTest
    if (project.hasProperty("testClass")) {
        val testClass = project.property("testClass") as String
        filter {
            includeTestsMatching("*.$testClass")
        }
    }
}