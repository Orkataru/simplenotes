# Simple Notes App

A lightweight, secure Android note-taking application that allows users to create and manage notes directly on their device.


<div style="display: flex; justify-content: space-around;">
  <img src="https://github.com/user-attachments/assets/9d5d83fd-375d-4b17-bea0-4eb485c73bf9" alt="Screenshot 1" width="250"/>
  <img src="https://github.com/user-attachments/assets/edcd3308-b049-455f-a8a8-3d99168fb662" alt="Screenshot 2" width="250"/>
  <img src="https://github.com/user-attachments/assets/5d571eaa-6763-44b7-922b-54cb41949d4f" alt="Screenshot 3" width="250"/>
</div>


## Features

- 📝 Clean, distraction-free note taking
- 🔄 Automatic saving as you type
- 🔒 Secure on-device storage
- 📱 Native Android app
- 🚫 No login required
- ⚡ Fast and responsive

## Technical Stack

- **Language:** Java
- **Architecture:** MVVM
- **Database:** Room with SQLCipher encryption
- **UI Components:** AndroidX, Material Design
- **Navigation:** AndroidX Navigation Component
- **Async Operations:** Kotlin Coroutines

## Getting Started

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or newer
- JDK 11 or newer
- Android SDK with minimum API level 21 (Android 5.0)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Orkataru/simplenotes.git
   ```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Run the app on an emulator or physical device

## Building

To build the app:

1. From Android Studio:
   - Select `Build > Build Bundle(s) / APK(s) > Build APK(s)`
   - The APK will be generated in `app/build/outputs/apk/debug/`

2. From command line:
   ```bash
   ./gradlew assembleDebug
   ```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 