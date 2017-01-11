/** 
 * @author  LiuCong  
 * @file    ParaValueList.java 
 * @date    Date：2015年12月23日 下午9:21:05 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

import java.util.Iterator;

import cn.edu.buaa.Matlab2C.exception.SemanticsException;
import cn.edu.buaa.Matlab2C.symboltbl.FunctionType;

public class ParaValueListNode extends MatNode {

	private FunctionType functionType;

	public ParaValueListNode(int line) {
		// TODO Auto-generated constructor stub
		super(line);
	}

	@Override
	public void typeCheck() throws SemanticsException {
		// TODO Auto-generated method stub
		super.typeCheck();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		int i = 0;
		while (true) {
			EleNode eleNode = (EleNode) iterator.next();
			eleNode.typeCheck();
			if(eleNode.isSetOpe()) {
				throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "Function "
						+ functionType.getfuncName() + " paraValueList can not be <> or {}!");
			}
			if (!functionType.getSubType(i++).equals(eleNode.getType())) {
				throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "Function "
						+ functionType.getfuncName() + " para " + i
						+ " type mismatch!");
			}
			if(iterator.hasNext()) {
				iterator.next();
			} else {
				if(i!=functionType.getSubSize()) {
					throw SemanticsException.mismatchException(getRootNode().getFileName(), line, "Function "
							+ functionType.getfuncName() + " para nums mismatch!");
				}
				break;
			}
		}
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		StringBuilder paraValueList = new StringBuilder();
		Iterator<MatNode> iterator = getChildNodes().iterator();
		int i = 0;
		while(true) {
			EleNode eleNode = (EleNode)iterator.next();
			eleNode.toCCode();
			if(eleNode.getType().isPoint() && !eleNode.getType().isNilType() 
					&& eleNode.getType().getSubType(0).isVoid()) {
				paraValueList.append("(" + functionType.getSubType(i).getSubType(0).getImage() + "*)");//需要在修改（int *强制转换image为int,其他的image可能直接是指针）
			}
			paraValueList.append(cStringBuilder);
			cleanCStringBuilder();
			if(iterator.hasNext()) {
				iterator.next();
				paraValueList.append(", ");
				i++;
			} else {
				break;
			}
		}
		cStringBuilder.append(paraValueList);
	}

	public void setFunctionType(FunctionType functionType) {
		this.functionType = functionType;
	}
}
