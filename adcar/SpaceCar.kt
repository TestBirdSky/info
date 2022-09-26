package com.geg.greencar.adcar

/**
 * Date：2022/9/14
 * Describe:
 */
enum class SpaceCar (val carName:String,var isLoading:Boolean,val adWrapCar: AdWrapCar){
    SPACE_OPEN("open",false,AdWrapCar()),
    SPACE_HOME("home",false,AdWrapCar()),
    SPACE_RESULT("result",false,AdWrapCar()),
    SPACE_CONNECTION("connection",false,AdWrapCar()),
    SPACE_BACK("back",false,AdWrapCar()),
}