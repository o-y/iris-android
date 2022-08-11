package wtf.zv.android.iris.ui.util;

import androidx.lifecycle.MutableLiveData;

public class CachedMutableLiveData<T> {

    private MutableLiveData<T> mutableLiveData;
    private T optional;

    public CachedMutableLiveData(T value) {
        optional = value;
    }

    public void postValue(T value) {
        if (mutableLiveData == null) mutableLiveData = new MutableLiveData<T>(value);

        optional = value;
        mutableLiveData.postValue(value);
    }

    public void setValue(T value) {
        if (mutableLiveData == null) mutableLiveData = new MutableLiveData<T>(value);

        optional = value;
        mutableLiveData.setValue(value);
    }

    public MutableLiveData<T> getMutableLiveData(){
        if (mutableLiveData == null) mutableLiveData = new MutableLiveData<T>(optional);
        return mutableLiveData;
    }
}
