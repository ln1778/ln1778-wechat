package com.ln1778.wechat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.util.UriUtil;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.qq.wx.voice.recognizer.VoiceRecognizer;
import com.qq.wx.voice.recognizer.VoiceRecognizerListener;
import com.qq.wx.voice.recognizer.VoiceRecordState;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelbiz.WXOpenCustomerServiceChat;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXFileObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by tdzl2_000 on 2015-10-10.
 */
public class WeChatVoice extends ReactContextBaseJavaModule implements VoiceRecognizerListener {
    private String appId;

    private IWXAPI api = null;
    private final static String NOT_REGISTERED = "registerApp required.";
    private final static String INVOKE_FAILED = "WeChat API invoke returns false.";
    private final static String INVALID_ARGUMENT = "invalid argument.";
    private String voiceresult="";
    private Callback voiceback=null;
    private ReactContext mcontext;
    public WeChatVoice(ReactApplicationContext context) {
        super(context);
        this.mcontext=context;
    }

    @Override
    public String getName() {
        return "Voice";
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        this.getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
    
    public boolean canOverrideExistingModule() {
        return true;
    }

    private static ArrayList<WeChatVoice> modules = new ArrayList<>();

    @Override
    public void initialize() {
        super.initialize();
        modules.add(this);
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        if (api != null) {
            api = null;
        }
        modules.remove(this);
    }
    @Override
    public void onGetError(int arg0) {

    }

    @Override
    public void onGetVoicePackage(byte[] bytes, String s) {

    }

    @Override
    public void onGetResult(VoiceRecognizerResult result) {
// TODO Auto-generated method stub
        String res = "";
        if (result != null && result.words != null) {
            int wordSize = result.words.size();
            StringBuilder results = new StringBuilder();
            for (int i = 0; i<wordSize; ++i) {
                VoiceRecognizerResult.Word word = (VoiceRecognizerResult.Word) result.
                        words.get(i);
                if (word != null && word.text != null){
                    results.append("\r\n");
                    results.append(word.text.replace(" ", ""));
                }
            }
            results.append("\r\n");
            res = results.toString();
            WritableMap params = Arguments.createMap();
            params.putString("result", res);

            sendEvent(this.mcontext, "voiceback", params);
        }
    }
    @Override
    public void onGetVoiceRecordState(VoiceRecordState state) {
// TODO Auto-generated method stub
    }
    @Override
    public void onVolumeChanged(int arg0) {

    }

    @ReactMethod
    public void initVoice(String appId,Callback callback) {
       this.appId=appId;
        VoiceRecognizer.shareInstance().setSilentTime(1500);
        VoiceRecognizer.shareInstance().setListener((VoiceRecognizerListener)this);
        if (VoiceRecognizer.shareInstance().init(this.mcontext, this.appId) != 0) {
            callback.invoke("false");
        }
        callback.invoke("true");
    }

    @ReactMethod
    public void VoiceDestroy() {
        VoiceRecognizer.shareInstance().destroy();
    }
    @ReactMethod
    public void startVoice() {
        VoiceRecognizer.shareInstance().start();
    }
    @ReactMethod
    public void stopVoice() {
        VoiceRecognizer.shareInstance().stop();
    }
    @ReactMethod
    public void cancelVoice() {
        VoiceRecognizer.shareInstance().cancel();
    }

}
