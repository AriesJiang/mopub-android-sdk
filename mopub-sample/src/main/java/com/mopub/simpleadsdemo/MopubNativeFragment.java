package com.mopub.simpleadsdemo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mopub.nativeads.AdapterHelper;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.ViewBinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MopubNativeFragment extends Fragment {

    private MoPubNative moPubNative;
    private ViewGroup parentView;

    public MopubNativeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mopub_native, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentView = view.findViewById(R.id.fl_mopub_ad_holder);
        view.findViewById(R.id.mopub_loadAd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoPubNative.MoPubNativeNetworkListener moPubNativeNetworkListener = new MoPubNative.MoPubNativeNetworkListener() {

                    @Override
                    public void onNativeLoad(NativeAd nativeAd) {
                        Log.d("MoPub", "Native ad has loaded.");
                        showAd(nativeAd);
                    }

                    @Override
                    public void onNativeFail(NativeErrorCode errorCode) {
                        Log.d("MoPub", "Native ad failed to load with error: " + errorCode.toString());
                    }
                };

                moPubNative = new MoPubNative(MopubNativeFragment.this.getContext(), "11a17b188668469fb0412708c3d16813 ", moPubNativeNetworkListener);

                ViewBinder viewBinder = new ViewBinder.Builder(R.layout.native_ad_list_item)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build();

                MoPubStaticNativeAdRenderer moPubStaticNativeAdRenderer = new MoPubStaticNativeAdRenderer(viewBinder);
                moPubNative.registerAdRenderer(moPubStaticNativeAdRenderer);

                moPubNative.makeRequest();
            }
        });
    }

    private void showAd(NativeAd nativeAd) {


        NativeAd.MoPubNativeEventListener moPubNativeEventListener = new NativeAd.MoPubNativeEventListener() {
            @Override
            public void onImpression(View view) {
                Log.d("MoPub", "Native ad recorded an impression.");
                // Impress is recorded - do what is needed AFTER the ad is visibly shown here.
            }

            @Override
            public void onClick(View view) {
                Log.d("MoPub", "Native ad recorded a click.");
                // Click tracking.
            }
        };

        AdapterHelper adapterHelper = new AdapterHelper(MopubNativeFragment.this.getContext(), 0, 3); // When standalone, any range will be fine.

        // Retrieve the pre-built ad view that AdapterHelper prepared for us.
        View v = adapterHelper.getAdView(null, parentView, nativeAd, new ViewBinder.Builder(0).build());
        // Set the native event listeners (onImpression, and onClick).
        nativeAd.setMoPubNativeEventListener(moPubNativeEventListener);
        // Add the ad view to our view hierarchy
        parentView.removeAllViews();
        parentView.addView(v);
    }
}
