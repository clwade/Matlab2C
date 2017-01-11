/** 
 * @author  LiuCong  
 * @file    UCharLitNode.java 
 * @date    Date：2016年1月25日 下午8:01:45 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class TrueLitNode extends MatNode {

	private Type type = Type.newPropositionType();
	
	public TrueLitNode(int line) {
		super(line);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		cStringBuilder.append("true");
	}

	public Type getType() {
		return type;
	}
}
