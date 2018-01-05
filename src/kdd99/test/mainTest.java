/**
* kdd99.test
*  
* @author zhanglbjames@163.com
* @date 2017年1月6日
* 
*/
package kdd99.test;

import java.io.File;

/**
*  mainTest
* 
* @author zhanglbjames@163.com
* @date 2017年1月6日
*
*/

public class mainTest {
	
	/**
	* evaluate : 10折交叉验证
	*  
	* @parms
	* @return void 
	* @throws
	*/
	public static void evaluate() throws Exception{
		CompleteTest.evaluate();
		DismissTest.evaluate();
		
	}
	
	/**
	* classify : 分类
	*  
	* @parms
	* @return void 
	* @throws
	*/
	public static void classify() throws Exception{
		//生成测试文件
		/**
		 * 注意不管如何这两个文件都是建立的
		 * */
		File wekaTest = GenerateTestData.wekaData();
		File rsTest = GenerateTestData.RoughSetData();
		File[] testfiles = new File[]{wekaTest,rsTest};
		CompleteTest.classify(testfiles);
		DismissTest.classify(testfiles);
	}
	
	
	public static void main(String[] args) throws Exception{
		evaluate();
		classify();
		
		System.out.println("全部完成");
	}

}
