Structured Card Query Language (SCQL) Interpreter
=================================================

Structured Card Query Language Interpreter is the project, that translates an SCQL command into 
Application Protocol Data Unit (APDU) call, as defined by ISO/IEC 7816-7 Interindustry commands for Structured Card Query Language (SCQL).
Additionally, generated APDUs are sent to the Java Card Simulator via Socket.

Related projects
----------------
[scql-applet](https://github.com/PopularTracy/scql-applet) - is the project implementation of a database on a Java Smart Card Applet, defined by ISO/IEC 7816-7.

Getting Started
--------------
1. Make sure, that you have specified `JC_HOME_TOOLS` and `JC_HOME_SIMULATOR` variables, installed from Oracle's Java Card Development Kit Simulator and Java Card Development Kit Tools;

2. SCQL Interpreter project requires minimum Java 11+ version. Additionally, the project uses Gradle 8.2 with Kotlin DSL;

3. Clone repository and build the project by Gradle command.
    ```shell
    ./gradlew build
    ```

Project usage
-------------
1. Inside the root path `src/main/resources/` modify the `command.scql` with custom SCQL commands and run the project.
2. In the same folder, `command_apdu.script` will be generated.

Java Card Communication
-----------------------
After the APDU script was generated, the communication process establishes between a Java Card Simulator and the Interpreter.
All communication is done via `java.net.Socket` and Java Card `com.sun.javacard.apduio` library.

Communication is going through `localhost:9025`.

Applet AID: 
```txt
FFFFFFFFFF/0102
```

Select Applet APDU command:
```apdu
0x00 0xA4 0x04 0x00 0x07 0xFF 0xFF 0xFF 0xFF 0xFF 0x01 0x02 0x7F
```

