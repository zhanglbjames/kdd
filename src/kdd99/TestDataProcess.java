/**
* kdd99
*  
* @author zhanglbjames@163.com
* @date 2017年1月6日
* 
*/
package kdd99;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
*  TestDataProcess
* 
* @author zhanglbjames@163.com
* @date 2017年1月6日
*
*/
public class TestDataProcess extends Process{
	
	public TestDataProcess(){
		super();
		setLogger("kdd99.TestDataProcess");
		
		//默认weka
		setInputFilePath(Path+"/test/originTest.csv");
		setOutputFilePath(Path+"/test/kdd99_weka_test.arff");
		setOutputSplitSymbol(",");
		
		try {
			setOutFileWriter(Path+"/outInfo/weka_test.info");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<String[]> File2Arr() throws Exception{
		File InputFile = new File(getInputFilePath());
		if(!InputFile.exists()){
			throw new FileNotFoundException("Not Found Input File "+getInputSplitSymbol());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputFile),"utf-8"));
		String Line = "";
		String[] Attrs;
		ArrayList<String[]> ReadArr = new ArrayList<String[]>();
		while((Line = br.readLine())!=null){
			Attrs=SplitOneLineAndSeletAttr(Line);
			for(int i =0;i<Attrs.length;i++){
				//flag 属性没有提取，大量为0，全部取代为?
				if(i == 4){
					Attrs[i] = "SF";
				}
				if(i == 1 || i == 2 || i == 3 || i == 6 || i == 11 || i == 20 ||  i == 21){
					Attrs[i] = replaceWithMissingSymbol(Attrs[i]);
				}else{
					if(Attrs[i].equals("-1")) Attrs[i] = "0";
				}
				
				//替换 -1
				//Attrs[i] = replaceWithMissingSymbol(Attrs[i]);
			}
			ReadArr.add(Attrs);
		}
		//类别标签顺序转换
		ReadArr = convertArray(ReadArr);
		
		Logger logger = getLogger();
		logger.info("reads "+ReadArr.size()+" lines");
		
		br.close();
		return ReadArr;
	}
	
	/**
	* replaceWithMissingSymbol : 将 -1 和所有未曾出现的标签 替换成 ?(针对非数值属性)
	*  
	* @parms
	* @return String 
	* @throws
	*/
	private String replaceWithMissingSymbol(String attr){
		if(attr.equals("-1") || attr.equals("unknown") ||attr.equals("https") ||attr.equals("metbios_dgm")){
			return "?";
		}
		return attr;
	}
	
	/**
	* convertArray : 将类别标签的顺序进行转换,并将最后一个标签替换成?,因为即使是这样也要将最后的类别标签写上缺省值标志
	*  
	* @parms
	* @return ArrayList<String[]> 
	* @throws
	*/
	private ArrayList<String[]> convertArray(ArrayList<String[]> array){
		ArrayList<String[]> newArray = new ArrayList<String[]>();
		
		for(String[] line : array){
			String[] newLine = new String[42];
			//String[] newLine = new String[41];
			for(int i =0;i<41;i++){
				newLine[i] = line[i+1];
			}
			newLine[41] = "normal";
			newArray.add(newLine);
		}
		return newArray;
	}
}
