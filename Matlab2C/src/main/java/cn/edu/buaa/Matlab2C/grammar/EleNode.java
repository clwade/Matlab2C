/** 
 * @author  LiuCong  
 * @file    EleNode.java 
 * @date    Date：2015年12月23日 下午9:27:58 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.Type;

public class EleNode extends MatNode {
	
	private Type type;
	private boolean setOpe; //
	private Type leftType;//方便<> {}类型的转换
	
	public EleNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		if(getChildNodes().size() == 1) {
			AndProENode andNode = (AndProENode)getChildNodeAt(0);
			andNode.typeCheck();
			setOpe = andNode.isSetOpe();
			this.type = andNode.getType();
		} else {
			this.type = Type.newIntType();
			Iterator<MatNode> iterator = getChildNodes().iterator();
			while(true) {
				AndProENode andNode = (AndProENode)iterator.next();
				andNode.typeCheck();
				setOpe = setOpe||andNode.isSetOpe();
				if(!andNode.getType().isNumber()) {
					throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "'||' both side should be boolean type!");
				}
				if(iterator.hasNext()) {
					iterator.next();
				} else {
					break;
				}
			}
		}
	}
	
	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		StringBuilder stringBuilder = new StringBuilder();
		while(true) {
			AndProENode andProENode = (AndProENode)iterator.next();
			if(andProENode.isSetOpe()) {
				andProENode.setLeftType(this.leftType);
			}
			andProENode.toCCode();
			stringBuilder.append(cStringBuilder);
			cleanCStringBuilder();
			if(iterator.hasNext()) {
				stringBuilder.append("||");
				iterator.next();
			} else {
				break;
			}
		}
		cStringBuilder.append(stringBuilder);
	}

	public void setLeftType(Type type) {
		this.leftType = type;
	}
	
	public Type getLeftType(){
		return this.leftType;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public boolean isSetOpe() {
		return setOpe;
	}
}
