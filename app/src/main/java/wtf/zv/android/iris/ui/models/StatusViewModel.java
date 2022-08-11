package wtf.zv.android.iris.ui.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import wtf.zv.android.iris.base.service.state.ServiceState;
import wtf.zv.android.iris.base.service.state.internal.ThreadSafeServiceStateBuffer;
import wtf.zv.android.iris.helpers.sink.ThreadSafeBuffer;
import wtf.zv.android.iris.ui.util.CachedMutableLiveData;

@HiltViewModel
public class StatusViewModel extends ViewModel {
    private final CachedMutableLiveData<ServiceState> serviceStateCache = new CachedMutableLiveData<>(ServiceState.DISCONNECTED);

    @Inject
    public StatusViewModel(
            @ThreadSafeServiceStateBuffer ThreadSafeBuffer<ServiceState> statusBuffer
    ){
        statusBuffer.getBuffer().asFlux().subscribe(serviceStateCache::postValue);
    }

    public MutableLiveData<ServiceState> getServiceState() {
        return serviceStateCache.getMutableLiveData();
    }
}
