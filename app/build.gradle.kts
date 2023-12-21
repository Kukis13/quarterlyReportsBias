plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
	implementation("com.github.mizosoft.methanol:methanol:1.7.0")
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter("5.10.0")
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

application {
    mainClass.set("lukaszja.stockdata.App")
}
