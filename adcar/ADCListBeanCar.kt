package com.geg.greencar.adcar

/**
 * Date：2022/9/14
 * Describe:
 */
data class ADCListBeanCar(
    val green_s_num:Int,
    val green_c_num:Int,
    val green_c_sp: ArrayList<AdConfigureBeanCar>,
    val green_c_m_n: ArrayList<AdConfigureBeanCar>,
    val green_c_r_n: ArrayList<AdConfigureBeanCar>,
    val green_c_m_i: ArrayList<AdConfigureBeanCar>,
    val green_c_ser_i: ArrayList<AdConfigureBeanCar>,
)
