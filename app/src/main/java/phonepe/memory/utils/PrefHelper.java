package phonepe.memory.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefHelper {
    private static PrefHelper mInstance;
    private SharedPreferences mPref;


    public static synchronized PrefHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PrefHelper(context);
        }
        return mInstance;
    }

    public PrefHelper(Context context) {
        mPref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setScore(int score){
        mPref.edit().putInt(Constants.SCORE,score).commit();
    }

    public int getScore(){
       return mPref.getInt(Constants.SCORE,0);
    }
}
