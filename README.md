# 🚗 🔌 Electric charging points 

The goal of this project is to determine the minimum number of charging points
for an urban community. The project follows these following rules
- A city must have or connected with a city who have a charging point.
- The number of charging points try to be the lowest possible (our algorithm
uses a heuristic)

## 📝 Functionalities

- Resolve manually
- Resolve automatically with a heuristic
- Load urban community from a file (add the path to the file in the command
line arguments)
- Save urban community in a file

## ✅ Requirements

- OpenJDK 17

## 🏁 Quick start

The main method is in the class App.

### 📨 Run directly

- Get the source code on your machine
- Clear the `bin` directory if needed with `rm -rf bin`
- Build sources files with `javac -d bin --source-path src src/App.java`
- Run the project with `java -cp bin App`

### 📦 Build the jar

- Get the source code on your machine
- Run the `build.bash`
- Run the generated jar with `java -jar project_paa.jar`



