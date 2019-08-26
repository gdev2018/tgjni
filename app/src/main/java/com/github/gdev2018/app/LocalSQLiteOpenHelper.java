/*
 * This is the source code of Powerlabor (TM) for Android
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Livesey Inc. (c) 2007-2019.
 */

package com.github.gdev2018.app;

import com.github.gdev2018.master.BaseBuildVars;
import com.github.gdev2018.master.DispatchQueue;
import com.github.gdev2018.master.FileLog;
import com.github.gdev2018.master.SQLite.SQLiteDatabase;
import com.github.gdev2018.master.di.BaseApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LocalSQLiteOpenHelper {

    private DispatchQueue storageQueue = new DispatchQueue("storageQueue");
    private SQLiteDatabase database;
    private File cacheFile;
    private File walCacheFile;
    private File shmCacheFile;

    private static volatile LocalSQLiteOpenHelper Instance[] = new LocalSQLiteOpenHelper[1];
    private final static int LAST_DB_VERSION = 10;

    public static LocalSQLiteOpenHelper getInstance(int num) {
        LocalSQLiteOpenHelper localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (LocalSQLiteOpenHelper.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    Instance[num] = localInstance = new LocalSQLiteOpenHelper(num);
                }
            }
        }
        return localInstance;
    }

    public LocalSQLiteOpenHelper(int instance) {
        //storageQueue.setPriority(Thread.MAX_PRIORITY);
        storageQueue.postRunnable(() -> openDatabase(1));
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public DispatchQueue getStorageQueue() {
        return storageQueue;
    }

    // not need since librot13.so links not in java, but in sqlite3.c by SELECT load_extension ('librot13', 'sqlite3_rot_init')
    static {
//        System.loadLibrary("rot13");      // file name - librot13.so
//        System.loadLibrary("sqlitemd5");  // file name - libsqlitemd5.so
    }

    public void openDatabase(int openTries) {
        if (BaseBuildVars.LOGS_ENABLED) {
            FileLog.d("LocalSQLiteOpenHelper.openDatabase - start");
        }

        File filesDir = BaseApplication.getFilesDirFixed();
//        if (currentAccount != 0) {
//            filesDir = new File(filesDir, "account" + currentAccount + "/");
//            filesDir.mkdirs();
//        }
        cacheFile = new File(filesDir, "cache4.db");
        walCacheFile = new File(filesDir, "cache4.db-wal");
        shmCacheFile = new File(filesDir, "cache4.db-shm");

        boolean createTable = false;
//        cacheFile.delete(); // TODO: 2019-08-14 comment this row
        if (!cacheFile.exists()) {
            createTable = true;
        }

        try {
            // открыли БД

            database = new SQLiteDatabase(cacheFile.getPath());
            database.executeFast("PRAGMA secure_delete = ON").stepThis().dispose();
            database.executeFast("PRAGMA temp_store = 1").stepThis().dispose();
            database.executeFast("PRAGMA journal_mode = WAL").stepThis().dispose();

            database.enableloadextension(1);
//            database.executeFast("SELECT sqlite3_enable_load_extension()").stepThis().dispose();
            // 1/done - sqlite3_enable_load_extension
            // 2/done - sqlitemd5
			// 3 - 5. Persistent Loadable Extensions
			// 4 - 6. Statically Linking A Run-Time Loadable Extension
            // 5 - v_Steps
            // 6 - libsqlitemd5 in libtmessages.29.so
            // 7 - Java_com_github_gdev2018_master_SQLite_SQLiteDatabase_enableloadextension in myWrapper.cpp
            // 10 - do it on SQLite ODBC
            // 11 - SQLite ODBC: тестировать, н-р, rtrim, вроде там уже есть extension-functions.c https://www.sqlite.org/contrib
            // 12 - SQLite ODBC: пробовать положить libsqlitemd5.so в корневую папку и вызвать SELECT load_extension ('libsqlitemd5', 'sqlite3_sqlitemd_init')"

            database.executeFast("SELECT load_extension ('librot13', 'sqlite3_rot_init')").stepThis().dispose();
            database.executeFast("SELECT load_extension ('libsqlitemd5', 'sqlite3_sqlitemd_init')").stepThis().dispose();
//            database.executeFast(".load libsqlitemd5.so").stepThis().dispose();


            if (createTable) {
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("create new database. db path = " + cacheFile.getPath());
                }

                // clone from template
                copyDatabase(cacheFile.getPath());

//                database.executeFast("CREATE TABLE messages_holes(uid INTEGER, start INTEGER, end INTEGER, PRIMARY KEY(uid, start));").stepThis().dispose();
//                database.executeFast("CREATE INDEX IF NOT EXISTS uid_end_messages_holes ON messages_holes(uid, end);").stepThis().dispose();

                //version
                database.executeFast("PRAGMA user_version = " + LAST_DB_VERSION).stepThis().dispose();

            } else {
                int version = database.executeInt("PRAGMA user_version");
                if (BaseBuildVars.LOGS_ENABLED) {
                    FileLog.d("current db version = " + version);
                }
                if (version == 0) {
                    throw new Exception("malformed");
                }

            }
        } catch (Exception e) {
            FileLog.e(e);
        }

        FileLog.d("LocalSQLiteOpenHelper.openDatabase - finish");
    }


    private void cleanupInternal(boolean deleteFiles) {
        if (database != null) {
            database.close();
            database = null;
        }
        if (deleteFiles) {
            if (cacheFile != null) {
                cacheFile.delete();
                cacheFile = null;
            }
            if (walCacheFile != null) {
                walCacheFile.delete();
                walCacheFile = null;
            }
            if (shmCacheFile != null) {
                shmCacheFile.delete();
                shmCacheFile = null;
            }
        }
    }


    public void cleanup(final boolean isLogin) {
        if (!isLogin) {
            storageQueue.cleanupQueue();
        }
        storageQueue.postRunnable(() -> {
            cleanupInternal(true);
            openDatabase(1);
            if (isLogin) {
//                Utilities.stageQueue.postRunnable(() -> getStepsFastController().getDifference());
            }
        });
    }


    // Имя файла с базой-шаблоном
    private static final String DB_TEMPLATE_NAME = "test.db";   //2018-01-22| don't save scheme changes (all changes have to do in DB_NAME)


    public void copyDatabase(String fileNameTo) throws IOException {
        copyDatabase(DB_TEMPLATE_NAME, fileNameTo);
    }

    /**
     * Метод побайтного копирования файла
     *
     * @param fileFrom_NameInAssets шаблон БД должен лежать в директории assets
     * @param fileTo_PathName Путь к уже созданной пустой базе в андроиде (DB_PATH + DB_NAME)
     * @throws IOException
     */
    public void copyDatabase(String fileFrom_NameInAssets,  String fileTo_PathName) throws IOException {
        if (BaseBuildVars.LOGS_ENABLED) {
            FileLog.d("LocalSQLiteOpenHelper.copyDataBase - start");
        }

        // Открываем поток для чтения из уже созданной нами БД
        InputStream externalDbStream = TestApplication.mApplicationContext.getAssets().open(fileFrom_NameInAssets);

        // Теперь создадим поток для записи в эту БД побайтно
        OutputStream localDbStream = new FileOutputStream(fileTo_PathName);

        // Собственно, копирование
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        // закроем потоки
        localDbStream.flush();
        localDbStream.close();
        externalDbStream.close();

        if (BaseBuildVars.LOGS_ENABLED) {
            FileLog.d("LocalSQLiteOpenHelper.copyDataBase - finish");
        }
    }



}