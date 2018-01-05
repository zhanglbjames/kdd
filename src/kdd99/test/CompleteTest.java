/**
* kdd99.test
*  
* @author zhanglbjames@163.com
* @date 2017年1月6日
* 
*/
package kdd99.test;

/**
*  CompleteTest
* 
* @author zhanglbjames@163.com
* @date 2017年1月6日
*
*/
import kdd99.Process;
import kdd99.complete.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

public class CompleteTest {
	private static boolean C45eval = true;
	private static boolean SVMeval = true;
	private static boolean RoughSeteval = true;
	private static boolean C45classify = true;
	private static boolean SVMclassify = true;
	private static boolean RoughSetclassify = true;
	private static String Path = Process.Path;
	//初始配置
	static{
		init();
	}
	public static void init(){
		Properties Setting = new Properties();
		try {
			Setting.load(new FileInputStream(Path+"/Setting.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(Setting.getProperty("C45CompleteEval").equals("true")){
			C45eval = true;
		}else if(Setting.getProperty("C45CompleteEval").equals("false")){
			C45eval = false;
		}
		if(Setting.getProperty("SVMCompleteEval").equals("true")){
			SVMeval = true;
		}else if(Setting.getProperty("SVMCompleteEval").equals("false")){
			SVMeval = false;
		}
		if(Setting.getProperty("RoughSetCompleteEval").equals("true")){
			RoughSeteval = true;
		}else if(Setting.getProperty("RoughSetCompleteEval").equals("false")){
			RoughSeteval = false;
		}
		if(Setting.getProperty("C45CompleteClassify").equals("true")){
			C45classify = true;
		}else if(Setting.getProperty("C45CompleteClassify").equals("false")){
			C45classify = false;
		}
		if(Setting.getProperty("SVMCompleteClassify").equals("true")){
			SVMclassify = true;
		}else if(Setting.getProperty("SVMCompleteClassify").equals("false")){
			SVMclassify = false;
		}
		if(Setting.getProperty("RoughSetCompleteClassify").equals("true")){
			RoughSetclassify = true;
		}else if(Setting.getProperty("RoughSetCompleteClassify").equals("false")){
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
			C45Complete.evaluetaTest();
		}

		if(SVMeval){
			//SVM
			SVMComplete.evaluetaTest();
		}
		
		if(RoughSeteval){
			//RoughSet
			RoughSetComplete.evaluetaTest();
		}
	}
	
	/**
	* classify : 对新数据进行分类
	*  
	* @param files 输入的测试文件 ，weka roughset
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
		    C45Results = C45Complete.classiftyTest(files[0]);
		}
		
		if(SVMclassify){
			 //SVM
		   SVMResults = SVMComplete.classiftyTest(files[0]);
		  
		}
		
		if(RoughSetclassify){
			//RoughSet
		    RSResults = RoughSetComplete.classiftyTest(files[1]);
		    System.out.println("hhhh");
		}
		
		if(C45classify && SVMclassify){
			 //表头信息
		    combineResults.add("C4.5,SVM,roughset".split(","));
		    for(int i =0;i<C45Results.size();i++){
		    	String[] oneResult = new String[]{C45Results.get(i),SVMResults.get(i),"notmatch"};
		    	combineResults.add(oneResult);
		    }
		    Process  combineProcess = new Process();
		    combineProcess
		    	.setOutputFilePath(Path+"/test/result/completeTest.results")
		        .setHeadInfo(false, "", "");
		    
		    //写入文件
		    combineProcess.Arr2File(combineResults);
		    //关闭资源
		    combineProcess.getInfoFileWriter().close();
		}
	}
}
