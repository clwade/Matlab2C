/** 
 * @author  LiuCong  
 * @file    IdNode.java 
 * @date    Date：2015年12月23日 下午9:32:45 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.grammar;

public class IdNode extends MatNode {
	private String id;
	
	public IdNode(String id, int line){
		super(line);
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}

	@Override
	public void toCCode() {
		// TODO Auto-generated method stub
		super.toCCode();
		cStringBuilder.append(id);
	}

}
