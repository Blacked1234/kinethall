package com.example.kinethall.Model

class Comment {

    private var comment: String = ""
    private var publisher: String = ""

    constructor()

    constructor(comment: String, publisher: String) {
        this.comment = comment
        this.publisher = publisher
    }

    fun getPublisher(): String{
        return publisher
    }

    fun getComment(): String{
        return comment
    }

    fun setComment(postid: String)
    {
        this.comment = comment
    }

    fun setPublisher(publisher: String)
    {
        this.publisher = publisher
    }

}
