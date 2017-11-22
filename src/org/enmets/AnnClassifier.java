package org.enmets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by luoluyao on 2017/11/21.
 */
public class AnnClassifier {
    private int mInputCount;
    private int mOutputCount;
    private int mHiddenCount;

    private List<NetworkNode> mInputNodes;
    private List<NetworkNode> mHiddenNodes;
    private List<NetworkNode> mOutputNodes;

    private float[][] mInputHiddenWeight;
    private float[][] mHiddenOutputWeight;

    private List<DataNode> mTrainNodes;

    public AnnClassifier(int mInputCount, int mHiddenCount, int mOutputCount, List<DataNode> trainNodes) {
        this.mInputCount = mInputCount;
        this.mHiddenCount = mHiddenCount;
        this.mOutputCount = mOutputCount;
        this.mTrainNodes = trainNodes;

        mInputNodes = new ArrayList<NetworkNode>();
        mOutputNodes = new ArrayList<NetworkNode>();
        mHiddenNodes = new ArrayList<NetworkNode>();

        mInputHiddenWeight = new float[mInputCount][mHiddenCount];
        mHiddenOutputWeight = new float[mHiddenCount][mOutputCount];
    }

    private void init() {
        mInputNodes.clear();
        mHiddenNodes.clear();
        mOutputNodes.clear();

        for (int i = 0; i < mInputCount; i++) {
            mInputNodes.add(new NetworkNode(NetworkNode.TYPE_INPUT));
        }

        for (int i = 0; i < mHiddenCount; i++) {
            mHiddenNodes.add(new NetworkNode(NetworkNode.TYPE_HIDDEN));
        }

        for (int i = 0; i < mOutputCount; i++) {
            mOutputNodes.add(new NetworkNode(NetworkNode.TYPE_OUTPUT));
        }

        for (int i = 0; i < mInputCount; i++) {
            for (int j = 0; j < mHiddenCount; j++) {
                mInputHiddenWeight[i][j] = (float)(0.1 * Math.random());
            }
        }

        for (int i = 0; i < mHiddenCount; i++) {
            for (int j = 0; j < mOutputCount; j++) {
                mHiddenOutputWeight[i][j] = (float)(0.1 * Math.random());
            }
        }
    }

    /**
     * 向前传递
     * @param list
     */
    private void forward(List<Float> list) {

        //input level
        for (int i = 0; i < list.size(); i++) {
            mInputNodes.get(i).setmForwardInput(list.get(i));
        }

        // hidden level
        for (int i = 0; i < mHiddenCount; i++) {
            float tmp = 0;
            for (int j = 0; j < mInputCount; j++) {
                tmp += mInputHiddenWeight[j][i]
                        * mInputNodes.get(j).getmForwardOuput();
            }
            mHiddenNodes.get(i).setmForwardInput(tmp);
        }

        // output level
        for (int i = 0; i < mOutputCount; i++) {
            float tmp = 0;
            for (int j = 0; j < mHiddenCount; j++) {
                tmp += mHiddenOutputWeight[j][i]
                        * mHiddenNodes.get(j).getmForwardOuput();
            }
            mOutputNodes.get(i).setmForwardInput(tmp);
        }
    }

    /**
     *
     * @param type
     *         已知的分类类型
     */
    private void backward(int type) {

        // output level
        for (int i = 0; i < mOutputCount; i++) {
            float result = -1;
            if (i == type) {
                result = 1;
            }
            mOutputNodes.get(i).setmBackInput(
                    mOutputNodes.get(i).getmForwardOuput() - result);
        }

        // hidden level
        for (int i = 0; i < mHiddenCount; i++) {
            float tmp = 0;
            for (int j = 0; j < mOutputCount; j++) {
                tmp += mHiddenOutputWeight[i][j]
                        * mOutputNodes.get(j).getmBackOutput();
            }
            mHiddenNodes.get(i).setmBackInput(tmp);
        }
    }

    /**
     * 更新权重，每个权重的梯度都等于与其相连的前一层的节点输出乘以与其相连的后一层的反向传播输出
     * @param argv
     */
    private void update(float argv) {

        // update input & hidden level
        for (int i = 0; i < mInputCount; i++) {
            for (int j = 0; j < mHiddenCount; j++) {
                mInputHiddenWeight[i][j] -= argv
                        * mInputNodes.get(i).getmForwardOuput()
                        * mHiddenNodes.get(j).getmBackOutput();
             }
        }
        //printWeight();
        // update hidden & output level
        for (int i = 0; i < mHiddenCount; i++) {
            for (int j = 0; j < mOutputCount; j++) {
                mHiddenOutputWeight[i][j] -= argv
                        * mHiddenNodes.get(i).getmForwardOuput()
                        * mOutputNodes.get(j).getmBackOutput();
            }
        }

    }

    /**
     *  由训练集开始训练weight
     * @param argv
     * @param n 迭代次数
     */
    public void train(float argv, int n) {
        init();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < mTrainNodes.size(); j++) {
                forward(mTrainNodes.get(j).getAttribute());
                backward(mTrainNodes.get(j).getType());
                update(argv);
            }
        }
    }

    /**
     *
     * 测试阶段，测试未知的节点是否正确
     * @param node
     * @return type 分类的结果
     */

    public int test(DataNode node) {
        forward(node.getAttribute());
        float result = 2;
        int type = 0;
        for (int i = 0; i < mOutputCount ; i++) {
            if ((1 - mOutputNodes.get(i).getmForwardOuput()) < result) {
                result = 1 - mOutputNodes.get(i).getmForwardOuput();
                type = i;
            }
        }
        return type;
    }

    /**
     * 输出权重的结果
     */
    public void printWeight() {
        for (int i = 0; i < mInputCount; i++) {
            System.out.println(Arrays.toString(mInputHiddenWeight[i]));
        }
        for (int i = 0; i < mHiddenCount; i++) {
            System.out.println(Arrays.toString(mHiddenOutputWeight[i]));
        }
    }

}
