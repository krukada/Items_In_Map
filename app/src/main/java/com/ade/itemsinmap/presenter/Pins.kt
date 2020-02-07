package com.ade.itemsinmap.presenter

import android.os.Parcel
import android.os.Parcelable

 class Pins(
    val id:Int = 0,
    val service: String? = "",
    val coordinates: Coordinates,
    var visible:Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        Coordinates( parcel.readDouble(), parcel.readDouble()),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(service)
        parcel.writeDouble(coordinates.lat)
        parcel.writeDouble(coordinates.lng)
        parcel.writeByte(if (visible) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pins> {
        override fun createFromParcel(parcel: Parcel): Pins {
            return Pins(parcel)
        }

        override fun newArray(size: Int): Array<Pins?> {
            return arrayOfNulls(size)
        }
    }
}