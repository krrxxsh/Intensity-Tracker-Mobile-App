# Intensity Tracker Mobile App

Android app for measuring image intensity from camera, gallery, and video frames.

## Tech Stack
- **Language:** Java
- **Platform:** Android (minSdk 27, targetSdk 35, compileSdk 35)
- **Build:** Gradle (Android Application plugin)
- **Architecture style:** Activity-based UI + Room persistence

## Main Features
- Capture an image from the camera and analyze intensity
- Pick an image from gallery and analyze intensity
- Record/select video, capture a frame, then analyze intensity
- Crop selected image/frame before processing
- Draw ROI (region of interest) around brightest area
- Save analysis history locally
- Export selected history records to Excel (`.xlsx`)

## Libraries and Modules Used

### AndroidX / UI
- `androidx.appcompat:appcompat`
- `com.google.android.material:material`
- `androidx.activity:activity`
- `androidx.constraintlayout:constraintlayout`
- `androidx.recyclerview:recyclerview`
- `androidx.cardview:cardview`

### Data Storage
- `androidx.room:room-runtime`
- `androidx.room:room-compiler` (annotation processor)

### Image/Video Processing
- `:openCV` local module (OpenCV integration)
- `com.github.CanHub:Android-Image-Cropper` (image/frame cropping)
- `com.google.android.exoplayer:exoplayer` (video playback for frame selection)

### Export
- `org.apache.poi:poi-ooxml` (Excel export)

### Testing
- `junit:junit`
- `androidx.test.ext:junit`
- `androidx.test.espresso:espresso-core`

## Project Structure
- `/app` - main Android application module
- `/openCV` - OpenCV module integrated into the app
- `app/src/main/java/com/example/intensitytracker/`
  - `MainActivity` - menu/navigation entry point
  - `ImageProcessingActivity` - image/frame processing + intensity calculation + save to DB
  - `VideoFrameSelectorActivity` - video playback and frame capture flow
  - `HistoryActivity` - history list + Excel export
  - `HistoryAdapter` - RecyclerView adapter for saved analyses
- `app/src/main/java/com/example/intensitytracker/database/`
  - `ImageEntity` - Room table schema
  - `HistoryDao` - Room DAO queries
  - `HistoryDatabase` - Room database singleton

## How the Intensity Extraction Algorithm Works
Implementation is in `ImageProcessingActivity#processImage(...)`.

1. Convert the selected/cropped `Bitmap` to OpenCV `Mat`
2. Convert color image to grayscale (`Imgproc.cvtColor(..., COLOR_BGR2GRAY)`)
3. Find the brightest pixel location with `Core.minMaxLoc(gray)`
4. Build a **10x10 ROI** centered around that brightest point (clamped to image bounds)
5. Compute ROI mean intensity using `Core.mean(roi)`
6. Compute ROI min/max intensity using `Core.minMaxLoc(roi)`
7. Draw a red rectangle on the original image at ROI location
8. Show calculated metrics on screen:
   - Mean intensity
   - Min intensity
   - Max intensity
   - ROI location (`x`, `y`)
   - ROI size (`width`, `height`)
9. Save processed result and metadata in Room DB:
   - Image name
   - ROI coordinates and dimensions
   - Intensity values
   - Processed image bytes
   - Timestamp

## Data Persistence
- Table: `image_table`
- Stored fields include image metadata, ROI coordinates, intensity values, image bytes, and timestamp.
- History is ordered by newest first (`ORDER BY timestamp DESC`).

## Export Flow
From `HistoryActivity`:
1. Select one or more history rows
2. Tap export
3. App generates an `.xlsx` file in Downloads using Apache POI
4. File can be opened or shared through Android intents

## Build and Run
1. Open the project in Android Studio
2. Sync Gradle
3. Run on emulator/device

Typical command:
```bash
./gradlew assembleDebug
```

## Permissions
Declared in `AndroidManifest.xml`:
- Camera
- Read external storage
- Write external storage
- Record video

## Notes
- OpenCV is initialized in `ImageProcessingActivity` static block via `OpenCVLoader.initDebug()`.
- The app currently uses Room destructive fallback migration (`fallbackToDestructiveMigration`).
