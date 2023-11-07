package com.zlin.smartrefresh.api.config;

public class BallInfoConfig {
    private float ballRadius=15f;
    private float ballHgap=10f;
    private float ballVgap=18f;

    private float viewWidth=0f;
    private float viewHeight=0f;

    public BallInfoConfig() {
    }

    public BallInfoConfig(float ballRadius, float ballHgap, float ballVgap, float viewWidth, float viewHeight) {
        this.ballRadius = ballRadius;
        this.ballHgap = ballHgap;
        this.ballVgap = ballVgap;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
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

    public float getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(float viewWidth) {
        this.viewWidth = viewWidth;
    }

    public float getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(float viewHeight) {
        this.viewHeight = viewHeight;
    }
}
