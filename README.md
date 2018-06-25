# Using the Micronet Hardware Library 

#### How to use the .aar file with your project
Follow directions located here https://stackoverflow.com/a/34919810.

#### How to get an instance of the Micronet Hardware Library
Use “MicronetHardware.getInstance()” to get a Micronet Hardware object. 

```java
MicronetHardware micronetHardware = MicronetHardware.getInstance();
```
    
From there, use that object to call the other available functions. 

```java
try{
    String mcuVersion = micronetHardware.getMcuVersion();
    String fpgaVersion = micronetHardware.getFpgaVersion();
    String rtcDateTime = micronetHardware.getRtcDateTime();
} catch (MicronetHardwareException e) {
    Log.e(TAG, e.toString());
}
```

#### Available Fields
| Name | Description |
|------|-------------|
| kADC_ANALOG_IN1 | Constant describing ignition. |
| kADC_CABLE_TYPE | Constant describing cable type. |
| kADC_GPIO_IN1 | Constant describing gpio input 1. |
| kADC_GPIO_IN2 | Constant describing gpio input 2. |
| kADC_GPIO_IN3 | Constant describing gpio input 3. |
| kADC_GPIO_IN4 | Constant describing gpio input 4. |
| kADC_GPIO_IN5 | Constant describing gpio input 5. |
| kADC_GPIO_IN6 | Constant describing gpio input 6. |
| kADC_GPIO_IN7 | Constant describing gpio input 7. |
| kADC_POWER_IN | Constant describing battery voltage. |
| kADC_POWER_VCAP | Constant describing the super cap. |
| kADC_TEMPERATURE | Constant describing the temperature sensor. |
| TYPE_IGNITION | A constant describing ignition signal id. Same as kADC_ANALOG_IN1 |

#### Available Methods
| Name | Description |
|------|-------------|
| checkRtcBattery() | Checks if the RTC battery is good, bad or not present. |
| getAllAnalogInput() | Gets analog input state of all A2D input signals. |
| getAllPinInState() | Gets all input pin state of an Automotive I/O signal. |
| getAnalogInput(int inputType) | Gets analog input state of an A2D input signal. |
| getFpgaVersion() | Gets the fpga version. |
| getInputState(int inputType) | Gets input state of an Automotive input signal. |
| getInstance()  | Gets an instance of MicronetHardware. |
| getLedStatus(int led_num) | To get the LED status, the following command can be sent. |
| getMcuVersion() | Gets the MCU version. |
| getPowerUpIgnitionState() | Get Power up ignition connected I/O state. |
| getRtcCalReg() | Get the digital and analog rtc calibration registers. |
| getRtcDateTime() | Gets the MCU rtc date and time. |
| SetDelayedPowerDownTime(int timeInSeconds) | Sets Delayed Power down Time in seconds. |
| setLedStatus(int led, int brightness, int rgb) | Set an LEDs brightness and color. |
| setRtcDateTime(java.lang.String dateTime) | Sets the MCU rtc date and time. |

#### Example to get Ignition State and Voltage
```java
int ignitionVoltage = micronetHardware.getAnalogInput(MicronetHardware.TYPE_IGNITION);
int ignitionState = micronetHardware.getInputState(MicronetHardware.TYPE_IGNITION);
```

#### Other Example Usage
```java
int analog_in1 = micronetHardware.getAnalogInput(MicronetHardware.kADC_ANALOG_IN1);
int gpio_in1 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN1);
int gpio_in2 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN2);
int gpio_in3 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN3);
int gpio_in4 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN4);
int gpio_in5 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN5);
int gpio_in6 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN6);
int gpio_in7 = micronetHardware.getAnalogInput(MicronetHardware.kADC_GPIO_IN7);
int power_in = micronetHardware.getAnalogInput(MicronetHardware.kADC_POWER_IN);
int power_vcap = micronetHardware.getAnalogInput(MicronetHardware.kADC_POWER_VCAP);
int temperature = micronetHardware.getAnalogInput(MicronetHardware.kADC_TEMPERATURE);
int cable_type = micronetHardware.getAnalogInput(MicronetHardware.kADC_CABLE_TYPE);

int analog_in1 = micronetHardware.getInputState(MicronetHardware.kADC_ANALOG_IN1);
int gpio_in1 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN1);
int gpio_in2 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN2);
int gpio_in3 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN3);
int gpio_in4 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN4);
int gpio_in5 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN5);
int gpio_in6 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN6);
int gpio_in7 = micronetHardware.getInputState(MicronetHardware.kADC_GPIO_IN7);
```

##### Look at Javadocs for additional information on the library




