# Fox Alert 🦊🗓️

**Stay organized and never miss a special date!**

Fox Alert is an offline-first Android application designed to help you keep track of important dates and set timely reminders. Whether it's a birthday, a meeting, or any significant event, Fox Alert allows you to mark the date, add details, categorize it, and get notified.

Built with modern Android development tools, including Jetpack Compose, Room Database, and following Clean Architecture and MVVM principles with Hilt for dependency injection.

## ✨ Features

* **Mark Special Dates:** Easily add important dates to your calendar.
* **Set Reminders:** Create reminders with a title, description, and specific time.
* **Categorize Events:** Assign categories to your reminders (e.g., Birthday, Meeting, Anniversary).
* **Define Custom Categories:** Create and manage your own categories to personalize your organization system.
* **Offline First:** All your data is stored locally using Room Database, ensuring access even without an internet connection.
* **Clean & Intuitive UI:** A user-friendly interface built with Jetpack Compose.

## 📸 Screenshots / GIFs

*(Replace these with actual screenshots or a GIF of your app)*

<img src="link_to_screenshot_1.png" alt="Screenshot 1" width="300"/>
<img src="link_to_screenshot_2.png" alt="Screenshot 2" width="300"/>
<img src="link_to_gif.gif" alt="App GIF" width="300"/>

## 🛠 Technologies Used

* **Kotlin:** A modern, concise, and safe programming language for Android development.
* **Jetpack Compose:** Android's modern toolkit for building native UI declaratively.
* **Room Database:** Persistence library providing an abstraction layer over SQLite.
* **ViewModel:** Manages UI-related data in a lifecycle-aware manner.
* **Kotlin Coroutines & Flows:** For asynchronous programming and reactive data streams.
* **Hilt:** Dependency Injection library for Android built on top of Dagger.
* **Clean Architecture:** Layered architecture promoting separation of concerns (Presentation, Domain, Data).
* **MVVM (Model-View-ViewModel):** Architectural pattern to separate UI logic from business logic.

## 📐 Architecture

The application follows the principles of **Clean Architecture** and the **MVVM** architectural pattern.

* **Presentation Layer:** Contains the UI (Jetpack Compose) and ViewModels. ViewModels expose state to the UI and handle user interactions, communicating with the Domain layer via Use Cases.
* **Domain Layer:** The core business logic layer. Contains Use Cases that orchestrate actions and define the application's capabilities. It's independent of any specific framework.
* **Data Layer:** Responsible for handling data operations. It includes Repositories that abstract the data sources (like Room Database) and provide data to the Domain layer.

**Hilt** is used throughout the project for efficient and scalable dependency injection, managing the creation and provision of dependencies across these layers.

## 🚀 Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/ArianAhmadifard/FoxAlert.git]
    ```

2.  **Open in Android Studio:** Open the cloned project in Android Studio.

3.  **Sync Project:** Android Studio will automatically sync the project and download the necessary dependencies.

4.  **Run on Emulator or Device:** Select an emulator or connect an Android device and run the project.

## 👋 Contributing

Contributions are welcome! If you find a bug or have an idea for an improvement, please open an issue or submit a pull request.

## 📄 License

This project is licensed under the [َApache 2.0](LICENSE) - see the [LICENSE](https://github.com/ArianAhmadifard/FoxAlert/blob/dev/LICENSE) file for details. 

## ✍️ Author

* [https://github.com/ArianAhmadifard]
* [https://www.linkedin.com/in/arian-ahmadifard/]

---
