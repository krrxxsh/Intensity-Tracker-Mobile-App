@echo off
"D:\\Android-Studio\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HC:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=27" ^
  "-DANDROID_PLATFORM=android-27" ^
  "-DANDROID_ABI=arm64-v8a" ^
  "-DCMAKE_ANDROID_ARCH_ABI=arm64-v8a" ^
  "-DANDROID_NDK=D:\\Android-Studio\\ndk\\27.0.12077973" ^
  "-DCMAKE_ANDROID_NDK=D:\\Android-Studio\\ndk\\27.0.12077973" ^
  "-DCMAKE_TOOLCHAIN_FILE=D:\\Android-Studio\\ndk\\27.0.12077973\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=D:\\Android-Studio\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=C:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\build\\intermediates\\cxx\\Debug\\gj521n6c\\obj\\arm64-v8a" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=C:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\build\\intermediates\\cxx\\Debug\\gj521n6c\\obj\\arm64-v8a" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BC:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\.cxx\\Debug\\gj521n6c\\arm64-v8a" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
