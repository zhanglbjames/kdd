/**
* kdd99.dismiss
*  
* @author zhanglbjames@163.com
* @date 2016年12月30日
* 
*/
package kdd99.dismiss;

/**
*  C45Dismiss
* 
* @author zhanglbjames@163.com
* @date 2016年12月30日
*
*/
import weka.classifiers.trees.J48;

import kdd99.RandomDelete;

import java.io.File;
import java.util.ArrayList;
import kdd99.ClassifierWrapper;

public class C45Dismiss {
	
	public static void evaluetaTest() throws Exception{
		//创建 C45 分类器
		J48 C45 = new J48();
	
		//配置预处理器
		RandomDelete c45RandomDelete = new RandomDelete();
		
		//设置C45分类包装器
		ClassifierWrapper c45Wrapper = new ClassifierWrapper("C4.5分类器 缺省数据集");
		c45Wrapper
					//设置分类器
					.setClassifier(C45)
					//设置预处理器
					.setProcess(c45RandomDelete)
					//执行
					.evaluate();
	}
	public static ArrayList<String> classiftyTest(File testDataFile) throws Exception{
		
		//创建 C45 分类器
		J48 C45 = new J48();
		
		//配置预处理器
		RandomDelete c45RandomDelete = new RandomDelete();
		
		//设置C45分类包装器
		ClassifierWrapper c45Wrapper = new ClassifierWrapper("C4.5分类器 缺省数据集");
		return c45Wrapper
					//设置分类器
					.setClassifier(C45)
					//设置预处理器
					.setProcess(c45RandomDelete)
					//执行
					.classify(testDataFile);
	}
	public static void main(String[] args) throws Exception{
		//配置预处理器
				RandomDelete roughsetRandomDelete = new RandomDelete();
				roughsetRandomDelete .execute();
				roughsetRandomDelete.getInfoFileWriter().close();
	}

}
