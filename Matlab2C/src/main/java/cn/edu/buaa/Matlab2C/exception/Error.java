/** 
 * @author  LiuCong  
 * @file    Error.java 
 * @date    Date：2016年1月13日 下午8:58:57 
 * @version 1.0 
 */

package cn.edu.buaa.Matlab2C.exception;

public enum Error {
	REDEFINE,
	UNDEFINE,
	MISMATCH,//不匹配，包括函数的参数，集合的类型，运算的类型
	UNDEFINEDCHILD,
	PARAMETERERROR
}
