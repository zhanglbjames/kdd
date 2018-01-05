/**
* kdd99.dismiss
*  
* @author zhanglbjames@163.com
* @date 2016年12月29日
* 
*/
package kdd99.dismiss;

import java.io.File;
import java.util.ArrayList;

import kdd99.RandomDelete;
import kdd99.roughset.RoughSet;

/**
*  RoughSetDismiss
* 
* @author zhanglbjames@163.com
* @date 2016年12月29日
*
*/
public class RoughSetDismiss {

	public static void evaluetaTest() throws Exception{
		
		//配置预处理器
		RandomDelete roughsetRandomDelete = new RandomDelete();
		roughsetRandomDelete
				  //设置weka、roughtset输入的文件头信息
				  .setHeadInfo(true, RandomDelete.Path+"/headInfoFile/kdd99_roughset.head", "END")
				  
				  //生成的标准格式的文件信息以及分隔符
				  .setOutputFilePath(RandomDelete.Path+"/train/kdd99_roughsetMiss.tab")
				  .setOutputSplitSymbol(" ")
				  
				  //设置分类信息写入器
				  .setOutFileWriter(RandomDelete.Path+"/outInfo/roughsetMiss.info");
		
		RoughSet roughset = new RoughSet(roughsetRandomDelete,"Rough set 缺省数据");
		roughset.crossValidation();
	}
	public static ArrayList<String> classiftyTest(File testDataFile) throws Exception{
		
		//配置预处理器
		RandomDelete roughsetRandomDelete = new RandomDelete();
		roughsetRandomDelete
				//设置weka、roughtset输入的文件头信息
		  		.setHeadInfo(true, RandomDelete.Path+"/headInfoFile/kdd99_roughset.head", "END")
		  
		  		//生成的标准格式的文件信息以及分隔符
		  		.setOutputFilePath(RandomDelete.Path+"/train/kdd99_roughsetMiss.tab")
		  		.setOutputSplitSymbol(" ")
		  
		  		//设置分类信息写入器
		  		.setOutFileWriter(RandomDelete.Path+"/outInfo/roughsetMiss.info");
		
		RoughSet roughset = new RoughSet(roughsetRandomDelete,"Rough set 缺省数据");
		return roughset.classify(testDataFile);
	}
	public static void main(String[] args) throws Exception{
		//配置预处理器
				RandomDelete roughsetRandomDelete = new RandomDelete();
				roughsetRandomDelete
						  //设置weka、roughtset输入的文件头信息
						  .setHeadInfo(true, RandomDelete.Path+"/headInfoFile/kdd99_roughset.head", "END")
						  
						  //生成的标准格式的文件信息以及分隔符
						  .setOutputFilePath(RandomDelete.Path+"/train/kdd99_roughsetMiss95.tab")
						  .setOutputSplitSymbol(" ")
						  
						  //设置分类信息写入器
						  .setOutFileWriter(RandomDelete.Path+"/outInfo/roughsetMiss.info")
						  .execute();
				roughsetRandomDelete.getInfoFileWriter().close();
	}
	

}
