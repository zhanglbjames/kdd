/**
* kdd99.complete
*  
* @author zhanglbjames@163.com
* @date 2016年12月30日
* 
*/
package kdd99.complete;

/**
*  RoughSetComplete
* 
* @author zhanglbjames@163.com
* @date 2016年12月30日
*
*/
import kdd99.roughset.RoughSet;
import kdd99.test.GenerateTestData;

import java.io.File;
import java.util.ArrayList;

import kdd99.Process;

public class RoughSetComplete {
	
	public static void evaluetaTest() throws Exception{
		//配置预处理器
		Process roughsetProcess = new Process();
		roughsetProcess
				  //设置weka、roughtset输入的文件头信息
				  .setHeadInfo(true, Process.Path+"/headInfoFile/kdd99_roughset.head", "END")
				  
				  //生成的标准格式的文件信息以及分隔符
				  .setOutputFilePath(Process.Path+"/train/kdd99_roughsetComplete.tab")
				  .setOutputSplitSymbol(" ")
				  
				  //设置分类信息写入器
				  .setOutFileWriter(Process.Path+"/outInfo/roughsetComplete.info");
		RoughSet roughset = new RoughSet(roughsetProcess,"粗糙集分类器 完整数据");
		roughset.crossValidation();
	}
	
	public static ArrayList<String> classiftyTest(File testDataFile) throws Exception{
		//配置预处理器
			Process roughsetProcess = new Process();
			roughsetProcess
				  //设置weka、roughtset输入的文件头信息
				  .setHeadInfo(true, Process.Path+"/headInfoFile/kdd99_roughset.head", "END")
				  
				  //生成的标准格式的文件信息以及分隔符
				  .setOutputFilePath(Process.Path+"/train/kdd99_roughsetComplete.tab")
				  .setOutputSplitSymbol(" ")
				  
				  //设置分类信息写入器
				  .setOutFileWriter(Process.Path+"/outInfo/roughsetComplete.info");
				RoughSet roughset = new RoughSet(roughsetProcess,"粗糙集分类器 完整数据");
			return	roughset.classify(testDataFile);
	}
	public static void main(String[] args) throws Exception{
		File rsTest = GenerateTestData.RoughSetData();
		classiftyTest(rsTest);
	}
}
