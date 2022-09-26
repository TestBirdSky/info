package com.geg.greencar.adcar

/**
 * Date：2022/9/14
 * Describe:
 */
class AdWrapCar {
    var ad: Any? = null
        set(value) {
            field = value
            if (field != null) {
                time = System.currentTimeMillis()
            }
        }
        get() {
            return if (isCanUse()) field else null
        }

    private var time = 0L


    private fun isCanUse(): Boolean {
        return System.currentTimeMillis() - time < 1000 * 60 * 60
    }
}