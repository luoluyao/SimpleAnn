package org.enmets;

/**
 * Created by luoluyao on 2017/11/21.
 */
public class NetworkNode {
    public static final int TYPE_INPUT = 0;
    public static final int TYPE_HIDDEN = 1;
    public static final int TYPE_OUTPUT = 2;

    private int type;

    private float mForwardInput;
    private float mForwardOuput;

    private float mBackInput;
    private float mBackOutput;

    public NetworkNode(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getmForwardInput() {
        return mForwardInput;
    }

    public void setmForwardInput(float mForwardInput) {
        this.mForwardInput = mForwardInput;
        setmForwardOuput(mForwardInput);
    }

    public float getmBackInput() {
        return mBackInput;
    }

    public void setmBackInput(float mBackInput) {
        this.mBackInput = mBackInput;
        setmBackOutput(mBackInput);
    }

    public float getmForwardOuput() {
        return mForwardOuput;
    }

    public void setmForwardOuput(float mForwardOuput) {

        this.mForwardOuput = forwardSigmod(mForwardOuput);
    }

    public float getmBackOutput() {
        return mBackOutput;
    }

    public void setmBackOutput(float mBackOutput) {
        this.mBackOutput = backPropagate(mBackOutput);
    }

    /**
     * tan-sigmod 函数
     *
     * @param in
     *         函数的输入x
     * @return 函数的y值
     *
     */
    private float tanhSigmod(float in) {
        return (float)((Math.exp(in) - Math.exp(-in)) / (Math.exp(in) + Math.exp(-in)));
    }

    /**
     * tan-sigmod 函数的导数
     * @param in
     *        函数输出值
     * @return
     */
    private float tanhSderivative(float in) {
        return (float) ((1 - Math.pow(mForwardOuput, 2)) * in);
    }

    /**
     * 针对不同类型的in进行判断得到输出的值
     * @param in
     * @return
     */
    private float forwardSigmod(float in) {
        switch (type) {
            case TYPE_INPUT:
                return in;
            case TYPE_HIDDEN:
            case TYPE_OUTPUT:
                return tanhSigmod(in);
        }
        return 0;
    }

    /**
     * 误差反向传播时，激活函数的导数
     * @param in
     * @return
     */
    private float backPropagate(float in) {
        switch (type) {
            case TYPE_INPUT:
                return in;
            case TYPE_HIDDEN:
            case TYPE_OUTPUT:
                return tanhSderivative(in);
        }
        return 0;
    }
}
