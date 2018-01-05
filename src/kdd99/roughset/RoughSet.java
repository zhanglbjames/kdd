/**
* kdd99.roughset
*  
* @author zhanglbjames@163.com
* @date 2017年1月4日
* 
*/
package kdd99.roughset;

/**
*  RoughSet
* 
* @author zhanglbjames@163.com
* @date 2017年1月4日
*
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import kdd99.Process;

import rseslib.structure.data.DoubleData;
import rseslib.structure.rule.Rule;
import rseslib.structure.table.ArrayListDoubleDataTable;
import rseslib.processing.classification.ClassifierSet;
import rseslib.structure.table.DoubleDataTable;
import rseslib.system.Report;
import rseslib.system.output.StandardDebugOutput;
import rseslib.system.output.StandardErrorOutput;
import rseslib.system.output.StandardOutput;
import rseslib.system.progress.EmptyProgress;
import rseslib.system.progress.StdOutProgress;

import rseslib.structure.attribute.NominalAttribute;

import rseslib.processing.classification.rules.roughset.RoughSetRuleClassifier;
import rseslib.processing.classification.MultipleCrossValidationTest;
import rseslib.processing.classification.MultipleTestResult;

public class RoughSet {
	
	//rules
	private Collection<Rule> DecisionRules = null;
	
	//Properties 得new初始化，才能使用其load方法，否则包nullexception
    private Properties Setting = new Properties();
    
    //预处理
    private Process roughsetProcess;
    
    //输入格式数据
    private DoubleDataTable trainTable;
    
    //分类信息输出
    FileWriter infoWriter;
    
    private String name = "Rough Set";
    
    /**
    * constructor 构造函数
    *
    */
    public RoughSet(Process process, String name) throws Exception{
    	
    	this.roughsetProcess = process;
    	//采样
    	Setting.load(new FileInputStream(Process.Path+"/Setting.properties"));
    	int num = Integer.parseInt(Setting.getProperty("ResampleRadio"));
    	DoubleDataTable table = new ArrayListDoubleDataTable(roughsetProcess.execute(), new EmptyProgress());
        ArrayList<DoubleData>[] parts = table.randomSplit(100-num, num);
        DoubleDataTable resampleTable = new ArrayListDoubleDataTable(parts[1]);
    	this.trainTable = resampleTable;
    	
    	this.infoWriter = roughsetProcess.getInfoFileWriter();
    	this.name = name;
    }
   
    /**
    * crossValidation : 10折交叉验证评估
    *  
    * @parms
    * @return void 
    * @throws
    */
    public void crossValidation() throws Exception{
    	System.out.println("开始10折交叉验证");
        Report.addErrorOutput(new StandardErrorOutput());
        Report.addInfoOutput(new StandardOutput());
        Report.addDebugOutput(new StandardDebugOutput());
        
        //构建没有一个分类器的分类器集合，然后添加roughset分类器
        ClassifierSet testers = new ClassifierSet();
        testers.addClassifier("RoughSetRuleClassifier", rseslib.processing.classification.rules.roughset.RoughSetRuleClassifier.class, Setting);
        
        //交叉验证
        MultipleCrossValidationTest crossValid = new MultipleCrossValidationTest(Setting, testers);
        
        long start = System.currentTimeMillis();
        Map<String,MultipleTestResult> results = crossValid.test(trainTable, new StdOutProgress());
        infoWriter.write("10折交叉验证耗时："+(System.currentTimeMillis()-start)/1000.0 +" s\n");
        
        for (Map.Entry<String,MultipleTestResult> entry : results.entrySet()){
        	String key = entry.getKey();
        	MultipleTestResult value = entry.getValue();
        	
        	infoWriter.write(name + "\n");
        	infoWriter.write("10折交叉验证结果信息\n");
        	infoWriter.write(key+":\n");
        	infoWriter.write(value+"\n");
        	System.out.println(value+"\n");
        }
        
        Report.close();
        //关闭分类信息文件资源
        infoWriter.close();
        System.out.println("10折交叉验证完成");
    }
    
 
    /**
    * trainForRules : 训练原始数据产生规则，以便使用规则进行分类
    *  
    * @parms
    * @return void 
    * @throws
    */
    private void trainForRules() throws Exception{
    	System.out.println("开始产生规则");
        Report.addErrorOutput(new StandardErrorOutput());
        Report.addInfoOutput(new StandardOutput());
        
       /*
        ArrayList<DoubleData>[] parts = trainTable.randomSplit(20, 1);
  
        DoubleDataTable testTable = new ArrayListDoubleDataTable(parts[1]);
        */
        
        // 构造并训练数据产生规则(rules)
        //null 表示的当前包里面的 .properties文件
        RoughSetRuleClassifier RSclassifier = new RoughSetRuleClassifier(Setting, trainTable, new StdOutProgress());
        this.DecisionRules = RSclassifier.getRules();
        
        infoWriter.write(name + "\n");
        infoWriter.write("训练原始数据产生规则，以便为后续分类做准备\n");
        
        Report.close();
        System.out.println("产生规则完成");
    }
   
    /**
    * classify : 分类未打标数据
    *  
    * @param testFile File 待分类的数据文件
    * 
    * @return ArrayList<String> 结果字符列表
    * @throws
    */
    public ArrayList<String> classify(File testFile) throws Exception{
    	
    	//训练数据
    	trainForRules();
    	
    	DoubleDataTable testTable = new ArrayListDoubleDataTable(testFile, new EmptyProgress());
    
    	ArrayList<DoubleData> testdatas = testTable.getDataObjects();
    
    	//结果
    	ArrayList<String> results = new ArrayList<String>();
        
        //利用 生成的规则 和 决策属性信息 新建分类器
    	/**
    	 * 特别注意：把测试集的类别设置成缺省值?
    	 * 用训练生成的规则和 *(训练集的决策属性信息)* 构建分类器
    	 * */
        RoughSetRuleClassifier classifier = new RoughSetRuleClassifier(DecisionRules,trainTable.attributes().nominalDecisionAttribute());
        
        //分类
        System.out.println("开始分类打标");
       
        
       for(DoubleData data : testdatas){
        	//System.out.println(data.attributes().decision());
        	double result = classifier.classify(data);//全局double型字符型数据，已经没有规则了
        	results.add(NominalAttribute.stringValue(result));//这个属性的按照顺序从0-N-1的属性值代替,标签信息不全容易报错
        	System.out.println(NominalAttribute.stringValue(result));
        }
        System.out.println("分类打标完成");
        //关闭分类信息文件资源
        infoWriter.close();
        return results;
    }
   
}
