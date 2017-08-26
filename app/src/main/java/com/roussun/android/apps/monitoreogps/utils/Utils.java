package com.roussun.android.apps.monitoreogps.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static void saveImage(Bitmap image, String path, String fileName) {
        //String root = getFilesDir();//Environment.getExternalStorageDirectory().toString();
        //File myDir = new File(root + "/" + path);
        File myDir = new File(path);
        File file = new File(myDir, fileName);
        if (!file.exists()) {
            try {
                FileOutputStream out = new FileOutputStream(file);
                image.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void createFolder(String name) {
        Log.e("", "name: " + name);
        File dir = new File(name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void deleteFile(String path, String fileName) {
        File file = new File(path + "/" + fileName);
        if (file.exists()) {
            file.delete();
        }
    }


    public static boolean existsFile(String path, String fileName) {
        File file = new File(path + "/" + fileName);
        return file.exists();
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle, boolean front) {
        Matrix matrix = new Matrix();

        // Perform matrix rotations/mirrors depending on camera that took the photo
        if (front) {
            float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
            Matrix matrixMirrorY = new Matrix();
            matrixMirrorY.setValues(mirrorY);

            matrix.postConcat(matrixMirrorY);
        }

        matrix.postRotate(angle);

        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap rotateImage(String filePath, Uri uri, Context mContext) {
        Bitmap resultBitmap = null;
        ;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[16 * 1024];
        options.inSampleSize = 2;

        try {
            ContentResolver cr = mContext.getContentResolver();
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);

            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                resultBitmap = RotateBitmap(bitmap, 90, false);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                resultBitmap = RotateBitmap(bitmap, 180, false);
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                resultBitmap = RotateBitmap(bitmap, 270, false);
            } else {
                resultBitmap = RotateBitmap(bitmap, 0, false);
            }

            // Rotate the bitmap

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return resultBitmap;
    }

    public static float getDegree(String exifOrientation) {
        float degree = 0;
        if (exifOrientation.equals("6"))
            degree = 90;
        else if (exifOrientation.equals("3"))
            degree = 180;
        else if (exifOrientation.equals("8"))
            degree = 270;
        return degree;
    }

    public static Bitmap getBitmap(Uri uri, String path, Context mContext) {

        Bitmap bitmap = null;
        Uri actualUri = uri;//Uri.parse(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[16 * 1024];
        options.inSampleSize = 2;
        ContentResolver cr = mContext.getContentResolver();
        float degree = 0;
        try {

            ExifInterface exif = new ExifInterface(path);
            String exifOrientation = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            bitmap = BitmapFactory.decodeStream(cr.openInputStream(actualUri), null, options);
            if (bitmap != null) {
                degree = getDegree(exifOrientation);
                if (degree != 0)
                    bitmap = createRotatedBitmap(bitmap, degree);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createRotatedBitmap(Bitmap bm, float degree) {
        Bitmap bitmap = null;
        if (degree != 0) {
            Matrix matrix = new Matrix();
            matrix.preRotate(degree);
            bitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        }

        return bitmap;
    }

    public static Bitmap cropImage(Bitmap sourceBitmap) {

        Bitmap cropBitmap;

        if (sourceBitmap.getWidth() >= sourceBitmap.getHeight()) {

            cropBitmap = Bitmap.createBitmap(
                    sourceBitmap,
                    sourceBitmap.getWidth() / 2 - sourceBitmap.getHeight() / 2,
                    0,
                    sourceBitmap.getHeight(),
                    sourceBitmap.getHeight()
            );

        } else {

            cropBitmap = Bitmap.createBitmap(
                    sourceBitmap,
                    0,
                    sourceBitmap.getHeight() / 2 - sourceBitmap.getWidth() / 2,
                    sourceBitmap.getWidth(),
                    sourceBitmap.getWidth()
            );
        }
        return cropBitmap;
    }

    public static String getFormatedDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        String sDate = null;
        sDate = dateFormat.format(date);
        return sDate;
    }

    public static Date getFormatedDateToDate(String sdate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(PATTERN);
        Date date = null;
        try {
            date = dateFormat.parse(sdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static Date getFormatedParseDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getTime();
    }


    @SuppressLint("SimpleDateFormat")
    public static String getCurrentHour() {

        Date fechaActual = new Date();
        DateFormat formato = new SimpleDateFormat("HH:mm");

        return formato.format(fechaActual);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDateAndHour() {

        Date fechaActual = new Date();
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formato.format(fechaActual);
    }

    public static void ShowToast(Context context, String message, int duration) {

        TextView text = new TextView(context);
        text.setText(message);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
        text.setTypeface(typeface);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(duration);
        toast.setView(text);

        toast.show();
    }

    public static boolean contactExists(Context context, String number) {
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        }
        finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }

    public static void writePhoneContact(String displayName, String number, Context cntx /*App or Activity Ctx*/) {
        Context contetx = cntx; //Application's context or Activity's context
        String strDisplayName = displayName; // Name of the Person to add
        String strNumber = number; //number of the person to add with the Contact
        System.out.println("NAME> " + strDisplayName + "    NUMBER ====>  " + strNumber);
        ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
        int contactIndex = cntProOper.size();//ContactSize

        //Newly Inserted contact
        // A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());

        //Display name will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
                .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
                .build());
        //Mobile number will be inserted in ContactsContract.Data table
        cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, contactIndex)
                .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, strNumber) // Number to be added
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
        try {
            // We will do batch operation to insert all above data
            //Contains the output of the app of a ContentProviderOperation.
            //It is sure to have exactly one of uri or count set
            ContentProviderResult[] contentProresult = null;
            contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper); //apply above data insertion into contacts lis

        } catch (RemoteException exp) {
            //logs;
        } catch (OperationApplicationException exp) {
            //logs
        }
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null)
            return false;
        if (!activeNetwork.isConnectedOrConnecting())
            return false;
        if (!activeNetwork.isAvailable())
            return false;
        return true;

    }

    /**
     * Hides the soft keyboard
     */
    public static void hideSoftKeyboard(Activity activity) {
        if(activity.getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateStringFormat(int year, int monthOfYear, int dayOfMonth) {

        GregorianCalendar fechaActual = new GregorianCalendar(year,
                monthOfYear, dayOfMonth);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        return formato.format(fechaActual.getTime());

    }
    @SuppressLint("SimpleDateFormat")
    public static String getDateTimeStringFormat(int year, int monthOfYear, int dayOfMonth, int hour, int minute) {

        GregorianCalendar fechaActual = new GregorianCalendar(year,
                monthOfYear, dayOfMonth, hour, minute);
        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formato.format(fechaActual.getTime());

    }
}

