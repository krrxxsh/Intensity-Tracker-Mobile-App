@echo off
"D:\\Android-Studio\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HC:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\libcxx_helper" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=27" ^
  "-DANDROID_PLATFORM=android-27" ^
  "-DANDROID_ABI=x86" ^
  "-DCMAKE_ANDROID_ARCH_ABI=x86" ^
  "-DANDROID_NDK=D:\\Android-Studio\\ndk\\27.0.12077973" ^
  "-DCMAKE_ANDROID_NDK=D:\\Android-Studio\\ndk\\27.0.12077973" ^
  "-DCMAKE_TOOLCHAIN_FILE=D:\\Android-Studio\\ndk\\27.0.12077973\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=D:\\Android-Studio\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=C:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\build\\intermediates\\cxx\\RelWithDebInfo\\5u2v69yp\\obj\\x86" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=C:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\build\\intermediates\\cxx\\RelWithDebInfo\\5u2v69yp\\obj\\x86" ^
  "-DCMAKE_BUILD_TYPE=RelWithDebInfo" ^
  "-BC:\\Users\\EDA LAB\\AndroidStudioProjects\\Intensitytracker\\openCV\\.cxx\\RelWithDebInfo\\5u2v69yp\\x86" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
