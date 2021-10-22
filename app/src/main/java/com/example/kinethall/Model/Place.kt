package com.example.kinethall.Model

class Place
{
    private var placeid: String = ""
    private var placeimage: String = ""
    private var description: String = ""

    constructor()

    constructor(placeid: String, placeimage: String, description: String) {
        this.placeid = placeid
        this.placeimage = placeimage
        this.description = description
    }

    fun getPlaceid(): String{
        return placeid
    }

    fun getPlaceimage(): String{
        return placeimage
    }

    fun getDescription(): String{
        return description
    }

    fun setPlaceid(placeid: String)
    {
        this.placeid = placeid
    }

    fun setPlaceimage(placeimage: String)
    {
        this.placeimage = placeimage
    }

    fun setDescription(description: String)
    {
        this.description = description
    }
}