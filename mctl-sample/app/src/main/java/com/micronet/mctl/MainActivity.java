package com.micronet.mctl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.micronet.mcontrol.MControl;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "mctl";

//    static {
//        System.loadLibrary("mctl");
//    }

//    mcontrol functions
//    get_mcu_version
//    get_fpga_version
//    set_led_status
//    get_adc_or_gpi_voltage
//    get_power_on_reason
//    set_device_power_off
//    get_rtc_date_time
//    set_rtc_date_time
//    get_rtc_cal_reg
//    get_led_status
//    check_rtc_battery
//    get_power_on
//    setSysPropPowerCtlShutdown

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MControl mControl = new MControl();

        Log.d(TAG, "get_mcu_version: " + mControl.get_mcu_version());
        Log.d(TAG, "get_fpga_version: " + mControl.get_fpga_version());
//        Log.d(TAG, "set_led_status: ");
        // Set right led to red
//         mControl.set_led_status(0,255,16711680);
        Log.d(TAG, "get_adc_or_gpi_voltage: " + mControl.get_adc_or_gpi_voltage(0x09));
        Log.d(TAG, "get_power_on_reason: " + mControl.get_power_on_reason());
//        Log.d(TAG, "set_device_power_off: " + mControl.set_device_power_off(5));
        Log.d(TAG, "get_rtc_date_time: " + mControl.get_rtc_date_time());
//        Log.d(TAG, "set_rtc_date_time: " + mControl.set_rtc_date_time(""));
        Log.d(TAG, "get_rtc_cal_reg: " + mControl.get_rtc_cal_reg());
        Log.d(TAG, "get_led_status: " + mControl.get_led_status(0));
        Log.d(TAG, "check_rtc_battery: " + mControl.check_rtc_battery());
        Log.d(TAG, "get_power_on: " + mControl.get_power_on());
//        Log.d(TAG, "setSysPropPowerCtlShutdown: " + mControl.setSysPropPowerCtlShutdown());


        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
    }

}
