/**
* kdd99.complete
*  
* @author zhanglbjames@163.com
* @date 2016年12月30日
* 
*/
package kdd99.complete;

/**
*  C45Complete
* 
* @author zhanglbjames@163.com
* @date 2016年12月30日
*
*/

import weka.classifiers.trees.J48;

import kdd99.Process;

import java.io.File;
import java.util.ArrayList;

import kdd99.ClassifierWrapper;

public class C45Complete {
	 
	public static void evaluetaTest() throws Exception{
		
		//创建 C45 分类器
		J48 C45 = new J48();
		
		//配置预处理器
		Process c45Process = new Process();
		
		//设置C45分类包装器
		ClassifierWrapper c45Wrapper = new ClassifierWrapper("C4.5分类器 完整数据集");
		c45Wrapper
					//设置分类器
					.setClassifier(C45)
					//设置预处理器
					.setProcess(c45Process)
					//执行
					.evaluate();
	}
	public static ArrayList<String> classiftyTest(File testDataFile) throws Exception{
		//创建 C45 分类器
		J48 C45 = new J48();
				
		//配置预处理器
		Process c45Process = new Process();
				
		//设置C45分类包装器
		ClassifierWrapper c45Wrapper = new ClassifierWrapper("C4.5分类器 完整训练集");
		return c45Wrapper
				//设置分类器
				.setClassifier(C45)
				//设置预处理器
				.setProcess(c45Process)
				//执行
			    .classify(testDataFile);
	}

}
