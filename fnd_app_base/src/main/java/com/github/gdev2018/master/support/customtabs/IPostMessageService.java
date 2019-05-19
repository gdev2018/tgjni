/*  * This is the source code of Telegram for Android v. 5.x.x.  * It is licensed under GNU GPL v. 2 or later.  * You should have received a copy of the license in this archive (see LICENSE).  *  * Copyright Nikolai Kudashov, 2013-2018.  */

package com.github.gdev2018.master.support.customtabs;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPostMessageService extends IInterface {

    void onMessageChannelReady(ICustomTabsCallback var1, Bundle var2) throws RemoteException;

    void onPostMessage(ICustomTabsCallback var1, String var2, Bundle var3) throws RemoteException;

    abstract class Stub extends Binder implements IPostMessageService {
        private static final String DESCRIPTOR = "android.support.customtabs.IPostMessageService";
        static final int TRANSACTION_onMessageChannelReady = 2;
        static final int TRANSACTION_onPostMessage = 3;

        public Stub() {
            this.attachInterface(this, "android.support.customtabs.IPostMessageService");
        }

        public static IPostMessageService asInterface(IBinder obj) {
            if(obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("android.support.customtabs.IPostMessageService");
                return (iin != null && iin instanceof IPostMessageService?(IPostMessageService)iin:new IPostMessageService.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ICustomTabsCallback _arg0;
            switch(code) {
                case 2:
                    data.enforceInterface("android.support.customtabs.IPostMessageService");
                    _arg0 = ICustomTabsCallback.Stub.asInterface(data.readStrongBinder());
                    Bundle _arg11;
                    if(0 != data.readInt()) {
                        _arg11 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg11 = null;
                    }

                    this.onMessageChannelReady(_arg0, _arg11);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface("android.support.customtabs.IPostMessageService");
                    _arg0 = ICustomTabsCallback.Stub.asInterface(data.readStrongBinder());
                    String _arg1 = data.readString();
                    Bundle _arg2;
                    if(0 != data.readInt()) {
                        _arg2 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }

                    this.onPostMessage(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString("android.support.customtabs.IPostMessageService");
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IPostMessageService {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "android.support.customtabs.IPostMessageService";
            }

            public void onMessageChannelReady(ICustomTabsCallback callback, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("android.support.customtabs.IPostMessageService");
                    _data.writeStrongBinder(callback != null?callback.asBinder():null);
                    if(extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void onPostMessage(ICustomTabsCallback callback, String message, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("android.support.customtabs.IPostMessageService");
                    _data.writeStrongBinder(callback != null?callback.asBinder():null);
                    _data.writeString(message);
                    if(extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }
}
