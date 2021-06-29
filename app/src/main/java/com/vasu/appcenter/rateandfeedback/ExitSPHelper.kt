package com.vasu.appcenter.rateandfeedback

import android.content.Context

/**
 * ExitSPHelper.kt - A simple SharedPreferences helper class.
 * @author Jignesh N Patel
 * @date 08-11-2019
 */

class ExitSPHelper(mContext: Context) {

    // SP to be save & retrieve
    private val israted = "israted"
    private val isdismiss = "isdismiss"
    private val exitcount = "exitcount"

    private val sp: SharedPreferences

    init {
        sp = SharedPreferences(mContext)
    }

    fun saveRate() {
        sp.save(israted, true)
    }

    fun isRated(): Boolean {
        return sp.read(israted, false)
    }


    fun saveDismissed(status: Boolean) {
        sp.save(isdismiss, status)
    }

    fun isDismissed(): Boolean {
        return sp.read(isdismiss, false)
    }

    fun updateExitCount() {
        sp.save(exitcount, getExitCount() + 1)
    }

    fun getExitCount(): Int {
        return sp.read(exitcount, 0)
    }


    /*
     * ToDo..  SharedPreferences helper class
     */
    private inner class SharedPreferences//  Default constructor
    internal constructor(private val mContext: Context) {
        private val myPreferences = "exit_pref"

        //  Save Integer value
        fun save(key: String, value: Int) {
            val editor = mContext.getSharedPreferences(myPreferences, Context.MODE_MULTI_PROCESS).edit()
            editor.putInt(key, value)
            editor.apply()
        }

        //  Save boolean value
        fun save(key: String, value: Boolean) {
            val editor = mContext.getSharedPreferences(myPreferences, 0).edit()
            editor.putBoolean(key, value)
            editor.apply()
        }

        //  Read Integer value
        fun read(key: String, defValue: Int): Int {
            return mContext.getSharedPreferences(myPreferences, 0).getInt(key, defValue)
        }

        //  Read Boolean value
        fun read(key: String, defValue: Boolean): Boolean {
            return mContext.getSharedPreferences(myPreferences, 0).getBoolean(key, defValue)
        }

    }

}
