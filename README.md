# PelecardExam

Welcome to the **PelecardExam** project! This is a demonstration of a professional, scalable, and efficient approach to implementing a payment SDK using modern Android development practices.

## 📋 Overview

The **PelecardExam** project is a comprehensive implementation focused on handling payment operations efficiently and securely using Kotlin, Jetpack Compose, and Hilt for dependency injection. The architecture emphasizes clean code principles and easy maintainability.

## 🚀 Features

- **MVVM Architecture**: Decoupled and scalable code design, following the Model-View-ViewModel pattern.
- **Jetpack Compose**: Modern UI development with Compose, eliminating the need for XML layouts.
- **Hilt for Dependency Injection**: Simplifying dependency management across the project.
- **Repository Pattern**: Separation of data sources for better testability and abstraction.
- **Kotlin DSL (KTS) Gradle**: Efficient and clear build configurations using Kotlin scripting.

## 📂 Project Structure

```plaintext
├── MyApp
│   ├── MainActivity.kt
│   ├── repos
│   ├── ui
│   ├── di
│   ├── data
│   ├── model
│   └── utils
```

- **MainActivity**: Entry point for the app, utilizing Jetpack Compose for the UI.
- **repos**: Contains repository classes to manage data sources.
- **ui**: Compose-based UI elements and screens.
- **di**: Dependency injection setup using Hilt.
- **data**: Handles data models and remote/local data sources.
- **model**: Defines the domain models used across the app.
- **utils**: Utility classes and helper functions.

## ⚙️ Getting Started

1. **Clone the repository**:
   ```bash
   git clone https://github.com/unix14/PelecardExam.git
   ```
2. **Open the project** in Android Studio.
3. **Build and run** on an Android emulator or device.

## 🛠️ How It Was Built

- **Architecture**: The app uses the MVVM architecture, enhancing separation of concerns and making the code easier to maintain and test.
- **Jetpack Compose**: The UI is built using Jetpack Compose, embracing declarative programming and eliminating the use of XML layouts.
- **Dependency Injection**: Hilt is used to manage dependencies efficiently, ensuring a clean and modular setup.
- **Repository Pattern**: Data operations are abstracted using repositories, simplifying the flow of data between the data sources and the ViewModel.

## 📝 Author

**Eyal Yaakobi**
- GitHub: [unix14](https://github.com/unix14)

Feel free to reach out or check my other projects for more Android and Flutter content!

## 📜 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
