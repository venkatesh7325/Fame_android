package com.purpledot.fame.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.purpledot.fame.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ondoor (Hasnain) on 6/9/2016.
 */
public class Utility {
    public static Context appContext;

    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        //textView.setTypeface(Typeface.DEFAULT_BOLD);
        snackbar.show();
    }

    public static void hideKeyBoard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //FOR CHECKING INTERNET CONNECTITIVITY
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    //FOR CHECKING EMAIL VALIDIY
    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //For getting string sharedpreference
    public static String getSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(Constants.CLASSTIME_PREF, 0);
        return settings.getString(name, "");
    }

    //For getting integer sharedpreference
    public static int getIngerSharedPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(Constants.CLASSTIME_PREF, 0);
        return settings.getInt(name, 0);
    }

    //For getting boolean sharedpreference
    public static boolean getSharedPreferencesBoolean(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(Constants.CLASSTIME_PREF, 0);
        return settings.getBoolean(name, false);
    }

    //For setting integer shared preference
    public static void setIntegerSharedPreference(Context context, String name, int value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(Constants.CLASSTIME_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    //For setting string shared preference
    public static void setSharedPreference(Context context, String name, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(Constants.CLASSTIME_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        // editor.clear();
        editor.putString(name, value);
        editor.apply();
    }

    //For setting boolean shared preference
    public static void setSharedPreferenceBoolean(Context context, String name, boolean value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(Constants.CLASSTIME_PREF, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static void saveUniversityList(Context context, String unvList) {
        appContext = context;
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(Constants.CLASSTIME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("universitylist", unvList);
        editor.apply();
    }

    public static String getUniversityList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CLASSTIME_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString("universitylist", "");
    }


    public static void saveBranchList(Context context, String branchlist) {
        appContext = context;
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(Constants.CLASSTIME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("branchlist", branchlist);
        editor.apply();
    }

    public static String getBranchListList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CLASSTIME_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString("branchlist", "");
    }

    public static void saveUserID(Context context, int branchlist) {
        appContext = context;
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(Constants.CLASSTIME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("UserID", branchlist);
        editor.apply();
    }

    public static int getUserID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CLASSTIME_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("UserID", 0);
    }


    public static void saveRole(Context context, String branchlist) {
        appContext = context;
        SharedPreferences sharedPreferences = appContext.getSharedPreferences(Constants.CLASSTIME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("role_id", branchlist);
        editor.apply();
    }

    public static String getRole(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CLASSTIME_PREF, Context.MODE_PRIVATE);
        return sharedPreferences.getString("role_id", "");
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    public static Date getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String strDate = mdformat.format(calendar.getTime());
        Date date = null;
        try {
            date = mdformat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getCurrentDateOnly() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String strDate = mdformat.format(calendar.getTime());
        Date date = null;
        try {
            date = mdformat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }
        return extension;
    }

   /* public static void setDocTypeImage(Context context, ImageView docImage, String extension) {
        switch (extension) {
            case "pdf":
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.pdf));
                break;
            case "docx":
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.doc));
                break;
            case "doc":
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.doc));
                break;
            case "xls":
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.xl));
                break;
            case "txt":
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.txt));
                break;
            case "png":
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.jpg));
                break;
            case "jpeg":
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.jpg));
                break;
            case "jpg":
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.jpg));
                break;
            default:
                docImage.setImageDrawable(context.getResources().getDrawable(R.drawable.doc));
                break;
        }
    }*/

    public static void showDatePickerDialog(Context context, final EditText textView, String title) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.US);
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog fromDatePickerDialog = new
                DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);
                textView.setText(dateFormatter.format(newCalendar.getTime()));
            }


        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.setCancelable(false);
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.setTitle(title);
        fromDatePickerDialog.show();
    }


}
