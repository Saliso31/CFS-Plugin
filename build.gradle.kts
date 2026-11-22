import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.intellij.platform")
    id("org.jetbrains.changelog")
    id("org.jetbrains.intellij.platform.grammarkit")
}

dependencies {
    testImplementation("junit:junit:4.13.2")

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        intellijIdea("2026.2.5")
        testFramework(TestFrameworkType.Platform)
    }
}

sourceSets {
    main {
        java.srcDir("src/main/gen")
    }
}

tasks.generateLexer {
    sourceFile.set(file("src/main/grammar/Cfs.flex"))
    targetRootOutputDir.set(file("src/main/gen"))
}

tasks.generateParser {
    sourceFile.set(file("src/main/grammar/Cfs.bnf"))
    targetRootOutputDir.set(file("src/main/gen"))
    pathToParser.set("com/saliso/cfs/parser/CfsParser.java")
    pathToPsiRoot.set("com/saliso/cfs/psi")
    purgeOldFiles.set(true)
}

tasks.compileJava { dependsOn(tasks.generateLexer, tasks.generateParser) }
tasks.compileKotlin { dependsOn(tasks.generateLexer, tasks.generateParser) }

sourceSets["main"].java.srcDir("src/main/gen")