/* 
 *  Copyright (C) 2013 Micronet ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package micronet.hardware;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static micronet.hardware.MicronetHardware.TAG;

/**
 *  Micronet Hardware Information class.
 */
public class Info {

    /**
     * VERSION
     * Version of the API for Micronet Hardware API.
     * Format is YYYYMMDD.NUMBER.
     * For example 20150817.000 represents August 17th 2015 and minor version 0.
     */
    public static final String VERSION = "20180413.000";

    /**
     * Returns Serial Number of the device.
     */
    public String GetSerialNumber() {
        String serialNumber = "";

        try {
            String commands[] = {"/system/bin/getprop", "ro.serialno"};
            Process process = Runtime.getRuntime().exec(commands);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                serialNumber = line.replaceAll("\\s+","");
            }
            Log.d(TAG, "Serial Number: " + serialNumber);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        return serialNumber;
    }
}
