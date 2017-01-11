/** 
 * @author  LiuCong  
 * @file    CharLitNode.java 
 * @date    Date：2015年12月23日 下午9:32:10 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class CharLitNode extends MatNode {
	
	private char charValue;
	private Type type = Type.newCharType();
	
	public CharLitNode(char charValue, int line) {
		super(line);
		this.charValue = charValue;
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		cStringBuilder.append("'"+charValue+"'");
	}

	public char getCharValue() {
		return charValue;
	}
	
	public Type getType() {
		return this.type;
	}
}
