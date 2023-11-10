package com.zlin.smartrefresh.config

class BallInfoConfig {
    var ballRadius = 0f
    var ballHgap = 0f
    var ballVgap = 0f
    var drawableWidth = 0f
    var drawableHeight = 0f

    constructor() {}

    constructor(ballRadius: Float, ballHgap: Float, ballVgap: Float, drawableWidth: Float, drawableHeight: Float) {
        this.ballRadius = ballRadius
        this.ballHgap = ballHgap
        this.ballVgap = ballVgap
        this.drawableWidth = drawableWidth
        this.drawableHeight = drawableHeight
    }

}