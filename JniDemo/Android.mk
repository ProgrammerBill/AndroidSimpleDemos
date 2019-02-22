#
# Copyright (C) 2008 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# This makefile shows how to build a shared library and an activity that
# bundles the shared library and calls it using JNI.

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

# Build activity
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, java)

LOCAL_PACKAGE_NAME := JniDemo

LOCAL_JNI_SHARED_LIBRARIES := \
	libdemo_jni_static \
	libdemo_jni_dynamic \

LOCAL_PROGUARD_ENABLED := disabled

LOCAL_CERTIFICATE := platform

LOCAL_STATIC_JAVA_LIBRARIES := \
	android-support-v4 \
	android-support-v7-appcompat \
	android-support-constraint-layout \
	android-support-constraint-layout-solver

appcompat_dir := frameworks/support/v7/appcompat/res
constraint_layout_dir := libs/constraint-layout/res

res_dir := res $(constraint_layout_dir)
LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, $(res_dir)) \
					  $(appcompat_dir)

LOCAL_AAPT_FLAGS := --auto-add-overlay \
					--extra-packages android.support.v7.appcompat \
					--extra-packages android.support.constraint 

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
	android-support-constraint-layout:libs/constraint-layout/libs/android-support-constraint-layout.jar
include $(BUILD_MULTI_PREBUILT)

include $(CLEAR_VARS)
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := \
	android-support-constraint-layout-solver:libs/constraint-layout-solver/android-support-constraint-layout-solver.jar
include $(BUILD_MULTI_PREBUILT)

# ============================================================

# Also build all of the sub-targets under this one: the shared library.
include $(call all-makefiles-under,$(LOCAL_PATH))
