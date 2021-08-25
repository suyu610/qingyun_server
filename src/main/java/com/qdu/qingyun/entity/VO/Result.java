package com.qdu.qingyun.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Result
 * @Description 同意返回值
 * @Author uuorb
 * @Date 2021/5/18 12:54 下午
 * @Version 0.1
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
  int code;
  String msg;
  T data;

  public static<T> Result ok(T data){
    return new Result(200,"成功",data);
  }
  public static<T> Result ok(){
    return new Result(200,"成功",null);
  }
  public static<T> Result error(String msg){
    return new Result(600,msg,null);
  }

}
