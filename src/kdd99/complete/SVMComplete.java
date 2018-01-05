/**
* kdd99.complete
*  
* @author zhanglbjames@163.com
* @date 2016年12月30日
* 
*/
package kdd99.complete;

/**
*  SVMComplete
* 
* @author zhanglbjames@163.com
* @date 2016年12月30日
*
*/
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

import kdd99.Process;
import kdd99.ClassifierWrapper;

public class SVMComplete extends ClassifierWrapper{
	
	/**
	* constructor
	*
	* @param name
	*/
	public SVMComplete(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/* (非 Javadoc)
	* <p>Title: filterInstances</p>
	* <p>Description: </p>
	* @return
	* @throws Exception
	* @see kdd99.complete.ClassifierWrapper#filterInstances()
	*/
	public Instances filterInstances() throws Exception{
		FileWriter infoWriter = getProcess().getInfoFileWriter();
		Logger logger = getLogger();
		long start = System.currentTimeMillis();
		
		Instances resample = super.filterInstances();//从父类集成的采样方法
		//归一化 [-1,1]
		Normalize filter = new Normalize();
		filter.setInputFormat(resample);//一定要设置filter实例的输入格式，否则会报错
		Instances filterInstances = Filter.useFilter(resample, filter);
		
		logger.info("归一化耗时："+(System.currentTimeMillis()-start)/1000.0 +" s");
		infoWriter.write("归一化耗时："+(System.currentTimeMillis()-start)/1000.0 +" s\n");
		return filterInstances;
	}
	
	public static void evaluetaTest() throws Exception{
		
		//创建 SVM 分类器
		LibSVM SVM = new LibSVM();
		
		//配置预处理器
		Process SVMProcess = new Process();
		
		//设置SVM分类包装器
		SVMComplete SVMWrapper = new SVMComplete("SVM分类器  完整数据集");
		SVMWrapper
					//设置分类器
					.setClassifier(SVM)
					//设置预处理器
					.setProcess(SVMProcess)
					//执行
					.evaluate();
	}
	public static ArrayList<String> classiftyTest(File testDataFile) throws Exception{
		
		//创建 SVM 分类器
		LibSVM SVM = new LibSVM();
		
		//配置预处理器
		Process SVMProcess = new Process();
		
		//设置SVM分类包装器
		SVMComplete SVMWrapper = new SVMComplete("SVM分类器 完整训练集");
		return SVMWrapper
					//设置分类器
					.setClassifier(SVM)
					//设置预处理器
					.setProcess(SVMProcess)
					//执行
					.classify(testDataFile);
	}
	

}
