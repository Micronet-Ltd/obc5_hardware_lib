#include <jni.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include "iosocket.h"
#include "api.h"

#ifdef __cplusplus
extern "C" {
#endif

#define RTC_STRING_SIZE 23

enum LED {
    GPS,
    VIOLATION
};

static pthread_mutex_t mutexlock;

JNIEXPORT jobjectArray JNICALL
Java_micronet_hardware_MControl_jniGetMCUVersion(JNIEnv *env, jobject instance) {
    uint8_t data[255];
    memset(data, 0, sizeof(data)); // for automatically-allocated arrays

    int result = -1;
    jstring jresult = NULL;

    // Create initial arr
    jobjectArray ret = (jobjectArray) env->NewObjectArray(2,env->FindClass("java/lang/String"),env->NewStringUTF(""));

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_mcu_version(&fd, data, 4);
        iosocket_disconnect(&fd);
        snprintf((char *) data, sizeof(data), "%X.%X.%X.%X", data[0], data[1], data[2],
                 data[3]);
        jresult = env->NewStringUTF((char *) data);
    }

    // convert result int to char arr
    char resultStr[6];
    memset(resultStr, 0, sizeof(resultStr));
    snprintf(resultStr, sizeof(resultStr), "%d", result);

    // Set array data
    env->SetObjectArrayElement(ret, 0, env->NewStringUTF(resultStr));
    env->SetObjectArrayElement(ret, 1, jresult);

    return ret;
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetFPGAVersion(JNIEnv *env, jobject instance) {
    int size = 2;
    jintArray ret = env->NewIntArray(size);

    uint32_t fpga_ver = 0;
    int result = -1;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_fpga_version(&fd, &fpga_ver, 4);
        //LOGI("result: %d, FPGA Version: %X", result, fpga_ver);
        iosocket_disconnect(&fd);
    }

    // Set result array
    jint tmp[2];
    tmp[0] = result;
    tmp[1] = fpga_ver;

    env->SetIntArrayRegion(ret, 0, size, tmp);

    return ret;
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetADCorGPIVoltage(JNIEnv *env, jobject instance, jint gpi_num) {
    int size = 2;
    jintArray ret = env->NewIntArray(size);

    uint32_t voltage = 0;
    int result = -1;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_adc_or_gpi_voltage(&fd, gpi_num, &voltage, sizeof(voltage));
        //LOGI("result: %d, FPGA Version: %X", result, fpga_ver);
        iosocket_disconnect(&fd);
    }

    // Set result array
    jint tmp[2];
    tmp[0] = result;
    tmp[1] = voltage;

    env->SetIntArrayRegion(ret, 0, size, tmp);

    return ret;
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetLEDStatus(JNIEnv *env, jobject instance, jint led_num) {
    int size = 5;
    jintArray ret = env->NewIntArray(size);

    int result = -1;

    jint tmp[5];
    uint8_t brightness = 0;
    uint8_t red = 0;
    uint8_t green = 0;
    uint8_t blue = 0;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_led_status(&fd, led_num, &brightness, &red, &green, &blue);
        iosocket_disconnect(&fd);

    }

    // Set info in ret array
    tmp[0] = result;
    tmp[1] = red;
    tmp[2] = green;
    tmp[3] = blue;
    tmp[4] = brightness;
    env->SetIntArrayRegion(ret, 0, size, tmp);

    return ret;
}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniSetLEDValue(JNIEnv *env, jobject instance, jint led, jint brightness, jint rgb) {
    int result = -1;

    uint8_t red = (rgb & 0xFF0000) >> 16;
    uint8_t green = (rgb & 0x00FF00) >> 8;
    uint8_t blue = rgb & 0x0000FF;

    pthread_mutex_lock(&mutexlock);
    int fd = iosocket_connect();
    if (fd != 0) {
        result = set_led_status(&fd, led, brightness, red, green, blue);
        iosocket_disconnect(&fd);
    }
    pthread_mutex_unlock(&mutexlock);

    return result;
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetPowerOnThresholdCfg(JNIEnv *env, jobject instance) {
    int size = 4;
    jintArray ret = env->NewIntArray(size);

    int result = -1;

    jint tmp[4];
    uint16_t wiggle_count = 0;
    uint16_t wig_cnt_sample_period = 0;
    uint16_t ignition_threshold = 0;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_power_on_threshold_cfg(&fd, &wiggle_count, &wig_cnt_sample_period, &ignition_threshold);
        iosocket_disconnect(&fd);
    }

    tmp[0] = result;
    tmp[1] = wiggle_count;
    tmp[2] = wig_cnt_sample_period;
    tmp[3] = ignition_threshold;
    env->SetIntArrayRegion(ret, 0, size, tmp);

    return ret;
}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniSetPowerOnThresholdCfg(JNIEnv *env, jobject instance) {

    // TODO

}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetPowerOnReason(JNIEnv *env, jobject instance) {
    int size = 2;
    jintArray ret = env->NewIntArray(size);

    int result = -1;
    uint8_t power_on_reason = 0;

    int fd = iosocket_connect();
    if(fd != 0) {
        result = get_power_on_reason(&fd, &power_on_reason);
        iosocket_disconnect(&fd);
    }

    jint tmp[2];
    tmp[0] = result;
    tmp[1] = power_on_reason;
    env->SetIntArrayRegion(ret, 0, size, tmp);

    return ret;
}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniSetDevicePowerOff(JNIEnv *env, jobject instance, jint wait_time) {
    int result = -1;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = set_device_power_off(&fd, wait_time);
        iosocket_disconnect(&fd);
    }

    return result;
}

JNIEXPORT jobjectArray JNICALL
Java_micronet_hardware_MControl_jniGetRTCDateTime(JNIEnv *env, jobject instance) {
    // Create initial arr
    jobjectArray ret = (jobjectArray) env->NewObjectArray(2,env->FindClass("java/lang/String"),env->NewStringUTF(""));

    char dt_str[RTC_STRING_SIZE] = "2016-03-29 19:09:06.58";
    int result = -1;
    jstring jresult = NULL;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_rtc_date_time(&fd, dt_str);
        iosocket_disconnect(&fd);
        jresult = env->NewStringUTF(dt_str);
    }

    // convert result int to char arr
    char resultStr[6];
    memset(resultStr, 0, sizeof(resultStr));
    snprintf(resultStr, sizeof(resultStr), "%d", result);

    // Set array data
    env->SetObjectArrayElement(ret, 0, env->NewStringUTF(resultStr));
    env->SetObjectArrayElement(ret, 1, jresult);

    return ret;
}

/**
 *  Expected dt_str format: year-month-day hour:min:sec.deciseconds
 * 					  Ex : 2016-03-29 19:09:06.58
 */
JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniSetRTCDateTime(JNIEnv *env, jobject instance, jstring time) {
    char * dt_str = (char *)env->GetStringUTFChars(time, JNI_FALSE);

    int result = -1;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = set_rtc_date_time(&fd, dt_str);
        iosocket_disconnect(&fd);
    }

    return result;
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetRTCCalReg(JNIEnv *env, jobject instance) {
    jintArray jarr = env->NewIntArray(3);
    jint *narr = env->GetIntArrayElements(jarr, NULL);

    int result = -1;
    uint8_t rtc_dig_cal = 0;
    uint8_t rtc_analog_cal = 0;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_rtc_cal_reg(&fd, &rtc_dig_cal, &rtc_analog_cal);
        iosocket_disconnect(&fd);
    }

    narr[0] = result;
    narr[1] = rtc_dig_cal;
    narr[2] = rtc_analog_cal;
    env->ReleaseIntArrayElements(jarr, narr, NULL);

    return jarr;
}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniSetRTCCalReg(JNIEnv *env, jobject instance) {

    // TODO

}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniGetRTCRegDBG(JNIEnv *env, jobject instance) {

    // TODO

}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniSetRTCRegDBG(JNIEnv *env, jobject instance) {

    // TODO

}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniSetGPIOStateDBG(JNIEnv *env, jobject instance, jint jgpio_num, jint jgpio_value) {
    int result = -1;
    uint8_t gpio_value = 0x00000001 & jgpio_value;
    uint16_t gpio_number = 0x0000ffff & jgpio_num;

    int fd = iosocket_connect();
    if(fd != 0){
        result = set_gpio_state_dbg(&fd, gpio_number, gpio_value);
        iosocket_disconnect(&fd);
    }

    return result;
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetGPIOStateDBG(JNIEnv *env, jobject instance, jint jgpio_num) {
    int size = 2;
    jintArray ret = env->NewIntArray(size);

    int result = -1;
    uint8_t gpio_value = 0x00000000;
    uint16_t gpio_number = 0x0000ffff & jgpio_num;

    int fd = iosocket_connect();
    if(fd != 0){
        result = get_gpio_state_dbg(&fd, gpio_number, &gpio_value);
        iosocket_disconnect(&fd);
    }

    jint tmp[2];
    tmp[0] = result;
    tmp[1] = gpio_value;
    env->SetIntArrayRegion(ret, 0, size, tmp);

    return ret;
}

/**
 * returns false if RTC battery is bad or register could not be read.
 * returns true if RTC battery is good.
 */
JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniCheckRTCBattery(JNIEnv *env, jobject instance) {
    int size = 2;
    jintArray ret = env->NewIntArray(size);

    uint8_t battery_state = 0;
    int result = -1;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = check_rtc_battery(&fd, &battery_state);
        iosocket_disconnect(&fd);
    }

    jint tmp[2];
    tmp[0] = result;
    tmp[1] = battery_state;
    env->SetIntArrayRegion(ret, 0, size, tmp);

    // If battery_state is 0 that indicates bad or not present RTC battery.
    return ret;
}

JNIEXPORT void JNICALL
Java_micronet_hardware_MControl_jniSetSysPropPowerCtlShutdown(JNIEnv *env, jobject instance) {
    // preferred method shutting down Android
    system("setprop sys.powerctl shutdown");
}

#ifdef __cplusplus
}
#endif