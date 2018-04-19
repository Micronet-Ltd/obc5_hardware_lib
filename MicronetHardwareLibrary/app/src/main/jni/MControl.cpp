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

JNIEXPORT jstring JNICALL
Java_micronet_hardware_MControl_jniGetMCUVersion(JNIEnv *env, jobject instance) {
    uint8_t data[255];
    memset(data, 0, sizeof(data)); // for automatically-allocated arrays

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int result = 0;
    jstring jresult = NULL;

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_mcu_version(&fd, data, 4);

        // If result > 0 then call successful
        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        snprintf((char *) data, sizeof(data), "%X.%X.%X.%X", data[0], data[1], data[2],
                 data[3]);
        jresult = env->NewStringUTF((char *) data);
        return jresult;
    }
    else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return jresult;
    }
}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniGetFPGAVersion(JNIEnv *env, jobject instance) {

    uint32_t fpga_ver = 0;
    int result = 0;

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_fpga_version(&fd, &fpga_ver, 4);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        return fpga_ver;
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return fpga_ver;
    }
}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniGetADCorGPIVoltage(JNIEnv *env, jobject instance, jint gpi_num) {
    uint32_t voltage = 0;
    int result = 0;

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int fd = iosocket_connect();
    if (fd != 0) {
        result = get_adc_or_gpi_voltage(&fd, gpi_num, &voltage, sizeof(voltage));

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        return voltage;
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return voltage;
    }
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetLEDStatus(JNIEnv *env, jobject instance, jint led_num) {
    int size = 4;
    jintArray newArray = env->NewIntArray(size);

    int result = 0;

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int fd = iosocket_connect();
    if (fd != 0) {
        jint tmp[3];
        uint8_t brightness, red, green, blue;
        result = get_led_status(&fd, led_num, &brightness, &red, &green, &blue);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        tmp[0] = red;
        tmp[1] = green;
        tmp[2] = blue;
        tmp[3] = brightness;
        // copy jint[] to jintarray
        env->SetIntArrayRegion(newArray, 0, size, tmp);
        return newArray;
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return newArray;
    }
}

JNIEXPORT void JNICALL
Java_micronet_hardware_MControl_jniSetLEDValue(JNIEnv *env, jobject instance, jint led, jint brightness, jint rgb) {

    int result = 0;

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    uint8_t red = (rgb & 0xFF0000) >> 16;
    uint8_t green = (rgb & 0x00FF00) >> 8;
    uint8_t blue = rgb & 0x0000FF;

    pthread_mutex_lock(&mutexlock);
    int fd = iosocket_connect();
    if (fd != 0) {
        result = set_led_status(&fd, led, brightness, red, green, blue);
        //LOGI("LED: %d, red: 0x%02X, green: 0x%02X, blue: 0x%02X, result: %d", led, red, green, blue, result);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
    }else{
        pthread_mutex_unlock(&mutexlock);

        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
    }
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetPowerOnThresholdCfg(JNIEnv *env, jobject instance) {

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int result;

    int ret = 0;
    int size = 3;
    jintArray newArray = env->NewIntArray(size);
    int fd = iosocket_connect();
    if (fd != 0) {
        jint tmp[3];
        uint16_t wiggle_count, wig_cnt_sample_period, ignition_threshold;
        result = get_power_on_threshold_cfg(&fd, &wiggle_count, &wig_cnt_sample_period, &ignition_threshold);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        tmp[0] =wiggle_count;
        tmp[1] =wig_cnt_sample_period;
        tmp[2] = ignition_threshold;
        env->SetIntArrayRegion(newArray, 0, size, tmp);

        return newArray;
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return newArray;
    }
}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniSetPowerOnThresholdCfg(JNIEnv *env, jobject instance) {

    // TODO

}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniGetPowerOnReason(JNIEnv *env, jobject instance) {

    uint8_t power_on_reason = -3;
    int fd = iosocket_connect();
    int result;

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    if(fd != 0) {
        result = get_power_on_reason(&fd, &power_on_reason);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        return power_on_reason;
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return power_on_reason;
    }
}

JNIEXPORT void JNICALL
Java_micronet_hardware_MControl_jniSetDevicePowerOff(JNIEnv *env, jobject instance, jint wait_time) {

    int result = 0;

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int fd = iosocket_connect();

    if (fd != 0) {
        result = set_device_power_off(&fd, wait_time);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
    }
}

JNIEXPORT jstring JNICALL
Java_micronet_hardware_MControl_jniGetRTCDateTime(JNIEnv *env, jobject instance) {
    char dt_str[RTC_STRING_SIZE] = "2016-03-29 19:09:06.58";
    int result = 0;
    jstring jresult = NULL;

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int fd = iosocket_connect();

    if (fd != 0) {
        result = get_rtc_date_time(&fd, dt_str);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        jresult = env->NewStringUTF(dt_str);
        return jresult;
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return jresult;
    }
}

/**
 *  Expected dt_str format: year-month-day hour:min:sec.deciseconds
 * 					  Ex : 2016-03-29 19:09:06.58
 */
JNIEXPORT void JNICALL
Java_micronet_hardware_MControl_jniSetRTCDateTime(JNIEnv *env, jobject instance, jstring time) {

    char * dt_str = (char *)env->GetStringUTFChars(time, JNI_FALSE);

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int result = 0;
    int fd = iosocket_connect();

    if (fd != 0) {
        result = set_rtc_date_time(&fd, dt_str);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
    }
}

JNIEXPORT jintArray JNICALL
Java_micronet_hardware_MControl_jniGetRTCCalReg(JNIEnv *env, jobject instance) {
    uint8_t rtc_dig_cal, rtc_analog_cal;
    jintArray jarr = env->NewIntArray(2);
    jint *narr = env->GetIntArrayElements(jarr, NULL);

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int result = 0;
    int fd = iosocket_connect();

    if (fd != 0) {
        result = get_rtc_cal_reg(&fd, &rtc_dig_cal, &rtc_analog_cal);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        printf("get rtc cal registers, dig cal: %x analog cal: %x, ret = %d  \n", \
					rtc_dig_cal, rtc_analog_cal, result);
        iosocket_disconnect(&fd);
        narr[0] = rtc_dig_cal;
        narr[1] = rtc_analog_cal;

        env->ReleaseIntArrayElements(jarr, narr, NULL);

        return jarr;
    }else{
        env->ReleaseIntArrayElements(jarr, narr, NULL);

        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return jarr;
    }
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

JNIEXPORT void JNICALL
Java_micronet_hardware_MControl_jniSetGPIOStateDBG(JNIEnv *env, jobject instance, jint jgpio_num, jint jgpio_value) {

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int result = 0;
    int fd = iosocket_connect();
    uint8_t gpio_value = 0x00000001 & jgpio_value;
    uint16_t gpio_number = 0x0000ffff & jgpio_num;

    if(fd != 0){
        result = set_gpio_state_dbg(&fd, gpio_number, gpio_value);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
    }
}

JNIEXPORT jint JNICALL
Java_micronet_hardware_MControl_jniGetGPIOStateDBG(JNIEnv *env, jobject instance, jint jgpio_num) {

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int result = 0;
    int fd = iosocket_connect();
    uint8_t gpio_value = 0x00000000;
    uint16_t gpio_number = 0x0000ffff & jgpio_num;

    if(fd != 0){
        result = get_gpio_state_dbg(&fd, gpio_number, &gpio_value);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        return gpio_value;
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);
        return gpio_value;
    }


}

/**
 * returns false if RTC battery is bad or register could not be read.
 * returns true if RTC battery is good.
 */
JNIEXPORT jboolean JNICALL
Java_micronet_hardware_MControl_jniCheckRTCBattery(JNIEnv *env, jobject instance) {
    int result = 0;

    jclass cls = env->FindClass("micronet/hardware/MControl");
    jfieldID fid = env->GetStaticFieldID(cls,"returnCode","I");

    int fd = iosocket_connect();

    if (fd != 0) {
        result = check_rtc_battery(&fd);

        if(result >= 0){
            env->SetStaticIntField(cls,fid,0);
        }else{
            env->SetStaticIntField(cls,fid,result);
        }

        iosocket_disconnect(&fd);
        return result >= 0;
    }else{
        // Couldn't connect to iosocket
        env->SetStaticIntField(cls,fid,-1);

        // 0 indicates no response or bad RTC battery
        return result >= 0;
    }


}

JNIEXPORT void JNICALL
Java_micronet_hardware_MControl_jniSetSysPropPowerCtlShutdown(JNIEnv *env, jobject instance) {
    // preferred method shutting down Android
    system("setprop sys.powerctl shutdown");
}

#ifdef __cplusplus
}
#endif