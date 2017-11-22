package org.enmets;

import org.enmets.util.DataUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by luoluyao on 2017/11/21.
 */
public class MainClass {
    public static float argv = 0.02f;
    public static int iter = 1000;
    public static void main(String[] args) throws Exception{
        String trainfile = "E:\\luoluyao\\java\\bpnn\\data\\train.txt";
        String testfile = "E:\\luoluyao\\java\\bpnn\\data\\test.txt";
        String outputfile = "E:\\luoluyao\\java\\bpnn\\data\\out.txt";
        String separator = ",";
        DataUtil util = DataUtil.getInstance();
        List<DataNode> trainList = util.getDataList(trainfile, separator);
        List<DataNode> testList = util.getDataList(testfile, separator);
        BufferedWriter output = new BufferedWriter(new FileWriter(new File(
                outputfile)));
        int typeCount = util.getTypeCount();
        AnnClassifier annClassifier = new AnnClassifier(trainList.get(0).getAttribute().size(), trainList.get(0).getAttribute().size() + 8, typeCount, trainList);
        annClassifier.train(argv, iter);
        annClassifier.printWeight();
        int correctNum = 0;
        for (int i = 0; i < testList.size(); i++)
        {
            DataNode test = testList.get(i);
            int type = annClassifier.test(test);
            if(type == test.getType()) {
                correctNum++;
            }
            List<Float> attribs = test.getAttribute();
            for (int n = 0; n < attribs.size(); n++)
            {
                output.write(attribs.get(n) + ",");
                output.flush();
            }
            output.write(util.getTypeName(type) + "\n");
            output.flush();
        }
        output.close();
        System.out.println("正确数目是：" + correctNum + "正确率是：" + ((double)correctNum/testList.size()));
    }
}
