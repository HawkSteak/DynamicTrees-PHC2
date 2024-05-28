import com.google.gson.Gson
import com.google.gson.JsonObject
import com.matthewprenger.cursegradle.*
import java.io.InputStreamReader
import java.time.Instant
import java.time.format.DateTimeFormatter

fun property(key: String) = project.findProperty(key).toString()

plugins {
    id("java")
    id("net.minecraftforge.gradle")
    id("org.parchmentmc.librarian.forgegradle")
    id("idea")
    id("maven-publish")
    id("com.matthewprenger.cursegradle") version "1.4.0"
}

repositories {
    maven("https://ldtteam.jfrog.io/ldtteam/modding/")
    // TEMP FIX: TEHNUT MAVEN IS DOWN
    //maven("https://maven.tehnut.info")
    maven("https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    maven("https://harleyoconnor.com/maven")
    maven("https://squiddev.cc/maven/")
}

val modName = property("modName")
val modId = property("modId")
val modVersion = property("modVersion")
val mcVersion = property("mcVersion")

version = "$mcVersion-$modVersion"
group = property("group")

minecraft {
    mappings(property("mappingsChannel"), property("mappingsVersion"))

    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            applyDefaultConfiguration()

            if (project.hasProperty("mcUuid")) {
                args("--uuid", property("mcUuid"))
            }
            if (project.hasProperty("mcUsername")) {
                args("--username", property("mcUsername"))
            }
            if (project.hasProperty("mcAccessToken")) {
                args("--accessToken", property("mcAccessToken"))
            }
        }

        create("server") {
            applyDefaultConfiguration()
        }

        create("data") {
            applyDefaultConfiguration()

            args(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/"),
                "--existing", file("src/main/resources"),
                "--existing-mod", "dynamictrees",
                "--existing-mod", "pamhc2trees"
            )
        }
    }
}

fun net.minecraftforge.gradle.common.util.RunConfig.applyDefaultConfiguration(runDirectory: String = "run") {
    workingDirectory = file(runDirectory).absolutePath

    property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
    property("forge.logging.console.level", "debug")

    property("mixin.env.remapRefMap", "true")
    property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")

    mods {
        create(modId) {
            source(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
}

dependencies {
    // Not sure if we need this one, what is a "forge" anyway?
    minecraft("net.minecraftforge:forge:1.20.1-47.2.0")

    // Compile PHC and DT, of course.
    implementation(fg.deobf("curse.maven:pams-harvestcraft-2-trees-365460:4625518"))
    implementation(fg.deobf("curse.maven:dynamictrees-252818:5065701"))

    // Compile Jade, but don't include in runtime.
    compileOnly(fg.deobf("curse.maven:jade-324717:4986594"))

    // DT+ is optional, but it's implemented as there is access to its classes and needs to be compiled.
    //Uncomment/add the dependency for DynamicTreesPlus before building
    //implementation(fg.deobf("com.ferreusveritas.dynamictreesplus:DynamicTreesPlus-$mcVersion:${property("dynamicTreesPlusVersion")}"))

    /////////////////////////////////////////
    /// Runtime Dependencies (optional)
    /////////////////////////////////////////

    // At runtime, use the full Jade mod.
    runtimeOnly(fg.deobf("curse.maven:jade-324717:4986594"))

    // At runtime, use the full JEI mod.
    // TEMP FIX: TEHNUT MAVEN IS DOWN
    compileOnly(fg.deobf("mezz.jei:jei-$mcVersion-forge-api:${property("jeiVersion")}"))
    compileOnly(fg.deobf("mezz.jei:jei-$mcVersion-common-api:${property("jeiVersion")}"))
    runtimeOnly(fg.deobf("mezz.jei:jei-$mcVersion-forge:${property("jeiVersion")}"))

    // At runtime, use CC for creating growth chambers.
    runtimeOnly(fg.deobf("cc.tweaked:cc-tweaked-$mcVersion-core:${property("ccVersion")}"))
    runtimeOnly(fg.deobf("cc.tweaked:cc-tweaked-$mcVersion-forge:${property("ccVersion")}"))

    // At runtime, get rid of experimental settings warning screen.
    runtimeOnly(fg.deobf("curse.maven:ShutUpExperimentalSettings-407174:3759881"))

    // At runtime use serene seasons to test seasonal mechanics
    runtimeOnly(fg.deobf("curse.maven:SereneSeasons-291874:4761603"))

    // At runtime, use suggestion provider fix mod.
    runtimeOnly(fg.deobf("curse.maven:suggestion-provider-fix-469647:4591193"))

    // If needed, include Cyanide mod to get more info about datapack errors
    runtimeOnly(fg.deobf("curse.maven:Cyanide-541676:4824162"))

}

tasks.jar {
    manifest.attributes(
        "Specification-Title" to project.name,
        "Specification-Vendor" to "Max Hyper",
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to project.version,
        "Implementation-Vendor" to "Max Hyper",
        "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    )

    archiveBaseName.set(modName)
    finalizedBy("reobfJar")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

fun readChangelog(): String? {
    val versionInfoFile = file("version_info.json")
    val jsonObject = Gson().fromJson(InputStreamReader(versionInfoFile.inputStream()), JsonObject::class.java)
    return jsonObject
        .get(mcVersion)?.asJsonObject
        ?.get(project.version.toString())?.asString
}

fun enablePublishing() =
    project.hasProperty("curseApiKey") && project.hasProperty("curseFileType") && project.hasProperty("projectId")

tasks.withType(CurseUploadTask::class.java) {
    onlyIf {
        enablePublishing()
    }
}

curseforge {
    if (project.hasProperty("curseApiKey") && project.hasProperty("curseFileType")) {
        apiKey = property("curseApiKey")

        project {
            id = property("curseProjectId")

            addGameVersion(mcVersion)

            changelog = file("build/changelog.txt")
            changelogType = "markdown"
            releaseType = property("curseFileType")

            addArtifact(tasks.findByName("sourcesJar"))

            mainArtifact(tasks.findByName("jar")) {
                relations {
                    requiredDependency("dynamictrees")
                    requiredDependency("pams-harvestcraft-2-trees")
                }
            }
        }
    } else {
        project.logger.log(LogLevel.WARN, "API Key and file type for CurseForge not detected; uploading will be disabled.")
    }
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "$modName-$mcVersion"
            version = modVersion

            from(components["java"])

            pom {
                name.set(modName)
                url.set("https://github.com/supermassimo/$modName")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://mit-license.org")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/supermassimo/$modName.git")
                    developerConnection.set("scm:git:ssh://github.com/supermassimo/$modName.git")
                    url.set("https://github.com/supermassimo/$modName")
                }
            }

            pom.withXml {
                val element = asElement()

                // Clear dependencies.
                for (i in 0 until element.childNodes.length) {
                    val node = element.childNodes.item(i)
                    if (node?.nodeName == "dependencies") {
                        element.removeChild(node)
                    }
                }
            }
        }
    }
    repositories {
        maven("file:///${project.projectDir}/mcmodsrepo")
        if (hasProperty("harleyOConnorMavenUsername") && hasProperty("harleyOConnorMavenPassword")) {
            maven("https://harleyoconnor.com/maven") {
                name = "HarleyOConnor"
                credentials {
                    username = property("harleyOConnorMavenUsername")
                    password = property("harleyOConnorMavenPassword")
                }
            }
        } else {
            logger.log(LogLevel.WARN, "Credentials for maven not detected; it will be disabled.")
        }
    }
}

// Extensions to make CurseGradle extension slightly neater.

fun CurseExtension.project(action: CurseProject.() -> Unit) {
    this.project(closureOf(action))
}

fun CurseProject.mainArtifact(artifact: Task?, action: CurseArtifact.() -> Unit) {
    this.mainArtifact(artifact, closureOf(action))
}

fun CurseArtifact.relations(action: CurseRelation.() -> Unit) {
    this.relations(closureOf(action))
}