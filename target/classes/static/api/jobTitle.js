function getJobTitleList(data) {
    return $axios({
        'url': '/jobTitle/list',
        'method': 'post',
        data
    })
}

function addJobTitle(data) {
    return $axios({
        'url': '/jobTitle/add',
        'method': 'post',
        data
    })
}


function deleteJobTitle(id) {
    return $axios({
        'url': '/jobTitle/delete/'+id,
        'method': 'delete',
    })
}

function queryJobTitleById(id) {
    return $axios({
        'url': '/jobTitle/query/'+id,
        'method': 'get',
    })
}

function permission2String(selectedPermission) {
    ret = '';
    for (let i = 0; i <selectedPermission.length; i++) {
        ret = ret + selectedPermission[i][0] + selectedPermission[i][1];
        if(i !==selectedPermission.length-1) ret=ret+',';
    }
    return ret;
}

function String2Permission(s) {
    permissions = [];
    let tmp = s.split(",");
    console.log(tmp);
    for(let i=0;i<tmp.length;i++){
        console.log(tmp[i]);
        let t = tmp[i].split("_");
        t[0] = t[0]+'_';
        permissions.push(t);
    }
    return permissions;
}





