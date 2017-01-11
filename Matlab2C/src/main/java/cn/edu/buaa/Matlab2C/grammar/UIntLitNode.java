/** 
 * @author  LiuCong  
 * @file    UIntLitNode.java 
 * @date    Date：2015年12月23日 下午9:30:30 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class UIntLitNode extends MatNode {
	int intValue;
	String hexValue;
	int isHex = 0;
	long longValue;
	int isLong = 0;
	private Type type = Type.newUIntType();

	public UIntLitNode(int intValue, int line) {
		super(line);
		this.intValue = intValue;
	}

	public UIntLitNode(String hexValue, int line) {
		super(line);
		this.isHex = 1;
		this.hexValue = hexValue;
	}

	public UIntLitNode(long longValue, int line) {
		super(line);
		this.isLong = 1;
		this.longValue = longValue;
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		if (isHex == 0) {
			cStringBuilder.append(intValue);
		} else {
			cStringBuilder.append(hexValue);
		}
	}

	public int getIntValue() {
		return intValue;
	}

	public boolean isHex() {
		return isHex == 0?false:true;
	}
	
	public String getHexValue() {
		return this.hexValue;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public boolean isLong(){
		return isLong == 0?false:true;
	}
}
