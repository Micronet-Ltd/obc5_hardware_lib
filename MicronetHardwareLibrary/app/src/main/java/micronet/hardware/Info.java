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

import android.os.Build;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 *  Micronet Hardware Information class.
 */
public final class Info {

    /**
     * VERSION
     * Version of the API for Micronet Hardware API.
     * Format is YYYYMMDD.NUMBER.
     * For example 20150817.000 represents August 17th 2015 and minor version 0.
     */
    public static final String VERSION = "20190408.000";

    /**
     * Constant for SmartHub.
     */
    public static final int SMART_HUB = 0;

    /**
     * Constant for MT5 in Basic Cradle.
     */
    public static final int BASIC_CRADLE = 1;

    /**
     * Constant for MT5 in Smart Cradle.
     */
    public static final int SMART_CRADLE = 2;

    /**
     * Returns Serial Number of the device.
     */
    public String GetSerialNumber() {
        return Build.SERIAL;
    }

    /**
     * @return which type of device it is: SmartHub, Smart Cradle, or Basic Cradle.
     */
    public int getDeviceType() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/sys/class/switch/dock/state"));

            StringBuilder sb = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                sb.append(inputLine);
            }

            if (!sb.toString().equalsIgnoreCase("")) {
                int dockState = Integer.parseInt(sb.toString());

                switch (dockState) {
                    case 1:
                        return 1;
                    case 3:
                        return 1;
                    case 5:
                        return 2;
                    case 7:
                        return 2;
                    case 8:
                        return 0;
                    case 10:
                        return 0;
                }
            }
        } catch (Exception e) {
            // Error getting device type
            return -1;
        }

        // Error getting device type
        return -1;
    }
}
