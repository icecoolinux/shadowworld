TARGET_PLATFORM := android-32
#TARGET_ARCH=arm64
#TARGET_ARCH_ABI=arm64-v8a
#APP_ABI := arm64-v8a

ROOT_PATH := $(call my-dir)

########################################################################################################

include $(CLEAR_VARS)

LOCAL_MODULE     := openal
LOCAL_ARM_MODE   := arm
LOCAL_PATH       := $(ROOT_PATH)
LOCAL_C_INCLUDES := $(LOCAL_PATH) $(LOCAL_PATH)/../openal/include $(LOCAL_PATH)/../openal/OpenAL32/Include
LOCAL_SRC_FILES  := ../openal/OpenAL32/alAuxEffectSlot.c \
                    ../openal/OpenAL32/alBuffer.c        \
                    ../openal/OpenAL32/alEffect.c        \
                    ../openal/OpenAL32/alError.c         \
                    ../openal/OpenAL32/alExtension.c     \
                    ../openal/OpenAL32/alFilter.c        \
                    ../openal/OpenAL32/alListener.c      \
                    ../openal/OpenAL32/alSource.c        \
                    ../openal/OpenAL32/alState.c         \
                    ../openal/OpenAL32/alThunk.c         \
                    ../openal/Alc/ALc.c                  \
                    ../openal/Alc/alcConfig.c            \
                    ../openal/Alc/alcDedicated.c         \
                    ../openal/Alc/alcEcho.c              \
                    ../openal/Alc/alcModulator.c         \
                    ../openal/Alc/alcReverb.c            \
                    ../openal/Alc/alcRing.c              \
                    ../openal/Alc/alcThread.c            \
                    ../openal/Alc/ALu.c                  \
                    ../openal/Alc/bs2b.c                 \
                    ../openal/Alc/helpers.c              \
                    ../openal/Alc/hrtf.c                 \
                    ../openal/Alc/mixer.c                \
                    ../openal/Alc/panning.c              \
                    ../openal/Alc/backends/android.c     \
                    ../openal/Alc/backends/loopback.c    \
                    ../openal/Alc/backends/null.c        \

#                    ../openal/Alc/backends/opensl.c     \

LOCAL_CFLAGS     := -ffast-math -DAL_BUILD_LIBRARY -DAL_ALEXT_PROTOTYPES
LOCAL_LDLIBS     := -llog -Wl,-s

include $(BUILD_SHARED_LIBRARY)

########################################################################################################


include $(CLEAR_VARS)

LOCAL_MODULE     := openaltest
LOCAL_ARM_MODE   := arm
LOCAL_PATH       := $(ROOT_PATH)
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../openal/include
LOCAL_SRC_FILES  := com_icecool_worldinshadow_ActividadOpenAL.c     \

LOCAL_LDLIBS     := -llog -Wl,-s

LOCAL_SHARED_LIBRARIES := libopenal

include $(BUILD_SHARED_LIBRARY)
