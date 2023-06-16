function getDepartmentList(data) {
    return $axios({
        'url': '/department/list',
        'method': 'post',
        data
    })
}

function addDepartment(data) {
    return $axios({
        'url': '/department/add',
        'method': 'post',
        data
    })
}

function updateDepartment(data) {
    return $axios({
        'url': '/department/update',
        'method': 'put',
        data
    })
}

function deleteDepartment(id) {
    return $axios({
        'url': '/department/delete/'+id,
        'method': 'delete',
    })
}

function queryDepartmentById(id) {
    return $axios({
        'url': '/department/query/'+id,
        'method': 'get',
    })
}