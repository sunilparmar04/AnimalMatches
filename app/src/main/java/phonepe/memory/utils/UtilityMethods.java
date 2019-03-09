package phonepe.memory.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import phonepe.memory.R;

public class UtilityMethods {
    private static UtilityMethods mInstance;

    public static synchronized UtilityMethods getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UtilityMethods();
        }
        return mInstance;
    }

    public List getImageList() {
        List list = new ArrayList();
        list.add(R.drawable.animal_1);

        list.add(R.drawable.animal_2);
        list.add(R.drawable.animal_3);
        list.add(R.drawable.animal_4);
        list.add(R.drawable.animal_6);
        list.add(R.drawable.animal_8);

        return list;
    }
}
