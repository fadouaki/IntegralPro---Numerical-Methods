# IntegralPro 📐

**IntegralPro** is a modern Android application built with **Jetpack Compose** that allows users to perform numerical integration calculations with ease. It features a clean, intuitive interface, real-time visualization of the integral, and a history of past calculations.

---

## 📸 Screenshots

| Input Screen | Result Screen | History Screen |
|:---:|:---:|:---:|
| ![Input](placeholder_input.png) | ![Result](placeholder_result.png) | ![History](placeholder_history.png) |

---

## 📑 Table of Contents
- [Features](#-features)
- [Technical Stack](#-technical-stack)
- [Supported Methods](#-supported-methods)
- [Installation](#-installation)
- [Usage Guide](#-usage-guide)
- [Project Structure](#-project-structure)
- [License](#-license)

---

## ✨ Features

- **Input Flexibility**: Support for complex mathematical expressions (e.g., `x^2`, `sin(x)`, `e^x`).
- **Templates**: Quick access to common function templates via suggestion chips.
- **Visualization**: Canvas-based drawing of the function curve and approximation shapes (rectangles/trapezoids).
- **History**: Automatically saves calculation history using a local database.
- **Comparisons**: View past results to compare accuracy between methods.
- **Dark Mode**: Fully supports system dark theme.
- **Haptic Feedback**: Tactile response for interactions.
- **Help Guide**: In-app explanations for each numerical method.

---

## 🛠 Technical Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material3)
- **Architecture**: Clean Architecture with MVVM (Model-View-ViewModel)
- **Concurrency**: Kotlin Coroutines & Flow
- **Persistence**: [Room Database](https://developer.android.com/training/data-storage/room)
- **Math Parsing**: [exp4j](https://www.objecthunter.net/exp4j/)
- **Navigation**: Navigation Compose
- **Build System**: Gradle Kotlin DSL

---

## 🔢 Supported Methods

IntegralPro implements the following numerical integration algorithms:

1.  **Riemann Sum (Left)**: Approximates area using rectangles based on the left endpoint.
2.  **Riemann Sum (Right)**: Approximates area using rectangles based on the right endpoint.
3.  **Midpoint Rule**: Uses the function value at the center of each interval for better accuracy.
4.  **Trapezoidal Rule**: Approximates the region using trapezoids.
5.  **Simpson's Rule**: Uses quadratic polynomials (parabolas) for high accuracy (requires even $n$).

---

## 📥 Installation

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/integral-pro.git
    cd integral-pro
    ```
2.  **Open in Android Studio**:
    - Select "Open" and choose the project directory.
    - Wait for Gradle sync to complete.
3.  **Run the App**:
    - Connect an Android device or start an emulator (API 24+).
    - Click the **Run** (▶️) button.

---

## 📖 Usage Guide

1.  **Enter Function**: Type a mathematical expression in terms of `x` (e.g., `x^2` or `sin(x)`). You can tap a template chip to auto-fill.
2.  **Set Bounds**: Enter the **Lower Bound (a)** and **Upper Bound (b)**.
3.  **Set Subdivisions**: Enter the number of intervals **(n)**. Higher values generally provide better accuracy.
4.  **Select Method**: Choose an integration algorithm from the dropdown menu.
5.  **Calculate**: Tap the **Calculate** button.
6.  **View Result**:
    - See the numeric approximation.
    - Visualize the area under the curve.
    - Tap **History** to see previous calculations.

### Example

To calculate $\int_0^1 x^2 \, dx$:
- **Function**: `x^2`
- **Lower Bound**: `0`
- **Upper Bound**: `1`
- **Subdivisions**: `100`
- **Method**: `Trapezoidal`
- **Result**: `0.3333` (approx 1/3)

---

## 📂 Project Structure

The project follows a modular Clean Architecture structure:

```
app/src/main/java/com/example/integralpro/
├── data/           # Data layer (Room DB, Repository)
│   ├── local/      # DAO, Entities, Database
├── domain/         # Domain layer (Business Logic)
│   ├── NumericalIntegrator.kt  # Integration algorithms
│   ├── UseCases.kt             # Interactors
├── ui/             # Presentation layer (Compose)
│   ├── MainScreen.kt           # Input UI
│   ├── ResultScreen.kt         # Result & Visualization
│   ├── HistoryScreen.kt        # History List
│   ├── HelpScreen.kt           # Educational Content
│   ├── IntegralVisualizer.kt   # Canvas Drawing
│   ├── MainViewModel.kt        # State Management
│   ├── theme/                  # Material3 Theme
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
