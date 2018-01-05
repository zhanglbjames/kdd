/**
* @Title: process.java
* @Package kdd99
* @Description: TODO:
* @author zss
* @date 2016年12月6日
* @version V1.0
*/
package kdd99;

/**
* @ClassName: process
* @Description: TODO:预处理的基类
* @author zss
* @date 2016年12月6日
*
*/
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import kdd99.util.MyException;

public class Process {
	/**
	* @Fields AttrRange : TODO:属性范围
	*/
	private Logger logger;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private String AttrRange ="";
	private String[] Regexs;
	public static final String Path = "I:/kdd99_14";
	//public static final String Path = "/home/jht/kdd99";
	/**
	 * 默认值 针对weka 数据格式设计的
	 * */
	private boolean isNeedHeadInfo = true;
	private String headInfoEndStr = "END";
	private String headInfoFilePath = Path+"/headInfoFile/kdd99_weka.head";
	private String inputFilePath = Path+"/kddcup.data";
	private String inputSplitSymbol = ",";
	private String outputSplitSymbol = ",";
	private String outputFilePath = Path+"/train/kdd99_wekaComplete.arff";
	private FileWriter infoFileWriter ;
	
	/**
	* 创建一个新的实例 Process.
	*
	*/
	public Process(){
		logger = Logger.getLogger("kdd99.Process");
		try {
			setOutFileWriter(Path+"/outInfo/wekaComplete.info");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* setInfoSaveFilePath : 设置文件输出器
	*  
	* @parms path 信息输出文件路径
	* @return Process 
	* @throws
	*/
	/**
	 * 说明一定要弄清楚try catch模块的作用域有效区间的关系，还有尽量不要在设置this的值时，在设置方法内用try catch，容易
	 * 造成值没有设置成功，切记切记
	 * */
	public Process setOutFileWriter(String path) throws Exception{
		//关闭原来设置的文件流
		//默认已经打开一个文件流，新建预处理器时需要关闭默认的文件流
		if(infoFileWriter != null){
			infoFileWriter.close();
		}
		
		File outInfoFile = new File(path);
		if(!outInfoFile.exists()){
			outInfoFile.createNewFile();
		}
		this.infoFileWriter = new FileWriter(outInfoFile,true);//追加方式写入信息，便于比较
		return this;
	}
	/**
	* getInfoFileWriter : 获得文件写入器
	*  
	* @parms
	* @return FileWriter 
	* @throws
	*/
	public FileWriter getInfoFileWriter(){
		return this.infoFileWriter;
	}
	/**
	* @Title: setHeadInfo
	* @Description: TODO: 设置头文件信息
	* @param @param isNeedHeadInfo 是否需要头文件标志
	* @param @param headInfoEndStr   头文件结束的文件符号（是为了解决  头文件中有很多的空行问题）
	* @return void    返回类型
	* @throws
	*/
	public Process setHeadInfo(boolean isNeedHeadInfo,String headInfoFilePath,String headInfoEndStr){
		this.isNeedHeadInfo = isNeedHeadInfo;
		this.headInfoFilePath = headInfoFilePath;
		this.headInfoEndStr = headInfoEndStr;

		return this;
	}
	
	/**
	* setLogger : 设置日志记录器
	*  
	* @parms
	* @return void
	* @throws
	*/
	public void setLogger(String loggerName){
		this.logger = Logger.getLogger(loggerName);
	}
	/**
	* getLogger : 获得日志记录器
	*  
	* @parms
	* @return Logger 
	* @throws
	*/
	public Logger getLogger(){
		return logger;
	}
	
	/**
	* setAttrRange :设置属性范围
	*  
	* @parms
	* @return Process 返回Process的链式引用
	* @throws
	*/
	public Process setAttrRange(String range){
		this.AttrRange = range;
		return this;
	}
	/**
	* getAttrRange :获得属性范围
	*  
	* @parms
	* @return String 
	* @throws
	*/
	public String getAttrRange(){
		return AttrRange;
	}
	
	/**
	* setRegexs :设置每个属性的正则规则替换的字符串
	*  
	* @parms
	* @return Process 返回Process的链式引用
	* @throws
	*/
	public Process setRegexs(String[] regexs){
		this.Regexs = regexs;
		return this;
	}
	/**
	* getRegexs : 获得正则字符数组
	*  
	* @parms
	* @return String[] 
	* @throws
	*/
	public String[] getRegexs(){
		return Regexs;
	}
	
	/**
	* setInputFilePath : 设置输入原始文件的路径
	*  
	* @parms
	* @return Process 链式引用
	* @throws
	*/
	public Process setInputFilePath(String path){
		this.inputFilePath = path;
		return this;
	}
	/**
	* getInputFilePath : 获得输入的文件路径
	*  
	* @parms
	* @return String 
	* @throws
	*/
	public String getInputFilePath(){
		return inputFilePath;
	}
	
	/**
	* setInputSplitSymbol : 设置输入原始文件的属性之间的分隔符
	*  
	* @parms
	* @return Process 链式引用
	* @throws
	*/
	public Process setInputSplitSymbol(String symbol){
		this.inputSplitSymbol = symbol;
		return this;
	}
	/**
	* getInputSplitSymbol : 获得输入的属性分隔符
	*  
	* @parms
	* @return String 
	* @throws
	*/
	public String getInputSplitSymbol(){
		return inputSplitSymbol;
	}
	/**
	* setOutputFilePath : 设置输出的标准格式文件的路径
	*  
	* @parms
	* @return Process 链式引用 
	* @throws
	*/
	public Process setOutputFilePath(String path){
		this.outputFilePath = path;
		return this;
	}
	
	/**
	* getOutputFilePath : 获得输出的文件路径
	*  
	* @parms
	* @return String 
	* @throws
	*/
	public String getOutputFilePath(){
		return outputFilePath;
	}
	/**
	* serOutputSplitSymbol : 设置输出标准文件的属性分隔符
	*  
	* @parms
	* @return Process 链式引用
	* @throws
	*/
	public Process setOutputSplitSymbol(String symbol){
		this.outputSplitSymbol = symbol;
		return this;
	}
	/**
	* getOutputSplitSymbol : 获得输出为属性的分隔符
	*  
	* @parms
	* @return String 
	* @throws
	*/
	public String getOutputSplitSymbol(){
		return outputSplitSymbol;
	}
	/**
	* @Title: File2Arr
	* @Description: TODO:将文件转换为数组列表
	* @param @param InputFile 输入文件
	* @param @return 数组列表
	* @param @throws Exception    参数
	* @return ArrayList    返回类型
	* @throws
	*/
	public ArrayList<String[]> File2Arr() throws Exception{
		File InputFile = new File(inputFilePath);
		if(!InputFile.exists()){
			throw new FileNotFoundException("Not Found Input File "+inputSplitSymbol);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputFile),"utf-8"));
		String Line = "";
		String[] Attrs;
		ArrayList<String[]> ReadArr = new ArrayList<String[]>();
		while((Line = br.readLine())!=null){
			Attrs=SplitOneLineAndSeletAttr(Line);
			for(int i =0;i<Attrs.length;i++){
				Attrs[i] = HandleOneAttr(Attrs[i],i);
			}
			ReadArr.add(Attrs);
		}
		
		logger.info("reads "+ReadArr.size()+" lines");
		
		br.close();
		return ReadArr;
		
	}
	
	/**
	* @Title: Arr2File
	* @Description: TODO:将数组列表写入到文件中
	* @param @param InputArr 输入的数组列表
	* @param @throws Exception    参数
	* @return File    返回类型
	* @throws
	*/
	public File Arr2File(ArrayList<String[]> InputArr) throws Exception{
		File OutFile = new File(outputFilePath);
		if(!OutFile.exists()){
			OutFile.createNewFile();
		
			logger.info("Create New File: <"+outputFilePath+">");
		}
		else{
			OutFile.delete();
			OutFile.createNewFile();
			
			logger.info("Cover already exists File: <"+outputFilePath+">");
		}
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(OutFile),"utf-8"));
		if(isNeedHeadInfo){
			addHeadInfo(bw);
		}
		for(int i =0;i<InputArr.size();i++){
			String[] Attrs =InputArr.get(i);
			String NewLine ="";
			for(int j=0;j<Attrs.length;j++){
				NewLine+=Attrs[j];
				if(j==Attrs.length-1){
					break;
				}
				NewLine+=outputSplitSymbol;
			}
			bw.write(NewLine+"\n");
		}
		bw.close();
		
		logger.info("writes "+InputArr.size()+" lines");
		
		return OutFile;
	}
	
	/**
	* @Title: SplitOneLineAndSeletAttr
	* @Description: TODO: 将一行拆分成多个属性，并对属性进行选择
	* @param @param InputLine 输入的一行
	* @param @return 属性选择后的数组
	* @param @throws MyException    参数
	* @return String[]    返回类型
	* @throws
	*/
	public String[] SplitOneLineAndSeletAttr(String InputLine) throws MyException{
		String[] OneLineArr = InputLine.split(inputSplitSymbol);
		if(AttrRange.isEmpty()) return OneLineArr;
		else{
			String[] index = AttrRange.split(" ");
			if(index.length!=2){
				System.out.println("Usage: this function need two param splited with a black space");
				throw new MyException("无效的效属性范围");
			}else{
				return Arrays.copyOfRange(OneLineArr, Integer.parseInt(index[0]), Integer.parseInt(index[1]));
			}
			
		}
	}
	/**
	* @Title: HandleOneAttr
	* @Description: TODO: 对每一个属性进行处理
	* @param @param InputAttr 输入的属性                     
	* @param @param Regex 正则字符串
	* @param @return   正则后的属性值
	* @return String    返回类型
	* @throws
	*/
	public String HandleOneAttr(String InputAttr,int Index){
		
		//对最后标签属性进行合并标签
		if(Index == 41){
			String reducedStr = "";
			switch(InputAttr){
			//dos 6
			case "back.":reducedStr =  "dos";break;
			case "land.":reducedStr =  "dos";break;
			case "neptune.":reducedStr =  "dos";break;
			case "pod.":reducedStr =  "dos";break;
			case "smurf.":reducedStr =  "dos";break;
			case "teardrop.":reducedStr =  "dos";break;
			//u2r 4
			case "buffer_overflow.":reducedStr =  "u2r";break;
			case "loadmodule.":reducedStr =  "u2r";break;
			case "perl.":reducedStr =  "u2r";break;
			case "rootkit.":reducedStr =  "u2r";break;
			//r2l 8
			case "ftp_write.":reducedStr =  "r2l";break;
			case "guess_passwd.":reducedStr =  "r2l";break;
			case "imap.":reducedStr =  "r2l";break;
			case "multihop.":reducedStr =  "r2l";break;
			case "phf.":reducedStr =  "r2l";break;
			case "spy.":reducedStr =  "r2l";break;
			case "warezclient.":reducedStr =  "r2l";break;
			case "warezmaster.":reducedStr =  "r2l";break;
			//probe 4
			case "ipsweep.":reducedStr =  "probe";break;
			case "nmap.":reducedStr =  "probe";break;
			case "portsweep.":reducedStr =  "probe";	break;
			case "satan.":reducedStr =  "probe";break;
			//normal
			case "normal.":reducedStr =  "normal";break;
			}
			return reducedStr;
			
		}else{
			return InputAttr;
		}
		
	}
	/**
	* @Title: addHeadInfo
	* @Description: TODO:添加头文件到输出的文件中
	* @param @param OutFileBr 输出的Bw对象
	* @param @throws Exception    参数
	* @return void    返回类型
	* @throws
	*/
	public void addHeadInfo(BufferedWriter OutFileBw) throws Exception{
		File headInfoFile = new File(headInfoFilePath);
		if(!headInfoFile.exists()){
			throw new FileNotFoundException("Not Found Head Info File "+headInfoFile);
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(headInfoFile),"utf-8"));
		String newLine = "";
		while(!(newLine=br.readLine()).equals(headInfoEndStr)){
			OutFileBw.write(newLine+"\n");
		}
		br.close();
		logger.info("Add Head Info File: <"+headInfoFilePath+">");
	}
	
	/**
	* execute : 执行预处理
	* @return File 预处理后的标准格式文件
	* @throws
	*/
	public File execute() throws Exception{
		//记录开始时间
		long start = System.currentTimeMillis();
		infoFileWriter.write(SDF.format(start)+"\n");
		infoFileWriter.write(toString());
		logger.info("start to execute");
		
		logger.info("loading origin data.");
		ArrayList<String[]> arrayList = File2Arr();
		logger.info("loading origin data successfully.");
		
		logger.info("writting array to output file.");
		File outputFile = Arr2File(arrayList);
		logger.info("write array to output file successfully.");
		logger.info("预处理耗时："+(System.currentTimeMillis()-start)/1000.0 +" s");
		infoFileWriter.write("预处理耗时："+(System.currentTimeMillis()-start)/1000.0 +" s\n");
		return outputFile;
	}
	public String toString(){
		return "Process+\n";
	}
}
