#include "sqlite/sqlite3.h"
#include "jni.h"

extern "C" {

    jint Java_com_github_gdev2018_master_SQLite_SQLiteDatabase_enableloadextension(JNIEnv *env, jobject object, jlong sqliteHandle, jint value) {
        sqlite3 *handle = (sqlite3 *) (intptr_t) sqliteHandle;
        return sqlite3_enable_load_extension(handle, value);
    }
}
