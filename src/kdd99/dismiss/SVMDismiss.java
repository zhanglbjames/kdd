/**
* kdd99.dismiss
*  
* @author zhanglbjames@163.com
* @date 2016年12月30日
* 
*/
package kdd99.dismiss;

/**
*  SVMDismiss
* 
* @author zhanglbjames@163.com
* @date 2016年12月30日
*
*/

import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

import kdd99.RandomDelete;
import kdd99.ClassifierWrapper;

public class SVMDismiss extends ClassifierWrapper{
	
	public SVMDismiss(String name){
		super(name);
		setLogger("kdd99.dismiss.SVMDismiss");
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
		
		//缺省值填充为均值和最大频率标签值
		logger.info("Start to Complete Missing attributes.");
		ReplaceMissingValues CompleteFilter = new ReplaceMissingValues();
		CompleteFilter.setInputFormat(resample);//一定要设置filter实例的输入格式，否则会报错
		Instances completeInstances = Filter.useFilter(resample, CompleteFilter);
		
		//归一化 [-1,1]
		logger.info("Start to Standardize numeric attributes.");
		Normalize Normalizefilter = new Normalize();
		Normalizefilter.setScale(2.0);
		Normalizefilter.setScale(-1.0);
		Normalizefilter.setInputFormat(completeInstances);//一定要设置filter实例的输入格式，否则会报错
		Instances filterInstances = Filter.useFilter(completeInstances, Normalizefilter);
		
		logger.info("缺省值填充、归一化耗时："+(System.currentTimeMillis()-start)/1000.0 +" s\n");
		infoWriter.write("缺省值填充、归一化耗时："+(System.currentTimeMillis()-start)/1000.0 +" s\n");
		return filterInstances;
	}
	
    public static void evaluetaTest() throws Exception{
		
		//创建 SVM 分类器
		LibSVM SVM = new LibSVM();
		
		//配置预处理器
		RandomDelete SVMRandomDelete = new RandomDelete();
		
		//设置SVM分类包装器
		ClassifierWrapper SVMWrapper = new SVMDismiss("SVM分类器 缺省数据");
		SVMWrapper
					//设置分类器
					.setClassifier(SVM)
					//设置预处理器
					.setProcess(SVMRandomDelete)
					//执行
					.evaluate();
	}
	public static ArrayList<String> classiftyTest(File testDataFile) throws Exception{
		
		//创建 SVM 分类器
		LibSVM SVM = new LibSVM();
		
		//配置预处理器
		RandomDelete SVMRandomDelete = new RandomDelete();
		
		//设置SVM分类包装器
		ClassifierWrapper SVMWrapper = new SVMDismiss("SVM分类器 缺省数据");
		return SVMWrapper
					//设置分类器
					.setClassifier(SVM)
					//设置预处理器
					.setProcess(SVMRandomDelete)
					//执行
					.classify(testDataFile);
	}

}
