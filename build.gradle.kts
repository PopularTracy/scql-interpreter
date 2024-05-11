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

tasks.test {
    useJUnitPlatform()
}