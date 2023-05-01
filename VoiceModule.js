import { DeviceEventEmitter, NativeModules, Platform } from 'react-native';
import { EventEmitter } from 'events';

let isAppRegistered = false;
const { Voice } = NativeModules;

const emitter = new EventEmitter();

DeviceEventEmitter.addListener('voiceback', resp => {
    console.log(resp,"resp");
    emitter.emit(resp.type, resp);
  });
  

   const startVoice = wrapApi(Voice.startVoice);

   const cancelVoice = wrapApi(Voice.cancelVoice);
  
   function registerApp(appid){
    return new Promise((resolve, reject) => {
      Voice.initVoice(appid,(err,state) => {
        resolve(state);
      });
    });
  }

  export function stopVoice(){
    return new Promise((resolve, reject) => {
        Voice.stopVoice();
        emitter.once('StopVoice.Resp', resp => {
          if (resp.errCode === 0) {
            resolve(resp);
          } else {
            reject(new WechatError(resp));
          }
      });
    });
  }

function wrapApi(nativeFunc) {
    if (!nativeFunc) {
      return undefined;
    }
    return (...args) => {
      if (!isAppRegistered) {
        return Promise.reject(new Error('registerApp required.'));
      }
      return new Promise((resolve, reject) => {
        nativeFunc.apply(null, [
          ...args,
          (error, result) => {
            if (!error) {
              return resolve(result);
            }
            if (typeof error === 'string') {
              return reject(new Error(error));
            }
            reject(error);
          },
        ]);
      });
    };
  }

/**
 * promises will reject with this error when API call finish with an errCode other than zero.
 */
class VoiceError extends Error {
  constructor(resp) {
    const message = resp.errStr || resp.errCode.toString();
    super(message);
    this.name = 'VoiceError';
    this.code = resp.errCode;

    // avoid babel's limition about extending Error class
    // https://github.com/babel/babel/issues/3083
    if (typeof Object.setPrototypeOf === 'function') {
      Object.setPrototypeOf(this, VoiceError.prototype);
    } else {
      this.__proto__ = VoiceError.prototype;
    }
  }
}

  const VoiceModule={registerApp,startVoice,cancelVoice,stopVoice,VoiceError};


  export default VoiceModule;