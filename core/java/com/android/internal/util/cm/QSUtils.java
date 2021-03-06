package com.android.internal.util.cm;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.hardware.display.WifiDisplayStatus;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.nfc.NfcAdapter;
import android.os.BatteryManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.internal.telephony.PhoneConstants;

import java.io.File;

public class QSUtils {
        public static boolean deviceSupportsImeSwitcher(Context ctx) {
            Resources res = ctx.getResources();
            return res.getBoolean(com.android.internal.R.bool.config_show_cmIMESwitcher);
        }

        public static boolean deviceSupportsUsbTether(Context ctx) {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            return (cm.getTetherableUsbRegexs().length != 0);
        }

        public static boolean deviceSupportsWifiDisplay(Context ctx) {
            DisplayManager dm = (DisplayManager) ctx.getSystemService(Context.DISPLAY_SERVICE);
            return (dm.getWifiDisplayStatus().getFeatureState() != WifiDisplayStatus.FEATURE_STATE_UNAVAILABLE);
        }

        public static boolean deviceSupportsMobileData(Context ctx) {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.isNetworkSupported(ConnectivityManager.TYPE_MOBILE);
        }

        public static boolean deviceSupportsBluetooth() {
            return (BluetoothAdapter.getDefaultAdapter() != null);
        }

        public static boolean systemProfilesEnabled(ContentResolver resolver) {
            return (Settings.System.getInt(resolver, Settings.System.SYSTEM_PROFILES_ENABLED, 1) == 1);
        }

        public static boolean deviceSupportsPerformanceProfiles(Context ctx) {
            Resources res = ctx.getResources();
            String perfProfileProp = res.getString(
                    com.android.internal.R.string.config_perf_profile_prop);
            return !TextUtils.isEmpty(perfProfileProp);
        }

        public static boolean expandedDesktopEnabled(ContentResolver resolver) {
            return Settings.System.getIntForUser(resolver, Settings.System.EXPANDED_DESKTOP_STYLE,
                    0, UserHandle.USER_CURRENT_OR_SELF) != 0;
        }

        public static boolean deviceSupportsNfc(Context ctx) {
            return NfcAdapter.getDefaultAdapter(ctx) != null;
        }

        public static boolean deviceSupportsLte(Context ctx) {
            final TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            return (tm.getLteOnCdmaMode() == PhoneConstants.LTE_ON_CDMA_TRUE) || tm.getLteOnGsmMode() != 0;
        }

        public static boolean deviceSupportsDockBattery(Context ctx) {
            BatteryManager bm = (BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
            return bm.isDockBatterySupported();
        }

        public static boolean deviceSupportsCamera() {
            return Camera.getNumberOfCameras() > 0;
        }

        public static boolean deviceSupportsGps(Context context) {
            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
        }

        public static boolean deviceSupportsTorch(Context context) {
            return context.getResources().getBoolean(com.android.internal.R.bool.config_enableTorch);
        }

        public static boolean adbEnabled(ContentResolver resolver) {
            return (Settings.Global.getInt(resolver, Settings.Global.ADB_ENABLED, 0)) == 1;
        }

        public static boolean deviceSupportsFastcharge() {
            return new File("/sys/kernel/fast_charge/force_fast_charge").exists();
        }

        public static boolean deviceSupportsCPUFreq() {
            /*
             * Actually every kernel should support this,
             * but just in case!
             */
            String[] paths = { "/sys/devices/system/cpu/cpu0/cpufreq/scaling_max_freq", "/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq" };
            for (String path : paths)
                if (new File(path).exists()) return true;
            return false;
        }

        public static boolean deviceSupportsCompass(Context context) {
            SensorManager sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            return (sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null
                    && sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null);
        }
}
