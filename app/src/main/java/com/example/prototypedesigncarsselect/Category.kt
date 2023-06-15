package com.example.prototypedesigncarsselect

class ModelCategory {
    //variables
    var id:String=""
    var category:String=""
    var timeStamp:Long=0
    var uid:String=""

    //empty constructor
    constructor()

    //parameterized constructor
    constructor(id: String, category: String, timeStamp: Long, uid: String) {
        this.id = id
        this.category = category
        this.timeStamp = timeStamp
        this.uid = uid
    }



}