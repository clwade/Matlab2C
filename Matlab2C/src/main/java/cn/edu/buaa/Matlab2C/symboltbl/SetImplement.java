/** 
 * @author  LiuCong  
 * @file    test.java
 * @date    Date：2015年12月20日 下午2:55:03 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.symboltbl;

public enum SetImplement {
	List, Array,Tree;

	public static SetImplement fromString(String setI) {
		switch (setI) {
		case "List":
			return List;
		case "Array":
			return Array;
		default:
			return null;
		}
	}
}
