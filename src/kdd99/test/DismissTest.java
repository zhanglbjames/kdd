/**
* kdd99.test
*  
* @author zhanglbjames@163.com
* @date 2017年1月6日
* 
*/
package kdd99.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import kdd99.Process;
/**
*  DismissTest
* 
* @author zhanglbjames@163.com
* @date 2017年1月6日
*
*/
import kdd99.dismiss.*;

public class DismissTest {
	private static boolean C45eval = true;
	private static boolean SVMeval = true;
	private static boolean RoughSeteval = true;
	private static boolean C45classify = true;
	private static boolean SVMclassify = true;
	private static boolean RoughSetclassify = true;
 
	
	//初始配置
	static{
		init();
	}
	public static void init(){
		Properties Setting = new Properties();
		try {
			Setting.load(new FileInputStream(Process.Path+"/Setting.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(Setting.getProperty("C45MissEval").equals("true")){
			C45eval = true;
		}else if(Setting.getProperty("C45MissEval").equals("false")){
			C45eval = false;
		}
		if(Setting.getProperty("SVMMissEval").equals("true")){
			SVMeval = true;
		}else if(Setting.getProperty("SVMMissEval").equals("false")){
			SVMeval = false;
		}
		if(Setting.getProperty("RoughSetMissEval").equals("true")){
			RoughSeteval = true;
		}else if(Setting.getProperty("RoughSetMissEval").equals("false")){
			RoughSeteval = false;
		}
		if(Setting.getProperty("C45MissClassify").equals("true")){
			C45classify = true;
		}else if(Setting.getProperty("C45MissClassify").equals("false")){
			C45classify = false;
		}
		if(Setting.getProperty("SVMMissClassify").equals("true")){
			SVMclassify = true;
		}else if(Setting.getProperty("SVMMissClassify").equals("false")){
			SVMclassify = false;
		}
		if(Setting.getProperty("RoughSetMissClassify").equals("true")){
			RoughSetclassify = true;
		}else if(Setting.getProperty("RoughSetMissClassify").equals("false")){
			RoughSetclassify = false;
		}
	}
	
	/**
	* check : 10 折交叉验证
	*  
	* @parms
	* @return void 
	* @throws
	*/
	public static void evaluate() throws Exception{
		if(C45eval){
			//C45
			C45Dismiss.evaluetaTest();
		}

		if(SVMeval){
			//SVM
			SVMDismiss.evaluetaTest();
		}
		
		if(RoughSeteval){
			//RoughSet
			RoughSetDismiss.evaluetaTest();
		}
		
	}
	/**
	* classify : 对新的数据进行分类测试
	*  
	* @param files 输入的待测试数据文件 weka,roughset
	* @return void 
	* @throws
	*/
	public static void classify(File[] files) throws Exception{
		ArrayList<String> C45Results  = new ArrayList<String>();
		ArrayList<String> SVMResults  = new ArrayList<String>();
		ArrayList<String> RSResults  = new ArrayList<String>();
		ArrayList<String[]> combineResults = new ArrayList<String[]>();
		
		if(C45classify){
			//C45
		    C45Results = C45Dismiss.classiftyTest(files[0]);
		}
		
		if(SVMclassify){
			 //SVM
		   SVMResults = SVMDismiss.classiftyTest(files[0]);
		}
		
		if(RoughSetclassify){
			//RoughSet
		    RSResults = RoughSetDismiss.classiftyTest(files[1]);
		}
		
		if(C45classify && SVMclassify && RoughSetclassify){
			 //表头信息
		    combineResults.add("C4.5,SVM".split(","));
		    for(int i =0;i<C45Results.size();i++){
		    	String[] oneResult = new String[]{C45Results.get(i),SVMResults.get(i)};
		    	combineResults.add(oneResult);
		    }
		    Process  combineProcess = new Process();
		    combineProcess
		    	.setOutputFilePath(Process.Path+"/test/result/MissTest.results")
		        .setHeadInfo(false, "", "");
		    
		    //写入文件
		    combineProcess.Arr2File(combineResults);
		    //关闭资源
		    combineProcess.getInfoFileWriter().close();
		}
	}

}
