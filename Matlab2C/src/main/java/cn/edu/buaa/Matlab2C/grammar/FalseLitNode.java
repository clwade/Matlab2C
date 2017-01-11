/** 
 * @author  LiuCong  
 * @file    FalseLitNode.java 
 * @date    Date：2016年1月25日 下午8:03:27 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class FalseLitNode extends MatNode {

	private Type type = Type.newPropositionType();
	
	public FalseLitNode(int line) {
		super(line);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		cStringBuilder.append("false");
	}

	public Type getType() {
		return this.type;
	}
}
