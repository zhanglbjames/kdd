/**
* kdd99
*  
* @author zhanglbjames@163.com
* @date 2016年12月30日
* 
*/
package kdd99;

import java.io.File;
import java.io.FileInputStream;
/**
*  ClassifierWrapper 分类包装器
* 
* @author zhanglbjames@163.com
* @date 2016年12月30日
*
*/
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.Resample;
import weka.classifiers.Evaluation;


public class ClassifierWrapper {
	
	//类别信息
	private static final String DOS = "dos";
	private static final String U2R = "u2r";
	private static final String R2L = "r2l";
	private static final String PROBE = "probe";
	private static final String NORMAL = "normal";
	
	private Logger logger;
	private Process processor;
	Classifier classifier;
	private Instances m_instances;
	private String name = "classifierWrapper";
	private String ResampleRadio = "10";
	
	
	/**
	* constructor 无参构造函数
	*
	*/
	public ClassifierWrapper(String name){
		this.logger = Logger.getLogger("kdd99.ClassifierWrapper");
		this.name = name;
	}
	/**
	* getName : 获取分类器的名称
	*  
	* @parms
	* @return String 
	* @throws
	*/
	public String getName(){
		return this.name;
	}
	/**
	* setLogger :设置日志记录器
	*  
	* @parms
	* @return void 
	* @throws
	*/
	public void setLogger(String loggerName){
		this.logger = Logger.getLogger(loggerName);
	}
	
	/**
	* getLogger :获取日志记录器
	*  
	* @parms
	* @return Logger 
	* @throws
	*/
	public Logger getLogger(){
		return this.logger;
	}
	/**
	* setProcess : 设置预处理器
	*  
	* @parms
	* @return ClassifierWrapper 
	* @throws
	*/
	public ClassifierWrapper setProcess(Process process){
		this.processor = process;
		this.logger = Logger.getLogger("kdd99.ClassifierWrapper");
		Properties Setting = new Properties();
		try {
			Setting.load(new FileInputStream(Process.Path+"/Setting.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		ResampleRadio = Setting.getProperty("ResampleRadio");
		return this;
	}
	
	/**
	* getProcess : 获取预处理器
	*  
	* @parms
	* @return Process 
	* @throws
	*/
	public Process getProcess(){
		return processor;
	}
	/**
	* setClassifier : 设置分类器
	*  
	* @parms
	* @return ClassifierWrapper 
	* @throws
	*/
	public ClassifierWrapper setClassifier(Classifier classifier){
		this.classifier = classifier;
		return this;
	}

	
	/**
	* setInstances : 加载Instances
	*  
	* @parms
	* @return void
	* @throws
	*/
	public void loadInstances() throws Exception{
		Instances data = new Instances(new FileReader(processor.execute()));
		logger.info("loadding instances.");
		data.setClassIndex(data.numAttributes()-1);
		this.m_instances = data;
	}
	/**
	* getInstances : 获取Instances
	*  
	* @parms
	* @return Instances 
	* @throws
	*/
	public Instances getInstances(){
		return m_instances;
	}
	/**
	* filterInstances : 对Instances进行属性和属性值处理
	*  
	* @parms
	* @return Instances 处理后的Instances
	* @throws
	*/
	public Instances filterInstances() throws Exception{
		
		Resample resample = new Resample();
		String[] options = {"-Z",ResampleRadio};//采样率为10%
		//设置
		resample.setOptions(options);
		resample.setInputFormat(getInstances());//一定要设置filter实例的输入格式，否则会报错
		
		Instances filterInstances = Filter.useFilter(getInstances(), resample);
		logger.info("filter instances");
		return filterInstances;
	}
	
	/**
	* evaluate : 评估：10折交叉验证
	*  
	* @parms
	* @return void 
	* @throws
	*/
	public void evaluate() throws Exception{
		FileWriter infoWriter = processor.getInfoFileWriter();
		
		loadInstances();//从文件加载Instances
		Instances filterInstances = filterInstances();//对加载的Instances进行过滤处理
		
		logger.info("building classifier");
		long start = System.currentTimeMillis();
		Evaluation eval = new Evaluation(filterInstances);
		eval.crossValidateModel(classifier,filterInstances,10,new Random(1));
		
		//输出控制
		logger.info("10折交叉验证时间"+ (System.currentTimeMillis()-start)/1000.0 +"\n");
		logger.info(eval.toClassDetailsString("\n类别准确率的详细情况")+"\n");
		logger.info(eval.toSummaryString("概要",true)+"\n");
		logger.info(eval.toMatrixString("分类的混合矩阵")+"\n");
		
		//记录信息
		infoWriter.write(name+"\n");
		infoWriter.write("采样率: "+ResampleRadio+"%\n");
		infoWriter.write("10折交叉验证时间"+ (System.currentTimeMillis()-start)/1000.0 +" s\n");
		infoWriter.write(eval.toClassDetailsString("\n类别准确率的详细情况")+"\n");
		infoWriter.write(eval.toSummaryString("概要",true)+"\n");
		infoWriter.write(eval.toMatrixString("分类的混合矩阵")+"\n");
		
		//关闭信息写入器
		infoWriter.close();
	}
	/**
	* trainForClassify : 为接下来的测试训练
	*  
	* @parms
	* @return void 
	* @throws
	*/
	private void trainForClassify() throws Exception{
        
		FileWriter infoWriter = processor.getInfoFileWriter();
		
		loadInstances();//从文件加载Instances
		Instances filterInstances = filterInstances();//对加载的Instances进行过滤处理
		
		logger.info("building classifier");
		long start = System.currentTimeMillis();
		
		//训练分类器
		classifier.buildClassifier(filterInstances);
		
		logger.info("building classifier finished!");
		
		infoWriter.write(name+" 分类器训练与分类\n");
		infoWriter.write("分类器训练耗时："+ (System.currentTimeMillis()-start)/1000.0 +" s\n");
		infoWriter.write("训练完成待分类测试\n");
		
		//关闭信息写入器
		infoWriter.close();
	}
	
	private String lableTypeConvert(double lable){
		int index = (int) lable;
		switch(index){
		case 0:return "("+DOS+")";
		case 1:return "("+U2R+")";
		case 2:return "("+R2L+")";
		case 3:return "("+PROBE+")";
		case 4:return "("+NORMAL+")";
		default:return "(NON TYPE)";
		}
		
	}
	/**
	* classify : 对待分类的数据分类
	*  
	* @param inputTestFile weka格式的输出待测试文件
	* @param ResultFile 结果保存的文件路径字符串
	* @return ArrayList<String> 结果列表，待写入文件 
	* @throws
	*/
	public ArrayList<String> classify(File inputTestFile) throws Exception{
		//测试实例
		Instances testdata = new Instances(new FileReader(inputTestFile));
		//新加载的数据必须设定
		testdata.setClassIndex(testdata.numAttributes()-1);
		//测试结果
		ArrayList<String> results = new ArrayList<String>();
        
        //训练数据
        trainForClassify();
        
        //分类
		for(Instance data : testdata){
			String result = lableTypeConvert(classifier.classifyInstance(data));
			logger.info(result);
			results.add(result);
		}
		return results;
	}
}
