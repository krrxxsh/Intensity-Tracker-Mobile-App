# Intensity Tracker Mobile App

The **Intensity Tracker Mobile App** is a native Android application designed to analyze images using OpenCV. It provides detailed information about images, such as pixel coordinates, region dimensions, and intensity values. The app is ideal for engineers, students, and researchers who need quick insight into image properties.

## Features

- **Image Processing with OpenCV:** Analyze images to extract coordinates (X, Y), width, height, and minimum, maximum, and average intensity values.
- **Flexible Image Input:** Choose images from your device’s gallery or capture new photos with your camera.
- **Interactive Cropping:** Crop images before analysis using the CanHub Android Image Cropper library for precise region selection.
- **History Tracking:** Save and view a history of all processed images and their analysis for easy reference and comparison.
- **Export to Excel:** Export selected image analyses to `.xlsx` files using Apache POI (XSSFWorkbook), making it easy to share or document your results.
- **User-Friendly UI:** Simple, intuitive interface designed for fast and efficient workflow.

## Technologies Used

- **OpenCV:** High-performance image processing and analysis.
- **CanHub Android Image Cropper:** Interactive cropping functionality for better region selection.
- **Apache POI:** Exporting analysis data to Excel files (`.xlsx`).
- **Android (Java):** Native Android development for optimal performance and device integration.

## Getting Started

Follow the instructions below to build and run the Intensity Tracker Mobile App on your Android device:

### Prerequisites

- Android Studio (latest stable version recommended)
- Android SDK (API level 21 or above)
- Java JDK 8 or above
- An Android device or emulator

### Installation

1. **Clone the Repository**
   ```sh
   git clone https://github.com/krrxxsh/Intensity-Tracker-Mobile-App.git
   cd Intensity-Tracker-Mobile-App
   ```

2. **Open in Android Studio**
   - Open Android Studio.
   - Select `Open an existing project`.
   - Navigate to the `Intensity-Tracker-Mobile-App` folder and open it.

3. **Sync Gradle**
   - Wait for Gradle to sync all dependencies.
   - If prompted, install any missing SDK components.

4. **Build and Run**
   - Connect your Android device via USB (with developer mode enabled), or set up an emulator.
   - Click the **Run** button (green triangle) in Android Studio, or use `Shift+F10`.
   - The app will be built and installed on your device.

### Permissions

The app requires the following permissions:
- Camera access (to capture photos)
- Storage access (to select images and export Excel files)

Android will prompt you to grant these permissions as needed.

## Usage

1. **Select or Capture an Image:** Use the gallery or camera options.
2. **Crop the Image:** Use the cropping tool to select the region of interest.
3. **Analyze:** View detailed intensity and region data.
4. **Save to History:** Optionally save the analysis for future reference.
5. **Export:** Select one or more history entries and export them to Excel.

## Screenshots

> _Add screenshots here to showcase key features and UI._

## Contributing

Contributions are welcome! Please open an issue or submit a pull request with improvements or bug fixes.

## License

> _Add license details if applicable._

## Author

**[krrxxsh](https://github.com/krrxxsh)**

---

**Note:** This README was generated automatically by analyzing the repository. For further customization or questions, feel free to edit or ask!
