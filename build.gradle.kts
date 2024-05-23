plugins {
    id("java")
    id("idea")
    id("eclipse")
}

group = "com.kpi.scql"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

dependencies {

    val jcTools = System.getenv("JC_HOME_TOOLS") + "/lib/tools.jar"
    val jcSimulator = System.getenv("JC_HOME_SIMULATOR") + "/lib/api_classic.jar"

    implementation(files(jcTools))
    implementation(files(jcSimulator))

    // https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
    implementation("org.apache.commons:commons-lang3:3.14.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.javadoc {
    // Set the source for Javadoc generation
    source = sourceSets.main.get().allJava

    // Set the classpath for the Javadoc generation
    classpath = sourceSets.main.get().compileClasspath

    // Configure additional Javadoc options here if needed
    options {
        // Set the title of the generated documentation
        title = "SCQL Interpreter"

        // Configure other options as needed
        encoding = "UTF-8"
        memberLevel = JavadocMemberLevel.PUBLIC
        // Add other Javadoc options if required
    }
}

// Add a custom task to generate the Javadoc jar
tasks.register<Jar>("javadocJar") {
    dependsOn(tasks.javadoc)
    from(tasks.javadoc.get().destinationDir)
    archiveClassifier.set("javadoc")
}

tasks.test {
    useJUnitPlatform()
}