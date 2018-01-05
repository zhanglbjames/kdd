/**
* kdd99.test
*  
* @author zhanglbjames@163.com
* @date 2017年1月6日
* 
*/
package kdd99.test;

import java.io.File;
import kdd99.TestDataProcess;

/**
*  GenerateTestData
* 
* @author zhanglbjames@163.com
* @date 2017年1月6日
*
*/
public class GenerateTestData {
	
	/**
	* wekaData : 产生weka格式的测试数据文件
	*  
	* @parms
	* @return File 
	* @throws
	*/
	public static File wekaData() throws Exception{
		TestDataProcess testdataWekaProcess = new TestDataProcess();
		File wekaDataFile = testdataWekaProcess.execute();
		return wekaDataFile;
	}
	
	/**
	* RoughSetData : RoughSet 格式测试数据文件
	*  
	* @parms
	* @return File 
	* @throws
	*/
	public static File RoughSetData() throws Exception{
		
		TestDataProcess testdataRSProcess = new TestDataProcess();
		testdataRSProcess
		  //设置weka、roughtset输入的文件头信息
		  .setHeadInfo(true, TestDataProcess.Path+"/test/head/kdd99_roughset_test.head", "END")
		 
		  //生成的标准格式的文件信息以及分隔符
		  .setOutputFilePath(TestDataProcess.Path+"/test/kdd99_roughset_test.tab")
		  .setOutputSplitSymbol(" ")
		  
		  //设置分类信息写入器
		  .setOutFileWriter(TestDataProcess.Path+"/outInfo/roughset_test.info");
		
		File RSDataFile = testdataRSProcess.execute();
		return RSDataFile;
	}
	public static void main(String[] args) throws Exception{
		RoughSetData();
	}
}
