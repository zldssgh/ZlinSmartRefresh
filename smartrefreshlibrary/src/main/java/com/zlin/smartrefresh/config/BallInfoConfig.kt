package com.zlin.smartrefresh.config;

public class BallInfoConfig {
    private float ballRadius=0f;
    private float ballHgap=0f;
    private float ballVgap=0f;

    private float drawableWidth =0f;
    private float drawableHeight =0f;

    public BallInfoConfig() {
    }

    public BallInfoConfig(float ballRadius, float ballHgap, float ballVgap, float drawableWidth, float drawableHeight) {
        this.ballRadius = ballRadius;
        this.ballHgap = ballHgap;
        this.ballVgap = ballVgap;
        this.drawableWidth = drawableWidth;
        this.drawableHeight = drawableHeight;
    }

    public float getBallRadius() {
        return ballRadius;
    }

    public void setBallRadius(float ballRadius) {
        this.ballRadius = ballRadius;
    }

    public float getBallHgap() {
        return ballHgap;
    }

    public void setBallHgap(float ballHgap) {
        this.ballHgap = ballHgap;
    }

    public float getBallVgap() {
        return ballVgap;
    }

    public void setBallVgap(float ballVgap) {
        this.ballVgap = ballVgap;
    }

    public float getDrawableWidth() {
        return drawableWidth;
    }

    public void setDrawableWidth(float drawableWidth) {
        this.drawableWidth = drawableWidth;
    }

    public float getDrawableHeight() {
        return drawableHeight;
    }

    public void setDrawableHeight(float drawableHeight) {
        this.drawableHeight = drawableHeight;
    }

}
