LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := mp3decoder
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_LDLIBS := \
	-llog \

LOCAL_SRC_FILES := \
	app/src/main/jni/main.c \
	app/src/main/jni/minimp3.c \

LOCAL_C_INCLUDES += app/src/main/jni
LOCAL_C_INCLUDES += app/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
