const SERVER_URL = "http://localhost:8080";
const CONTEXT_PATH = "/RBAC-demo";

const EMP_READ = "EMPLOYEE_READ";
const EMP_UPDATE = "EMPLOYEE_UPDATE";
const EMP_INSERT = "EMPLOYEE_INSERT";
const EMP_DELETE = "EMPLOYEE_DELETE";
const JOBTITLE_READ   = "JOBTITLE_READ";
const JOBTITLE_UPDATE = "JOBTITLE_UPDATE";
const JOBTITLE_INSERT = "JOBTITLE_INSERT";
const JOBTITLE_DELETE = "JOBTITLE_DELETE";
const DEP_READ   = "DEPARTMENT_READ";
const DEP_UPDATE = "DEPARTMENT_UPDATE";
const DEP_INSERT = "DEPARTMENT_INSERT";
const DEP_DELETE = "DEPARTMENT_DELETE";





/* 自定义trim */
function trim (str) {  //删除左右两端的空格,自定义的trim()方法
    return str == undefined ? "" : str.replace(/(^\s*)|(\s*$)/g, "")
}

//获取url地址上面的参数
function requestUrlParam(argname){
    var url = location.href
    var arrStr = url.substring(url.indexOf("?")+1).split("&")
    for(var i =0;i<arrStr.length;i++)
    {
        var loc = arrStr[i].indexOf(argname+"=")
        if(loc!==-1){
            return arrStr[i].replace(argname+"=","").replace("?","")
        }
    }
    return ""
}





