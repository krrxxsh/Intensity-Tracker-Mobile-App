@echo off
"D:\\Android-Studio\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HC:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=21" ^
  "-DANDROID_PLATFORM=android-21" ^
  "-DANDROID_ABI=x86" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86" ^
  "-DANDROID_NDK=D:\\Android-Studio\\ndk\\27.0.12077973" ^
  "-DCMAKE_ANDROID_NDK=D:\\Android-Studio\\ndk\\27.0.12077973" ^
  "-DCMAKE_TOOLCHAIN_FILE=D:\\Android-Studio\\ndk\\27.0.12077973\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=D:\\Android-Studio\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=C:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\build\\intermediates\\cxx\\Debug\\516f252b\\obj\\x86" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=C:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\build\\intermediates\\cxx\\Debug\\516f252b\\obj\\x86" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-BC:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\.cxx\\Debug\\516f252b\\x86" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
