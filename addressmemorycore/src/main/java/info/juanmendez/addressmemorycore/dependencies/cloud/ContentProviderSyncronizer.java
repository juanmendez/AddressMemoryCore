package info.juanmendez.addressmemorycore.dependencies.cloud;

import io.reactivex.Single;

/**
 * Created by juan on 1/14/18.
 *
 */

public class ContentProviderSyncronizer {
    private ContentProviderService mService;

    public ContentProviderSyncronizer(ContentProviderService contentProviderService) {
        mService = contentProviderService;
    }

    public Single<Boolean> connect(){

        mService.connect();
        return Single.create(emitter -> {

            if( !mService.getSynced()){

                /**
                 * If it requires syncing, then sync, otherwise notify false.
                 * If after syncing, there were no updates made, then notify false.
                 */
                mService.confirmRequiresSyncing(itRequires -> {

                     if( itRequires ){

                        //if provider has records, then go ahead and sync
                        mService.confirmSyncing(updates -> {
                            mService.setSynced(true);
                            emitter.onSuccess(updates>0);

                        });
                    }else{
                        //if provider has no records, then don't sync
                        mService.setSynced(true);
                        emitter.onSuccess(false);
                    }
                });
            }else{
                //previously synced, no need to wait then
                emitter.onSuccess(false);
            }
        });
    }

    public void disconnect(){
        mService.disconnect();
    }
}
