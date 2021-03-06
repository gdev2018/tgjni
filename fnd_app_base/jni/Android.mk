LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := crypto

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    LOCAL_SRC_FILES := ./boringssl/lib/libcrypto_armeabi-v7a.a
else ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_SRC_FILES := ./boringssl/lib/libcrypto_arm64-v8a.a
else ifeq ($(TARGET_ARCH_ABI),x86)
    LOCAL_SRC_FILES := ./boringssl/lib/libcrypto_x86.a
else ifeq ($(TARGET_ARCH_ABI),x86_64)
    LOCAL_SRC_FILES := ./boringssl/lib/libcrypto_x86_64.a
endif

include $(PREBUILT_STATIC_LIBRARY)


include $(CLEAR_VARS)

LOCAL_CPPFLAGS := -Wall -std=c++11 -DANDROID -frtti -DHAVE_PTHREAD -finline-functions -ffast-math -O0
LOCAL_C_INCLUDES += ./jni/boringssl/include/
LOCAL_ARM_MODE := arm
LOCAL_MODULE := tgnet
#LOCAL_STATIC_LIBRARIES := registerNativeTgNetFunctions

LOCAL_SRC_FILES := \
./tgnet/ApiScheme.cpp \
./tgnet/BuffersStorage.cpp \
./tgnet/ByteArray.cpp \
./tgnet/ByteStream.cpp \
./tgnet/Connection.cpp \
./tgnet/ConnectionSession.cpp \
./tgnet/ConnectionsManager.cpp \
./tgnet/ConnectionSocket.cpp \
./tgnet/Datacenter.cpp \
./tgnet/EventObject.cpp \
./tgnet/FileLog.cpp \
./tgnet/MTProtoScheme.cpp \
./tgnet/NativeByteBuffer.cpp \
./tgnet/Request.cpp \
./tgnet/Timer.cpp \
./tgnet/TLObject.cpp \
./tgnet/FileLoadOperation.cpp \
./tgnet/ProxyCheckInfo.cpp \
./tgnet/Handshake.cpp \
./tgnet/Config.cpp

include $(BUILD_STATIC_LIBRARY)

include $(CLEAR_VARS)

LOCAL_CFLAGS := -Wall -DANDROID -DHAVE_MALLOC_H -DHAVE_PTHREAD -DWEBP_USE_THREAD -finline-functions -ffast-math -ffunction-sections -fdata-sections -O0
LOCAL_C_INCLUDES += ./jni/libwebp/src
LOCAL_ARM_MODE := arm
LOCAL_STATIC_LIBRARIES := cpufeatures
LOCAL_MODULE := webp

ifneq ($(findstring armeabi-v7a, $(TARGET_ARCH_ABI)),)
  NEON := c.neon
else
  NEON := c
endif

LOCAL_SRC_FILES := \
./libwebp/dec/alpha.c \
./libwebp/dec/buffer.c \
./libwebp/dec/frame.c \
./libwebp/dec/idec.c \
./libwebp/dec/io.c \
./libwebp/dec/quant.c \
./libwebp/dec/tree.c \
./libwebp/dec/vp8.c \
./libwebp/dec/vp8l.c \
./libwebp/dec/webp.c \
./libwebp/dsp/alpha_processing.c \
./libwebp/dsp/alpha_processing_sse2.c \
./libwebp/dsp/cpu.c \
./libwebp/dsp/dec.c \
./libwebp/dsp/dec_clip_tables.c \
./libwebp/dsp/dec_mips32.c \
./libwebp/dsp/dec_neon.$(NEON) \
./libwebp/dsp/dec_sse2.c \
./libwebp/dsp/enc.c \
./libwebp/dsp/enc_avx2.c \
./libwebp/dsp/enc_mips32.c \
./libwebp/dsp/enc_neon.$(NEON) \
./libwebp/dsp/enc_sse2.c \
./libwebp/dsp/lossless.c \
./libwebp/dsp/lossless_mips32.c \
./libwebp/dsp/lossless_neon.$(NEON) \
./libwebp/dsp/lossless_sse2.c \
./libwebp/dsp/upsampling.c \
./libwebp/dsp/upsampling_neon.$(NEON) \
./libwebp/dsp/upsampling_sse2.c \
./libwebp/dsp/yuv.c \
./libwebp/dsp/yuv_mips32.c \
./libwebp/dsp/yuv_sse2.c \
./libwebp/enc/alpha.c \
./libwebp/enc/analysis.c \
./libwebp/enc/backward_references.c \
./libwebp/enc/config.c \
./libwebp/enc/cost.c \
./libwebp/enc/filter.c \
./libwebp/enc/frame.c \
./libwebp/enc/histogram.c \
./libwebp/enc/iterator.c \
./libwebp/enc/picture.c \
./libwebp/enc/picture_csp.c \
./libwebp/enc/picture_psnr.c \
./libwebp/enc/picture_rescale.c \
./libwebp/enc/picture_tools.c \
./libwebp/enc/quant.c \
./libwebp/enc/syntax.c \
./libwebp/enc/token.c \
./libwebp/enc/tree.c \
./libwebp/enc/vp8l.c \
./libwebp/enc/webpenc.c \
./libwebp/utils/bit_reader.c \
./libwebp/utils/bit_writer.c \
./libwebp/utils/color_cache.c \
./libwebp/utils/filters.c \
./libwebp/utils/huffman.c \
./libwebp/utils/huffman_encode.c \
./libwebp/utils/quant_levels.c \
./libwebp/utils/quant_levels_dec.c \
./libwebp/utils/random.c \
./libwebp/utils/rescaler.c \
./libwebp/utils/thread.c \
./libwebp/utils/utils.c

include $(BUILD_STATIC_LIBRARY)


include $(CLEAR_VARS)
LOCAL_ARM_MODE  := arm
LOCAL_CFLAGS 	:= -w -std=c11 -Os -DNULL=0 -DSOCKLEN_T=socklen_t -DLOCALE_NOT_USED -D_LARGEFILE_SOURCE=1
LOCAL_CFLAGS 	+= -DANDROID_NDK -DDISABLE_IMPORTGL -fno-strict-aliasing -fprefetch-loop-arrays -DAVOID_TABLES -DANDROID_TILE_BASED_DECODE -DANDROID_ARMV6_IDCT -DHAVE_STRCHRNUL=0
LOCAL_MODULE := sqlite
LOCAL_SRC_FILES     := \
./sqlite/sqlite3.c
include $(BUILD_STATIC_LIBRARY)


include $(CLEAR_VARS)
LOCAL_ARM_MODE  := arm
LOCAL_MODULE := lz4
LOCAL_CFLAGS 	:= -w -std=c11 -O3
LOCAL_SRC_FILES     := \
./lz4/lz4.c \
./lz4/lz4frame.c \
./lz4/xxhash.c
include $(BUILD_STATIC_LIBRARY)


include $(CLEAR_VARS)
LOCAL_ARM_MODE  := arm
LOCAL_MODULE := rlottie
LOCAL_CPPFLAGS := -DNDEBUG -Wall -std=c++14 -DANDROID -fno-rtti -DHAVE_PTHREAD -finline-functions -ffast-math -Os -fno-exceptions -fno-unwind-tables -fno-asynchronous-unwind-tables -Wnon-virtual-dtor -Woverloaded-virtual -Wno-unused-parameter -fvisibility=hidden
ifeq ($(TARGET_ARCH_ABI),$(filter $(TARGET_ARCH_ABI),armeabi-v7a))
 LOCAL_CFLAGS := -DUSE_ARM_NEON  -fno-integrated-as
 LOCAL_CPPFLAGS += -DUSE_ARM_NEON  -fno-integrated-as
else ifeq ($(TARGET_ARCH_ABI),$(filter $(TARGET_ARCH_ABI),arm64-v8a))
 LOCAL_CFLAGS := -DUSE_ARM_NEON -D__ARM64_NEON__ -fno-integrated-as
 LOCAL_CPPFLAGS += -DUSE_ARM_NEON -D__ARM64_NEON__ -fno-integrated-as
endif
LOCAL_C_INCLUDES := \
./jni/rlottie/inc \
./jni/rlottie/src/vector/ \
./jni/rlottie/src/vector/pixman \
./jni/rlottie/src/vector/freetype \
./jni/rlottie/src/vector/stb
LOCAL_SRC_FILES     := \
./rlottie/src/lottie/lottieanimation.cpp \
./rlottie/src/lottie/lottieitem.cpp \
./rlottie/src/lottie/lottiekeypath.cpp \
./rlottie/src/lottie/lottieloader.cpp \
./rlottie/src/lottie/lottiemodel.cpp \
./rlottie/src/lottie/lottieparser.cpp \
./rlottie/src/lottie/lottieproxymodel.cpp \
./rlottie/src/vector/freetype/v_ft_math.cpp \
./rlottie/src/vector/freetype/v_ft_raster.cpp \
./rlottie/src/vector/freetype/v_ft_stroker.cpp \
./rlottie/src/vector/pixman/vregion.cpp \
./rlottie/src/vector/stb/stb_image.cpp \
./rlottie/src/vector/vbezier.cpp \
./rlottie/src/vector/vbitmap.cpp \
./rlottie/src/vector/vbrush.cpp \
./rlottie/src/vector/vcompositionfunctions.cpp \
./rlottie/src/vector/vdasher.cpp \
./rlottie/src/vector/vdebug.cpp \
./rlottie/src/vector/vdrawable.cpp \
./rlottie/src/vector/vdrawhelper.cpp \
./rlottie/src/vector/vdrawhelper_neon.cpp \
./rlottie/src/vector/velapsedtimer.cpp \
./rlottie/src/vector/vimageloader.cpp \
./rlottie/src/vector/vinterpolator.cpp \
./rlottie/src/vector/vmatrix.cpp \
./rlottie/src/vector/vpainter.cpp \
./rlottie/src/vector/vpath.cpp \
./rlottie/src/vector/vpathmesure.cpp \
./rlottie/src/vector/vraster.cpp \
./rlottie/src/vector/vrect.cpp \
./rlottie/src/vector/vrle.cpp
ifeq ($(TARGET_ARCH_ABI),$(filter $(TARGET_ARCH_ABI),armeabi-v7a))
    LOCAL_SRC_FILES += ./rlottie/src/vector/pixman/pixman-arm-neon-asm.S.neon
else ifeq ($(TARGET_ARCH_ABI),$(filter $(TARGET_ARCH_ABI),arm64-v8a))
    LOCAL_SRC_FILES += ./rlottie/src/vector/pixman/pixman-arma64-neon-asm.S.neon
endif
LOCAL_STATIC_LIBRARIES := cpufeatures
include $(BUILD_STATIC_LIBRARY)


include $(CLEAR_VARS)
LOCAL_PRELINK_MODULE := false

LOCAL_MODULE 	:= tmessages.29
LOCAL_CFLAGS 	:= -w -std=c11 -Os -DNULL=0 -DSOCKLEN_T=socklen_t -DLOCALE_NOT_USED -D_LARGEFILE_SOURCE=1
LOCAL_CFLAGS 	+= -Drestrict='' -D__EMX__ -DOPUS_BUILD -DFIXED_POINT -DUSE_ALLOCA -DHAVE_LRINT -DHAVE_LRINTF -fno-math-errno
LOCAL_CFLAGS 	+= -DANDROID_NDK -DDISABLE_IMPORTGL -fno-strict-aliasing -fprefetch-loop-arrays -DAVOID_TABLES -DANDROID_TILE_BASED_DECODE -DANDROID_ARMV6_IDCT -ffast-math -D__STDC_CONSTANT_MACROS
LOCAL_CPPFLAGS 	:= -DBSD=1 -ffast-math -Os -funroll-loops -std=c++11
LOCAL_LDLIBS 	:= -ljnigraphics -llog -lz -latomic -lOpenSLES -lEGL -lGLESv2 -landroid
LOCAL_STATIC_LIBRARIES := webp sqlite lz4 rlottie tgnet crypto

LOCAL_C_INCLUDES    := \
./jni/boringssl/include \
./jni/intro \
./jni/rlottie/inc \
./jni/lz4

LOCAL_SRC_FILES     += \
./jni.c \
./image.c \
./intro/IntroRenderer.c \
./lottie.cpp \
./SqliteWrapper.cpp \
./SqliteWrapperExtension.cpp \
./TgNetWrapper.cpp \
./NativeLoader.cpp

include $(BUILD_SHARED_LIBRARY)



include $(CLEAR_VARS)
LOCAL_ARM_MODE  := arm
LOCAL_CFLAGS 	:= -w -std=c11 -Os -DNULL=0 -DSOCKLEN_T=socklen_t -DLOCALE_NOT_USED -D_LARGEFILE_SOURCE=1
LOCAL_CFLAGS 	+= -DANDROID_NDK -DDISABLE_IMPORTGL -fno-strict-aliasing -fprefetch-loop-arrays -DAVOID_TABLES -DANDROID_TILE_BASED_DECODE -DANDROID_ARMV6_IDCT -DHAVE_STRCHRNUL=0
LOCAL_MODULE := sqlitemd5
LOCAL_SRC_FILES     := \
./sqlite/md5.c
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_ARM_MODE  := arm
LOCAL_CFLAGS 	:= -w -std=c11 -Os -DNULL=0 -DSOCKLEN_T=socklen_t -DLOCALE_NOT_USED -D_LARGEFILE_SOURCE=1
LOCAL_CFLAGS 	+= -DANDROID_NDK -DDISABLE_IMPORTGL -fno-strict-aliasing -fprefetch-loop-arrays -DAVOID_TABLES -DANDROID_TILE_BASED_DECODE -DANDROID_ARMV6_IDCT -DHAVE_STRCHRNUL=0
LOCAL_MODULE := sqliterot13
LOCAL_SRC_FILES     := \
./sqlite/rot13.c
include $(BUILD_SHARED_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_ARM_MODE  := arm
#LOCAL_CFLAGS 	:= -w -std=c11 -Os -DNULL=0 -DSOCKLEN_T=socklen_t -DLOCALE_NOT_USED -D_LARGEFILE_SOURCE=1
#LOCAL_CFLAGS 	+= -DANDROID_NDK -DDISABLE_IMPORTGL -fno-strict-aliasing -fprefetch-loop-arrays -DAVOID_TABLES -DANDROID_TILE_BASED_DECODE -DANDROID_ARMV6_IDCT -DHAVE_STRCHRNUL=0
#LOCAL_MODULE := sqliteicu
#LOCAL_SRC_FILES     := \
#./sqlite/icu.c
#include $(BUILD_SHARED_LIBRARY)

$(call import-module,android/cpufeatures)