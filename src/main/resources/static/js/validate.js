 // 各种验证函数
function isValidUsername (str) {
  return ['admin', 'editor'].indexOf(str.trim()) >= 0;
}

function isExternal (path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

function isCellPhone (val) {
  if (!/^1(3|4|5|6|7|8)\d{9}$/.test(val)) {
    return false
  } else {
    return true
  }
}

 //校验密码
 function checkPassword (rule, value, callback){
   if (value === "") {
     callback(new Error("请输入密码"))
   } else if (value.length > 20 || value.length <5) {
     callback(new Error("密码长度应是5-20"))
   } else {
     callback()
   }
 }

//校验账号
function checkUserName (rule, value, callback){
  if (value === "") {
    callback(new Error("请输入账号"))
  } else if (value.length > 20 || value.length <3) {
    callback(new Error("账号长度应是3-20"))
  } else {
    callback()
  }
}

 function checkJobTitle (rule, value, callback){
   if (value === "") {
     callback(new Error("请选择职位"))
   }
   else {
     callback();
   }
 }

 function checkDepartment (rule, value, callback){
   if (value === "") {
     callback(new Error("请选择部门"))
   }
   else {
     callback()
   }
 }

//校验姓名
function checkName (rule, value, callback){
  if (value === "") {
    callback(new Error("请输入姓名"))
  } else if (value.length > 8) {
    callback(new Error("账号长度应是2-8"))
  } else {
    callback()
  }
}


 //校验姓名
 function checkStatus (rule, value, callback){
   if (value == 0 || value ==1 || value == "0" || value == "1") {
       callback()
   } else {
       callback(new Error("状态只能是0或者1"))
   }
 }

 //校验部门名称
 function checkDepName (rule, value, callback){
   if (value === "") {
     callback(new Error("请输入部门名称"))
   } else if (value.length > 8) {
     callback(new Error("部门名称长度应是2-8"))
   } else {
     callback()
   }
 }

 //校验
 function checkRank (rule, setRank,rawRank, callback){
  if (isNaN(setRank)){
    callback(new Error("优先级只能是数字！"))
  }
   if (setRank < rawRank){
     callback(new Error("优先级不得高于你自己！"))
   }
   else {
     callback();
   }
 }


function checkPhone (rule, value, callback){
  // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
  if (value === "") {
    callback(new Error("请输入手机号"))
  } else if (!isCellPhone(value)) {//引入methods中封装的检查手机格式的方法
    callback(new Error("请输入正确的手机号!"))
  } else {
    callback()
  }
}


function validID (rule,value,callback) {
  // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
  let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  if(value === '') {
    callback(new Error('请输入身份证号码'))
  } else if (reg.test(value)) {
    callback()
  } else {
    callback(new Error('身份证号码不正确'))
  }
}