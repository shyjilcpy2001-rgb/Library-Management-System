# Library Management System - Deployment Guide

## Prerequisites
- Java Development Kit (JDK) 8 or higher installed
- Command-line access (Windows PowerShell, Command Prompt, or Terminal)
- (Optional) Git for version control

## Compilation Steps
1. **Clean Previous Builds**
   - Delete the old `bin` directory to remove previous `.class` files:
     - Windows: `rmdir /s /q bin`
2. **Create bin Directory**
   - `mkdir bin`
3. **Compile Source Files**
   - Compile all Java files and output to `bin`:
     - `javac -d bin src/model/*.java src/service/*.java src/utility/*.java src/*.java`

## JAR Creation
1. **Create Manifest File**
   - At the project root, create `manifest.txt` with the following content:
     - `Main-Class: Main`
   - Ensure there is an empty line at the end of the file.
2. **Package as Executable JAR**
   - Run:
     - `jar cfm LibraryManagementSystem.jar manifest.txt -C bin .`

## Execution Steps
- Run the application using:
  - `java -jar LibraryManagementSystem.jar`
- Follow the on-screen menu to interact with the system.

## Troubleshooting
- **javac/jar not found:**
  - Ensure your JDK `bin` directory is in your system `PATH`.
- **Class not found or NoClassDefFoundError:**
  - Make sure all source files are compiled and the manifest is correct.
- **JAR does not run:**
  - Check that the manifest file has the correct `Main-Class` and an empty line at the end.
- **Persistence issues:**
  - Ensure you have write permissions in the project directory for CSV files.
- **Admin features not working:**
  - Use `admin` as the username and `abc@123` as the password.

---

For further help, consult the project documentation or contact the maintainer.
