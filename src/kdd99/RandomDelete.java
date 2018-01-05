/**
* kdd99
*  
* @author zhanglbjames@163.com
* @date 2016年12月29日
* 
*/
package kdd99;

import java.util.Random;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;
import java.text.DecimalFormat;
/**
*  RandomDelete
* 
* @author zhanglbjames@163.com
* @date 2016年12月29日
*
*/
public class RandomDelete extends Process{
	
	private Random random = new Random();
	private DecimalFormat df = new DecimalFormat("##0.00");
	/**
	* aItemProb 任意一条记录被删除的可能性 %
	* 默认值为20%
	*/
	private int aItemProb = 20;
	
	/**
	* maxDeleteAttrNum 一条记录中最大被删除的属性数量
	* 默认值为5
	*/
	private int maxDeleteAttrNum = 5;
	
	/**
	*  默认最小被删除属性值
	*/
	private int minDeleteAttrNum = 10;
	/**
	* roughset weka 缺省值表示为?
	*/
	private String replaceStr = "?";
	
	/**
	* constructor 无参构造函数
	* 默认值
	*/
	
	//配置初始化
	{
		init();
	}
	public void init(){
		Properties Setting = new Properties();
		try {
			Setting.load(new FileInputStream(Path+"/Setting.properties"));
			aItemProb = Integer.parseInt(Setting.getProperty("LineProb"));
			maxDeleteAttrNum = Integer.parseInt(Setting.getProperty("MaxAttrNum"));
			minDeleteAttrNum = Integer.parseInt(Setting.getProperty("MinAttrNum"));
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public RandomDelete(){
		super();//继承父类的构造函数，必须把super放在子类构造函数的第一行
		setLogger("kdd99.RandomDelete");
		
		//默认weka
		setOutputFilePath(Path+"/train/kdd99_wekaMiss.arff");
		
		try {
			setOutFileWriter(Path+"/outInfo/wekaMiss.info");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	* constructor 子类实例构造器
	*
	* @param aItemProb
	* @param maxDeleteAttrNum
	* @param replaceStr
	*/
	public RandomDelete(String replaceStr){
		super();
		setLogger("kdd99.RandomDelete");
		this.replaceStr = replaceStr;
		
		//默认weka
		setOutputFilePath(Path+"/train/kdd99_wekaMiss.arff");
				
		try {
			setOutFileWriter(Path+"/outInfo/wekaMiss.info");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	* setAItemProb :设置ItemProb
	*  
	* @parms
	* @return RandomDelete 链式引用
	* @throws
	*/
	public RandomDelete setAItemProb(int aItemProb){
		this.aItemProb = aItemProb;
		return this;
	}
    
	/**
	* setMaxDeleteAttrNum : 设置maxDeleteAttrNum
	*  
	* @parms
	* @return RandomDelete 链式引用
	* @throws
	*/
	public RandomDelete setMaxDeleteAttrNum(int num){
		this.maxDeleteAttrNum = num;
		return this;
	}
	/**
	* setReplaceStr : 设置代替的字符串
	*  
	* @parms
	* @return void 
	* @throws
	*/
	public void setReplaceStr(String str){
		this.replaceStr = str;
	}
	/**
	* getProbItem :是否修改任意一条记录
	*  
	* @parms
	* @return boolean 是否修改
	* @throws
	*/
	public boolean doModifyItem(){
		return random.nextInt(100) < aItemProb;
	}
	/**
	* getDleteNumOfAttr : 获得某一条记录删除属性的数量
	*  
	* @parms
	* @return int 删除属性的数量
	* @throws
	*/
	public int getDleteNumOfAttr(){
		//注意 next的参数里如果没有参数，则正负数值都有可能，而加参数这表示从0-num,其中num必须大于0，否则抛出异常
		//返回的待删除的属性值数量为minDeleteAttrNum ~ maxDeleteAttrNum
		if(maxDeleteAttrNum <= minDeleteAttrNum){
			return minDeleteAttrNum;
		}else{
			return minDeleteAttrNum + (random.nextInt(1000) % (maxDeleteAttrNum-minDeleteAttrNum));
		}
	}
	
	/**
	* getDeleteAttrPosition : 获得每一个待删除属性的位置索引，不包含重复位置
	*  
	* @parms
	* @return int[] 待删除的的属性索引位置数组
	* @throws
	*/
	public int[] getDeleteAttrPosition(int num){
		int[] positions = new int[num];
		//初始化索引列表
		int[] index = new int[41];
		for(int i=0;i<41;i++){
			index[i] = i;
		}
		for(int i = 0; i < num; i++){
			int k = random.nextInt(1000) % (41-i);
			positions[i] = index[k];
			
			int[] newIndex = new int[41-i-1];
			for(int j=0;j<41-i-1;j++){
				if(j<k){
					newIndex[j] = index[j];
				}
				else{
					newIndex[j] = index[j+1];
				}
			}
			index = newIndex;
		}
		
		return positions;
	}
	
	/**
	* checkIn : 检查索引index是否存在positions里
	*  
	* @parms positions 位置数组
	* @parms index 当前索引
	* @return boolean 
	* @throws
	*/
	public boolean checkIn(int[] positions, int index){
		for(int position : positions){
			if(index == position){
				return true;
			}
		}
		return false;
		
	}
	
	/* (非 Javadoc)
	* <p>Title: File2Arr</p>
	* <p>Description: </p>
	* @return
	* @throws Exception
	* @see kdd99.Process#File2Arr()
	*/
	public ArrayList<String[]> File2Arr() throws Exception{
		File InputFile = new File(getInputFilePath());
		if(!InputFile.exists()){
			throw new FileNotFoundException("Not Found Input File "+getInputFilePath());
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(InputFile),"utf-8"));
		String Line = "";
		String[] Attrs;
		ArrayList<String[]> ReadArr = new ArrayList<String[]>();
		while((Line = br.readLine())!=null){
			Attrs=SplitOneLineAndSeletAttr(Line);
			
			//随机删除属性 ，替换为空
			if(doModifyItem()){
				int[] positions = getDeleteAttrPosition(getDleteNumOfAttr());
				for(int i =0;i<Attrs.length;i++){
					if(checkIn(positions,i)){
						Attrs[i] = replaceStr; //weka 缺省值标记为?
					}
					Attrs[i] = HandleOneAttr(Attrs[i],i);
				}
			}
			else{
				for(int i =0;i<Attrs.length;i++){
					Attrs[i] = HandleOneAttr(Attrs[i],i);
				}
			}
			
			ReadArr.add(Attrs);
		}
		
		Logger logger = getLogger();
		logger.info("reads "+ReadArr.size()+" lines");
		
		br.close();
		return ReadArr;
		
	}
	/**
	* meanValue :1 ~ num 的平均值
	*  
	* @parms
	* @return float 
	* @throws
	*/
	private float meanValue(){
		float sumValue = 0L;
		for(int i = minDeleteAttrNum; i <= maxDeleteAttrNum; i++){
			sumValue += i;
		}
		System.out.println(sumValue / (maxDeleteAttrNum-minDeleteAttrNum+1));
		return sumValue / (maxDeleteAttrNum-minDeleteAttrNum+1);
		
	}
	
	/* (非 Javadoc)
	* <p>Title: toString</p>
	* <p>Description: </p>
	* @return
	* @see kdd99.Process#toString()
	*/
	public String toString(){
		return  "RandomDelete\n"+
				"构造缺失记录\n"+ 
				"每条记录被修改的概率：" + aItemProb + "%"+ "\t每条记录最大被修改的属性个数:" + maxDeleteAttrNum +"\t每条记录最小被修改的属性个数:"+ minDeleteAttrNum+"\t缺省值替代符:" + replaceStr
				+"\n"
				+"每个属性平均缺省率为："+df.format((aItemProb * meanValue() / 41.0)).toString() + "%\n";
		
	}
	public static void main(String[] args){
		RandomDelete random = new RandomDelete();
		int[] pos = random.getDeleteAttrPosition(40);
		System.out.println(Arrays.toString(pos));
	}
}
